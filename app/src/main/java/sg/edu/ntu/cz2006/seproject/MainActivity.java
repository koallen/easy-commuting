package sg.edu.ntu.cz2006.seproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements OnMapReadyCallback, MainView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FloatingSearchView mSearchView;
    private GoogleApiClient mGoogleApiClient;
    private MaterialDialog mRequestDialog;
    private Location mLastLocation;
    private LatLngBounds mLatLngBounds;
    private AutocompleteFilter mAutoCompleteFilter;
    private GoogleMap mMap;

    private static final LatLng SINGAPORE = new LatLng(1.3, 103.8);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Drawer drawer = new DrawerBuilder().withActivity(this).build();

        mRequestDialog = new MaterialDialog.Builder(MainActivity.this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
//                .cancelable(false)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Log.d("MainActivity", "onShow()");
                    }
                })
                .build();

        mLatLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(1.19, 103.59))
                .include(new LatLng(1.46, 104.03))
                .build();

        mAutoCompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    //get suggestions based on newQuery
                    PendingResult<AutocompletePredictionBuffer> result =
                            Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, newQuery,
                                    mLatLngBounds, mAutoCompleteFilter);

                    result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                            List<PlaceSuggestion> newSuggestions = new ArrayList<PlaceSuggestion>();

                            for (int i = 0; i < 4; ++i) {
                                try {
                                    newSuggestions.add(new PlaceSuggestion(autocompletePredictions.get(i).getPrimaryText(null).toString()));
                                } catch (Exception e) {
                                }

//                            Log.d("PLACES", prediction.getPrimaryText(null).toString());
                            }

                            autocompletePredictions.release();
                            mSearchView.swapSuggestions(newSuggestions);
                            mSearchView.hideProgress();
                        }
                    });
                }

                //pass them on to the search view
                Log.d("MAINACTIVITY", "Suggestion changed");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Log.d("MAINACTIVITY", "onSuggestionClicked()");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                String origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                String destination = searchSuggestion.getBody();
                presenter.fetchUVIndexData(origin, destination);
            }

            @Override
            public void onSearchAction() {
                Log.d("MainAvtivity", "Search button presses");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                String origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                String destination = mSearchView.getQuery();
                presenter.fetchUVIndexData(origin, destination);
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.action_settings:
                        Intent about = new Intent(MainActivity.this.getBaseContext(), AboutActivity.class);
                        startActivity(about);
                        break;
                    case R.id.action_search:
                        Toast.makeText(getApplicationContext(), "Voice Search not available", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
//                drawer.openDrawer();
//                mSearchView.
            }

            @Override
            public void onMenuClosed() {

            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {
                mSearchView.closeMenu(false);
            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // move the camera to Singapore at a zoom level of 10
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SINGAPORE, 10));
        // enable my location
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
        mMap.setMyLocationEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        locateUser(myLocation);
    }

    @Override
    public void locateUser(LatLng userLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    @Override
    public void showData(String apiData) {
        Intent nav = new Intent(MainActivity.this, NavigationActivity.class);
        nav.putExtra("EXTRA_UVINDEX_DATA", apiData);
        mRequestDialog.dismiss();
        startActivity(nav);
    }

    @Override
    public void onConnected(Bundle bundle) {
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showLoading() {
        mRequestDialog.show();
    }
}
