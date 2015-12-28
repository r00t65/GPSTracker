package de.hof_universtiy.gpstracker.Controller.messenger;

import android.os.AsyncTask;

import org.apache.commons.lang3.StringUtils;
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
    private MessengerInterface listener;

    private boolean successfullConnected;

    public MessengerController(MessengerInterface listener){
        this.listener = listener;
        successfullConnected = true;
    }

    @Override
    protected Object doInBackground(Object[] params)  {

        try {
            buildConfiguration();
            establishConnection();
            joinMultiUserChat();
            addMessageListener();
        }
        catch (Exception e){
            successfullConnected = false;
        }

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

                int start =  message.getFrom().lastIndexOf("/");
                String name = message.getFrom().substring(start + 1);

                listener.addMessageToList(message.getBody(),name);
            }
        });

    }


    public void sendMessage(String message) {

        if(StringUtils.isNotBlank(message)) {

            try {
                chat.sendMessage(message);
                listener.clearTextField();
            }
            catch (Exception e) {
                listener.showCouldNotSendMessageToast();
            }
        }
        else{
            listener.showEmptyMessageToast();
        }
    }



    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(successfullConnected)
            listener.onTaskCompleted();
        else
            listener.showCouldNotConnectToChatToast();
    }

}
