package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class TourList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<TourListItem> tourListPackagesList = new ArrayList<>();
        tourListPackagesList.add(new TourListItem(R.drawable.background2, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background3, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background4, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background5, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourListPackagesList.add(new TourListItem(R.drawable.background6, "Rodex Tour", "11/04/2019", "12/04/2019"));

        mRecyclerView = findViewById(R.id.RV_tourListPackages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TourListAdapter(tourListPackagesList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
