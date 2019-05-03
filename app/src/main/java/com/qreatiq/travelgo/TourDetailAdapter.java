package com.qreatiq.travelgo;

import android.graphics.Bitmap;
import android.support.design.button.MaterialButton;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TourDetailAdapter extends RecyclerView.Adapter<TourDetailAdapter.ViewHolder> {

    private ArrayList<JSONObject> array;
    ClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image;
        TextView name,price,quantity;
        ImageView remove_quantity,add_quantity;
        MaterialButton add;
        LinearLayout set_quantity;
        int qty=0;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = (RoundedImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            remove_quantity = (ImageView) itemView.findViewById(R.id.remove_quantity);
            add_quantity = (ImageView) itemView.findViewById(R.id.add_quantity);
            add = (MaterialButton) itemView.findViewById(R.id.add);
            set_quantity = (LinearLayout) itemView.findViewById(R.id.set_quantity);

            quantity.setText(String.valueOf(qty));


        }
    }

    public TourDetailAdapter(ArrayList<JSONObject> array){
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_detail_item, viewGroup,false);
        ViewHolder tlpvh = new ViewHolder(v);
        return tlpvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final JSONObject currentItem = array.get(i);

        try {
            viewHolder.name.setText(currentItem.getString("name"));
            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(currentItem.getDouble("price"));
            viewHolder.price.setText("Rp. " + formattedNumber);

            viewHolder.remove_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.qty--;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.qty));
                    if(viewHolder.qty == 0) {
                        viewHolder.set_quantity.setVisibility(View.GONE);
                        viewHolder.add.setVisibility(View.VISIBLE);
                    }
                        clickListener.onRemoveQuantityClick(viewHolder.qty,i);
                }
            });

            viewHolder.add_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.qty++;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.qty));
                        clickListener.onAddQuantityClick(viewHolder.qty,i);
                }
            });

            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.qty=1;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.qty));
                    viewHolder.set_quantity.setVisibility(View.VISIBLE);
                    viewHolder.add.setVisibility(View.GONE);
                        clickListener.onAddClick(viewHolder.qty,i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public interface ClickListener{
        void onAddClick(int quantity,int position);
        void onRemoveQuantityClick(int quantity,int position);
        void onAddQuantityClick(int quantity,int position);
    }

    public void setOnChangeQuantityListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }
}
