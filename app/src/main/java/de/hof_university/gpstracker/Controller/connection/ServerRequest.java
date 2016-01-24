package de.hof_university.gpstracker.Controller.connection;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ServerRequest {

    ConnectionController main;
    String serverUrl;

    public ServerRequest(ConnectionController main, String serverUrl) {
        this.main = main;
        this.serverUrl = serverUrl;
    }

    public void request(String json) {

        RequestRunnable requestRunner = new RequestRunnable(json);

        new Thread(requestRunner).start();

    }

    public void request(String func, JSONArray data) {
        JSONObject request = new JSONObject();
        try {
            request.put("func", func);
            request.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request(request.toString());
    }

<<<<<<< HEAD
    public void request(String func, String userID) {
        JSONObject request = new JSONObject();
        try {
            request.put("func", func);
            request.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request(request.toString());
    }

    public void request(String func, String userID, double latitude, double longitude) {
=======
    public void request(String func, String userID, double latitude, double longitude ) {
>>>>>>> parent of 74a6b27... - clean up
        JSONObject request = new JSONObject();
        try {
            request.put("func", func);
            request.put("userID", userID);
            request.put("lat", latitude);
            request.put("lon", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request(request.toString());
    }

<<<<<<< HEAD
    public void request(String func, String userID, String friendID, String trackID) {
        JSONObject request = new JSONObject();
        try {
            request.put("func", func);
            request.put("userID", userID);
            request.put("friendID", friendID);
            request.put("trackID", trackID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request(request.toString());
    }

    class RequestRunnable implements Runnable {
=======
    class RequestRunnable implements Runnable{
>>>>>>> parent of 74a6b27... - clean up

        String json = "";

        public RequestRunnable(String json) {
            this.json = json;
        }


        @Override
        public void run() {

            BufferedReader reader = null;
            String text = "";

            try {
                String data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(this.json, "UTF-8");
                data += "&" + URLEncoder.encode("debug", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");

                URL url = new URL(serverUrl);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                text = sb.toString();
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                try {
                    assert reader != null;
                    reader.close();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            Message msg = Message.obtain();
            msg.obj = text;
            main._handler.sendMessage(msg);

        }
    }

}
