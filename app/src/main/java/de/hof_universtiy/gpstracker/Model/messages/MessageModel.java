package de.hof_universtiy.gpstracker.Model.messages;

import de.hof_universtiy.gpstracker.Model.Model;

/**
 * Created by Patrick Büttner on 29.11.2015.
 */
public class MessageModel extends Model
{
    private String sender;
    private String timestamp;
    private String message;

    public MessageModel()
    {
    }

    public MessageModel(String sender, String timestamp, String message)
    {
        this.sender = sender;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "sender='" + sender + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public String getID() {
        return null;
    }
}
