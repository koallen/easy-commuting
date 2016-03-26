package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class BusStop {
    @SerializedName("name")
    private String mBusStopName;

    /**
     * Returns the bus stop name
     * @return Bus stop name
     */
    public String getBusStopName() {
        return mBusStopName;
    }
}
