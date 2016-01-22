package de.hof_university.gpstracker.Controller.connection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hof_university.gpstracker.Controller.facebook.FbConnector;
import de.hof_university.gpstracker.Controller.listener.NotificationTrackListener;
import de.hof_university.gpstracker.Controller.listener.RadarListener;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.radar.FriendsPositionModel;
import de.hof_university.gpstracker.Model.track.Track;

/**
 * Created by Lothar Mödl on 19.11.15.
 * Updated by Patrick Rüpplein on 21.01.16
 */
public class ConnectionController implements NotificationTrackListener {

    private final String ID = "userID";
    private final String LONGITUDE = "lon";
    private final String LATITUDE = "lat";
    private final String LOG_TAG = ConnectionController.class.getSimpleName();
    private RadarListener radarController = null;

    private Context context = null;
    private String facebookId = "000000";

    private List<FriendsPositionModel> position;
    ServerRequest serverRequest;

    FbConnector fbConnector;

    // - - - - - - - - - -
    // Konstanten
    // - - - - - - - - - -

    private final String SERVER_URL = "http://aap.rt-dns.de/connection_db.php";

    // Server Funktionen
    private final String ADD_SHARE = "addShare";
    private final String DEL_SHARE = "delShare";
    private final String GET_FRIENDS = "getFriends";
    private final String GET_TRACK = "getTrack";
    private final String NEW_USER = "newUser";
    private final String SET_POSITION = "setPosition";


    public ConnectionController(@NonNull final Context context, final RadarListener radarController) {
        this.radarController = radarController;
        this.context = context;

        this.serverRequest = new ServerRequest(this, SERVER_URL);
        this.fbConnector = new FbConnector();

        if (fbConnector.isLoggedIn()) {
            facebookId = fbConnector.getUserId();
        }
    }

    public ConnectionController(String facebookId) {

        serverRequest = new ServerRequest(this, SERVER_URL);
        this.facebookId = facebookId;
    }


    // - - - - - - - - - -
    // Server Verbindung
    // - - - - - - - - - -

    // Freundeposition von Server holen

    public void getWaypointsOfFriends() {
//        newPosition(new Location(50.298941, 11.968903,new Date())); // -- for debuging purpose

        try {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            if(response.getJSONObject() != null) {
                                try {
                                    JSONArray friends = response.getJSONObject().getJSONArray("data");
                                    JSONArray data = new JSONArray();

                                    for (int i = 0; i < friends.length(); i++){
                                        JSONObject friend = new JSONObject();
                                        friend.put("id", friends.getJSONObject(i).getString("id"));

                                        data.put(friend);
                                    }
                                    serverRequest.request(GET_FRIENDS, data);

                                } catch (JSONException e) { e.printStackTrace(); }
                            }
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            Log.d("Facebook_ERROR: ", "getUserFriendlistJSON ");
        }
    }

    // Position an Server senden

    @Override
    public void newPosition(@NonNull Location location) {
        android.location.Location loc = location.getLocation();
        serverRequest.request(SET_POSITION, facebookId, loc.getLatitude(), loc.getLongitude());
    }


    // - - - - - - - - - -
    // Lokale Methoden
    // - - - - - - - - - -

    // Freunde Position parsen

    private void parsePosition(JSONArray jFriends) {
        position = new ArrayList<>();

        try {

            for (int i = 0; i < jFriends.length(); i++) {

                JSONObject jFriend = jFriends.getJSONObject(i);

                String id = jFriend.getString(ID);
                Double latitude = jFriend.getDouble(LATITUDE);
                Double longitude = jFriend.getDouble(LONGITUDE);

                position.add(new FriendsPositionModel(id, latitude, longitude, new Date()));
            }

            Log.d("LASTPOSITION", position.get(0).toString());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Fehler beim Parsen der Position: " + e.getMessage());
            e.printStackTrace();
        }

    }


    // - - - - - - - - - -
    // Getter und Setter
    // - - - - - - - - - -

    // Freunde Positions Liste

    public List<FriendsPositionModel> getPosition() {
        return position;
    }

    public void setPosition(List<FriendsPositionModel> position) {
        this.position = position;
    }


    // - - - - - - - - - -
    // Handler zum Empfangen der Server-Daten
    // - - - - - - - - - -

    @SuppressLint("HandlerLeak")
    public Handler _handler = new Handler() {
        @Override public void handleMessage(Message msg) {

            System.out.println("ServerResponse recieved data: "+msg.obj);

            try {

                JSONObject reader = new JSONObject((String)msg.obj);

                switch(reader.getInt("status")){
                    case 100:
                        System.out.println("ServerResponse for: "+reader.getString("func")+" - Status: "+reader.getInt("status"));
                        switch (reader.getString("func")) {

                            case "getFriends":
                                parsePosition(reader.getJSONArray("data"));
                                radarController.setListOfFriends(null, position);
                                break;

                        }
                        break;

                    case 210:

                        System.out.println("ServerRequest DB Error in: "+reader.getString("func")+" - Status: "+reader.getInt("status"));
                        System.out.println("DB Error Message: "+reader.getString("debug"));
                        break;

                    default:

                        System.out.println("ServerRequest Error: "+reader.getString("func")+" - Status: "+reader.getInt("status"));
                        break;
                }

            } catch (JSONException e) { e.printStackTrace(); }
            super.handleMessage(msg);
        }
    };


    // TODO: Methoden checken

    public boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void newUser() { serverRequest.request(NEW_USER, facebookId); }

    public void getTracks(String id) { serverRequest.request(GET_TRACK, facebookId); }

    // TODO: Warum UserID ? UserID = facebookID
    public void shareTrack(String userID, String friendID, String trackID) { serverRequest.request(ADD_SHARE, facebookId, friendID, trackID ); }

    // TODO: Warum UserID ? UserID = facebookID
    public void deleteShareTrack(String userID, String friendID, String trackID) { serverRequest.request(DEL_SHARE, facebookId, friendID, trackID ); }

    @Override
    public void trackFinish(@NonNull Track track) {

        List<Location> trackList = track.getTracks();

        JSONObject object = new JSONObject();

        try {
            object.put("userID", facebookId);
            object.put("func", "addTrack");
            object.put("date", Calendar.getInstance().getTimeInMillis() + "");
            JSONArray array = new JSONArray();

            for (Location g : trackList) {
                JSONObject o = new JSONObject();
                o.put("lat", "" + g.getLocation().getLatitude());
                o.put("lon", "" + g.getLocation().getLongitude());
                o.put("date", g.getDate().toString());
                //o.put("alt", g.getAltitude() + "");
                //o.put("speed", g.getSpeed() + "");
                //o.put("slant", g.getSlant() + "");
                array.put(o);
            }

            object.put("track", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        serverRequest.request(object.toString());
    }


    @Override
    public void createTrack(@NonNull String name) {
        //Ignore
    }

    @Override
    public void newWayPoint(@NonNull Location location) throws Track.TrackFinishException {
        //Ignore
    }


    // - - - - - - - - - -
    // Obsolete
    // - - - - - - - - - -

    // TODO: probably obsolete

//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            receivedJson = s;
//
//            switch (action) {
//
//                case "sendTrack":
//                    StorageController storageController = new StorageController(context);
//
//                    break;
//            }
//
//
//        }

}