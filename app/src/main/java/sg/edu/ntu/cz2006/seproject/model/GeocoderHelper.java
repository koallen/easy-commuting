package sg.edu.ntu.cz2006.seproject.model;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.edu.ntu.cz2006.seproject.MyApp;

/**
 * Created by koAllen on 22/3/16.
 */
public class GeocoderHelper {
    private static GeocoderHelper mInstance = new GeocoderHelper();

    private Geocoder mGeocoder;

    public static GeocoderHelper getInstance() {
        return mInstance;
    }

    private GeocoderHelper() {
        mGeocoder = new Geocoder(MyApp.getContext());
    }

    public LatLng getDestinationLatLng(String destination) throws IOException, IndexOutOfBoundsException {
        List<Address> addresses = mGeocoder.getFromLocationName(destination, 1);
        Address destinationInfo = addresses.get(0);
        LatLng destinationLatLng = new LatLng(destinationInfo.getLatitude(), destinationInfo.getLongitude());
        return destinationLatLng;
    }

    public Observable<List<PlaceSuggestion>> getPlaceSuggestions(String query, GoogleApiClient googleApiClient, final LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {

        Observable<AutocompletePredictionBuffer> buffer = getPredictionBuffer(googleApiClient, query, latLngBounds, autocompleteFilter);

        Observable<List<PlaceSuggestion>> suggestions = buffer.flatMap(new Func1<AutocompletePredictionBuffer, Observable<List<PlaceSuggestion>>>() {
            @Override
            public Observable<List<PlaceSuggestion>> call(AutocompletePredictionBuffer autocompletePredictions) {
                List<PlaceSuggestion> newSuggestions = new ArrayList<>();
                // add suggestion to list
                for (int i = 0; i < 4; ++i) {
                    try {
                        String primaryText = autocompletePredictions.get(i).getPrimaryText(null).toString();
                        String fullText = autocompletePredictions.get(i).getFullText(null).toString();
                        // get place's coordinates from its full name
                        LatLng placeLatLng = getDestinationLatLng(fullText);
                        // if the place is within Singapore, add it to suggestion list
                        if (latLngBounds.contains(placeLatLng)) {
                            newSuggestions.add(new PlaceSuggestion(primaryText, fullText));
//                    Log.d("MainPresenter", newSuggestions.toString());
                        }
                    } catch (Exception e) {
                        Log.d("GeocoderHelper", e.toString());
                    }
                }
                // release the buffer
                autocompletePredictions.release();
                // call another function to return the results
                return Observable.just(newSuggestions);
            }
        });
        // return the result
        return suggestions;
    }

    public Observable<AutocompletePredictionBuffer> getPredictionBuffer(final GoogleApiClient googleApiClient, final String query, final LatLngBounds latLngBounds, final AutocompleteFilter autocompleteFilter) {
        Observable<AutocompletePredictionBuffer> observable = Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                if (o instanceof Subscriber) {
                    ((Subscriber) o).onNext(getBuffer(googleApiClient, query, latLngBounds, autocompleteFilter));
                    ((Subscriber) o).onCompleted();
                }
            }
        });

        return observable;
    }

    public AutocompletePredictionBuffer getBuffer(GoogleApiClient googleApiClient, String query, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
        PendingResult < AutocompletePredictionBuffer > result =
                Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query,
                        latLngBounds, autocompleteFilter);
        // get the result async
        AutocompletePredictionBuffer predictions = result.await();
        return predictions;
    }
}
