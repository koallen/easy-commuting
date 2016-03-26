package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * A class holding response obtained from NEA Weather API
 */
@Root(name = "channel", strict = false)
public class WeatherResponse {
    @ElementList(entry = "area", inline = true)
    @Path("item/weatherForecast")
    private List<WeatherData> weatherDataList;

    /**
     * Return a list of weather data
     * @return List of weather data
     */
    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
