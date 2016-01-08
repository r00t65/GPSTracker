package de.hof_universtiy.gpstracker.Controller.serialize;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 17.12.15.
 GPSTracker
 */
public class StorageController implements StorageControllerInterface{

    private final static String TRACKS = "track.txt";
    private final static String TRACKSBIN = "tracklist.bin";
    private final static String DIR_TRACKS = "tracks";
    public final static String KEY_SHAREDPREF_TRACK = "aktTrack";

    private final Context context;
    private final List<String> listOfTracks = new ArrayList<>();

    public StorageController(@NonNull final Context context){
        this.context = context;
    }

    public List<String> getListOfTrackNames(){
        return  this.listOfTracks;
    }

    public Track getTrack(@NonNull final String key) throws IOException, ClassNotFoundException {
        Track track = null;
        final File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS+"/"+key+".track");
        final FileInputStream fis = new FileInputStream(file);
        final ObjectInputStream ois = new ObjectInputStream(fis);
        track = (Track) ois.readObject();
        ois.close();
        fis.close();
        return track;
    }

    private void updateFiles(@NonNull final Track track) throws IOException {
        if(track != null) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS +"/"+ StorageController.TRACKS);
            file.createNewFile();

            final FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(track.getName()  + "\n");
            fileWriter.close();

            file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS +"/"+ StorageController.TRACKSBIN);
            file.createNewFile();
            final FileOutputStream fos = new FileOutputStream(file);

            final ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.listOfTracks);
            oos.close();
            fos.close();
        }
    }

    private void loadFiles() throws IOException, ClassNotFoundException {
        final File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS +"/"+ StorageController.TRACKSBIN);
        if(file.createNewFile()){
            this.updateFiles(new Track("Liste aller Tracks:\n"));
        }
        final FileInputStream fis = new FileInputStream(file);

        final ObjectInputStream ois = new ObjectInputStream(fis);
        final List<String> list = (List<String>) ois.readObject();
        ois.close();
        fis.close();

        this.listOfTracks.clear();
        this.listOfTracks.addAll(list);
    }

    private void saveTrackInFile(@NonNull final Track track) throws IOException {
        this.saveTrackLOG(track);

        final File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS+"/"+track.getName()+".track");
        file.createNewFile();
        final FileOutputStream fos = new FileOutputStream(file);
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(track);
        oos.close();
        fos.close();
    }

    private void saveTrackLOG(@NonNull final Track track) throws IOException{
        final File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS+"/"+track.getName()+"_track.txt");
        file.createNewFile();
        final FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.append("Das ist der Track "+track.getName() + " || Aufgenommen am "+new Date().toString()+"\n");
        fileWriter.append("---------------------------------------------------------------\n");
        for(Location location:track.getTracks())
            fileWriter.append(location.getLocation() + " | " + location.getDate()+ "\n");
        fileWriter.append("---------------------------------------------------------------\n");
        fileWriter.append("End of Track\n");
        fileWriter.close();
    }

    @Override
    public void onStartService() throws IOException, ClassNotFoundException {
        final File parent = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ StorageController.DIR_TRACKS);
        Log.d("ParentFile",parent.getAbsolutePath());
        if(!parent.exists())
            parent.mkdir();
        this.loadFiles();
    }

    @Override
    public void onDestroyService() throws IOException {
        this.updateFiles(null);
    }

    @Override
    public void saveTrack(Track track) throws IOException {
        this.updateFiles(track);
        this.saveTrackInFile(track);
    }
}
