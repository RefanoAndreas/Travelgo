package com.qreatiq.travelgo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {
    ArrayList<JSONObject> ticketList;
    Context context;
    ClickListener clickListener;

    public TicketAdapter(ArrayList<JSONObject> ticketList, Context context) {
        this.ticketList = ticketList;
        this.context = context;
    }

    public class TicketHolder extends RecyclerView.ViewHolder {
        public TextView airlinesName, departureTime, duration, arrivalTime, departureAirport, totalTransit, arrivalAirport, ticketPrice;
        public ConstraintLayout layout;
        public Button mButton;

        public TicketHolder(@NonNull View itemView) {
            super(itemView);
            airlinesName = itemView.findViewById(R.id.TV_airlines_name);
            departureTime = itemView.findViewById(R.id.TV_departTime);
            duration = itemView.findViewById(R.id.TV_duration);
            arrivalTime = itemView.findViewById(R.id.TV_arriveTime);
            departureAirport = itemView.findViewById(R.id.TV_departAirport);
            totalTransit = itemView.findViewById(R.id.TV_totalTransit);
            arrivalAirport = itemView.findViewById(R.id.TV_arriveAirport);
            ticketPrice = itemView.findViewById(R.id.TV_price);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            mButton = itemView.findViewById(R.id.chooseButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flight_search_jadwal_item, viewGroup,false);
        return new TicketHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder ticketHolder, int i) {
        JSONObject jsonObject = ticketList.get(i);

        try {
            if(jsonObject.has("airlines"))
                ticketHolder.airlinesName.setText(jsonObject.getString("airlines"));
            else
                ticketHolder.airlinesName.setText(jsonObject.getString("name")+"\n("+jsonObject.getString("class")+" "+jsonObject.getString("sub-class")+")");

            ticketHolder.departureTime.setText(jsonObject.getString("departTime"));
            ticketHolder.duration.setText(jsonObject.getString("duration"));
            ticketHolder.arrivalTime.setText(jsonObject.getString("arrivalTime"));
            if(jsonObject.has("departAirport"))
                ticketHolder.departureAirport.setText(jsonObject.getString("departAirport"));
            else if(jsonObject.has("departStation"))
                ticketHolder.departureAirport.setText(jsonObject.getString("departStation"));
            if(jsonObject.has("totalTransit"))
                ticketHolder.totalTransit.setText(jsonObject.getString("totalTransit"));
            else
                ticketHolder.totalTransit.setVisibility(View.GONE);
            if(jsonObject.has("arrivalAirport"))
                ticketHolder.arrivalAirport.setText(jsonObject.getString("arrivalAirport"));
            else if(jsonObject.has("arrivalStation"))
                ticketHolder.arrivalAirport.setText(jsonObject.getString("arrivalStation"));

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedNumber = formatter.format(Double.parseDouble(jsonObject.getString("price").replace(".", "")));

            ticketHolder.ticketPrice.setText("Rp. "+formattedNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }


}
