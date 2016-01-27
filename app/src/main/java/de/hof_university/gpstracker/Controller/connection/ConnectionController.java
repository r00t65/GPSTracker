package de.hof_university.gpstracker.Controller.connection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.hof_university.gpstracker.Controller.listener.NotificationTrackListener;
import de.hof_university.gpstracker.Controller.listener.RadarListener;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.radar.FriendsPositionModel;
import de.hof_university.gpstracker.Model.track.Track;


/**
 * Created by Lothar Mödl on 19.11.15.
 */
public class ConnectionController implements NotificationTrackListener{

    private final String SERVER_URL = "https://aap.rt-dns.de/connection_db.php";
    private final String ID = "userID";
    private final String LONGITUDE = "lon";
    private final String LATITUDE = "lat";
    private final String LOG_TAG = ConnectionController.class.getSimpleName();

    private RadarListener radarController = null;
    private Context context = null;
    private String facebookId;

    private Location location;
    private List<FriendsPositionModel> position;

    // -----------------------
    // Constructors
    // ----------------------

    public ConnectionController(@NonNull final Context context , final RadarListener radarController){
        this.radarController = radarController;
        this.context = context;

        if(AccessToken.getCurrentAccessToken() != null) {
            FacebookSdk.sdkInitialize(context);

            try {
                facebookId = Profile.getCurrentProfile().getId();
            } catch (NullPointerException e) {
                Log.e("NullPointerException", "Not logged in or FacebookSdk initialization failed");
                throw new FacebookAuthorizationException();
            }
        }else {
            throw new FacebookAuthorizationException();
        }

    }

    public ConnectionController(String facebookId){
        this.facebookId = facebookId;
    }

    // -------------------------------------------------
    // Methode zum Abrufen der Positionen der Freunde
    // -------------------------------------------------

    public void getWaypointsOfFriends()
    {
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

                                    JSONObject object1 = new JSONObject();
                                    object1.put("func", "getFriends");
                                    //object1.put("userID",facebookId);
                                    object1.put("data", data);
                                    new HttpsAsyncTaskPosition().execute(SERVER_URL, "json=" + object1.toString(), "getWaypointsOfFriends");

                                } catch (JSONException e) { e.printStackTrace(); }
                            }
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            Log.d("Facebook_ERROR: ", "getUserFriendlistJSON ");
        }

    }

    // ------------------------------------------------------
    // Methoden für spätere Funktionen
    // --------------------------------------------------

    public void newUser(){
        String json = "json={\"func\":\"newUser\", \"userId\"" + facebookId + "\"}";
        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "newUser");
    }

    public void getTracks(String id){
        String json = "json={\"func\":\"getTrack\",\"userID\"" + facebookId + "\"}";

        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "getTracks");

    }

    public void shareTrack(String friendID, String trackID){
        String json = "json={\"func\":\"addShare\",\"userID\":\"" + facebookId + "\",\"friendID\":\"" + friendID + "\",\"trackID\":\"" + trackID + "\"}";

        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "shareTrack");

    }

    public void deleteShareTrack(String friendID, String trackID){
        String json = "json={\"func\":\"delShare\",\"userID\":\"" + facebookId + "\",\"friendID\":\"" + friendID + "\",\"trackID\":\"" + trackID + "\"}";

        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "deleteSharedTrack");

    }


    // ----------------------------------------------------------------
    // Methode zum hochladen der aktuellen Position des Benutzers
    // ----------------------------------------------------------------

    @Override
    public void newPosition(@NonNull Location location) {

        this.location = location;
        String json = "json={\"func\":\"setPosition\", \"userID\"" + facebookId + ",\"lat\":\"" + location.getLocation().getLatitude() + "\",\"lon\":\"" + location.getLocation().getLongitude() + "\"}";

        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "sendLastPosition");
    }


    // -------------------------------------
    // Methode zum hochladen eines Tracks
    // -------------------------------------


    @Override
    public void trackFinish(@NonNull Track track) {

        List<Location> trackList = track.getTracks();

        JSONObject object = new JSONObject();

        try {
            object.put("userID", facebookId);
            object.put("func", "addTrack");
            object.put("date", Calendar.getInstance().getTimeInMillis() + "");
            JSONArray array = new JSONArray();

            for(Location g : trackList){
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

        new HttpsAsyncTaskPosition().execute(SERVER_URL, "json=" + object.toString(), "sendTrack");
    }

    public boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    // ----------------------------------------------------------------
    // Private Klasse für den asyncronen Up- und Download der Daten
    // ----------------------------------------------------------------

    private class HttpsAsyncTaskPosition extends AsyncTask<String, Void, String>{

        private String action;


        // ---------------------------------------
        // Methode, zum Ausführen des AsyncTasks
        // ---------------------------------------

        @Override
        protected String doInBackground(String... params) {
            action = params[2];

            return receiveJsonData(params[0], params[1]);

        }


        // ----------------------------------------------------------
        // Methode, die nach dem Up- oder Download ausgeführt wird
        // ----------------------------------------------------------

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (action) {
                case "sendLastPosition":

                    break;
                case "getWaypointsOfFriends":
                    parsePosition(s);
                    radarController.setListOfFriends(null, position);

                    break;
            }

        }

        // ----------------------------------------
        // Sellt die Verbindung zum Server her
        // ----------------------------------------

        private String receiveJsonData(String urlString, String json) {
            String result = "";

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return  hv.verify("aap.rt-dns.de", session);
                }
            };


            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                }, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            HttpsURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);

                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(hostnameVerifier);

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");

                String debugJson = json + "&debug=true";

                outputStreamWriter.write(debugJson);
                outputStreamWriter.flush();
                outputStreamWriter.close();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf8"));

                String line = "";

                while ((line = in.readLine()) != null){
                    result += line;
                }

                in.close();
                is.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }


        // ----------------------------------------------
        // Json Parser für die Positionen der Freunde
        // ----------------------------------------------

        private void parsePosition(String json){
            position = new ArrayList<FriendsPositionModel>();

            try
            {

                JSONObject jObj = new JSONObject(json);
                int status = jObj.getInt("status");

                JSONArray jFriends = jObj.getJSONArray("data");

                for(int i=0;i<jFriends.length();i++)
                {
                    JSONObject jFriend = jFriends.getJSONObject(i);

                    String id =  jFriend.getString(ID);
                    Double latitude = jFriend.getDouble(LATITUDE);
                    Double longitude = jFriend.getDouble(LONGITUDE);

                    FriendsPositionModel positionModel = new FriendsPositionModel(id,latitude, longitude,new Date());

                    position.add(positionModel);
                }

            }

            catch(Exception e)
            {
                Log.e(LOG_TAG, "Fehler beim Parsen der Position: " + e.getMessage());
                e.printStackTrace();
            }

        }

    }


    // --------------------
    // Getter und Setter
    // --------------------

    public List<FriendsPositionModel> getPosition() {
        return position;
    }

    public void setPosition(List<FriendsPositionModel> position) {
        this.position = position;
    }

    @Override
    public void createTrack(@NonNull String name) {
        //Ignore
    }

    @Override
    public void newWayPoint(@NonNull Location location) throws Track.TrackFinishException {
        //Ignore
    }

}