package de.hof_universtiy.gpstracker.Controller.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Andreas Ziemer on 11.01.2016.
 */
public class RadarServiceReceiver extends BroadcastReceiver{
    public static final int REQUEST_CODE = 17;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, RadarService.class);
        context.startService(i);
    }
}
