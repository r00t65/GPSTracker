package de.hof_universtiy.gpstracker.Controller.radar;

import android.content.Context;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Model.mapoverlays.FriendMapOverlay;
import de.hof_universtiy.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.radar.Friend;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.Date;
import java.util.List;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public class RadarController implements RadarControllerInterface {

    private final MapView radarView;
    private final Context context;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;

    public RadarController(@NonNull Context context, @NonNull MapView lRadarView) {
        this.context = context;
        this.radarView = lRadarView;

        this.configRadarView();
    }

    @Override
    public void setListOfFriends(@NonNull final Location myPosition, @NonNull final List<Friend> friendList) {
        this.clearRadar();
        this.drawMyPosition(myPosition);
        this.drawFriends(friendList);
    }

    private void drawFriends(@NonNull final List<Friend> friendList) {
        for (final Friend friend : friendList)
            drawFriend(friend);
    }

    private void drawFriend(@NonNull final Friend friend) {
        final FriendMapOverlay mapPoint = new FriendMapOverlay(this.context, friend);
        this.radarView.getOverlayManager().add(mapPoint);
        radarIsInvalidate();
        radarIsInvalidate();
    }

    private void showMyPosition() throws SecurityException {
        this.radarView.getController().setZoom(10);
        this.radarView.getController().setCenter(new GeoPoint(new Location(50.324759,11.940344,new Date()).getLocation()));
    }

    private void configRadarView() {
        this.radarView.setTileSource(TileSourceFactory.BASE_OVERLAY_NL);

        this.radarView.setBuiltInZoomControls(true);
        this.radarView.setMultiTouchControls(true);
        this.radarView.setMinZoomLevel(7);
        this.radarView.setHovered(true);
        this.radarView.setVerticalFadingEdgeEnabled(false);
        this.showMyPosition();

        this.mCompassOverlay = new CompassOverlay(this.context, new InternalCompassOrientationProvider(this.context), this.radarView);
        this.radarView.getOverlayManager().add(this.mCompassOverlay);

        mRotationGestureOverlay = new RotationGestureOverlay(this.context, this.radarView);
        mRotationGestureOverlay.setEnabled(true);
        this.radarView.getOverlayManager().add(this.mRotationGestureOverlay);

        radarIsInvalidate();
    }

    private void drawMyPosition(@NonNull final Location location) {
        final MyPositionMapOverlay mapPoint = new MyPositionMapOverlay(this.context, location);
        this.radarView.getOverlayManager().add(mapPoint);
        radarIsInvalidate();
    }

    private void radarIsInvalidate() {
        this.radarView.invalidate();
    }

    private void clearRadar() {
        this.radarView.getOverlays().clear();
        this.radarView.invalidate();
    }
}
