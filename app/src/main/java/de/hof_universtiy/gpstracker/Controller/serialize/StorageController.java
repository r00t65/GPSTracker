package de.hof_universtiy.gpstracker.Controller.serialize;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;
import de.hof_universtiy.gpstracker.Controller.sensor.gps.GPSController;
import de.hof_universtiy.gpstracker.Controller.tracking.TrackingController;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 17.12.15.
 GPSTracker
 */
public class StorageController extends ControllerService implements TrackingController.TrackingSaveListener {

    private final static String TRACKS = "track.list";
    private final static String TRACKSBIN = "tracklist.bin";
    private final static String DIR_TRACKS = "tracks";
    public final static String KEY_SHAREDPREF_TRACK = "aktTrack";

    private final Context context;
    private final List<String> listOfTracks = new ArrayList<>();

    public StorageController(final Context context){
        this.context = context;
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
            oos.close();
            fos.close();
        }
    }

    private void loadFiles() throws IOException, ClassNotFoundException {
        final FileInputStream fis = new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath() + StorageController.TRACKSBIN));

        final ObjectInputStream ois = new ObjectInputStream(fis);
        final List<String> list = (List<String>) ois.readObject();
        ois.close();
        fis.close();

        this.listOfTracks.clear();
        this.listOfTracks.addAll(list);
    }

    private void saveTrackInFile(Track track) throws IOException {
        final FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + StorageController.DIR_TRACKS+"/"+track.getTitle()+".track"));

        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.listOfTracks);
        oos.close();
        fos.close();
    }

    @Override
    public void onStartService(Bundle data) throws GPSController.GPSException, IOException, ClassNotFoundException {
        new File(Environment.getExternalStorageDirectory().getPath() + StorageController.DIR_TRACKS).mkdir();
        this.loadFiles();
    }

    @Override
    public void onDestroyService(Bundle data) throws GPSController.GPSException, IOException {
        this.updateFiles(null);
    }

    @Override
    public void saveTrack(Track track) throws IOException {
        this.updateFiles(track);
        this.saveTrackInFile(track);
    }



}
