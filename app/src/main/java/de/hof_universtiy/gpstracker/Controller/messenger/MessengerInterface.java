package de.hof_universtiy.gpstracker.Controller.messenger;


public interface MessengerInterface {
    void onTaskCompleted();
    void addMessageToList(String Message, String Sender);
    void clearTextField();
    void showEmptyMessageToast();
    void showCouldNotSendMessageToast();
    void showCouldNotConnectToChatToast();

}
