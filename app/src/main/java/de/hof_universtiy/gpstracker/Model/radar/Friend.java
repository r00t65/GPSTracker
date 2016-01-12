package de.hof_universtiy.gpstracker.Model.radar;

import de.hof_universtiy.gpstracker.Model.position.Location;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public class Friend implements Serializable {
    private Location lastKnowPosition;
    private String ID;//ändern nach Bedarf

    public Friend(){

    }

    public Location getLocation() {
        return this.lastKnowPosition;
    }
}
