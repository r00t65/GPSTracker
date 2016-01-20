package de.hof_university.gpstracker.Model.position;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 17.01.16.
 * <p/>
 * Redesign by alex on 17.01.16
 */
public final class SensorData implements Serializable {
    private final double speed;
    private final double slant;
    private final Date date;

    public SensorData(final double speed, final double slant) {
        this.date = new Date();
        this.speed = speed;
        this.slant = slant;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSlant() {
        return slant;
    }

    public Date getDate() {
        return this.date;
    }

}
