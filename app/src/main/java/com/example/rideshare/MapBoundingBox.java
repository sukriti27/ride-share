package com.example.rideshare;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.util.List;

/**
 * A class with a single static method for getting LatLngBounds from a route's polyline
 *
 * @author Sukriti
 * @version 1.0
 */
public class MapBoundingBox {
    /**
     * Takes a polyline and its precision and gives the corresponding latitude and longitude bounds
     *
     * @param routePolyline          a polyline that represents the route
     * @param routePolylinePrecision precision of the polyline, usually 5 or 6
     * @return latitude and longitude bounds
     */
    public static LatLngBounds getLatLngBoundsFromPolyline(String routePolyline, int routePolylinePrecision) {
        LineString lineString = LineString.fromPolyline(routePolyline, routePolylinePrecision);
        List<Point> coordinates = lineString.coordinates();

        double minLatitude = coordinates.get(0).latitude();
        double maxLatitude = coordinates.get(0).latitude();
        double minLongitude = coordinates.get(0).longitude();
        double maxLongitude = coordinates.get(0).longitude();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).latitude() < minLatitude)
                minLatitude = coordinates.get(i).latitude();
            if (coordinates.get(i).latitude() > maxLatitude)
                maxLatitude = coordinates.get(i).latitude();
            if (coordinates.get(i).longitude() < minLongitude)
                minLongitude = coordinates.get(i).longitude();
            if (coordinates.get(i).longitude() > maxLongitude)
                maxLongitude = coordinates.get(i).longitude();
        }

        LatLng northEast = new LatLng();
        northEast.setLatitude(minLatitude);
        northEast.setLongitude(minLongitude);

        LatLng southWest = new LatLng();
        southWest.setLongitude(maxLongitude);
        southWest.setLatitude(maxLatitude);

        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(northEast)
                .include(southWest)
                .build();

        return latLngBounds;
    }
}
