package de.hof_universtiy.gpstracker.Controller.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.hof_universtiy.gpstracker.Controller.listener.GPSChangeListener;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.position.MapOverlay;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.*;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * Created by alex on 13.11.15.
 */
public class MapController implements MapControllerInterface {
    private final MapView mapView;
    private final Context activityContext;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MapOverlay myPosition;
    private final GPSChangeListenerMap gpsChangeListenerMap;

    public MapController(final Context context, final MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
        this.gpsChangeListenerMap = new GPSChangeListenerMap();
    }

    @Override
    public void goTo(@NonNull final GeoPoint point) {
        getMapController().setZoom(5);
        getMapController().setCenter(point);
    }

    @Override
    public void onStart() {
        configMapView();
    }

    @Override
    public void onDestroy() {
    }

    public void showMyPosition() throws SecurityException {
        this.myPosition = new MapOverlay(this.activityContext, new Location(((LocationManager) this.activityContext.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER)));
        this.mapView.getOverlayManager().add(myPosition);
    }

    public GPSChangeListener getListener(){
        return this.gpsChangeListenerMap;
    }

    private void configMapView() {
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getController().setZoom(5);
        enableCompass(true);
        enableRotationGesture();
    }

    private IMapController getMapController() {
        return this.mapView.getController();
    }

    private void addNewOverlayPoint(@NonNull final Location point) {
        MapOverlay mapPoint = new MapOverlay(this.activityContext, point);
        this.mapView.getOverlayManager().add(mapPoint);
        this.mapView.invalidate();
    }

    private void enableCompass(final boolean value) {
        this.mCompassOverlay = new CompassOverlay(this.activityContext, new InternalCompassOrientationProvider(this.activityContext), this.mapView);
        this.mapView.getOverlayManager().add(this.mCompassOverlay);
        this.mapView.invalidate();
    }

    private void enableRotationGesture() {
        mRotationGestureOverlay = new RotationGestureOverlay(this.activityContext, this.mapView);
        mRotationGestureOverlay.setEnabled(true);
        this.mapView.getOverlayManager().add(this.mRotationGestureOverlay);
        this.mapView.invalidate();

    }

    public class GPSChangeListenerMap implements GPSChangeListener {

        @Override
        public void newPosition(@NonNull Location location) {
            myPosition.setNewPosition(location);
        }

        @Override
        public void createTrack(@NonNull String name) {
            Toast.makeText(activityContext,"Create Track",Toast.LENGTH_LONG).show();
        }

        @Override
        public void newWayPoint(@NonNull Location location) {
            Toast.makeText(activityContext,"New Point",Toast.LENGTH_LONG).show();
        }

        @Override
        public void endTrack() {
            Toast.makeText(activityContext,"End Track",Toast.LENGTH_LONG).show();

        }
    }
}
