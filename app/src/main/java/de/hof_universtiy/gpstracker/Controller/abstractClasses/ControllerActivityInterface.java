package de.hof_universtiy.gpstracker.Controller.abstractClasses;

import android.os.Bundle;

/**
 * Created by alex on 09.12.15.
 */
public interface ControllerActivityInterface {
    public void onStart(Bundle data);
    public void onDestroy(Bundle data);
    public void onPause(Bundle data);
}
