package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A helper class for parsing response of Google's API
 */
public class NavigationHelper {
    private static NavigationHelper mInstance = new NavigationHelper();
    private Gson mGson;

    public static NavigationHelper getInstance() {
        return mInstance;
    }

    private NavigationHelper() {
        mGson = new GsonBuilder().create();
    }

    /**
     * Parse the response into a RouteResponse object
     * @param route Response from Google's API
     * @return A RouteResponse object
     */
    public RouteResponse getNavigationList(String route) {
        RouteResponse routeResponse = mGson.fromJson(route, RouteResponse.class);
        return routeResponse;
    }

    /**
     * Finds the eta from Google's response
     * @param route Response from Google's API
     * @return The eta of the trip
     */
    public String getEta(String route) {
        RouteResponse routeResponse = mGson.fromJson(route, RouteResponse.class);
        return routeResponse.getDuration();
    }
}
