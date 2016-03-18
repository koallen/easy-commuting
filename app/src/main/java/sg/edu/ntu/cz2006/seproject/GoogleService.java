package sg.edu.ntu.cz2006.seproject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by koAllen on 18/3/16.
 */
public interface GoogleService {
    @GET("maps/api/directions/json")
    Observable<RouteResponse> getRoute(
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
