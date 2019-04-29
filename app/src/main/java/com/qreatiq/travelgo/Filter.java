package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;

public class Filter extends AppCompatActivity {

    TextView minPrice, maxPrice;
    CrystalRangeSeekbar rangeSeekbar;

    ArrayList<String> arrayTime = new ArrayList<String>();
    ArrayList<String> arrayTimeArrive = new ArrayList<String>();
    TimeAdapter adapter;
    GridView gridDeparture, gridArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter.super.onBackPressed();
            }
        });

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPrice.setText(String.valueOf(minValue));
                maxPrice.setText(String.valueOf(maxValue));
            }
        });

        gridDeparture = (GridView) findViewById(R.id.gridDepart);
        gridArrival = (GridView) findViewById(R.id.gridArrive);

        arrayTime.add("00:00 - 11:00");
        arrayTime.add("11:00 - 15:00");
        arrayTime.add("15:00 - 18:30");
        arrayTime.add("18:30 - 23:59");

        adapter = new TimeAdapter(arrayTime,this);
        gridDeparture.setAdapter(adapter);
        gridArrival.setAdapter(adapter);

    }
}
