package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An entity class for holding Google API response
 */
public class Route {

    @SerializedName("legs")
    private List<Leg> mLeg;

    @SerializedName("overview_polyline")
    private OverviewPolyline polyline;

    /**
     * Returns the overview polyline of the route
     * @return Overview polyline
     */
    public OverviewPolyline getPolyline() {
        return polyline;
    }

    /**
     * Return the leg of the route
     * @return Leg of the route
     */
    public Leg getLeg() {
        return mLeg.get(0);
    }
}
