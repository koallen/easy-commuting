package sg.edu.ntu.cz2006.seproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.nav_rv)
    RecyclerView recyclerView;
    private String apiData;
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
        List<LatLng> waypoints = PolyUtil.decode(apiData);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SINGAPORE, 10));
        googleMap.addPolyline(new PolylineOptions()
                .addAll(waypoints)
                .color(Color.BLUE));
    }
}
