package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomePackagesViewHolder> {

    private ArrayList<HomeItem> mHomeList;
    Context context;
    ClickListener clickListener;

    Display display;

    public class HomePackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ConstraintLayout layout;

        public HomePackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_Home_RV);
            mTextView1 = itemView.findViewById(R.id.itemRV_Home_TV1);
            mTextView2 = itemView.findViewById(R.id.itemRV_Home_TV2);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);

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

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener = clickListner;
    }


    public HomeAdapter(ArrayList<HomeItem> homelist, Context context){
        mHomeList = homelist;
        this.context = context;
    }

    @Override
    public HomePackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homelist_item, viewGroup,false);
        HomePackagesViewHolder homevh = new HomePackagesViewHolder(v);
        return homevh;
    }

    @Override
    public void onBindViewHolder(HomePackagesViewHolder homePackagesViewHolder, int i) {
        HomeItem currentItem = mHomeList.get(i);
//        homePackagesViewHolder.mRoundedImageView.setImageResource(currentItem.getImageResources());
        Picasso.get().load(currentItem.getImageResources()).placeholder(R.mipmap.ic_launcher).into(homePackagesViewHolder.mRoundedImageView);
        homePackagesViewHolder.mTextView1.setText(currentItem.getText1());
        homePackagesViewHolder.mTextView2.setText(currentItem.getText2());

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, convertpx(10), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(0), 0, convertpx(16), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(0), 0, convertpx(10), 0);
            homePackagesViewHolder.layout.setLayoutParams(params);
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
        return mHomeList.size();
    }
}
