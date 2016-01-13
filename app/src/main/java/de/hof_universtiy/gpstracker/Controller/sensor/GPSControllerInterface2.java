package de.hof_universtiy.gpstracker.Controller.sensor;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by alex on 13.01.16.
 */
public interface GPSControllerInterface2 extends GPSControllerInterface {

    public void startTracking(@NonNull final String name) throws GPSController.GPSException;

    public void endTracking() throws IOException, ClassNotFoundException;
}
