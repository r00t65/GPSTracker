package de.hof_universtiy.gpstracker.Controller.serialize;

import java.io.IOException;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface StorageControllerInterface {
    public void onStartService() throws IOException, ClassNotFoundException;
    public void onDestroyService() throws IOException;
}
