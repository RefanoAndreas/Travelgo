package com.qreatiq.travelgo;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreatePackageLocationAdapter extends RecyclerView.Adapter<TourCreatePackageLocationAdapter.ViewHolder> {

    private ArrayList<JSONObject> arrayList;
    ClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        LinearLayout content_layout;
        CardView layout;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            this.itemView = itemView;
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public TourCreatePackageLocationAdapter(ArrayList<JSONObject> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_package_location_item, viewGroup,false);
        ViewHolder tlpvh = new ViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
//        viewHolder.text.setText(arrayList.get(i));
        JSONObject jsonObject = arrayList.get(i);

        try {
            viewHolder.text.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}