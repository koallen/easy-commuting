package sg.edu.ntu.cz2006.seproject.model;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Definitions for requesting LTA API
 */
public interface LTAService {
    @Headers({
            "Accept: application/json"
    })
    @GET("BusArrival")
    Observable<BusArrivalResponse> getBusArrival(
            @Header("AccountKey") String accountKey,
            @Header("UniqueUserID") String uniqueUserID,
            @Query("BusStopID") String busStopID
    );

//    @Headers({
//            "Accept: application/json"
//    })
//    @GET("BusStops")

}
