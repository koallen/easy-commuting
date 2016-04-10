package sg.edu.ntu.cz2006.seproject.presenter;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.model.ApiRequestHelper;
import sg.edu.ntu.cz2006.seproject.model.DataPackage;
import sg.edu.ntu.cz2006.seproject.model.GeocoderHelper;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.NavigationHelper;
import sg.edu.ntu.cz2006.seproject.model.SuggestionGenerationHelper;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.model.PlaceSuggestion;

/**
 * Presenter for MainActivity
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    // class variables
    private Observable<String> mSuggesstionGenerationTask;
    private DataPackage mDataPackage;
    private String mDestinationString;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mLatLngBounds;
    private LatLng mDestination;
    private AutocompleteFilter mAutocompleteFilter;

    // constructor
    public MainPresenter() {
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
        mAutocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
    }

    private void cancelSubscription() {
        if (mSuggesstionGenerationTask != null) {
            mSuggesstionGenerationTask.unsubscribeOn(Schedulers.io());
        }
    }

    /**
     * Get suggestion for place from Google Places API
     * @param query the query to search
     */
    public void getSuggesstions(String query) {
        if (isViewAttached()) {
            getView().showProgress();
        }
        Log.d("MainPresenter suggest", "GETTING suggestion");
        // TODO: make it cancelable
        GeocoderHelper.getInstance().getPlaceSuggestions(query, mGoogleApiClient, mLatLngBounds, mAutocompleteFilter).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PlaceSuggestion>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("MainPresenter...error", e.toString());
                        if (isViewAttached()) {
                            getView().hideProgress();
                        }
                    }

                    @Override
                    public void onNext(List<PlaceSuggestion> placeSuggestions) {
                        if (isViewAttached()) {
                            Log.d("MainPresenter...", placeSuggestions.toString());
                            getView().showSuggestions(placeSuggestions);
                            getView().hideProgress();
                        }
                    }
                });
    }

    /**
     * fetch data from api and generate suggestions
     * @param origin starting location of a route
     * @param destination destination of a route
     */
    public void getDestinationInfo(final String origin, String destination) {
        // display a progress dialog while fetching data from api
        if (isViewAttached()) {
            getView().showLoading();
        }
        // check for origin and destination
        Log.d("getRouteInfo", origin);
        Log.d("getRouteInfo", destination);
        mDestinationString = destination; // save destination string

        // get data from NEA, LTA and Google
        try {
            // get the latitude and longitude for the destination
            Observable<LatLng> geocoderTask = Observable.just(GeocoderHelper.getInstance().getDestinationLatLng(destination));

            // use the result to get route and other API info
            Observable<DataPackage> apiFetchingTask = geocoderTask.flatMap(new Func1<LatLng, Observable<DataPackage>>() {
                @Override
                public Observable<DataPackage> call(LatLng latLng) {
                    Log.d("getRouteInfo", latLng.toString());
                    mDestination = latLng; // save the destination
                    return ApiRequestHelper.getInstance().getApiData(origin, latLng.latitude + "," + latLng.longitude);
                }
            });

            // use all API response to generate suggestions
            mSuggesstionGenerationTask = apiFetchingTask.flatMap(new Func1<DataPackage, Observable<String>>() {
                @Override
                public Observable<String> call(DataPackage dataPackage) {
                    mDataPackage = dataPackage;
                    return SuggestionGenerationHelper.getInstance().getSuggestion(mDestination, dataPackage);
                }
            });

            // subscribe to the observable to execute the tasks
            mSuggesstionGenerationTask.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("taskOnError", e.toString());
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
                                getView().clearSuggestions();
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            Log.d("Suggestion: ", s);
                            if (isViewAttached()) {
                                getView().hideRequestDialog();
                                getView().clearSearchText();
                                getView().showData(s, mDestinationString);
                            }
                        }
                    });
        } catch (IOException e) {
            Log.d("Observable geocoder", e.toString());
            if (isViewAttached()) {
                getView().hideRequestDialog();
                getView().showError("Network unavailable");
            }
        } catch (IndexOutOfBoundsException e) {
            if (isViewAttached()) {
                getView().hideRequestDialog();
                getView().showError("Invalid Address");
            }
        }

////        mApiFetchingTask = ApiRequestHelper.getInstance().getApiData(origin, destination);
//
//        newObservable.subscribe(new Subscriber<DataPackage>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("MAINPRESENTER", e.toString());
//                // construct error message
//                String errorMessage = "Unknown error";
//                if (e instanceof IndexOutOfBoundsException) {
//                    errorMessage = "No route found.";
//                } else if (e instanceof HttpException) {
//                    errorMessage = "Network not available.";
//                } else if (e instanceof SocketTimeoutException) {
//                    errorMessage = "Network timeout";
//                }
//                // display the message on screen
//                if (isViewAttached()) {
//                    getView().hideRequestDialog();
//                    getView().showError(errorMessage);
//                }
//            }
//
//            @Override
//            public void onNext(DataPackage o) {
//                Log.d("MAINPRESENTER", "DataPackage received");
//                if (isViewAttached()) {
//                    getView().showData(o.getRouteResponse().getRoute().getPolyline().getPoints());
//                }
//            }
//        });
    }

    /**
     * Returns the route information to MainActivity
     */
    public void getRouteInfo() {
        if (NavigationHelper.getInstance().getNavigationList(mDataPackage.getRouteResponse()).getStatus().equals("OK")) {
            if (isViewAttached()) {
                getView().showRoute(mDataPackage.getRouteResponse());
            }
        } else {
            if (isViewAttached()) {
                getView().showError("No route available");
            }
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
     * Clears the suggestions when the search query is an empty string
     */
    public void getEmptyQuery() {
        if (isViewAttached()) {
            getView().clearSuggestions();
        }
    }

    /**
     * called when Activity is destroyed, will cancel all tasks running
     * @param retainPresenterInstance whether the state of presenter should be retained
     */
    public void detachView(boolean retainPresenterInstance){
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance){
            cancelSubscription();
        }
    }
}
