package sg.edu.ntu.cz2006.seproject.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by koAllen on 24/3/16.
 */
public class MapLocation {

    @SerializedName("lat")
    private double mLat;

    @SerializedName("lng")
    private double mLng;

    public LatLng getLatLng() {
        return new LatLng(mLat, mLng);
    }
}
