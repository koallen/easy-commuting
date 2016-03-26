package sg.edu.ntu.cz2006.seproject.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * An entity class for holding Google API response
 */
public class MapLocation {

    @SerializedName("lat")
    private double mLat;

    @SerializedName("lng")
    private double mLng;

    /**
     * Returns the latitude and longitude of the location
     * @return Latitude longitude of the location
     */
    public LatLng getLatLng() {
        return new LatLng(mLat, mLng);
    }
}
