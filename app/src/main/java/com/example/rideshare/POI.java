package com.example.rideshare;

import java.io.Serializable;

/**
 * A point of interest i.e. a University of Edinburgh building location
 *
 * @author Sukriti
 * @version 1.0
 */
public class POI implements Serializable {

    public int id;
    public String name;
    public String stub;
    public String area;
    public String type;
    public double longitude;
    public double latitude;
    public String address1;
    public String address2;
    public String city;
    public String postcode;

    public float distanceInMetres = 0.0f;

    public POI() {
    }

    public POI(int id,
               String name,
               String stub,
               String area,
               String type,
               double longitude,
               double latitude,
               String address1,
               String address2,
               String city,
               String postcode) {

        this.id = id;
        this.name = name;
        this.stub = stub;
        this.area = area;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postcode = postcode;
    }

    public POI(int id,
               String name,
               String stub,
               String area,
               String type,
               double longitude,
               double latitude,
               String address1,
               String city,
               String postcode) {

        this.id = id;
        this.name = name;
        this.stub = stub;
        this.area = area;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address1 = address1;
        this.address2 = "";
        this.city = city;
        this.postcode = postcode;
    }

    public POI(int id,
               String name,
               String stub,
               String type,
               double longitude,
               double latitude,
               String address1,
               String address2,
               String city,
               String postcode) {

        this.id = id;
        this.name = name;
        this.stub = stub;
        this.area = "";
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postcode = postcode;
    }

    public POI(int id,
               String name,
               String stub,
               String type,
               double longitude,
               double latitude,
               String address1,
               String city,
               String postcode) {

        this.id = id;
        this.name = name;
        this.stub = stub;
        this.area = "";
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address1 = address1;
        this.address2 = "";
        this.city = city;
        this.postcode = postcode;
    }
}


