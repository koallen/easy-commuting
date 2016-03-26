package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class BusLine {

    @SerializedName("short_name")
    private String mBusLineName;

    /**
     * Returns the bus line name
     * @return Bus line name
     */
    public String getBusLineName() {
        return mBusLineName;
    }
}
