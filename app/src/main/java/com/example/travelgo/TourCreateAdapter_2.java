package com.example.travelgo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TourCreateAdapter_2 extends RecyclerView.Adapter<TourCreateAdapter_2.TourCreatePackagesViewHolder> {

    private ArrayList<TourCreateItem_2> mTourCreatePackagesList_2;

    public static class TourCreatePackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public TourCreatePackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_tourCreatePackages_listPackages);
            mTextView1 = itemView.findViewById(R.id.itemRV_TV_title_tourCreatePackages_listPackages);
            mTextView2 = itemView.findViewById(R.id.itemRV_TV_dateFrom_tourCreatePackages_listPackages);
            mTextView3 = itemView.findViewById(R.id.itemRV_TV_dateTo_tourCreatePackages_listPackages);
        }
    }

    public TourCreateAdapter_2(ArrayList<TourCreateItem_2> tourCreatePackagesItem_2){
        mTourCreatePackagesList_2 = tourCreatePackagesItem_2;
    }

    @Override
    public TourCreatePackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_create_listpackages_item, viewGroup,false);
        TourCreatePackagesViewHolder tlpvh = new TourCreatePackagesViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(TourCreatePackagesViewHolder tourCreatePackagesViewHolder, int i) {
        TourCreateItem_2 currentItem = mTourCreatePackagesList_2.get(i);

        tourCreatePackagesViewHolder.mRoundedImageView.setImageResource(currentItem.getImageResources());
        tourCreatePackagesViewHolder.mTextView1.setText(currentItem.getText1());
        tourCreatePackagesViewHolder.mTextView2.setText(currentItem.getText2());
        tourCreatePackagesViewHolder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mTourCreatePackagesList_2.size();
    }
}