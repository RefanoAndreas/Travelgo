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

        history();

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
                    Log.d("type", notifList.get(position).toString());

                    if(notifList.get(position).getString("salesType").equals("tour")) {
                        if (notifList.get(position).getString("type").equals("purchasing")) {
                            startActivity(new Intent(D1Notifikasi.this, TransactionDetail.class)
                                            .putExtra("origin", "history")
                                            .putExtra("sales_id", notifList.get(position).getString("id")));
                        } else {
                            startActivity(new Intent(D1Notifikasi.this, DetailTourTransaction.class).putExtra("sales_id", notifList.get(position).getString("id")));
                        }
                    }
                    else{
                        startActivity(new Intent(D1Notifikasi.this, D2NotifikasiDetail.class)
                                .putExtra("info", notifList.get(position).getString("info2"))
                                .putExtra("type", notifList.get(position).getString("type")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void history(){
        if(dataIntent.equals("purchasing"))
            url = link.C_URL+"history-purchasing";
        else if(dataIntent.equals("sales"))
            url = link.C_URL+"history-sales";
        else
            url = link.C_URL+"history";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(dataIntent.equals("purchasing")) {
                        for (int x = 0; x < response.getJSONArray("purchasing").length(); x++) {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonSales = response.getJSONArray("purchasing").getJSONObject(x);

                            jsonObject.put("id", jsonSales.getString("sales_id"));
                            jsonObject.put("date1", jsonSales.getString("date1"));
                            jsonObject.put("type", jsonSales.getString("type"));
                            jsonObject.put("salesType", jsonSales.getString("salesType"));

                            jsonObject.put("info1", jsonSales.getString("info1"));
                            jsonObject.put("info2", jsonSales.getString("info2"));
                            jsonObject.put("info3", jsonSales.getString("info3"));
                            jsonObject.put("info4", jsonSales.getString("info4"));
                            jsonObject.put("status", jsonSales.getString("status"));

                            notifList.add(jsonObject);
                            if (x != 0)
                                mAdapter.notifyItemInserted(x);
                            else
                                mAdapter.notifyDataSetChanged();
                        }
                    }
                    else if(dataIntent.equals("sales")) {
                        for(int x=0;x<response.getJSONArray("sales").length();x++) {
                            JSONObject jsonSales= response.getJSONArray("sales").getJSONObject(x);

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("id", jsonSales.getString("sales_id"));
                            jsonObject.put("date1", jsonSales.getString("date1"));
                            jsonObject.put("type", "tour");
                            jsonObject.put("salesType", jsonSales.getString("salesType"));

                            jsonObject.put("info1", jsonSales.getString("info1"));
                            jsonObject.put("info2", jsonSales.getString("info2"));
                            jsonObject.put("info3", jsonSales.getString("info3"));
                            jsonObject.put("info4", jsonSales.getString("info4"));
                            jsonObject.put("status", "Dipesan");

                            notifList.add(jsonObject);
                            if(x != 0)
                                mAdapter.notifyItemInserted(x);
                            else
                                mAdapter.notifyDataSetChanged();

                        }
                    }
                    else{
                        Log.d("respon", response.toString());

                        for(int x=0;x<response.getJSONArray("history").length();x++) {
                            JSONObject jsonSales= response.getJSONArray("history").getJSONObject(x);

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("id", jsonSales.getString("id"));
                            jsonObject.put("date1", jsonSales.getString("date1"));
                            jsonObject.put("type", jsonSales.getString("type"));
                            jsonObject.put("salesType", jsonSales.getString("salesType"));

                            jsonObject.put("info1", jsonSales.getString("info1"));
                            jsonObject.put("info2", jsonSales.getString("info2"));
                            jsonObject.put("info3", jsonSales.getString("info3"));
                            jsonObject.put("info4", jsonSales.getString("info4"));
                            if(jsonSales.getString("type").equals("sales"))
                                jsonObject.put("status", "Dipesan");
                            else
                                jsonObject.put("status", jsonSales.getString("status"));

                            notifList.add(jsonObject);
                            if(x != 0)
                                mAdapter.notifyItemInserted(x);
                            else
                                mAdapter.notifyDataSetChanged();

                        }
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
