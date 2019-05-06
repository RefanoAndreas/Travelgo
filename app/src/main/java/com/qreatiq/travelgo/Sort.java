package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Sort extends AppCompatActivity {

    ListView LV_sortItem;
    Intent intent;
    String intentString;
    String[] sortItem;
    ArrayAdapter<String> sortListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        link.setToolbar(this);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        LV_sortItem = (ListView)findViewById(R.id.LV_sortItem);

        if(intentString.equals("hotel")){
            sortItem = getResources().getStringArray(R.array.sort_hotel);
        }
        else{
            sortItem = getResources().getStringArray(R.array.sort_flight_train);
        }

        sortListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sortItem);
        LV_sortItem.setAdapter(sortListAdapter);

        LV_sortItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sort = sortItem[position];
                Intent i = new Intent();
                i.putExtra("sort", sort);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }
}
