package com.qreatiq.travelgo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TourCreatePackageLocationModal extends DialogFragment {

    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayTemp = new ArrayList<JSONObject>();
    RecyclerView list;
    TourCreatePackageLocationAdapter adapter;
    Context context;
    String url;
    TourCreate parent;

    TextInputEditText search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_package_location_modal,container,false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = view.findViewById(R.id.list);
        search = view.findViewById(R.id.search);

        parent = (TourCreate) getActivity();

//        arrayList.add("Bali");
//        arrayList.add("Surabaya");
//        arrayList.add("Malang");
//        arrayList.add("Medan");
//        arrayList.add("Makassar");

        locationList();

        adapter = new TourCreatePackageLocationAdapter(arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        for(int x=0;x<arrayList.size();x++)
            arrayTemp.add(arrayList.get(x));

        adapter.setOnItemClickListner(new TourCreatePackageLocationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {

                try {
                    parent.location_id = arrayList.get(position).getString("id");
                    parent.location.setText(arrayList.get(position).getString("name"));
                    parent.location_layout.setError("");
                    dismiss();
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
                Boolean flag = false;
                arrayList.clear();
                adapter.notifyDataSetChanged();
                for (int x = 0; x < arrayTemp.size(); x++) {
                    arrayList.add(arrayTemp.get(x));
                    adapter.notifyItemInserted(x);
                }
                while (!flag) {
                    flag = true;
                        try {
                            for (int x = 0; x < arrayList.size(); x++) {
                                if (!arrayList.get(x).getString("name").toLowerCase().contains(s.toString().toLowerCase())) {
                                    arrayList.remove(x);
                                    adapter.notifyItemRemoved(x);
                                    adapter.notifyItemRangeChanged(x, arrayList.size());
                                    flag = false;
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void locationList(){
        url = link.C_URL+"location";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("location");
                    for(int x=0; x<jsonArray.length(); x++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", jsonArray.getJSONObject(x).getString("id"));
                        jsonObject.put("name", jsonArray.getJSONObject(x).getString("name"));
                        arrayList.add(jsonObject);
                    }
                    adapter.notifyDataSetChanged();
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", parent.userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        parent.requestQueue.add(jsonObjectRequest);

    }
}
