package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryPurchasing extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NotifikasiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> historyList = new ArrayList<>();
    String url, user_ID;
    SharedPreferences userID;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_purchasing);

        link.setToolbar(this);

        try {
            historyList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                    "\"route\":\"Surabaya > Jakarta\", " +
                    "\"infoTrip\":\"Lion Air\", " +
                    "\"totalPack\":\"2 Penumpang\", " +
                    "\"routeType\":\"Sekali Jalan\", " +
                    "\"status\":\"Berhasil\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_historyTransaction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NotifikasiAdapter(historyList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new NotifikasiAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(HistoryPurchasing.this, TransactionDetail.class));
            }
        });

    }
}
