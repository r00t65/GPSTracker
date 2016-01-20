package de.hof_university.gpstracker.Controller.listener;

import android.support.annotation.NonNull;

import java.io.IOException;

import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.track.Track;

/**
 * TODO:Wird in TrackController verwendet und an den GPSController übergeben
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface GPSChangeListener {
    public void newPosition(@NonNull final Location location);

    public void createTrack(@NonNull final String name);

    public void newWayPoint(@NonNull final Location location) throws Track.TrackFinishException;

    public void trackFinish(final Track track) throws IOException, ClassNotFoundException;
}
