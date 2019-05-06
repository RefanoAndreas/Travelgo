package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FlightIsiDataPenumpang extends BaseActivity {
    LinearLayout linearBagasi, titleName_layout, idNo_layout, pasporNo_layout;
    MaterialButton batal;
    TextView titleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_isi_data_penumpang);

        link.setToolbar(this);

        Intent i = getIntent();
        String intentString = i.getStringExtra("packageName");
        Log.d("intentString", intentString);

        linearBagasi = (LinearLayout)findViewById(R.id.linearBagasi);
        titleData = (TextView)findViewById(R.id.titleData);
        titleName_layout = (LinearLayout) findViewById(R.id.titleName_layout);
        idNo_layout = (LinearLayout)findViewById(R.id.idNo_layout);
        pasporNo_layout = (LinearLayout)findViewById(R.id.pasporNo_layout);

        if(intentString.equals("tour") || intentString.equals("hotel")){
            getSupportActionBar().setTitle("Isi Data Tamu");
            titleData.setText("Data Tamu");
            linearBagasi.setVisibility(View.GONE);
            titleName_layout.setVisibility(View.GONE);
            idNo_layout.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);
        }
        else if(intentString.equals("flight")){
            idNo_layout.setVisibility(View.GONE);
        }
        else if(intentString.equals("train")){
            linearBagasi.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);
        }

        batal = (MaterialButton) findViewById(R.id.batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
