package sg.edu.ntu.cz2006.seproject.model;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Observable;

/**
 * Created by koAllen on 22/3/16.
 */
public class SuggestionGenerationHelper {
    private static SuggestionGenerationHelper mInstance = new SuggestionGenerationHelper();

    public static SuggestionGenerationHelper getInstance() {
        return mInstance;
    }

    private SuggestionGenerationHelper() {

    }

    public Observable<String> getSuggestion(LatLng destination, DataPackage dataPackage) {
        // get suggestion on weather
        WeatherData destinationWeather = getDestinationWeather(destination, dataPackage.getWeatherResponse());
        String weatherSuggestion = getWeatherSuggestion(destinationWeather);
        // get suggestion on PSI
        PSIData destinationPSI = getDestinationPSI(destination, dataPackage.getPSIResponse());
        String psiSuggestion = getPSISuggestion(dataPackage.getPSIResponse());
        // get suggestion on UV index
        String uvIndexSuggestion = getUVIndexSuggestion(dataPackage.getUVIndexResponse());
        return Observable.just(weatherSuggestion + "\n" + psiSuggestion + "\n" + uvIndexSuggestion);
    }

    public WeatherData getDestinationWeather(LatLng destination, WeatherResponse weatherResponse) {
        // get all weather data
        List<WeatherData> weatherDataList = weatherResponse.getWeatherDataList();

        // initialize locations
        double startLat = destination.latitude;
        double startLon = destination.longitude;
        double endLat = weatherDataList.get(0).getLat();
        double endLon = weatherDataList.get(0).getLon();
        float[] shortestDistanceArray = new float[1];

        // get initial distance
        Location.distanceBetween(startLat, startLon, endLat, endLon, shortestDistanceArray);
        float shortestDistance = shortestDistanceArray[0];
        WeatherData shortestDistanceWeatherData = weatherDataList.get(0);

        // find nearest location
        for (WeatherData weatherData : weatherDataList) {
            Location.distanceBetween(startLat, startLon, weatherData.getLat(), weatherData.getLon(), shortestDistanceArray);
            if (shortestDistance > shortestDistanceArray[0]) {
                shortestDistance = shortestDistanceArray[0];
                shortestDistanceWeatherData = weatherData;
            }
        }

        // return the nearest location's weather data
        Log.d("Generator", shortestDistanceWeatherData.getPlace());
        return shortestDistanceWeatherData;
    }

    public PSIData getDestinationPSI(LatLng destination, PSIResponse psiResponse) {
        Log.d("Generator", psiResponse.getPsiReading().get(0).toString());
        return psiResponse.getPsiReading().get(0);
    }

    public String getWeatherSuggestion(WeatherData weatherData) {
        return "Weather is sunny today";
    }

    public String getUVIndexSuggestion(UVIndexResponse uvIndexResponse) {
        return "there is no sun today";
    }

    public String getPSISuggestion(PSIResponse psiResponse) {
        return "psi looks good";
    }
}
