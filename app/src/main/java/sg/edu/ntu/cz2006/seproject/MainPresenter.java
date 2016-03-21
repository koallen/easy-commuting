package sg.edu.ntu.cz2006.seproject;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func4;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    private String data = "";
    // get singleton objects
    private NEAServiceHelper mNeaServiceRequestor = NEAServiceHelper.getInstance();
    private GoogleServiceHelper mGoogleServiceHelper = GoogleServiceHelper.getInstance();

    public void fetchUVIndexData(String origin, String destination) {
        if (isViewAttached()) {
            getView().showLoading();
        }

        data = "";

        Observable.zip(
                mNeaServiceRequestor.getUVIndex(),
                mNeaServiceRequestor.getPSI(),
                mNeaServiceRequestor.getWeather(),
                mGoogleServiceHelper.getRoute(origin, destination),
                new Func4<UVIndexResponse, PSIResponse, WeatherResponse, RouteResponse, String>() {
                    @Override
                    public String call(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, RouteResponse routeResponse) {
//                        data = "UV Index Reading: " + uvIndexResponse.getUvIndexReading();
//                        data += "\nPSI Reading: " + psiResponse.getPsiReading();
//                        data += "\nWeather: " + weatherResponse.getWeatherDataList();
//                        data += "\nRoute: " + routeResponse.getRoute();
                        data = routeResponse.getRoute().getPolyline().getPoints();
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
