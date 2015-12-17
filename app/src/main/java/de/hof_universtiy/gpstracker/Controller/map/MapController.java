package de.hof_universtiy.gpstracker.Controller.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerActivity;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerActivityInterface;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * Created by alex on 13.11.15.
 */
public class MapController extends ControllerActivity {
    private final MapView mapView;
    private final Context activityContext;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MyLocationNewOverlay mLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private final RouteController routeController;
    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPrefChangeListener;
    private final SharedPreferences prefs;

    public MapController(Context context,MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
        this.routeController = new RouteController(this);
        this.prefs = this.getContext().getSharedPreferences(StorageController.KEY_SHAREDPREF_TRACK, 0);
        this.sharedPrefChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                //refresh
            }
        };
    }


   /*@Override
    public void positionHasChange(Location location) {
        final GeoPoint gpt = new GeoPoint(location);
        this.mapView.getController().setCenter(gpt);
        if(this.mapView.getZoomLevel()>5)
            this.mapView.getController().setZoom(2);
        getMapController().setCenter(gpt);
        addNewOverlayPoint(gpt);
        this.routeController.addGeoPoint(gpt);
    }*/

    public void goTo(GeoPoint point){
        getMapController().setZoom(5);
        getMapController().setCenter(point);
    }

    public RouteController getRouteController(){
        return this.routeController;
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

    private void addNewOverlayPoint(GeoPoint point){
        //final MapOverlay mapPoint = new MapOverlay(this.activityContext,point);
        //this.mapView.getOverlayManager().add(mapPoint);
        this.mapView.invalidate();
    }

    private void enableCompass(boolean value){
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

    private void setOwnPosition(){
        this.mLocationOverlay = new MyLocationNewOverlay(this.activityContext, new GpsMyLocationProvider(this.activityContext),this.mapView);
        this.mapView.getOverlays().add(this.mLocationOverlay);
        this.mapView.invalidate();
    }

    public Context getContext() {
        return this.activityContext;
    }

    public void addRoadToMap(){
      //  this.mapView.getOverlayManager().add(this.routeController.getPolylineRoad());
        this.mapView.invalidate();
    }

    @Override
    public void onStart(Bundle data) {
        configMapView();
        this.goTo((GeoPoint) this.mapView.getMapCenter());
        prefs.registerOnSharedPreferenceChangeListener(this.sharedPrefChangeListener);
    }

    @Override
    public void onDestroy(Bundle data) {
        prefs.unregisterOnSharedPreferenceChangeListener(this.sharedPrefChangeListener);
    }

    @Override
    public void onPause(Bundle data) {

    }
}
