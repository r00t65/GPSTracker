package de.hof_university.gpstracker.Controller.serialize;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.track.Track;

/**
 * Created by alex on 17.12.15.
 * GPSTracker
 */
public class StorageController implements StorageControllerInterface {
    public final static String SharedTracks = "TRACK";
    private final static String TRACKS = "track.txt";
    private final static String DIR_TRACKS = "tracks";
    private final Context context;

    public StorageController(@NonNull final Context context) {
        this.context = context;
    }

    public static Track loadTrackFromUri(Uri uri) throws IOException, ClassNotFoundException {
        final File file = new File(uri.getPath());
        final FileInputStream fis = new FileInputStream(file);

        final ObjectInputStream ois = new ObjectInputStream(fis);
        final Track track = (Track) ois.readObject();
        ois.close();
        fis.close();
        return track;
    }

    public static List<String> loadTrackList(){
        ArrayList<String> list = new ArrayList<>();
        final File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS);
        for(File file:parent.listFiles())
            if(file.getName().endsWith(".track"))
                list.add(file.getName().substring(0,file.getName().indexOf(".track")));
        return list;

    }

    @Override
    public void onStartService() throws IOException, ClassNotFoundException {
        final File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS);
        Log.d("ParentFile", parent.getAbsolutePath());
        if (!parent.exists())
            parent.mkdir();
    }

    @Override
    public void onDestroyService() throws IOException {
        this.updateFiles(null);
    }

    @Override
    public void saveTrack(@NonNull final Track track) throws IOException {
        this.updateFiles(track);
        this.saveTrackInFile(track);
        this.updateFiles(track);
    }

    @Override
    public void renameFile(@NonNull final String newName, @NonNull final String oldName) throws IOException, ClassNotFoundException {
        final Track track = this.loadTrack(oldName);
        this.saveTrack(track);
    }

    @Override
    public Track loadTrack(@NonNull String nameOfTrack) throws IOException, ClassNotFoundException {
        final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS + "/" + nameOfTrack + ".track");

        final FileInputStream fis = new FileInputStream(file);
        final ObjectInputStream ois = new ObjectInputStream(fis);
        Track track = (Track) ois.readObject();
        ois.close();
        return track;
    }

    private void updateFiles(@NonNull final Track track) throws IOException {
        if (track != null) {
            final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS + "/" + StorageController.TRACKS);
            file.createNewFile();

            final FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(track.getName() + "\n");
            fileWriter.close();
        }
    }

    private void saveTrackInFile(@NonNull final Track track) throws IOException {
        this.saveTrackLOG(track);

        final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS + "/" + track.getName() + ".track");
        file.createNewFile();
        final FileOutputStream fos = new FileOutputStream(file);
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(track);
        oos.flush();
        fos.flush();
        oos.close();
        fos.close();
    }

    private void saveTrackLOG(@NonNull final Track track) throws IOException {
        final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + StorageController.DIR_TRACKS + "/" + track.getName() + "_track.txt");
        file.createNewFile();
        final FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.append("Das ist der Track " + track.getName() + " || Aufgenommen am " + new Date().toString() + "\n");
        fileWriter.append("---------------------------------------------------------------\n");
        for (final Location location : track.getTracks())
            fileWriter.append(location.getLocation() + " | " + location.getDate() + "\n");
        fileWriter.append("---------------------------------------------------------------\n");
        fileWriter.append("End of Track\n");
        fileWriter.close();
    }
}
