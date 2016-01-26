package de.hof_university.gpstracker.Controller.sensor;

import android.hardware.SensorEventListener;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface SensorControllerInterface extends SensorEventListener {
    public void onStartService();

    public void onDestroyService();
}
