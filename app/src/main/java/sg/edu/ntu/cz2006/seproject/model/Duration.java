package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class Duration {
    @SerializedName("text")
    private String mDurationText;

    /**
     * Returns the ETA of the trip
     * @return Trip ETA
     */
    public String getDurationText() {
        return mDurationText;
    }
}
