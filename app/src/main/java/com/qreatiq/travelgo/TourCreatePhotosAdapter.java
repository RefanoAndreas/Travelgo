package com.qreatiq.travelgo;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreatePhotosAdapter extends RecyclerView.Adapter<TourCreatePhotosAdapter.TourCreatePackagesViewHolder_1> {

    private ArrayList<JSONObject> mTourCreatePackagesList_1;
    ClickListener clickListener;

    public class TourCreatePackagesViewHolder_1 extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public ImageView mImageView;
        public TextView mTextView1;
        LinearLayout content_layout;
        CardView layout;
        View view;

        public TourCreatePackagesViewHolder_1(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_RIV_upload_tourCreatePackages_listPackages);
            mImageView = itemView.findViewById(R.id.ic_uploadPhoto);
            mTextView1 = itemView.findViewById(R.id.text_uploadPhoto);
            content_layout = itemView.findViewById(R.id.content_layout);
            layout = itemView.findViewById(R.id.layout);
            view = itemView;
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public TourCreatePhotosAdapter(ArrayList<JSONObject> tourCreatePackagesItem_1){
        mTourCreatePackagesList_1 = tourCreatePackagesItem_1;
    }

    @Override
    public TourCreatePackagesViewHolder_1 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_create_uploadphoto_item, viewGroup,false);
        TourCreatePackagesViewHolder_1 tlpvh = new TourCreatePackagesViewHolder_1(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(TourCreatePackagesViewHolder_1 tourCreatePackagesViewHolder_1, final int i) {
        JSONObject currentItem = mTourCreatePackagesList_1.get(i);

        tourCreatePackagesViewHolder_1.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(i);
            }
        });

        try {

            if (currentItem.getString("status").equals("add")) {
                if (i == 0)
                    tourCreatePackagesViewHolder_1.mRoundedImageView.setImageResource(currentItem.getInt("background"));
                else
                    tourCreatePackagesViewHolder_1.mRoundedImageView.setImageBitmap((Bitmap) currentItem.get("background"));
            } else {
                Picasso.get()
                        .load(currentItem.getString("background"))
                        .placeholder(R.mipmap.ic_launcher)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(tourCreatePackagesViewHolder_1.mRoundedImageView);
            }

            if(!currentItem.getBoolean("is_button_upload")) {
                tourCreatePackagesViewHolder_1.content_layout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mTourCreatePackagesList_1.size();
    }
}
