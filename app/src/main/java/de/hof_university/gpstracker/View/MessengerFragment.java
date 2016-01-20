package de.hof_university.gpstracker.View;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hof_university.gpstracker.Controller.facebook.FbConnector;
import de.hof_university.gpstracker.Controller.messenger.MessengerController;
import de.hof_university.gpstracker.Controller.messenger.MessengerInterface;
import de.hof_university.gpstracker.R;


public class MessengerFragment extends Fragment implements MessengerInterface, View.OnClickListener {

    Class fragmentClass;
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

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);


        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        //internet
        if (false) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Bitte Internet aktivieren");

            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aktivieren",
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


        //checkt ob Nutzer eingeloggt ist falls nicht wird er zum login geleitet
        facebookConnector = new FbConnector();
        if (!facebookConnector.isLoggedIn()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Bitte Anmelden");
            alertDialog.setMessage("Um den Messenger nutzen zu können, müssen Sie sich bitte anmelden");
            alertDialog.setCancelable(false);
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
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fragmentClass = GPSTrackerFragment.class;
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
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
        Toast.makeText(getContext(), getResources().getText(R.string.messenger_emptyText), Toast.LENGTH_SHORT).show();
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


//--------------------------------------------------------------------------------------------------------------------------

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
        hideKeyboard(getContext());
        try {
            ctrl.cancel(true);
        } catch (Exception e) {
        }
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
