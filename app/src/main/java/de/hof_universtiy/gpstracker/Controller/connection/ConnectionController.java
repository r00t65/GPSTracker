package de.hof_universtiy.gpstracker.Controller.connection;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.hof_universtiy.gpstracker.Model.messages.MessageModel;
import de.hof_universtiy.gpstracker.Model.position.PositionModel;

/**
 * Created by Lothar Mödl on 19.11.15.
 */
public class ConnectionController{

    private static final String URL_SEND_LAST_WAYPOINT = "";
    private static final String URL_GET_WAYPOINTS_OF_FRIENDS = "https://aap.rt-dns.de/getFriends.php";
    private static final String URL_GET_MESSAGES = "https://aap.rt-dns.de/getMessage.php";

    final String FRIENDS_NEARBY = "getFriends";
    final String ID = "userID";
    final String LONGITUDE = "lon";
    final String LATITUDE = "lat";

    final String MESSAGE = "Message";
    final String TIMESTAMP = "timestamp";
    final String SENDER = "from";
    final String INFO = "message";

    private String LOG_TAG = ConnectionController.class.getSimpleName();

    private String receivedJson;

    private List<MessageModel> messages;
    private List<PositionModel> position;

    public void sendLastWaypoint(String id, GeoPoint geoPoint){

        String json = "json={\"func\":\"setPosition\", \"id\"" + id + ",\"lat\":\"" + geoPoint.getLatitude() + "\",\"lon\":\"" + geoPoint.getLongitude() + "\"}";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("latitude", geoPoint.getLatitude());
            jsonObject.accumulate("longitude", geoPoint.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //json = jsonObject.toString();
        receiveJsonData(URL_SEND_LAST_WAYPOINT, json);
    }

    public void getMessages(String id){

        new HttpsAsyncTaskMessages().execute(URL_GET_MESSAGES, id);


    }

    public void getWaypointsOfFriends(String id)
    {
        String jsonToSend = "json={\"func\":\"getFriends\",\"userID\":\"" + id + "\"}";
        new HttpsAsyncTaskPosition().execute(URL_GET_WAYPOINTS_OF_FRIENDS, jsonToSend);
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
            urlConnection.setDoInput(true);


           // if(json != null) {

                Log.d("json to send", json);

                urlConnection.setDoOutput(true);
                //urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");
                //urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();


                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os, "utf8"));

                out.write(json);
                out.flush();
                os.close();
                out.close();


           // }

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


    public boolean isConnected(Activity activity){
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }



    private void parsePosition(String json){
        position = new ArrayList<PositionModel>();

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

                PositionModel positionModel = new PositionModel(id,latitude, longitude);

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

        @Override
        protected String doInBackground(String... params) {

            return receiveJsonData(params[0], params[1]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            receivedJson = s;

            Log.d(LOG_TAG, s);

            parsePosition(s);

        }
    }






























    private class HttpsAsyncTaskMessages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return receiveJsonData(params[0], params[1]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            receivedJson = s;

            messages = new ArrayList<MessageModel>();
            try {
                JSONObject jObj = new JSONObject(s);
                JSONArray jMessages = jObj.getJSONArray(MESSAGE);

                for (int i = 0; i < jMessages.length(); i++) {
                    JSONObject jMessage = jMessages.getJSONObject(i);

                    String sender = jMessage.getString(SENDER);
                    String message = jMessage.getString(INFO);
                    String timestamp = jMessage.getString(TIMESTAMP);

                    MessageModel msgModel = new MessageModel(sender, message, timestamp);

                    messages.add(msgModel);

                    Log.d(LOG_TAG, "Messages:" + receivedJson);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Fehler beim Parsen der Messages: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }


    public List<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }

    public List<PositionModel> getPosition() {
        return position;
    }

    public void setPosition(List<PositionModel> position) {
        this.position = position;
    }
}
