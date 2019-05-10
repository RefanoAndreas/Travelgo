package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentTour extends Fragment {

    private RecyclerView mRecyclerView;
    private TourAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> tourList = new ArrayList<>();
    RequestQueue requestQueue;
    String url, userID;
    SharedPreferences user_id;
    String loc_id="";
    EditText search;
    ImageView tourFilterBtn;

    Intent intent;
    String intentString;

    BottomNavContainer parent;

    int[] sampleImages1 = {R.drawable.background2, R.drawable.background3, R.drawable.background4};
    int[] sampleImages2 = {R.drawable.background3, R.drawable.background4, R.drawable.background5};
    int[] sampleImages3 = {R.drawable.background4, R.drawable.background5, R.drawable.background6};
    int[] sampleImages4 = {R.drawable.background5, R.drawable.background6, R.drawable.background2};
    int[] sampleImages5 = {R.drawable.background6, R.drawable.background2, R.drawable.background3};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tourList.clear();
        return inflater.inflate(R.layout.activity_tour, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (BottomNavContainer) getActivity();
        parent.toolbar.setVisibility(View.GONE);
        user_id = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("user_id", "No data found");

        search = (EditText)view.findViewById(R.id.tour_search);
        search.setKeyListener(null);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchTour.class));
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());

        getTrip();

//        tourList.add(new TourItem(sampleImages1, "Kuta Bali Tour", "Rp 2.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
//        tourList.add(new TourItem(sampleImages2, "Lombok NTB Tour", "Rp 3.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
//        tourList.add(new TourItem(sampleImages3, "Komodo NTT Tour", "Rp 4.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
//        tourList.add(new TourItem(sampleImages4, "Madura East Java Tour", "Rp 5.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
//        tourList.add(new TourItem(sampleImages5, "Bawean East Java Tour", "Rp 6.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));

        mRecyclerView = view.findViewById(R.id.tour_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new TourAdapter(tourList, getActivity());
//        mAdapter = new TourAdapter(tourList, this);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new TourAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    startActivity(new Intent(getActivity(), TourDetail.class).putExtra("idLocation", tourList.get(position).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tourFilterBtn = (ImageView)view.findViewById(R.id.tour_filter);
        tourFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FilterTour.class));
            }
        });

    }

    private void getTrip(){
        url = link.C_URL+"tour/trip?loc_id="+parent.fragmentTour.loc_id;

        Log.d("linkURL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("trip");
                    for(int x=0; x<jsonArray.length(); x++){

                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("photo", jsonArray.getJSONObject(x).getJSONArray("photo"));
                        jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                        jsonObject.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject.put("trip_price", jsonArray.getJSONObject(x).getString("trip_price"));
                        jsonObject.put("trip_description", jsonArray.getJSONObject(x).getString("description"));

                        tourList.add(jsonObject);

//                        tourList.add(new TourItem(sampleImages1,
//                                jsonArray.getJSONObject(x).getString("tour"),
//                                "Rp 2.500.000",
//                                jsonArray.getJSONObject(x).getString("description"),
//                                jsonArray.getJSONObject(x).getString("id")));
                        mAdapter.notifyItemInserted(x);
                    }
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
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        loc_id = "";
    }

    public void viewTourFilter(View v){
        startActivity(new Intent(getActivity(), FilterTour.class));
    }
}
