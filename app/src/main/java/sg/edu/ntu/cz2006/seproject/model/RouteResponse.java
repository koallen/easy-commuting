package sg.edu.ntu.cz2006.seproject.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An entity class for holding Google API response
 */
public class RouteResponse {

    @SerializedName("routes")
    private List<Route> route;

    @SerializedName("status")
    private String status;

    /**
     * Returns the route from Google's resposne
     * @return Route
     */
    public Route getRoute() {
        return route.get(0);
    }

    /**
     * Returns the status from Google's reponse
     * @return Status code
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the duration of the trip
     * @return Duration in text
     */
    public String getDuration() {
        return getRoute().getLeg().getDuration().getDurationText();
    }

    /**
     * Returns the list of steps
     * @return List of steps
     */
    public List<Step> getSteps() {
        return getRoute().getLeg().getSteps();
    }

    /**
     * Returns the destination
     * @return Destination location
     */
    public LatLng getDestination() {
        return getRoute().getLeg().getMapLocation().getLatLng();
    }
}
