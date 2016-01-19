package de.hof_universtiy.gpstracker.Controller.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;

import java.io.IOException;

import de.hof_universtiy.gpstracker.Controller.connection.ConnectionController;
import de.hof_universtiy.gpstracker.Controller.sensor.GPSController;
import de.hof_universtiy.gpstracker.Controller.sensor.GPSControllerInterface;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 */
public class RadarService extends IntentService {

       private final ConnectionController mConnectionController;
       private final GPSControllerInterface mGPSController;

    public RadarService() throws GPSController.GPSException, FacebookAuthorizationException {
        super("MyRadarService");
        try {
            this.mConnectionController = new ConnectionController(this.getBaseContext(), null);
        }catch(FacebookAuthorizationException fbE){
            throw fbE;
        }
        this.mGPSController = new GPSController(this.getApplicationContext(),this.mConnectionController);
    }

    /**
     * @param intent
     * Funktion die je nach Einstellung des Nutzers wiederholt die Postion ausgefuehrt wird
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //kein sleep, da über AlarmManager in Standby geht
        Log.i("RadarService", "Service running");
    }


    public void onDestroy(){
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
