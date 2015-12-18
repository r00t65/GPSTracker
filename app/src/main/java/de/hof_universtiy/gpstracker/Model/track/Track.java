package de.hof_universtiy.gpstracker.Model.track;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hof_universtiy.gpstracker.Model.Model;

/**
 * Created by alex on 17.12.15.
 */
public class Track extends Model{

    private final Date startDate;
    private String title = "Track";
    private final List<Location> trackPoints = new ArrayList<>();
    private Date endDate;

    public Track(){
        this.startDate = new Date();
    }

    public void addNode(android.location.Location location){
        this.trackPoints.add(new Location(location));
    }

    public String getTitle() {
        return title;
    }

    public void trackEnd(){
        this.endDate = new Date();
    }

    public String getRangeTime() {
        return this.startDate + "--" + this.endDate;
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
