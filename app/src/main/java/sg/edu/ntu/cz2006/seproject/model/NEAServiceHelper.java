package sg.edu.ntu.cz2006.seproject.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 21/3/16.
 */
public class NEAServiceHelper {
    private static NEAServiceHelper mInstance = new NEAServiceHelper();

    public static NEAServiceHelper getInstance() {
        return mInstance;
    }

    // class attributes
    private Retrofit mRetrofit;
    private NEAService mService;

    // some constants for API requests
    private static final String apiKey = "781CF461BB6606AD62B1E1CAA87ECA612A87DF33A3ECDC11";
    private static final String uvDataSet = "uvi";
    private static final String psiDataSet = "psi_update";
    private static final String weatherDataSet = "2hr_nowcast";

    private NEAServiceHelper() {
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://www.nea.gov.sg/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        mService = mRetrofit.create(NEAService.class);
    }

    public Observable<UVIndexResponse> getUVIndex() {
        return mService.getUVIndex(uvDataSet, apiKey);
    }

    public Observable<PSIResponse> getPSI() {
        return mService.getPSI(psiDataSet, apiKey);
    }

    public Observable<WeatherResponse> getWeather() {
        return mService.getWeather(weatherDataSet, apiKey);
    }
}
