package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

public class Tour extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int[] sampleImages1 = {R.drawable.background2, R.drawable.background3, R.drawable.background4};
    int[] sampleImages2 = {R.drawable.background3, R.drawable.background4, R.drawable.background5};
    int[] sampleImages3 = {R.drawable.background4, R.drawable.background5, R.drawable.background6};
    int[] sampleImages4 = {R.drawable.background5, R.drawable.background6, R.drawable.background2};
    int[] sampleImages5 = {R.drawable.background6, R.drawable.background2, R.drawable.background3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        ArrayList<TourItem> tourList = new ArrayList<>();

        tourList.add(new TourItem(sampleImages1, "Kuta Bali Tour", "Rp 2.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages2, "Lombok NTB Tour", "Rp 3.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages3, "Komodo NTT Tour", "Rp 4.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages4, "Madura East Java Tour", "Rp 5.500.000", getResources().getString(R.string.cityDetail_Detail)));
        tourList.add(new TourItem(sampleImages5, "Bawean East Java Tour", "Rp 6.500.000", getResources().getString(R.string.cityDetail_Detail)));

        mRecyclerView = findViewById(R.id.tour_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
//        new GravitySnapHelper(Gravity.START).attachToRecyclerView(mRecyclerView);
        mAdapter = new TourAdapter(tourList, this);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
