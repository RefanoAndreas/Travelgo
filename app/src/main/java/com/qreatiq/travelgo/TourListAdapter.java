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
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.TourListPackagesViewHolder> {

    ArrayList<JSONObject> tripList;
    Context context;

    public TourListAdapter(ArrayList<JSONObject> tripList, Context context){
        this.tripList = tripList;
        this.context = context;
    }

    public class TourListPackagesViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public SwipeLayout swipeLayout;
        ConstraintLayout view_foreground;
        RelativeLayout view_background;

        public TourListPackagesViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_tourListPackages);
            mTextView1 = itemView.findViewById(R.id.itemRV_TV_title_tourListPackages);
            mTextView2 = itemView.findViewById(R.id.itemRV_TV_dateFrom_tourListPackages);
            mTextView3 = itemView.findViewById(R.id.itemRV_TV_dateTo_tourListPackages);

            view_foreground = itemView.findViewById(R.id.view_foreground);
            view_background = itemView.findViewById(R.id.view_background);
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
            Picasso.get().load(jsonObject.getString("photo")).placeholder(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).into(tourListPackagesViewHolder.mRoundedImageView);

            tourListPackagesViewHolder.mTextView1.setText(jsonObject.getString("trip_name"));
            tourListPackagesViewHolder.mTextView2.setText(jsonObject.getString("start_date"));
            tourListPackagesViewHolder.mTextView3.setText(jsonObject.getString("end_date"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

}
