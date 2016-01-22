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
    private final android.location.Location location;
    private final Date date;

    public Location(@NonNull final android.location.Location location) {
        this.location = location;
        this.date = new Date();
    }

    public Location(double latitude, double longitude, @NonNull final Date time) {
        this.location = new android.location.Location(Context.LOCATION_SERVICE);
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
        this.date = time;
    }

    public Date getDate() {
        return this.date;
    }

    public android.location.Location getLocation() {
        return this.location;
    }

    public String getCoordinates() {
        return ""+this.location.getLongitude()+","+this.location.getLatitude()+","+this.location.getAltitude();
    }
}
