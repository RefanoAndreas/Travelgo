package com.qreatiq.travelgo;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qreatiq.travelgo.R;

import java.util.ArrayList;

public class TimeAdapter extends BaseAdapter {

    ArrayList<String> array;
    Context context;

    public TimeAdapter(ArrayList<String> array, Context context){
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
            convertView = layoutInflater.inflate(R.layout.filter_time_item, null);
        }

        AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setText(this.array.get(position));

        return convertView;
    }
}
