package sg.edu.ntu.cz2006.seproject.model;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func4;

/**
 * A helper class for request API data from NEA, LTA and Google
 */
public class ApiRequestHelper {

    private static ApiRequestHelper mInstance = new ApiRequestHelper();

    public static ApiRequestHelper getInstance() {
        return mInstance;
    }

    private NEAServiceHelper mNeaServiceHelper;
    private GoogleServiceHelper mGoogleServiceHelper;

    private ApiRequestHelper() {
        mNeaServiceHelper = NEAServiceHelper.getInstance();
        mGoogleServiceHelper = GoogleServiceHelper.getInstance();
    }

    /**
     * Returns the data obtained from various APIs
     * @param origin User's current location
     * @param destination User's destination
     * @return The data from APIs
     */
    public Observable<DataPackage> getApiData(String origin, String destination) {
        return Observable.zip(
                mNeaServiceHelper.getUVIndex(),
                mNeaServiceHelper.getPSI(),
                mNeaServiceHelper.getWeather(),
                mGoogleServiceHelper.getRoute(origin, destination),
                new Func4<UVIndexResponse, PSIResponse, WeatherResponse, ResponseBody, DataPackage>() {
                    @Override
                    public DataPackage call(UVIndexResponse uvIndexResponse, PSIResponse psiResponse, WeatherResponse weatherResponse, ResponseBody routeResponse) {

                        DataPackage apiData = null;
                        try {
                            apiData = new DataPackage(uvIndexResponse, psiResponse, weatherResponse, routeResponse.string());
                        } catch (IOException e) {
                            Log.d("API", e.toString());
                        }
                        return apiData;
                    }
                }
        );
    }
}
