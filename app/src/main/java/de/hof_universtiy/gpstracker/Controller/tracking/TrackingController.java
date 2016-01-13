package de.hof_universtiy.gpstracker.Controller.tracking;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;
import de.hof_universtiy.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_universtiy.gpstracker.Controller.listener.NotificationTrackListener;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.track.Track;

import java.io.IOException;
import java.util.Date;


/**
 * Created by alex on 17.12.15.
 */
public final class TrackingController implements TrackingControllerInterface {

    public Track track;
    private final Context context;
    private GPSMapChangeListener gpsChangeListener;
    private NotificationTrackListener listenerForServerConnetion;

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
    public void endTrack() throws IOException, ClassNotFoundException {
        this.saveTrack();

        if (this.gpsChangeListener != null) {
            this.gpsChangeListener.updateTrack(this.track);
        }
        if (this.listenerForServerConnetion != null)
            this.listenerForServerConnetion.trackFinish(this.track);
    }

    private void saveTrack() throws IOException, ClassNotFoundException {
        final StorageController str = new StorageController(this.context);
        str.onStartService();
        if(str.getListOfTrackNames().contains(this.track.getName()))
            this.track = new Track(this.track.getName()+"|"+new Date(),this.track);
        str.saveTrack(this.track);
    }

    public void setNewName(String newName) {
        this.track = new Track(newName,this.track);
    }
}
