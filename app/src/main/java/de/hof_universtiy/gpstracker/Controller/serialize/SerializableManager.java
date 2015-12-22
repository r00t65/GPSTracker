package de.hof_universtiy.gpstracker.Controller.serialize;

//**

/**
 * Created by Patrick Büttner on 02.12.2015.
 */

public class SerializableManager//TODO:Fehlerbehandlung
{
  /*  private final String LOG_TAG = ConnectionController.class.getSimpleName();
    private ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

    public void saveFile(ArrayList<?> msg)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath()+"/messages.bin"));

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(msg);
            oos.flush();
            oos.close();
            fos.close();
        }

        catch(Exception e)
        {
            Log.d(LOG_TAG, "Serialization Save Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<?> readFile(File f)
    {
        try
        {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            messages = (ArrayList) ois.readObject();
            ois.close();
            fis.close();

            return messages;
        }

        catch(Exception e)
        {
            Log.d(LOG_TAG, "Serialization Read Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }*/
}
