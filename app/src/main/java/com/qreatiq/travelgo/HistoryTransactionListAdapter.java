package com.qreatiq.travelgo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryTransactionListAdapter extends RecyclerView.Adapter<HistoryTransactionListAdapter.HistoryTransactionHolder> {
    ArrayList<JSONObject> historyList;
    ClickListener clickListener;

    public HistoryTransactionListAdapter(ArrayList<JSONObject> historyList){
        this.historyList = historyList;
    }

    public class HistoryTransactionHolder extends RecyclerView.ViewHolder{
        public RoundedImageView mRoundedImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public SwipeLayout swipeLayout;

        public HistoryTransactionHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.itemRV_historyTransaction_title);
            mTextView2 = itemView.findViewById(R.id.itemRV_historyTransaction_location);
            mTextView3 = itemView.findViewById(R.id.itemRV_historyTransaction_price);

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
    public HistoryTransactionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_transaction_list, viewGroup,false);
        return new HistoryTransactionHolder(v);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTransactionHolder historyTransactionHolder, int i) {
        JSONObject jsonObject = historyList.get(i);

        try {
            historyTransactionHolder.mTextView1.setText(jsonObject.getString("packageName"));
            historyTransactionHolder.mTextView2.setText(jsonObject.getString("location"));
            historyTransactionHolder.mTextView3.setText("Rp. "+jsonObject.getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(HistoryTransactionListAdapter.ClickListener clickListner){
        this.clickListener= clickListner;
    }


}
