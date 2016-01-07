package de.hof_universtiy.gpstracker.Controller.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import de.hof_universtiy.gpstracker.Controller.sensor.GPSController;
import de.hof_universtiy.gpstracker.Controller.tracking.TrackingController;
import de.hof_universtiy.gpstracker.MainActivity;
import de.hof_universtiy.gpstracker.R;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Track aufzeichnen und an Server senden
 */
public class TrackingService extends Service{

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Notification trackerNotification;
    NotificationManager notificationManager;
    private final IBinder mBinder = new LocalBinder();

    //-------------------------------Controller---------------------------
    private  GPSController gpsController;
    private  TrackingController trackingController;
    //--------------------------------------------------------------------

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            //work
            /**
             long endTime = System.currentTimeMillis() + 30*1000;
            while(System.currentTimeMillis() < endTime){
                synchronized (this){
                    try{
                        wait(endTime - System.currentTimeMillis());
                    }catch (Exception e){
                    }
                }
            }

            //ende
            (msg.arg1);
             */
        }
    }

    @Override
    public void onCreate(){
        Log.v("Service", "Service erstellt");
        //-------------------------------Controller---------------------------
        trackingController = new TrackingController(this.getBaseContext());
        gpsController = new GPSController(this.getBaseContext(),this.trackingController);
        //--------------------------------------------------------------------
        HandlerThread thread = new HandlerThread("ServiceStartArg", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Log.v("Service", "Service gestartet");
        Toast.makeText(this, "Service gestartet", Toast.LENGTH_SHORT).show();

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startID;
        mServiceHandler.sendMessage(msg);

        //Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        trackerNotification = new Notification.Builder(this)
                .setContentTitle("GPSTracker")
                .setContentText(getString(R.string.tracking))
                .setSmallIcon(R.drawable.person)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        trackerNotification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,trackerNotification);
        //Notification ende

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v("Service", "Service beendet");
        notificationManager.cancel(1);
    }


    public class LocalBinder extends Binder {
        public TrackingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public String getServiceInfo(){
        String info = "Bound Service";
        return info;
    }

    public void registerListener(){

    }

    public void unregisterListener(){

    }

    public void saveTrack(){

    }
}

