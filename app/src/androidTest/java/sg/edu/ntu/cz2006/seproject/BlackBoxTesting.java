package sg.edu.ntu.cz2006.seproject;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import sg.edu.ntu.cz2006.seproject.model.GeocoderHelper;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.PlaceSuggestion;

/**
 * Black box testing on GeocoderHelper
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlackBoxTesting extends ApplicationTestCase<Application> {
    public BlackBoxTesting() {
        super(Application.class);
    }

    @Test
    public void test1_getPlaceSuggestions1_getSingaporePlaces() {
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
    }

    @Test
    public void test1_getPlaceSuggestions2_getNoPlacesIfAddressIsInAnotherCountry() {
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

        Observable<List<PlaceSuggestion>> obs = geocoderHelper.getPlaceSuggestions("Los Angeles", googleApiClient, latLngBounds, autocompleteFilter);
        List<PlaceSuggestion> suggestions = obs.toBlocking().first();

        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void test1_getPlaceSuggestions3_getNoPlacesIfAddressIsInvalid() {
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

        Observable<List<PlaceSuggestion>> obs = geocoderHelper.getPlaceSuggestions("invalid", googleApiClient, latLngBounds, autocompleteFilter);
        List<PlaceSuggestion> suggestions = obs.toBlocking().first();

        assertTrue(suggestions.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void test3_1_getPlaceSuggestion_throwErrorIfNetworkNotAvailable() {
        WifiManager wifiManager = (WifiManager)MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);

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

        Observable<List<PlaceSuggestion>> obs = geocoderHelper.getPlaceSuggestions("some unknown place", googleApiClient, latLngBounds, autocompleteFilter);
        List<PlaceSuggestion> suggestions = obs.toBlocking().first();
    }

    @Test
    public void test2_getDestinationLatLng1_getCorrectLatLng() throws IOException {
        GeocoderHelper geocoderHelper = GeocoderHelper.getInstance();

        assertEquals(new LatLng(1.2966426, 103.7763939), geocoderHelper.getDestinationLatLng("National University of Singapore"));

        assertEquals(new LatLng(1.3483099, 103.6831347), geocoderHelper.getDestinationLatLng("Nanyang Technological University"));

        assertEquals(new LatLng(1.3010601, 103.85599), geocoderHelper.getDestinationLatLng("Bugis MRT Singapore"));

        assertEquals(new LatLng(1.3774142, 103.7719498), geocoderHelper.getDestinationLatLng("Bukit Panjang Singapore"));

        assertEquals(new LatLng(1.3612182, 103.8862529), geocoderHelper.getDestinationLatLng("Hougang Singapore"));

        assertEquals(new LatLng(1.3644202, 103.9915308), geocoderHelper.getDestinationLatLng("Changi Airport"));

        assertEquals(new LatLng(1.2815683, 103.8636132), geocoderHelper.getDestinationLatLng("Gardens by the Bay Singapore"));

        assertEquals(new LatLng(1.4043485, 103.793023), geocoderHelper.getDestinationLatLng("Singapore Zoo Singapore"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test2_getDestinationLatLng2_throwErrorIfAddressIsInvalid() throws IOException {
        GeocoderHelper geocoderHelper = GeocoderHelper.getInstance();

        geocoderHelper.getDestinationLatLng("some unknown place");
    }

    @Test
    public void test3_2_getDestinationLatLng_throwErrorIfNetworkNotAvailable() {
        WifiManager wifiManager = (WifiManager)MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);

        GeocoderHelper geocoderHelper = GeocoderHelper.getInstance();

        try {
            geocoderHelper.getDestinationLatLng("Changi Airport");
        } catch (IOException e) {
            assertEquals("java.io.IOException: Timed out waiting for response from server", e.toString());
        } finally {
            wifiManager.setWifiEnabled(true);
        }
    }
}