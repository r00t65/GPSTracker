package de.hof_universtiy.gpstracker.Controller.map;


import de.hof_universtiy.gpstracker.Controller.listener.GPSMapChangeListener;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface MapControllerInterface {
    public void showMyPosition() throws SecurityException;

    public GPSMapChangeListener getListener();
}
