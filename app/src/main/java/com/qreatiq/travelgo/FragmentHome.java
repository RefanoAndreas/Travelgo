package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentHome extends Fragment {

    RecyclerView mRecyclerView;
    HomeAdapter mAdapter;
    ArrayList<JSONObject> homeList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManager;

    String url, urlPhoto, userID;
    SharedPreferences user_id;
    CardView tourBtn, flightBtn, hotelBtn, trainBtn;
    BottomNavContainer parent;
    SwipeRefreshLayout swipe;

    FilterTourLocationAdapter adapter;

    RecyclerViewSkeletonScreen skeleton;
    boolean is_loaded = false;

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            if(parent.isNetworkConnected())
                getLocation();
            else
                mHandler.postDelayed(mHandlerTask, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (BottomNavContainer) getActivity();
        parent.toolbar.setVisibility(View.GONE);
        parent.selectedFragment = parent.fragmentHome;
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(Color.BLUE, Color.RED);

        tourBtn = (CardView)view.findViewById(R.id.tourBtn);
        tourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.bottomNav.setSelectedItemId(R.id.nav_tour);
            }
        });

        flightBtn = (CardView)view.findViewById(R.id.flightBtn);
        flightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FlightSearch.class));
            }
        });

        hotelBtn = (CardView)view.findViewById(R.id.hotelBtn);
        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HotelSearch.class));
            }
        });

        trainBtn = (CardView)view.findViewById(R.id.trainBtn);
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TrainSearch.class));
            }
        });

        user_id = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        mRecyclerView = view.findViewById(R.id.RV_Home);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        mAdapter = new HomeAdapter(homeList, getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new HomeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    startActivity(new Intent(getActivity(), CityDetail.class).putExtra("idLocation", homeList.get(position).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandlerTask.run();
            }
        });

        if(parent.home_list.toString().equals("[]"))
            mHandlerTask.run();
        else{
            try {
                homeList.clear();
                JSONArray jsonArray = parent.home_list;
                for(int x=0; x<jsonArray.length(); x++)
                    homeList.add(jsonArray.getJSONObject(x));
                mAdapter.notifyDataSetChanged();
                skeleton.hide();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getLocation(){
        skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_homelist_item).show();
        url = parent.C_URL+"home";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    homeList.clear();
                    JSONArray jsonArray = response.getJSONArray("location");
                    for(int x=0; x<jsonArray.length(); x++){
                        urlPhoto = parent.C_URL_IMAGES+"location?image="+jsonArray.getJSONObject(x).getString("urlPhoto")+
                                "&mime="+jsonArray.getJSONObject(x).getString("mimePhoto");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("photo", urlPhoto);
                        jsonObject.put("name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject.put("description", jsonArray.getJSONObject(x).getString("description"));
                        jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                        jsonObject.put("review", jsonArray.getJSONObject(x).getDouble("overall_review"));

                        jsonObject.put("user", userID);
                        homeList.add(jsonObject);
                    }
                    mAdapter.notifyDataSetChanged();
                    skeleton.hide();

                    parent.home_list = new JSONArray(homeList.toString());
                    is_loaded = true;
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parent.error_exception(error,parent.layout);
                skeleton.hide();
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

        parent.requestQueue.add(jsonObjectRequest);
    }
}
