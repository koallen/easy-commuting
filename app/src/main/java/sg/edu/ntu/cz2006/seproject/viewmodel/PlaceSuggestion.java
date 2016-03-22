package sg.edu.ntu.cz2006.seproject.viewmodel;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.location.places.AutocompletePrediction;

/**
 * Created by koAllen on 18/3/16.
 */
public class PlaceSuggestion implements SearchSuggestion {

    private String mPrimary;
    private String mFull;

    public PlaceSuggestion(String primary, String full) {
        mPrimary = primary;
        mFull = full;
    }

    public PlaceSuggestion(Parcel parcel) {
        mPrimary = parcel.readString();
    }

    @Override
    public String getBody() {
        return mPrimary;
    }

    public String getFullAddress() {
        return mFull;
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
        dest.writeString(mPrimary);
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
