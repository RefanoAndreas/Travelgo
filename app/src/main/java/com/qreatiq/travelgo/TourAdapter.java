package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourPackagesViewHolder> {

    private ArrayList<JSONObject> mTourPackagesList;
    Context context;
    ClickListener clickListener;
    ViewSkeletonScreen skeleton;

    public class TourPackagesViewHolder extends RecyclerView.ViewHolder{
        public CarouselView carouselView;
        public TextView mTextView1, mTextView2, mTextView3;
        public FrameLayout layout;

        public TourPackagesViewHolder(View itemView) {
            super(itemView);
            carouselView = itemView.findViewById(R.id.itemRV_tour_CV);
            mTextView1 = itemView.findViewById(R.id.itemRV_tour_TV1);
            mTextView2 = itemView.findViewById(R.id.itemRV_tour_TV2);
            mTextView3 = itemView.findViewById(R.id.itemRV_tour_TV3);
            layout = (FrameLayout) itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(TourAdapter.ClickListener clickListner){
        this.clickListener = clickListner;
    }

    public TourAdapter(ArrayList<JSONObject> tourPackageslist, Context context){
        mTourPackagesList = tourPackageslist;
        this.context = context;
    }

    @Override
    public TourPackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_item, viewGroup,false);
        TourPackagesViewHolder tlpvh = new TourPackagesViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(final TourPackagesViewHolder tourPackagesViewHolder, final int i) {
        final JSONObject jsonObject = mTourPackagesList.get(i);

        try {
            tourPackagesViewHolder.mTextView1.setText(jsonObject.getString("trip_name"));

            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(Double.parseDouble(jsonObject.getString("trip_price")));
            tourPackagesViewHolder.mTextView2.setText("Rp. " + formattedNumber);

            tourPackagesViewHolder.mTextView3.setText(jsonObject.getString("trip_description"));

            tourPackagesViewHolder.carouselView.setPageCount(jsonObject.getJSONArray("photo").length());
//            skeleton.hide();
            tourPackagesViewHolder.carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    try {

                        for(int x=0;x<jsonObject.getJSONArray("photo").length();x++) {
                            Picasso.get()
                                    .load(jsonObject.getJSONArray("photo").getJSONObject(position).getString("name"))
//                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

//                                        skeleton.hide();
                                        }

                                        @Override
                                        public void onError(Exception e) {
//                                        skeleton.hide();
                                        }
                                    });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(8), convertpx(8), convertpx(8), 0);
            tourPackagesViewHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(8), convertpx(8), convertpx(8), convertpx(8));
            tourPackagesViewHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(8), convertpx(8), convertpx(8), 0);
            tourPackagesViewHolder.layout.setLayoutParams(params);
        }
    }

    public int convertpx(int dp){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    @Override
    public int getItemCount() {
        return mTourPackagesList.size();
    }
}
