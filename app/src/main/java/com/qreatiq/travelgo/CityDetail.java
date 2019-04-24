package com.qreatiq.travelgo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CityDetail extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};

    ArrayList<JSONObject> locPhoto = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String location_id, url, urlGetImg;
    RequestQueue requestQueue;
    TextView locationName, expandBtn, description;
    ObjectAnimator animator;
    LinearLayout ratingLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        Intent i = getIntent();
        location_id = i.getStringExtra("idLocation");

        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityDetail.super.onBackPressed();
            }
        });

        ratingLoc = (LinearLayout)findViewById(R.id.rating);
        ratingLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        locationName = (TextView)findViewById(R.id.name);
        description = (TextView) this.findViewById(R.id.cityDesc);
        expandBtn = (TextView) this.findViewById(R.id.btnExpand);


        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandBtn.getText().toString().equals("View More")){
                    expandBtn.setText("View Less");
                    animator = ObjectAnimator.ofInt(
                            description,"maxLines", 500
                    );
                    animator.setDuration(1000);
                    animator.start();
                }
                else{
                    expandBtn.setText("View More");
                    animator = ObjectAnimator.ofInt(
                            description,"maxLines", 4
                    );
                    animator.setDuration(1000);
                    animator.start();
                }

            }
        });

        carouselView = (CarouselView) findViewById(R.id.cityDetail_Carousel);
        carouselView.setImageListener(imageListener);

        detailLocation();
        getLocationPhoto();

//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);


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
            try {
                Picasso.get().load(link.C_URL_IMAGES+"location/"+locPhoto.get(position).getString("urlPhoto")).into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void getLocationPhoto(){
        url = link.C_URL+"getPhotoLocation.php?id="+location_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("photo");
                    for (int x=0; x<jsonArray.length();x++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlPhoto", jsonArray.getJSONObject(x).getString("urlPhoto"));
                        locPhoto.add(jsonObject);
                    }
                    carouselView.setPageCount(locPhoto.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public void detailLocation(){
        url = link.C_URL+"getPlaceDetail.php?id="+location_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")){
                        locationName.setText(response.getJSONObject("data").getString("name"));
                        description.setText(response.getJSONObject("data").getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
