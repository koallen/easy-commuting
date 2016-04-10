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


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.edu.ntu.cz2006.seproject.Globals;
import sg.edu.ntu.cz2006.seproject.model.GoogleApiHelper;
import sg.edu.ntu.cz2006.seproject.presenter.MainPresenter;
import sg.edu.ntu.cz2006.seproject.view.MainView;
import sg.edu.ntu.cz2006.seproject.model.PlaceSuggestion;
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
    private String mQuery;

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

    /**
     * Create the presenter corresponding to this activity class
     * @return Constructed presenter
     */
    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    /**
     * A callback function for initializing Google Maps
     * @param map The map that is created
     */
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

    /**
     * A listener function for handling user input
     * The map will move to user's current location if this button is pressed
     */
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

    /**
     * Moves the camera to a certain location
     * @param location The location to be moved to
     */
    @Override
    public void moveCamera(LatLng location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    /**
     * Displays suggestion for the destination from various API
     * @param apiData Suggestion obtained from API
     * @param destination User's destination
     */
    @Override
    public void showData(String apiData, String destination) {
        mInfoDialog.setTitle(destination);
        mInfoDialog.setContent(apiData);
        mInfoDialog.show();
    }

    /**
     * Starts another activity which displays the actual route
     * @param routeResponse Response from Google's API
     */
    @Override
    public void showRoute(String routeResponse) {
        Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
        intent.putExtra("EXTRA_ROUTE_RESPONSE", routeResponse);
        startActivity(intent);
    }

    /**
     * Display a progress dialog when loading data
     */
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

    /**
     * Displays a error message in the form of a toast message
     * @param errorMessage The error message to be displayed
     */
    @Override
    public void showError(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Hide the progress dialog when data is loaded
     */
    @Override
    public void hideRequestDialog() {
        mRequestDialog.dismiss();
    }

    /**
     * Change the suggestions in the search bar
     * @param suggesstions New suggestion for the user
     */
    @Override
    public void showSuggestions(List<PlaceSuggestion> suggesstions) {
        mSearchView.swapSuggestions(suggesstions);
    }

    /**
     * Hide the progress wheel of the search bar
     */
    @Override
    public void hideProgress() {
        mSearchView.hideProgress();
    }

    /**
     * Show the progress wheel of the search bar
     */
    @Override
    public void showProgress() {
        mSearchView.showProgress();
    }

    /**
     * Clear the search suggestions of the search bar
     */
    @Override
    public void clearSuggestions() {
        mSearchView.clearSuggestions();
    }

    /**
     * Clear the search text of the search bar
     */
    @Override
    public void clearSearchText() {
        mSearchView.setSearchText("");
    }

    /**
     * Create an intent that can start the Speech Recognizer activity
     */
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    /**
     * This callback is invoked when the Speech Recognizer returns.
     * This is where you process the intent and extract the speech text from the intent.
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data Speech recognized by voice search
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            mSearchView.setSearchText(spokenText);
            mQuery = spokenText;
//            String destination = mSearchView.getQuery();
            Log.d("getRouteVoice", mQuery);
            search(mQuery);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Set the query change listener for the search bar
     */
    private void setSearchViewOnQueryChangeListener() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                mQuery = newQuery;

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

    /**
     * Set the on search listener for the search bar
     */
    private void setSearchViewOnSearchListener() {
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Log.d("MAINACTIVITY", "onSuggestionClicked()");
                String destination = ((PlaceSuggestion) searchSuggestion).getFullAddress();
                search(destination);
            }

            @Override
            public void onSearchAction() {
                Log.d("MainAvtivity", "Search button presses");
                Log.d("onSearchAction()", mQuery);
                search(mQuery);
            }
        });
    }

    /**
     * Set the on menu click listener
     */
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
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.getRouteInfo();
                    }
                })
                .build();
    }

    private void search(String destination) {
        hideProgress();
        mLastLocation = GoogleApiHelper.getInstance().getLastLocation();
        String origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
        Log.d("search()", destination);
        presenter.getDestinationInfo(origin, destination);
    }
}
