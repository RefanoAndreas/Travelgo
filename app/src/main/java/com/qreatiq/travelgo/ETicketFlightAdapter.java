package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

public class ETicketFlightAdapter extends RecyclerView.Adapter<ETicketFlightAdapter.ViewHolder> {
    ArrayList<JSONObject> flightList;
    Context context;

    public ETicketFlightAdapter(ArrayList<JSONObject> flightList, Context context){
        this.flightList = flightList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eticket_flight_train, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }
}
