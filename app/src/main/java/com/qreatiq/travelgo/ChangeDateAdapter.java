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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeDateAdapter extends RecyclerView.Adapter<ChangeDateAdapter.ChangeDateHolder> {
    ArrayList<JSONObject> dateList;
    Context context;

    public ChangeDateAdapter(ArrayList<JSONObject> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
    }

    public class ChangeDateHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ConstraintLayout layout;
        public Button mButton;

        public ChangeDateHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TV_day);
            mTextView2 = itemView.findViewById(R.id.TV_date);
            mTextView3 = itemView.findViewById(R.id.TV_price);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
            mButton = itemView.findViewById(R.id.chooseButton);

        }
    }

    @NonNull
    @Override
    public ChangeDateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.change_date_list, viewGroup,false);
        return new ChangeDateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeDateHolder changeDateHolder, int i) {
        JSONObject jsonObject = dateList.get(i);

        try {
            changeDateHolder.mTextView1.setText(jsonObject.getString("day"));
            changeDateHolder.mTextView2.setText(jsonObject.getString("date"));
            changeDateHolder.mTextView3.setText("Rp. "+jsonObject.getString("price"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(i == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(12), 0, 0, 0);
            changeDateHolder.layout.setLayoutParams(params);
        }
        else if(i == getItemCount()-1){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, convertpx(12), 0);
            changeDateHolder.layout.setLayoutParams(params);
        }
        else{
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(convertpx(2), 0, convertpx(2), 0);
            changeDateHolder.layout.setLayoutParams(params);
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
        return dateList.size();
    }

}
