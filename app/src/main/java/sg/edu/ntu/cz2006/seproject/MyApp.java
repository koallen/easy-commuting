package sg.edu.ntu.cz2006.seproject;

import android.app.Application;

/**
 * Created by koAllen on 20/3/16.
 */
public class MyApp extends Application {

    private GoogleApiHelper mGoogleApiHelper;
    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mGoogleApiHelper = new GoogleApiHelper(getApplicationContext());
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.mGoogleApiHelper;
    }
    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }
}
