package com.qreatiq.travelgo;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTourSpotAdapter extends RecyclerView.Adapter<SearchTourSpotAdapter.ViewHolder> {

    ArrayList<JSONObject> mSpotList;
    Context context;
    ClickListener clickListener;

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        this.clickListener= clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1,subtitle;
        public ConstraintLayout layout;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_cityDetail_RIV);
            mTextView1 = itemView.findViewById(R.id.itemRV_cityDetail_TV);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            view = itemView;
        }
    }

    public SearchTourSpotAdapter(ArrayList<JSONObject> spotList, Context context){
        mSpotList = spotList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_spot_list, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder searchTourSpotHolder, final int i) {
        JSONObject current = mSpotList.get(i);

        try {
            Picasso.get()
                    .load(current.getString("photo"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(searchTourSpotHolder.mRoundedImageView);
            searchTourSpotHolder.mTextView1.setText(current.getString("name"));
            searchTourSpotHolder.subtitle.setText(current.getString("subtitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        searchTourSpotHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(i);
            }
        });

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, 0, 0);
            searchTourSpotHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, convertpx(16), 0);
            searchTourSpotHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(16), 0, 0, 0);
            searchTourSpotHolder.layout.setLayoutParams(params);
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
        return mSpotList.size();
    }

}
