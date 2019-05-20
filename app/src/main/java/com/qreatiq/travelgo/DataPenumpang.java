package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataPenumpang extends BaseActivity {
    LinearLayout linearBagasi, titleName_layout, idNo_layout, pasporNo_layout, email_layout, phone_layout;
    MaterialButton batal, submitBtn;
    TextView titleData;
    Spinner spinner_titel;
    TextInputEditText name;

    JSONObject data,depart_ticket,return_ticket,baggage_depart,baggage_arrive;
    RecyclerView list_baggage;
    ArrayList<JSONObject> baggage_array = new ArrayList<JSONObject>();
    PassengerBaggageAdapter baggage_adapter;

    int selected_baggage_index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penumpang);

        set_toolbar();

        Intent i = getIntent();
        String intentString = i.getStringExtra("packageName");

        if(i.hasExtra("data")) {
            try {
                data = new JSONObject(i.getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

            try {
                depart_ticket = new JSONObject(getIntent().getStringExtra("depart_ticket"));
                if(getIntent().getBooleanExtra("isReturn",false))
                    return_ticket = new JSONObject(getIntent().getStringExtra("return_ticket"));

                data = new JSONObject(i.getStringExtra("data"));

                String title = data.getString("title");
                if(!title.equals(""))
                    spinner_titel.setSelection(getIndex(spinner_titel,title));
                name.setText(data.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}
