package sg.edu.ntu.cz2006.seproject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by koAllen on 16/3/16.
 */
public interface NEAService {
    @GET("api/WebAPI/")
    Observable<ResponseBody> listUVIndex(
            @Query("dataset") String dataset,
            @Query("keyref") String apiKey);
}
