package com.ice.bunchbead.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ice.bunchbead.android.adapters.RankAdapter;
import com.ice.bunchbead.android.data.Rank;
import com.ice.bunchbead.android.helpers.RankHelper;
import com.ice.bunchbead.android.helpers.UserHelper;
import com.ice.bunchbead.android.listener.firebase.DataChildListener;

import timber.log.Timber;

public class NotificationResultActivity extends AppCompatActivity {

    // FireBase variable
    private DatabaseReference mDatabase;

    // View variable
    private RecyclerView mRecycler;
    private View mIndicator;

    // Listener variable
    private RankAdapter mAdapter;
    private ChildEventListener mDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check user before start main
        UserHelper.checkLogin(mAuth, () -> UserHelper.runLogin(this));

        // Init layout
        setContentView(R.layout.activity_notification_result);

        // Init View variable
        mIndicator = findViewById(R.id.itemsIndicatorGroup);
        mRecycler = findViewById(R.id.itemsRecycler);

        // Configure recyclerView
        mAdapter = new RankAdapter(this);
        mAdapter.setClickListener(this::runUpdateIngredient);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);

        // Init FireVase database
        mDatabase = FirebaseDatabase.getInstance().getReference("rank");

        setTitle(R.string.notification);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listen data when start
        listenData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove all listener when stop
        mDatabase.removeEventListener(mDataListener);
    }

    private void listenData() {
        Timber.d("Listen Data");
        // Clear all local data before start get data
        mAdapter.clear();
        // Show loading indicator when start get data
        mIndicator.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.INVISIBLE);
        // Create data listener
        mDataListener = new DataChildListener(
                (dataSnapshot, previousChildName) -> {
                    Rank data = RankHelper.convertDataSnapshot(dataSnapshot);

                    mAdapter.add(data);

                    // Show list and hide loading indicator after add data
                    mIndicator.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                    Timber.d("On Child Added, %s", data);
                },
                (dataSnapshot, previousChildName) -> {
                    Rank data = RankHelper.convertDataSnapshot(dataSnapshot);
                    mAdapter.change(data);
                    Timber.d("On Child Changed, %s", data);
                },
                dataSnapshot -> {
                    Rank data = RankHelper.convertDataSnapshot(dataSnapshot);
                    mAdapter.remove(data);
                    Timber.d("On Child Removed, %s", data);
                },
                (dataSnapshot, previousChildName) -> Timber.d("On Child Moved"),
                databaseError -> Timber.d("On Child Canceled")
        );
        mDatabase.orderByChild("timestamp").addChildEventListener(mDataListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void runUpdateIngredient(String id, String rankId, String key) {
        if (id == null || rankId == null) return;

        Intent intent = new Intent(this, UpdateIngredientActivity.class);
        intent.putExtra("ITEM_ID", id);
        intent.putExtra("RANK_ID", rankId);
        intent.putExtra("KEY", key);
        startActivity(intent);
    }

}
