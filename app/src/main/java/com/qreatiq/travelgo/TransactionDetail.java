package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TransactionDetail extends BaseActivity {

    TextView isiDataPeserta;
    Intent intent;
    String intentString, url;
    MaterialButton saveButton;
    String trip_id, trip_pack, urlPhoto, trip_loc, trip_date;
    RequestQueue requestQueue;
    TextView TV_status_transaction, TV_buy_date, TV_trip_date, TV_trip_location;
    TextView TV_total_price, TV_tour_phone;
    double price;
    String tourPhone;

    String userID, selectedPack;
    SharedPreferences user_id, selected_package;

    RecyclerView mRecyclerView;
    TransactionDetailAdapter mAdapter;
    ArrayList<JSONObject> tripPackList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView mRecyclerViewParticipant;
    ParticipantListAdapter mAdapterParticipant;
    ArrayList<JSONObject> ParticipantList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManagerParticipant;
    String sales_id;

    int DATA_PENUMPANG = 1, selected_arr = -1, ADD_GUEST = 11, EDIT_GUEST = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        set_toolbar();

        selected_package = getSharedPreferences("selected_pack", Context.MODE_PRIVATE);
        selectedPack = selected_package.getString("selected_pack", "Data not found");

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
                startActivityForResult(new Intent(TransactionDetail.this, DataPenumpang.class)
                        .putExtra("packageName", "tour"), ADD_GUEST);
            }
        });

        mRecyclerView = findViewById(R.id.RV_tripPack);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setAutoMeasureEnabled(true);
        mAdapter = new TransactionDetailAdapter(tripPackList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerViewParticipant = findViewById(R.id.RV_participant);
        mRecyclerViewParticipant.setHasFixedSize(true);
        mLayoutManagerParticipant = new LinearLayoutManager(this);
        mAdapterParticipant = new ParticipantListAdapter(ParticipantList, this);

        mRecyclerViewParticipant.setLayoutManager(mLayoutManagerParticipant);
        mRecyclerViewParticipant.setAdapter(mAdapterParticipant);

//        mAdapterParticipant.setOnItemClickListener(new ParticipantListAdapter.ClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                selected_arr = position;
//                startActivityForResult(new Intent(TransactionDetail.this, DataPenumpang.class)
//                                .putExtra("packageName", "tour")
//                                .putExtra("data", ParticipantList.get(selected_arr).toString()),
//                        ADD_OR_EDIT_GUEST);
//            }
//        });

        if(selectedPack.equals("Data not found")) {
            if (intentString.equals("history")) {
                isiDataPeserta.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                sales_id = intent.getStringExtra("sales_id");
                mRecyclerViewParticipant.setVisibility(View.VISIBLE);
                detailHistory();
            } else if (intentString.equals("pay")) {
                trip_pack = intent.getStringExtra("trip_pack");
                trip_loc = intent.getStringExtra("trip_location");
                trip_date = intent.getStringExtra("trip_date");
                price = intent.getIntExtra("total_price", 0);
                tourPhone = intent.getStringExtra("tour_phone");
                detailPayment();
            }
        }
        else{
            try {
                JSONObject jsonObject = new JSONObject(selectedPack);
                trip_pack = jsonObject.getString("trip_pack");
                trip_loc = jsonObject.getString("trip_location");
                trip_date = jsonObject.getString("trip_date");
                price = jsonObject.getDouble("total_price");
                tourPhone = jsonObject.getString("tour_phone");
                detailPayment();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapterParticipant.setOnItemClickListener(new ParticipantListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                selected_arr = position;
                startActivityForResult(new Intent(TransactionDetail.this, DataPenumpang.class)
                        .putExtra("packageName", "tour")
                        .putExtra("data",ParticipantList.get(selected_arr).toString()),
                        EDIT_GUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParticipantList.size() == 0){
                    ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                    Snackbar snackbar=Snackbar.make(layout,"Data Peserta kosong",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    submit();
                    startActivity(new Intent(TransactionDetail.this, Payment.class));
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_GUEST || requestCode == EDIT_GUEST) {
                mRecyclerViewParticipant.setVisibility(View.VISIBLE);

                JSONObject data_penumpang = null;
                try {
                    data_penumpang = new JSONObject(data.getStringExtra("data"));
                    data_penumpang.put("edit", true);
                    if(requestCode == EDIT_GUEST)
                        ParticipantList.set(selected_arr,data_penumpang);
                    else if(requestCode == ADD_GUEST)
                        ParticipantList.add(data_penumpang);

                    mAdapterParticipant.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
            for(int x=0; x<jsonArray.length();x++){
                JSONObject jsonObject1 = new JSONObject();

                if(!jsonArray.getJSONObject(x).getString("qty").equals("0")) {
                    jsonObject1.put("photo", jsonArray.getJSONObject(x).getString("photo"));
                    jsonObject1.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                    jsonObject1.put("trip_price", jsonArray.getJSONObject(x).getString("price"));

                    tripPackList.add(jsonObject1);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor1 = getSharedPreferences("selected_pack", Context.MODE_PRIVATE).edit();
        editor1.clear().commit();
        editor1.apply();
    }

    private void detailHistory(){

        url = C_URL+"history/detail?id="+sales_id+"&type=tour";

        Log.d("id", sales_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonDetail = response.getJSONObject("detail");

                    TV_status_transaction.setText(jsonDetail.getString("status"));
                    TV_buy_date.setText(jsonDetail.getString("order_date"));

                    for(int x=0; x<jsonDetail.getJSONArray("participant").length();x++){
                        JSONObject jsonParticipant = jsonDetail.getJSONArray("participant").getJSONObject(x);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("title", jsonParticipant.getString("title"));
                        jsonObject.put("name", jsonParticipant.getString("name"));

                        ParticipantList.add(jsonObject);

                        if(x == 0)
                            mAdapterParticipant.notifyItemInserted(ParticipantList.size());
                        else
                            mAdapterParticipant.notifyDataSetChanged();
                    }

                    JSONObject jsonTripPack = jsonDetail.getJSONArray("detail")
                            .getJSONObject(0)
                            .getJSONObject("trip_pack");

                    for(int x=0; x<jsonDetail.getJSONArray("detail").length();x++){

                        JSONObject jsonObjectPack = jsonDetail.getJSONArray("detail").getJSONObject(x);

                        JSONObject jsonObject1 = new JSONObject();

//                        jsonObject1.put("photo", jsonObjectPack.getString("photo"));
                        jsonObject1.put("trip_name", jsonObjectPack.getJSONObject("trip_pack").getString("name"));
                        jsonObject1.put("trip_price", jsonObjectPack.getJSONObject("trip_pack").getString("price"));
                        Log.d("trip", jsonObject1.toString());

                        tripPackList.add(jsonObject1);

                        if (x == 0)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemInserted(x);

//                        if(x > 0)
//                            mAdapter.notifyItemInserted(tripPackList.size());
//                        else
//                            mAdapter.notifyDataSetChanged();
                    }

                    TV_trip_date.setText(jsonTripPack.getJSONObject("trip").getString("trip_date"));
                    TV_trip_location.setText(jsonTripPack.getJSONObject("trip").getJSONObject("city").getString("name"));

                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(response.getDouble("price"));
                    TV_total_price.setText("Rp. "+formattedNumber);

                    TV_tour_phone.setText(jsonTripPack.getJSONObject("trip").getJSONObject("tour").getString("phone"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void submit(){
        url = C_URL+"sales/tour";

        JSONObject jsonObject = new JSONObject();

        try {
            JSONArray participant = new JSONArray(ParticipantList.toString());
            JSONArray jsonArray = new JSONArray(trip_pack);
            jsonObject.put("participant", participant);
            jsonObject.put("trip_pack", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent in = new Intent(TransactionDetail.this, Payment.class);
                try {
                    JSONArray jsonArray = new JSONArray(trip_pack);
                    in.putExtra("id",response.getString("id"));
                    in.putExtra("trip_pack",jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
//                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);


    }
}
