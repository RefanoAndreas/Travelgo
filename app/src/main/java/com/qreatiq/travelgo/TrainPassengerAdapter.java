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

public class TrainPassengerAdapter extends RecyclerView.Adapter<TrainPassengerAdapter.ViewHolder> {
    ArrayList<JSONObject> passengerList;
    Context context;

    public TrainPassengerAdapter(ArrayList<JSONObject> passengerList, Context context){
        this.passengerList = passengerList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, id_no, class_train;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.TV_passanger_name);
            type = itemView.findViewById(R.id.TV_passanger_type);
            class_train = itemView.findViewById(R.id.TV_train_class);
            id_no = itemView.findViewById(R.id.TV_passenger_id_no);

            view = itemView;
        }
    }

    @NonNull
    @Override
    public TrainPassengerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eticket_train_penumpang, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainPassengerAdapter.ViewHolder viewHolder, int i) {
        final JSONObject jsonObject = passengerList.get(i);

        try {
            viewHolder.name.setText(jsonObject.getString("name"));
            viewHolder.class_train.setText(jsonObject.getString("class_train"));
            viewHolder.id_no.setText(jsonObject.getString("no_id"));
            viewHolder.type.setText(jsonObject.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }
}
