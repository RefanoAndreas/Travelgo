package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    RequestQueue requestQueue;
    ArrayList<JSONObject> tourListPackagesList = new ArrayList<>();
    FloatingActionButton fabAdd;

    RecyclerViewSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        link.setToolbar(this);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "No data found");

        requestQueue = Volley.newRequestQueue(this);



        mRecyclerView = findViewById(R.id.RV_tourListPackages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TourListAdapter(tourListPackagesList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
                                tourListPackagesList.remove(pos);
                                mAdapter.notifyItemRemoved(pos);
                                mAdapter.notifyItemRangeRemoved(pos,tourListPackagesList.size());
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
                startActivity(new Intent(TourList.this, TourCreate.class));
            }
        });

        skeletonScreen = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_tour_list_item_swipe).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrip();
    }

    private void getTrip(){
        tourListPackagesList.clear();
        url = link.C_URL+"trip";

        Log.d("token", userID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("trip");
                    for (int x=0; x<jsonArray.length(); x++){
                        JSONObject jsonObject = new JSONObject();

                        Log.d("responJson", jsonArray.toString());
                        urlPhoto = link.C_URL_IMAGES+"trip?image="
                                +jsonArray.getJSONObject(x).getString("urlPhoto")
                                +"&mime="+jsonArray.getJSONObject(x).getString("mimePhoto");
                        jsonObject.put("photo", urlPhoto);
                        jsonObject.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject.put("start_date", jsonArray.getJSONObject(x).getString("start_date_display"));
                        jsonObject.put("end_date", jsonArray.getJSONObject(x).getString("end_date_display"));

                        tourListPackagesList.add(jsonObject);
                        if(x != 0)
                            mAdapter.notifyItemInserted(x);
                        else
                            mAdapter.notifyDataSetChanged();
//                        mAdapter.notifyItemRangeChanged(x, tourListPackagesList.size());
                    }

                    skeletonScreen.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message="";
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
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
