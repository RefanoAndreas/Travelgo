package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreate extends AppCompatActivity {

    private RecyclerView mRecyclerView_1, mRecyclerView_2;
    private RecyclerView.Adapter mAdapter_1;
    private RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;

    ArrayList<JSONObject> tour_pack_array = new ArrayList<JSONObject>();
    TourCreateAdapter_2 mAdapter_2;

    ArrayList<String> array = new ArrayList<String>();
    TourCreateFacilitiesAdapter adapter;
    GridView grid;

    int CREATE_TOUR_PACKAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grid = (GridView) findViewById(R.id.grid);

        ArrayList<TourCreateItem_1> tourCreatePackagesList_1 = new ArrayList<>();
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.upload_photo, R.drawable.ic_add_a_photo_888888_24dp, "Upload Photo"));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background2, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background3, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background4, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background5, 0,null));
        tourCreatePackagesList_1.add(new TourCreateItem_1(R.drawable.background6, 0,null));

        mRecyclerView_1 = findViewById(R.id.RV_tourCreatePackages_1);
        mRecyclerView_1.setHasFixedSize(true);
        mLayoutManager_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter_1 = new TourCreateAdapter_1(tourCreatePackagesList_1);

        mRecyclerView_1.setLayoutManager(mLayoutManager_1);
        mRecyclerView_1.setAdapter(mAdapter_1);


        try {
            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background2+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background3+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background4+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background5+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView_2 = findViewById(R.id.RV_tourCreatePackages_2);
        mRecyclerView_2.setHasFixedSize(true);
        mLayoutManager_2 = new LinearLayoutManager(this);
        mAdapter_2 = new TourCreateAdapter_2(tour_pack_array);

        mRecyclerView_2.setLayoutManager(mLayoutManager_2);
        mRecyclerView_2.setAdapter(mAdapter_2);

        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");

        adapter = new TourCreateFacilitiesAdapter(array,this);
        grid.setAdapter(adapter);
    }

    public void createTourPack(View v){
        startActivityForResult(new Intent(this,TourCreatePackage.class),CREATE_TOUR_PACKAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CREATE_TOUR_PACKAGE){
                try {
                    JSONObject data_from_facilities = new JSONObject(data.getStringExtra("data"));

                    JSONObject json = new JSONObject();
                    json.put("image",R.drawable.background2);
                    json.put("name",data_from_facilities.getString("name"));
                    json.put("price",data_from_facilities.getString("price"));
                    json.put("start_date","11/04/2019");
                    json.put("end_date","12/04/2019");

                    tour_pack_array.add(json);
                    mAdapter_2.notifyItemInserted(tour_pack_array.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
