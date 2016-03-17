package sg.edu.ntu.cz2006.seproject;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {
    private String data = "";

    public void fetchUVIndexData() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        data = "";

        Observable<UVIndexResponse> uvdata = new NEAServiceRequestor().getUVIndex();

        uvdata.subscribe(new Subscriber<UVIndexResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("MAINPRESENTER", e.toString());
            }

            @Override
            public void onNext(UVIndexResponse uvIndexData) {
                data += uvIndexData.getUVIndexData().getUV();
            }
        });

        new NEAServiceRequestor().getPSI().subscribe(new Subscriber<PSIResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("MAINPRESENTER", e.toString());
            }

            @Override
            public void onNext(PSIResponse psiResponse) {
                data += psiResponse.getPsiData().getPsiRegions().toString();
            }
        });
    }
}
