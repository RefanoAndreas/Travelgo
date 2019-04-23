package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class CityDetail extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carouselView = (CarouselView) findViewById(R.id.cityDetail_Carousel);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        ArrayList<CityDetailItem> cityList = new ArrayList<>();

        cityList.add(new CityDetailItem(R.drawable.background2, "Kuta, Bali"));
        cityList.add(new CityDetailItem(R.drawable.background3, "Lombok, NTB"));
        cityList.add(new CityDetailItem(R.drawable.background4, "Komodo, NTT"));
        cityList.add(new CityDetailItem(R.drawable.background5, "Madura, East Java"));
        cityList.add(new CityDetailItem(R.drawable.background6, "Bawean, East java"));

        mRecyclerView = findViewById(R.id.RV_cityDetail);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(mRecyclerView);
        mAdapter = new CityDetailAdapter(cityList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}
