package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ETicketAdapter extends RecyclerView.Adapter<ETicketAdapter.ETicketHolder> {
    ArrayList<JSONObject> eTicketList;
    Context context;
    ClickListener clickListener;

    public ETicketAdapter(ArrayList<JSONObject> eTicketList, Context context){
        this.eTicketList = eTicketList;
        this.context = context;
    }

    public class ETicketHolder extends RecyclerView.ViewHolder {
        TextView TV_origin;
        public ConstraintLayout layout;
        public ETicketHolder(@NonNull View itemView) {
            super(itemView);
            TV_origin = itemView.findViewById(R.id.TV_origin);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);

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
    public ETicketHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eticket_item_list, viewGroup,false);
        return new ETicketHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ETicketHolder eTicketHolder, int i) {
        final JSONObject jsonObject = eTicketList.get(i);

        try {
            eTicketHolder.TV_origin.setText(jsonObject.getString("info_eticket"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eTicketList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }
}
