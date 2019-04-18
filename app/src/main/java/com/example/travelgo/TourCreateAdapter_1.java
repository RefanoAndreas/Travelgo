package com.example.travelgo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TourCreateAdapter_1 extends RecyclerView.Adapter<TourCreateAdapter_1.TourCreatePackagesViewHolder_1> {

    private ArrayList<TourCreateItem_1> mTourCreatePackagesList_1;

    public static class TourCreatePackagesViewHolder_1 extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public ImageView mImageView;
        public TextView mTextView1;

        public TourCreatePackagesViewHolder_1(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_upload_tourCreatePackages_listPackages);
            mImageView = itemView.findViewById(R.id.ic_uploadPhoto);
            mTextView1 = itemView.findViewById(R.id.text_uploadPhoto);
        }
    }

    public TourCreateAdapter_1(ArrayList<TourCreateItem_1> tourCreatePackagesItem_1){
        mTourCreatePackagesList_1 = tourCreatePackagesItem_1;
    }

    @Override
    public TourCreateAdapter_1.TourCreatePackagesViewHolder_1 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_create_uploadphoto_item, viewGroup,false);
        TourCreateAdapter_1.TourCreatePackagesViewHolder_1 tlpvh = new TourCreatePackagesViewHolder_1(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(TourCreatePackagesViewHolder_1 tourCreatePackagesViewHolder_1, int i) {
        TourCreateItem_1 currentItem = mTourCreatePackagesList_1.get(i);

        tourCreatePackagesViewHolder_1.mRoundedImageView.setImageResource(currentItem.getImageResources1());
        tourCreatePackagesViewHolder_1.mImageView.setImageResource(currentItem.getImageResources2());
        tourCreatePackagesViewHolder_1.mTextView1.setText(currentItem.getText1());
    }

    @Override
    public int getItemCount() {
        return mTourCreatePackagesList_1.size();
    }
}
