package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTourAdapter extends RecyclerView.Adapter<SearchTourAdapter.ViewHolder> {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        this.clickListener= clickListener;
    }

    public SearchTourAdapter(ArrayList<JSONObject> array, Context context){
        this.array = array;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kota,poi;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kota = (TextView) itemView.findViewById(R.id.kota);
            poi = (TextView) itemView.findViewById(R.id.POI);
            poi.setVisibility(View.GONE);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_flight_item, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final JSONObject json = array.get(i);

        try {
            viewHolder.kota.setText(json.getString("name"));
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
