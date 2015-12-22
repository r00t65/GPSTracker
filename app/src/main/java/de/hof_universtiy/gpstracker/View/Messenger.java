package de.hof_universtiy.gpstracker.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.hof_universtiy.gpstracker.Controller.messenger.MessengerController;
import de.hof_universtiy.gpstracker.R;


public class Messenger extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText messageField;
    private ListView messageList;
    private Button sendButton;

    private MessengerController ctrl;
    private ArrayAdapter<String> messageAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messenger, container, false);

        messageField = (EditText) view.findViewById(R.id.messageField);
        messageList = (ListView) view.findViewById(R.id.messagesList);
        sendButton = (Button) view.findViewById(R.id.sendButton);

        messageAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);

        Log.e("start","start");
        ctrl = new MessengerController();
       // ctrl.execute();

        return view;
    }

//--------------------------------------------------------------------------------------------------------------------------

   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
