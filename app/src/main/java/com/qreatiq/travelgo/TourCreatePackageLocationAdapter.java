package com.qreatiq.travelgo;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreatePackageLocationAdapter extends RecyclerView.Adapter<TourCreatePackageLocationAdapter.ViewHolder> {

    private ArrayList<String> arrayList;
    ClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        LinearLayout content_layout;
        CardView layout;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public TourCreatePackageLocationAdapter(ArrayList<String> arrayList){
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
        viewHolder.text.setText(arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}