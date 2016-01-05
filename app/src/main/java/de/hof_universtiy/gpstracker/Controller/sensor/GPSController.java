package de.hof_universtiy.gpstracker.Controller.sensor;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;

/**
 * Created by alex on 09.12.15.
 */
public class GPSController implements LocationListener, GPSControllerInterface {

    private final LocationManager locationManager;
    private final Context context;
    private final GPSChangeListener listener;

    private boolean isTracking = false;

    public final static String IS_TRACKING = "IS_TR";
    private GPSChangeListener mapChangeListener = null;

    public GPSController(@NonNull final Context context, @NonNull final GPSChangeListener listener) {
        this.context = context;
        this.listener = listener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStartService() throws GPSException {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            throw new GPSException(GPSException.ERROR_1);
        }
        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, this.context.getMainLooper());
        }catch (SecurityException e){
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    @Override
    public void onDestroyService() throws GPSException {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        this.endTracking();
    }

    @Override
    public void startTracking(@NonNull String name) throws GPSException {
        this.isTracking = true;
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,100,this);
        }catch (SecurityException e){
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        this.listener.createTrack(name);
    }

    @Override
    public void endTracking() {
        this.isTracking = false;
        this.listener.endTrack();
    }

    /**
     * Für den Listener im MapController
     *
     * @param mapChangeListener
     */
    @Override
    public void registerListener(@NonNull GPSChangeListener mapChangeListener) {
        this.mapChangeListener = mapChangeListener;
    }

    @Override
    public void unregisterListener() {
        this.mapChangeListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.listener.newPosition(new de.hof_universtiy.gpstracker.Model.position.Location(location));
        if(this.isTracking)
            this.listener.newWayPoint(new de.hof_universtiy.gpstracker.Model.position.Location(location));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this.context, "onStatusChanged " + provider,
                Toast.LENGTH_SHORT).show();
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

    public class GPSException extends Exception {
        public final static String ERROR_1 = "GPS wurde nicht eingeschaltet";
        public final static String ERROR_2 = "GPS wird nicht unterstützt";
        public GPSException(final String message) {
            super(message);
        }
    }
}
