package de.hof_universtiy.gpstracker.Controller.serialize;

import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.IOException;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface StorageControllerInterface {
    public void onStartService() throws IOException, ClassNotFoundException;

    public void onDestroyService() throws IOException;

    public void saveTrack(Track track) throws IOException;
}
