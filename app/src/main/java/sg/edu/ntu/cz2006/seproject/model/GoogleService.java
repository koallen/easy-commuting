package sg.edu.ntu.cz2006.seproject.model;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Definitions for requesting Google API
 */
public interface GoogleService {
    @GET("maps/api/directions/json")
    Observable<ResponseBody> getRoute(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String apiKey,
            @Query("mode") String travelMode,
            @Query("avoid") String avoid,
            @Query("region") String region,
            @Query("transit_mode") String transitMode,
            @Query("transit_routing_preference") String transitPreference
    );
}
