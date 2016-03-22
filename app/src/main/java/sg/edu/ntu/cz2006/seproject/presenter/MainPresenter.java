package sg.edu.ntu.cz2006.seproject.presenter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.activity.MainActivity;
import sg.edu.ntu.cz2006.seproject.model.ApiRequestHelper;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.viewmodel.PlaceSuggestion;

public class MainPresenter extends MvpBasePresenter<MainView> {

    // class variables
    private Observable<String> mApiFetchingTask;
    private Geocoder mGeocoder;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mLatLngBounds;
    private AutocompleteFilter mAutoCompleteFilter;

    // constructor
    public MainPresenter(Context context) {
        // initialize geocoder
        mGeocoder = new Geocoder(context);
        // initialize google client api
        mGoogleApiClient = new GoogleApiClient.Builder(MyApp.getContext())
                .addConnectionCallbacks(GoogleApiHelper.getInstance())
                .addOnConnectionFailedListener(GoogleApiHelper.getInstance())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
        // initialize latitude and longitude bound of Singapore
        mLatLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(1.19, 103.59))
                .include(new LatLng(1.46, 104.03))
                .build();
        // initialize autocomplete filter
        mAutoCompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
    }

    private void cancelSubscription() {
        if (mApiFetchingTask != null) {
            mApiFetchingTask.unsubscribeOn(Schedulers.io());
        }
    }

    public void getSuggesstions(String query) {
        if (isViewAttached()) {
            getView().showProgress();
        }
        Log.d("MainPresenter suggest", "GETTING suggestion");
        //get suggestions based on the query
        PendingResult<AutocompletePredictionBuffer> result =
                Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query,
                        mLatLngBounds, mAutoCompleteFilter);
        // set a callback to process suggestions
        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
            @Override
            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                Log.d("MainPresenter suggest", "RESULT RECEIVED");
                List<PlaceSuggestion> newSuggestions = new ArrayList<>();

                for (int i = 0; i < 4; ++i) {
                    try {
                        newSuggestions.add(new PlaceSuggestion(autocompletePredictions.get(i).getPrimaryText(null).toString()));
                    } catch (Exception e) {
                        Log.d("MainPresenter suggest", e.toString());
                    }
                }

                autocompletePredictions.release();
                if (isViewAttached()) {
                    getView().hideProgress();
                    getView().showSuggestions(newSuggestions);
                }
            }
        });
    }

    public void getRouteInfo(String origin, String destination) {
        if (isViewAttached()) {
            getView().showLoading();
        }
        Log.d("fetchUVIndexData", origin);
        Log.d("fetchUVIndexData", destination);

        mApiFetchingTask = ApiRequestHelper.getInstance().getApiData(origin, destination);

        mApiFetchingTask.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MAINPRESENTER", e.toString());
                // construct error message
                String errorMessage = "Unknown error";
                if (e instanceof IndexOutOfBoundsException) {
                    errorMessage = "No route found.";
                } else if (e instanceof HttpException) {
                    errorMessage = "Network not available.";
                } else if (e instanceof SocketTimeoutException) {
                    errorMessage = "Network timeout";
                }
                // display the message on screen
                if (isViewAttached()) {
                    getView().hideRequestDialog();
                    getView().showError(errorMessage);
                }
            }

            @Override
            public void onNext(String o) {
                Log.d("MAINPRESENTER", o);
                if (isViewAttached()) {
                    getView().showData(o);
                }
            }
        });
    }

    public void getDestinationInfo(String address) {
        if (isViewAttached()) {
            getView().showLoading();
        }
        Log.d("MainPresenterGetDesInfo", address);
        try {
            // TODO: make network request async
            List<Address> addresses = mGeocoder.getFromLocationName(address, 1);
            Address addressInfo = addresses.get(0);
            if (isViewAttached()) {
                getView().showMarker(new LatLng(addressInfo.getLatitude(), addressInfo.getLongitude()), address, "test");
            }
        } catch (IOException e) {
            Log.d("MAINPRESENTER", e.toString());
            if (isViewAttached()) {
                getView().hideRequestDialog();
                getView().showError("Network not available.");
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("MAINPRESENTER", e.toString());
            if (isViewAttached()) {
                getView().hideRequestDialog();
                getView().showError("Unable to find the entered location.");
            }
        }
    }

    public void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Log.d("MainPresenter", "Google Api Client conntected");
        }
    }

    public void disconnectGoogleApiClient() {
        mGoogleApiClient.disconnect();
        Log.d("MainPresenter", "Google Api Client disconnected");
    }

    public void getEmptyQuery() {
        if (isViewAttached()) {
            getView().clearSuggestions();
        }
    }

    // called when Activity is destroyed, will cancel all tasks running
    public void detachView(boolean retainPresenterInstance){
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance){
            cancelSubscription();
        }
    }
}
