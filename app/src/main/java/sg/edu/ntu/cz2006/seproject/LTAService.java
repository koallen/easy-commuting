package sg.edu.ntu.cz2006.seproject;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by koAllen on 17/3/16.
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
