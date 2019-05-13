package com.qreatiq.travelgo;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourDetailFacilitiesAdapter extends BaseAdapter {

    ArrayList<JSONObject> array;
    Context context;

    public TourDetailFacilitiesAdapter(ArrayList<JSONObject> array, Context context){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.tour_detail_facilities_item, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.IV_facilities);
        TextView textView = (TextView)convertView.findViewById(R.id.TV_facilities);

        JSONObject jsonObject = array.get(position);

        try {
            textView.setText(jsonObject.getString("name"));

            if(jsonObject.getString("name").equals("Dinner")){
                imageView.setImageResource(R.drawable.ic_restaurant_menu_black_24dp);
            }
            else if(jsonObject.getString("name").equals("Wifi")){
                imageView.setImageResource(R.drawable.ic_wifi_black_24dp);
            }
            else if(jsonObject.getString("name").equals("Documentation")){
                imageView.setImageResource(R.drawable.ic_photo_camera_black_24dp);
            }
            else if(jsonObject.getString("name").equals("Cafe and Resto")){
                imageView.setImageResource(R.drawable.ic_local_bar_black_24dp);
            }
            else if(jsonObject.getString("name").equals("Coffee Break")){
                imageView.setImageResource(R.drawable.ic_local_cafe_black_24dp);
            }
            else if(jsonObject.getString("name").equals("Medicine")){
                imageView.setImageResource(R.drawable.ic_local_hospital_black_24dp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
