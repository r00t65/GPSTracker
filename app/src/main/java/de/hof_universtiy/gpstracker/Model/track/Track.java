package de.hof_universtiy.gpstracker.Model.track;

import java.util.*;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.Model;
import de.hof_universtiy.gpstracker.Model.position.Location;

/**
 * Created by alex on 17.12.15.
 */
public final class Track extends Model {

    private final String NAME;
    private final List<Location> trackList = new ArrayList<>();

    private boolean trackIsFinish = false;

    public Track(@NonNull final String name){
        this.NAME = name;
    }

    public void addNode(@NonNull final Location location) throws TrackFinishException {
        if(trackIsFinish){
            throw new TrackFinishException();
        }
        this.trackList.add((location));
    }

    public String getName() {
        return this.NAME;
    }

    public void finishTrack(){
        trackIsFinish = true;
    }

    private void sortTrackList(){
        Collections.sort(this.trackList, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public final class TrackFinishException extends Exception{

    }
}
