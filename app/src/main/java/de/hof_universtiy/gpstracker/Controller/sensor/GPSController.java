package de.hof_universtiy.gpstracker.Controller.sensor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerService;

/**
 * Created by alex on 09.12.15.
 */
public class GPSController extends ControllerService implements LocationListener {

    private final LocationManager locationManager;
    private final Context context;
    private final PositionChangeListener listener;


    public GPSController(Context context, PositionChangeListener listener) {
        this.context = context;
        this.listener = listener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStartService(Bundle data) throws GPSException {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.context.startActivity(intent);
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            throw new GPSException("GPS wurde nicht angemeldet");
        }
        if(data.getBoolean("isSingleRequest")){
            locationManager.requestSingleUpdate("",this,this.context.getMainLooper());
        }else{
            locationManager.requestLocationUpdates("GPS", 400, 1, this);
        }

    }

    @Override
    public void onDestroyService(Bundle data) {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.listener.setNewPosition(location);
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

    public abstract class PositionChangeListener{

        public abstract void setNewPosition(Location location);
    }

    public class GPSException extends Exception {
        public GPSException(String message) {
            super(message);
        }
    }
}
