package de.hof_universtiy.gpstracker.Controller.messenger;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class MessengerController extends AsyncTask {

    private XMPPTCPConnectionConfiguration.Builder configuration;
    private AbstractXMPPConnection connection;

    @Override
    protected Object doInBackground(Object[] params) {

        buildConfiguration();

        try {
            establishConnection();
            joinMultiUserChat();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    private void buildConfiguration() {
        configuration = XMPPTCPConnectionConfiguration.builder();
        configuration.setUsernameAndPassword("speedhammerapp", "hochschulehof")
                .setServiceName("chat.mvp.avinotec.de")
                .setHost("mvp.avinotec.de")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(5222)
                .setResource(System.nanoTime() + "")
                .build();
    }

    private void establishConnection() throws  Exception{

        connection = new XMPPTCPConnection(configuration.build());

        connection.connect();
        connection.login();

        Log.e(connection.getUser(),connection.getServiceName());
        Log.e(connection.getConnectionCounter() + "",connection.getHost());

    }


    private void joinMultiUserChat() throws Exception{

    }

}
