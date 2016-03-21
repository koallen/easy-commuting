package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by koAllen on 18/3/16.
 */
public class RouteResponse {

    @SerializedName("routes")
    private List<Route> route;

    @SerializedName("status")
    private String status;

    public Route getRoute() {
        return route.get(0);
    }

    public String getStatus() {
        return status;
    }
}
