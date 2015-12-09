package de.hof_universtiy.gpstracker.Model.position;

import org.osmdroid.util.GeoPoint;

import de.hof_universtiy.gpstracker.Model.ModelInterface;

/**
 * Created by Patrick Büttner on 21.11.2015.
 */
public class PositionModel implements ModelInterface
{
    private String id;
    private GeoPoint geoPoint;

    public PositionModel()
    {

    }

    public PositionModel(String id, double latitude, double longitude)
    {
        this.id = id;
        geoPoint = new GeoPoint(latitude, longitude);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public double getLongitude()
    {
        return geoPoint.getLongitude();
    }

    /*public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }*/

    public double getLatitude()
    {
        return geoPoint.getLatitude();
    }

    @Override
    public String getID() {
        return null;
    }

    /*public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }*/
}
