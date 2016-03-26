package sg.edu.ntu.cz2006.seproject.model;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Observable;

/**
 * A helper class for generating route suggestions
 */
public class SuggestionGenerationHelper {
    private static SuggestionGenerationHelper mInstance = new SuggestionGenerationHelper();

    public static SuggestionGenerationHelper getInstance() {
        return mInstance;
    }

    private SuggestionGenerationHelper() {

    }

    /**
     * Provides suggestions given API information and destination
     * @param destination User's destination
     * @param dataPackage Data received from API
     * @return The suggestion
     */
    public Observable<String> getSuggestion(LatLng destination, DataPackage dataPackage) {
        // get suggestion on weather
        WeatherData destinationWeather = getDestinationWeather(destination, dataPackage.getWeatherResponse());
        String weatherSuggestion = getWeatherSuggestion(destinationWeather);
        // get suggestion on PSI
        PSIData destinationPSI = getDestinationPSI(destination, dataPackage.getPSIResponse());
        String psiSuggestion = getPSISuggestion(destinationPSI);
        // get suggestion on UV index
        String uvIndexSuggestion = getUVIndexSuggestion(dataPackage.getUVIndexResponse());
        // get ETA
        String etaInfo = NavigationHelper.getInstance().getEta(dataPackage.getRouteResponse());
        return Observable.just(weatherSuggestion + "\n\n" + psiSuggestion + "\n\n" + uvIndexSuggestion + "\n\n" + etaInfo);
    }

    private WeatherData getDestinationWeather(LatLng destination, WeatherResponse weatherResponse) {
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
        return shortestDistanceWeatherData;
    }

    private PSIData getDestinationPSI(LatLng destination, PSIResponse psiResponse) {
        // get all PSI data
        List<PSIData> psiDataList = psiResponse.getPsiReading();

        // initialize locations
        double startLat = destination.latitude;
        double startLon = destination.longitude;
        double endLat = psiDataList.get(0).getLat();
        double endLon = psiDataList.get(0).getLon();
        float[] shortestDistanceArray = new float[1];

        // get initial distance
        Location.distanceBetween(startLat, startLon, endLat, endLon, shortestDistanceArray);
        float shortestDistance = shortestDistanceArray[0];
        PSIData shortestDistancePSIData = psiDataList.get(0);

        // find nearest location
        for (PSIData psiData : psiDataList) {
            Location.distanceBetween(startLat, startLon, psiData.getLat(), psiData.getLon(), shortestDistanceArray);
            if (shortestDistance > shortestDistanceArray[0]) {
                shortestDistance = shortestDistanceArray[0];
                shortestDistancePSIData = psiData;
            }
        }

        // return the nearest location's weather data
        return shortestDistancePSIData;
    }

    private String getWeatherSuggestion(WeatherData weatherData) {
        String forecast = weatherData.getForecast();
        String weatherSuggestion = "";
        // give suggestion based on forecast
        if (forecast.equals("BR")) {
            weatherSuggestion = "Mist. Be careful outside.";
        } else if (forecast.equals("CL")) {
            weatherSuggestion = "Cloudy. Looks like a good day.";
        } else if (forecast.equals("DR")) {
            weatherSuggestion = "Drizzle. Bring your umbrella.";
        } else if (forecast.equals("FA")) {
            weatherSuggestion = "Fair. Enjoy your trip.";
        } else if (forecast.equals("FG")) {
            weatherSuggestion = "Fog. Be caureful outside.";
        } else if (forecast.equals("FN")) {
            weatherSuggestion = "Fair. Enjoy your trip.";
        } else if (forecast.equals("FW")) {
            weatherSuggestion = "Fair & Warm. Enjoy the good weather.";
        } else if (forecast.equals("HG")) {
            weatherSuggestion = "Thundery Showers with Gusty Wind. Better stay at home.";
        } else if (forecast.equals("HR")) {
            weatherSuggestion = "Heavy Rain. Take your umbrella if you want to go.";
        } else if (forecast.equals("HS")) {
            weatherSuggestion = "Heavy Showers. Take your umbrella if you want to go.";
        } else if (forecast.equals("HT")) {
            weatherSuggestion = "Heavy Thundery Showers. Take your umbrella if you want to go.";
        } else if (forecast.equals("HZ")) {
            weatherSuggestion = "Hazy. Better stay at home.";
        } else if (forecast.equals("LH")) {
            weatherSuggestion = "Slightly Hazy. Wear a mask if you want to go.";
        } else if (forecast.equals("LR")) {
            weatherSuggestion = "Light Rain. Bring your umbrella.";
        } else if (forecast.equals("LS")) {
            weatherSuggestion = "Light Showers. Bring your umbrella.";
        } else if (forecast.equals("OC")) {
            weatherSuggestion = "Overcast. Better bring your umbrella.";
        } else if (forecast.equals("PC")) {
            weatherSuggestion = "Partly Cloudy. Enjoy your trip.";
        } else if (forecast.equals("PN")) {
            weatherSuggestion = "Partly Cloudy. Enjoy your trip.";
        } else if (forecast.equals("PS")) {
            weatherSuggestion = "Passing Showers. Wait for a while before you go.";
        } else if (forecast.equals("RA")) {
            weatherSuggestion = "Moderate Rain. Bring your umbrella.";
        } else if (forecast.equals("SH")) {
            weatherSuggestion = "Showers.Bring your umbrella.";
        } else if (forecast.equals("SK")) {
            weatherSuggestion = "Showers with Strong Wind. Bring your umbrella.";
        } else if (forecast.equals("SN")) {
            weatherSuggestion = "Snow. Stay warm outside.";
        } else if (forecast.equals("SR")) {
            weatherSuggestion = "Rain with Strong Wind. Bring your umbrella.";
        } else if (forecast.equals("SS")) {
            weatherSuggestion = "Snow Showers. Stay warm outside";
        } else if (forecast.equals("SU")) {
            weatherSuggestion = "Sunny. Enjoy the beautiful weather.";
        } else if (forecast.equals("SW")) {
            weatherSuggestion = "Strong Winds. Stay warm.";
        } else if (forecast.equals("TL")) {
            weatherSuggestion = "Thundery Showers. Remember to bring your umbrella.";
        } else if (forecast.equals("WC")) {
            weatherSuggestion = "Windy & Cloudy.";
        } else if (forecast.equals("WD")) {
            weatherSuggestion = "Windy.";
        } else if (forecast.equals("WF")) {
            weatherSuggestion = "Windy & Fair. Enjoy your day.";
        } else if (forecast.equals("WR")) {
            weatherSuggestion = "Windy & Rain. Bring your umbrella.";
        } else if (forecast.equals("WS")) {
            weatherSuggestion = "Windy & Showers. Take your umbrella with you.";
        }
        return weatherSuggestion;
    }

    private String getUVIndexSuggestion(UVIndexResponse uvIndexResponse) {
        int uvIndex = uvIndexResponse.getUvIndexReading();
        String uvIndexSuggestion = "";
        // give suggestions
        if (uvIndex <= 2) {
            uvIndexSuggestion = "Low UV Index. No protection needed.";
        } else if (uvIndex <= 5) {
            uvIndexSuggestion = "Moderate UV Index. Put some sun lotion on and wear sunglasses.";
        } else if (uvIndex <= 7) {
            uvIndexSuggestion = "High UV Index. Put sun lotion on and wear sunglasses.";
        } else if (uvIndex <= 10) {
            uvIndexSuggestion = "Very High UV Index. Protect yourself from sunburn.";
        } else {
            uvIndexSuggestion = "Extreme UV Index. Protect yourself well.";
        }
        return uvIndexSuggestion;
    }

    private String getPSISuggestion(PSIData psiData) {
        int psiReading = psiData.getReading();
        String psiReadingSuggestion = "";
        // give suggestions
        if (psiReading <= 50) {
            psiReadingSuggestion = "Good air quality. The air is fresh.";
        } else if (psiReading <= 100) {
            psiReadingSuggestion = "Moderate level air quality. You don't need to worry about it.";
        } else if (psiReading <= 200) {
            psiReadingSuggestion = "Air is unhealthy. Maybe it's better to stay home.";
        } else if (psiReading <= 300) {
            psiReadingSuggestion = "Very unhealthy air. Don't go out";
        } else {
            psiReadingSuggestion = "Air is hazardous. Wear a mask if you have to go outside.";
        }
        return psiReadingSuggestion;
    }
}
