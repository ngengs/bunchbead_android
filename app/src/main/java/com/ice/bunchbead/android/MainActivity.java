package com.ice.bunchbead.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ice.bunchbead.android.adapters.IngredientsAdapter;
import com.ice.bunchbead.android.data.Ingredient;
import com.ice.bunchbead.android.helpers.UserHelper;
import com.ice.bunchbead.android.listener.firebase.DataChildListener;
import com.ice.bunchbead.android.listener.search.SearchListener;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    // View variable
    private RecyclerView mRecycler;
    private SearchView mSearchView;
    private View mIndicator;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    // Listener variable
    private IngredientsAdapter mAdapter;
    private ChildEventListener mDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check user before start main
        UserHelper.checkLogin(mAuth, () -> UserHelper.runLogin(this));

        // Init layout
        setContentView(R.layout.activity_main);

        // Init View variable
        mIndicator = findViewById(R.id.itemsIndicatorGroup);
        mRecycler = findViewById(R.id.itemsRecycler);
        mDrawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mNavigationView = findViewById(R.id.nav_view);

        // Configure toolbar and drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Configure recyclerView
        mAdapter = new IngredientsAdapter(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);

        // Init FireVase database
        mDatabase = FirebaseDatabase.getInstance().getReference("bahan");

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listen data when start
        listenData();
        // Listen search when start
        listenSearch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove all listener when stop
        mDatabase.removeEventListener(mDataListener);
        mSearchView.setOnCloseListener(null);
        mSearchView.setOnQueryTextListener(null);
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
                    // Add single data to the list (adapter)
                    Ingredient data = dataSnapshot.getValue(Ingredient.class);
                    if (data != null) {
                        data.setId(dataSnapshot.getKey());
                        mAdapter.add(data);
                    }
                    // Invalidate menu to showing the search indicator
                    invalidateOptionsMenu();
                    // Show list and hide loading indicator after add data
                    mIndicator.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                    Timber.d("On Child Added, %s", data);
                },
                (dataSnapshot, previousChildName) -> {
                    // Change single data to the list
                    Ingredient data = dataSnapshot.getValue(Ingredient.class);
                    if (data != null) {
                        data.setId(dataSnapshot.getKey());
                        mAdapter.change(data);
                    }
                    Timber.d("On Child Changed, %s", data);
                },
                dataSnapshot -> {
                    // Remove single data from the list
                    Ingredient data = dataSnapshot.getValue(Ingredient.class);
                    if (data != null) {
                        data.setId(dataSnapshot.getKey());
                        mAdapter.remove(data);
                    }
                    Timber.d("On Child Removed, %s", data);
                },
                (dataSnapshot, previousChildName) -> Timber.d("On Child Moved"),
                databaseError -> Timber.d("On Child Canceled")
        );
        // Start listening data
        mDatabase.orderByChild("nama").addChildEventListener(mDataListener);
    }

    private void listenSearch() {
        Timber.d("listenSearch called");
        // Listen searched text  when search is ready
        if (mSearchView != null) {
            Timber.d("listenSearch started");
            // Build search view properties
            mSearchView.setQueryHint(getString(R.string.search_placeholder));
            // Listen Search View
            mSearchView.setOnCloseListener(() -> {
                // Clear search when close button clicker
                Timber.d("Text cleared");
                mAdapter.clearSearch();
                mSearchView.onActionViewCollapsed();
                return true;
            });
            mSearchView.setOnQueryTextListener(new SearchListener(
                    null, // No need listen submit action because already listen typing
                    text -> {
                        // Perform search when typing & remove search when type value is empty
                        Timber.d("Text changed to: %s", text);
                        if (text.equals("")) mAdapter.clearSearch();
                        else mAdapter.search(text);
                        return true;
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // If don't have data don't need create search menu
        if (mAdapter.getItemCountOriginal() == 0) return false;

        // Create search menu if data not empty
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_toolbar_menu, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.menu_toolbarsearch);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        // Listen search when search view is ready
        listenSearch();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close search when pressing back but search view is open
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            mAdapter.clearSearch();
        } else {
            // Default back action
            super.onBackPressed();
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Timber.d("onNavigationItemSelected() called with: item = [" + id + "]");
        boolean changed;
        switch (id) {
            case R.id.nav_home:
                changed = true;
                break;
            case R.id.nav_notification:
                changed = true;
                runNotification();
                break;
            case R.id.nav_logout:
                changed = true;
                UserHelper.runLogout(this);
                break;
            default:
                changed = false;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return changed;
    }

    private void runNotification() {
        startActivityForResult(new Intent(this, NotificationResultActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNavigationView.setCheckedItem(R.id.nav_home);
    }
}
