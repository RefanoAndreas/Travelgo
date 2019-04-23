package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CityDetailAdapter extends RecyclerView.Adapter<CityDetailAdapter.CityDetailPackagesViewHolder> {

    private ArrayList<CityDetailItem> mCityDetailPackagesList;
    Context context;

    public static class CityDetailPackagesViewHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public ConstraintLayout layout;

        public CityDetailPackagesViewHolder(View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_cityDetail_RIV);
            mTextView1 = itemView.findViewById(R.id.itemRV_cityDetail_TV);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }
    }

    public CityDetailAdapter(ArrayList<CityDetailItem> cityDetailPackageslist, Context context){
        mCityDetailPackagesList = cityDetailPackageslist;
        this.context = context;
    }

    @Override
    public CityDetailPackagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_detail_item, viewGroup,false);
        CityDetailPackagesViewHolder tlpvh = new CityDetailPackagesViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(CityDetailPackagesViewHolder cityDetailPackagesViewHolder, int i) {
        CityDetailItem currentItem = mCityDetailPackagesList.get(i);

        cityDetailPackagesViewHolder.mRoundedImageView.setImageResource(currentItem.getImageResources());
        cityDetailPackagesViewHolder.mTextView1.setText(currentItem.getText1());

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, 0, 0);
            cityDetailPackagesViewHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, convertpx(16), 0);
            cityDetailPackagesViewHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    convertpx(328),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, 0, 0);
            cityDetailPackagesViewHolder.layout.setLayoutParams(params);
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
        return mCityDetailPackagesList.size();
    }
}
