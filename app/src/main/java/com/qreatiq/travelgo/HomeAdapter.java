package com.qreatiq.travelgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomePackagesViewHolder> {

    ArrayList<JSONObject> mHomeList;
    ClickListener clickListener;
    Context context;

    public HomeAdapter(ArrayList<JSONObject> mHomeList, Context context){
        this.mHomeList = mHomeList;
        this.context = context;
    }

    public class HomePackagesViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2, rating_number;
        public ConstraintLayout layout;
        RatingBar rating;

        public HomePackagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_Home_RV);
            mTextView1 = itemView.findViewById(R.id.itemRV_Home_TV1);
            mTextView2 = itemView.findViewById(R.id.itemRV_Home_TV2);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            rating_number = (TextView) itemView.findViewById(R.id.rating_number);

            if(getAdapterPosition() == 0){
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(convertpx(16), 0, convertpx(10), 0);
                layout.setLayoutParams(params);
            }
            else if(getAdapterPosition() == getItemCount()-1){
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(convertpx(0), 0, convertpx(16), 0);
                layout.setLayoutParams(params);
            }
            else{
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(convertpx(0), 0, convertpx(10), 0);
                layout.setLayoutParams(params);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public HomePackagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homelist_item, viewGroup,false);
        return new HomePackagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePackagesViewHolder homePackagesViewHolder, int i) {
        final JSONObject jsonObject = mHomeList.get(i);

        try {
            Picasso.get()
                    .load(jsonObject.getString("photo"))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(homePackagesViewHolder.mRoundedImageView);

            homePackagesViewHolder.mTextView1.setText(jsonObject.getString("name"));
            homePackagesViewHolder.mTextView2.setText(jsonObject.getString("description"));

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

            homePackagesViewHolder.rating.setRating((float) jsonObject.getDouble("review"));
            homePackagesViewHolder.rating_number.setText(String.valueOf(jsonObject.getDouble("review")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
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

}
