package de.hof_universtiy.gpstracker.Controller.messenger;


public interface OnTaskCompleted {
    void onTaskCompleted();
    void addMessageToList(String Message, String Sender);
}
