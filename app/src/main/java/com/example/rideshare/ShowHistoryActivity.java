package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Activity to display all the trips of the logged in user
 *
 * @author Sukriti
 * @version 1.0
 */
public class ShowHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    RecyclerView historyTripsRecyclerView;
    private ArrayList<Trip> tripList;
    private HistoryTripListItemAdapter historyTripListItemAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "ShowHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        historyTripsRecyclerView = findViewById(R.id.historyTripsRecyclerView);

        tripList = new ArrayList<>();
        historyTripsRecyclerView.setHasFixedSize(false);
        historyTripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyTripListItemAdapter = new HistoryTripListItemAdapter(ShowHistoryActivity.this, tripList);
        historyTripsRecyclerView.setAdapter(historyTripListItemAdapter);

        // Get the tripIds of all the trips of the logged in user
        DocumentReference docRef = db.collection(FirestoreCollections.HISTORY).document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "User trips fetched from database");
                    DocumentSnapshot document = task.getResult();
                    UserTripHistory userTripHistory = document.toObject(UserTripHistory.class);

                    // If user has at least one trip, display the trip
                    if (!userTripHistory.getTripIdList().isEmpty())
                        showTrip(userTripHistory.getTripIdList(), 0);
                }
            }
        });

        // Use toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.historyNavigation);
    }

    /**
     * Closes navigation drawer if it is open and user presses back button.
     * Otherwise, takes user to CreateBookingActivity
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            finish();
            Intent intent = new Intent(ShowHistoryActivity.this, CreateBookingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * Handle navigation drawer item clicks
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.historyNavigation:
                break;
            case R.id.logoutNavigation:
                mAuth.signOut();
                finish();
                intent = new Intent(ShowHistoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.createBookingNavigation:
                finish();
                intent = new Intent(ShowHistoryActivity.this, CreateBookingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Fetches a trips document from Firestore and adds it to the trip list displayed through recycler view
     *
     * @param tripIdList A list containing all the tripIds of the trips of the logged in user
     * @param index      Index of the tripId that is to be fetched from database
     */
    private void showTrip(final ArrayList<String> tripIdList, final int index) {
        DocumentReference docRef = db.collection(FirestoreCollections.PUBLIC_TRIPS).document(tripIdList.get(index));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Trip fetched from database " + tripIdList.get(index));
                    DocumentSnapshot document = task.getResult();
                    Trip trip = document.toObject(Trip.class);

                    tripList.add(trip);

                    // If there are more trips in the list, fetch the next one
                    if (index + 1 != tripIdList.size())
                        showTrip(tripIdList, index + 1);
                        // If this is the last trip in the list, sort the list based on average pick up time of trips
                    else {
                        Log.i(TAG, "Trip list sorted");
                        Collections.sort(tripList, new Comparator<Trip>() {
                            @Override
                            public int compare(Trip trip1, Trip trip2) {
                                // To sort in descending order based on value of average pick up time of the trips
                                return Long.compare(trip2.getAvgPickupTimeEpochBST(), trip1.getAvgPickupTimeEpochBST());
                            }
                        });
                        historyTripListItemAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.i(TAG, "Failed to fetch trip with id " + tripIdList.get(index));
                    Log.d(TAG, "Exception: ", task.getException());
                }
            }
        });
    }
}

