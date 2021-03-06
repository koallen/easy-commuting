package sg.edu.ntu.cz2006.seproject.model;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * A class for holding place suggestion for search
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

    /**
     * Returns the body of the suggestion
     * @return Address's primary text
     */
    @Override
    public String getBody() {
        return mPrimary;
    }

    /**
     * Returns the full address
     * @return Full address
     */
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
