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
 * A helper class for handling geolocation related functions
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

    /**
     * Returns the geolocation of the destination
     * @param destination User's destination
     * @return The geolocation
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public LatLng getDestinationLatLng(String destination) throws IOException, IndexOutOfBoundsException {
        Log.d("GetLATLNG", destination);
        List<Address> addresses = mGeocoder.getFromLocationName(destination, 1);
        Address destinationInfo = addresses.get(0);
        return new LatLng(destinationInfo.getLatitude(), destinationInfo.getLongitude());
    }

    /**
     * Returns the list of place suggestion
     * @param query User's search query
     * @param googleApiClient Google API client
     * @param latLngBounds A latitude-longitude bound for Singapore
     * @param autocompleteFilter A filter for results
     * @return List of suggested places
     */
    public Observable<List<PlaceSuggestion>> getPlaceSuggestions(String query, GoogleApiClient googleApiClient, final LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {

        Observable<AutocompletePredictionBuffer> buffer = getPredictionBuffer(googleApiClient, query, latLngBounds, autocompleteFilter);

        Observable<List<PlaceSuggestion>> suggestions = buffer.flatMap(new Func1<AutocompletePredictionBuffer, Observable<List<PlaceSuggestion>>>() {
            @Override
            public Observable<List<PlaceSuggestion>> call(AutocompletePredictionBuffer autocompletePredictions) {
                List<PlaceSuggestion> newSuggestions = new ArrayList<>();
                // add suggestion to list
                for (int i = 0; i < 4; ++i) {
                    String primaryText = autocompletePredictions.get(i).getPrimaryText(null).toString();
                    String fullText = autocompletePredictions.get(i).getFullText(null).toString();
                    if (fullText.contains("Singapore")) {
                        newSuggestions.add(new PlaceSuggestion(primaryText, fullText));
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

    /**
     * Creates an observable for getting place suggestions
     * @param googleApiClient Google API client
     * @param query User's query
     * @param latLngBounds A latitude-longitude bound for Singapore
     * @param autocompleteFilter A filter for results
     * @return The prediction given by Google
     */
    private Observable<AutocompletePredictionBuffer> getPredictionBuffer(final GoogleApiClient googleApiClient, final String query, final LatLngBounds latLngBounds, final AutocompleteFilter autocompleteFilter) {
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

    /**
     * Gets the prediction from Google
     * @param googleApiClient Google API client
     * @param query User's query
     * @param latLngBounds A latitude-longitude bound for Singapore
     * @param autocompleteFilter A filter for results
     * @return The prediction given by Google
     */
    private AutocompletePredictionBuffer getBuffer(GoogleApiClient googleApiClient, String query, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
        PendingResult < AutocompletePredictionBuffer > result =
                Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query,
                        latLngBounds, autocompleteFilter);
        // get the result async
        AutocompletePredictionBuffer predictions = result.await();
        return predictions;
    }
}
