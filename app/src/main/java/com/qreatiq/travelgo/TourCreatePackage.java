package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class TourCreatePackage extends AppCompatActivity {

    TextInputEditText name,price;
    TextInputLayout name_layout,price_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create_package);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextInputEditText) findViewById(R.id.name);
        price = (TextInputEditText) findViewById(R.id.price);
        name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        price_layout = (TextInputLayout) findViewById(R.id.price_layout);
    }

    public void submit(View v){
        if(name.getText().toString().equals(""))
            name_layout.setError("Name is empty");
        else if(price.getText().toString().equals(""))
            price_layout.setError("Price is empty");
        else {
            JSONObject json = new JSONObject();
            try {
                json.put("name", name.getText().toString());
                json.put("price", price.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent in = new Intent();
            in.putExtra("data", json.toString());
            setResult(RESULT_OK, in);
            finish();
        }
    }
}
