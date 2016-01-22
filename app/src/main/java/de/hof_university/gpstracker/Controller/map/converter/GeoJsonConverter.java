package de.hof_university.gpstracker.Controller.map.converter;

import android.support.annotation.NonNull;
import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.Model.track.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * geojson.org
     * @return
     * @throws ParserConfigurationException
     */
    @Override
    public JSONObject convert() throws ParserConfigurationException, JSONException {
        JSONObject root = new JSONObject();
        root.put("type","Feature");
        JSONObject geometry = new JSONObject();
        geometry.put("type","LineString");
        geometry.put("coordinates",this.getCoordinates());
        root.put("geometry",geometry);

        JSONObject properties = new JSONObject();
        root.put("properties",properties);
        return root;
    }

    private JSONArray getCoordinates() throws JSONException {
        JSONArray listOfCoordinates = new JSONArray();
        for(Location loc:this.track.getTracks()){
            JSONArray cor = new JSONArray();
            cor.put(loc.getLocation().getLongitude());cor.put(loc.getLocation().getLatitude());cor.put(loc.getLocation().getAltitude());
            listOfCoordinates.put(cor);
        }
        return listOfCoordinates;
    }
}
