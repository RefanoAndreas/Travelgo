package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {
    ArrayList<JSONObject> ticketList;
    Context context;

    public TicketAdapter(ArrayList<JSONObject> ticketList) {
        this.ticketList = ticketList;
    }

    public class TicketHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ConstraintLayout layout;
        public Button mButton;

        public TicketHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TV_day);
            mTextView2 = itemView.findViewById(R.id.TV_date);
            mTextView3 = itemView.findViewById(R.id.TV_price);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            mButton = itemView.findViewById(R.id.chooseButton);
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
            ticketHolder.mTextView1.setText(jsonObject.getString("day"));
            ticketHolder.mTextView2.setText(jsonObject.getString("date"));
            ticketHolder.mTextView3.setText("Rp. "+jsonObject.getString("price"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }


}
