package de.hof_universtiy.gpstracker.Model.radar;

import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patrick Büttner on 21.11.2015.
 */
public final class FriendsPositionModel implements Serializable {
    private final String id;
    private final Location location;

    public FriendsPositionModel(@NonNull final String id,@NonNull final double latitude,@NonNull final double longitude,@NonNull final  Date time) {
        this.id = id;
        location = new Location(latitude, longitude, time);
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }
}
