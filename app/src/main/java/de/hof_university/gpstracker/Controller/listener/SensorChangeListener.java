package de.hof_university.gpstracker.Controller.listener;

import de.hof_university.gpstracker.Model.position.SensorData;

/**
 * Stellt eine Verbindung zwischen SensorController und dem TrackingController
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface SensorChangeListener {
    /**
     *
     * @param sensorData das neu erzeugten SensorDaten
     */
    public void updateSensorData(SensorData sensorData);
}
