package de.hof_universtiy.gpstracker.Controller.tracking;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;
import de.hof_universtiy.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_universtiy.gpstracker.Controller.listener.NotificationTrackListener;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface TrackingControllerInterface extends GPSChangeListener {
    /**
     * Für den Listener im MapController
     *
     * @param gpsChangeListener
     */
    public void registerGPSListener(@NonNull final GPSMapChangeListener gpsChangeListener);

    public void unregisterGPSListener();

    public void registerServerListener(@NonNull final NotificationTrackListener listener);

    public void unregisterServerListener();

    public void onStartService();

    public void onDestroyService();
}
