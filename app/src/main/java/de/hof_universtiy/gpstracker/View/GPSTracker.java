package de.hof_universtiy.gpstracker.View;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import org.osmdroid.views.MapView;

import de.hof_universtiy.gpstracker.Controller.map.MapController;
import de.hof_universtiy.gpstracker.Controller.sensor.GPSController;
import de.hof_universtiy.gpstracker.Controller.serialize.StorageController;
import de.hof_universtiy.gpstracker.Controller.service.TrackingService;
import de.hof_universtiy.gpstracker.Controller.tracking.TrackingController;
import de.hof_universtiy.gpstracker.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GPSTracker.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSTracker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSTracker extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MapController mapController;
    //----------------------Test---GPS----------------
    private GPSController gpsController;
    private TrackingController trackingController;
    private StorageController storageController;
    //----------------------Test ---GPS//---------------
    private Intent trackingServiceIntent;
    TrackingService mService;



    public GPSTracker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GPSTracker.
     */
    // TODO: Rename and change types and number of parameters
    public static GPSTracker newInstance(String param1, String param2) {
        final GPSTracker fragment = new GPSTracker();
        final Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gpstracker, container, false);
        // Inflate the layout for this fragment
        this.mapController = new MapController(this.getContext(), (MapView) rootView.findViewById(R.id.mapView));
        this.mapController.showMyPosition();

        //----------------------Test---GPS----------------
        /*storageController = new StorageController(this.getContext());
        this.trackingController = new TrackingController(this.getContext(),this.storageController);
        gpsController = new GPSController(this.getContext(), new GPSController.PositionChangeListener() {
            @Override
            public void setNewPosition(Location location) {
                Log.i(this.getClass().toString(),location.toString());
            }
        }, this.trackingController);
        try {
            final Bundle bundle = new Bundle();
            bundle.putBoolean(GPSController.IS_TRACKING,true);
            this.gpsController.onStartService(bundle);
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        }
        try {
            this.storageController.onStartService(null);
        } catch (GPSController.GPSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        //----------------------Test ---GPS//---------------

        //Tracking Service Button

        trackingServiceIntent = new Intent(this.getActivity(), TrackingService.class);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMyServiceRunning(TrackingService.class)) {
                    getActivity().startService(trackingServiceIntent);
                    getActivity().bindService(trackingServiceIntent, trackingConnection, Context.BIND_AUTO_CREATE);

//                    String test = mService.getServiceInfo();
//                    Log.v("BoundService", test);

                } else {
                    getActivity().stopService(trackingServiceIntent);
                    getActivity().unbindService(trackingConnection);
                }
            }
        });

        //-----------------------

        return rootView;
    }

    private ServiceConnection trackingConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TrackingService.LocalBinder binder = (TrackingService.LocalBinder) service;
            mService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart(){
        super.onStart();
        this.mapController.onStart();
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

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.mapController.onDestroy();
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
