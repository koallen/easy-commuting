package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by koAllen on 18/3/16.
 */
public class Route {

    @SerializedName("legs")
    private List<Leg> mLeg;

    @SerializedName("overview_polyline")
    private OverviewPolyline polyline;

    public OverviewPolyline getPolyline() {
        return polyline;
    }

    public Leg getLeg() {
        return mLeg.get(0);
    }
}
