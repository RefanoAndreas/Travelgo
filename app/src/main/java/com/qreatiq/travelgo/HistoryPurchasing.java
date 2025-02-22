package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryPurchasing extends BaseActivity {

    private RecyclerView mRecyclerView;
    private NotifikasiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> historyList = new ArrayList<>();
    String url, user_ID;
    SharedPreferences userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        user_ID = userID.getString("user_id", "No Data Found");

        set_toolbar();

        try {
            historyList.add(new JSONObject("{\"date\":\"Kamis, 2 Mei 2019\", " +
                    "\"route\":\"Surabaya > Jakarta\", " +
                    "\"infoTrip\":\"Lion Air\", " +
                    "\"totalPack\":\"2 Penumpang\", " +
                    "\"routeType\":\"Sekali Jalan\", " +
                    "\"status\":\"Berhasil\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_historyTransaction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NotifikasiAdapter(historyList,this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new NotifikasiAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(HistoryPurchasing.this, TransactionDetail.class).putExtra("origin", "history"));
            }
        });


    }

    private void getHistory(){
        url = C_URL+"getInvoice.php?id="+user_ID;

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
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}
