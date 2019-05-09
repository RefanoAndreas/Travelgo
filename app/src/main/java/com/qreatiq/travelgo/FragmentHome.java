package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FragmentHome extends Fragment {

    RecyclerView mRecyclerView;
    HomeAdapter mAdapter;
    ArrayList<JSONObject> homeList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManager;

    String url, urlPhoto, userID;
    SharedPreferences user_id;
    RequestQueue requestQueue;
    CardView tourBtn, flightBtn, hotelBtn, trainBtn;
    BottomNavContainer parent;

    FilterTourLocationAdapter adapter;

    RecyclerViewSkeletonScreen skeleton;

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

        requestQueue = Volley.newRequestQueue(getActivity());

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
                    startActivity(new Intent(getActivity(), CityDetail.class).putExtra("idLocation", homeList.get(position).getString("location_id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(getActivity(), CityDetail.class).putExtra("idLocation", homeList.get(position).getID()));location_description
            }
        });

        skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_homelist_item).show();

        getLocation();
    }

    private void getLocation(){
        url = link.C_URL+"home/locationList";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("location");
                    for(int x=0; x<jsonArray.length(); x++){
//                        homeList.add(new JSONObject(urlPhoto+jsonArray.getJSONObject(x).getString("urlPhoto")
//                                +"&mime="+jsonArray.getJSONObject(x).getString("mimePhoto"), jsonArray.getJSONObject(x).getString("name")
//                                , jsonArray.getJSONObject(x).getString("description"), jsonArray.getJSONObject(x).getString("id")));

                        urlPhoto = link.C_URL_IMAGES+"location?image="+jsonArray.getJSONObject(x).getString("urlPhoto")+"&mime="+jsonArray.getJSONObject(x).getString("mimePhoto");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("location_photo", urlPhoto);
                        jsonObject.put("location_name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject.put("location_description", jsonArray.getJSONObject(x).getString("description"));
                        jsonObject.put("location_id", jsonArray.getJSONObject(x).getString("id"));
                        homeList.add(jsonObject);

//                        homeList.add(new JSONObject("{\"locationPhoto\": "+urlPhoto+", " +
//                                "\"location_name\": "+jsonArray.getJSONObject(x).getString("name")+", " +
//                                "\"location_description\": "+jsonArray.getJSONObject(x).getString("description")+", " +
//                                "\"location_description\": "+jsonArray.getJSONObject(x).getString("description")+""}"));

                    }
                    mAdapter.notifyDataSetChanged();
                    skeleton.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                Snackbar snackbar=Snackbar.make(parent.layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
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

        requestQueue.add(jsonObjectRequest);
    }
}
