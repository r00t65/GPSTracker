package de.hof_universtiy.gpstracker.Controller;

import android.os.Bundle;

/**
 * Created by alex on 09.12.15.
 */
public interface ControllerActivity {
    public void onStart(Bundle data);
    public void onDestroy(Bundle data);
    public void onPause(Bundle data);
}
