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

    public Observable<DataPackage> getApiData(String origin, String destination) {
        return Observable.zip(
                mNeaServiceHelper.getUVIndex(),
                mNeaServiceHelper.getPSI(),
                mNeaServiceHelper.getWeather(),
                mGoogleServiceHelper.getRoute(origin, destination),
                new Func4<UVIndexResponse, PSIResponse, WeatherResponse, RouteResponse, DataPackage>() {
                    @Override
                    public DataPackage call(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, RouteResponse routeResponse) {
                        DataPackage apiData = new DataPackage(uvIndexResponse, psiResponse, weatherResponse, routeResponse);
                        return apiData;
                    }
                }
        );
    }
}
