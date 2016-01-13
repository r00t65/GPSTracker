package de.hof_universtiy.gpstracker.Controller.listener;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.track.Track;

/**
 * TODO:soll in ConnectionController verwendet werden und an den TrackingController
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface NotificationTrackListener extends GPSListener{
    public void trackFinish(@NonNull final Track track);
}
