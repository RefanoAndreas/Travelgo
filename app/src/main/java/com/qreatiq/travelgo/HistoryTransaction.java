package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryTransaction extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private HistoryTransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> historyList = new ArrayList<>();
    String url, user_ID;
    SharedPreferences userID;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        user_ID = userID.getString("user_id", "No Data Found");

        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        getHistory();

        try {
            historyList.add(new JSONObject("{\"packageName\": \"Bali Trip\", \"location\": \"Bali\", \"price\": \"1.000.000\"}"));
            historyList.add(new JSONObject("{\"packageName\": \"Jakarta Trip\", \"location\": \"Jakarta\", \"price\": \"1.000.000\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_historyTransaction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HistoryTransactionListAdapter(historyList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new HistoryTransactionListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(HistoryTransaction.this, TransactionDetail.class));
            }
        });


    }

    private void getHistory(){
        url = link.C_URL+"getInvoice.php?id="+user_ID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int x=0; x<jsonArray.length(); x++){
                        historyList.add(jsonArray.getJSONObject(x));
                        mAdapter.notifyItemInserted(x);
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
