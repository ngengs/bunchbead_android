package com.ice.bunchbead.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ice.bunchbead.android.adapters.IngredientsAdapter;
import com.ice.bunchbead.android.data.Ingredient;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    // Firebase variable
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // View variable
    private RecyclerView mRecycler;
    private SearchView mSearchView;
    private View mIndicator;
    private DrawerLayout mDrawer;

    // Listener variable
    private IngredientsAdapter mAdapter;
    private ChildEventListener mDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        // Check user before start main
        checkUser();

        // Init layout
        setContentView(R.layout.activity_main);

        // Init View variable
        mIndicator = findViewById(R.id.itemsIndicatorGroup);
        mRecycler = findViewById(R.id.itemsRecycler);
        mDrawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView mNavigationView = findViewById(R.id.nav_view);

        // Configure toolbar and drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Configure recyclerview
        mAdapter = new IngredientsAdapter(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);

        // Init firebase database
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
        mDataListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Change single data to the list
                Ingredient data = dataSnapshot.getValue(Ingredient.class);
                if (data != null) {
                    data.setId(dataSnapshot.getKey());
                    mAdapter.change(data);
                }
                Timber.d("On Child Changed, %s", data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Remove single data from the list
                Ingredient data = dataSnapshot.getValue(Ingredient.class);
                if (data != null) {
                    data.setId(dataSnapshot.getKey());
                    mAdapter.remove(data);
                }
                Timber.d("On Child Removed, %s", data);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Timber.d("On Child Moved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("On Child Canceled");
            }
        };
        // Start listening data
        mDatabase.orderByChild("nama").addChildEventListener(mDataListener);
    }

    private void checkUser() {
        // If dont have user account run login page
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            runLogin();
        }
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
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // No need listen submit action because already listen typing
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Perform search when typing & remove search when type value is empty
                    Timber.d("Text changed to: %s", newText);
                    if (newText.equals("")) mAdapter.clearSearch();
                    else mAdapter.search(newText);
                    return true;
                }
            });
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

    private void runLogin() {
        // Open login page and close this activity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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
                runLogout();
                break;
            default:
                changed = false;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return changed;
    }

    private void runLogout() {
        FirebaseAuth.getInstance().signOut();
        runLogin();
        finish();
    }

    private void runNotification() {
        // TODO: Handle this function to open notification page
    }
}
