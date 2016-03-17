package sg.edu.ntu.cz2006.seproject;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.textview)
    TextView text;
    @Bind(R.id.nav_rv)
    RecyclerView recyclerView;

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
        String apiData = "No UV Index data";
        if (extras != null) {
            apiData = extras.getString("EXTRA_UVINDEX_DATA");
        }
        // show it in TextView
        text.setText(apiData);

        List<InfoData> infoDataList = new ArrayList<InfoData>();
        infoDataList.add(new InfoData("UV Index: 0", "You can go out!"));
        infoDataList.add(new InfoData("PSI Index: 55", "Moderate level"));
        infoDataList.add(new InfoData("Weather: Sunny", "Go enjoy the sun"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));
        infoDataList.add(new InfoData("aaa", "bbbb"));


        InformationAdapter adapter = new InformationAdapter(infoDataList);
        recyclerView.setAdapter(adapter);

//        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.sheet_layout));
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
