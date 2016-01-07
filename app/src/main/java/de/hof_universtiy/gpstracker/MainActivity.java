package de.hof_universtiy.gpstracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import de.hof_universtiy.gpstracker.Controller.connection.ConnectionController;
import de.hof_universtiy.gpstracker.Controller.service.RadarService;
import de.hof_universtiy.gpstracker.View.GPSTracker;
import de.hof_universtiy.gpstracker.View.LoginLogout;
import de.hof_universtiy.gpstracker.View.Messenger;
import de.hof_universtiy.gpstracker.View.Radar;
import de.hof_universtiy.gpstracker.View.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GPSTracker.OnFragmentInteractionListener,
        LoginLogout.OnFragmentInteractionListener, Messenger.OnFragmentInteractionListener,
        Radar.OnFragmentInteractionListener, Settings.OnFragmentInteractionListener {




    private Fragment fragment = null;
    Class fragmentClass;

    private Intent radarServiceIntent;

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
        fragmentClass = Radar.class;

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

        //Service fuer Positionsupdates
        //radarServiceIntent = new Intent(this, RadarService.class);
        //startService(radarServiceIntent);


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

        } else if (id == R.id.nav_settings) {
            selectDrawerItem(item);
        }

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
                fragmentClass = GPSTracker.class;
                break;
            case R.id.nav_radar:
                fragmentClass = Radar.class;
                break;
            case R.id.nav_messenger:
                fragmentClass = Messenger.class;
                break;
            case R.id.nav_loginLogout:
                fragmentClass = LoginLogout.class;
                break;
            case R.id.nav_settings:
                fragmentClass = Settings.class;
                break;
            default:
                fragmentClass = GPSTracker.class;
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
