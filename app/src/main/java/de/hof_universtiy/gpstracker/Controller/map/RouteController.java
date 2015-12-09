package de.hof_universtiy.gpstracker.Controller.map;

import android.os.Bundle;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerActivity;
import de.hof_universtiy.gpstracker.Controller.abstractClasses.ControllerActivityInterface;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 19.11.15.
 */
public class RouteController extends ControllerActivity/*,AddRouteListener*/ {

   // private final RoadManager roadManager = new OSRMRoadManager();
    private final List<GeoPoint> path = new ArrayList<>();
    private final MapController mapController;

    public RouteController(MapController mapController) {
        this.mapController = mapController;
    }


    /*@Override
    public void addGeoPoint(GeoPoint geoPoint) {
        this.path.add(geoPoint);
    }

    @Override
    public void clearPath() {
        this.path.clear();
    }

    @Override
    public List<GeoPoint> getPath() {
        return this.path;
    }

    public Road getRoad(){
        return roadManager.getRoad((ArrayList<GeoPoint>) this.path);
    }

    public Polyline getPolylineRoad(){
        return RoadManager.buildRoadOverlay( getRoad(), this.mapController.getContext());
    }

   */
}
