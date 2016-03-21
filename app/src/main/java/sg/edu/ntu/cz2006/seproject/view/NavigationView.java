package sg.edu.ntu.cz2006.seproject.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by koAllen on 16/3/16.
 */
public interface NavigationView extends MvpView {
    // show API data
    void showData(String apiData);
}
