package com.qreatiq.travelgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
    TextView TV_status_transaction, TV_buy_date, TV_trip_date, TV_trip_location, tax, total;
    TextView TV_total_price, TV_tour_phone;
    double price;
    String tourPhone;

    String userID;
    SharedPreferences user_id;

    RecyclerView mRecyclerView;
    TransactionDetailAdapter mAdapter;
    ArrayList<JSONObject> tripPackList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView mRecyclerViewParticipant;
    ParticipantListAdapter mAdapterParticipant;
    ArrayList<JSONObject> ParticipantList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManagerParticipant;
    String sales_id;

    int DATA_PENUMPANG = 1, selected_arr = -1, ADD_GUEST = 11, EDIT_GUEST = 12,AUTH = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        set_toolbar();

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
        tax = (TextView)findViewById(R.id.tax);
        total = (TextView)findViewById(R.id.total);

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

        if (intentString.equals("history")) {
            mRecyclerViewParticipant.setVisibility(View.VISIBLE);
            isiDataPeserta.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            sales_id = intent.getStringExtra("sales_id");
            detailHistory();
        } else if (intentString.equals("pay")) {
            trip_pack = intent.getStringExtra("trip_pack");
            trip_loc = intent.getStringExtra("trip_location");
            trip_date = intent.getStringExtra("trip_date");
            price = intent.getIntExtra("total_price", 0);
            tourPhone = intent.getStringExtra("tour_phone");
            detailPayment();
        }

        mAdapterParticipant.setOnItemClickListener(new ParticipantListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                if (intentString.equals("pay")) {
                    selected_arr = position;
                    startActivityForResult(new Intent(TransactionDetail.this, DataPenumpang.class)
                                    .putExtra("packageName", "tour")
                                    .putExtra("data", ParticipantList.get(selected_arr).toString()),
                            EDIT_GUEST);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParticipantList.size() == 0){
                    ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                    Snackbar snackbar= Snackbar.make(layout,getResources().getString(R.string.transaction_detail_error_participant_label),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    if(!userID.equals("Data not found"))
                        submit();
                    else
                        startActivityForResult(new Intent(TransactionDetail.this,LogIn.class),AUTH);
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
            else if(requestCode == AUTH){
                userID = user_id.getString("access_token", "Data not found");
                submit();
            }
        }
    }

    private void detailPayment(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");

        TV_status_transaction.setText(getResources().getStringArray(R.array.status_sales)[1]);
        TV_buy_date.setText(mdformat.format(calendar.getTime()));
        TV_trip_date.setText(trip_date);
        TV_trip_location.setText(trip_loc);

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(Double.parseDouble(String.valueOf(price)));
        TV_total_price.setText("Rp. "+formattedNumber);
        tax.setText("Rp. "+formatter.format(Double.parseDouble(String.valueOf(price))*0.1));
        total.setText("Rp. "+formatter.format(Double.parseDouble(String.valueOf(price)) + (Double.parseDouble(String.valueOf(price))*0.1)));

        TV_tour_phone.setText(tourPhone);

        Log.d("trip_pack", trip_pack);

        try {
            JSONArray jsonArray = new JSONArray(trip_pack);
            for(int x=0; x<jsonArray.length();x++){
                JSONObject jsonObject1 = new JSONObject();

                if(!jsonArray.getJSONObject(x).getString("qty").equals("0")) {
                    Log.d("trip_price", jsonArray.getJSONObject(x).getString("price"));
                    jsonObject1.put("photo", jsonArray.getJSONObject(x).getString("photo"));
                    jsonObject1.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                    jsonObject1.put("trip_price", formatter.format(jsonArray.getJSONObject(x).getDouble("price"))+" x "+jsonArray.getJSONObject(x).getString("qty"));

                    tripPackList.add(jsonObject1);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void detailHistory(){

        url = C_URL+"history/detail?id="+sales_id+"&type=tour";

        Log.d("url", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("responJson", response.toString());
                    JSONObject jsonDetail = response.getJSONObject("detail");
                    Log.d("jsonDetail", jsonDetail.toString());

                    TV_status_transaction.setText(getResources().getStringArray(R.array.status_sales)[jsonDetail.getInt("status")]);
                    TV_buy_date.setText(jsonDetail.getString("order_date"));

                    for(int x=0; x<jsonDetail.getJSONArray("participant").length();x++){
                        JSONObject jsonParticipant = jsonDetail.getJSONArray("participant").getJSONObject(x);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("title", jsonParticipant.getInt("title"));
                        jsonObject.put("name", jsonParticipant.getString("name"));

                        ParticipantList.add(jsonObject);

                        if(x == 0)
                            mAdapterParticipant.notifyDataSetChanged();
                        else
                            mAdapterParticipant.notifyItemInserted(x);
                    }

                    JSONObject jsonTripPack = jsonDetail.getJSONArray("detail")
                            .getJSONObject(0)
                            .getJSONObject("trip_pack");

                    int subTotalTour = 0;
                    for(int x=0; x<jsonDetail.getJSONArray("detail").length();x++){

                        JSONObject jsonObjectPack = jsonDetail.getJSONArray("detail").getJSONObject(x);

                        JSONObject jsonObject1 = new JSONObject();
                        NumberFormat formatter = new DecimalFormat("#,###");

                        Log.d("logTrip", jsonObjectPack.toString());

                        urlPhoto = C_URL_IMAGES+"trip-pack?image="
                                +jsonObjectPack.getJSONObject("trip_pack").getString("urlPhoto")
                                +"&mime="+jsonObjectPack.getJSONObject("trip_pack").getString("mimePhoto");

                        jsonObject1.put("photo", urlPhoto);
                        jsonObject1.put("trip_name", jsonObjectPack.getJSONObject("trip_pack").getString("name"));
                        jsonObject1.put("trip_price", formatter.format(jsonObjectPack.getDouble("price"))+" x "+jsonObjectPack.getString("pack_quantity"));
                        Log.d("trip", jsonObject1.toString());

                        subTotalTour += jsonObjectPack.getDouble("price") * jsonObjectPack.getInt("pack_quantity");
//                        Log.d("subTotal", String.valueOf(subTotalTour));

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
                    TV_total_price.setText("Rp. "+formatter.format(subTotalTour));
                    tax.setText("Rp. "+formatter.format(subTotalTour*0.1));
                    total.setText("Rp. "+formattedNumber);

                    TV_tour_phone.setText(jsonTripPack.getJSONObject("trip").getJSONObject("tour").getString("phone"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
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

        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMax(100);
        loading.setTitle(getResources().getString(R.string.confirmation_booking_progress_label));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setCancelable(false);
        loading.setProgress(0);
        loading.show();

        final JSONObject jsonObject = new JSONObject();

        try {
            JSONArray participant = new JSONArray(ParticipantList.toString());
            JSONArray jsonArray = new JSONArray(trip_pack);
            jsonObject.put("participant", participant);
            jsonObject.put("trip_pack", jsonArray);
            jsonObject.put("price", Double.parseDouble(String.valueOf(price)) + (Double.parseDouble(String.valueOf(price))*0.1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("data",jsonObject.toString());
        Log.d("url",url);
        Log.d("auth",userID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                Intent in = new Intent(TransactionDetail.this, Payment.class);
                try {
                    JSONArray jsonArray = new JSONArray(trip_pack);
                    in.putExtra("type","tour");
                    in.putExtra("id",response.getString("id"));
                    in.putExtra("trip_pack",jsonArray.toString());
                    in.putExtra("price", price + (price*0.1));

                    Log.d("price", String.valueOf(price + (price*0.1)));
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
                loading.dismiss();
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
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
