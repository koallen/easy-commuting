package sg.edu.ntu.cz2006.seproject;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {
    private String data = "";

    public void fetchUVIndexData() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        data = "";

        NEAServiceRequestor requestor = new NEAServiceRequestor();

        Observable.zip(
                requestor.getUVIndex(),
                requestor.getPSI(),
                requestor.getWeather(),
                new Func3<UVIndexResponse, PSIResponse, WeatherResponse, String>() {
                    @Override
                    public String call(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse) {
                        data = "UV Index Reading: " + uvIndexResponse.getUvIndexReading();
                        data += "\nPSI Reading: " + psiResponse.getPsiReading();
                        data += "\nWeather: " + weatherResponse.getWeatherDataList();
                        return data;
                    }
                }
        ).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("MAINPRESENTER", e.toString());
            }

            @Override
            public void onNext(String o) {
                Log.d("MAINPRESENTER", o);
                if (isViewAttached()) {
                    getView().showData(o);
                }
            }
        });
    }
}
