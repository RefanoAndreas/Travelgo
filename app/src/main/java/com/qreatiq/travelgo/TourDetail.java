package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
    String location_id, url, urlPhoto;
    RequestQueue requestQueue;
    TextView locationName, locationDesc, total_packages_label, total_price_label, payPackageBtn;
    TextView TV_trip_date, TV_tour_name;
    String trip_date, trip_location, tour_phone;

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
        TV_tour_name = (TextView)findViewById(R.id.TV_tour_name);
        TV_trip_date = (TextView)findViewById(R.id.TV_trip_date);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) scroll.getLayoutParams();
        lp.bottomMargin = 0;
        scroll.setLayoutParams(lp);

        scroll.getParent().requestChildFocus(scroll, scroll);

        list = (RecyclerView) findViewById(R.id.tourDetail_RV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);

        adapter = new TourDetailAdapter(array);
        list.setAdapter(adapter);

        detailLocation();

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
                Log.d("trippack", array.toString());
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

        link.setToolbar(this);

        carouselView = (CarouselView) findViewById(R.id.tourDetail_Carousel);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

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

    public void detailLocation(){
        url = link.C_URL+"tour/trip/detail?id="+location_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("trip");
                    locationName.setText(jsonObject.getString("name"));
                    TV_tour_name.setText("Managed by "+jsonObject.getString("tour_name"));
                    TV_trip_date.setText(jsonObject.getString("trip_date")+" @ "+jsonObject.getString("location_trip"));
                    locationDesc.setText(jsonObject.getString("description"));

                    tour_phone = jsonObject.getString("tour_phone");

                    trip_date = jsonObject.getString("trip_date");
                    trip_location = jsonObject.getString("location_trip");

                    for (int x=0; x<jsonObject.getJSONArray("trip_pack").length();x++){
                        JSONArray jsonArray = jsonObject.getJSONArray("trip_pack");

                        JSONObject jsonObject1 = new JSONObject();
                        urlPhoto = link.C_URL_IMAGES+"trip-pack?image="
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
