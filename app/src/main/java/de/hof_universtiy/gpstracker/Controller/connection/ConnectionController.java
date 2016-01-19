package de.hof_universtiy.gpstracker.Controller.connection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookSdk;
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
import java.net.MalformedURLException;
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

import de.hof_universtiy.gpstracker.Controller.listener.NotificationTrackListener;
import de.hof_universtiy.gpstracker.Controller.listener.RadarListener;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import de.hof_universtiy.gpstracker.Model.radar.FriendsPositionModel;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.Model.track.Track;

/**
 * Created by Lothar Mödl on 19.11.15.
 */
public class ConnectionController implements NotificationTrackListener{

    private final String SERVER_URL = "https://aap.rt-dns.de/connection_db.php";
    private final String FRIENDS_NEARBY = "getFriends";
    private final String ID = "userID";
    private final String LONGITUDE = "lon";
    private final String LATITUDE = "lat";
    private final String LOG_TAG = ConnectionController.class.getSimpleName();

    private  RadarListener radarController = null;
    private Context context = null;
    private String facebookId;

    private Location location;
    private String receivedJson;
    private List<FriendsPositionModel> position;

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

    public void getWaypointsOfFriends()
    {
        String jsonToSend = "json={\"func\":\"getFriends\",\"userID\":\"" + this.facebookId + "\"}";
        JSONObject object = new JSONObject();
        try {
            object.put("func", "getFriends");
            object.put("userID",  this.facebookId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            new HttpsAsyncTaskPosition().execute(SERVER_URL, jsonToSend, "getWaypointsOfFriends");

    }


    public void newUser(){
        String json = "json={\"func\":\"newUser\", \"userId\"" + facebookId + "\"}";
        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "newUser");
    }

    public void getTracks(String id){
        String json = "json={\"func\":\"getTrack\",\"userID\"" + id + "\"}";

            new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "getTracks");


    }

    public void shareTrack(String userID, String friendID, String trackID){
        String json = "json={\"func\":\"addShare\",\"userID\":\"" + userID + "\",\"friendID\":\"" + friendID + "\",\"trackID\":\"" + trackID + "\"}";

            new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "shareTrack");

    }

    public void deleteShareTrack(String userID, String friendID, String trackID){
        String json = "json={\"func\":\"delShare\",\"userID\":\"" + userID + "\",\"friendID\":\"" + friendID + "\",\"trackID\":\"" + trackID + "\"}";


            new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "deleteSharedTrack");

    }


    @Override
    public void newPosition(@NonNull Location location) {

        this.location = location;
        String json = "json={\"func\":\"setPosition\", \"userID\"" + facebookId + ",\"lat\":\"" + location.getLocation().getLatitude() + "\",\"lon\":\"" + location.getLocation().getLongitude() + "\"}";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("id", facebookId);
            jsonObject.accumulate("latitude", location.getLocation().getLatitude());
            jsonObject.accumulate("longitude", location.getLocation().getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //json = jsonObject.toString();

        new HttpsAsyncTaskPosition().execute(SERVER_URL, json, "sendLastPosition");
    }

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
           // if(json != null) {

                Log.d("json to send", json);

                urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
                //urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlConnection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");

            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            //if(!url.getHost().equals(urlConnection.getURL().getHost())){

            BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf8"));

            String line = "";

            while ((line = in.readLine()) != null){
                result += line;
            }

            in.close();
            // }

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


    public boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }



    private void parsePosition(String json){
        position = new ArrayList<FriendsPositionModel>();

        try
        {

            JSONObject jObj = new JSONObject(json);
            int status = jObj.getInt("status");

            Log.d("status Code", "" + status);

            JSONArray jFriends = jObj.getJSONArray(FRIENDS_NEARBY);

            for(int i=0;i<jFriends.length();i++)
            {
                JSONObject jFriend = jFriends.getJSONObject(i);

                String id =  jFriend.getString(ID);
                Double latitude = jFriend.getDouble(LATITUDE);
                Double longitude = jFriend.getDouble(LONGITUDE);

                FriendsPositionModel positionModel = new FriendsPositionModel(id,latitude, longitude,new Date());

                position.add(positionModel);
            }

            Log.d("LASTPOSITION", position.get(0).toString());
        }

        catch(Exception e)
        {
            Log.e(LOG_TAG, "Fehler beim Parsen der Position: " + e.getMessage());
            e.printStackTrace();
        }

    }



    private class HttpsAsyncTaskPosition extends AsyncTask<String, Void, String>{

        private String action;

        @Override
        protected String doInBackground(String... params) {
            action = params[2];

            return receiveJsonData(params[0], params[1]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            receivedJson = s;

            switch (action) {
                case "sendLastPosition":

                    break;
                case "sendTrack":
                    StorageController storageController = new StorageController(context);


                    break;
                case "getWaypointsOfFriends":
                    parsePosition(s);
                   // if(radarController != null)
                    radarController.setListOfFriends(null,position);

                    break;
            }




        }
    }

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
