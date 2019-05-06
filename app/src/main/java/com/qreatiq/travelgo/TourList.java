package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TourList extends BaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String url, userID;
    SharedPreferences user_id;
    RequestQueue requestQueue;
    ArrayList<TourListItem> tourListPackagesList = new ArrayList<>();
    FloatingActionButton fabAdd;

    RecyclerViewSkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        link.setToolbar(this);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("user_id", "No data found");

        requestQueue = Volley.newRequestQueue(this);



        tourListPackagesList.add(new TourListItem(R.drawable.background2,
                "Bali Tour",
                "01-01-2019",
                "01-01-2020"));
        tourListPackagesList.add(new TourListItem(R.drawable.background2,
                "Bali Tour",
                "01-01-2019",
                "01-01-2020"));
        tourListPackagesList.add(new TourListItem(R.drawable.background2,
                "Bali Tour",
                "01-01-2019",
                "01-01-2020"));
        tourListPackagesList.add(new TourListItem(R.drawable.background2,
                "Bali Tour",
                "01-01-2019",
                "01-01-2020"));

        mRecyclerView = findViewById(R.id.RV_tourListPackages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TourListAdapter(tourListPackagesList);



        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this,mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton("",
                        R.drawable.ic_delete_ff4444_24dp,
                        Color.parseColor("#FFFFFF"),
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

//        skeletonScreen = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_tour_list_item_swipe).show();
//        getPackage();
    }

    private void getPackage(){
        url = link.C_URL+"getPackageUser.php?id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("package");
                    for (int x=0; x<jsonArray.length(); x++){
                        tourListPackagesList.add(new TourListItem(R.drawable.background2,
                                jsonArray.getJSONObject(x).getString("packageName"),
                                jsonArray.getJSONObject(x).getString("start_date"),
                                jsonArray.getJSONObject(x).getString("end_date")));
                        mAdapter.notifyItemInserted(x);
                    }
                    skeletonScreen.hide();
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
