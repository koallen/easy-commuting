package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "channel", strict = false)
public class WeatherResponse {
    @ElementList(entry = "area", inline = true)
    @Path("item/weatherForecast")
    private List<WeatherData> weatherDataList;

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
