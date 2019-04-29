package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeDateActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChangeDateAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date);

        link.setToolbar(this);

        try {
            dateList.add(new JSONObject("{\"day\": \"Senin\", \"date\": \"29 Apr 2019\", \"price\": \"1.000.000\"}"));
            dateList.add(new JSONObject("{\"day\": \"Selasa\", \"date\": \"30 Apr 2019\", \"price\": \"2.000.000\"}"));
            dateList.add(new JSONObject("{\"day\": \"Rabu\", \"date\": \"01 Mei 2019\", \"price\": \"3.000.000\"}"));
            dateList.add(new JSONObject("{\"day\": \"Kamis\", \"date\": \"02 Mei 2019\", \"price\": \"4.000.000\"}"));
            dateList.add(new JSONObject("{\"day\": \"Jumat\", \"date\": \"03 Mei 2019\", \"price\": \"5.000.000\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_chooseDate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ChangeDateAdapter(dateList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
