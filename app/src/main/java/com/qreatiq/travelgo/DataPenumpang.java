package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DataPenumpang extends BaseActivity {
    LinearLayout linearBagasi, titleName_layout, idNo_layout, pasporNo_layout, email_layout, phone_layout;
    MaterialButton batal, submitBtn;
    TextView titleData;
    Spinner spinner_titel;
    TextInputEditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penumpang);

        link.setToolbar(this);

        Intent i = getIntent();
        String intentString = i.getStringExtra("packageName");

        linearBagasi = (LinearLayout)findViewById(R.id.linearBagasi);
        titleData = (TextView)findViewById(R.id.titleData);
        titleName_layout = (LinearLayout) findViewById(R.id.titleName_layout);
        idNo_layout = (LinearLayout)findViewById(R.id.idNo_layout);
        pasporNo_layout = (LinearLayout)findViewById(R.id.pasporNo_layout);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
        submitBtn = (MaterialButton)findViewById(R.id.submitBtn);

        spinner_titel = (Spinner)findViewById(R.id.spinner_titel);
        name = (TextInputEditText)findViewById(R.id.TIET_name);

        if(intentString.equals("tour") || intentString.equals("hotel")){
            getSupportActionBar().setTitle("Isi Data Tamu");
            titleData.setText("Data Tamu");
            linearBagasi.setVisibility(View.GONE);
//            titleName_layout.setVisibility(View.GONE);
            idNo_layout.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);

            if(!intentString.equals("hotel")) {
                email_layout.setVisibility(View.GONE);
                phone_layout.setVisibility(View.GONE);
            }
        }
        else if(intentString.equals("flight")){
            idNo_layout.setVisibility(View.GONE);
            email_layout.setVisibility(View.GONE);
            phone_layout.setVisibility(View.GONE);
        }
        else if(intentString.equals("train")){
            linearBagasi.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);
            email_layout.setVisibility(View.GONE);
            phone_layout.setVisibility(View.GONE);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("title", spinner_titel.getSelectedItem().toString());
                    json.put("name", name.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent in = new Intent();
                in.putExtra("data", json.toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });

        batal = (MaterialButton) findViewById(R.id.batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
