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
    private RouteResponse mRouteResponse;

    public DataPackage(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, RouteResponse routeResponse) {
        mUVIndexResponse = uvIndexResponse;
        mPSIResponse = psiResponse;
        mWeatherResponse = weatherResponse;
        mRouteResponse = routeResponse;
    }

    public UVIndexResponse getUVIndexResponse() {
        return mUVIndexResponse;
    }

    public PSIResponse getPSIResponse() {
        return mPSIResponse;
    }

    public WeatherResponse getWeatherResponse() {
        return mWeatherResponse;
    }

    public RouteResponse getRouteResponse() {
        return mRouteResponse;
    }

}
