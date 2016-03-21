package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 18/3/16.
 */
public class Route {

    @SerializedName("overview_polyline")
    private OverviewPolyline polyline;

    public OverviewPolyline getPolyline() {
        return polyline;
    }
}
