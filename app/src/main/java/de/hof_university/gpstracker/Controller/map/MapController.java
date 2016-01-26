package de.hof_university.gpstracker.Controller.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.facebook.AccessToken;
import com.facebook.Profile;
import de.hof_university.gpstracker.Controller.listener.GPSMapChangeListener;
import de.hof_university.gpstracker.Controller.listener.LoadTrackListener;
import de.hof_university.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_university.gpstracker.Model.mapoverlays.RouteMapOverlay;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.track.Track;
import de.hof_university.gpstracker.R;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

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
    private MyLocationNewOverlay mLocationOverlay;

    public MapController(final Context context, final MapView mapView) {
        activityContext = context;
        this.mapView = mapView;
        this.gpsChangeListenerMap = new GPSChangeListenerMap();
        configMapView();
    }

    @Override
    public void showMyPosition() throws SecurityException {
        this.mLocationOverlay = new MyLocationNewOverlay(activityContext, new GpsMyLocationProvider(activityContext), this.mapView);
        this.mapView.getOverlays().add(this.mLocationOverlay);
        this.mLocationOverlay.enableFollowLocation();
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.person));
    }

    private void standardView() throws SecurityException {
        this.mapView.getController().setZoom(20);
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
        standardView();
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

    private void drawTrack(@NonNull final Track track) {
        //RoadManager roadManager = new OSRMRoadManager();
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        for (Location location : track.getTracks()) {
            geoPoints.add(new GeoPoint(location.getLocation()));
        }
        //Road road = roadManager.getRoad(geoPoints);
        Polyline roadOverlay = new Polyline(this.activityContext);

        roadOverlay.setPoints(geoPoints);
        roadOverlay.setVisible(true);
        roadOverlay.setWidth(4);

        mapView.getOverlays().add(new RouteMapOverlay(activityContext,track));

        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }

    private void clearMap() {
        this.mapView.getOverlays().clear();
        this.showMyPosition();
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
            //mPosition = location;
            //drawPosition(location);
            /*if (mTrack != null)
                drawTrack(mTrack);*/
        }

        @Override
        public void updateTrack(@NonNull Track track) {
            clearMap();
            //mTrack = track;
            drawTrack(track);
            /*if (mPosition != null)
                drawPosition(mPosition);
        */}
    }

    public void getProfileImage(){
        if (AccessToken.getCurrentAccessToken() != null){
            Profile profile = Profile.getCurrentProfile();
            Uri uri = profile.getProfilePictureUri(50, 50);

            new DownloadImageTask().execute(uri.toString());
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap menuImage = null;

            try {
                URL url = new URL(params[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();

                menuImage = BitmapFactory.decodeStream(inputStream);


                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return menuImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mLocationOverlay.setPersonIcon(bitmap);
        }
    }
}
