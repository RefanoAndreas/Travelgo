package com.qreatiq.travelgo;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class HotelRoomAdapter extends RecyclerView.Adapter<HotelRoomAdapter.HotelRoomHolder> {

    ArrayList<JSONObject> hotelRoomList;
    Context context;
    ClickListener clickListener;

    public HotelRoomAdapter(ArrayList<JSONObject> hotelRoomList) {
        this.hotelRoomList = hotelRoomList;
    }

    public class HotelRoomHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView name,price,quantity;
        ImageView remove_quantity,add_quantity;
        MaterialButton add;
        LinearLayout set_quantity;
        int qty=0;

        public HotelRoomHolder(@NonNull View itemView) {
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

    @NonNull
    @Override
    public HotelRoomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hotel_room_list, viewGroup,false);
        return new HotelRoomHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HotelRoomHolder hotelRoomHolder, final int i) {
        final JSONObject currentItem = hotelRoomList.get(i);

        try {
            hotelRoomHolder.name.setText(currentItem.getString("name"));
            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(currentItem.getDouble("price"));
            hotelRoomHolder.price.setText("Rp. "+formattedNumber);

            hotelRoomHolder.remove_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotelRoomHolder.qty--;
                    hotelRoomHolder.quantity.setText(String.valueOf(hotelRoomHolder.qty));
                    if(hotelRoomHolder.qty == 0) {
                        hotelRoomHolder.set_quantity.setVisibility(View.GONE);
                        hotelRoomHolder.add.setVisibility(View.VISIBLE);
                    }
                    clickListener.onRemoveQuantityClick(hotelRoomHolder.qty,i);
                }
            });

            hotelRoomHolder.add_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotelRoomHolder.qty++;
                    hotelRoomHolder.quantity.setText(String.valueOf(hotelRoomHolder.qty));
                    clickListener.onAddQuantityClick(hotelRoomHolder.qty,i);
                }
            });

            hotelRoomHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotelRoomHolder.qty=1;
                    hotelRoomHolder.quantity.setText(String.valueOf(hotelRoomHolder.qty));
                    hotelRoomHolder.set_quantity.setVisibility(View.VISIBLE);
                    hotelRoomHolder.add.setVisibility(View.GONE);
                    clickListener.onAddClick(hotelRoomHolder.qty,i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface ClickListener{
        void onAddClick(int quantity,int position);
        void onRemoveQuantityClick(int quantity,int position);
        void onAddQuantityClick(int quantity,int position);
    }

    public void setOnChangeQuantityListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    @Override
    public int getItemCount() {
        return hotelRoomList.size();
    }

}