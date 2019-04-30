package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelSearchResult extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HotelListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> hotelList = new ArrayList<>();
    MaterialButton filterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search_result);

        link.setToolbar(this);

        try {
            hotelList.add(new JSONObject("{\"hotelName\": \"Fave Hotel\", " +
                    "\"hotelLocation\": \"Surabaya, Jawa Timur\", " +
                    "\"hotelReview\": \"Cozy\", " +
                    "\"hotelPrice\": 500.000}"));
            hotelList.add(new JSONObject("{\"hotelName\": \"Shangri-La Hotel\", " +
                    "\"hotelLocation\": \"Surabaya, Jawa Timur\", " +
                    "\"hotelReview\": \"Mewah\", " +
                    "\"hotelPrice\": 1.500.000}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_hotel_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HotelListAdapter(hotelList);

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
        startActivity(new Intent(HotelSearchResult.this, FilterHotel.class));
    }
}
