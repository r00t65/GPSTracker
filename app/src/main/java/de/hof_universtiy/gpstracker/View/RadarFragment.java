package de.hof_universtiy.gpstracker.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.FacebookAuthorizationException;

import org.osmdroid.views.MapView;

import de.hof_universtiy.gpstracker.Controller.connection.ConnectionController;
import de.hof_universtiy.gpstracker.Controller.facebook.FbConnector;
import de.hof_universtiy.gpstracker.Controller.radar.RadarController;
import de.hof_universtiy.gpstracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RadarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RadarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Class fragmentClass;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RadarController mRadarController;
    private ConnectionController mConetionController;
    private FbConnector facebookConnector;
    private Fragment fragment = null;


    public RadarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Radar.
     */
    // TODO: Rename and change types and number of parameters
    public static RadarFragment newInstance(String param1, String param2) {
        RadarFragment fragment = new RadarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Radar");


        //gps
        if(false){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Bitte GPS aktivieren");

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

        //internet
        if(false){
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
              if(!facebookConnector.isLoggedIn()){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                  alertDialog.setTitle("Bitte Anmelden");
                  alertDialog.setMessage("Um den Radar nutzen zu können, müssen Sie sich bitte anmelden");
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



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_radar, container, false);
        this.mRadarController = new RadarController(this.getContext(), (MapView) rootView.findViewById(R.id.radar));
        try {
            this.mConetionController = new ConnectionController(this.getContext(),this.mRadarController);
        } catch (FacebookAuthorizationException x) {

        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(getClass().toString(), "Floating RadarFragment");
                mConetionController.getWaypointsOfFriends();
            }
        });
        return rootView;


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
