package sg.edu.ntu.cz2006.seproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.nav_rv)
    RecyclerView recyclerView;

    private String apiData;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap mMap;
    private static final LatLng SINGAPORE = new LatLng(1.3, 103.8);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NavigationActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // get google api client
        mGoogleApiClient = MyApp.getGoogleApiHelper().getGoogleApiClient();
//        mLastLocation = MyApp.getGoogleApiHelper().getLastLocation();

        // get api data
        Bundle extras = getIntent().getExtras();
        apiData = "No UV Index data";
        if (extras != null) {
            apiData = extras.getString("EXTRA_UVINDEX_DATA");
        }

        List<InfoData> infoDataList = new ArrayList<InfoData>();
        infoDataList.add(new InfoData("UV Index: 0", "You can go out!"));
        infoDataList.add(new InfoData("PSI Index: 55", "Moderate level"));
        infoDataList.add(new InfoData("Weather: Sunny", "Go enjoy the sun"));
        infoDataList.add(new InfoData("Route: take 179", "It's gonna take a long time"));

        InformationAdapter adapter = new InformationAdapter(infoDataList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> waypoints = PolyUtil.decode(apiData);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SINGAPORE, 10));
        mMap.addPolyline(new PolylineOptions()
                .addAll(waypoints)
                .color(Color.BLUE));
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
        mLastLocation = MyApp.getGoogleApiHelper().getLastLocation();
        if (mLastLocation != null) {
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            locateUser(myLocation);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void locateUser(LatLng userLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    protected void onStart() {
        MyApp.getGoogleApiHelper().connect();
        super.onStart();
    }

    protected void onStop() {
        MyApp.getGoogleApiHelper().disconnect();
        super.onStop();
    }
}
