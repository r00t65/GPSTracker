package de.hof_university.gpstracker.Controller.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;

import java.io.IOException;

import de.hof_university.gpstracker.Controller.connection.ConnectionController;
import de.hof_university.gpstracker.Controller.sensor.GPSController;
import de.hof_university.gpstracker.Controller.sensor.GPSControllerInterface;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 */
public class RadarService extends IntentService {

    private ConnectionController mConnectionController;
    private GPSControllerInterface mGPSController;

    public RadarService() throws GPSController.GPSException, FacebookAuthorizationException {
        super("MyRadarService");

    }

    /**
     * @param intent Funktion die je nach Einstellung des Nutzers wiederholt die Postion ausgefuehrt wird
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //kein sleep, da über AlarmManager in Standby geht
        try {
            Log.i("RadarService", "GPSController");
            mConnectionController = new ConnectionController(this.getBaseContext(), null);
        } catch (FacebookAuthorizationException fbE) {
            throw fbE;
        }
        try {
            this.mGPSController = new GPSController(this.getBaseContext(), this.mConnectionController);
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        }
        Log.i("RadarService", "Service running");
    }


    public void onDestroy() {
        try {
            mGPSController.onDestroyService();
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
