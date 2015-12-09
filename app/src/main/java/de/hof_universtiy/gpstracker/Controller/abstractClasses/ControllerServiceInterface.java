package de.hof_universtiy.gpstracker.Controller.abstractClasses;

import android.os.Bundle;

/**
 * Created by alex on 09.12.15.
 */
public interface ControllerServiceInterface {

    public void onStartService(Bundle data);
    public void onDestroyService(Bundle data);
}
