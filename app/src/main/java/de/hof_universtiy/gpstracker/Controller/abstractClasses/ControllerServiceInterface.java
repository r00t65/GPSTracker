package de.hof_universtiy.gpstracker.Controller.abstractClasses;

import android.os.Bundle;
import de.hof_universtiy.gpstracker.Controller.sensor.GPSController;

/**
 * Created by alex on 09.12.15.
 */
public interface ControllerServiceInterface {

    public void onStartService(Bundle data) throws GPSController.GPSException;
    public void onDestroyService(Bundle data);
}
