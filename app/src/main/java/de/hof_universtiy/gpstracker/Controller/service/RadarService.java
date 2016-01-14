package de.hof_universtiy.gpstracker.Controller.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
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

    public RadarService() throws GPSController.GPSException {
        super("MyRadarService");
        this.mConnectionController = new ConnectionController(null,this.getBaseContext());
        this.mGPSController = new GPSController(this.getBaseContext(),this.mConnectionController);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        //TODO sleep
        Log.i("RadarService", "Service running");
    }
}
