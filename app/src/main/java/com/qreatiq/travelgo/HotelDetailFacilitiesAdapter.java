package com.qreatiq.travelgo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class HotelDetailFacilitiesAdapter extends BaseAdapter {
    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public interface ClickListener{
        void onItemClick(int position,boolean checked);
    }

    public void setOnItemClickListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public HotelDetailFacilitiesAdapter(ArrayList<JSONObject> array, Context context){
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.hotel_facilities_item, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        try {
            text.setText(array.get(position).getString("name"));
            image.setImageResource(R.drawable.ic_wifi_black_24dp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
