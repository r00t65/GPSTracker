package de.hof_university.gpstracker.Controller.sensor;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.IOException;

import de.hof_university.gpstracker.Controller.listener.GPSChangeListener;
import de.hof_university.gpstracker.Model.track.Track;

/**
 *  Liest GPSDaten vom System und packt sie in Location
 *
 * Created by alex on 09.12.15.
 */
public class GPSController implements GPSControllerInterface2 {

    private final LocationManager locationManager;
    private final Context context;
    private final GPSChangeListener listener;
    private boolean isTracking = false;

    public GPSController(@NonNull final Context context, @NonNull final GPSChangeListener listener) throws GPSException {
        this.context = context;
        this.listener = listener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.onStartService();
    }

    /**
     * Testet ob GPS Empfänger vorhanden ist
     * @param context
     * @return true für verfügbar und false für nicht verfügbar
     */
    public static boolean isGPSEnable(@NonNull final Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Ist für den Lebenszyklus von Services ausgelegt
     *
     * Die Listener werden Registriert
     */
    @Override
    public void onStartService() throws GPSException {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            throw new GPSException(GPSException.ERROR_1);
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 200, this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    /**
     * Ist für den Lebenszyklus von Services ausgelegt
     *
     * Die Listener werden entfernt
     */
    @Override
    public void onDestroyService() throws GPSException, IOException, ClassNotFoundException {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    /**
     * Wir aufgerufen, wenn der Track gestartet wird
     * @param name Der Name des Tracks
     * @throws GPSException Wird geworfen wenn GPS-Sensor nicht vorhanden ist
     */
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 300, this);
        } catch (SecurityException e) {
            GPSException ex = new GPSException(GPSException.ERROR_2);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        this.listener.createTrack(name);
    }

    /**
     *
     * @throws IOException siehe {@see de.hof_university.gpstracker.Controller.serializeStorageController}
     * @throws ClassNotFoundException siehe {@see de.hof_university.gpstracker.Controller.serializeStorageController}
     */
    @Override
    public void endTracking() throws IOException, ClassNotFoundException {
        this.isTracking = false;
        this.listener.trackFinish(null);
    }
    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        final de.hof_university.gpstracker.Model.position.Location location1 = new de.hof_university.gpstracker.Model.position.Location(location);
        if (this.isTracking) {
            try {
                this.listener.newWayPoint(location1);
            } catch (Track.TrackFinishException e) {
                e.printStackTrace();
            }
        }
        this.listener.newPosition(location1);
    }
    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     * update.
     * @param status {@link LocationProvider#OUT_OF_SERVICE} if the
     * provider is out of service, and this is not expected to change in the
     * near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
     * the provider is temporarily unavailable but is expected to be available
     * shortly; and {@link LocationProvider#AVAILABLE} if the
     * provider is currently available.
     * @param extras an optional Bundle which will contain provider specific
     * status variables.
     *
     * <p> A number of common key/value pairs for the extras Bundle are listed
     * below. Providers that use any of the keys on this list must
     * provide the corresponding value as described below.
     *
     * <ul>
     * <li> satellites - the number of satellites used to derive the fix
     * </ul>
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this.context, "onStatusChanged " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     * update.
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this.context, "enabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     * update.
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this.context, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Exception für die Fehlerbehandlung
     */
    public class GPSException extends Exception {
        public final static String ERROR_1 = "GPS wurde nicht eingeschaltet";
        public final static String ERROR_2 = "GPS wird nicht unterstützt";

        public GPSException(final String message) {
            super(message);
        }
    }
}
