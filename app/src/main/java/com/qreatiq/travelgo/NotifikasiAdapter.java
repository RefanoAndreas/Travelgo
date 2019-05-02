package com.qreatiq.travelgo;

import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.NotifikasiHolder> {

    ArrayList<JSONObject> notifList;
    ClickListener clickListener;

    public NotifikasiAdapter(ArrayList<JSONObject> notifList){
        this.notifList = notifList;
    }

    public class NotifikasiHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public Chip mChip1;

        public NotifikasiHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TV_date);
            mTextView2 = itemView.findViewById(R.id.TV_route);
            mTextView3 = itemView.findViewById(R.id.TV_infoTrip);
            mTextView4 = itemView.findViewById(R.id.TV_total_pack);
            mTextView5 = itemView.findViewById(R.id.TV_routeType);
            mChip1 = itemView.findViewById(R.id.CHIP_status);

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
    public NotifikasiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.d1_notifikasi_item, viewGroup,false);
        return new NotifikasiHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiHolder notifikasiHolder, int i) {
        JSONObject jsonObject = notifList.get(i);

        try {
            notifikasiHolder.mTextView1.setText(jsonObject.getString("date"));
            notifikasiHolder.mTextView2.setText(jsonObject.getString("route"));
            notifikasiHolder.mTextView3.setText(jsonObject.getString("infoTrip"));
            notifikasiHolder.mTextView4.setText(jsonObject.getString("totalPack"));
            notifikasiHolder.mTextView5.setText(jsonObject.getString("routeType"));
            notifikasiHolder.mChip1.setText(jsonObject.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

}
