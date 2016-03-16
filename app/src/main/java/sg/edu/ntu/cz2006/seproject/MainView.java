package sg.edu.ntu.cz2006.seproject;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by koAllen on 15/3/16.
 */
public interface MainView extends MvpView {
    // locate user
    void locateUser(LatLng userLocation);
    // show API data
    void showData(String apiData);
}
