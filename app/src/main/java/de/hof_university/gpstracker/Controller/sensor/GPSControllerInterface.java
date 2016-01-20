package de.hof_university.gpstracker.Controller.sensor;

import android.location.LocationListener;

import java.io.IOException;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface GPSControllerInterface extends LocationListener {
    public void onStartService() throws GPSController.GPSException, IOException, ClassNotFoundException;

    public void onDestroyService() throws GPSController.GPSException, IOException, ClassNotFoundException;
}
