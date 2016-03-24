package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class BusLine {

    @SerializedName("short_name")
    private String mBusLineName;

    public String getBusLineName() {
        return mBusLineName;
    }
}
