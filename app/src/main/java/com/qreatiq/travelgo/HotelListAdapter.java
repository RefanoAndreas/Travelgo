package com.qreatiq.travelgo;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.HotelListHolder> {
    ArrayList<JSONObject> hotelList;
    Context context;
    ClickListener clickListener;

    public HotelListAdapter(ArrayList<JSONObject> hotelList,Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    public class HotelListHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView hotelName, hotelLocation, hotelPrice;
        RatingBar rating;
        public ConstraintLayout layout;

        public HotelListHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.itemRV_hotelPhoto);
            hotelName = itemView.findViewById(R.id.TV_hotelName);
            hotelLocation = itemView.findViewById(R.id.TV_hotelLocation);
            hotelPrice = itemView.findViewById(R.id.TV_hotelPrice);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            rating = (RatingBar) itemView.findViewById(R.id.rating);

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
            hotelListHolder.hotelName.setText(jsonObject.getString("name"));
            hotelListHolder.hotelLocation.setText(jsonObject.getString("location"));

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            hotelListHolder.hotelPrice.setText("Rp. "+formatter.format(jsonObject.getInt("price"))+" "+context.getResources().getString(R.string.schedule_per_night_label));

            hotelListHolder.rating.setRating((float) jsonObject.getDouble("rating"));


            if(jsonObject.has("photo") && !jsonObject.getString("photo").equals("")) {
                Log.d("photo",jsonObject.getString("photo"));
                Picasso.get()
                        .load(jsonObject.getString("photo"))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(hotelListHolder.mImageView);
            }
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
