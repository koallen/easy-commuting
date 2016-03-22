package sg.edu.ntu.cz2006.seproject.view;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by koAllen on 16/3/16.
 */
public interface NavigationView extends MvpView {
    // show API data
    void showData(String apiData);

    void showMarker(LatLng location, String address, String snippet);

    void moveCamera(LatLng location);
}
