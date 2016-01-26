package de.hof_university.gpstracker.Model.track;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.position.SensorData;

/**
 * Created by alex on 17.12.15.
 */
public final class Track implements Serializable {
    private final String NAME;
    private final ArrayList<Location> trackList = new ArrayList<>();
    private final ArrayList<SensorData> sensorList = new ArrayList<>();

    private boolean trackIsFinish = false;

    public Track(@NonNull final String name) {
        this.NAME = name;
    }

    public Track(@NonNull final String newName, @NonNull final Track track) {
        this.NAME = newName;
        this.trackList.addAll(track.getTracks());
    }

    public void addNode(@NonNull final Location location) throws TrackFinishException {
        if (trackIsFinish) {
            throw new TrackFinishException();
        }
        this.trackList.add((location));
    }

    public String getName() {
        return this.NAME;
    }

    public void finishTrack() {
        trackIsFinish = true;
    }

    private void sortTrackList() {
        Collections.sort(this.trackList, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public List<Location> getTracks() {
        return this.trackList;
    }

    public void addNode(SensorData sensorData) {
        this.sensorList.add(sensorData);
    }

    public final class TrackFinishException extends Exception {

    }
}
