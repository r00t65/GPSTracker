package de.hof_universtiy.gpstracker.Controller.listener;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.track.Track;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public interface GPSMapChangeListener {
    public void newPosition(@NonNull final Location location);
    public void updateTrack(@NonNull final Track track);
}
