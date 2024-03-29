package de.hof_university.gpstracker.Controller.listener;

import android.support.annotation.NonNull;

import java.util.List;

import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.radar.FriendsPositionModel;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public interface RadarListener {
    public void setListOfFriends(final Location myPosition, @NonNull final List<FriendsPositionModel> friendList);
}
