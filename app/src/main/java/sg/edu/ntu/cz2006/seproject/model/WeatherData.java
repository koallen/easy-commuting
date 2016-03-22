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

    public String getForecast() {
        return mForecast;
    }

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLon;
    }

    public String getPlace() {
        return mPlace;
    }
}
