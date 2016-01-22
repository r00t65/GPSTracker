package de.hof_university.gpstracker.View.fragment;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import de.hof_university.gpstracker.Controller.map.converter.GeoJsonConverter;
import de.hof_university.gpstracker.Controller.sensor.GPSController;
import de.hof_university.gpstracker.Controller.tracking.TrackingController;
import de.hof_university.gpstracker.View.LoadTrack;
import de.hof_university.gpstracker.View.activity.MainActivity;
import org.json.JSONException;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.ArrayList;

import de.hof_university.gpstracker.Controller.map.MapController;
import de.hof_university.gpstracker.Controller.serialize.StorageController;
import de.hof_university.gpstracker.Controller.service.TrackingService;
import de.hof_university.gpstracker.Model.track.Track;
import de.hof_university.gpstracker.R;

import javax.xml.parsers.ParserConfigurationException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GPSTrackerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSTrackerFragment extends Fragment implements LoadTrack {
    public static final int FILE_SELECT_CODE = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TrackingService mService;
    Class fragmentClass;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String trackName = "G";
    private OnFragmentInteractionListener mListener;
    private MapController mapController;
    private Intent trackingServiceIntent;
    private Boolean isBound = false;
    private TextView textViewSens;
    private Fragment fragment = null;


    // private Button lastTrackButton;
    private ServiceConnection trackingConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d("Binder", "binder");
            TrackingService.LocalBinder binder = (TrackingService.LocalBinder) service;
            mService = binder.getService();
            mService.registerListener(mapController.getListener());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            try {
                mService.saveTrack(trackName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            mService.unregisterListener();
        }
    };

    public GPSTrackerFragment() {
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
    public static GPSTrackerFragment newInstance(String param1, String param2) {
        final GPSTrackerFragment fragment = new GPSTrackerFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("GPSTracker");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Tracking Service Button

        trackingServiceIntent = new Intent(this.getActivity(), TrackingService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gpstracker, container, false);
        //textview für sensordaten
        textViewSens = (TextView) rootView.findViewById(R.id.textViewSensor);

        // Inflate the layout for this fragment
        this.mapController = new MapController(this.getContext(), (MapView) rootView.findViewById(R.id.mapView));

        //gps
        if (!GPSController.isGPSEnable(getContext())) {//TODO:Falsche Position für dich Kontroller!
            MainActivity.startGPSEnableDialog(getActivity());
        }

        //internet
        if (false) {//TODO @Lothar
            MainActivity.startInternetEnableDialog(getActivity());
        }
        // lastTrackButton = (Button) rootView.findViewById(R.id.lastTrack);

        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.lastTrack);
        fab2.setOnClickListener(new View.OnClickListener() {
            public Track track;
            public StorageController storageController;

            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                storageController = new StorageController(getContext());
                try {
                    storageController.onStartService();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                alertDialog.setTitle("Trackliste" + storageController.getListOfTrackNames().size());

                final ListView list = new ListView(getActivity());

                list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>(storageController.getListOfTrackNames())));
                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            track = storageController.loadTrack((String) parent.getItemAtPosition(position));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {//TODO: Brian
                            new GeoJsonConverter(track).convert();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        list.setSelection(position);
                        return false;
                    }
                });
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            track = storageController.loadTrack((String) parent.getItemAtPosition(position));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        list.setSelection(position);
                    }
                });
                alertDialog.setView(list);

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (track != null)
                                    mapController.getListener().updateTrack(track);
                            }
                        });

                alertDialog.show();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMyServiceRunning(TrackingService.class)) {//TODO https://github.com/iPaulPro/aFileChooser
                    getActivity().startService(trackingServiceIntent);
                    isBound = getActivity().bindService(trackingServiceIntent, trackingConnection, Context.BIND_AUTO_CREATE);
                    Context context = getContext();
                    CharSequence text = "Track wurde gestartet";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.greenRunning)));


//                    String test = mService.getServiceInfo();
//                    Log.v("BoundService", test);

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Track speichern");
                    alertDialog.setMessage("Bitte geben sie den Namen des Tracks ein: ");

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertDialog.setView(input);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abbrechen",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    trackName = input.getText().toString();
                                    if (input.getText().toString().isEmpty()) {
                                        trackName = "Unbenannter Track";
                                        dialog.dismiss();
                                    }
                                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(TrackingController.SharedReNameTrack, trackName).commit();

                                    if (isBound) {
                                        getActivity().unbindService(trackingConnection);
                                    }
                                    getActivity().stopService(trackingServiceIntent);
                                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorAccent)));
                                }
                            });

                    alertDialog.show();
                }
            }
        });

        //-----------------------

        return rootView;
    }

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
    public void onStart() {
        super.onStart();
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void load(@NonNull Track track) {
        Toast.makeText(getContext(), track.getName(), Toast.LENGTH_LONG).show();
        this.mapController.getListener().updateTrack(track);
    }

    public void doLastTrack(View view) {

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