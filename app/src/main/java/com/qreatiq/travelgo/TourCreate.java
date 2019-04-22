package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class TourCreate extends AppCompatActivity {

    private RecyclerView mRecyclerView_1, mRecyclerView_2;
    private RecyclerView.Adapter mAdapter_1, mAdapter_2;
    private RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<TourCreateItem_1> tourCreatePackagesList_1 = new ArrayList<>();
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.upload_photo, R.drawable.ic_add_a_photo_888888_24dp, "Upload Photo"));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background2, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background3, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background4, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background5, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background6, 0,null));

        mRecyclerView_1 = findViewById(R.id.RV_tourCreatePackages_1);
        mRecyclerView_1.setHasFixedSize(true);
        mLayoutManager_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter_1 = new TourCreateAdapter_1(tourCreatePackagesList_1);

        mRecyclerView_1.setLayoutManager(mLayoutManager_1);
        mRecyclerView_1.setAdapter(mAdapter_1);

        ArrayList<TourCreateItem_2> tourCreatePackagesList = new ArrayList<>();
        tourCreatePackagesList.add(new TourCreateItem_2(R.drawable.background2, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourCreatePackagesList.add(new TourCreateItem_2(R.drawable.background3, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourCreatePackagesList.add(new TourCreateItem_2(R.drawable.background4, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourCreatePackagesList.add(new TourCreateItem_2(R.drawable.background5, "Rodex Tour", "11/04/2019", "12/04/2019"));
        tourCreatePackagesList.add(new TourCreateItem_2(R.drawable.background6, "Rodex Tour", "11/04/2019", "12/04/2019"));

        mRecyclerView_2 = findViewById(R.id.RV_tourCreatePackages_2);
        mRecyclerView_2.setHasFixedSize(true);
        mLayoutManager_2 = new LinearLayoutManager(this);
        mAdapter_2 = new TourCreateAdapter_2(tourCreatePackagesList);

        mRecyclerView_2.setLayoutManager(mLayoutManager_2);
        mRecyclerView_2.setAdapter(mAdapter_2);
    }
}
