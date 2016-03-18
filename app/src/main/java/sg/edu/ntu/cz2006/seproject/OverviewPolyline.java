package sg.edu.ntu.cz2006.seproject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 18/3/16.
 */
public class OverviewPolyline {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }
}
