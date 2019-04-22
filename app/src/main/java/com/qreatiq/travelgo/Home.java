package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String url;
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestQueue = Volley.newRequestQueue(this);

        getLocation();

    }

    private void getLocation(){

        url = link.C_URL+"getLocation.php";

        ArrayList<HomeItem> homeList = new ArrayList<>();

        Log.d("urlLink", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("responJSONObject", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);


//        homeList.add(new HomeItem(R.drawable.background2, "Kuta, Bali", "Bali is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background3, "Lombok, NTB", "Lombok is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background4, "Komodo, NTT", "Komodo is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background5, "Madura, East Java", "Madura is an Indonesian island known for ite forested volcanis mountains, ..."));
//        homeList.add(new HomeItem(R.drawable.background6, "Bawean, East java", "Bawean is an Indonesian island known for ite forested volcanis mountains, ..."));
//
//        mRecyclerView = findViewById(R.id.RV_Home);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
//        mAdapter = new HomeAdapter(homeList, this);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

    }
}
