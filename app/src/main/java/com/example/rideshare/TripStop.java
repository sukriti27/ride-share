package com.example.rideshare;

import java.io.Serializable;

/**
 * A single stop in a trip
 *
 * @author Sukriti
 * @version 1.0
 */
public class TripStop implements Serializable {
    private String name;
    private double distanceMetersFromPreviousStop;
    private double timeSecondsFromPreviousStop;

    public TripStop() {

    }

    public TripStop(String name, double distanceMetersFromPreviousStop, double timeSecondsFromPreviousStop) {
        this.name = name;
        this.distanceMetersFromPreviousStop = distanceMetersFromPreviousStop;
        this.timeSecondsFromPreviousStop = timeSecondsFromPreviousStop;
    }

    public double getDistanceMetersFromPreviousStop() {
        return distanceMetersFromPreviousStop;
    }

    public double getTimeSecondsFromPreviousStop() {
        return timeSecondsFromPreviousStop;
    }

    public String getName() {
        return name;
    }

    public void setDistanceMetersFromPreviousStop(double distanceMetersFromPreviousStop) {
        this.distanceMetersFromPreviousStop = distanceMetersFromPreviousStop;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeSecondsFromPreviousStop(double timeSecondsFromPreviousStop) {
        this.timeSecondsFromPreviousStop = timeSecondsFromPreviousStop;
    }
}
