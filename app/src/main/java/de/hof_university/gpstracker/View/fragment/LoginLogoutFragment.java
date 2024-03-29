package de.hof_university.gpstracker.View.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Arrays;
import java.util.List;

import de.hof_university.gpstracker.Controller.facebook.FbConnector;
import de.hof_university.gpstracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginLogoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginLogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginLogoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CallbackManager callbackManager;
    Class fragmentClass;
    ProfilePictureView profilePictureView = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Fragment fragment = null;
    private FbConnector facebookConnector;


    public LoginLogoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginLogout.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginLogoutFragment newInstance(String param1, String param2) {
        LoginLogoutFragment fragment = new LoginLogoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("Login/Logout");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Facebook
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_login_logout, container, false);
        facebookConnector = new FbConnector();
        if (facebookConnector.isLoggedIn()) {
            profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePic);


            profilePictureView.setProfileId(facebookConnector.getUserId());

        }

        // Create Userpermissions
        List<String> facebookPermissions = Arrays.asList("email", "public_profile", "user_friends");

        // Initialize Loginbutton
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(facebookPermissions);

        // If using in a fragment
        loginButton.setFragment(this);

        //internet
        if (false) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(getResources().getText(R.string.LoginLogout_alertDialogInternet));

            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getText(R.string.LoginLogout_alertDialogActivate),
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

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("onSucess", "onSuccess ");

                profilePictureView = (ProfilePictureView) getView().findViewById(R.id.profilePic);


                profilePictureView.setProfileId(facebookConnector.getUserId());

                //ConnectionController connectionController = new ConnectionController(Profile.getCurrentProfile().getId());
                //connectionController.newUser();
            }

            @Override
            public void onCancel() {
                Log.d("onCancel", "onCancel ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("FACEBOOK ERROR", exception.toString());
            }
        });

        // Initialize TestButton
        /*
        Button tmpButton = (Button) view.findViewById(R.id.clickME_button);
        tmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FbConnector fbConn = new FbConnector();
                Log.d("TEST 1", String.valueOf(fbConn.isLoggedIn()));
                Log.d("TEST 2", fbConn.getUserId());
                fbConn.getUserInfoJSON();
                fbConn.getUserFriendlistJSON();
            }
        });
        */

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
