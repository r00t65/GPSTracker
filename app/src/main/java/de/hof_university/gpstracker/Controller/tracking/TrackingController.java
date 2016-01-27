package de.hof_university.gpstracker.Controller.tracking;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import de.hof_university.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_university.gpstracker.Controller.listener.NotificationTrackListener;
import de.hof_university.gpstracker.Controller.listener.SensorChangeListener;
import de.hof_university.gpstracker.Controller.serialize.StorageController;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.position.SensorData;
import de.hof_university.gpstracker.Model.track.Track;


/**
 * Created by alex on 17.12.15.
 */
public final class TrackingController implements TrackingControllerInterface {
    public final static String SharedReNameTrack = "RETRACK";

    private final Context context;
    public Track track;
    private GPSMapChangeListener gpsChangeListener;
    /**
     * Für den ConnectionController
     */
    private NotificationTrackListener listenerForServerConnetion;
    private SensorChangeListener sensorChangeListenerUI;

    public TrackingController(@NonNull final Context context) {
        this.context = context;
    }

    /**
     * Für den Listener im MapController
     *
     * @param gpsChangeListener
     */
    @Override
    public void registerGPSListener(@NonNull GPSMapChangeListener gpsChangeListener) {
        this.gpsChangeListener = gpsChangeListener;
    }

    @Override
    public void unregisterGPSListener() {
        this.gpsChangeListener = null;
    }

    @Override
    public void registerServerListener(@NonNull NotificationTrackListener listener) {
        this.listenerForServerConnetion = listener;
    }

    @Override
    public void unregisterServerListener() {
        this.listenerForServerConnetion = null;
    }

    @Override
    public void onStartService() {

    }

    @Override
    public void onDestroyService() {

    }

    @Override
    public void registerSensorListener(SensorChangeListener sensorChangeListener) {
        this.sensorChangeListenerUI = sensorChangeListener;
    }

    @Override
    public void newPosition(@NonNull Location location) {
        if (this.gpsChangeListener != null) {
            this.gpsChangeListener.newPosition(location);
        }
        if (this.listenerForServerConnetion != null)
            this.listenerForServerConnetion.newPosition(location);
    }

    @Override
    public void createTrack(@NonNull String name) {
        this.track = new Track(name);
        if (this.gpsChangeListener != null) {
            this.gpsChangeListener.updateTrack(this.track);
        }
    }

    @Override
    public void newWayPoint(@NonNull Location location) throws Track.TrackFinishException {
        if (this.gpsChangeListener != null) {
            this.gpsChangeListener.updateTrack(this.track);
        }
        if (this.listenerForServerConnetion != null)
            this.listenerForServerConnetion.newPosition(location);
        this.track.addNode(location);
    }

    @Override
    public void trackFinish(Track track) throws IOException, ClassNotFoundException {
        this.saveTrack();

        /*if (this.gpsChangeListener != null) {
            this.gpsChangeListener.updateTrack(this.track);
        }*/
        if (this.listenerForServerConnetion != null)
            this.listenerForServerConnetion.trackFinish(this.track);
    }

    private void saveTrack() throws IOException, ClassNotFoundException {
        this.setNewName(PreferenceManager.getDefaultSharedPreferences(context).getString(TrackingController.SharedReNameTrack, "unnamed Track"));
        final StorageController str = new StorageController(this.context);
        str.onStartService();
        if (StorageController.loadTrackList().contains(this.track.getName()))
            this.track = new Track(this.track.getName() + "|" + new Date(), this.track);
        str.saveTrack(this.track);
    }

    private void setNewName(String newName) {
        Toast.makeText(this.context, newName, Toast.LENGTH_LONG).show();
        this.track = new Track(newName, this.track);
    }

    @Override
    public void updateSensorData(SensorData sensorData) {
        this.track.addNode(sensorData);
        if(this.sensorChangeListenerUI != null)
            this.sensorChangeListenerUI.updateSensorData(sensorData);
    }
}
