package de.hof_universtiy.gpstracker.Controller.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 *
 */
public class RadarService extends IntentService{

    public RadarService() {
        super("MyRadarService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("RadarService", "Service running");
    }
}
