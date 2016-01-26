package de.hof_university.gpstracker.Model.position;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 17.01.16.
 * <p/>
 * Redesign by alex on 17.01.16
 */
public final class SensorData implements Serializable {

    private float mLinX, mLinY, mLinZ;
    private final Date date;

    public SensorData(final float linX, final float linY, final float linZ) {
        this.date = new Date();
        mLinX = linX;
        mLinY = linY;
        mLinZ = linZ;

    }

    @Override
    public String toString() {
        return "X: "+mLinX+" Y: "+mLinY+" Z: "+mLinZ;
    }

    public float getX() { return mLinX; }

    public float getY() {
        return mLinY;
    }

    public float getZ() { return mLinZ; }

    public Date getDate() {
        return this.date;
    }

}
