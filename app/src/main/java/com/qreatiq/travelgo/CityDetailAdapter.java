package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityDetailAdapter extends RecyclerView.Adapter<CityDetailAdapter.CityDetailPackagesViewHolder> {

    ArrayList<JSONObject> CityDetailItem;
    HomeAdapter.ClickListener clickListener;
    Context context;

    public CityDetailAdapter(ArrayList<JSONObject> CityDetailItem, Context context){
        this.CityDetailItem = CityDetailItem;
        this.context = context;
    }

    public class CityDetailPackagesViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public ConstraintLayout layout;

        public CityDetailPackagesViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageView = itemView.findViewById(R.id.itemRV_cityDetail_RIV);
            mTextView1 = itemView.findViewById(R.id.itemRV_cityDetail_TV);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);

        }
    }

    @NonNull
    @Override
    public CityDetailPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_detail_item, viewGroup,false);
        return new CityDetailPackagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CityDetailPackagesViewHolder cityDetailPackagesViewHolder, int i) {
        JSONObject jsonObject = CityDetailItem.get(i);

        try {
            Picasso.get().load(jsonObject.getString("photo")).placeholder(R.mipmap.ic_launcher).into(cityDetailPackagesViewHolder.mRoundedImageView);
            cityDetailPackagesViewHolder.mTextView1.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, 0, 0);
            cityDetailPackagesViewHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, convertpx(16), 0);
            cityDetailPackagesViewHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
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
        return CityDetailItem.size();
    }

}
