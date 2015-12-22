package de.hof_universtiy.gpstracker.Controller.messenger;

import android.os.AsyncTask;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;


public class MessengerController extends AsyncTask {

    private XMPPTCPConnectionConfiguration.Builder configuration;
    private AbstractXMPPConnection connection;
    private MultiUserChatManager manager;
    private MultiUserChat chat;
    private OnTaskCompleted listener;


    public MessengerController(OnTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] params)  {

        try {
            buildConfiguration();
            establishConnection();
            joinMultiUserChat();
            addMessageListener();
        }
        catch (Exception e){}


        return null;
    }



    private void buildConfiguration() throws Exception{
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

    }


    private void joinMultiUserChat() throws Exception{

        manager = MultiUserChatManager.getInstanceFor(connection);

        DiscussionHistory chatHistory = new DiscussionHistory();
        chatHistory.setMaxStanzas(20);

        chat = manager.getMultiUserChat("16920719731286378643@chat.mvp.avinotec.de");

        //ToDo: statt "nickname" Facebook-Nutzernamen einfügen
        chat.join("nickname", "hochschulehof", chatHistory, SmackConfiguration.getDefaultPacketReplyTimeout());

    }


    public void addMessageListener() throws Exception{

        chat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {

                listener.addMessageToList(message.getBody(),message.getFrom());
            }
        });

    }


    public void sendMessage(String message) {
        try {
            chat.sendMessage(message);
        }
        catch (Exception e){}
    }



    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        listener.onTaskCompleted();
    }

}
