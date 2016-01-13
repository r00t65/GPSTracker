package de.hof_universtiy.gpstracker.Model.position;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patrick Büttner on 21.11.2015.
 */
public class PositionModel implements Serializable {
    private final String id;
    private final Location location;

    public PositionModel(@NonNull final String id, double latitude, double longitude, Date time) {
        this.id = id;
        location = new Location(latitude, longitude, time);
    }

    public String getId() {
        return id;
    }

    public double getLongitude() {
        return location.getLocation().getLongitude();
    }

    /*public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }*/

    public double getLatitude() {
        return location.getLocation().getLatitude();
    }

    /*public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }*/
}
