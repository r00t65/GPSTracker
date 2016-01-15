package de.hof_universtiy.gpstracker.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hof_universtiy.gpstracker.Controller.facebook.FbConnector;
import de.hof_universtiy.gpstracker.Controller.messenger.MessengerController;
import de.hof_universtiy.gpstracker.Controller.messenger.MessengerInterface;
import de.hof_universtiy.gpstracker.R;


public class MessengerFragment extends Fragment implements MessengerInterface, View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private EditText messageField;
    private ListView messageList;
    private Button sendButton;
    private ImageButton reloadButton;

    private MessengerController ctrl;
    private ArrayAdapter<String> messageAdapter;
    private ArrayList<String> messageArrayList;

    private FbConnector facebookConnector;

    private Fragment fragment = null;
    Class fragmentClass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //checkt ob Nutzer eingeloggt ist falls nicht wird er zum login geleitet
        facebookConnector = new FbConnector();
        if(!facebookConnector.isLoggedIn()){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Bitte Anmelden");
            alertDialog.setMessage("Um den Messenger zu verwenden bitte anmelden.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Anmelden",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentClass = LoginLogoutFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();

                            dialog.dismiss();


                        }
                    });
            alertDialog.show();
        }

        getActivity().setTitle("Messenger");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messenger, container, false);

        messageField = (EditText) view.findViewById(R.id.messageField);
        messageList = (ListView) view.findViewById(R.id.messagesList);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        reloadButton = (ImageButton) view.findViewById(R.id.reloadButton);

        sendButton.setOnClickListener(this);
        reloadButton.setOnClickListener(this);
        reloadButton.setEnabled(false);

        messageAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        messageArrayList = new ArrayList<>();

        ctrl = new MessengerController(MessengerFragment.this);
        ctrl.execute();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                String message = messageField.getText().toString();
                ctrl.sendMessage(message);
                break;

            case R.id.reloadButton:
                disableAllButtons();

                messageAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
                messageArrayList = new ArrayList<>();

                ctrl.disconnect();
                ctrl = new MessengerController(MessengerFragment.this);
                ctrl.execute();
                break;

            default:
                break;
        }
    }


    @Override
    public void enableSendButton() {
        sendButton.setEnabled(true);
    }

    @Override
    public void enableReloadButton() {
        reloadButton.setEnabled(true);
    }

    @Override
    public void disableAllButtons() {
        reloadButton.setEnabled(false);
        sendButton.setEnabled(false);
    }

    @Override
    public void clearTextField() {
        messageField.setText("");
    }

    @Override
    public void showEmptyMessageToast() {
        Toast.makeText(getContext(), "Not possible to send empty message!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCouldNotSendMessageToast() {
        Toast.makeText(getContext(), "Error occured while sending message; check your internet connection and try to reload chat", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCouldNotConnectToChatToast() {
        Toast.makeText(getContext(), "Error occured while connecting to chat; check your internet connection and try to reload chat", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessfullyConnectedToast() {
        Toast.makeText(getContext(), "Successfully connected to chat", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addMessageToList(final String message, final String sender) {

        messageList.post(new Runnable() {
            @Override
            public void run() {

                messageArrayList.clear();

                messageArrayList.add(sender + ": " + message);
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