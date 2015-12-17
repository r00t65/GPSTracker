package de.hof_universtiy.gpstracker.Controller.abstractClasses;

import android.os.Bundle;
import de.hof_universtiy.gpstracker.Controller.sensor.gps.GPSController;

import java.io.IOException;

/**
 * Created by alex on 09.12.15.
 */
public interface ControllerServiceInterface {

    public void onStartService(Bundle data) throws GPSController.GPSException, IOException, ClassNotFoundException;
    public void onDestroyService(Bundle data) throws GPSController.GPSException, IOException;
}
