package sg.edu.ntu.cz2006.seproject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by koAllen on 16/3/16.
 */
public interface NEAService {
    @GET("api/WebAPI/")
    Observable<UVIndexResponse> listUVIndex(
            @Query("dataset") String dataset,
            @Query("keyref") String apiKey
    );

    @GET("api/WebAPI/")
    Observable<PSIResponse> listPSI(
            @Query("dataset") String dataset,
            @Query("keyref") String apiKey
    );
}
