package com.qreatiq.travelgo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class HotelDetail extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5, R.drawable.background6};
    MaterialButton chooseBtn;

    private RecyclerView mRecyclerView;
    private HotelRoomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> hotelRoomList = new ArrayList<>();

    int total_pack = 0, total_price = 0;
    TextView expandBtn, description;
    ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        link.setToolbar(this);

        carouselView = (CarouselView) findViewById(R.id.hotel_Carousel);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        chooseBtn = (MaterialButton)findViewById(R.id.chooseRoomBtn);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelDetail.this, ConfirmationOrder.class).putExtra("origin", "hotel"));
            }
        });

        try {
            hotelRoomList.add(new JSONObject("{\"name\": \"Deluxe Room\",\"price\": 1000000}"));
            hotelRoomList.add(new JSONObject("{\"name\": \"President Suite\",\"price\": 10000000}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.roomList_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HotelRoomAdapter(hotelRoomList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnChangeQuantityListener(new HotelRoomAdapter.ClickListener() {
            @Override
            public void onAddClick(int quantity, int position) {
                total_pack++;
                try {
                    total_price += hotelRoomList.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                set_cart();
            }

            @Override
            public void onRemoveQuantityClick(int quantity, int position) {
                total_pack--;
                try {
                    total_price -= hotelRoomList.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                set_cart();
            }

            @Override
            public void onAddQuantityClick(int quantity, int position) {
                total_pack++;
                try {
                    total_price += hotelRoomList.get(position).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                set_cart();
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

    public void set_cart(){
//        total_packages_label.setText(String.valueOf(total_pack)+" Package"+(total_pack > 0 ? "s" : ""));
//        NumberFormat formatter = new DecimalFormat("#,###");
//        String formattedNumber = formatter.format(total_price);
//        total_price_label.setText("Rp. "+formattedNumber);
    }

}
