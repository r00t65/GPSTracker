package de.hof_university.gpstracker.Controller.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.IOException;

import de.hof_university.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_university.gpstracker.Controller.sensor.GPSController;
import de.hof_university.gpstracker.Controller.tracking.TrackingController;
import de.hof_university.gpstracker.MainActivity;
import de.hof_university.gpstracker.R;

/**
 * Created by Andreas Ziemer on 16.12.15.
 * Track aufzeichnen und an Server senden
 */
public class TrackingService extends Service {

    private final IBinder mBinder = new LocalBinder();
    NotificationManager notificationManager;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Notification trackerNotification;
    //-------------------------------Controller---------------------------
    private GPSController gpsController;
    private TrackingController trackingController;
    //--------------------------------------------------------------------

    @Override
    public void onCreate() {
        Log.v("Service", "Service erstellt");
        //-------------------------------Controller---------------------------
        trackingController = new TrackingController(this.getBaseContext());
        try {
            gpsController = new GPSController(this.getBaseContext(), this.trackingController);
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------
        HandlerThread thread = new HandlerThread("ServiceStartArg", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.v("Service", "Service gestartet");
        //-------------------------------Controller---------------------------
        trackingController.onStartService();
        try {
            gpsController.onStartService();
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        }
        try {
            gpsController.startTracking("Test");
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startID;
        mServiceHandler.sendMessage(msg);

        //Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        PendingIntent pIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
        trackerNotification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.NotificationTitle))
                .setContentText(getString(R.string.tracking))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        trackerNotification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, trackerNotification);
        //Notification ende

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //-------------------------------Controller---------------------------
        try {
            gpsController.endTracking();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        trackingController.onDestroyService();
        try {
            gpsController.onDestroyService();
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------
        Log.v("Service", "Service beendet");
        notificationManager.cancel(1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method for clients
     */
    public String getServiceInfo() {
        String info = "Bound Service";
        return info;
    }

    public void registerListener(GPSMapChangeListener gpsChangeListener) {
        //trackingController.registerListener(gpsChangeListener);
        trackingController.registerGPSListener(gpsChangeListener);
    }

    public void unregisterListener() {
        // trackingController.unregisterListener();
        trackingController.unregisterGPSListener();
    }

    public void saveTrack(final String trackName) throws IOException, ClassNotFoundException {
        this.trackingController.setNewName(trackName);
        this.trackingController.trackFinish(null);
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
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

    public class LocalBinder extends Binder {
        public TrackingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackingService.this;
        }
    }
}

