package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class D1Notifikasi extends BaseActivity {

    Intent intent;
    String dataIntent;

    private RecyclerView mRecyclerView;
    private NotifikasiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> notifList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d1_notifikasi);

        link.setToolbar(this);

        intent = getIntent();
        dataIntent = intent.getStringExtra("data");

        if(dataIntent.equals("sales")){
            getSupportActionBar().setTitle("History Sales");
        }
        else if(dataIntent.equals("purchasing")){
            getSupportActionBar().setTitle("History Purchasing");
        }

        try {
            if(!dataIntent.equals("sales")) {
                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Surabaya > Jakarta\", " +
                        "\"infoTrip\":\"Lion Air\", " +
                        "\"totalPack\":\"2 Penumpang\", " +
                        "\"routeType\":\"Sekali Jalan\", " +
                        "\"type\":\"flight\", " +
                        "\"status\":\"Berhasil\"}"));

                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Surabaya > Jakarta\", " +
                        "\"infoTrip\":\"Argo Parahyangan\", " +
                        "\"totalPack\":\"1 Penumpang\", " +
                        "\"routeType\":\"Pulang Pergi\", " +
                        "\"type\":\"train\", " +
                        "\"status\":\"Berhasil\"}"));

                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Labuan Bajo\", " +
                        "\"infoTrip\":\"Hotel ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"routeType\":\"1 Malam\", " +
                        "\"type\":\"hotel\", " +
                        "\"status\":\"Berhasil\"}"));

                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Labuan Bajo Trip\", " +
                        "\"infoTrip\":\"Tour ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"routeType\":\"1 Malam\", " +
                        "\"type\":\"tour\", " +
                        "\"status\":\"Berhasil\"}"));
            }
            if(dataIntent.equals("all") || dataIntent.equals("sales")) {
                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Labuan Bajo Trip\", " +
                        "\"infoTrip\":\"Tour ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"routeType\":\"1 Malam\", " +
                        "\"type\":\"tour\", " +
                        "\"status\":\"Dipesan\"}"));
                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"route\":\"Labuan Bajo Trip\", " +
                        "\"infoTrip\":\"Tour ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"routeType\":\"1 Malam\", " +
                        "\"type\":\"tour\", " +
                        "\"status\":\"Dipesan\"}"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_notif);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NotifikasiAdapter(notifList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new NotifikasiAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    if(notifList.get(position).getString("type").equals("tour")) {
                        if (notifList.get(position).getString("status").equals("Berhasil")) {
                            startActivity(new Intent(D1Notifikasi.this, TransactionDetail.class).putExtra("origin", "history"));
                        } else {
                            startActivity(new Intent(D1Notifikasi.this, DetailTourTransaction.class));
                        }
                    }
                    else{
                        startActivity(new Intent(D1Notifikasi.this, D2NotifikasiDetail.class)
                                .putExtra("routeType", notifList.get(position).getString("routeType"))
                                .putExtra("type", notifList.get(position).getString("type")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
