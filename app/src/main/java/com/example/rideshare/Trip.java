package com.example.rideshare;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A trip with several stops and one or more user bookings
 *
 * @author Sukriti
 * @version 1.0
 */
public class Trip implements Serializable {
    @DocumentId
    private String tripId;
    private String avgPickupTimeBST;
    private long avgPickupTimeEpochBST;
    private String visibility;
    private POI origin;
    private int originBookingIndex;
    private POI destination;
    private int destinationBookingIndex;
    private double savingsDistanceMeters;
    private double totalDistanceMeters;
    private double totalTimeSeconds;
    private ArrayList<UserBooking> bookings;

    // A polyline string that represents the route that covers all the stops in the trip
    private String polylineRoute;

    // Precision of the polyline string route, usually 5 or 6
    private int polylineStringPrecision;

    private ArrayList<TripStop> stops;

    // Stores the increase in savings of the trip because of the last merge
    private double increaseInSavingsByLastMerge;

    Trip() {

    }

    /**
     * Copy constructor
     *
     * @param trip the trip that is to copied
     */
    public Trip(Trip trip) {
        this.tripId = trip.tripId;
        this.avgPickupTimeBST = trip.avgPickupTimeBST;
        this.avgPickupTimeEpochBST = trip.avgPickupTimeEpochBST;
        this.visibility = trip.visibility;
        this.origin = trip.origin;
        this.destination = trip.destination;
        this.savingsDistanceMeters = trip.savingsDistanceMeters;
        this.totalDistanceMeters = trip.totalDistanceMeters;
        this.totalTimeSeconds = trip.totalTimeSeconds;
        this.bookings = trip.bookings;
        this.originBookingIndex = trip.originBookingIndex;
        this.destinationBookingIndex = trip.destinationBookingIndex;
        this.polylineRoute = trip.polylineRoute;
        this.polylineStringPrecision = trip.polylineStringPrecision;
        this.stops = trip.stops;
        this.increaseInSavingsByLastMerge = trip.increaseInSavingsByLastMerge;
    }

    /**
     * Constructor that initializes the trip with a single user booking
     *
     * @param booking    the only booking in the trip
     * @param visibility visibility of the trip, that is, public or private
     */
    Trip(UserBooking booking, String visibility) {
        avgPickupTimeBST = booking.getPickupTimeBST();
        avgPickupTimeEpochBST = booking.getPickupTimeEpochBST();
        this.visibility = visibility;
        origin = booking.getOrigin();
        destination = booking.getDestination();
        savingsDistanceMeters = 0.0;
        totalDistanceMeters = booking.getDistanceMeters();
        totalTimeSeconds = booking.getTimeSeconds();
        bookings = new ArrayList<UserBooking>();
        bookings.add(booking);
        originBookingIndex = 0;
        destinationBookingIndex = 0;
        polylineRoute = booking.getPolylineRoute();
        polylineStringPrecision = 5;
        stops = new ArrayList<TripStop>();
        stops.add(new TripStop(booking.getOrigin().name, 0.0, 0.0));
        stops.add(new TripStop(booking.getDestination().name, booking.getDistanceMeters(), booking.getTimeSeconds()));
        increaseInSavingsByLastMerge = 0.0;
    }

    public void setIncreaseInSavings(double increaseInSavings) {
        this.increaseInSavingsByLastMerge = increaseInSavings;
    }

    public double getIncreaseInSavings() {
        return increaseInSavingsByLastMerge;
    }

    public ArrayList<TripStop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<TripStop> stops) {
        this.stops = stops;
    }

    public int getPolylineStringPrecision() {
        return polylineStringPrecision;
    }

    public void setPolylineStringPrecision(int polylineStringPrecision) {
        this.polylineStringPrecision = polylineStringPrecision;
    }

    public String getPolylineRoute() {
        return polylineRoute;
    }

    public void setPolylineRoute(String polylineRoute) {
        this.polylineRoute = polylineRoute;
    }

    public int getDestinationBookingIndex() {
        return destinationBookingIndex;
    }

    public int getOriginBookingIndex() {
        return originBookingIndex;
    }

    public void setDestinationBookingIndex(int destinationBookingIndex) {
        this.destinationBookingIndex = destinationBookingIndex;
    }

    public void setOriginBookingIndex(int originBookingIndex) {
        this.originBookingIndex = originBookingIndex;
    }

    public String getTripId() {
        return tripId;
    }

    public String getAvgPickupTimeBST() {
        return avgPickupTimeBST;
    }

    public long getAvgPickupTimeEpochBST() {
        return avgPickupTimeEpochBST;
    }

    public String getVisibility() {
        return visibility;
    }

    public POI getOrigin() {
        return origin;
    }

    public POI getDestination() {
        return destination;
    }

    public double getSavingsDistanceMeters() {
        return savingsDistanceMeters;
    }

    public double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public double getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public ArrayList<UserBooking> getBookings() {
        return bookings;
    }

    public void setAvgPickupTimeBST(String avgPickupTimeBST) {
        this.avgPickupTimeBST = avgPickupTimeBST;
    }

    public void setAvgPickupTimeEpochBST(long avgPickupTimeEpochBST) {
        this.avgPickupTimeEpochBST = avgPickupTimeEpochBST;
        this.avgPickupTimeBST = String.format("%1$td %1$tb, %1$tY %1$tH:%1$tM", avgPickupTimeEpochBST);
    }

    public void setBookings(ArrayList<UserBooking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(UserBooking booking) {
        this.bookings.add(booking);
    }

    /**
     * Gives the sum of distance if the user bookings in the trip were made individually
     *
     * @return total of individual user booking distances
     */
    public double getTotalOfIndividualTripDistances() {
        double sum = 0.0;
        for (UserBooking booking : bookings) {
            sum += booking.getDistanceMeters();
        }
        return sum;
    }

    public void setDestination(POI destination) {
        this.destination = destination;
    }

    public void setOrigin(POI origin) {
        this.origin = origin;
    }

    public void setSavingsDistanceMeters(double savingsDistanceMeters) {
        this.savingsDistanceMeters = savingsDistanceMeters;
    }

    public void setTotalDistanceMeters(double totalDistanceMeters) {
        this.totalDistanceMeters = totalDistanceMeters;
    }

    public void setTotalTimeSeconds(double totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
