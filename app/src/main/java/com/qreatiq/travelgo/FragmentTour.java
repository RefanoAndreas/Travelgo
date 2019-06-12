package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import static android.app.Activity.RESULT_OK;

public class FragmentTour extends Fragment {

    private RecyclerView mRecyclerView;
    private TourAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> tourList = new ArrayList<>();
    JSONObject filter = new JSONObject();
    String url,urlPhoto;
    String loc_id="";
    TextView search, TV_no_result;
    ImageView tourFilterBtn;
    SwipeRefreshLayout swipeLayout;

    int SEARCH_TOUR = 1, FILTER_TOUR = 2;

    Intent intent;
    String intentString;

    BottomNavContainer parent;

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

        search = (TextView)view.findViewById(R.id.tour_search);
        search.setKeyListener(null);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SearchTour.class), SEARCH_TOUR);
            }
        });

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tourList.clear();
                mAdapter.notifyDataSetChanged();
                getTrip();
            }
        });
        TV_no_result = (TextView)view.findViewById(R.id.TV_no_result);

        getTrip();

        mRecyclerView = view.findViewById(R.id.tour_RV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new TourAdapter(tourList, getActivity());


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new TourAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    startActivity(new Intent(getActivity(), TourDetail.class).putExtra("trip_id", tourList.get(position).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tourFilterBtn = (ImageView)view.findViewById(R.id.tour_filter);
        tourFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getActivity(), FilterTour.class)
                        .putExtra("filter",filter.toString()),
                        FILTER_TOUR);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == SEARCH_TOUR){
                search.setText(data.getStringExtra("location_label"));
                loc_id = data.getStringExtra("location");
                getTrip();
            }
            else if(requestCode == FILTER_TOUR){
                try {
                    filter = new JSONObject(data.getStringExtra("filter"));

                    String uri = "@drawable/ic_filter_list_primary_24dp";
                    int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                    tourFilterBtn.setImageDrawable(getResources().getDrawable(imageResource));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getTrip();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void getTrip(){
        url = parent.C_URL+"tour/trip?loc_id="+loc_id;

        if(!filter.toString().equals("{}")){
            try {
                url += "&start_date="+filter.getString("start_date");
                url += "&end_date="+filter.getString("end_date");
                url += "&max_price="+filter.getLong("max_price");
                url += "&min_price="+filter.getLong("min_price");
                url += "&location="+filter.getString("location");
                if(filter.getInt("time_range") >= 0)
                    url += "&duration="+filter.getString("time_range");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("url",url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("trip");
                    if(jsonArray.length()>0) {
                        TV_no_result.setVisibility(View.GONE);
                        swipeLayout.setVisibility(View.VISIBLE);
                        tourList.clear();
                        mAdapter.notifyDataSetChanged();
                        for (int x = 0; x < jsonArray.length(); x++) {

                            JSONObject jsonObject = new JSONObject();

                            ArrayList<JSONObject> photo = new ArrayList<JSONObject>();
                            for (int y = 0; y < jsonArray.getJSONObject(x).getJSONArray("photo").length(); y++) {
                                urlPhoto = parent.C_URL_IMAGES + "trip?image=" + jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(y).getString("urlPhoto")
                                        + "&mime=" + jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(y).getString("mimePhoto");
                                ;
                                photo.add(new JSONObject("{\"name\":\"" + urlPhoto + "\"}"));
                            }

                            jsonObject.put("photo", new JSONArray(photo.toString()));
                            jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                            jsonObject.put("trip_name", jsonArray.getJSONObject(x).getString("name"));
                            jsonObject.put("trip_price", jsonArray.getJSONObject(x).getString("trip_price"));
                            jsonObject.put("trip_description", jsonArray.getJSONObject(x).getString("description"));

                            tourList.add(jsonObject);

                            mAdapter.notifyItemInserted(x);
                        }
                        swipeLayout.setRefreshing(false);
                    }
                    else{
                        swipeLayout.setVisibility(View.GONE);
                        TV_no_result.setVisibility(View.VISIBLE);
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

        parent.requestQueue.add(jsonObjectRequest);
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
