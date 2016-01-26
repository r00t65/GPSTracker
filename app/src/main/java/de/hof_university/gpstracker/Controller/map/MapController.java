package de.hof_university.gpstracker.Controller.map;

import android.content.Context;
import android.support.annotation.NonNull;

import de.hof_university.gpstracker.Controller.listener.LoadTrackListener;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hof_university.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_university.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.track.Track;

/**
 * Created by alex on 13.11.15 um 16:09
 * GPSTracker
 */
public class MapController implements MapControllerInterface,LoadTrackListener {
    private final MapView mapView;
    private final Context activityContext;
    private final GPSChangeListenerMap gpsChangeListenerMap;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private Location mPosition;
    private Track mTrack;
    private MyPositionMapOverlay myPositionOverlay;

    public MapController(final Context context, final MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
        this.gpsChangeListenerMap = new GPSChangeListenerMap();
        configMapView();
    }

    @Override
    public void showMyPosition() throws SecurityException {
        this.mapView.getController().setZoom(10);
        this.myPositionOverlay = new MyPositionMapOverlay(this.activityContext, new Location(50.324759, 11.940344, new Date()));
        this.mapView.getController().setCenter(new GeoPoint(new Location(50.324759, 11.940344, new Date()).getLocation()));
    }

    @Override
    public GPSMapChangeListener getListener() {
        return this.gpsChangeListenerMap;
    }

    private void configMapView() {
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.setMinZoomLevel(7);
        this.mapView.setHovered(true);
        this.mapView.setVerticalFadingEdgeEnabled(false);
        this.mapView.setScrollableAreaLimit(new BoundingBoxE6(84.34635, -178.80544, -84.34635, +178.80544));
        this.showMyPosition();

        this.mCompassOverlay = new CompassOverlay(this.activityContext, new InternalCompassOrientationProvider(this.activityContext), this.mapView);
        this.mapView.getOverlayManager().add(this.mCompassOverlay);
        this.mapView.invalidate();
        mRotationGestureOverlay = new RotationGestureOverlay(this.activityContext, this.mapView);
        mRotationGestureOverlay.setEnabled(true);
        this.mapView.getOverlayManager().add(this.mRotationGestureOverlay);
        this.mapView.invalidate();

    }

    private void drawPosition(@NonNull final Location location) {
        final MyPositionMapOverlay mapPoint = new MyPositionMapOverlay(this.activityContext, location);
        this.mapView.getOverlayManager().add(mapPoint);
        this.mapView.invalidate();
    }

    private void drawTrack(@NonNull final List<Location> track) {
        //RoadManager roadManager = new OSRMRoadManager();
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        for (Location location : track) {
            geoPoints.add(new GeoPoint(location.getLocation()));
        }
        //Road road = roadManager.getRoad(geoPoints);
        Polyline roadOverlay = new Polyline(this.activityContext);

        roadOverlay.setPoints(geoPoints);
        roadOverlay.setVisible(true);
        roadOverlay.setWidth(4);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }

    private void clearMap() {
        this.mapView.getOverlays().clear();
        this.mapView.invalidate();
    }

    @Override
    public void loadOtherTrack(@NonNull Track track) {
        this.getListener().updateTrack(track);
    }

    public class GPSChangeListenerMap implements GPSMapChangeListener {

        @Override
        public void newPosition(@NonNull Location location) {
            //clearMap();
            mPosition = location;
            drawPosition(location);
            if (mTrack != null)
                drawTrack(mTrack.getTracks());
        }

        @Override
        public void updateTrack(@NonNull Track track) {
            //clearMap();
            mTrack = track;
            drawTrack(track.getTracks());
            drawPosition(track.getTracks().get(0));
            if (mPosition != null)
                drawPosition(mPosition);
        }
    }
}
