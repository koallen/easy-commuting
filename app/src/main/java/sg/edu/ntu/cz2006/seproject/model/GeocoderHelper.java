package sg.edu.ntu.cz2006.seproject.model;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public Observable<LatLng> getDestinationLatLng(String destination) throws IOException, IndexOutOfBoundsException {
        List<Address> addresses = mGeocoder.getFromLocationName(destination, 1);
        Address destinationInfo = addresses.get(0);
        LatLng destinationLatLng = new LatLng(destinationInfo.getLatitude(), destinationInfo.getLongitude());
        return Observable.just(destinationLatLng);
    }
}
