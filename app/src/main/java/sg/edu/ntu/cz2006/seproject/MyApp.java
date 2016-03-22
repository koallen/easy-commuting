package sg.edu.ntu.cz2006.seproject;

import android.app.Application;
import android.content.Context;

import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;

/**
 * Created by koAllen on 20/3/16.
 */
public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
