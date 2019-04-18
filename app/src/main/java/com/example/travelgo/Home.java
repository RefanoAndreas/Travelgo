package com.example.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ArrayList<HomeItem> homeList = new ArrayList<>();
        homeList.add(new HomeItem(R.drawable.background2, "Kuta, Bali", "Bali is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background3, "Lombok, NTB", "Lombok is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background4, "Komodo, NTT", "Komodo is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background5, "Madura, East Java", "Madura is an Indonesian island known for ite forested volcanis mountains, ..."));
        homeList.add(new HomeItem(R.drawable.background6, "Bawean, East java", "Bawean is an Indonesian island known for ite forested volcanis mountains, ..."));

        mRecyclerView = findViewById(R.id.RV_Home);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        mAdapter = new HomeAdapter(homeList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
