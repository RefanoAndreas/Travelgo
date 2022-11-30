package com.qreatiq.travelgo;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.IOException;
import java.util.ArrayList;

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.TourListPackagesViewHolder> {

    ArrayList<JSONObject> tripList;
    Context context;
    ClickListener clickListener;

    public TourListAdapter(ArrayList<JSONObject> tripList, Context context){
        this.tripList = tripList;
        this.context = context;
    }

    public class TourListPackagesViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public SwipeLayout swipeLayout;
        ConstraintLayout view_foreground;
        RelativeLayout view_background;

        public TourListPackagesViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_tourListPackages);
            mTextView1 = itemView.findViewById(R.id.itemRV_TV_title_tourListPackages);
            mTextView2 = itemView.findViewById(R.id.itemRV_TV_dateFrom_tourListPackages);
            mTextView3 = itemView.findViewById(R.id.itemRV_TV_dateTo_tourListPackages);
            mTextView4 = itemView.findViewById(R.id.itemRV_TV_status);

            view_foreground = itemView.findViewById(R.id.view_foreground);
            view_background = itemView.findViewById(R.id.view_background);

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
    public TourListAdapter.TourListPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tourlist_itemswipe, viewGroup,false);
        return new TourListPackagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TourListAdapter.TourListPackagesViewHolder tourListPackagesViewHolder, int i) {
        final JSONObject jsonObject = tripList.get(i);


        try {
            Picasso.get().load(jsonObject.getString("photo"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(tourListPackagesViewHolder.mRoundedImageView);

            tourListPackagesViewHolder.mTextView1.setText(jsonObject.getString("trip_name"));
            tourListPackagesViewHolder.mTextView2.setText(jsonObject.getString("start_date"));
            tourListPackagesViewHolder.mTextView3.setText(jsonObject.getString("end_date"));
            tourListPackagesViewHolder.mTextView4.setText(jsonObject.getString("status"));

            if(jsonObject.getString("status").equals("Approve")){
                tourListPackagesViewHolder.mTextView4.setTextColor(Color.parseColor("#0DD32C"));
            }
            else if(jsonObject.getString("status").equals("Decline")){
                tourListPackagesViewHolder.mTextView4.setTextColor(Color.parseColor("#FF0000"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

}
