package de.hof_university.gpstracker.Controller.listener;

import de.hof_university.gpstracker.Model.position.SensorData;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface SensorChangeListener {

    public void updateSensorData(SensorData sensorData);
}
