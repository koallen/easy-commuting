package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class OverviewPolyline {
    @SerializedName("points")
    private String points;

    /**
     * Returns points in a polyline
     * @return Points in a polyline
     */
    public String getPoints() {
        return points;
    }
}
