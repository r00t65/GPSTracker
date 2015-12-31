package de.hof_universtiy.gpstracker.Controller.messenger;


public interface MessengerInterface {
    void enableSendButton();
    void enableReloadButton();
    void disableAllButtons();
    void addMessageToList(String Message, String Sender);
    void clearTextField();
    void showEmptyMessageToast();
    void showCouldNotSendMessageToast();
    void showCouldNotConnectToChatToast();
    void showSuccessfullyConnectedToast();

}
