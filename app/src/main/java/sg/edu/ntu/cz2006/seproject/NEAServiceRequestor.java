package sg.edu.ntu.cz2006.seproject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 17/3/16.
 */
public class NEAServiceRequestor {
    private Retrofit retrofit;
    private NEAService service;

    private static final String apiKey = "781CF461BB6606AD62B1E1CAA87ECA612A87DF33A3ECDC11";
    private static final String uvDataSet = "uvi";
    private static final String psiDataSet = "psi_update";
    private static final String weatherDataSet = "2hr_nowcast";

    public NEAServiceRequestor() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://www.nea.gov.sg/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        service = retrofit.create(NEAService.class);
    }

    public Observable<UVIndexResponse> getUVIndex() {
        return service.getUVIndex(uvDataSet, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PSIResponse> getPSI() {
        return service.getPSI(psiDataSet, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WeatherResponse> getWeather() {
        return service.getWeather(weatherDataSet, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
