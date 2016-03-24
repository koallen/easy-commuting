package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class Duration {
    @SerializedName("text")
    private String mDurationText;

    public String getDurationText() {
        return mDurationText;
    }
}
