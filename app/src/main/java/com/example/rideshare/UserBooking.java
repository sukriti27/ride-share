package com.example.rideshare;

import java.io.Serializable;

/**
 * Represents a single booking made by a user
 *
 * @author Sukriti
 * @version 1.0
 */
public class UserBooking implements Serializable {

    private String userId;
    public String userName;
    private String pickupTimeBST;
    private long pickupTimeEpochBST;
    private POI origin;
    private POI destination;
    private double distanceMeters;
    private double timeSeconds;
    private String polylineRoute;

    UserBooking(String userId, String userName, long pickupTimeEpochBST, POI origin, POI destination, double distanceMeters, double timeSeconds, String polylineRoute) {
        this.userId = userId;
        this.userName = userName;
        this.pickupTimeEpochBST = pickupTimeEpochBST;
        pickupTimeBST = String.format("%1$td %1$tb, %1$tY %1$tH:%1$tM", pickupTimeEpochBST);
        this.origin = origin;
        this.destination = destination;
        this.distanceMeters = distanceMeters;
        this.timeSeconds = timeSeconds;
        this.polylineRoute = polylineRoute;
    }

    UserBooking() {
    }

    public String getPolylineRoute() {
        return polylineRoute;
    }

    public void setPolylineRoute(String polylineRoute) {
        this.polylineRoute = polylineRoute;
    }

    public String getUserId() {
        return userId;
    }

    public String getPickupTimeBST() {
        return pickupTimeBST;
    }

    public long getPickupTimeEpochBST() {
        return pickupTimeEpochBST;
    }

    public POI getOrigin() {
        return origin;
    }

    public POI getDestination() {
        return destination;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    public String getUserName() {
        return userName;
    }
}

