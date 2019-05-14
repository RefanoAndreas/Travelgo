package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class D1Notifikasi extends BaseActivity {

    Intent intent;
    String dataIntent, url, userID;
    SharedPreferences user_id;
    private RecyclerView mRecyclerView;
    private NotifikasiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> notifList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d1_notifikasi);

        set_toolbar();

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        intent = getIntent();
        dataIntent = intent.getStringExtra("data");

        if(dataIntent.equals("sales")){
            getSupportActionBar().setTitle("History Sales");
        }
        else if(dataIntent.equals("purchasing")){
            getSupportActionBar().setTitle("History Purchasing");
        }

        try {
            if(dataIntent.equals("purchasing")) {
                historyPurchasing();
            }
            if(dataIntent.equals("all") || dataIntent.equals("sales")) {
                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"destination\":\"Labuan Bajo Trip\", " +
                        "\"infoTrip\":\"Tour ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"duration\":\"1 Malam\", " +
                        "\"type\":\"tour\", " +
                        "\"status\":\"Dipesan\"}"));
                notifList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                        "\"destination\":\"Labuan Bajo Trip\", " +
                        "\"infoTrip\":\"Tour ABCDE\", " +
                        "\"totalPack\":\"2 Mei 2019 - 3 Mei 2019\", " +
                        "\"duration\":\"1 Malam\", " +
                        "\"type\":\"tour\", " +
                        "\"status\":\"Dipesan\"}"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_notif);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NotifikasiAdapter(notifList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new NotifikasiAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    if(notifList.get(position).getString("type").equals("tour")) {
                        if (notifList.get(position).getString("status").equals("berhasil")) {
                            startActivity(new Intent(D1Notifikasi.this, TransactionDetail.class).putExtra("origin", "history"));
                        } else {
                            startActivity(new Intent(D1Notifikasi.this, DetailTourTransaction.class).putExtra("inv_id", notifList.get(position).getString("id")));
                        }
                    }
                    else{
                        startActivity(new Intent(D1Notifikasi.this, D2NotifikasiDetail.class)
                                .putExtra("routeType", notifList.get(position).getString("routeType"))
                                .putExtra("type", notifList.get(position).getString("type")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void historyPurchasing(){
        url = link.C_URL+"history-purchasing";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    for(int x=0;x<response.getJSONArray("invoice").length();x++){
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonInvoice= response.getJSONArray("invoice").getJSONObject(x);

                        jsonObject.put("id", jsonInvoice.getString("id"));
                        jsonObject.put("date", jsonInvoice.getString("date"));
                        jsonObject.put("status", jsonInvoice.getString("status"));

                        for(int y=0;y<jsonInvoice.getJSONArray("detail").length();y++){
                            JSONObject jsonSalesTour = jsonInvoice.getJSONArray("detail").getJSONObject(y).getJSONObject("sales_tour");

                            jsonObject.put("type", jsonInvoice.getJSONArray("detail").getJSONObject(y).getString("type"));

                            for(int z=0;z<jsonSalesTour.getJSONArray("detail").length();z++){
                                JSONObject jsonTripPack = jsonSalesTour.getJSONArray("detail").getJSONObject(z).getJSONObject("trip_pack");
                                Log.d("trip", jsonTripPack.getJSONObject("trip").getString("name"));

                                jsonObject.put("destination", jsonTripPack.getJSONObject("trip").getString("name"));
                                jsonObject.put("infoTrip", jsonTripPack.getJSONObject("trip").getJSONObject("tour").getString("name"));
                                jsonObject.put("duration", jsonTripPack.getJSONObject("trip").getString("duration"));
                                jsonObject.put("trip_date", jsonTripPack.getJSONObject("trip").getString("trip_date"));

                            }
                        }
                        notifList.add(jsonObject);
                        if(x != 0)
                            mAdapter.notifyItemInserted(x);
                        else
                            mAdapter.notifyDataSetChanged();
                    }
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
}
