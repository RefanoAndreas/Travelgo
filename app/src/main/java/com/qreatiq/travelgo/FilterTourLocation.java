package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
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
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterTourLocation extends BaseActivity {

    GridView gridViewLocation;
    ArrayList<JSONObject> arrayLocation = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayData = new ArrayList<JSONObject>();
    FilterTourLocationAdapter adapter;
    JSONArray data_from_intent = new JSONArray();

    EditText tour_search;
    TextView reset;

    MaterialButton submit;
    boolean from_system = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour_location);

        set_toolbar();

        gridViewLocation = (GridView) findViewById(R.id.GV_selectLocation);
        tour_search = (EditText) findViewById(R.id.tour_search);
        submit = (MaterialButton) findViewById(R.id.submit);
        reset = (TextView) findViewById(R.id.reset);

        get_visit_place("");

        if(!getIntent().getStringExtra("location").equals("[]")) {
            try {
                data_from_intent = new JSONArray(getIntent().getStringExtra("location"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int x=0;x<data_from_intent.length();x++) {
            try {
                arrayData.add(data_from_intent.getJSONObject(x));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new FilterTourLocationAdapter(arrayLocation,this);
        gridViewLocation.setAdapter(adapter);

        adapter.setOnItemClickListener(new FilterTourLocationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, boolean isChecked) {
                try {
                    if(position <= arrayLocation.size()-1) {
                        if (arrayLocation.get(position).getBoolean("from_system")) {
                            arrayLocation.get(position).put("from_system", false);
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        JSONObject json = new JSONObject();
                        json.put("id", arrayLocation.get(position).getString("id"));
                        json.put("label", arrayLocation.get(position).getString("label"));

                        if (!isChecked) {
                            for (int x = 0; x < arrayData.size(); x++) {
                                if (json.toString().equals(arrayData.get(x)))
                                    arrayData.remove(x);
                            }
                        } else
                            arrayData.add(json);
                        arrayLocation.get(position).put("from_system", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tour_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                get_visit_place(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("data",arrayData.toString());
                Intent i = new Intent();
                i.putExtra("location", arrayData.toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for(int x=0;x<arrayLocation.size();x++) {
                        arrayLocation.get(x).put("checked", false);
                        arrayLocation.get(x).put("from_system", true);
                    }
                    adapter.notifyDataSetChanged();
                    arrayData.clear();

                    tour_search.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void get_visit_place(String search){
        String url = C_URL+"tour/visit-place?search="+search;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                arrayLocation.clear();
                adapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = response.getJSONArray("place_visit");
                            for(int x=0; x<jsonArray.length(); x++){
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("label", capitalizeString(jsonArray.getJSONObject(x).getString("name")));
                                jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                                jsonObject.put("checked", false);
                                jsonObject.put("from_system", false);

                                for(int y=0;y<arrayData.size();y++) {
                                    if (arrayData.get(y).getString("id").equals(jsonArray.getJSONObject(x).getString("id"))) {
                                        jsonObject.put("checked", true);
                                        jsonObject.put("from_system", true);
                                        break;
                                    }
                                }


                                arrayLocation.add(jsonObject);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },100);
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
