package sg.edu.ntu.cz2006.seproject;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by koAllen on 15/3/16.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    private UVIndexDataTask uvIndexDataTask;

    private void cancelUVIndexDataTaskIfRunning() {
        if (uvIndexDataTask != null) {
            uvIndexDataTask.cancel(true);
        }
    }

    public void fetchUVIndexData() {
        cancelUVIndexDataTaskIfRunning();

        uvIndexDataTask = new UVIndexDataTask(new UVIndexDataTask.UVIndexDataTaskListener() {
            @Override
            public void onDataFetched(String apiData) {
                if (isViewAttached()) {
                    getView().showData(apiData);
                }
            }
        });

        uvIndexDataTask.execute();
    }

    public void detachView(boolean retainPresenterInstance){
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance){
            cancelUVIndexDataTaskIfRunning();
        }
    }
}
