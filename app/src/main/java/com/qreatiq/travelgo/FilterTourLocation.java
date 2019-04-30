package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class FilterTourLocation extends AppCompatActivity {

    GridView gridViewLocation;
    ArrayList<String> arrayLocation = new ArrayList<String>();
    FilterTourLocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour_location);

        link.setToolbar(this);

        gridViewLocation = (GridView) findViewById(R.id.GV_selectLocation);

        arrayLocation.add("Ubud");
        arrayLocation.add("Kuta");
        arrayLocation.add("Jimbaran");
        arrayLocation.add("Legian");
        arrayLocation.add("Uluwatu");
        arrayLocation.add("Kintamani");
        arrayLocation.add("GWK");
        arrayLocation.add("Bedugul");
        arrayLocation.add("Denpasar");

        adapter = new FilterTourLocationAdapter(arrayLocation,this);
        gridViewLocation.setAdapter(adapter);


    }
}
