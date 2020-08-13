package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.mapbox.mapboxsdk.Mapbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity to show all the recommendations for a trip
 *
 * @author Sukriti
 * @version 1.0
 */
public class ShowRecommendationsActivity extends AppCompatActivity {

    private RecyclerView tripRecommendationsRecyclerView, tripBookingsRecyclerView;
    private ProgressBar progressBar;

    private RecommendedTripListItemAdapter recommendedTripListItemAdapter;
    private UserBookingListItemAdapter selectedTripBookingsListItemAdapter;

    private ArrayList<Trip> recommendedTrips;
    private Trip selectedTrip;

    private FirebaseFirestore db;

    private static final String TAG = "ShowRecommendationsActivity";
    private static final String OPTIMIZATION_API_CALL_START = "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/";
    private static final String OPTIMIZATION_API_CALL_END = "?roundtrip=false&source=first&destination=last&access_token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommendations);

        selectedTrip = (Trip) getIntent().getSerializableExtra("selectedTrip");

        db = FirebaseFirestore.getInstance();

        recommendedTrips = new ArrayList<>();

        tripRecommendationsRecyclerView = findViewById(R.id.tripRecommendationsRecyclerView);
        tripBookingsRecyclerView = findViewById(R.id.tripBookingsRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        tripRecommendationsRecyclerView.setHasFixedSize(false);
        tripRecommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripBookingsRecyclerView.setHasFixedSize(false);
        tripBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recommendedTripListItemAdapter = new RecommendedTripListItemAdapter(ShowRecommendationsActivity.this, recommendedTrips, selectedTrip);
        tripRecommendationsRecyclerView.setAdapter(recommendedTripListItemAdapter);

        selectedTripBookingsListItemAdapter = new UserBookingListItemAdapter(selectedTrip.getBookings(), true);
        tripBookingsRecyclerView.setAdapter(selectedTripBookingsListItemAdapter);

        getRecommendations();
    }

    /**
     * Get recommendations for the selected trip
     */
    public void getRecommendations() {
        recommendedTrips.clear();

        // Fetch trips that have pick up time 30 minutes before or after the pickup time of the selected trip
        db.collection(FirestoreCollections.PUBLIC_TRIPS)
                .whereLessThanOrEqualTo("avgPickupTimeEpochBST", selectedTrip.getAvgPickupTimeEpochBST() + 1800000)
                .whereGreaterThanOrEqualTo("avgPickupTimeEpochBST", selectedTrip.getAvgPickupTimeEpochBST() - 1800000)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Documents fetched");

                            progressBar.setVisibility(View.VISIBLE);
                            ArrayList<Trip> trips = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Trip trip = document.toObject(Trip.class);
                                if (!trip.getTripId().equals(selectedTrip.getTripId())
                                        && trip.getBookings().size() <= 2)
                                    trips.add(trip);
                            }

                            // For each trip in the time interval check if it is a good recommendation
                            for (int i = 0; i < trips.size(); i++)
                                goodRecommendationCheck(trips.get(i), i == trips.size() - 1);

                            if (trips.size() == 0)
                                progressBar.setVisibility(View.GONE);

                            recommendedTripListItemAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Could not fetch documents. Exception: ", task.getException());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * Given a trip, check the number of bookings and find whether the trip is a good recommendation
     *
     * @param trip            the trip being considered as recommendation
     * @param isLastTripCheck is the trip being considered the last trip
     */
    public void goodRecommendationCheck(final Trip trip, boolean isLastTripCheck) {
        Trip newTrip = new Trip(trip);
        newTrip.setTotalDistanceMeters(trip.getTotalOfIndividualTripDistances() + selectedTrip.getTotalOfIndividualTripDistances());

        if (trip.getBookings().size() == 1) // If the trip has only 1 booking
            twoBookingsOptimization(1, trip, newTrip, isLastTripCheck);
        else // If the trip has two bookings
            threeBookingsOptimization(1, trip, newTrip, isLastTripCheck);
    }

    /**
     * Parse input order of stops in API call and output of API call to get the order of stops in the trip
     *
     * @param stopNamesInputOrder input order of stops in the API call
     * @param trips               a trip describes a route through multiple waypoints. Contains an array of route leg objects
     * @param waypoints           input coordinate snapped to the roads network. Contains the index of the position of the given waypoint within the trip
     * @return an ordered list of stops in the trip
     */
    public ArrayList<TripStop> getStopsForRoute(ArrayList<String> stopNamesInputOrder, JSONArray trips, JSONArray waypoints) throws JSONException {
        TripStop[] stops = new TripStop[stopNamesInputOrder.size()];
        for (int i = 0; i < waypoints.length(); i++) {
            TripStop stop = new TripStop(stopNamesInputOrder.get(i), 0.0, 0.0);
            stops[waypoints.getJSONObject(i).getInt("waypoint_index")] = stop;
        }

        JSONArray legs = trips.getJSONObject(0).getJSONArray("legs");
        for (int i = 0; i < legs.length(); i++) {
            stops[i + 1].setDistanceMetersFromPreviousStop(legs.getJSONObject(i).getDouble("distance"));
            stops[i + 1].setTimeSecondsFromPreviousStop(legs.getJSONObject(i).getDouble("duration"));
        }

        return new ArrayList<>(Arrays.asList(stops));
    }

    /**
     * Find the most optimized merged route for the two bookings. If found add it to the recommended trips list
     *
     * @param optimizationAPICallNumber call number out of the 4 calls
     * @param trip                      the trip being considered as recommendation
     * @param newTrip                   the best modified trip corresponding to the merge, but without the selected trip booking
     * @param isLastTripCheck           is the trip being considered the last trip
     */
    public void twoBookingsOptimization(int optimizationAPICallNumber, final Trip trip, final Trip newTrip, final boolean isLastTripCheck) {
        switch (optimizationAPICallNumber) {
            case 1:
                String trip1OriginTrip2Destination = OPTIMIZATION_API_CALL_START +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude + ";" +
                        trip.getOrigin().longitude + "," + trip.getOrigin().latitude + ";" +
                        trip.getDestination().longitude + "," + trip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest1 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip1OriginTrip2Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 1 Trip ID " + trip.getTripId());
                                try {
                                    if (response.getString("code").equals("Ok")) // if at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");

                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));
                                        if (newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) // Compare trip1 distance + trip2 distance with result distance
                                        {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(selectedTrip.getOrigin());
                                            newTrip.setOriginBookingIndex(1);
                                            newTrip.setDestination(trip.getDestination());
                                            newTrip.setDestinationBookingIndex(0);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);
                                            stopNamesInputOrder.add(trip.getOrigin().name);
                                            stopNamesInputOrder.add(trip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getAvgPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 2);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the second call
                                twoBookingsOptimization(2, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest1);
                break;

            case 2:
                String trip1OriginTrip1Destination = OPTIMIZATION_API_CALL_START +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        trip.getOrigin().longitude + "," + trip.getOrigin().latitude + ";" +
                        trip.getDestination().longitude + "," + trip.getDestination().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest2 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip1OriginTrip1Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 2 Trip ID " + trip.getTripId());
                                try {
                                    if (response.getString("code").equals("Ok")) // if at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");

                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                        // Compare trip1 distance + trip2 distance with result distance
                                        // Check order of stops
                                        if (waypoints.getJSONObject(1).getInt("waypoint_index") < waypoints.getJSONObject(2).getInt("waypoint_index")
                                                && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(selectedTrip.getOrigin());
                                            newTrip.setDestination(selectedTrip.getDestination());
                                            newTrip.setOriginBookingIndex(1);
                                            newTrip.setDestinationBookingIndex(1);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(trip.getOrigin().name);
                                            stopNamesInputOrder.add(trip.getDestination().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getAvgPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 2);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the third call
                                twoBookingsOptimization(3, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest2);
                break;

            case 3:
                String trip2OriginTrip1Destination = OPTIMIZATION_API_CALL_START +
                        trip.getOrigin().longitude + "," + trip.getOrigin().latitude + ";" +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        trip.getDestination().longitude + "," + trip.getDestination().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest3 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip2OriginTrip1Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 3 Trip ID " + trip.getTripId());

                                try {
                                    if (response.getString("code").equals("Ok")) // if at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");

                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));
                                        if (newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(trip.getOrigin());
                                            newTrip.setDestination(selectedTrip.getDestination());
                                            newTrip.setOriginBookingIndex(0);
                                            newTrip.setDestinationBookingIndex(1);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(trip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(trip.getDestination().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getAvgPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 2);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the fourth call
                                twoBookingsOptimization(4, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest3);
                break;

            case 4:
                String trip2OriginTrip2Destination = OPTIMIZATION_API_CALL_START +
                        trip.getOrigin().longitude + "," + trip.getOrigin().latitude + ";" +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude + ";" +
                        trip.getDestination().longitude + "," + trip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();
                ;

                JsonObjectRequest optimizationAPIRequest4 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip2OriginTrip2Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 4 Trip ID " + trip.getTripId());

                                try {
                                    if (response.getString("code").equals("Ok")) // if at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");
                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                        if (waypoints.getJSONObject(1).getInt("waypoint_index") < waypoints.getJSONObject(2).getInt("waypoint_index")
                                                && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(trip.getOrigin());
                                            newTrip.setDestination(trip.getDestination());
                                            newTrip.setOriginBookingIndex(0);
                                            newTrip.setDestinationBookingIndex(0);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(trip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);
                                            stopNamesInputOrder.add(trip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getAvgPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 2);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                Log.i("Final trip savings", newTrip.getSavingsDistanceMeters() + "");
                                Log.i("Old trip savings", trip.getSavingsDistanceMeters() + "");

                                if (newTrip.getSavingsDistanceMeters() > trip.getSavingsDistanceMeters()) {
                                    Log.i(TAG, trip.getTripId() + " is a good recommendation.");
                                    recommendedTrips.add(newTrip);
                                    recommendedTripListItemAdapter.notifyDataSetChanged();
                                }

                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest4);
                break;
        }
    }

    /**
     * Find the most optimized merged route for the three bookings. If found add it to the recommended trips list
     *
     * @param optimizationAPICallNumber call number out of the 4 calls
     * @param trip                      the trip being considered as recommendation
     * @param newTrip                   the best modified trip corresponding to the merge, but without the selected trip booking
     * @param isLastTripCheck           is the trip being considered the last trip
     */
    public void threeBookingsOptimization(int optimizationAPICallNumber, final Trip trip, final Trip newTrip, final boolean isLastTripCheck) {
        switch (optimizationAPICallNumber) {
            case 1:
                final UserBooking trip2DestinationBooking = trip.getBookings().get(trip.getDestinationBookingIndex());
                int trip2OtherIndex = trip.getDestinationBookingIndex() == 0 ? 1 : 0;
                final UserBooking trip2OtherBooking = trip.getBookings().get(trip2OtherIndex);

                String trip1OriginTrip2Destination = OPTIMIZATION_API_CALL_START +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude + ";" +
                        trip2OtherBooking.getOrigin().longitude + "," + trip2OtherBooking.getOrigin().latitude + ";" +
                        trip2OtherBooking.getDestination().longitude + "," + trip2OtherBooking.getDestination().latitude + ";" +
                        trip2DestinationBooking.getOrigin().longitude + "," + trip2DestinationBooking.getOrigin().latitude + ";" +
                        trip2DestinationBooking.getDestination().longitude + "," + trip2DestinationBooking.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest1 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip1OriginTrip2Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 1 Trip ID " + trip.getTripId());
                                try {
                                    if (response.getString("code").equals("Ok")) // If at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");
                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                        if (waypoints.getJSONObject(2).getInt("waypoint_index") < waypoints.getJSONObject(3).getInt("waypoint_index") &&
                                                newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) // Compare trip1 distance + trip2 distance with result distance
                                        {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(selectedTrip.getOrigin());
                                            newTrip.setDestination(trip.getDestination());
                                            newTrip.setOriginBookingIndex(2);
                                            newTrip.setDestinationBookingIndex(trip.getDestinationBookingIndex());
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);
                                            stopNamesInputOrder.add(trip2OtherBooking.getOrigin().name);
                                            stopNamesInputOrder.add(trip2OtherBooking.getDestination().name);
                                            stopNamesInputOrder.add(trip2DestinationBooking.getOrigin().name);
                                            stopNamesInputOrder.add(trip2DestinationBooking.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getBookings().get(0).getPickupTimeEpochBST()
                                                    + trip.getBookings().get(1).getPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 3);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the second call
                                threeBookingsOptimization(2, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest1);
                break;

            case 2:
                String trip1OriginTrip1Destination = OPTIMIZATION_API_CALL_START +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        trip.getBookings().get(0).getOrigin().longitude + "," + trip.getBookings().get(0).getOrigin().latitude + ";" +
                        trip.getBookings().get(0).getDestination().longitude + "," + trip.getBookings().get(0).getDestination().latitude + ";" +
                        trip.getBookings().get(1).getOrigin().longitude + "," + trip.getBookings().get(1).getOrigin().latitude + ";" +
                        trip.getBookings().get(1).getDestination().longitude + "," + trip.getBookings().get(1).getDestination().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest2 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip1OriginTrip1Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 2 Trip ID " + trip.getTripId());
                                try {
                                    if (response.getString("code").equals("Ok")) // if at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");

                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                        // Comparing total of individual booking distances with the shared route distance
                                        // Checking order of stops to make sure that origin is visited before destination
                                        if (waypoints.getJSONObject(1).getInt("waypoint_index") < waypoints.getJSONObject(2).getInt("waypoint_index")
                                                && waypoints.getJSONObject(3).getInt("waypoint_index") < waypoints.getJSONObject(4).getInt("waypoint_index")
                                                && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(selectedTrip.getOrigin());
                                            newTrip.setDestination(selectedTrip.getDestination());
                                            newTrip.setOriginBookingIndex(2);
                                            newTrip.setDestinationBookingIndex(2);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(trip.getBookings().get(0).getOrigin().name);
                                            stopNamesInputOrder.add(trip.getBookings().get(0).getDestination().name);
                                            stopNamesInputOrder.add(trip.getBookings().get(1).getOrigin().name);
                                            stopNamesInputOrder.add(trip.getBookings().get(1).getDestination().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getBookings().get(0).getPickupTimeEpochBST()
                                                    + trip.getBookings().get(1).getPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 3);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the third call
                                threeBookingsOptimization(3, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest2);
                break;

            case 3:
                final UserBooking trip2OriginBooking = trip.getBookings().get(trip.getOriginBookingIndex());
                int otherTrip2BookingIndex = trip.getOriginBookingIndex() == 0 ? 1 : 0;
                final UserBooking otherTrip2Booking = trip.getBookings().get(otherTrip2BookingIndex);

                String trip2OriginTrip1Destination = OPTIMIZATION_API_CALL_START +
                        trip2OriginBooking.getOrigin().longitude + "," + trip2OriginBooking.getOrigin().latitude + ";" +
                        trip2OriginBooking.getDestination().longitude + "," + trip2OriginBooking.getDestination().latitude + ";" +
                        otherTrip2Booking.getOrigin().longitude + "," + otherTrip2Booking.getOrigin().latitude + ";" +
                        otherTrip2Booking.getDestination().longitude + "," + otherTrip2Booking.getDestination().latitude + ";" +
                        selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                        selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest3 = new JsonObjectRequest(
                        Request.Method.GET,
                        trip2OriginTrip1Destination,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Optimization API call 3 Trip ID " + trip.getTripId());

                                try {
                                    if (response.getString("code").equals("Ok")) // If at least one valid trip
                                    {
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        JSONArray waypoints = response.getJSONArray("waypoints");

                                        Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                        if (waypoints.getJSONObject(2).getInt("waypoint_index") < waypoints.getJSONObject(3).getInt("waypoint_index")
                                                && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                            Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                            newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                            newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                            newTrip.setOrigin(trip.getOrigin());
                                            newTrip.setDestination(selectedTrip.getDestination());
                                            newTrip.setOriginBookingIndex(trip.getOriginBookingIndex());
                                            newTrip.setDestinationBookingIndex(2);
                                            newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                            newTrip.setPolylineStringPrecision(5);
                                            newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                            Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                            ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                            stopNamesInputOrder.add(trip2OriginBooking.getOrigin().name);
                                            stopNamesInputOrder.add(trip2OriginBooking.getDestination().name);
                                            stopNamesInputOrder.add(otherTrip2Booking.getOrigin().name);
                                            stopNamesInputOrder.add(otherTrip2Booking.getDestination().name);
                                            stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                            stopNamesInputOrder.add(selectedTrip.getDestination().name);

                                            // To update the stops based on JSON output
                                            newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                            newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                            newTrip.setAvgPickupTimeEpochBST((trip.getBookings().get(0).getPickupTimeEpochBST()
                                                    + trip.getBookings().get(1).getPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 3);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                }

                                // Making the fourth call
                                threeBookingsOptimization(4, trip, newTrip, isLastTripCheck);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                                if (isLastTripCheck)
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                );

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest3);
                break;

            case 4:
                String trip2OriginTrip2Destination;
                JsonObjectRequest optimizationAPIRequest4;

                if (trip.getOriginBookingIndex() == trip.getDestinationBookingIndex()) {
                    final UserBooking trip2OriginDestinationBooking = trip.getBookings().get(trip.getOriginBookingIndex());
                    int otherBookingIndexTrip2 = trip.getOriginBookingIndex() == 0 ? 1 : 0;
                    final UserBooking otherBookingTrip2 = trip.getBookings().get(otherBookingIndexTrip2);

                    trip2OriginTrip2Destination = OPTIMIZATION_API_CALL_START +
                            trip2OriginDestinationBooking.getOrigin().longitude + "," + trip2OriginDestinationBooking.getOrigin().latitude + ";" +
                            selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                            selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude + ";" +
                            otherBookingTrip2.getOrigin().longitude + "," + otherBookingTrip2.getOrigin().latitude + ";" +
                            otherBookingTrip2.getDestination().longitude + "," + otherBookingTrip2.getDestination().latitude + ";" +
                            trip2OriginDestinationBooking.getDestination().longitude + "," + trip2OriginDestinationBooking.getDestination().latitude +
                            OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                    optimizationAPIRequest4 = new JsonObjectRequest(
                            Request.Method.GET,
                            trip2OriginTrip2Destination,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(TAG, "Optimization API call 4 Trip ID " + trip.getTripId());

                                    try {
                                        if (response.getString("code").equals("Ok")) // if at least one valid trip
                                        {
                                            JSONArray jsonArray = response.getJSONArray("trips");
                                            JSONArray waypoints = response.getJSONArray("waypoints");

                                            Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));

                                            if (waypoints.getJSONObject(1).getInt("waypoint_index") < waypoints.getJSONObject(2).getInt("waypoint_index")
                                                    && waypoints.getJSONObject(3).getInt("waypoint_index") < waypoints.getJSONObject(4).getInt("waypoint_index")
                                                    && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                                Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                                newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                                newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                                newTrip.setOrigin(trip.getOrigin());
                                                newTrip.setDestination(trip.getDestination());
                                                newTrip.setOriginBookingIndex(trip.getOriginBookingIndex());
                                                newTrip.setDestinationBookingIndex(trip.getDestinationBookingIndex());
                                                newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                                newTrip.setPolylineStringPrecision(5);
                                                newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                                Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                                ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                                stopNamesInputOrder.add(trip2OriginDestinationBooking.getOrigin().name);
                                                stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                                stopNamesInputOrder.add(selectedTrip.getDestination().name);
                                                stopNamesInputOrder.add(otherBookingTrip2.getOrigin().name);
                                                stopNamesInputOrder.add(otherBookingTrip2.getDestination().name);
                                                stopNamesInputOrder.add(trip2OriginDestinationBooking.getDestination().name);


                                                // To update the stops based on JSON output
                                                newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                                newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                                newTrip.setAvgPickupTimeEpochBST((trip.getBookings().get(0).getPickupTimeEpochBST()
                                                        + trip.getBookings().get(1).getPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 3);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                    }

                                    Log.i("Final trip savings", newTrip.getSavingsDistanceMeters() + "");
                                    Log.i("Old trip savings", trip.getSavingsDistanceMeters() + "");

                                    if (newTrip.getSavingsDistanceMeters() > trip.getSavingsDistanceMeters()) {
                                        Log.i(TAG, trip.getTripId() + " is a good recommendation.");
                                        recommendedTrips.add(newTrip);
                                        recommendedTripListItemAdapter.notifyDataSetChanged();
                                    }

                                    if (isLastTripCheck)
                                        progressBar.setVisibility(View.GONE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(TAG, error.toString());
                                    if (isLastTripCheck)
                                        progressBar.setVisibility(View.GONE);
                                }
                            }
                    );
                } else {
                    final UserBooking originBookingTrip2 = trip.getBookings().get(trip.getOriginBookingIndex());
                    final UserBooking destinationBookingTrip2 = trip.getBookings().get(trip.getDestinationBookingIndex());

                    trip2OriginTrip2Destination = OPTIMIZATION_API_CALL_START +
                            originBookingTrip2.getOrigin().longitude + "," + originBookingTrip2.getOrigin().latitude + ";" +
                            originBookingTrip2.getDestination().longitude + "," + originBookingTrip2.getDestination().latitude + ";" +
                            selectedTrip.getOrigin().longitude + "," + selectedTrip.getOrigin().latitude + ";" +
                            selectedTrip.getDestination().longitude + "," + selectedTrip.getDestination().latitude + ";" +
                            destinationBookingTrip2.getOrigin().longitude + "," + destinationBookingTrip2.getOrigin().latitude + ";" +
                            destinationBookingTrip2.getDestination().longitude + "," + destinationBookingTrip2.getDestination().latitude +
                            OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                    optimizationAPIRequest4 = new JsonObjectRequest(
                            Request.Method.GET,
                            trip2OriginTrip2Destination,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(TAG, "Optimization API call 4 Trip ID " + trip.getTripId());

                                    try {
                                        if (response.getString("code").equals("Ok")) // If at least one valid trip
                                        {
                                            JSONArray jsonArray = response.getJSONArray("trips");
                                            JSONArray waypoints = response.getJSONArray("waypoints");

                                            Log.i(TAG, "Distance: " + jsonArray.getJSONObject(0).getString("distance"));
                                            if (waypoints.getJSONObject(2).getInt("waypoint_index") < waypoints.getJSONObject(3).getInt("waypoint_index")
                                                    && newTrip.getTotalDistanceMeters() > jsonArray.getJSONObject(0).getDouble("distance")) {
                                                Log.i(TAG, "Smallest distance previously: " + Double.toString(newTrip.getTotalDistanceMeters()));
                                                newTrip.setTotalDistanceMeters(jsonArray.getJSONObject(0).getDouble("distance"));
                                                newTrip.setTotalTimeSeconds(jsonArray.getJSONObject(0).getDouble("duration"));
                                                newTrip.setOrigin(trip.getOrigin());
                                                newTrip.setDestination(trip.getDestination());
                                                newTrip.setOriginBookingIndex(trip.getOriginBookingIndex());
                                                newTrip.setDestinationBookingIndex(trip.getDestinationBookingIndex());
                                                newTrip.setPolylineRoute(jsonArray.getJSONObject(0).getString("geometry"));
                                                newTrip.setPolylineStringPrecision(5);
                                                newTrip.setSavingsDistanceMeters(selectedTrip.getTotalOfIndividualTripDistances() + trip.getTotalOfIndividualTripDistances() - newTrip.getTotalDistanceMeters());
                                                Log.i(TAG, "New trip savings: " + Double.toString(newTrip.getSavingsDistanceMeters()));

                                                ArrayList<String> stopNamesInputOrder = new ArrayList<>();
                                                stopNamesInputOrder.add(originBookingTrip2.getOrigin().name);
                                                stopNamesInputOrder.add(originBookingTrip2.getDestination().name);
                                                stopNamesInputOrder.add(selectedTrip.getOrigin().name);
                                                stopNamesInputOrder.add(selectedTrip.getDestination().name);
                                                stopNamesInputOrder.add(destinationBookingTrip2.getOrigin().name);
                                                stopNamesInputOrder.add(destinationBookingTrip2.getDestination().name);

                                                // To update the stops based on JSON output
                                                newTrip.setStops(getStopsForRoute(stopNamesInputOrder, jsonArray, waypoints));

                                                newTrip.setIncreaseInSavings(newTrip.getSavingsDistanceMeters() - trip.getSavingsDistanceMeters());
                                                newTrip.setAvgPickupTimeEpochBST((trip.getBookings().get(0).getPickupTimeEpochBST()
                                                        + trip.getBookings().get(1).getPickupTimeEpochBST() + selectedTrip.getAvgPickupTimeEpochBST()) / 3);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error parsing JSON. Exception: " + e.getMessage());
                                    }

                                    Log.i("Final trip savings", newTrip.getSavingsDistanceMeters() + "");
                                    Log.i("Old trip savings", trip.getSavingsDistanceMeters() + "");

                                    if (newTrip.getSavingsDistanceMeters() > trip.getSavingsDistanceMeters()) {
                                        Log.i(TAG, trip.getTripId() + " is a good recommendation.");
                                        recommendedTrips.add(newTrip);
                                        recommendedTripListItemAdapter.notifyDataSetChanged();
                                    }

                                    if (isLastTripCheck)
                                        progressBar.setVisibility(View.GONE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(TAG, error.toString());
                                    if (isLastTripCheck)
                                        progressBar.setVisibility(View.GONE);
                                }
                            }
                    );
                }

                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest4);
                break;
        }
    }
}

