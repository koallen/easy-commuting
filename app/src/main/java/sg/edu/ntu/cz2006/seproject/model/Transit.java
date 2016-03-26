package sg.edu.ntu.cz2006.seproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class Transit {

    @SerializedName("arrival_stop")
    private BusStop mArrivalStop;

    @SerializedName("departure_stop")
    private BusStop mDepartureStop;

    @SerializedName("line")
    private BusLine mBusLine;

    /**
     * Returns the arrival bus stop
     * @return Arrival bus stop
     */
    public BusStop getArrivalStop() {
        return mArrivalStop;
    }

    /**
     * Returns the departure bus stop
     * @return Departure bus stop
     */
    public BusStop getDepartureStop() {
        return mArrivalStop;
    }

    /**
     * Returns the bus line
     * @return Bus line
     */
    public BusLine getBusLine() {
        return mBusLine;
    }
}
