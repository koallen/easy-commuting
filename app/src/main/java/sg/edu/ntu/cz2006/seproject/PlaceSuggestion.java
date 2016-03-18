package sg.edu.ntu.cz2006.seproject;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by koAllen on 18/3/16.
 */
public class PlaceSuggestion implements SearchSuggestion {

    private String suggestion;

    public PlaceSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public PlaceSuggestion(Parcel parcel) {
        this.suggestion = parcel.readString();
    }

    @Override
    public String getBody() {
        return suggestion;
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(suggestion);
    }

    public static final Creator<PlaceSuggestion> CREATOR = new Creator<PlaceSuggestion>() {
        @Override
        public PlaceSuggestion createFromParcel(Parcel in) {
            return new PlaceSuggestion(in);
        }

        @Override
        public PlaceSuggestion[] newArray(int size) {
            return new PlaceSuggestion[size];
        }
    };
}
