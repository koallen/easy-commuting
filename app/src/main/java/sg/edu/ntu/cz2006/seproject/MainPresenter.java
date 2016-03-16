package sg.edu.ntu.cz2006.seproject;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    public void fetchUVIndexData() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        new NEAServiceRequestor().getUVIndex()
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (isViewAttached())
                            try {
                                getView().showData(responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                });
    }
}
