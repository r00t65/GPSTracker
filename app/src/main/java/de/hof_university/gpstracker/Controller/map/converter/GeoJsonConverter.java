package de.hof_university.gpstracker.Controller.map.converter;

import android.support.annotation.NonNull;
import de.hof_university.gpstracker.Model.track.Track;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alex on 20.01.16.
 * <p>
 * Redesign by alex on 20.01.16
 */
public class GeoJsonConverter implements GeoJsonConverterInterface {

    private final Track track;

    public GeoJsonConverter(@NonNull final Track track){
        this.track = track;
    }

    @Override
    public String convert() throws ParserConfigurationException {
        return null;
    }
}
