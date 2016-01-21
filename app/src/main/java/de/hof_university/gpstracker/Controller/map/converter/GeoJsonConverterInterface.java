package de.hof_university.gpstracker.Controller.map.converter;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alex on 20.01.16.
 * <p>
 * Redesign by alex on 20.01.16
 */
public interface GeoJsonConverterInterface {
    public JSONObject convert() throws ParserConfigurationException, JSONException;
}
