package sg.edu.ntu.cz2006.seproject.view;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import sg.edu.ntu.cz2006.seproject.viewmodel.InfoData;

/**
 * The interface between NavigationActivity and NavigationPresenter
 */
public interface NavigationView extends MvpView {
    // show API data
    void showRouteInfo(List<InfoData> infoDataList, String eta);

    // show route on map
    void showRoute(List<LatLng> route, List<LatLng> stationList);

    // move the camera
    void moveCamera(LatLng location);
}
