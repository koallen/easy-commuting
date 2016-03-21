package sg.edu.ntu.cz2006.seproject.model;

import rx.Observable;
import rx.functions.Func4;

public class ApiRequestHelper {

    private static ApiRequestHelper ourInstance = new ApiRequestHelper();

    public static ApiRequestHelper getInstance() {
        return ourInstance;
    }

    private NEAServiceHelper mNeaServiceHelper;
    private GoogleServiceHelper mGoogleServiceHelper;

    private ApiRequestHelper() {
        mNeaServiceHelper = NEAServiceHelper.getInstance();
        mGoogleServiceHelper = GoogleServiceHelper.getInstance();
    }

    public Observable<String> getApiData(String origin, String destination) {
        return Observable.zip(
                mNeaServiceHelper.getUVIndex(),
                mNeaServiceHelper.getPSI(),
                mNeaServiceHelper.getWeather(),
                mGoogleServiceHelper.getRoute(origin, destination),
                new Func4<UVIndexResponse, PSIResponse, WeatherResponse, RouteResponse, String>() {
                    @Override
                    public String call(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, RouteResponse routeResponse) {
//                        data = "UV Index Reading: " + uvIndexResponse.getUvIndexReading();
//                        data += "\nPSI Reading: " + psiResponse.getPsiReading();
//                        data += "\nWeather: " + weatherResponse.getWeatherDataList();
//                        data += "\nRoute: " + routeResponse.getRoute();
//                        data = routeResponse.getRoute().getPolyline().getPoints();
                        return routeResponse.getRoute().getPolyline().getPoints();
//                        return data;
                    }
                }
        );
    }
}
