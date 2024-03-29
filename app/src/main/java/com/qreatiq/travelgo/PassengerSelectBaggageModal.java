package com.qreatiq.travelgo;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PassengerSelectBaggageModal extends DialogFragment {

    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayTemp = new ArrayList<JSONObject>();
    RecyclerView list;
    TourCreatePackageLocationAdapter adapter;
    Context context;
    String url;
    DataPenumpang parent;
    int selected;
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
        search.setVisibility(View.GONE);

        parent = (DataPenumpang) getActivity();

        try {
            NumberFormat double_formatter = new DecimalFormat("#,###");
            for(int x=0;x<parent.baggage_array.get(selected).getJSONArray("baggage").length();x++){
                JSONObject data = parent.baggage_array.get(selected).getJSONArray("baggage").getJSONObject(x);
                JSONObject json = new JSONObject();
                json.put("name",data.getString("label")+"\nRp. "+double_formatter.format(data.getDouble("fare")));
                arrayList.add(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                    for(int x=0;x<parent.baggage_array.get(selected).getJSONArray("baggage").length();x++)
                        parent.baggage_array.get(selected).getJSONArray("baggage").getJSONObject(x).put("checked",false);
                    parent.baggage_array.get(selected).getJSONArray("baggage").getJSONObject(position).put("checked",true);
                    parent.baggage_adapter.notifyItemChanged(selected);
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
