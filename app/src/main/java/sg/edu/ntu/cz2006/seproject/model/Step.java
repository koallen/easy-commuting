package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class Step {

    @SerializedName("html_instructions")
    private String mInstruction;

    @SerializedName("travel_mode")
    private String mTravelMode;

    @SerializedName("transit_details")
    private Transit mTransit;

    /**
     * Returns the instruction given by Google
     * @return The instruction
     */
    public String getInstruction() {
        return mInstruction;
    }

    /**
     * Returns the travel mode of a step
     * @return The travel mode
     */
    public String getTravelMode() {
        return mTravelMode;
    }

    /**
     * Returns the transit details
     * @return Transit details
     */
    public Transit getTransit() {
        return mTransit;
    }
}
