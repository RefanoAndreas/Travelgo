package com.qreatiq.travelgo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreateTourPackageAdapter extends RecyclerView.Adapter<TourCreateTourPackageAdapter.TourCreatePackagesViewHolder> {

    private ArrayList<JSONObject> mTourCreatePackagesList_2;
    ClickListener clickListener;

    public static class TourCreatePackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        ImageView trash;
        View v;

        public TourCreatePackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_tourCreatePackages_listPackages);
            mTextView1 = itemView.findViewById(R.id.itemRV_TV_title_tourCreatePackages_listPackages);
            mTextView2 = itemView.findViewById(R.id.itemRV_TV_dateFrom_tourCreatePackages_listPackages);
            mTextView3 = itemView.findViewById(R.id.itemRV_TV_dateTo_tourCreatePackages_listPackages);
            trash = itemView.findViewById(R.id.trash);
            v = itemView;
        }
    }

    public TourCreateTourPackageAdapter(ArrayList<JSONObject> tourCreatePackagesItem_2){
        mTourCreatePackagesList_2 = tourCreatePackagesItem_2;
    }

    @Override
    public TourCreatePackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_create_listpackages_item, viewGroup,false);
        TourCreatePackagesViewHolder tlpvh = new TourCreatePackagesViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(TourCreatePackagesViewHolder tourCreatePackagesViewHolder, final int i) {
        JSONObject currentItem = mTourCreatePackagesList_2.get(i);

        try {
            if (!currentItem.getBoolean("is_link_image"))
                tourCreatePackagesViewHolder.mRoundedImageView.setImageBitmap((Bitmap) currentItem.get("image"));
            else{
                Picasso.get()
                        .load(currentItem.getString("image"))
                        .placeholder(R.mipmap.ic_launcher)
                        .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                        .into(tourCreatePackagesViewHolder.mRoundedImageView);
            }

            tourCreatePackagesViewHolder.mTextView1.setText(currentItem.getString("name"));
            tourCreatePackagesViewHolder.mTextView2.setText(currentItem.getString("start_date"));
            tourCreatePackagesViewHolder.mTextView3.setText(currentItem.getString("start_date"));

            tourCreatePackagesViewHolder.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onTrashClick(i);
                }
            });

            tourCreatePackagesViewHolder.v.setOnClickListener(new View.OnClickListener() {
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
        return mTourCreatePackagesList_2.size();
    }

    public interface ClickListener{
        void onTrashClick(int position);
        void onItemClick(int position);
    }

    public void setOnClickListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }
}
