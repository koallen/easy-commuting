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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.edu.ntu.cz2006.seproject.Globals;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.model.NavigationHelper;
import sg.edu.ntu.cz2006.seproject.model.RouteResponse;
import sg.edu.ntu.cz2006.seproject.model.Step;
import sg.edu.ntu.cz2006.seproject.view.NavigationView;
import sg.edu.ntu.cz2006.seproject.viewmodel.InfoData;
import sg.edu.ntu.cz2006.seproject.viewmodel.InformationAdapter;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.presenter.NavigationPresenter;
import sg.edu.ntu.cz2006.seproject.R;

public class NavigationActivity extends MvpActivity<NavigationView, NavigationPresenter> implements OnMapReadyCallback, NavigationView {
    @Bind(R.id.nav_rv)
    RecyclerView recyclerView;
    @Bind(R.id.draggable_text)
    TextView mTextView;
    @BindDrawable(R.drawable.ic_directions_bus_24dp)
    Drawable mBusIcon;
    @BindDrawable(R.drawable.ic_directions_walk_24dp)
    Drawable mWalkIcon;

    private RouteResponse mRouteResponse;
    private Location mLastLocation;
    private MapFragment mMapFragment;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        // setting up Google Maps
        setupMap();

        // setting up recycler view
        setupRecyclerView();

        // get api data
        Bundle extras = getIntent().getExtras();
        String routeResponse = "";
        if (extras != null) {
            routeResponse = extras.getString("EXTRA_ROUTE_RESPONSE");
            mRouteResponse = NavigationHelper.getInstance().getNavigationList(routeResponse);
        }

        mTextView.setText(mRouteResponse.getDuration());

        List<InfoData> infoDataList = new ArrayList<>();

        for (Step step : mRouteResponse.getSteps()) {
            if (step.getTravelMode().equals("TRANSIT")) {
                infoDataList.add(new InfoData("BUS " + step.getTransit().getBusLine().getBusLineName(), step.getInstruction(), mBusIcon));
            } else {
                infoDataList.add(new InfoData("WALKING", step.getInstruction(), mWalkIcon));
            }
        }
//        infoDataList.add(new InfoData("UV Index: 0", "You can go out!"));
//        infoDataList.add(new InfoData("PSI Index: 55", "Moderate level"));
//        infoDataList.add(new InfoData("Weather: Sunny", "Go enjoy the sun"));
//        infoDataList.add(new InfoData("Route: take 179", "It's gonna take a long time"));

        InformationAdapter adapter = new InformationAdapter(infoDataList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> waypoints = PolyUtil.decode(mRouteResponse.getRoute().getPolyline().getPoints());
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Globals.SINGAPORE_LOCATION, 10));
        mMap.addPolyline(new PolylineOptions()
                .addAll(waypoints)
                .color(Color.rgb(84, 178, 250)));
        mMap.addMarker(new MarkerOptions()
                .position(mRouteResponse.getDestination()));
    }

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

    public void locateUser(LatLng userLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    protected void onStart() {
        // connect to google api client if it's not connected
        if (!GoogleApiHelper.getInstance().isConnected()) {
            GoogleApiHelper.getInstance().connect();
        }
        super.onStart();
    }

    protected void onStop() {
        GoogleApiHelper.getInstance().disconnect();
        super.onStop();
    }

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

    @Override
    public void showData(String apiData) {

    }

    public void showMarker(LatLng location, String address, String snippet) {
        // remove previous added marker
        mMap.clear();
        // add the new marker
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(address))
                .setSnippet(snippet);
        // move camera to the marker
        moveCamera(location);
    }

    @Override
    public void moveCamera(LatLng location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}
