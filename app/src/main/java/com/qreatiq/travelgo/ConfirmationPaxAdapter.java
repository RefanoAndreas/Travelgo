package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ConfirmationPaxAdapter extends RecyclerView.Adapter<ConfirmationPaxAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public ConfirmationPaxAdapter(ArrayList<JSONObject> array, Context context){
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
        LinearLayout add_passenger;
        CardView edit;
        TextView name,isiDataPeserta,category;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            add_passenger = (LinearLayout) itemView.findViewById(R.id.add_passenger);
            edit = (CardView) itemView.findViewById(R.id.edit);
            name = (TextView) itemView.findViewById(R.id.name);
            isiDataPeserta = (TextView) itemView.findViewById(R.id.isiDataPeserta);
            category = (TextView) itemView.findViewById(R.id.type);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_confirmation_passenger_item, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject jsonObject = array.get(i);

        try {
            if(!jsonObject.getBoolean("edit")){
                viewHolder.add_passenger.setVisibility(View.VISIBLE);
                viewHolder.edit.setVisibility(View.GONE);
                viewHolder.isiDataPeserta.setText(jsonObject.getString("label"));
            }
            else{
                viewHolder.add_passenger.setVisibility(View.GONE);
                viewHolder.edit.setVisibility(View.VISIBLE);

                String str;
                str=context.getResources().getStringArray(R.array.titleName)[jsonObject.getInt("title")]+" "+jsonObject.getString("name");

                if(jsonObject.getString("for").equals("flight")) {
                    if (!jsonObject.getJSONObject("baggage_return").toString().equals("{}")) {
                        str += "\nBagasi Pergi: " + jsonObject.getJSONObject("baggage_depart").getString("label");
                        str += "\nBagasi Pulang: " + jsonObject.getJSONObject("baggage_return").getString("label");
                    } else {
                        str += "\nBagasi: " + jsonObject.getJSONObject("baggage_depart").getString("label");
                    }
                }

                viewHolder.name.setText(str);
                viewHolder.category.setText(jsonObject.getString("category_label"));
            }
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
