package de.hof_universtiy.gpstracker.Controller.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * Created by alex on 13.11.15.
 */
public class MapController implements MapControllerInterface {
    private final MapView mapView;
    private final Context activityContext;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MyLocationNewOverlay mLocationOverlay;

    public MapController(final Context context,final MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
    }

    @Override
    public void goTo(@NonNull final GeoPoint point){
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

    private void configMapView(){
        this.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getController().setZoom(5);
        enableCompass(true);
        enableRotationGesture();
        setOwnPosition();
    }

    private IMapController getMapController(){
        return this.mapView.getController();
    }

    private void addNewOverlayPoint(final GeoPoint point){
        //final MapOverlay mapPoint = new MapOverlay(this.activityContext,point);
        //this.mapView.getOverlayManager().add(mapPoint);
        this.mapView.invalidate();
    }

    private void setOwnPosition(){
        this.mLocationOverlay = new MyLocationNewOverlay(this.activityContext, new GpsMyLocationProvider(this.activityContext),this.mapView);
        this.mapView.getOverlays().add(this.mLocationOverlay);
        this.mapView.invalidate();
    }

    private void enableCompass(final boolean value){
        this.mCompassOverlay = new CompassOverlay(this.activityContext, new InternalCompassOrientationProvider(this.activityContext), this.mapView);
        this.mapView.getOverlayManager().add(this.mCompassOverlay);
        this.mapView.invalidate();
    }

    private void enableRotationGesture(){
        mRotationGestureOverlay = new RotationGestureOverlay(this.activityContext, this.mapView);
        mRotationGestureOverlay.setEnabled(true);
        this.mapView.getOverlayManager().add(this.mRotationGestureOverlay);
        this.mapView.invalidate();

    }
}
