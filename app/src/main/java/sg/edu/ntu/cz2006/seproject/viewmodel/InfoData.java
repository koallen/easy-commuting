package sg.edu.ntu.cz2006.seproject.viewmodel;

import android.graphics.drawable.Drawable;

/**
 * A information holder class for displaying information in a recycler view
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

    /**
     * Returns the data
     * @return Data
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the suggestion
     * @return Suggestion
     */
    public String getSuggestion() {
        return suggestion;
    }

    /**
     * Returns the icon to display
     * @return Icon
     */
    public Drawable getIcon() {
        return mIcon;
    }
}
