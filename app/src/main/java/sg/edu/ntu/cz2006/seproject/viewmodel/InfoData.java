package sg.edu.ntu.cz2006.seproject.viewmodel;

import android.graphics.drawable.Drawable;

/**
 * Created by koAllen on 17/3/16.
 */
public class InfoData {
    private String data;
    private String suggestion;
    private Drawable mIcon;

    public InfoData(String data, String suggestion, Drawable icon) {
        this.data = data;
        this.suggestion = suggestion;
        mIcon = icon;
    }

    public String getData() {
        return data;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
