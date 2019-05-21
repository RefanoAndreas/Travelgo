package com.qreatiq.travelgo;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecialOrderHotelAdapter extends BaseAdapter {
    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public interface ClickListener{
        void onItemClick(int position,boolean checked);
    }

    public void setOnItemClickListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }

    public SpecialOrderHotelAdapter(ArrayList<JSONObject> array, Context context){
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
            convertView = layoutInflater.inflate(R.layout.special_order_hotel_list, null);
        }

        AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox);
        try {
            checkBox.setText(this.array.get(position).getString("label"));
            checkBox.setChecked(array.get(position).getBoolean("checked"));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    clickListener.onItemClick(position,isChecked);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
