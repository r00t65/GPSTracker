package de.hof_university.gpstracker.Controller.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.widget.TextView;

import de.hof_university.gpstracker.Controller.listener.SensorChangeListener;
import de.hof_university.gpstracker.Model.position.SensorData;

/**
 * Liest SensorDaten vom System und packt sie in SensorData
 *
 * Created by alex on 09.12.15.
 */
public class SensorController implements SensorControllerInterface {

    private static final int UPDATE_THRESHOLD = 1000;
    private SensorManager mSMgr;
    private Sensor sMotion;
    private Sensor lAcc;
    private Sensor gyro;
    private Sensor rVec;
    private TriggerEventListener mTriggerEventListener;
    private SensorChangeListener mSensorChangeListener;

    private long mLastUpdate;
    private float linX, linY, linZ, rotX, rotY, rotZ, gyrX, gyrY, gyrZ;

    public SensorController(Context context, SensorChangeListener sensorChangeListener) {

        //Listener
        mSensorChangeListener = sensorChangeListener;

        //Get reference to sensormanager
        mSMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //Get reference to linear acceleration

        sMotion = mSMgr.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        lAcc = mSMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rVec = mSMgr.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gyro = mSMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);



        /* erst ab API18
        mTriggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                //doWork
            }
        };*/
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link android.hardware.SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link android.hardware.SensorEvent SensorEvent}.
     *
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link android.hardware.SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        long actualTime = System.currentTimeMillis();


        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                linX = event.values[0];
                linY = event.values[1];
                linZ = event.values[2];
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                rotX = event.values[0];
                rotY = event.values[1];
                rotZ = event.values[2];
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyrX = event.values[0];
                gyrY = event.values[1];
                gyrZ = event.values[2];
                break;
            default:
                break;
        }
        if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {
            mLastUpdate = actualTime;
            mSensorChangeListener.updateSensorData(new SensorData(linX,linY,linZ));
            //sData.setText("Linear X: " + String.format("%.02f", linX) + "\nLinear Y: " + String.format("%.02f", linY) + "\nLinear Z: " + String.format("%.02f", linZ) + "\nRotation X: " + String.format("%.02f", rotX) + "\nRotation Y: " + String.format("%.02f", rotY) + "\nRotation Z: " + String.format("%.02f", rotZ) + "\nGyro X: " + gyrX + "\nGyro Y: " + gyrY + "\nGyro Z: " + gyrZ);
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     *
     * <p>See the SENSOR_STATUS_* constants in
     * {@link android.hardware.SensorManager SensorManager} for details.
     *
     * @param accuracy The new accuracy of this sensor, one of
     *         {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Ist für den Lebenszyklus von Services ausgelegt
     *
     * Die Listener werden Registriert
     */
    @Override
    public void onStartService() {
        mSMgr.registerListener(this, lAcc, SensorManager.SENSOR_DELAY_UI);
        mSMgr.registerListener(this, rVec, SensorManager.SENSOR_DELAY_UI);
        /* erst ab API18
        mSMgr.requestTriggerSensor(mTriggerEventListener, sMotion);
         */
        mLastUpdate = System.currentTimeMillis();
    }

    /**
     * Ist für den Lebenszyklus von Services ausgelegt
     *
     * Die Listener werden entfernt
     */
    @Override
    public void onDestroyService() {
        mSMgr.unregisterListener(this);
    }

}
