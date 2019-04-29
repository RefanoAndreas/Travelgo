package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

public class ChangeDateActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDateActivity.super.onBackPressed();
            }
        });

        ArrayList<CityDetailItem> cityList = new ArrayList<>();

        cityList.add(new CityDetailItem(R.drawable.background2, "Kuta, Bali"));
        cityList.add(new CityDetailItem(R.drawable.background3, "Lombok, NTB"));
        cityList.add(new CityDetailItem(R.drawable.background4, "Komodo, NTT"));
        cityList.add(new CityDetailItem(R.drawable.background5, "Madura, East Java"));
        cityList.add(new CityDetailItem(R.drawable.background6, "Bawean, East java"));

        mRecyclerView = findViewById(R.id.RV_cityDetail);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(mRecyclerView);
        mAdapter = new CityDetailAdapter(cityList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
