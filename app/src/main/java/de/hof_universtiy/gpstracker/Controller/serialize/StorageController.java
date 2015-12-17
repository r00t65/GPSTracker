package de.hof_universtiy.gpstracker.Controller.serialize;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;
import de.hof_universtiy.gpstracker.Controller.sensor.gps.GPSController;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 17.12.15.
 */
public class StorageController extends ControllerService{

    private final static String TRACKS = "track.list";
    private final static String TRACKSBIN = "tracklist.bin";
    private final Context context;
    private final List<String> listOfTracks = new ArrayList<>();

    StorageController(final Context context){
        this.context = context;
    }

    public void saveTrack(final Track track) throws IOException {
        this.updateFiles(track);
    }

    public List<String> getListOfTrackNames(){
        return  this.listOfTracks;
    }

    public Track getTrack(final String key){
        return null;
    }

    private void updateFiles(final Track track) throws IOException {
        if(track != null) {
            final FileWriter fileWriter = new FileWriter(new File(Environment.getExternalStorageDirectory().getPath() + StorageController.TRACKS));
            fileWriter.append(track.getTitle() + " " + track.getRangeTime() + " || ");
            fileWriter.close();

            final FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + StorageController.TRACKSBIN));

            final ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.listOfTracks);
            oos.flush();
            oos.close();
            fos.close();
        }
    }

    @Override
    public void onStartService(Bundle data) throws GPSController.GPSException {
        //Laden
    }

    @Override
    public void onDestroyService(Bundle data) throws GPSController.GPSException {
        //Speichern
    }
}
