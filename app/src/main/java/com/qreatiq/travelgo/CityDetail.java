package com.qreatiq.travelgo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.qreatiq.travelgo.Utils.BaseActivity;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public class CityDetail extends BaseActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};

    ArrayList<JSONObject> locPhoto = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String location_id, url, urlPhoto;
    TextView locationName, expandBtn, description;
    ObjectAnimator animator;
    LinearLayout ratingLoc;
    BottomSheetDialog modal;
    String userID;
    SharedPreferences user_id;
    ArrayList<JSONObject> cityList = new ArrayList<>();
    Button btnFindTour;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        Intent i = getIntent();
        location_id = i.getStringExtra("idLocation");

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

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        btnFindTour = (Button)findViewById(R.id.findTourBtn);
        btnFindTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CityDetail.this, BottomNavContainer.class).putExtra("loc_id", location_id));
            }
        });

        ratingLoc = (LinearLayout)findViewById(R.id.rating);
        ratingLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog=new BottomSheetDialog(CityDetail.this);
                View view = View.inflate(CityDetail.this, R.layout.rating_city_detail_modal_fragment, null);
                bottomSheetDialog.setContentView(view);
                BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();
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
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            okhttp3.Request newRequest = null;
                            newRequest = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Authorization", userID)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            Picasso picasso = new Picasso.Builder(CityDetail.this)
                    .downloader(new OkHttp3Downloader(client))
                    .build();

            try {
                urlPhoto = link.C_URL_IMAGES+"location?image="+locPhoto.get(position).getString("urlPhoto")+"&mime="+locPhoto.get(position).getString("mimePhoto");
                picasso.load(urlPhoto).placeholder(R.mipmap.ic_launcher).into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    public void detailLocation(){
        url = C_URL+"cityDetail?id="+location_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("photo");

                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlPhoto", jsonArray.getJSONObject(x).getString("urlPhoto"));
                        jsonObject.put("mimePhoto", jsonArray.getJSONObject(x).getString("mimePhoto"));
                        locPhoto.add(jsonObject);
                        carouselView.setPageCount(locPhoto.size());
                    }

                    locationName.setText(response.getJSONObject("location").getString("name"));
                    description.setText(response.getJSONObject("location").getString("description"));

                    JSONArray jsonArrayVisitPlace = response.getJSONArray("visit_place");

                    for(int x=0;x<jsonArrayVisitPlace.length();x++){
                        urlPhoto = link.C_URL_IMAGES+"location?image="+jsonArrayVisitPlace.getJSONObject(x)
                                .getString("urlPhoto")+"&mime="+jsonArrayVisitPlace.getJSONObject(x)
                                .getString("mimePhoto");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("photo", urlPhoto);
                        jsonObject.put("name", jsonArrayVisitPlace.getJSONObject(x).getString("name"));

                        jsonObject.put("user", userID);
                        cityList.add(jsonObject);
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CoordinatorLayout layout=(CoordinatorLayout) findViewById(R.id.layout);
                String message="";
                if (error instanceof NetworkError) {
                    message="Network Error";
                }
                else if (error instanceof ServerError) {
                    message="Server Error";
                }
                else if (error instanceof AuthFailureError) {
                    message="Authentication Error";
                }
                else if (error instanceof ParseError) {
                    message="Parse Error";
                }
                else if (error instanceof NoConnectionError) {
                    message="Connection Missing";
                }
                else if (error instanceof TimeoutError) {
                    message="Server Timeout Reached";
                }
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
