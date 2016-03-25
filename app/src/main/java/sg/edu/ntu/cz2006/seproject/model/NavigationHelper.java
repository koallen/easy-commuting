package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by koAllen on 24/3/16.
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

    public RouteResponse getNavigationList(String route) {
        RouteResponse routeResponse = mGson.fromJson(route, RouteResponse.class);
        return routeResponse;
    }

    public String getEta(String route) {
        RouteResponse routeResponse = mGson.fromJson(route, RouteResponse.class);
        return routeResponse.getDuration();
    }
}
