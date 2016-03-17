package sg.edu.ntu.cz2006.seproject;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    public void fetchUVIndexData() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        new NEAServiceRequestor().getUVIndex()
                .subscribe(new Subscriber<List<UVIndex>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UVIndex> uvIndexes) {
                        if (isViewAttached()) {
                            getView().showData(uvIndexes.toString());
                        }
                    }
                });
    }
}
