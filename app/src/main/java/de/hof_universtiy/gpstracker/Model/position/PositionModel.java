package de.hof_universtiy.gpstracker.Model.position;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patrick Büttner on 21.11.2015.
 */
public class PositionModel implements Serializable {
    private String id;
    private Location location;

    public PositionModel() {

    }

    public PositionModel(String id, double latitude, double longitude, Date time) {
        this.id = id;
        location = new Location(latitude, longitude, time);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
