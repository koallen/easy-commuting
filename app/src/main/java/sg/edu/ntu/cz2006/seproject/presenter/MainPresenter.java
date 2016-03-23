package sg.edu.ntu.cz2006.seproject.presenter;

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
import java.util.ArrayList;
import java.util.List;

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
import sg.edu.ntu.cz2006.seproject.model.SuggestionGenerationHelper;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.model.PlaceSuggestion;

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
                    }

                    @Override
                    public void onNext(List<PlaceSuggestion> placeSuggestions) {
                        if (isViewAttached()) {
                            Log.d("MainPresenter...", placeSuggestions.toString());
//                            getView().hideProgress();
                            getView().showSuggestions(placeSuggestions);
                            getView().hideProgress();
                        }
                    }
                });
//        //get suggestions based on the query
//        PendingResult<AutocompletePredictionBuffer> result =
//                Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query,
//                        mLatLngBounds, mAutocompleteFilter);
//        // set a callback to process suggestions
//        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
//            @Override
//            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
//                Log.d("MainPresenter suggest", "RESULT RECEIVED");
//                mNewSuggestions = new ArrayList<>();
//
//                for (int i = 0; i < 4; ++i) {
//                    try {
//                        mPrimaryText = autocompletePredictions.get(i).getPrimaryText(null).toString();
//                        mFullText = autocompletePredictions.get(i).getFullText(null).toString();
//                        Observable<LatLng> geocoderTask = GeocoderHelper.getInstance().getDestinationLatLng(mFullText);
//                        geocoderTask.subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Subscriber<LatLng>() {
//                                    @Override
//                                    public void onCompleted() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//
//                                    }
//
//                                    @Override
//                                    public void onNext(LatLng latLng) {
//                                        if (mLatLngBounds.contains(latLng)) {
//                                            mNewSuggestions.add(new PlaceSuggestion(mPrimaryText, mFullText));
//                                            Log.d("MainPresenter", mNewSuggestions.toString());
//                                        }
//                                    }
//                                });
//                    } catch (Exception e) {
//                        Log.d("MainPresenter suggest", e.toString());
//                    }
//                }
//
//                autocompletePredictions.release();
//                if (isViewAttached()) {
//                    Log.d("MainPresenter...", mNewSuggestions.toString());
//                    getView().showSuggestions(mNewSuggestions);
//                    getView().hideProgress();
//                }
//            }
//        });
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
                            // TODO: add error handling
                            Log.d("SuggestionTask", e.toString());
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

//    public void getDestinationInfo(String destination) {
//        Log.d("MainPresenterGetDesInfo", destination);
//        // convert address to geo coordinates
//        try {
//            mGeocoderTask = GeocoderHelper.getInstance().getDestinationLatLng(destination);
//            mGeocoderTask.subscribe(new Subscriber<LatLng>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.d("MAINPRESENTER", e.toString());
//                    // construct error message
//                    String errorMessage = "Unknown error";
//                    if (e instanceof IndexOutOfBoundsException) {
//                        errorMessage = "No address found.";
//                    } else if (e instanceof SocketTimeoutException) {
//                        errorMessage = "Network timeout";
//                    } else if (e instanceof IOException) {
//                        errorMessage = "Network not available.";
//                    }
//                    // display the message on screen
//                    if (isViewAttached()) {
//                        getView().hideRequestDialog();
//                        getView().showError(errorMessage);
//                    }
//                }
//
//                @Override
//                public void onNext(LatLng latLng) {
//                    mDestination = latLng;
//                }
//            });
////            if (isViewAttached()) {
////                getView().showMarker(mDestination.latitude, mDestination.longitude, );
////            }
//        } catch (IOException e) {
//            Log.d("Geocoder", e.toString());
//        }
//    }


    public void getRouteInfo() {
        if (isViewAttached()) {
            getView().showRoute(mDataPackage.getRouteResponse().getRoute().getPolyline().getPoints());
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
