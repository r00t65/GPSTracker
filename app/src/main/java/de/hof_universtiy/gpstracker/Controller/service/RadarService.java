package de.hof_universtiy.gpstracker.Controller.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import de.hof_universtiy.gpstracker.Controller.sensor.GPSController;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 */
public class RadarService extends IntentService {

    //   private final ConnectionController mConnectionController;
    //   private final GPSControllerInterface mGPSController;

    public RadarService() throws GPSController.GPSException {
        super("MyRadarService");
        //    this.mConnectionController = new ConnectionController(null,this.getBaseContext());
        //    this.mGPSController = new GPSController(this.getBaseContext(),this.mConnectionController);
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
}
