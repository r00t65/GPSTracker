package de.hof_universtiy.gpstracker.Controller.tracking;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;
import de.hof_universtiy.gpstracker.Controller.sensor.gps.GPSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 17.12.15.
 */
public class TrackingController extends ControllerService implements GPSController.TrackingListener {

    public final List<Location> track = new ArrayList<Location>();
    private final Context context;

    public TrackingController(Context context) {
        this.context = context;
    }

    @Override
    public void setNewPosition(Location location) {
        track.add(location);
        Toast.makeText(this.context,"add Location",Toast.LENGTH_LONG).show();
    }

    @Override
    public void trackEnd() {
        this.saveTrack();
        track.clear();
    }

    private void saveTrack() {
    }

    @Override
    public void onStartService(Bundle data) throws GPSController.GPSException {

    }

    @Override
    public void onDestroyService(Bundle data) throws GPSController.GPSException {

    }
}
