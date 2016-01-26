package de.hof_university.gpstracker.Model.radar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

import de.hof_university.gpstracker.Model.position.Location;

/**
 * Created by Patrick Büttner on 21.11.2015.
 */
public final class FriendsPositionModel implements Serializable {
    private final String id;
    private final Location location;

    public FriendsPositionModel(@NonNull final String id, @NonNull final double latitude, @NonNull final double longitude, @NonNull final Date time) {
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
