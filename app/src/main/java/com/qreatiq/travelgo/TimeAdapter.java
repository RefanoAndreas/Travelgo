package com.qreatiq.travelgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.qreatiq.travelgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeAdapter extends BaseAdapter {

    ArrayList<JSONObject> array;
    Context context;
    ClickListener clickListener;

    public TimeAdapter(ArrayList<JSONObject> array, Context context){
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
            convertView = layoutInflater.inflate(R.layout.filter_time_item, null);
        }

        AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox);
        try {
            checkBox.setText(this.array.get(position).getString("label"));
            checkBox.setChecked(this.array.get(position).getBoolean("checked"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clickListener.onCheckedChange(position,isChecked);
            }
        });

        return convertView;
    }

    public interface ClickListener{
        void onCheckedChange(int position, boolean isChecked);
    }

    public void setOnCheckedChangeListener(ClickListener clickListner){
        this.clickListener= clickListner;
    }
}
