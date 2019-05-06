package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Filter extends BaseActivity {

    TextView minPrice, maxPrice;
    CrystalRangeSeekbar rangeSeekbar;

    ArrayList<String> arrayTime = new ArrayList<String>();
    ArrayList<String> arrayTimeArrive = new ArrayList<String>();
    TimeAdapter adapter;
    GridView gridDeparture, gridArrival;
    MaterialButton submitBtn;

    LinearLayout kelas,transit;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        link.setToolbar(this);

        intent = getIntent();

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);
        kelas = (LinearLayout) findViewById(R.id.kelas);
        transit = (LinearLayout) findViewById(R.id.transit);
        submitBtn = (MaterialButton)findViewById(R.id.submit_saveChanges_filter);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(minValue);
                minPrice.setText("Rp. "+formattedNumber);

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(maxValue);
                maxPrice.setText("Rp. "+formattedNumber1);
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

        if(intent.getStringExtra("type").equals("flight")){
            kelas.setVisibility(View.GONE);
            transit.setVisibility(View.VISIBLE);
        }
        else{
            transit.setVisibility(View.GONE);
            kelas.setVisibility(View.VISIBLE);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("filter", "filter");
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
