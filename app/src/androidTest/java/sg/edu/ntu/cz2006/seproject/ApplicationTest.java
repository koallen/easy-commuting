package sg.edu.ntu.cz2006.seproject;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.cz2006.seproject.model.GeocoderHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void getDestinationLatLng1_getCorrectLatLng() throws IOException {
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
    public void getDestinationLatLng2_throwErrorIfAddressIsInvalid() throws IOException {
        GeocoderHelper geocoderHelper = GeocoderHelper.getInstance();

        geocoderHelper.getDestinationLatLng("some unknown place");
    }

    @Test
    public void getDestinationLatLng3_throwErrorIfNetworkNotAvailable() {
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