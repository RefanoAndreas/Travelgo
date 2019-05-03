package com.qreatiq.travelgo;

import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {

    ArrayList<JSONObject> notifList;
    ClickListener clickListener;

    public SearchProductAdapter(ArrayList<JSONObject> notifList){
        this.notifList = notifList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView kota;
        public TextView poi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kota = itemView.findViewById(R.id.kota);
            poi = itemView.findViewById(R.id.POI);

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_flight_item, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        JSONObject jsonObject = notifList.get(i);

        try {
            viewHolder.kota.setText(jsonObject.getString("city_label"));
            viewHolder.poi.setText(jsonObject.getString("poi_label"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

}