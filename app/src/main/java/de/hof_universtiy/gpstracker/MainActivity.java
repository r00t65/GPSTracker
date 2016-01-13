package de.hof_universtiy.gpstracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.appevents.AppEventsLogger;
import de.hof_universtiy.gpstracker.Controller.connection.ConnectionController;
import de.hof_universtiy.gpstracker.Controller.service.RadarServiceReceiver;
import de.hof_universtiy.gpstracker.View.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GPSTrackerFragment.OnFragmentInteractionListener,
        LoginLogoutFragment.OnFragmentInteractionListener, MessengerFragment.OnFragmentInteractionListener,
        RadarFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private Fragment fragment = null;
    Class fragmentClass;

    SharedPreferences sharedPref;
    private boolean isRadarActive;
    private long radarInterval;

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty bro
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentClass = GPSTrackerFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        else {
            Log.i("Happening", "Nofragmentavailable");
        }


        ConnectionController connectionController = new ConnectionController();
        connectionController.getWaypointsOfFriends("1");


        //Service fuer
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isRadarActive = sharedPref.getBoolean("radar_active",false);
        if(isRadarActive){
           // scheduleRadar();
            Log.d("RadarStart","RadarService aktiv");
        }
        //Ende
    }

    private void scheduleRadar() {
        //radarInterval = sharedPref.getLong("radar_interval",30) * 60 * 1000;
        Intent intent = new Intent(getApplicationContext(), RadarServiceReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, RadarServiceReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager radarAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        radarAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 30*60*1000, pIntent);
    }

    private void cancelAlarm(){
        Intent intent = new Intent(getApplicationContext(), RadarServiceReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, RadarServiceReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager radarAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        radarAlarm.cancel(pIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentClass = SettingsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            else {
                Log.i("Happening","Nofragmentavailable");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gpstracker) {
            selectDrawerItem(item);

        } else if (id == R.id.nav_radar) {
            selectDrawerItem(item);

        } else if (id == R.id.nav_messenger) {
            selectDrawerItem(item);

        } else if (id == R.id.nav_loginLogout) {
            selectDrawerItem(item);

        }
       // else if (id == R.id.nav_settings) {
         //   selectDrawerItem(item);
       // }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        //Fragment fragment = null;

        //Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_gpstracker:
                fragmentClass = GPSTrackerFragment.class;
                break;
            case R.id.nav_radar:
                fragmentClass = RadarFragment.class;
                break;
            case R.id.nav_messenger:
                fragmentClass = MessengerFragment.class;
                break;
            case R.id.nav_loginLogout:
                fragmentClass = LoginLogoutFragment.class;
                break;
            //case R.id.nav_settings:
             //   fragmentClass = SettingsFragment.class;
              //  break;
            default:
                fragmentClass = GPSTrackerFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        else {
            Log.i("Happening","Nofragmentavailable");
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
