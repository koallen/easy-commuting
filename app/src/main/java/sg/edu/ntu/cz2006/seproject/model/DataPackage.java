package sg.edu.ntu.cz2006.seproject.model;

import android.provider.ContactsContract;

import org.simpleframework.xml.Root;

/**
 * A class holding all API responses
 */
public class DataPackage {

    private UVIndexResponse mUVIndexResponse;
    private PSIResponse mPSIResponse;
    private WeatherResponse mWeatherResponse;
    private String mRouteResponse;

    public DataPackage(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, String routeResponse) {
        mUVIndexResponse = uvIndexResponse;
        mPSIResponse = psiResponse;
        mWeatherResponse = weatherResponse;
        mRouteResponse = routeResponse;
    }

    /**
     * Returns the response from NEA's UV index API
     * @return UV index
     */
    public UVIndexResponse getUVIndexResponse() {
        return mUVIndexResponse;
    }

    /**
     * Returns the response from NEA's PSI API
     * @return PSI reading
     */
    public PSIResponse getPSIResponse() {
        return mPSIResponse;
    }

    /**
     * Returns the response from NEA's weather API
     * @return Current weather
     */
    public WeatherResponse getWeatherResponse() {
        return mWeatherResponse;
    }

    /**
     * Returns the response from Google's Direction API
     * @return Route information
     */
    public String getRouteResponse() {
        return mRouteResponse;
    }

}
