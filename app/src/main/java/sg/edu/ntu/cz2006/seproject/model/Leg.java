package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An entity class for holding Google API response
 */
public class Leg {

    @SerializedName("end_location")
    private MapLocation mMapLocation;
    @SerializedName("duration")
    private Duration mDuration;

    @SerializedName("steps")
    private List<Step> mSteps;

    /**
     * Returns the duration
     * @return Duration
     */
    public Duration getDuration() {
        return mDuration;
    }

    /**
     * Returns location on map
     * @return Map location
     */
    public MapLocation getMapLocation() {
        return mMapLocation;
    }

    /**
     * Returns a list of steps
     * @return List of steps
     */
    public List<Step> getSteps() {
        return mSteps;
    }
}
