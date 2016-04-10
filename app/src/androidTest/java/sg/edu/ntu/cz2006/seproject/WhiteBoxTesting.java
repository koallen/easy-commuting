package sg.edu.ntu.cz2006.seproject;

import android.app.Application;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

import rx.Observable;
import sg.edu.ntu.cz2006.seproject.model.GeocoderHelper;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.PlaceSuggestion;
import sg.edu.ntu.cz2006.seproject.model.SuggestionGenerationHelper;
import sg.edu.ntu.cz2006.seproject.model.UVIndexResponse;

/**
 * White box testing on getUVIndexSuggestion
 * and getPlaceSuggestions
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WhiteBoxTesting extends ApplicationTestCase<Application> {
    public WhiteBoxTesting() {
        super(Application.class);
    }

    @Test
    public void test1_getUVIndexSuggestion() {
        SuggestionGenerationHelper suggestionGenerationHelper = SuggestionGenerationHelper.getInstance();

        UVIndexResponse uvIndexResponse = new UVIndexResponse(1);
        assertEquals("Low UV Index. No protection needed.", suggestionGenerationHelper.getUVIndexSuggestion(uvIndexResponse));

        uvIndexResponse = new UVIndexResponse(4);
        assertEquals("Moderate UV Index. Put some sun lotion on and wear sunglasses.", suggestionGenerationHelper.getUVIndexSuggestion(uvIndexResponse));

        uvIndexResponse = new UVIndexResponse(6);
        assertEquals("High UV Index. Put sun lotion on and wear sunglasses.", suggestionGenerationHelper.getUVIndexSuggestion(uvIndexResponse));

        uvIndexResponse = new UVIndexResponse(8);
        assertEquals("Very High UV Index. Protect yourself from sunburn.", suggestionGenerationHelper.getUVIndexSuggestion(uvIndexResponse));

        uvIndexResponse = new UVIndexResponse(11);
        assertEquals("Extreme UV Index. Protect yourself well.", suggestionGenerationHelper.getUVIndexSuggestion(uvIndexResponse));
    }

    @Test
    public void test2_getPlaceSuggestions() {
        GeocoderHelper geocoderHelper = GeocoderHelper.getInstance();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(MyApp.getContext())
                .addConnectionCallbacks(GoogleApiHelper.getInstance())
                .addOnConnectionFailedListener(GoogleApiHelper.getInstance())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        googleApiClient.connect();
        // initialize latitude and longitude bound of Singapore
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(1.19, 103.59))
                .include(new LatLng(1.46, 104.03))
                .build();
        // initialize autocomplete filter
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();

        Observable<List<PlaceSuggestion>> obs = geocoderHelper.getPlaceSuggestions("Jalan", googleApiClient, latLngBounds, autocompleteFilter);
        List<PlaceSuggestion> suggestions = obs.toBlocking().first();

        for (PlaceSuggestion suggestion : suggestions) {
            assertTrue(suggestion.getFullAddress().contains("Singapore"));
        }

        obs = geocoderHelper.getPlaceSuggestions("Los Angeles", googleApiClient, latLngBounds, autocompleteFilter);
        suggestions = obs.toBlocking().first();

        assertTrue(suggestions.isEmpty());
    }
}
