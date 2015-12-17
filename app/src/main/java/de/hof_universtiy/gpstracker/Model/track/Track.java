package de.hof_universtiy.gpstracker.Model.track;

import de.hof_universtiy.gpstracker.Model.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 17.12.15.
 */
public class Track extends Model{

    private String title = "Track";
    private final List<Location> trackPoints = new ArrayList<>();

    public Track(){

    }

    public void addNode(android.location.Location location){
        this.trackPoints.add(new Location(location));
    }

    public final class Location extends Model{
        private final android.location.Location location;
        private final Date date;

        public Location(android.location.Location location){
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
}
