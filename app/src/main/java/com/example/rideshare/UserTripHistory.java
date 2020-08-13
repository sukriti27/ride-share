package com.example.rideshare;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

/**
 * Keeps track of all the trips of a user
 *
 * @author Sukriti
 * @version 1.0
 */
public class UserTripHistory {
    @DocumentId
    private String userId;
    private ArrayList<String> tripIdList;

    public UserTripHistory() {
        tripIdList = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getTripIdList() {
        return tripIdList;
    }

    public void setTripIdList(ArrayList<String> tripIdList) {
        this.tripIdList = tripIdList;
    }

    public void addTripId(String tripId) {
        tripIdList.add(tripId);
    }
}
