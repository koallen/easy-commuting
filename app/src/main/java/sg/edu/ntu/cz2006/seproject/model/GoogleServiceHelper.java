package sg.edu.ntu.cz2006.seproject.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 21/3/16.
 */
public class GoogleServiceHelper {
    private static GoogleServiceHelper ourInstance = new GoogleServiceHelper();

    public static GoogleServiceHelper getInstance() {
        return ourInstance;
    }

    // class attributes
    private Retrofit mRetrofit;
    private GoogleService mService;

    // some constants for API requests
    private static final String apiKey = "AIzaSyCRUDBJJ0hQ1_R6byuMZGpiDy0vXlMmHwc";
    private static final String travelMode = "transit";
    private static final String avoid = "indoor";
    private static final String region = "sg";
    private static final String transitMode = "bus";
    private static final String transitPreference = "fewer_transfers";

    private GoogleServiceHelper() {
        // create a retrofit client for API requests
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // create the services available from GoogleService
        mService = mRetrofit.create(GoogleService.class);
    }

    public Observable<RouteResponse> getRoute(String origin, String destination) {
        return mService.getRoute(
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
