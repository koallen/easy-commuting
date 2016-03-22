package sg.edu.ntu.cz2006.seproject.presenter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.Api;
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
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.model.ApiRequestHelper;
import sg.edu.ntu.cz2006.seproject.model.DataPackage;
import sg.edu.ntu.cz2006.seproject.model.GeocoderHelper;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.SuggestionGenerationHelper;
import sg.edu.ntu.cz2006.seproject.model.WeatherData;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.viewmodel.PlaceSuggestion;

public class MainPresenter extends MvpBasePresenter<MainView> {

    // class variables
    private Observable<String> mSuggesstionGenerationTask;
    private DataPackage mDataPackage;
    private String mDestinationString;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mLatLngBounds;
    private LatLng mDestination;
    private AutocompleteFilter mAutoCompleteFilter;

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
        mAutoCompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
    }

    private void cancelSubscription() {
//        if (mApiFetchingTask != null) {
//            mApiFetchingTask.unsubscribeOn(Schedulers.io());
//        }
//        if (mGeocoderTask != null) {
//            mGeocoderTask.unsubscribeOn(Schedulers.io());
//        }
    }

    /**
     * Get suggestion for place from Google Places API
     * @param query the qeury to search
     */
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
                        String primary = autocompletePredictions.get(i).getPrimaryText(null).toString();
                        String full = autocompletePredictions.get(i).getFullText(null).toString();
                        newSuggestions.add(new PlaceSuggestion(primary, full));
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
            Observable<LatLng> geocoderTask = GeocoderHelper.getInstance().getDestinationLatLng(destination);

            // use the result to get route and other API info
            Observable<DataPackage> apiFetchingTask = geocoderTask.flatMap(new Func1<LatLng, Observable<DataPackage>>() {
                @Override
                public Observable<DataPackage> call(LatLng latLng) {
                    Log.d("getRouteInfo", latLng.toString());
                    mDestination = latLng; // save the destination
                    return ApiRequestHelper.getInstance().getApiData(origin, latLng.latitude + "," + latLng.longitude);
                }
            });

            // generate suggestions from data received
//            mSuggesstionGenerationTask = apiFetchingTask.flatMap(new Func1<DataPackage, Observable<String>>() {
//                @Override
//                public Observable<String> call(DataPackage dataPackage) {
//                    return Observable.just(constructSuggestion(dataPackage));
//                }
//            });

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
