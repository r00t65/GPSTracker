package de.hof_universtiy.gpstracker.Controller.sensor;

import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;

import java.io.IOException;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface GPSControllerInterface extends LocationListener {
    public void onStartService() throws GPSController.GPSException, IOException, ClassNotFoundException;

    public void onDestroyService() throws GPSController.GPSException, IOException, ClassNotFoundException;
}
