package de.hof_universtiy.gpstracker.Controller.serialize;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.IOException;
import java.util.List;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface StorageControllerInterface {
    public void onStartService() throws IOException, ClassNotFoundException;

    public void onDestroyService() throws IOException;

    public void saveTrack(Track track) throws IOException;

    public void renameFile(@NonNull final String newName,@NonNull final String oldName);

    public Track loadTrack(@NonNull final String nameOfTrack);
    public List<String> getListOfTracks();
}
