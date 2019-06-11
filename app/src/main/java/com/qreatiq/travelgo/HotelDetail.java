package com.qreatiq.travelgo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class HotelDetail extends BaseActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};

    private RecyclerView mRecyclerView;
    private HotelRoomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> hotelRoomList = new ArrayList<>();

    ArrayList<JSONObject> facilities_array = new ArrayList<>();
    HotelDetailFacilitiesAdapter adapter;

    int total_pack = 0, total_price = 0;
    TextView expandBtn, description, name, address, rating_number;
    RatingBar rating;
    ObjectAnimator animator;
    GridView facilities;

    JSONObject hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        set_toolbar();

        carouselView = (CarouselView) findViewById(R.id.hotel_Carousel);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });


        facilities = (GridView) findViewById(R.id.facilities);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        rating = (RatingBar) findViewById(R.id.rating);
        rating_number = (TextView) findViewById(R.id.rating_number);

        try {
            hotel = new JSONObject(getIntent().getStringExtra("hotel_selected"));
            setTitle(hotel.getString("name"));
            name.setText(hotel.getString("name"));
            address.setText(hotel.getString("address"));
            rating.setRating((float) hotel.getDouble("rating"));
            rating_number.setText(String.valueOf(hotel.getDouble("rating")));

            for(int x=0;x<hotel.getJSONArray("rooms").length();x++) {
                JSONObject json = new JSONObject();
                json.put("name",hotel.getJSONArray("rooms").getJSONObject(x).getString("name")+" ("+(hotel.getJSONArray("rooms").getJSONObject(x).getBoolean("breakfast") ? "With Breakfast" : "Room Only")+")");
                json.put("breakfast",hotel.getJSONArray("rooms").getJSONObject(x).getBoolean("breakfast"));
                json.put("price",hotel.getJSONArray("rooms").getJSONObject(x).getInt("price"));
                json.put("id",hotel.getJSONArray("rooms").getJSONObject(x).getString("id"));
                hotelRoomList.add(json);
            }

            for(int x=0;x<hotel.getJSONArray("facilities").length();x++) {
                JSONObject json = new JSONObject();
                json.put("name",hotel.getJSONArray("facilities").getString(x));
                facilities_array.add(json);
            }

            adapter = new HotelDetailFacilitiesAdapter(facilities_array,this);
            facilities.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.roomList_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HotelRoomAdapter(hotelRoomList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new HotelRoomAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(HotelDetail.this, ConfirmationOrder.class)
                        .putExtra("origin", "hotel")
                        .putExtra("city", getIntent().getStringExtra("city"))
                        .putExtra("guest", getIntent().getIntExtra("guest",0))
                        .putExtra("room", getIntent().getIntExtra("room",0))
                        .putExtra("hotel_selected",getIntent().getStringExtra("hotel_selected"))
                        .putExtra("room_selected",hotelRoomList.get(position).toString())
                );
            }
        });

        description = (TextView) this.findViewById(R.id.hotelDesc);
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

    }

}
