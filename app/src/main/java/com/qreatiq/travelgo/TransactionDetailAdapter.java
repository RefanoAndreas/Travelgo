package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.TransactionDetailHolder> {

    ArrayList<JSONObject> tripPackList;
    Context context;

    public TransactionDetailAdapter(ArrayList<JSONObject> tripPackList, Context context){
        this.tripPackList = tripPackList;
        this.context = context;
    }

    public class TransactionDetailHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ConstraintLayout layout;
        public TransactionDetailHolder(@NonNull View itemView) {
            super(itemView);
            mRoundedImageView = itemView.findViewById(R.id.itemRV_Home_RV);
            mTextView1 = itemView.findViewById(R.id.itemRV_Home_TV1);
            mTextView2 = itemView.findViewById(R.id.itemRV_Home_TV2);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);

        }
    }

    @NonNull
    @Override
    public TransactionDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_detail_item, viewGroup,false);
        return new TransactionDetailHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailHolder transactionDetailHolder, int i) {
        final JSONObject jsonObject = tripPackList.get(i);

        try {
//            Picasso.get().load(jsonObject.getString("photo")).placeholder(R.mipmap.ic_launcher).into(transactionDetailHolder.mRoundedImageView);

            transactionDetailHolder.mTextView1.setText(jsonObject.getString("trip_name"));

            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(jsonObject.getDouble("trip_price"));
            transactionDetailHolder.mTextView2.setText("Rp. "+formattedNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tripPackList.size();
    }

}
