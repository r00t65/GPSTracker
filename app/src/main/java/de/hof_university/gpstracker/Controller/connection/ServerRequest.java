package de.hof_university.gpstracker.Controller.connection;

import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ServerRequest {

    ConnectionController main;

    public ServerRequest( ConnectionController main ){
        this.main = main;
    }

    public void request(String json){

        RequestRunnable requestRunner = new RequestRunnable( json );

        new Thread ( requestRunner ).start();

    }

    class RequestRunnable implements Runnable{

        String json = "";

        public RequestRunnable( String json ){
            this.json = json;
        }



        @Override
        public void run(){

            String data = "";
            BufferedReader reader=null;

            String text = "";

            try
            {
                data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(this.json, "UTF-8");
                data += "&" + URLEncoder.encode("debug", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
                URL url = new URL("http://aap.rt-dns.de/connection_db.php");
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                text = sb.toString();

            }
            catch (Exception ex){}
            finally
            {
                try { reader.close(); }
                catch (Exception ex){}
            }

            Message msg = Message.obtain();
            msg.obj = text;
            main._handler.sendMessage(msg);

        }
    }

}
