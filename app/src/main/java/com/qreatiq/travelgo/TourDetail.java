package com.qreatiq.travelgo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.qreatiq.travelgo.Utils.BaseActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TourDetail extends BaseActivity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};
    String trip_id, url, urlPhoto;
    RequestQueue requestQueue;
    TextView locationName, locationDesc, total_packages_label, total_price_label, payPackageBtn;
    TextView TV_trip_date, TV_tour_name, no_facilities, expandBtn;
    String trip_date, trip_location, tour_phone, userID;
    SharedPreferences user_id;
    ObjectAnimator animator;

    RecyclerView list;
    ArrayList<JSONObject> array = new ArrayList<>();
    TourDetailAdapter adapter;

    ArrayList<JSONObject> arrayPhoto = new ArrayList<>();

    NestedScrollView scroll;
    CoordinatorLayout layout;

    ConstraintLayout layout_pay;

    int total_pack = 0, total_price = 0, PACKAGE = 10;

    ArrayList<JSONObject> arrayFacilities = new ArrayList<>();
    TourDetailFacilitiesAdapter adapterFacilities;
    GridView gridFacilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        set_toolbar();

        Intent i = getIntent();
        trip_id = i.getStringExtra("trip_id");

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        requestQueue = Volley.newRequestQueue(this);

        expandBtn = (TextView)findViewById(R.id.btnExpand);
        locationName = (TextView)findViewById(R.id.name);
        locationDesc = (TextView)findViewById(R.id.cityDesc);
        layout_pay = (ConstraintLayout) findViewById(R.id.layout_pay);
        scroll = (NestedScrollView) findViewById(R.id.scroll);
        total_packages_label = (TextView) findViewById(R.id.tourDetail_totalPackages);
        total_price_label = (TextView) findViewById(R.id.tourDetail_totalPrice);
        payPackageBtn = (TextView)findViewById(R.id.payPackageBtn);
        TV_tour_name = (TextView)findViewById(R.id.TV_tour_name);
        TV_trip_date = (TextView)findViewById(R.id.TV_trip_date);
        no_facilities = (TextView) findViewById(R.id.no_facilities);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
        lp.bottomMargin = 0;
        scroll.setLayoutParams(lp);

        scroll.getParent().requestChildFocus(scroll, scroll);

        gridFacilities = (GridView) findViewById(R.id.GV_facilities);

        adapterFacilities = new TourDetailFacilitiesAdapter(arrayFacilities,this);
        gridFacilities.setAdapter(adapterFacilities);

        list = (RecyclerView) findViewById(R.id.tourDetail_RV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);

        adapter = new TourDetailAdapter(array);
        list.setAdapter(adapter);

        carouselView = (CarouselView) findViewById(R.id.tourDetail_Carousel);
        carouselView.setImageListener(imageListener);

        detailLocation();

        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandBtn.getText().toString().equals(getResources().getString(R.string.view_more_label))){
                    expandBtn.setText(getResources().getString(R.string.view_less_label));
                    animator = ObjectAnimator.ofInt(
                            locationDesc,"maxLines", 500
                    );
                    animator.setDuration(1000);
                    animator.start();
                }
                else{
                    expandBtn.setText(getResources().getString(R.string.view_more_label));
                    animator = ObjectAnimator.ofInt(
                            locationDesc,"maxLines", 4
                    );
                    animator.setDuration(1000);
                    animator.start();
                }
            }
        });

        adapter.setOnChangeQuantityListener(new TourDetailAdapter.ClickListener() {
            @Override
            public void onAddClick(int quantity, int position) {
                try {
                    array.get(position).put("qty",quantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                total_pack++;
                try {
                    total_price += array.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                layout_pay.setVisibility(View.VISIBLE);

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
                lp.bottomMargin = convertpx(54);
                scroll.setLayoutParams(lp);

                set_cart();
            }

            @Override
            public void onRemoveQuantityClick(int quantity, int position) {
                try {
                    array.get(position).put("qty",quantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                total_pack--;
                try {
                    total_price -= array.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(total_pack == 0) {
                    layout_pay.setVisibility(View.GONE);
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
                    lp.bottomMargin = 0;
                    scroll.setLayoutParams(lp);
                }
                set_cart();
            }

            @Override
            public void onAddQuantityClick(int quantity, int position) {
                try {
                    array.get(position).put("qty",quantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                total_pack++;
                try {
                    total_price += array.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                layout_pay.setVisibility(View.VISIBLE);

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
                lp.bottomMargin = convertpx(54);
                scroll.setLayoutParams(lp);

                set_cart();
            }
        });


        payPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TourDetail.this, TransactionDetail.class)
                        .putExtra("origin", "pay")
                        .putExtra("trip_pack", array.toString())
                        .putExtra("trip_date", trip_date)
                        .putExtra("trip_location", trip_location)
                        .putExtra("total_price", total_price)
                        .putExtra("tour_phone", tour_phone)
                );
            }
        });

    }

    public void set_cart(){
        total_packages_label.setText(String.valueOf(total_pack)+" "+getResources().getString(R.string.package_label)+(total_pack > 0 ? "s" : ""));
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(total_price);
        total_price_label.setText("Rp. "+formattedNumber);
    }

    public int convertpx(int dp){
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
            try {
                Picasso.get()
                        .load(C_URL_IMAGES + "trip?image=" + arrayPhoto.get(position).getString("urlPhoto")
                                +"&mime="+arrayPhoto.get(position).getString("mimePhoto"))
                        .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                        .into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void detailLocation(){
        url = C_URL+"tour/trip/detail?id="+trip_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("trip");
                    locationName.setText(jsonObject.getString("name"));
                    TV_tour_name.setText(getResources().getString(R.string.managed_by_label)+" "+jsonObject.getString("tour_name"));
                    TV_trip_date.setText(jsonObject.getString("trip_date")+" @ "+jsonObject.getString("location_trip"));
                    locationDesc.setText(jsonObject.getString("description"));

                    tour_phone = jsonObject.getString("tour_phone");

                    trip_date = jsonObject.getString("trip_date");
                    trip_location = jsonObject.getString("location_trip");

                    for(int x=0; x<jsonObject.getJSONArray("photo").length();x++){
                        JSONArray jsonArray = jsonObject.getJSONArray("photo");

                        JSONObject jsonObject1 = new JSONObject();

                        jsonObject1.put("urlPhoto", jsonArray.getJSONObject(x).getString("urlPhoto"));
                        jsonObject1.put("mimePhoto", jsonArray.getJSONObject(x).getString("mimePhoto"));

                        arrayPhoto.add(jsonObject1);
                    }
                    carouselView.setPageCount(arrayPhoto.size());

                    for (int x=0; x<jsonObject.getJSONArray("trip_pack").length();x++){
                        JSONArray jsonArray = jsonObject.getJSONArray("trip_pack");

                        JSONObject jsonObject1 = new JSONObject();
                        urlPhoto = C_URL_IMAGES+"trip-pack?image="
                                +jsonArray.getJSONObject(x).getString("urlPhoto")
                                +"&mime="+jsonArray.getJSONObject(x).getString("mimePhoto");

                        jsonObject1.put("id", jsonArray.getJSONObject(x).getString("id"));
                        jsonObject1.put("photo", urlPhoto);
                        jsonObject1.put("name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject1.put("price", jsonArray.getJSONObject(x).getString("price"));
                        jsonObject1.put("qty", 0);
                        array.add(jsonObject1);
                    }
                    adapter.notifyDataSetChanged();

                    if(jsonObject.getJSONArray("facilities").length() > 0) {
                        for (int x = 0; x < jsonObject.getJSONArray("facilities").length(); x++) {
                            JSONObject jsonObject1 = jsonObject.getJSONArray("facilities").getJSONObject(x).getJSONObject("facilities");

                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("name", jsonObject1.getString("name"));

                            arrayFacilities.add(jsonObject2);
                        }
                        adapterFacilities.notifyDataSetChanged();
                    }
                    else{
                        no_facilities.setVisibility(View.VISIBLE);
                        gridFacilities.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CoordinatorLayout layout=(CoordinatorLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}
