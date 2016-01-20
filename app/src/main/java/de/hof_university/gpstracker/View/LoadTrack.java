package de.hof_university.gpstracker.View;

import android.support.annotation.NonNull;

import de.hof_university.gpstracker.Model.track.Track;

/**
 * Created by alex on 14.01.16.
 * <p/>
 * Redesign by alex on 14.01.16
 */
public interface LoadTrack {
    public void load(@NonNull final Track track);
}
