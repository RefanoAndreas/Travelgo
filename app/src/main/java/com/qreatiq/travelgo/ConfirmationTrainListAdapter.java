package com.qreatiq.travelgo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmationTrainListAdapter extends RecyclerView.Adapter<ConfirmationTrainListAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public ConfirmationTrainListAdapter(ArrayList<JSONObject> array, Context context){
        this.array = array;
        this.context = context;
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,class_name,depart_date_place,depart_station,duration,arrive_date_place,arrive_station;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            class_name = (TextView) itemView.findViewById(R.id.class_name);
            depart_date_place = (TextView) itemView.findViewById(R.id.depart_date_place);
            depart_station = (TextView) itemView.findViewById(R.id.depart_station);
            duration = (TextView) itemView.findViewById(R.id.duration);
            arrive_date_place = (TextView) itemView.findViewById(R.id.arrive_date_place);
            arrive_station = (TextView) itemView.findViewById(R.id.arrive_station);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirmation_train, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject jsonObject = array.get(i);

        try {
            viewHolder.name.setText(jsonObject.getString("name"));
            viewHolder.class_name.setText(jsonObject.getString("class"));
            viewHolder.depart_date_place.setText(jsonObject.getString("depart_date_place"));
            viewHolder.depart_station.setText(jsonObject.getString("depart_airport"));
            viewHolder.duration.setText(jsonObject.getString("duration"));
            viewHolder.arrive_date_place.setText(jsonObject.getString("arrive_date_place"));
            viewHolder.arrive_station.setText(jsonObject.getString("arrive_airport"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

}

