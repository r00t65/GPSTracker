package de.hof_universtiy.gpstracker.Controller.tracking;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface TrackingControllerInterface extends GPSChangeListener {
    /**
     * Für den Listener im MapController
     * @param gpsChangeListener
     */
    public void registerListener(@NonNull final GPSChangeListener gpsChangeListener);
    public void unregisterListener();
    public void onStartService();
    public void onDestroyService();
}
