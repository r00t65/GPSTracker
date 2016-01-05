package de.hof_universtiy.gpstracker.Controller.listener;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;

/**
 * TODO:Wird in TrackController verwendet und an den GPSController übergeben
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface GPSChangeListener {
    public void newPosition(@NonNull final Location location);
    public void createTrack(@NonNull final String name);
    public void newWayPoint(@NonNull final Location location);
    public void endTrack();
}
