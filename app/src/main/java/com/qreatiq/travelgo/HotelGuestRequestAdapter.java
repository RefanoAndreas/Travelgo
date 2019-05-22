package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelGuestRequestAdapter extends RecyclerView.Adapter<HotelGuestRequestAdapter.ViewHolder> {
    ArrayList<JSONObject> requestList;
    Context context;

    public HotelGuestRequestAdapter(ArrayList<JSONObject> requestList, Context context){
        this.requestList = requestList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_request;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TV_request = itemView.findViewById(R.id.TV_request);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eticket_hotel_req, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final JSONObject jsonObject = requestList.get(i);

        try {
            viewHolder.TV_request.setText(jsonObject.getString("request"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
