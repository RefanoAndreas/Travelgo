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

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTourSpotAdapter extends RecyclerView.Adapter<SearchTourSpotAdapter.SearchTourSpotHolder> {

    ArrayList<SearchTourSpotList> mSpotList;
    Context context;

    public class SearchTourSpotHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public ConstraintLayout layout;

        public SearchTourSpotHolder(@NonNull View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_cityDetail_RIV);
            mTextView1 = itemView.findViewById(R.id.itemRV_cityDetail_TV);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }
    }

    public SearchTourSpotAdapter(ArrayList<SearchTourSpotList> spotList, Context context){
        mSpotList = spotList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchTourSpotHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_spot_list, viewGroup,false);
        return new SearchTourSpotHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTourSpotHolder searchTourSpotHolder, int i) {
        SearchTourSpotList current = mSpotList.get(i);

        searchTourSpotHolder.mRoundedImageView.setImageResource(current.getImageResources());
        searchTourSpotHolder.mTextView1.setText(current.getText1());

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
