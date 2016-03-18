package sg.edu.ntu.cz2006.seproject;

import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
//    @Bind(R.id.textview)
//    TextView text;
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
//        text.setText(apiData);

        List<InfoData> infoDataList = new ArrayList<InfoData>();
        infoDataList.add(new InfoData("UV Index: 0", "You can go out!"));
        infoDataList.add(new InfoData("PSI Index: 55", "Moderate level"));
        infoDataList.add(new InfoData("Weather: Sunny", "Go enjoy the sun"));

        InformationAdapter adapter = new InformationAdapter(infoDataList);
        recyclerView.setAdapter(adapter);

//        new BottomSheet.Builder(this).title("title").sheet(R.layout.sheet_layout).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//
//                }
//            }
//        }).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LinearLayout bottomSheetViewgroup
//                = (LinearLayout) findViewById(R.id.bottom_sheet);
//
//        BottomSheetBehavior bottomSheetBehavior =
//                BottomSheetBehavior.from(bottomSheetViewgroup);
//
//        bottomSheetBehavior.setPeekHeight(70);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
