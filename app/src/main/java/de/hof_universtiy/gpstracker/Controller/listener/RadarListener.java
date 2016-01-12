package de.hof_universtiy.gpstracker.Controller.listener;

import de.hof_universtiy.gpstracker.Model.radar.Friend;

import java.util.List;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public interface RadarListener {
    public void setListOfFriends(List<Friend> friendList);
}
