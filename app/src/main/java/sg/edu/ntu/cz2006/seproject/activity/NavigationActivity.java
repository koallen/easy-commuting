package sg.edu.ntu.cz2006.seproject.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.view.NavigationView;
import sg.edu.ntu.cz2006.seproject.viewmodel.InfoData;
import sg.edu.ntu.cz2006.seproject.viewmodel.InformationAdapter;
import sg.edu.ntu.cz2006.seproject.presenter.NavigationPresenter;
import sg.edu.ntu.cz2006.seproject.R;

/**
 * The activity for showing route on map and relevant route information
 */
public class NavigationActivity extends MvpActivity<NavigationView, NavigationPresenter> implements OnMapReadyCallback, NavigationView {

    @Bind(R.id.nav_rv)
    RecyclerView recyclerView;
    @Bind(R.id.draggable_text)
    TextView mTextView;
    @BindDrawable(R.drawable.ic_directions_bus_24dp)
    Drawable mBusIcon;
    @BindDrawable(R.drawable.ic_directions_walk_24dp)
    Drawable mWalkIcon;

    private Location mLastLocation;
    private MapFragment mMapFragment;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        // setting up recycler view
        setupRecyclerView();

        // setting up Google Maps
        setupMap();

        // get api data
        Bundle extras = getIntent().getExtras();
        String routeResponse = "";
        if (extras != null) {
            Log.d("NavActivity", "Got intent message");
            routeResponse = extras.getString("EXTRA_ROUTE_RESPONSE");
            presenter.getRouteInfo(routeResponse, mBusIcon, mWalkIcon);
        }
    }

    /**
     * A callback for initializing Google Maps
     * @param googleMap The map that is created
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        presenter.getRoute();
    }

    /**
     * A listener function for handling FAB button clicking.
     * The user's current location will be shown on map if the button is clicked.
     */
    @OnClick(R.id.fab)
    public void onFabClicked() {
        mLastLocation = GoogleApiHelper.getInstance().getLastLocation();
        if (mLastLocation != null) {
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            locateUser(myLocation);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show user's current location on map
     * @param userLocation User's current location
     */
    public void locateUser(LatLng userLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    /**
     * connect to Google Api Client during onStart()
     */
    protected void onStart() {
        presenter.connectGoogleApiClient();
        super.onStart();
    }

    /**
     * disconnect from Google Api Client during onStop()
     */
    protected void onStop() {
        presenter.disconnectGoogleApiClient();
        super.onStop();
    }

    /**
     * Constructs the corresponding presenter for this activity
     * @return The constructed presenter
     */
    @NonNull
    @Override
    public NavigationPresenter createPresenter() {
        return new NavigationPresenter();
    }

    private void setupMap() {
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NavigationActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    /**
     * Display route information in the bottom sheet
     * @param infoDataList A step by step navigation
     * @param eta Estimated arrival time
     */
    @Override
    public void showRouteInfo(List<InfoData> infoDataList, String eta) {
        // display ETA
        mTextView.setText(eta);
        // display navigation information
        InformationAdapter adapter = new InformationAdapter(infoDataList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Display route on map
     * @param route A list of points
     * @param destination The location of destination
     */
    @Override
    public void showRoute(List<LatLng> route, LatLng destination) {
        mMap.addPolyline(new PolylineOptions()
                .addAll(route)
                .color(Color.rgb(84, 178, 250)));
        mMap.addMarker(new MarkerOptions()
                .position(destination));
    }

    /**
     * Move the camera to a certain location
     * @param location The location to be moved to.
     */
    @Override
    public void moveCamera(LatLng location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
    }
}
