package de.hof_universtiy.gpstracker.Controller.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 *
 */
public class RadarService extends Service{

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

}
