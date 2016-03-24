package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class BusStop {
    @SerializedName("name")
    private String mBusStopName;

    public String getBusStopName() {
        return mBusStopName;
    }
}
