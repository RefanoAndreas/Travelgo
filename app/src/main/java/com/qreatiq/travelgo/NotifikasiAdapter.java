package com.qreatiq.travelgo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.NotifikasiHolder> {

    ArrayList<JSONObject> notifList;
    ClickListener clickListener;
    ScrollListener scrollListener;
    Context context;

    public NotifikasiAdapter(ArrayList<JSONObject> notifList,Context context){
        this.notifList = notifList;
        this.context = context;
    }

    public class NotifikasiHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public Chip mChip1;
        public ImageView notifIcon;

        public NotifikasiHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TV_date);
            mTextView2 = itemView.findViewById(R.id.TV_route);
            mTextView3 = itemView.findViewById(R.id.TV_infoTrip);
            mTextView4 = itemView.findViewById(R.id.TV_total_pack);
            mTextView5 = itemView.findViewById(R.id.TV_routeType);
            mChip1 = itemView.findViewById(R.id.CHIP_status);
            notifIcon = itemView.findViewById(R.id.notifIcon);

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
            notifikasiHolder.mTextView1.setText(jsonObject.getString("date1"));
            notifikasiHolder.mTextView2.setText(jsonObject.getString("info1"));
            notifikasiHolder.mTextView3.setText(jsonObject.getString("info2"));
            notifikasiHolder.mTextView4.setText(jsonObject.getString("info3"));
            notifikasiHolder.mTextView5.setText(jsonObject.getString("info4"));
            notifikasiHolder.mChip1.setText(context.getResources().getStringArray(R.array.status_sales)[jsonObject.getInt("status")]);

            if(jsonObject.getInt("status") == 0){
                notifikasiHolder.mChip1.setChipBackgroundColorResource(R.color.colorPrimary);
            }
            else if(jsonObject.getInt("status") == 2){
                notifikasiHolder.mChip1.setChipBackgroundColorResource(R.color.colorGreen);
                notifikasiHolder.mChip1.setTextColor(Color.parseColor("#424242"));
            }
            else{
                notifikasiHolder.mChip1.setChipBackgroundColorResource(R.color.colorYellow);
                notifikasiHolder.mChip1.setTextColor(Color.parseColor("#424242"));
            }

            if(jsonObject.getString("salesType").equals("flight")){
                notifikasiHolder.notifIcon.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
            }
            else if(jsonObject.getString("salesType").equals("train")){
                notifikasiHolder.notifIcon.setImageResource(R.drawable.ic_train_black_24dp);
            }
            else if(jsonObject.getString("salesType").equals("hotel") || jsonObject.getString("salesType").equals("tour")){
                notifikasiHolder.notifIcon.setImageResource(R.drawable.ic_location_city_black_24dp);
            }

            if(i == getItemCount()-1)
                scrollListener.onBottomReached();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public interface ScrollListener{
        void onBottomReached();
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public void setOnScrollListener(ScrollListener scrollListener){
        this.scrollListener= scrollListener;
    }

}
