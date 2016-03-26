package sg.edu.ntu.cz2006.seproject.model;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A helper class for creating requests for Google API
 */
public class GoogleServiceHelper {
    private static GoogleServiceHelper mInstance = new GoogleServiceHelper();

    public static GoogleServiceHelper getInstance() {
        return mInstance;
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

    /**
     * Returns the request to Google's API
     * @param origin User's current location
     * @param destination User's destination
     * @return The web request
     */
    public Observable<ResponseBody> getRoute(String origin, String destination) {
        return mService.getRoute(
                origin,
                destination,
                apiKey,
                travelMode,
                avoid,
                region,
                transitMode,
                transitPreference
        );
    }

}
