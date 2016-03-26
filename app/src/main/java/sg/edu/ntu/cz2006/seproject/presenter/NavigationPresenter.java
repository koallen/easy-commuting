package sg.edu.ntu.cz2006.seproject.presenter;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.cz2006.seproject.Globals;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.NavigationHelper;
import sg.edu.ntu.cz2006.seproject.model.RouteResponse;
import sg.edu.ntu.cz2006.seproject.model.Step;
import sg.edu.ntu.cz2006.seproject.view.NavigationView;
import sg.edu.ntu.cz2006.seproject.viewmodel.InfoData;

/**
 * The presenter class for NavigationActivity
 */
public class NavigationPresenter extends MvpBasePresenter<NavigationView> {

    private RouteResponse mRouteResponse;
    private GoogleApiClient mGoogleApiClient;

    public NavigationPresenter() {
        // initialize google client api
        mGoogleApiClient = new GoogleApiClient.Builder(MyApp.getContext())
                .addConnectionCallbacks(GoogleApiHelper.getInstance())
                .addOnConnectionFailedListener(GoogleApiHelper.getInstance())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Constructs the information to be displayed in the bottom sheet
     * @param routeResponse Response from Google API
     * @param busIcon The icon for bus
     * @param walkIcon The icon for walking
     */
    public void getRouteInfo(String routeResponse, Drawable busIcon, Drawable walkIcon) {
        // deserialize JSON into object
        mRouteResponse = NavigationHelper.getInstance().getNavigationList(routeResponse);
        // convert to a list of InfoData
        List<InfoData> infoDataList = new ArrayList<>();

        for (Step step : mRouteResponse.getSteps()) {
            if (step.getTravelMode().equals("TRANSIT")) {
                infoDataList.add(new InfoData("BUS " + step.getTransit().getBusLine().getBusLineName(), step.getInstruction(), busIcon));
            } else {
                infoDataList.add(new InfoData("WALKING", step.getInstruction(), walkIcon));
            }
        }
        // show eta and navigation info in NavigationActivity
        if (isViewAttached()) {
            getView().showRouteInfo(infoDataList, mRouteResponse.getDuration());
        }
    }

    /**
     * Constructs the route to be displayed on the map
     */
    public void getRoute() {
        // construct route and marker
        List<LatLng> route = PolyUtil.decode(mRouteResponse.getRoute().getPolyline().getPoints());
        // show route and destination on map
        if (isViewAttached()) {
            getView().moveCamera(Globals.SINGAPORE_LOCATION);
            getView().showRoute(route, mRouteResponse.getDestination());
        }
    }

    /**
     * Connect to Google API client
     */
    public void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Log.d("MainPresenter", "Google Api Client conntected");
        }
    }

    /**
     * Disconnect from Google API client
     */
    public void disconnectGoogleApiClient() {
        mGoogleApiClient.disconnect();
        Log.d("MainPresenter", "Google Api Client disconnected");
    }

    /**
     * called when Activity is destroyed, will cancel all tasks running
     * @param retainPresenterInstance whether the state of presenter should be retained
     */
    public void detachView(boolean retainPresenterInstance){
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance){
//            cancelSubscription();
        }
    }
}
