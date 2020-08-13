package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Activity to show all the details of a trip
 *
 * @author Sukriti
 * @version 1.0
 */
public class ShowTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView tripBookingsRecyclerView, tripStopsRecyclerView;
    private Button refreshButton, showRecommendationsButton;
    private TextView tripSavingsTextView;
    private ScrollView scrollView;

    private FirebaseFirestore db;

    private Trip selectedTrip;

    private UserBookingListItemAdapter userBookingListItemAdapter;
    private TripStopListItemAdapter tripStopListItemAdapter;

    private MapView mapView;
    private MapboxMap map;
    private static final String DIRECTIONS_LAYER = "DIRECTIONS_LAYER";
    private static final String LAYER_BELOW = "LAYER_BELOW";
    private static final String ROUTE_SOURCE_ID = "ROUTE_SOURCE_ID";
    LineLayer lineLayer;

    private static final String TAG = "ShowTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_show_trip);

        mapView = findViewById(R.id.tripMapView);
        scrollView = findViewById(R.id.scrollView);
        showRecommendationsButton = findViewById(R.id.showRecommendationsButton);
        tripBookingsRecyclerView = findViewById(R.id.tripBookingsRecyclerView);
        tripStopsRecyclerView = findViewById(R.id.tripStopsRecyclerView);
        refreshButton = findViewById(R.id.refreshButton);
        tripSavingsTextView = findViewById(R.id.tripSavingsTextView);

        mapView.onCreate(savedInstanceState);

        tripBookingsRecyclerView.setHasFixedSize(false);
        tripBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripStopsRecyclerView.setHasFixedSize(false);
        tripStopsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String tripId = (String) getIntent().getSerializableExtra("tripId");
        fetchTripDetails(tripId);

        // If user clicks on share ride button take them to ShowRecommendationsActivity
        showRecommendationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTripActivity.this, ShowRecommendationsActivity.class);
                intent.putExtra("selectedTrip", selectedTrip);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, CreateBookingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        ShowTripActivity.this.map = mapboxMap;
        map.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initRouteLineSourceAndLayer(style);
            }
        });
    }

    /**
     * Creates a line layer to render the route on the map
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

        // Display route on map
        showRouteOnMap();
    }

    /**
     * Fetch trip details from database
     *
     * @param tripId Id of the trip that is to be fetched from Firestore
     */
    private void fetchTripDetails(String tripId) {
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(FirestoreCollections.PUBLIC_TRIPS).document(tripId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Trip details fetched.");
                    DocumentSnapshot document = task.getResult();
                    selectedTrip = document.toObject(Trip.class);

                    // Display trip savings
                    String savings = RoundDouble.round(selectedTrip.getSavingsDistanceMeters() / 1000.0, 2) + " km";
                    String oldString = "Trip savings worth " + savings + " of fuel.";
                    String newString = oldString.replaceAll(savings, "<b><i>" + savings + "</i></b>");
                    tripSavingsTextView.setText(Html.fromHtml(newString));

                    mapView.getMapAsync(ShowTripActivity.this);

                    // Display user bookings in trip
                    userBookingListItemAdapter = new UserBookingListItemAdapter(selectedTrip.getBookings(), ShowTripActivity.this);
                    tripBookingsRecyclerView.setAdapter(userBookingListItemAdapter);

                    // Display stops in trip
                    tripStopListItemAdapter = new TripStopListItemAdapter(selectedTrip.getStops());
                    tripStopsRecyclerView.setAdapter(tripStopListItemAdapter);

                    // If trip has only a single bookings, show button to get recommendations to share ride
                    if (selectedTrip.getBookings().size() == 1)
                        showRecommendationsButton.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Trip details could not be fetched. Exception: " + task.getException());
                }
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
                    Log.i(TAG, "Displaying route.");
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                    lineLayer.setProperties(visibility(VISIBLE));
                    source.setGeoJson(LineString.fromPolyline(selectedTrip.getPolylineRoute(), selectedTrip.getPolylineStringPrecision()));
                }
            });

            // Display markers at stop locations
            IconFactory iconFactory = IconFactory.getInstance(ShowTripActivity.this);
            Icon icon = iconFactory.fromResource(R.drawable.pickup_icon);

            for (int i = 0; i < selectedTrip.getBookings().size(); i++) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(selectedTrip.getBookings().get(i).getOrigin().latitude, selectedTrip.getBookings().get(i).getOrigin().longitude))
                        .icon(icon)
                        .title(selectedTrip.getBookings().get(i).getOrigin().name));

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(selectedTrip.getBookings().get(i).getDestination().latitude, selectedTrip.getBookings().get(i).getDestination().longitude))
                        .title(selectedTrip.getBookings().get(i).getDestination().name));
            }

            // Zoom on the route on the map
            LatLngBounds latLngBounds = MapBoundingBox.getLatLngBoundsFromPolyline(selectedTrip.getPolylineRoute(), selectedTrip.getPolylineStringPrecision());
            map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 4000);
        }
    }

    /**
     * Refresh activity
     */
    public void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
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

