package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
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
    SwipeRefreshLayout swipeLayout;
    RecyclerViewSkeletonScreen skeleton;
    TextView no_data;

    int page = 1;
    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            if(isNetworkConnected())
                history();
            else
                mHandler.postDelayed(mHandlerTask, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d1_notifikasi);

        set_toolbar();

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        intent = getIntent();
        dataIntent = intent.getStringExtra("data");
        no_data = (TextView) findViewById(R.id.no_data);

        if(dataIntent.equals("sales")){
            getSupportActionBar().setTitle(getResources().getString(R.string.manifest_history_sales_title));
        }
        else if(dataIntent.equals("purchasing")){
            getSupportActionBar().setTitle(getResources().getString(R.string.manifest_history_purchase_title));
        }

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                notifList.clear();
                mAdapter.notifyDataSetChanged();
                mHandlerTask.run();
            }
        });



        mRecyclerView = findViewById(R.id.RV_notif);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NotifikasiAdapter(notifList,this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new NotifikasiAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
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
                                .putExtra("sales_id", notifList.get(position).getString("id"))
                                .putExtra("salesType", notifList.get(position).getString("salesType")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mAdapter.setOnScrollListener(new NotifikasiAdapter.ScrollListener() {
            @Override
            public void onBottomReached() {
                page++;
                swipeLayout.setRefreshing(true);
                history();
            }
        });
        mHandlerTask.run();
    }

    private void history(){
        if(page == 1)
            skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_notification).show();
        if(dataIntent.equals("purchasing"))
            url = C_URL+"history-purchasing";
        else if(dataIntent.equals("sales"))
            url = C_URL+"history-sales";
        else
            url = C_URL+"history";
        url += "?page="+page;
        Log.d("url",url);
        Log.d("auth",userID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(dataIntent.equals("purchasing")) {
                        if(response.getJSONObject("purchasing").getJSONArray("data").length() > 0) {
                            for (int x = 0; x < response.getJSONObject("purchasing").getJSONArray("data").length(); x++) {
                                JSONObject jsonObject = new JSONObject();
                                JSONObject jsonSales = response.getJSONObject("purchasing").getJSONArray("data").getJSONObject(x);

                                jsonObject.put("id", jsonSales.getString("id"));
                                jsonObject.put("date1", jsonSales.getString("buy_date"));
                                jsonObject.put("type", "purchasing");
                                jsonObject.put("salesType", jsonSales.getString("salesType"));

                                jsonObject.put("info1", jsonSales.getString("info1"));
                                jsonObject.put("info2", jsonSales.getString("info2"));
                                if (jsonSales.getString("salesType").equals("flight") || jsonSales.getString("salesType").equals("train")) {
                                    jsonObject.put("info3", jsonSales.getString("info3") + " " + getResources().getString(R.string.flight_search_passenger_label));
                                    if (jsonSales.getString("info4").equals("Pulang Pergi"))
                                        jsonObject.put("info4", getResources().getString(R.string.roundtrip_label));
                                    else
                                        jsonObject.put("info4", getResources().getString(R.string.confirmation_one_way_label));
                                } else {
                                    jsonObject.put("info3", jsonSales.getString("info3"));
                                    jsonObject.put("info4", jsonSales.getString("info4") + " " + getResources().getString(R.string.hotel_search_night_label));
                                }

                                jsonObject.put("status", jsonSales.getInt("status"));

                                notifList.add(jsonObject);
                                if (x != 0)
                                    mAdapter.notifyItemInserted(x);
                                else
                                    mAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                            no_data.setVisibility(View.VISIBLE);
                    }
                    else if(dataIntent.equals("sales")) {
                        if(response.getJSONObject("sales").getJSONArray("data").length() > 0) {
                            for (int x = 0; x < response.getJSONObject("sales").getJSONArray("data").length(); x++) {
                                JSONObject jsonSales = response.getJSONObject("sales").getJSONArray("data").getJSONObject(x);

                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("id", jsonSales.getString("sales_id"));
                                jsonObject.put("date1", jsonSales.getString("date1"));
                                jsonObject.put("type", "sales");
                                jsonObject.put("salesType", "tour");

                                jsonObject.put("info1", jsonSales.getString("info1"));
                                jsonObject.put("info2", jsonSales.getString("info2"));
                                jsonObject.put("info3", jsonSales.getString("info3"));
                                jsonObject.put("info4", jsonSales.getString("info4"));
                                jsonObject.put("status", 2);

                                notifList.add(jsonObject);
                                if (x != 0)
                                    mAdapter.notifyItemInserted(x);
                                else
                                    mAdapter.notifyDataSetChanged();

                            }
                        }
                        else
                            no_data.setVisibility(View.VISIBLE);
                    }
                    else{
                        if(response.getJSONObject("history").getJSONArray("data").length() > 0) {
                            for (int x = 0; x < response.getJSONObject("history").getJSONArray("data").length(); x++) {
                                JSONObject jsonSales = response.getJSONObject("history").getJSONArray("data").getJSONObject(x);

                                JSONObject jsonObject = new JSONObject();

                                Log.d("json", jsonSales.toString());

                                jsonObject.put("id", jsonSales.getString("id"));
                                jsonObject.put("date1", jsonSales.getString("buy_date"));
                                jsonObject.put("type", jsonSales.getString("type"));
                                jsonObject.put("salesType", jsonSales.getString("salesType"));
//                            jsonObject.put("salesType", "flight");

                                jsonObject.put("info1", jsonSales.getString("info1"));
                                jsonObject.put("info2", jsonSales.getString("info2"));
                                if (jsonSales.getString("salesType").equals("flight") || jsonSales.getString("salesType").equals("train")) {
                                    jsonObject.put("info3", jsonSales.getString("info3") + " " + getResources().getString(R.string.flight_search_passenger_label));
                                    if (jsonSales.getString("info4").equals("Pulang Pergi"))
                                        jsonObject.put("info4", getResources().getString(R.string.roundtrip_label));
                                    else
                                        jsonObject.put("info4", getResources().getString(R.string.confirmation_one_way_label));
                                } else {
                                    jsonObject.put("info3", jsonSales.getString("info3"));
                                    jsonObject.put("info4", jsonSales.getString("info4") + " " + getResources().getString(R.string.hotel_search_night_label));
                                }

                                if(!jsonSales.getString("type").equals("sales"))
                                    jsonObject.put("status", jsonSales.getInt("status"));
                                else
                                    jsonObject.put("status", 2);

                                notifList.add(jsonObject);
                                if (x != 0)
                                    mAdapter.notifyItemInserted(x);
                                else
                                    mAdapter.notifyDataSetChanged();

                            }
                        }
                        else
                            no_data.setVisibility(View.VISIBLE);
                    }
                    swipeLayout.setRefreshing(false);
                    if(page == 1)
                        skeleton.hide();
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

}
