package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.core.constants.Constants.PRECISION_5;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


/**
 * Activity for creating a new user booking.
 *
 * @author Sukriti
 * @version 1.0
 */
public class CreateBookingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CustomEditText originDropdownEditText, destinationDropdownEditText, datePickerEditText;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Spinner tripVisibilityPickerSpinner;
    private Button bookButton;
    private Drawable errorIcon;

    private int year, month, day;
    private Calendar pickUpTime;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private HashMap<String, POI> locations;

    private MapView mapView;
    private MapboxMap map;
    private Point originPosition;
    private Point destinationPosition;
    private LineLayer lineLayer;

    private double currentRouteDistance;
    private double currentRouteDuration;
    private String currentRoutePolyline;

    private Trip trip;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String DIRECTIONS_LAYER_ID = "DIRECTIONS_LAYER_ID";
    private static final String LAYER_BELOW_ID = "LAYER_BELOW_ID";
    private static final String TAG = "CreateBookingActivity";
    private static final int DROPDOWN_HEIGHT = 380;
    private static final String OPTIMIZATION_API_CALL_START = "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/";
    private static final String OPTIMIZATION_API_CALL_END = "?roundtrip=false&source=first&destination=last&access_token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_create_booking);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mapView = (MapView) findViewById(R.id.mapView);
        originDropdownEditText = findViewById(R.id.originDropdownEditText);
        destinationDropdownEditText = findViewById(R.id.destinationDropdownEditText);
        datePickerEditText = findViewById(R.id.datePickerEditText);
        bookButton = findViewById(R.id.bookButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        tripVisibilityPickerSpinner = findViewById(R.id.tripVisibilityPickerSpinner);

        errorIcon = getResources().getDrawable(R.drawable.error_icon);
        errorIcon.setBounds(new Rect(0, 0, 70, 70));

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                originPosition = null;
                destinationPosition = null;
                map.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        initRouteLineSourceAndLayer(style);
                    }
                });
            }
        });

        UOEMap maps = new UOEMap();

        // Maps location name to POI object
        locations = new HashMap<String, POI>();

        List<String> originLocations = new ArrayList<>();
        List<String> destinationLocations = new ArrayList<>();
        for (POI poi : maps.pointsOfInterest) {
            locations.put(poi.name, poi);
            originLocations.add(poi.name);
            destinationLocations.add(poi.name);
        }

        originDropdownEditText.setDropDownHeight(DROPDOWN_HEIGHT);
        AutoCompleteAdapter originAdapter = new AutoCompleteAdapter(this, R.layout.dropdown_item, originLocations, originDropdownEditText);
        originDropdownEditText.setAdapter(originAdapter);

        destinationDropdownEditText.setDropDownHeight(DROPDOWN_HEIGHT);
        AutoCompleteAdapter destinationAdapter = new AutoCompleteAdapter(this, R.layout.dropdown_item, destinationLocations, destinationDropdownEditText);
        destinationDropdownEditText.setAdapter(destinationAdapter);

        // Validation for origin
        originDropdownEditText.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                if (locations.containsKey(text.toString())) {
                    originDropdownEditText.setError(null);
                    originPosition = Point.fromLngLat(locations.get(text.toString()).longitude, locations.get(text.toString()).latitude);
                    getRoute(originPosition, destinationPosition);
                    return true;
                }
                originPosition = null;
                getRoute(originPosition, destinationPosition);
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                //originExposedDropdownMenu.setError("Selection required");
                originDropdownEditText.setError("", errorIcon);
                return invalidText;
            }
        });

        // Validation for destination
        destinationDropdownEditText.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                map.clear();
                if (locations.containsKey(text.toString())) {
                    destinationDropdownEditText.setError(null);
                    destinationPosition = Point.fromLngLat(locations.get(text.toString()).longitude, locations.get(text.toString()).latitude);
                    getRoute(originPosition, destinationPosition);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(locations.get(text.toString()).latitude, locations.get(text.toString()).longitude)));
                    return true;
                }
                destinationPosition = null;
                getRoute(originPosition, destinationPosition);
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                destinationDropdownEditText.setError("", errorIcon);
                return invalidText;
            }
        });

        // Perform validation of origin on focus change
        originDropdownEditText.setOnFocusChangeListener(new AutoCompleteTextView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.originDropdownEditText && !hasFocus)
                    ((AutoCompleteTextView) originDropdownEditText).performValidation();
            }
        });

        // Perform validation of destination on focus change
        destinationDropdownEditText.setOnFocusChangeListener(new AutoCompleteTextView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.destinationDropdownEditText && !hasFocus)
                    ((AutoCompleteTextView) destinationDropdownEditText).performValidation();
            }
        });

        // Perform validation when an item in the origin dropdown is selected
        originDropdownEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                originDropdownEditText.performValidation();
            }
        });

        // Perform validation when an item in the destination dropdown is selected
        destinationDropdownEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationDropdownEditText.performValidation();
            }
        });

        // Perform validation when user enters a text in origin
        originDropdownEditText.setOnEditorActionListener(
                new AutoCompleteTextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                originDropdownEditText.performValidation();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

        // Perform validation when user enters a text in destination
        destinationDropdownEditText.setOnEditorActionListener(
                new AutoCompleteTextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                destinationDropdownEditText.performValidation();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        int mHour = c.get(Calendar.HOUR_OF_DAY); // current hour
        int mMinute = c.get(Calendar.MINUTE); // current minute

        pickUpTime = Calendar.getInstance();
        pickUpTime.setTimeInMillis(0);

        // Set the default date as the current date
        // Once date has been selected in date picker dialog, open the time picker dialog
        datePickerDialog = new DatePickerDialog(CreateBookingActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);

        // Disable setting older date values in the picker
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour,
                                          int selectedMinute) {
                        pickUpTime.set(year, month, day, selectedHour, selectedMinute, 0);
                        datePickerEditText.setText(String.format("%1$td %1$tb, %1$tY %1$tH:%1$tM", pickUpTime));
                        datePickerEditText.setError(null);
                    }
                }, mHour, mMinute, false);

        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Show dropdown to select Public or Private visibility of trip
        ArrayList<String> visibilityStates = new ArrayList<>();
        visibilityStates.add("Public");
        visibilityStates.add("Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, visibilityStates);
        tripVisibilityPickerSpinner.setAdapter(adapter);

        // If user clicks on book button
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate origin and destination
                originDropdownEditText.performValidation();
                destinationDropdownEditText.performValidation();

                if (originDropdownEditText.getText().toString().equalsIgnoreCase(""))
                    originDropdownEditText.setError("", errorIcon);

                if (destinationDropdownEditText.getText().toString().equalsIgnoreCase(""))
                    destinationDropdownEditText.setError("", errorIcon);

                if (datePickerEditText.getText().toString().equalsIgnoreCase(""))
                    datePickerEditText.setError("", errorIcon);

                if (originDropdownEditText.isValid()
                        && destinationDropdownEditText.isValid()
                        && datePickerEditText.isValid()
                        && currentRoutePolyline != null
                        && !currentRoutePolyline.isEmpty()) {
                    Log.i(TAG, "Validation complete. Creating trip with booking.");

                    UserBooking booking = new UserBooking(mAuth.getUid(),
                            mAuth.getCurrentUser().getDisplayName(),
                            pickUpTime.getTimeInMillis(),
                            locations.get(originDropdownEditText.getText().toString()),
                            locations.get(destinationDropdownEditText.getText().toString()),
                            currentRouteDistance,
                            currentRouteDuration,
                            currentRoutePolyline);

                    trip = new Trip(booking, tripVisibilityPickerSpinner.getSelectedItem().toString());

                    CollectionReference dbTrips;
                    if (tripVisibilityPickerSpinner.getSelectedItem().toString().equals("Public"))
                        dbTrips = db.collection(FirestoreCollections.PUBLIC_TRIPS);
                    else
                        dbTrips = db.collection(FirestoreCollections.PRIVATE_TRIPS);

                    // Add trip to Firestore database
                    dbTrips.add(trip)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.i(TAG, "Trip added to database");
                                    Toast.makeText(CreateBookingActivity.this, "Trip booked", Toast.LENGTH_LONG).show();

                                    // Add trip to user's history
                                    DocumentReference userHistory = db.collection(FirestoreCollections.HISTORY).document(mAuth.getCurrentUser().getUid());
                                    userHistory.update("tripIdList", FieldValue.arrayUnion(documentReference.getId()));

                                    // Show trip in ShowTripActivity
                                    finish();
                                    Intent intent = new Intent(CreateBookingActivity.this, ShowTripActivity.class);
                                    intent.putExtra("tripId", documentReference.getId());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Trip could not be added to database. Exception: " + e.getMessage());
                            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 500);
                            toast.show();
                        }
                    });
                } else
                    Log.i(TAG, "Validation failed. Could not create booking");
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.createBookingNavigation);
    }

    /**
     * Creates a line layer to render the route on the map
     *
     * @param loadedMapStyle style of the Mapbox map
     */
    private void initRouteLineSourceAndLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID));
        lineLayer = new LineLayer(
                DIRECTIONS_LAYER_ID, SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(4.5f),
                lineColor(getResources().getColor(R.color.darkGrey))
        );
        loadedMapStyle.addLayerBelow(lineLayer, LAYER_BELOW_ID);
        Log.i(TAG, "Line layer added to map.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(CreateBookingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    /**
     * Handle navigation drawer item clicks
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.createBookingNavigation:
                break;
            case R.id.logoutNavigation:
                mAuth.signOut();
                finish();
                intent = new Intent(CreateBookingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.historyNavigation:
                finish();
                intent = new Intent(CreateBookingActivity.this, ShowHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Finds the best route from origin to destination and adds it to the map
     *
     * @param origin      Origin of the route
     * @param destination Destination of the route
     */
    private void getRoute(final Point origin, final Point destination) {
        if (origin != null && destination != null && map != null) {
            if (!origin.equals(destination)) {
                originDropdownEditText.setError(null);
                destinationDropdownEditText.setError(null);

                // Optimization API call
                String getOptimizedRouteAPIString = OPTIMIZATION_API_CALL_START +
                        origin.longitude() + "," + origin.latitude() + ";" +
                        destination.longitude() + "," + destination.latitude() +
                        OPTIMIZATION_API_CALL_END + Mapbox.getAccessToken();

                JsonObjectRequest optimizationAPIRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        getOptimizedRouteAPIString,
                        null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("code").equals("Ok")) // If at least one valid trip
                                    {
                                        Log.i(TAG, "Adding route to the map");
                                        JSONArray jsonArray = response.getJSONArray("trips");
                                        currentRoutePolyline = jsonArray.getJSONObject(0).getString("geometry");
                                        currentRouteDistance = jsonArray.getJSONObject(0).getDouble("distance");
                                        currentRouteDuration = jsonArray.getJSONObject(0).getDouble("duration");

                                        // Display route on map
                                        map.getStyle(new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {
                                                GeoJsonSource source = style.getSourceAs(SOURCE_ID);
                                                lineLayer.setProperties(visibility(VISIBLE));
                                                if (source != null)
                                                    source.setGeoJson(LineString.fromPolyline(currentRoutePolyline, PRECISION_5));
                                            }
                                        });

                                        // Zoom on the route on the map
                                        LatLngBounds latLngBounds = MapBoundingBox.getLatLngBoundsFromPolyline(currentRoutePolyline, PRECISION_5);
                                        map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 250), 3000);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Could not get route from API call. Exception: " + e.getMessage());
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.toString());
                            }
                        }
                );

                // Make the API request
                Volley.newRequestQueue(getApplicationContext()).add(optimizationAPIRequest);

            } else {
                originDropdownEditText.setError("", errorIcon);
                destinationDropdownEditText.setError("", errorIcon);
                lineLayer.setProperties(visibility(Property.NONE));
                currentRoutePolyline = null;
            }
        } else {
            lineLayer.setProperties(visibility(Property.NONE));
            currentRoutePolyline = null;
        }
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

