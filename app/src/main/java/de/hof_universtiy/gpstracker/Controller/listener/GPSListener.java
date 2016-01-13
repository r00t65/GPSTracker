package de.hof_universtiy.gpstracker.Controller.listener;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;

/**
 * Created by alex on 13.01.16.
 */
public interface GPSListener {
    public void newPosition(@NonNull final Location location);
}
