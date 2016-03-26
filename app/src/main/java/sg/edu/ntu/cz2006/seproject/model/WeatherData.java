package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * A class holding weather information of a location
 */
@Root(name = "weatherForecast")
public class WeatherData {
    @Attribute(name = "forecast")
    private String mForecast;

    @Attribute(name = "lat")
    private double mLat;

    @Attribute(name = "lon")
    private double mLon;

    @Attribute(name = "name")
    private String mPlace;

    public String toString() {
        return mForecast + " " + mLat + " " + mLon + " " + mPlace;
    }

    /**
     * Return the forecast
     * @return Forecast
     */
    public String getForecast() {
        return mForecast;
    }

    /**
     * Return the latitude
     * @return Latitude
     */
    public double getLat() {
        return mLat;
    }

    /**
     * Return the longitude
     * @return Longitude
     */
    public double getLon() {
        return mLon;
    }

    /**
     * Return the place
     * @return Place
     */
    public String getPlace() {
        return mPlace;
    }
}
