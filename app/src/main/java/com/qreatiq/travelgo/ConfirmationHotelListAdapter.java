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

public class ConfirmationHotelListAdapter extends RecyclerView.Adapter<ConfirmationHotelListAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;

    public ConfirmationHotelListAdapter(ArrayList<JSONObject> array, Context context){
        this.array = array;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,room_name,check_in,check_out,total_guest;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            room_name = (TextView) itemView.findViewById(R.id.room_name);
            check_in = (TextView) itemView.findViewById(R.id.check_in);
            check_out = (TextView) itemView.findViewById(R.id.check_out);
            total_guest = (TextView) itemView.findViewById(R.id.total_guest);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirmation_hotel, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject jsonObject = array.get(i);

        try {
            viewHolder.name.setText(jsonObject.getString("name"));
            viewHolder.room_name.setText(jsonObject.getString("room_name"));
            viewHolder.check_in.setText(jsonObject.getString("check_in"));
            viewHolder.check_out.setText(jsonObject.getString("check_out"));
            viewHolder.total_guest.setText(jsonObject.getString("total_guest"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

}