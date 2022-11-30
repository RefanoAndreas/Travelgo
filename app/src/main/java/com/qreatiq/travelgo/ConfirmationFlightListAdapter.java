package com.qreatiq.travelgo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ConfirmationFlightListAdapter extends RecyclerView.Adapter<ConfirmationFlightListAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public ConfirmationFlightListAdapter(ArrayList<JSONObject> array, Context context){
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
        TextView airlines,depart_date_place,depart_airport,duration,arrive_date_place,arrive_airport,transit,title;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            airlines = (TextView) itemView.findViewById(R.id.airlines);
            depart_date_place = (TextView) itemView.findViewById(R.id.depart_date_place);
            depart_airport = (TextView) itemView.findViewById(R.id.depart_airport);
            duration = (TextView) itemView.findViewById(R.id.duration);
            arrive_date_place = (TextView) itemView.findViewById(R.id.arrive_date_place);
            arrive_airport = (TextView) itemView.findViewById(R.id.arrive_airport);
            transit = (TextView) itemView.findViewById(R.id.transit);
            title = (TextView) itemView.findViewById(R.id.title);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirmation_flight, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject jsonObject = array.get(i);

        try {
            viewHolder.airlines.setText(jsonObject.getString("airlines"));
            viewHolder.depart_date_place.setText(jsonObject.getString("depart_date_place"));
            viewHolder.depart_airport.setText(jsonObject.getString("depart_airport"));
            viewHolder.duration.setText(jsonObject.getString("duration"));
            viewHolder.arrive_date_place.setText(jsonObject.getString("arrive_date_place"));
            viewHolder.arrive_airport.setText(jsonObject.getString("arrive_airport"));

            if(jsonObject.has("transit")){
                viewHolder.transit.setText(jsonObject.getString("transit"));
                viewHolder.transit.setVisibility(View.VISIBLE);
            }

            if(jsonObject.has("title")){
                viewHolder.title.setText(jsonObject.getString("title"));
                viewHolder.title.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

}
