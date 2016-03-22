package sg.edu.ntu.cz2006.seproject.view;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import sg.edu.ntu.cz2006.seproject.viewmodel.PlaceSuggestion;

/**
 * Created by koAllen on 15/3/16.
 */
public interface MainView extends MvpView {
    // locate user
    void moveCamera(LatLng location);
    // show API data
    void showData(String apiData, String destination);
    // show progress dialog
    void showLoading();
    // show a marker on map
    void showMarker(LatLng location, String address, String snippet);
    // show error message
    void showError(String errorMessage);
    // dismiss request dialog
    void hideRequestDialog();
    // show place suggesstions
    void showSuggestions(List<PlaceSuggestion> suggesstions);
    // hide progress on search bar
    void hideProgress();
    // show progress on search bar
    void showProgress();
    // clear suggesions
    void clearSuggestions();
}
