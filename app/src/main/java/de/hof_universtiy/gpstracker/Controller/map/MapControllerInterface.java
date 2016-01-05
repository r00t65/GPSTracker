package de.hof_universtiy.gpstracker.Controller.map;

import android.support.annotation.NonNull;
import org.osmdroid.util.GeoPoint;

/**
 * Created by alex on 05.01.16.
 * GPSTracker
 */
public interface MapControllerInterface {
    public void goTo(@NonNull GeoPoint point);
    public void onStart();
    public void onDestroy();
}
