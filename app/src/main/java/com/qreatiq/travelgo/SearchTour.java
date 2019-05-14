package com.qreatiq.travelgo;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTour extends AppCompatActivity {

    ImageView btnBack;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tour);

        ArrayList<SearchTourSpotList> spotList = new ArrayList<>();

        spotList.add(new SearchTourSpotList(R.drawable.background2, "Kuta, Bali"));
        spotList.add(new SearchTourSpotList(R.drawable.background3, "Lombok, NTB"));
        spotList.add(new SearchTourSpotList(R.drawable.background4, "Komodo, NTT"));
        spotList.add(new SearchTourSpotList(R.drawable.background5, "Madura, East Java"));
        spotList.add(new SearchTourSpotList(R.drawable.background6, "Bawean, East java"));

        mRecyclerView = findViewById(R.id.RV_spotWisata);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(mRecyclerView);
        mAdapter = new SearchTourSpotAdapter(spotList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        btnBack = (ImageView)findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTour.super.onBackPressed();
            }
        });
    }
}
