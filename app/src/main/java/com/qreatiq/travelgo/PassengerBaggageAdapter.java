package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PassengerBaggageAdapter extends RecyclerView.Adapter<PassengerBaggageAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public PassengerBaggageAdapter(ArrayList<JSONObject> array, Context context){
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
        TextView type,airlines,baggage;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            airlines = (TextView) itemView.findViewById(R.id.airlines);
            type = (TextView) itemView.findViewById(R.id.type);
            baggage = (TextView) itemView.findViewById(R.id.baggage);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bagasi_penumpang_item, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject jsonObject = array.get(i);

        try {
            viewHolder.airlines.setText(jsonObject.getString("airlines"));

            if(!jsonObject.getBoolean("is_return"))
                viewHolder.type.setText(context.getResources().getString(R.string.bagasi_penumpang_depart_title));
            else
                viewHolder.type.setText(context.getResources().getString(R.string.bagasi_penumpang_return_title));

            int index = 0;
            for(int x=0;x<jsonObject.getJSONArray("baggage").length();x++){
                if(jsonObject.getJSONArray("baggage").getJSONObject(x).getBoolean("checked")) {
                    index = x;
                    break;
                }
            }

            if(jsonObject.getJSONArray("baggage").length() > 0)
                viewHolder.baggage.setText(jsonObject.getJSONArray("baggage").getJSONObject(index).getString("label"));

            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

}
