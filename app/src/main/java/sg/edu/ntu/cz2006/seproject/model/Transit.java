package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class Transit {

    @SerializedName("arrival_stop")
    private BusStop mArrivalStop;

    @SerializedName("departure_stop")
    private BusStop mDepartureStop;

    @SerializedName("line")
    private BusLine mBusLine;

    public BusStop getArrivalStop() {
        return mArrivalStop;
    }

    public BusStop getDepartureStop() {
        return mArrivalStop;
    }

    public BusLine getBusLine() {
        return mBusLine;
    }
}
