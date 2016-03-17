package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "weatherForecast")
public class WeatherData {
    @Attribute(name = "forecast")
    private String forecast;

    @Attribute(name = "lat")
    private double lat;

    @Attribute(name = "lon")
    private double lon;

    @Attribute(name = "name")
    private String place;

    public String toString() {
        return forecast + " " + lat + " " + lon + " " + place;
    }
}
