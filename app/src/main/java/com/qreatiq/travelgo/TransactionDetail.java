package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionDetail extends AppCompatActivity {

    TextView isiDataPeserta;
    Intent intent;
    String intentString, url;
    MaterialButton saveButton;
    String trip_id, trip_pack, urlPhoto, trip_loc, trip_date;
    RequestQueue requestQueue;
    TextView TV_status_transaction, TV_buy_date, TV_trip_date, TV_trip_location;
    TextView TV_total_price, TV_tour_phone;
    int price;
    String tourPhone;

    String userID;
    SharedPreferences user_id;

    RecyclerView mRecyclerView;
    TransactionDetailAdapter mAdapter;
    ArrayList<JSONObject> tripPackList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        link.setToolbar(this);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        saveButton = (MaterialButton)findViewById(R.id.submit_transaction);

        TV_status_transaction = (TextView)findViewById(R.id.TV_status_transaction);
        TV_buy_date = (TextView)findViewById(R.id.TV_buy_date);
        TV_trip_date = (TextView)findViewById(R.id.TV_trip_date);
        TV_trip_location = (TextView)findViewById(R.id.TV_trip_location);
        TV_total_price = (TextView)findViewById(R.id.TV_total_price);
        TV_tour_phone = (TextView)findViewById(R.id.TV_tour_phone);

        requestQueue = Volley.newRequestQueue(this);

        isiDataPeserta = (TextView) findViewById(R.id.isiDataPeserta);
        isiDataPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionDetail.this, FlightIsiDataPenumpang.class)
                        .putExtra("packageName", "tour"));
            }
        });

        mRecyclerView = findViewById(R.id.RV_tripPack);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TransactionDetailAdapter(tripPackList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (intentString.equals("history")){
            isiDataPeserta.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
        else if(intentString.equals("pay")){
            trip_pack = intent.getStringExtra("trip_pack");
            trip_loc = intent.getStringExtra("trip_location");
            trip_date = intent.getStringExtra("trip_date");
            price = intent.getIntExtra("total_price", 0);
            tourPhone = intent.getStringExtra("tour_phone");
            detailPayment();
        }


    }

    private void detailPayment(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");

        TV_status_transaction.setText("Pending");
        TV_buy_date.setText(mdformat.format(calendar.getTime()));
        TV_trip_date.setText(trip_date);
        TV_trip_location.setText(trip_loc);

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(Double.parseDouble(String.valueOf(price)));
        TV_total_price.setText("Rp. "+formattedNumber);

        TV_tour_phone.setText(tourPhone);

        try {
            JSONArray jsonArray = new JSONArray(trip_pack);
            Log.d("jsonarray", jsonArray.toString());
            for(int x=0; x<jsonArray.length();x++){
                JSONObject jsonObject1 = new JSONObject();

                jsonObject1.put("photo", jsonArray.getJSONObject(x).getString("photo"));
                jsonObject1.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                jsonObject1.put("trip_price", jsonArray.getJSONObject(x).getString("price"));

                tripPackList.add(jsonObject1);
            }
            Log.d("trippack", tripPackList.toString());
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
