package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FragmentHome extends Fragment {

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String url, urlPhoto;
    RequestQueue requestQueue;
    ArrayList<HomeItem> homeList = new ArrayList<>();
    CardView tourBtn;
    BottomNavContainer parent;

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
                startActivity(new Intent(getActivity(), CityDetail.class).putExtra("idLocation", homeList.get(position).getID()));
            }
        });

        skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_homelist_item).show();

        getLocation();
    }

    private void getLocation(){
        url = link.C_URL+"getLocation.php";
        urlPhoto = link.C_URL_IMAGES+"location/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("location");
                    for(int x=0; x<jsonArray.length(); x++){
                        homeList.add(new HomeItem(urlPhoto+jsonArray.getJSONObject(x).getString("photo"), jsonArray.getJSONObject(x).getString("name")
                                , jsonArray.getJSONObject(x).getString("description"), jsonArray.getJSONObject(x).getString("id")));

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
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
