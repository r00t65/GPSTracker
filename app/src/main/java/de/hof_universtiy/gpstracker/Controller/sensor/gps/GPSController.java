package de.hof_universtiy.gpstracker.Controller.sensor.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;

/**
 * Created by alex on 09.12.15.
 */
public class GPSController extends ControllerService implements LocationListener {

    private final LocationManager locationManager;
    private final Context context;
    private final PositionChangeListener listener;
    private final TrackingListener trackingListener;


    private boolean isTracking = false;

    public final static String IS_TRACKING = "IS_TR";

    public final static String ERROR_1 = "GPS wurde nicht eingeschaltet";
    public final static String ERROR_2 = "GPS wird nicht unterstützt";

    public GPSController(Context context,@NonNull PositionChangeListener listener ,@NonNull TrackingListener trackingListener) {
        this.context = context;
        this.listener = listener;
        this.trackingListener = trackingListener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStartService(Bundle data) throws GPSException {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            throw new GPSException(GPSController.ERROR_1);
        }
        if (!data.getBoolean(GPSController.IS_TRACKING)) {
            try {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, this.context.getMainLooper());
            } catch (SecurityException e) {
                GPSException ex = new GPSException(GPSController.ERROR_2);
                ex.setStackTrace(e.getStackTrace());
                throw ex;
            }
        } else {
            try {
                this.isTracking = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
            } catch (SecurityException e) {
                GPSException ex = new GPSException(GPSController.ERROR_2);
                ex.setStackTrace(e.getStackTrace());
                throw ex;
            }
        }

    }

    @Override
    public void onDestroyService(Bundle data) throws GPSException {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSController.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    public void pauseTracking(){
        this.isTracking = false;
        this.trackingListener.trackEnd();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.listener.setNewPosition(location);
        if(this.isTracking)
            this.trackingListener.setNewPosition(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this.context, "enabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this.context, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public interface PositionChangeListener {

        public void setNewPosition(Location location);
    }
    public interface TrackingListener extends PositionChangeListener {

        public void trackEnd();
    }

    public class GPSException extends Exception {
        public GPSException(String message) {
            super(message);
        }
    }
}
