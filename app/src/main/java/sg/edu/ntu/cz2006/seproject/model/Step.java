package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class Step {

    @SerializedName("html_instructions")
    private String mInstruction;

    @SerializedName("travel_mode")
    private String mTravelMode;

    @SerializedName("transit_details")
    private Transit mTransit;

    public String getInstruction() {
        return mInstruction;
    }

    public String getTravelMode() {
        return mTravelMode;
    }

    public Transit getTransit() {
        return mTransit;
    }
}
