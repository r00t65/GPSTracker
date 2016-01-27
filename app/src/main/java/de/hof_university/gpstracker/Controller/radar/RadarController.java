package de.hof_university.gpstracker.Controller.radar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
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
import java.util.Date;
import java.util.List;

import de.hof_university.gpstracker.Model.mapoverlays.FriendMapOverlay;
import de.hof_university.gpstracker.Model.mapoverlays.MyPositionMapOverlay;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.radar.FriendsPositionModel;
import de.hof_university.gpstracker.R;

/**
 * Verwaltet das Radar
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

    /**
     * Listener-methode, welche vom ConnectionController aufgerufen wird
     * @param myPosition
     * @param friendList
     */
    @Override
    public void setListOfFriends(final Location myPosition, @NonNull final List<FriendsPositionModel> friendList) {
        this.clearRadar();
        // this.drawMyPosition(myPosition);
        this.drawFriends(friendList);
    }

    /**
     * Gibt de zeichenen-Befehl an die Methode drawFriend
     * @param friendList
     */
    private void drawFriends(@NonNull final List<FriendsPositionModel> friendList) {
        for (final FriendsPositionModel friend : friendList)
            drawFriend(friend);
    }

    /**
     * Zeichnet den einzelnen Freund
     * @param friend
     */
    private void drawFriend(@NonNull final FriendsPositionModel friend) {
        //Toast.makeText(this.context, friend.getLocation().getLocation().toString(), Toast.LENGTH_LONG).show();

        final FriendMapOverlay mapPoint = new FriendMapOverlay(this.context, friend);
        this.radarView.getOverlayManager().add(mapPoint);
        radarIsInvalidate();
        radarIsInvalidate();
    }

    /**
     * Verschiebt Radar auf aktuelle Position. Aktuell ein festgelegter Wert definiert
     * @throws SecurityException
     */
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
        this.mLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.person));
        this.getProfileImage();

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

    /**
     * Zeichnet das Radar neu
     */
    private void radarIsInvalidate() {
        this.radarView.invalidate();
    }

    /**
     * Entfernt alle Overlays auf der Karte
     */
    private void clearRadar() {
        this.radarView.getOverlays().clear();

        this.mLocationOverlay = new MyLocationNewOverlay(context, new GpsMyLocationProvider(context), this.radarView);
        this.radarView.getOverlays().add(this.mLocationOverlay);
        this.mLocationOverlay.enableFollowLocation();
        //this.mLocationOverlay.enableMyLocation();
        //this.mLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.person));

        this.radarView.invalidate();
    }

    /**
     * Lädt das Profilbild des eigenen Facebookbild
     *
     * Ist noch Fehleranfällig! Zusammenarbeit mit WP3 und WP5
     */
    public void getProfileImage(){
        if (AccessToken.getCurrentAccessToken() != null){
            Profile profile = Profile.getCurrentProfile();
            Uri uri = profile.getProfilePictureUri(50, 50);

            new DownloadImageTask().execute(uri.toString());
        }
    }

    /**
     * Task für das Laden des Profilbildes
     */
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
            clearRadar();
            radarView.invalidate();
        }
    }
}