package de.hof_university.gpstracker.Controller.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Andreas Ziemer on 11.01.2016.
 * BroadcastReveceiver für den AlarmManager zum starten des RadarService
 */
public class RadarServiceReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 17;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, RadarService.class);
        context.startService(i);
    }
}
