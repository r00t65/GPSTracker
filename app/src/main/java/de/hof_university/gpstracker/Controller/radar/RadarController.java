package de.hof_university.gpstracker.Controller.radar;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Date;
import java.util.List;

import de.hof_university.gpstracker.Model.mapoverlays.FriendMapOverlay;
import de.hof_university.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.radar.FriendsPositionModel;
import de.hof_university.gpstracker.R;

/**
 * Created by alex on 12.01.16.
 * GPSTracker
 */
public class RadarController implements RadarControllerInterface {

    private final MapView radarView;
    private final Context context;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MyLocationNewOverlay mLocationOverlay;

    public RadarController(@NonNull Context context, @NonNull MapView lRadarView) {
        this.context = context;
        this.radarView = lRadarView;

        this.configRadarView();
    }

    @Override
    public void setListOfFriends(final Location myPosition, @NonNull final List<FriendsPositionModel> friendList) {
        this.clearRadar();
        // this.drawMyPosition(myPosition);
        this.drawFriends(friendList);
    }

    private void drawFriends(@NonNull final List<FriendsPositionModel> friendList) {
        for (final FriendsPositionModel friend : friendList)
            drawFriend(friend);
    }

    private void drawFriend(@NonNull final FriendsPositionModel friend) {
        Toast.makeText(this.context, friend.getLocation().getLocation().toString(), Toast.LENGTH_LONG).show();

        final FriendMapOverlay mapPoint = new FriendMapOverlay(this.context, friend);
        this.radarView.getOverlayManager().add(mapPoint);
        radarIsInvalidate();
        radarIsInvalidate();
    }

    private void showMyPosition() throws SecurityException {
        this.radarView.getController().setZoom(10);
        this.radarView.getController().setCenter(new GeoPoint(new Location(50.324759, 11.940344, new Date()).getLocation()));
    }

    private void configRadarView() {
        this.radarView.setTileSource(TileSourceFactory.BASE_OVERLAY_NL);

        this.radarView.setBuiltInZoomControls(true);
        this.radarView.setMultiTouchControls(true);
        this.radarView.setMinZoomLevel(10);
        this.radarView.setMaxZoomLevel(15);
        this.radarView.setHovered(true);
        this.radarView.setVerticalFadingEdgeEnabled(false);
        this.radarView.setScrollableAreaLimit(new BoundingBoxE6(84.34635, -178.80544, -84.34635, +178.80544));
        this.showMyPosition();

        this.mLocationOverlay = new MyLocationNewOverlay(context, new GpsMyLocationProvider(context), this.radarView);
        this.radarView.getOverlays().add(this.mLocationOverlay);
        this.mLocationOverlay.enableFollowLocation();
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.radar96));

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