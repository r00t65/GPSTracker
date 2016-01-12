package de.hof_universtiy.gpstracker.Controller.map;

import android.content.Context;
import android.support.annotation.NonNull;
import de.hof_universtiy.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_universtiy.gpstracker.Controller.listener.RadarListener;
import de.hof_universtiy.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.radar.Friend;
import de.hof_universtiy.gpstracker.Model.track.Track;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 13.11.15 um 16:09
 GPSTracker
 */
public class MapController implements MapControllerInterface {
    private final MapView mapView;
    private final Context activityContext;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private final GPSChangeListenerMap gpsChangeListenerMap;

    private Location mPosition;
    private Track mTrack;

    public MapController(final Context context, final MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
        this.gpsChangeListenerMap = new GPSChangeListenerMap();
        configMapView();
    }

    @Override
    public void showMyPosition() throws SecurityException {
        this.mapView.getController().setZoom(5);
        this.mapView.getController().setCenter(new GeoPoint(this.mPosition.getLocation()));
    }

    @Override
    public GPSMapChangeListener getListener(){
        return this.gpsChangeListenerMap;
    }

    private void configMapView() {
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getController().setZoom(5);

        this.mCompassOverlay = new CompassOverlay(this.activityContext, new InternalCompassOrientationProvider(this.activityContext), this.mapView);
        this.mapView.getOverlayManager().add(this.mCompassOverlay);
        this.mapView.invalidate();
        mRotationGestureOverlay = new RotationGestureOverlay(this.activityContext, this.mapView);
        mRotationGestureOverlay.setEnabled(true);
        this.mapView.getOverlayManager().add(this.mRotationGestureOverlay);
        this.mapView.invalidate();

    }

    private void drawPosition(@NonNull final Location location){
        final MyPositionMapOverlay mapPoint = new MyPositionMapOverlay(this.activityContext, location);
        this.mapView.getOverlayManager().add(mapPoint);
        this.mapView.invalidate();
    }

    private void drawTrack(@NonNull final List<Location> track){
        RoadManager roadManager = new OSRMRoadManager();
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        for(Location location: track){
            geoPoints.add(new GeoPoint(location.getLocation()));
        }

        Road road = roadManager.getRoad(geoPoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, activityContext);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }

    private void clearMap(){
        this.mapView.getOverlays().clear();
        this.mapView.invalidate();
    }

    public class GPSChangeListenerMap implements GPSMapChangeListener {

        @Override
        public void newPosition(@NonNull Location location) {
            clearMap();
            mPosition = location;
            drawPosition(location);
            if(mTrack != null)
                drawTrack(mTrack.getTracks());
        }

        @Override
        public void updateTrack(@NonNull Track track) {
            clearMap();
            mTrack = track;
            drawTrack(track.getTracks());
            if(mPosition != null)
                drawPosition(mPosition);
        }
    }
    public class RadarMapListener implements RadarListener {

        @Override
        public void setListOfFriends(@NonNull Location myPosition, @NonNull List<Friend> friendList) {

        }
    }
}
