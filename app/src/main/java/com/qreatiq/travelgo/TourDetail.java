package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TourDetail extends AppCompatActivity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};
    String location_id, url;
    RequestQueue requestQueue;
    TextView locationName, locationDesc, total_packages_label, total_price_label, payPackageBtn;

    RecyclerView list;
    ArrayList<JSONObject> array = new ArrayList<>();
    TourDetailAdapter adapter;

    NestedScrollView scroll;
    CoordinatorLayout layout;

    ConstraintLayout layout_pay;

    int total_pack = 0, total_price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        Intent i = getIntent();
        location_id = i.getStringExtra("idLocation");

        requestQueue = Volley.newRequestQueue(this);

        locationName = (TextView)findViewById(R.id.name);
        locationDesc = (TextView)findViewById(R.id.cityDesc);
        layout_pay = (ConstraintLayout) findViewById(R.id.layout_pay);
        scroll = (NestedScrollView) findViewById(R.id.scroll);
        total_packages_label = (TextView) findViewById(R.id.tourDetail_totalPackages);
        total_price_label = (TextView) findViewById(R.id.tourDetail_totalPrice);
        payPackageBtn = (TextView)findViewById(R.id.payPackageBtn);

        payPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TourDetail.this, ConfirmationOrder.class));
            }
        });

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
        lp.bottomMargin = 0;
        scroll.setLayoutParams(lp);

        list = (RecyclerView) findViewById(R.id.tourDetail_RV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);

        try {
            array.add(new JSONObject("{\"name\": \"Bali Tour\",\"price\": 456464}"));
            array.add(new JSONObject("{\"name\": \"Bali Tour\",\"price\": 123}"));
            array.add(new JSONObject("{\"name\": \"Bali Tour\",\"price\": 789798}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new TourDetailAdapter(array);
        list.setAdapter(adapter);

        adapter.setOnChangeQuantityListener(new TourDetailAdapter.ClickListener() {
            @Override
            public void onAddClick(int quantity, int position) {
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

        link.setToolbar(this);

        carouselView = (CarouselView) findViewById(R.id.tourDetail_Carousel);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
//        detailLocation();
//        getLocationPhoto();
    }

    public void set_cart(){
        total_packages_label.setText(String.valueOf(total_pack)+" Package"+(total_pack > 0 ? "s" : ""));
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
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void getLocationPhoto(){
        url = link.C_URL+"getPhotoLocation.php?id="+location_id;

    }

    public void detailLocation(){
        url = link.C_URL+"getPlaceDetail.php?id="+location_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")){
                        locationName.setText(response.getJSONObject("data").getString("name"));
                        locationDesc.setText(response.getJSONObject("data").getString("description"));
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
