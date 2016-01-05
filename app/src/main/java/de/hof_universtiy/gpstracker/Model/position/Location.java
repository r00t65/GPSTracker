package de.hof_universtiy.gpstracker.Model.position;

import de.hof_universtiy.gpstracker.Model.Model;

import java.util.Date;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public final class Location extends Model {
    private final android.location.Location location;
    private final Date date;

    public Location(final android.location.Location location){
        this.location = location;
        this.date = new Date();
    }

    public Date getDate(){
        return this.date;
    }

    public android.location.Location getLocation(){
        return this.location;
    }

}
