package com.qreatiq.travelgo;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelSearchResult extends BaseActivity {

    private RecyclerView mRecyclerView;
    private HotelListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> hotelList = new ArrayList<>();
    MaterialButton filterBtn;
    int SORT = 10, FILTER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search_result);

        set_toolbar();

        try {
            hotelList.add(new JSONObject("{\"name\": \"Fave Hotel\", " +
                    "\"location\": \"Surabaya, Jawa Timur\", " +
                    "\"rating\": 3.0, " +
                    "\"price\": 500000}"));
            hotelList.add(new JSONObject("{\"name\": \"Shangri-La Hotel\", " +
                    "\"location\": \"Surabaya, Jawa Timur\", " +
                    "\"rating\": 5.0, " +
                    "\"price\": 1500000}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_hotel_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HotelListAdapter(hotelList,this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new HotelListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(HotelSearchResult.this, HotelDetail.class));
            }
        });
    }

    public void viewFilter(View v){
        startActivityForResult(new Intent(HotelSearchResult.this, Filter.class).putExtra("type", "hotel"), FILTER);
    }

    public void sortView(View v){
        startActivityForResult(new Intent(HotelSearchResult.this, Sort.class).putExtra("origin", "hotel"), SORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SORT){

            }
            else if(requestCode == FILTER){

            }
        }
    }
}
