package com.qreatiq.travelgo;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.HotelListHolder> {
    ArrayList<JSONObject> hotelList;
    Context context;
    ClickListener clickListener;

    public HotelListAdapter(ArrayList<JSONObject> hotelList) {
        this.hotelList = hotelList;
    }

    public class HotelListHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView hotelName, hotelLocation, hotelReview, hotelPrice;
        public ConstraintLayout layout;

        public HotelListHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.itemRV_hotelPhoto);
            hotelName = itemView.findViewById(R.id.TV_hotelName);
            hotelLocation = itemView.findViewById(R.id.TV_hotelLocation);
            hotelReview = itemView.findViewById(R.id.TV_hotelReview);
            hotelPrice = itemView.findViewById(R.id.TV_hotelPrice);
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
    public HotelListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hotel_list, viewGroup,false);
        return new HotelListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelListHolder hotelListHolder, int i) {
        JSONObject jsonObject = hotelList.get(i);

        try {
            hotelListHolder.hotelName.setText(jsonObject.getString("hotelName"));
            hotelListHolder.hotelLocation.setText(jsonObject.getString("hotelLocation"));
            hotelListHolder.hotelReview.setText(jsonObject.getString("hotelReview"));
            hotelListHolder.hotelPrice.setText("Rp. "+jsonObject.getString("hotePrice")+" per malam");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(HotelListAdapter.ClickListener clickListner){
        this.clickListener= clickListner;
    }


}
