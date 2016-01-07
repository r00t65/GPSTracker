package de.hof_universtiy.gpstracker.Controller.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import de.hof_universtiy.gpstracker.R;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Position an Server
 *
 */
public class RadarService extends Service{

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            //alle 20 minuten update
            long endTime = System.currentTimeMillis() + 20*60*1000;
            while(System.currentTimeMillis() < endTime){
                synchronized (this){
                    try{
                        wait(endTime - System.currentTimeMillis());
                        //überarbeiten und position an server
                    }catch (Exception e){
                    }
                }
            }
            //ende
        }
    }

    @Override
    public void onCreate(){
        HandlerThread thread = new HandlerThread("ServiceStartArg", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startID;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        serviceNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void serviceNotification(){
        Intent intent = new Intent(this, Messenger.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Radar")
                .setContentText("asd")
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,n);
    }

}
