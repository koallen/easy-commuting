package sg.edu.ntu.cz2006.seproject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 18/3/16.
 */
public class GoogleServiceRequestor {

    private Retrofit retrofit;
    private GoogleService service;

    private static final String apiKey = "AIzaSyCRUDBJJ0hQ1_R6byuMZGpiDy0vXlMmHwc";
    private static final String travelMode = "transit";
    private static final String avoid = "indoor";
    private static final String region = "sg";
    private static final String transitMode = "bus";
    private static final String transitPreference = "fewer_transfers";

    public GoogleServiceRequestor() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GoogleService.class);
    }

    public Observable<RouteResponse> getRoute(String origin, String destination) {
        return service.getRoute(
                origin,
                destination,
                apiKey,
                travelMode,
                avoid,
                region,
                transitMode,
                transitPreference
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
