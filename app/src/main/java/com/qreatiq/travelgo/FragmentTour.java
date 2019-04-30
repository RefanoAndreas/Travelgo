package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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

public class FragmentTour extends Fragment {

    private RecyclerView mRecyclerView;
    private TourAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<TourItem> tourList = new ArrayList<>();
    RequestQueue requestQueue;
    String url, userID;
    SharedPreferences user_id;
    String loc_id="";
    EditText search;
    ImageView tourFilterBtn;

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

        BottomNavContainer parent = (BottomNavContainer) getActivity();
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

//        getPackage();

        tourList.add(new TourItem(sampleImages1, "Kuta Bali Tour", "Rp 2.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
        tourList.add(new TourItem(sampleImages2, "Lombok NTB Tour", "Rp 3.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
        tourList.add(new TourItem(sampleImages3, "Komodo NTT Tour", "Rp 4.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
        tourList.add(new TourItem(sampleImages4, "Madura East Java Tour", "Rp 5.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));
        tourList.add(new TourItem(sampleImages5, "Bawean East Java Tour", "Rp 6.500.000", getResources().getString(R.string.cityDetail_Detail), "1"));

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
                startActivity(new Intent(getActivity(), TourDetail.class).putExtra("idLocation", tourList.get(position).getID()));
            }
        });

        tourFilterBtn = (ImageView)view.findViewById(R.id.tour_filter);
        tourFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FilterTour.class));
            }
        });


//        ArrayList<TourListItem> tourListPackagesList = new ArrayList<>();
//        tourListPackagesList.add(new TourListItem(R.drawable.background2, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background3, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background4, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background5, "Rodex Tour", "11/04/2019", "12/04/2019"));
//        tourListPackagesList.add(new TourListItem(R.drawable.background6, "Rodex Tour", "11/04/2019", "12/04/2019"));
//
//        mRecyclerView = view.findViewById(R.id.RV_tourListPackages);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mAdapter = new TourListAdapter(tourListPackagesList);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
    }

    private void getPackage(){
        url = link.C_URL+"getPackage.php?location="+loc_id;
        Log.d("urlPackage",url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("package");
                    for(int x=0; x<jsonArray.length(); x++){
                        tourList.add(new TourItem(sampleImages1,
                                jsonArray.getJSONObject(x).getString("tour"),
                                "Rp 2.500.000",
                                jsonArray.getJSONObject(x).getString("description"),
                                jsonArray.getJSONObject(x).getString("id")));
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

    @Override
    public void onPause() {
        super.onPause();
        loc_id = "";
    }

    public void viewTourFilter(View v){
        startActivity(new Intent(getActivity(), FilterTour.class));
    }
}
