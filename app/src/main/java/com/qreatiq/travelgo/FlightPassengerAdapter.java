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

public class FlightPassengerAdapter extends RecyclerView.Adapter<FlightPassengerAdapter.ViewHolder> {
    ArrayList<JSONObject> passengerList;
    Context context;

    public FlightPassengerAdapter(ArrayList<JSONObject> passengerList, Context context){
        this.passengerList = passengerList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, facilities;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.TV_passanger_name);
            type = itemView.findViewById(R.id.TV_passanger_type);
            facilities = itemView.findViewById(R.id.TV_facilities);

            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eticket_flight_penumpang, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final JSONObject jsonObject = passengerList.get(i);

        try {
            viewHolder.name.setText(jsonObject.getString("name"));
            viewHolder.facilities.setText(jsonObject.getString("facilities"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }
}
