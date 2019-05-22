package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFlight extends BaseActivity {

    RecyclerView list,last_search_list,popular_city_list;
    SearchProductAdapter adapter,last_search_adapter,popular_city_adapter;
    ArrayList<JSONObject> array = new ArrayList<JSONObject>(),last_search_array = new ArrayList<JSONObject>(),popular_city_array = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayTemp = new ArrayList<JSONObject>();

    EditText tour_search;
    LinearLayout headline;

    String shared_last_search = "";

    JSONObject data;

    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        in = getIntent();
        tour_search = (EditText) findViewById(R.id.tour_search);
        headline = (LinearLayout) findViewById(R.id.headline);
        list = (RecyclerView) findViewById(R.id.list);
        last_search_list = (RecyclerView) findViewById(R.id.last_search_list);
        popular_city_list = (RecyclerView) findViewById(R.id.popular_city_list);

        try {
            data = new JSONObject(getIntent().getStringExtra("data"));

            JSONArray json = new JSONArray();
            if(in.getStringExtra("type").equals("flight")) {
                json = new JSONArray(base_shared_pref.getString("flight.last_search", "[]"));
                shared_last_search = "flight.last_search";
            }
            else if(in.getStringExtra("type").equals("hotel")) {
                json = new JSONArray(base_shared_pref.getString("hotel.last_search", "[]"));
                shared_last_search = "hotel.last_search";
            }
            else if(in.getStringExtra("type").equals("train")) {
                json = new JSONArray(base_shared_pref.getString("train.last_search", "[]"));
                shared_last_search = "train.last_search";
            }

            for(int x=json.length()-1;x>=0;x--) {
                if(!json.getJSONObject(x).toString().equals(data.toString()))
                    last_search_array.add(json.getJSONObject(x));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        adapter = new SearchProductAdapter(array);
        list.setAdapter(adapter);

        LinearLayoutManager last_search_layout_manager = new LinearLayoutManager(this);
        last_search_list.setLayoutManager(last_search_layout_manager);
        last_search_adapter = new SearchProductAdapter(last_search_array);
        last_search_list.setAdapter(last_search_adapter);

        LinearLayoutManager popular_city_layout_manager = new LinearLayoutManager(this);
        popular_city_list.setLayoutManager(popular_city_layout_manager);
        popular_city_adapter = new SearchProductAdapter(popular_city_array);
        popular_city_list.setAdapter(popular_city_adapter);

        if(in.getStringExtra("type").equals("flight")){
            tour_search.setHint("Cari kota atau bandara");

            get_airport();
        }
        else if(in.getStringExtra("type").equals("hotel")){
            tour_search.setHint("Cari kota");

            get_hotel_city();
        }
        else if(in.getStringExtra("type").equals("train")){
            tour_search.setHint("Cari kota atau stasiun");

            get_station();
        }

        last_search_adapter.setOnItemClickListner(new SearchProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                set_last_search(position,last_search_array);

                Intent in = new Intent();
                in.putExtra("data", last_search_array.get(position).toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });

        popular_city_adapter.setOnItemClickListner(new SearchProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                set_last_search(position,popular_city_array);

                Intent in = new Intent();
                in.putExtra("data", popular_city_array.get(position).toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });

        adapter.setOnItemClickListner(new SearchProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                set_last_search(position,array);

                Intent in = new Intent();
                in.putExtra("data", array.get(position).toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });

        ((ImageView) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tour_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    list.setVisibility(View.VISIBLE);
                    headline.setVisibility(View.GONE);

                    Boolean flag = false;
                    array.clear();
                    for (int x = 0; x < arrayTemp.size(); x++)
                        array.add(arrayTemp.get(x));
                    try {
                        while (!flag) {
                            flag = true;
                            for (int x = 0; x < array.size(); x++) {
                                if (!array.get(x).getString("poi").contains(s.toString().toLowerCase()) &&
                                        !array.get(x).getString("city").contains(s.toString().toLowerCase())) {
                                    array.remove(x);
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    list.setVisibility(View.GONE);
                    headline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void set_last_search(int position,ArrayList<JSONObject> arr){
        ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
        if(!base_shared_pref.getString(shared_last_search,"[]").equals("[]")){
            try {
                JSONArray json = new JSONArray(base_shared_pref.getString(shared_last_search,"[]"));

                int index = -1;
                for(int x=0;x<json.length();x++) {
                    arrayList.add(json.getJSONObject(x));
                    if(json.getJSONObject(x).toString().equals(arr.get(position).toString()))
                        index = x;
                }
//                Log.d("data", String.valueOf(index));
                if (index >= 0)
                    arrayList.remove(index);
                else if(arrayList.size() > 2)
                    arrayList.remove(0);
                arrayList.add(arr.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            arrayList.add(arr.get(position));
        }
        edit_base_shared_pref.putString(shared_last_search,arrayList.toString()).commit();
    }

    private void get_airport(){
        String url = C_URL+"flight/airport";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int x=0; x<jsonArray.length(); x++){
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("city_label", capitalizeString(jsonArray.getJSONObject(x).getString("city")));
                        jsonObject.put("poi_label", capitalizeString(jsonArray.getJSONObject(x).getString("poi")));
                        jsonObject.put("city", jsonArray.getJSONObject(x).getString("city"));
                        jsonObject.put("poi", jsonArray.getJSONObject(x).getString("poi"));
                        jsonObject.put("code", jsonArray.getJSONObject(x).getString("code"));

                        if(!jsonObject.toString().equals(data.toString())) {
                            array.add(jsonObject);
                            popular_city_array.add(jsonObject);

                            if (x == 0) {
                                adapter.notifyDataSetChanged();
                                popular_city_adapter.notifyDataSetChanged();
                            } else {
                                adapter.notifyItemInserted(x);
                                popular_city_adapter.notifyItemInserted(x);
                            }
                        }
                    }

                    for(int x=0;x<array.size();x++)
                        arrayTemp.add(array.get(x));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
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
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void get_station(){
        String url = C_URL+"train/station";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int x=0; x<jsonArray.length(); x++){
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("city_label", capitalizeString(jsonArray.getJSONObject(x).getString("city")));
                        jsonObject.put("poi_label", capitalizeString(jsonArray.getJSONObject(x).getString("poi")));
                        jsonObject.put("city", jsonArray.getJSONObject(x).getString("city"));
                        jsonObject.put("poi", jsonArray.getJSONObject(x).getString("poi"));
                        jsonObject.put("code", jsonArray.getJSONObject(x).getString("code"));

                        if(!jsonObject.toString().equals(data.toString())) {
                            array.add(jsonObject);
                            popular_city_array.add(jsonObject);

                            if (x == 0) {
                                adapter.notifyDataSetChanged();
                                popular_city_adapter.notifyDataSetChanged();
                            } else {
                                adapter.notifyItemInserted(x);
                                popular_city_adapter.notifyItemInserted(x);
                            }
                        }
                    }

                    for(int x=0;x<array.size();x++)
                        arrayTemp.add(array.get(x));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
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
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void get_hotel_city(){
        String url = C_URL+"hotel/city";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int x=0; x<jsonArray.length(); x++){
                        JSONArray city_arr = jsonArray.getJSONObject(x).getJSONArray("city");
                        for(int y=0; y<city_arr.length(); y++) {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("city_label", capitalizeString(city_arr.getJSONObject(y).getString("name")));
                            jsonObject.put("poi_label", capitalizeString(jsonArray.getJSONObject(x).getString("name")));
                            jsonObject.put("city", city_arr.getJSONObject(y).getString("name"));
                            jsonObject.put("poi", jsonArray.getJSONObject(x).getString("name"));

                            if(!jsonObject.toString().equals(data.toString())) {
                                array.add(jsonObject);
                                popular_city_array.add(jsonObject);

                                if (x == 0) {
                                    adapter.notifyDataSetChanged();
                                    popular_city_adapter.notifyDataSetChanged();
                                } else {
                                    adapter.notifyItemInserted(x);
                                    popular_city_adapter.notifyItemInserted(x);
                                }
                            }
                        }
                    }
                    Log.d("data",array.toString());

                    for(int x=0;x<array.size();x++)
                        arrayTemp.add(array.get(x));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
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
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
