package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
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
        RoundedImageView image;
        TextView name,price;
        LinearLayout layout;

        public TransactionDetailHolder(@NonNull View itemView) {
            super(itemView);

            image = (RoundedImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

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

            Picasso.get()
                    .load(jsonObject.getString("photo"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(transactionDetailHolder.image);

            transactionDetailHolder.name.setText(jsonObject.getString("trip_name"));

            transactionDetailHolder.price.setText("Rp. "+jsonObject.getString("trip_price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tripPackList.size();
    }

}
