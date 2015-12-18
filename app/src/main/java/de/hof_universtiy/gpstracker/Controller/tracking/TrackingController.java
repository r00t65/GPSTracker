package de.hof_universtiy.gpstracker.Controller.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;
import de.hof_universtiy.gpstracker.Controller.sensor.gps.GPSController;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import de.hof_universtiy.gpstracker.Model.track.Track;


/**
 * Created by alex on 17.12.15.
 */
public class TrackingController extends ControllerService implements GPSController.TrackingListener {

    private final TrackingController.TrackingSaveListener listener;
    public Track track ;
    private final Context context;
    private final SharedPreferences prefs;

    public TrackingController(Context context, StorageController trackingSaveListener) {
        this.track = new Track();
        this.context = context;
        this.prefs = context.getSharedPreferences(StorageController.KEY_SHAREDPREF_TRACK, 0);
        this.listener = trackingSaveListener;
    }

    @Override
    public void setNewPosition(Location location) {
        track.addNode(location);
        Toast.makeText(this.context,"add Location",Toast.LENGTH_LONG).show();
    }

    @Override
    public void trackEnd() throws IOException {
        this.saveTrack();
        this.track = new Track();
    }

    private void saveTrack() throws IOException {
        this.listener.saveTrack(this.track);
    }

    @Override
    public void onStartService(Bundle data) throws GPSController.GPSException {
    }

    @Override
    public void onDestroyService(Bundle data) throws GPSController.GPSException {

    }

    public interface TrackingSaveListener {

        public void saveTrack(Track track) throws IOException;
    }
}
