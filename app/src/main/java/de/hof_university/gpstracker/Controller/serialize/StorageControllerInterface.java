package de.hof_university.gpstracker.Controller.serialize;

import android.support.annotation.NonNull;

import java.io.IOException;

import de.hof_university.gpstracker.Model.track.Track;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface StorageControllerInterface {
    public void onStartService() throws IOException, ClassNotFoundException;

    public void onDestroyService() throws IOException;

    public void saveTrack(Track track) throws IOException;

    public void renameFile(@NonNull final String newName, @NonNull final String oldName) throws IOException, ClassNotFoundException;

    public Track loadTrack(@NonNull final String nameOfTrack) throws IOException, ClassNotFoundException;
}
