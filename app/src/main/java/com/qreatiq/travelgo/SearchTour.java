package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTour extends BaseActivity {

    ImageView btnBack;
    RecyclerView mRecyclerView,search_list;
    SearchTourSpotAdapter mAdapter;
    SearchTourAdapter search_adapter;
    ArrayList<JSONObject> spotList = new ArrayList<JSONObject>(),search_array = new ArrayList<JSONObject>();
    TextView search, no_data;
    LinearLayout headline;

    ChipGroup rekomendasi_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tour);

        search = (TextView) findViewById(R.id.search);
        search_list = (RecyclerView) findViewById(R.id.search_list);
        headline = (LinearLayout) findViewById(R.id.headline);
        rekomendasi_layout = (ChipGroup) findViewById(R.id.rekomendasi_layout);
        no_data = (TextView)findViewById(R.id.no_data);

        mRecyclerView = findViewById(R.id.RV_spotWisata);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(mRecyclerView);
        mAdapter = new SearchTourSpotAdapter(spotList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        search_adapter = new SearchTourAdapter(search_array,this);
        search_list.setLayoutManager(mLayoutManager1);
        search_list.setAdapter(search_adapter);

        btnBack = (ImageView)findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_adapter.setOnItemClickListener(new SearchTourAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    Intent i = new Intent();
                    i.putExtra("location", search_array.get(position).getString("id"));
                    i.putExtra("location_label", search_array.get(position).getString("name"));
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mAdapter.setOnItemClickListener(new SearchTourSpotAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    Intent i = new Intent();
                    i.putExtra("location", spotList.get(position).getString("id"));
                    i.putExtra("location_label", spotList.get(position).getString("name")+", "+spotList.get(position).getString("subtitle"));
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    search_list.setVisibility(View.VISIBLE);
                    headline.setVisibility(View.GONE);
                    get_location(s.toString());
                }
                else{
                    search_list.setVisibility(View.GONE);
                    headline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        get_visit_place();
        get_keyword();
    }

    private void get_location(String search){
        String url = C_URL+"tour/location?search="+search;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    search_array.clear();
                    search_adapter.notifyDataSetChanged();
                    JSONArray jsonArray = response.getJSONArray("location");
                    Log.d("respon", String.valueOf(jsonArray.length()));
                    if(jsonArray.length() > 0) {
                        no_data.setVisibility(View.GONE);
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("name", capitalizeString(jsonArray.getJSONObject(x).getString("name")));
                            jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                            search_array.add(jsonObject);

                            if (search_array.size() == 0)
                                search_adapter.notifyDataSetChanged();
                            else
                                search_adapter.notifyItemInserted(x);
                        }
                    }
                    else{
                        no_data.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void get_visit_place(){
        String url = C_URL+"tour/visit-place";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("place_visit");
                    for(int x=0; x<jsonArray.length(); x++){
                        String urlPhoto = C_URL_IMAGES+"place-visit?image="+jsonArray.getJSONObject(x).getString("urlPhoto")+
                                "&mime="+jsonArray.getJSONObject(x).getString("mimePhoto");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", jsonArray.getJSONObject(x).getString("name"));
                        jsonObject.put("subtitle",jsonArray.getJSONObject(x).getJSONObject("location").getString("name"));
                        jsonObject.put("id",jsonArray.getJSONObject(x).getJSONObject("location").getString("id"));
                        jsonObject.put("photo",urlPhoto);
                        spotList.add(jsonObject);

//                        if(spotList.size() == 0)
//                            mAdapter.notifyDataSetChanged();
//                        else
//                            mAdapter.notifyItemInserted(x);

                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void get_keyword(){
        String url = C_URL+"tour/keyword";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("arr_keyword");
                    for(int x=0; x<jsonArray.length(); x++){
                        final Chip chip = new Chip(SearchTour.this);
                        chip.setLayoutParams(new ChipGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        chip.setText(jsonArray.getJSONObject(x).getString("name"));
                        chip.setChipCornerRadius(pxFromDp(SearchTour.this,10));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                        chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#707070")));
                        chip.setChipStrokeWidth(pxFromDp(SearchTour.this,1));
                        rekomendasi_layout.addView(chip);

                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent();
                                i.putExtra("location", chip.getText());
                                i.putExtra("location_label", chip.getText());
                                setResult(RESULT_OK, i);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
