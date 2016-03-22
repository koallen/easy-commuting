package sg.edu.ntu.cz2006.seproject.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.edu.ntu.cz2006.seproject.Globals;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.presenter.MainPresenter;
import sg.edu.ntu.cz2006.seproject.MyApp;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.viewmodel.PlaceSuggestion;
import sg.edu.ntu.cz2006.seproject.R;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements OnMapReadyCallback, MainView {

    // bind views
    @Bind(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    private MaterialDialog mRequestDialog;
    private MaterialDialog mInfoDialog;
    private Location mLastLocation;
    private MapFragment mMapFragment;
    private GoogleMap mMap;

    // some constants
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // set listeners on the floating search view
        setSearchViewOnQueryChangeListener();
        setSearchViewOnSearchListener();
        setSearchViewOnMenuItemClickListener();

        // setup Google Maps
        setupMap();

        // initialize the progress dialog
        setupProgressDialog();

        // initialize the info dialog
        setupInfoDialog();
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

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // move the camera to Singapore at a zoom level of 10
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Globals.SINGAPORE_LOCATION, 10));
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        mLastLocation = GoogleApiHelper.getInstance().getLastLocation();
        if (mLastLocation != null) {
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            moveCamera(myLocation);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void moveCamera(LatLng location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void showData(String apiData, String destination) {
//        Intent nav = new Intent(MainActivity.this, NavigationActivity.class);
//        nav.putExtra("EXTRA_UVINDEX_DATA", apiData);
//        mRequestDialog.dismiss();
//        startActivity(nav);
        mInfoDialog.setTitle(destination);
        mInfoDialog.setContent(apiData);
        mInfoDialog.show();
    }

    @Override
    public void showLoading() {
        mRequestDialog.show();
    }

    @Override
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
    public void showError(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideRequestDialog() {
        mRequestDialog.dismiss();
    }

    @Override
    public void showSuggestions(List<PlaceSuggestion> suggesstions) {
        mSearchView.swapSuggestions(suggesstions);
    }

    @Override
    public void hideProgress() {
        mSearchView.hideProgress();
    }

    @Override
    public void showProgress() {
        mSearchView.showProgress();
    }

    @Override
    public void clearSuggestions() {
        mSearchView.clearSuggestions();
    }


    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            mSearchView.setSearchText(spokenText);
            String destination = mSearchView.getQuery();
            search(destination);
//            presenter.fetchUVIndexData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setSearchViewOnQueryChangeListener() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    presenter.getEmptyQuery();
                } else {
                    // ask presenter to get suggestions
                    presenter.getSuggesstions(newQuery);
                }
                //pass them on to the search view
                Log.d("MAINACTIVITY", "Suggestion changed");
            }
        });
    }

    private void setSearchViewOnSearchListener() {
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Log.d("MAINACTIVITY", "onSuggestionClicked()");
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
                String destination = searchSuggestion.getBody();
                search(destination);
//                presenter.getLatLong(destination);
            }

            @Override
            public void onSearchAction() {
                Log.d("MainAvtivity", "Search button presses");
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
                String destination = mSearchView.getQuery();
                search(destination);
//                presenter.getLatLong(destination);
            }
        });
    }

    private void setSearchViewOnMenuItemClickListener() {
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
                        displaySpeechRecognizer();
//                        Toast.makeText(getApplicationContext(), "Voice Search not available", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setupMap() {
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void setupProgressDialog() {
        mRequestDialog = new MaterialDialog.Builder(MainActivity.this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Log.d("MainActivity", "onShow()");
                    }
                })
                .build();
    }

    private void setupInfoDialog() {
        mInfoDialog = new MaterialDialog.Builder(this)
                .title("")
                .content("")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .cancelable(false)
                .build();
    }

    private void search(String destination) {
        mLastLocation = GoogleApiHelper.getInstance().getLastLocation();
        String origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
        Log.d("search()", destination);
        presenter.getRouteInfo(origin, destination);
    }
}
