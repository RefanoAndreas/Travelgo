package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourPackagesViewHolder> {

    private ArrayList<TourItem> mTourPackagesList;
    Context context;
    ClickListener clickListener;

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

    public TourAdapter(ArrayList<TourItem> tourPackageslist, Context context){
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
    public void onBindViewHolder(final TourPackagesViewHolder tourPackagesViewHolder, int i) {
        final TourItem currentItem = mTourPackagesList.get(i);

        tourPackagesViewHolder.mTextView1.setText(currentItem.getText1());
        tourPackagesViewHolder.mTextView2.setText(currentItem.getText2());
        tourPackagesViewHolder.mTextView3.setText(currentItem.getText3());
        tourPackagesViewHolder.carouselView.setPageCount(currentItem.getImageCarousel().length);

        tourPackagesViewHolder.carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(currentItem.getImageCarousel()[position]);
            }
        });

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
