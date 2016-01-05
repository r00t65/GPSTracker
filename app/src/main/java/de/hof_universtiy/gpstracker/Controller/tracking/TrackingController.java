package de.hof_universtiy.gpstracker.Controller.tracking;

import android.content.Context;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.IOException;


/**
 * Created by alex on 17.12.15.
 */
public class TrackingController implements TrackingControllerInterface {

    public Track track;
    private final Context context;

    public TrackingController(@NonNull final Context context, StorageController trackingSaveListener) {
        this.context = context;
        //this.listener = trackingSaveListener;
    }

    private void saveTrack() throws IOException {
        //this.listener.saveTrack(this.track);
    }

    /**
     * Für den Listener im MapController
     *
     * @param gpsChangeListener
     */
    @Override
    public void registerListener(@NonNull GPSChangeListener gpsChangeListener) {

    }

    @Override
    public void unregisterListener() {

    }

    @Override
    public void onStartService() {

    }

    @Override
    public void onDestroyService() {

    }
}
