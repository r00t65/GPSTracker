package de.hof_university.gpstracker.Model.position;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public final class Location implements Serializable {
    private final Date date;
    private final double altitude;
    private final float accuracy;
    private final double longitude;
    private final double latitude;

    public Location(@NonNull final android.location.Location location) {
        this.altitude = location.getAltitude();
        this.accuracy = location.getAccuracy();
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.date = new Date();
    }

    public Location(double latitude, double longitude, @NonNull final Date time) {
        this.date = time;
        this.altitude = 0;
        this.accuracy = 0;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Date getDate() {
        return this.date;
    }

    public android.location.Location getLocation() {
        android.location.Location location = new android.location.Location(Context.LOCATION_SERVICE);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }

    public String getCoordinates() {
        return "" + longitude + "," + altitude ;
    }
}
