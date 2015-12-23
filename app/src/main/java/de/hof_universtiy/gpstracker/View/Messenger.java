package de.hof_universtiy.gpstracker.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import de.hof_universtiy.gpstracker.Controller.messenger.MessengerController;
import de.hof_universtiy.gpstracker.Controller.messenger.OnTaskCompleted;
import de.hof_universtiy.gpstracker.R;


public class Messenger extends Fragment implements OnTaskCompleted{

    private OnFragmentInteractionListener mListener;

    private EditText messageField;
    private ListView messageList;
    private Button sendButton;

    private MessengerController ctrl;
    private ArrayAdapter<String> messageAdapter;
    private ArrayList<String> messageArrayList;


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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageField.getText().toString();

                if (StringUtils.isNotBlank(message)) {
                    ctrl.sendMessage(message);
                    messageField.setText("");
                }
                else
                    Toast.makeText(getContext(),"Not possible to send empty message!",Toast.LENGTH_SHORT).show();

            }
        });

        messageAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);
        messageArrayList = new ArrayList<>();

        ctrl = new MessengerController(Messenger.this);
        ctrl.execute();


        return view;
    }



    @Override
    public void onTaskCompleted() {
        sendButton.setEnabled(true);
    }

    @Override
    public void addMessageToList(final String Message, final String Sender) {

        messageList.post(new Runnable() {
            @Override
            public void run() {

                messageArrayList.clear();

                int start =  Sender.lastIndexOf("/");
                String name = Sender.substring(start + 1);

                messageArrayList.add(name + ": " + Message);
                messageAdapter.addAll(messageArrayList);
                messageAdapter.notifyDataSetChanged();

                messageList.setAdapter(messageAdapter);
                messageList.setSelection(messageAdapter.getCount() - 1);

            }
        });


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
