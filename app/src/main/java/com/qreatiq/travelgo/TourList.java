package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourList extends BaseActivity {
    RecyclerView mRecyclerView;
    TourListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String url, userID, urlPhoto;
    SharedPreferences user_id;
    ArrayList<JSONObject> tourListPackagesList = new ArrayList<>();
    FloatingActionButton fabAdd;
    TextView no_trip,no_tour;

    RecyclerViewSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        set_toolbar();

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "No data found");

        mRecyclerView = findViewById(R.id.RV_tourListPackages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TourListAdapter(tourListPackagesList, this);
        no_trip = (TextView)findViewById(R.id.no_trip);
        no_tour = (TextView)findViewById(R.id.no_tour);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new TourListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    startActivity(new Intent(TourList.this, TourCreate.class)
                            .putExtra("type", "edit")
                            .putExtra("id", tourListPackagesList.get(position).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this,mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton("",
                        R.drawable.ic_delete_ff4444_24dp,
                        Color.parseColor("#FFFFFFFF"),
                        Color.parseColor("#FF3C30"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                try {
                                    delete(tourListPackagesList.get(pos).getString("id"));
                                    tourListPackagesList.remove(pos);
                                    mAdapter.notifyItemRemoved(pos);
                                    mAdapter.notifyItemRangeRemoved(pos,tourListPackagesList.size());

                                    check_data();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        TourList.this)
                );
            }
        };

        fabAdd = (FloatingActionButton)findViewById(R.id.fabAddTourList);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TourList.this, TourCreate.class)
                            .putExtra("type", "add"));
            }
        });

        skeletonScreen = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_tour_list_item_swipe).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrip();
    }

    private void check_data(){
        if(tourListPackagesList.size() > 0){
            no_trip.setVisibility(View.GONE);
            no_tour.setVisibility(View.GONE);
        }
        else{
            no_trip.setVisibility(View.VISIBLE);
            no_tour.setVisibility(View.GONE);
        }
    }

    private void getTrip(){
        tourListPackagesList.clear();
        url = C_URL+"trip";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(!response.isNull("tour")) {
                        JSONArray jsonArray = response.getJSONArray("trip");

                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = new JSONObject();

                            urlPhoto = C_URL_IMAGES + "trip?image="
                                    + jsonArray.getJSONObject(x).getString("urlPhoto")
                                    + "&mime=" + jsonArray.getJSONObject(x).getString("mimePhoto");
                            jsonObject.put("photo", urlPhoto);
                            jsonObject.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                            jsonObject.put("start_date", jsonArray.getJSONObject(x).getString("start_date_display"));
                            jsonObject.put("end_date", jsonArray.getJSONObject(x).getString("end_date_display"));
                            jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                            jsonObject.put("status", jsonArray.getJSONObject(x).getString("status"));

                            tourListPackagesList.add(jsonObject);
                            if (x != 0)
                                mAdapter.notifyItemInserted(x);
                            else
                                mAdapter.notifyDataSetChanged();
//                        mAdapter.notifyItemRangeChanged(x, tourListPackagesList.size());
                        }

                        check_data();
                    }
                    else{
                        fabAdd.hide();
                        no_tour.setVisibility(View.VISIBLE);
                        no_trip.setVisibility(View.GONE);
                    }
                    skeletonScreen.hide();
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

    private void delete(String trip_id){
        url = C_URL+"trip";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("_method", "delete");
            jsonObject.put("id", trip_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
