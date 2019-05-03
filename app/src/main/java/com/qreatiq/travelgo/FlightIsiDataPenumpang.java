package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class FlightIsiDataPenumpang extends AppCompatActivity {
    LinearLayout linearBagasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_isi_data_penumpang);

        link.setToolbar(this);

        linearBagasi = (LinearLayout)findViewById(R.id.linearBagasi);

        Intent i = getIntent();
        String intentString = i.getStringExtra("packageName");

        if(intentString.equals("tour")){
            linearBagasi.setVisibility(View.GONE);
        }

    }
}
