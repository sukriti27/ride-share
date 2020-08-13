package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


public class ShowRecommendedTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView recommendedTripSavingIncreaseTextView;
    private RecyclerView recommendedTripUserBookingsRecyclerView, selectedTripBookingRecyclerView, recommendedTripStopsRecyclerView;
    private Button joinRecommendedTripButton;
    private ScrollView scrollView;

    private FirebaseFirestore db;

    Trip originalTrip, recommendedTrip;

    private UserBookingListItemAdapter recommendedTripBookingsListItemAdapter, selectedTripBookingListItemAdapter;
    private TripStopListItemAdapter recommendedTripStopsListItemAdapter;

    private MapView mapView;
    private MapboxMap map;
    private static final String DIRECTIONS_LAYER = "DIRECTIONS_LAYER";
    private static final String LAYER_BELOW = "LAYER_BELOW";
    private static final String ROUTE_SOURCE_ID = "ROUTE_SOURCE_ID";
    LineLayer lineLayer;

    private static final String TAG = "ShowRecommendedTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_show_recommended_trip);

        db = FirebaseFirestore.getInstance();

        originalTrip = (Trip) getIntent().getSerializableExtra("selectedTrip");
        recommendedTrip = (Trip) getIntent().getSerializableExtra("recommendedTrip");

        mapView = findViewById(R.id.recommendedTripMapView);
        scrollView = findViewById(R.id.scrollView);
        recommendedTripStopsRecyclerView = findViewById(R.id.recommendedTripStopsRecyclerView);
        selectedTripBookingRecyclerView = findViewById(R.id.selectedTripBookingRecyclerView);
        recommendedTripUserBookingsRecyclerView = findViewById(R.id.recommendedTripUserBookingsRecyclerView);
        recommendedTripSavingIncreaseTextView = findViewById(R.id.recommendedTripSavingIncreaseTextView);
        joinRecommendedTripButton = findViewById(R.id.joinRecommendedTripButton);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        recommendedTripStopsRecyclerView.setHasFixedSize(false);
        recommendedTripStopsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendedTripStopsListItemAdapter = new TripStopListItemAdapter(recommendedTrip.getStops());
        recommendedTripStopsRecyclerView.setAdapter(recommendedTripStopsListItemAdapter);


        selectedTripBookingRecyclerView.setHasFixedSize(false);
        selectedTripBookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedTripBookingListItemAdapter = new UserBookingListItemAdapter(originalTrip.getBookings(), true);
        selectedTripBookingRecyclerView.setAdapter(selectedTripBookingListItemAdapter);

        recommendedTripUserBookingsRecyclerView.setHasFixedSize(true);
        recommendedTripUserBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendedTripBookingsListItemAdapter = new UserBookingListItemAdapter(recommendedTrip.getBookings(), this);
        recommendedTripUserBookingsRecyclerView.setAdapter(recommendedTripBookingsListItemAdapter);

        String savings = RoundDouble.round(recommendedTrip.getIncreaseInSavings() / 1000.0, 2) + " km";
        String oldString = "Increase savings by " + savings + " worth of fuel.";
        String newString = oldString.replaceAll(savings, "<b><i>" + savings + "</i></b>");
        recommendedTripSavingIncreaseTextView.setText(Html.fromHtml(newString));

        // Perform transaction to merge trips on button click
        joinRecommendedTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTransaction();
            }
        });

        // Handling movements on map as it is present inside a scroll view
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return mapView.onTouchEvent(event);
            }
        });
    }

    /**
     * Creates a line layer to render the route on the map and call method to show the route
     *
     * @param loadedMapStyle style of the Mapbox map
     */
    private void initRouteLineSourceAndLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        lineLayer = new LineLayer(
                DIRECTIONS_LAYER, ROUTE_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(4.5f),
                lineColor(getResources().getColor(R.color.darkGrey))
        );
        loadedMapStyle.addLayerBelow(lineLayer, LAYER_BELOW);
        Log.i(TAG, "Line layer added to map.");

        showRouteOnMap();
    }

    /**
     * Merge the selected trip with the recommended trip through a transaction
     */
    public void performTransaction() {
        db.runTransaction(new Transaction.Function<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Log.i(TAG, "Starting transaction");
                CollectionReference publicTripsRef = db.collection(FirestoreCollections.PUBLIC_TRIPS);

                // Reading data before making updates
                DocumentReference oldTrip = publicTripsRef.document(originalTrip.getTripId());
                DocumentReference newTrip = publicTripsRef.document(recommendedTrip.getTripId());
                DocumentReference userHistory = db.collection(FirestoreCollections.HISTORY).document(originalTrip.getBookings().get(0).getUserId());

                DocumentSnapshot oldTripSnapshot = transaction.get(oldTrip);
                DocumentSnapshot newTripSnapshot = transaction.get(newTrip);

                // If the number of bookings in the trips has not changed perform updates
                if (oldTripSnapshot.exists()
                        && newTripSnapshot.exists()
                        && ((ArrayList<UserBooking>) oldTripSnapshot.get("bookings")).size() == 1
                        && ((ArrayList<UserBooking>) newTripSnapshot.get("bookings")).size() == recommendedTrip.getBookings().size()) {
                    recommendedTrip.addBooking(originalTrip.getBookings().get(0));
                    transaction.set(newTrip, recommendedTrip);
                    transaction.delete(oldTrip);

                    // Update tripIds in user history
                    userHistory.update("tripIdList", FieldValue.arrayUnion(recommendedTrip.getTripId()));
                    userHistory.update("tripIdList", FieldValue.arrayRemove(originalTrip.getTripId()));

                    Log.i(TAG, "Transaction complete");
                    return true;
                }
                return false;
            }
        }).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // If transaction was successful go to ShowTripActivity
                if (result) {
                    Toast.makeText(ShowRecommendedTripActivity.this, "You have joined the trip.", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ShowRecommendedTripActivity.this, ShowTripActivity.class);
                    intent.putExtra("tripId", recommendedTrip.getTripId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                // If the transaction was not successful, go back to ShowTripActivity of the selected trip
                else {
                    Toast.makeText(ShowRecommendedTripActivity.this, "Unable to merge the two trips as one of them has been updated.", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ShowRecommendedTripActivity.this, ShowTripActivity.class);
                    intent.putExtra("tripId", originalTrip.getTripId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        ShowRecommendedTripActivity.this.map = mapboxMap;
        map.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initRouteLineSourceAndLayer(style);
            }
        });
    }

    /**
     * Show trip route on map
     */
    public void showRouteOnMap() {
        if (map != null) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                    lineLayer.setProperties(visibility(VISIBLE));
                    Log.i(TAG, "Displaying route.");
                    source.setGeoJson(LineString.fromPolyline(recommendedTrip.getPolylineRoute(), recommendedTrip.getPolylineStringPrecision()));
                }
            });

            // Display markers at stop locations
            IconFactory iconFactory = IconFactory.getInstance(ShowRecommendedTripActivity.this);
            Icon icon = iconFactory.fromResource(R.drawable.pickup_icon);

            for (int i = 0; i < recommendedTrip.getBookings().size(); i++) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(recommendedTrip.getBookings().get(i).getOrigin().latitude, recommendedTrip.getBookings().get(i).getOrigin().longitude))
                        .icon(icon)
                        .title(recommendedTrip.getBookings().get(i).getOrigin().name));

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(recommendedTrip.getBookings().get(i).getDestination().latitude, recommendedTrip.getBookings().get(i).getDestination().longitude))
                        .title(recommendedTrip.getBookings().get(i).getDestination().name));
            }

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(originalTrip.getOrigin().latitude, originalTrip.getOrigin().longitude))
                    .icon(icon)
                    .title(originalTrip.getOrigin().name));

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(originalTrip.getDestination().latitude, originalTrip.getDestination().longitude))
                    .title(originalTrip.getDestination().name));

            // Zoom on the route on the map
            LatLngBounds latLngBounds = MapBoundingBox.getLatLngBoundsFromPolyline(recommendedTrip.getPolylineRoute(), recommendedTrip.getPolylineStringPrecision());
            map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 4000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

