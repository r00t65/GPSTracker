package de.hof_universtiy.gpstracker.Controller.radar;

import android.content.Context;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.radar.Friend;
import org.osmdroid.views.MapView;

import java.util.List;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public class RadarController implements RadarControllerInterface {

    private final MapView radarView;
    private final Context context;

    public RadarController(@NonNull Context context,@NonNull MapView lRadarView){
        this.context = context;
        this.radarView = lRadarView;
    }

    @Override
    public void setListOfFriends(@NonNull Location myPosition, @NonNull List<Friend> friendList) {

    }
}
