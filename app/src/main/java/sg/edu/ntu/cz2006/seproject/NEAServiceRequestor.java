package sg.edu.ntu.cz2006.seproject;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 17/3/16.
 */
public class NEAServiceRequestor {
    private Retrofit retrofit;
    private NEAService service;

    public NEAServiceRequestor() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://www.nea.gov.sg/")
                .build();
        service = retrofit.create(NEAService.class);
    }

    public Observable<ResponseBody> getUVIndex() {
        return service.listUVIndex("uvi", "781CF461BB6606AD62B1E1CAA87ECA612A87DF33A3ECDC11")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
