package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Filter extends BaseActivity {

    TextView minPrice, maxPrice, reset;
    CrystalRangeSeekbar rangeSeekbar;

    ArrayList<JSONObject> arrayTimeDeparture = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayTimeArrive = new ArrayList<JSONObject>();
    TimeAdapter adapterDeparture,adapterArrival;
    GridView gridDeparture, gridArrival;
    MaterialButton submitBtn;
    ChipGroup transit_group,ranking_group;
    ArrayList<JSONObject> transit_array = new ArrayList<JSONObject>();
    ArrayList<String> ranking_array = new ArrayList<String>();

    LinearLayout ranking_layout,transit,depart_layout,arrive_layout;

    JSONObject filter;
    Long min_price,max_price;
    boolean from_system = false;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        set_toolbar();

        intent = getIntent();
        try {
            filter = new JSONObject(intent.getStringExtra("filter"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);
        ranking_layout = (LinearLayout) findViewById(R.id.ranking_layout);
        transit = (LinearLayout) findViewById(R.id.transit);
        submitBtn = (MaterialButton)findViewById(R.id.submit_saveChanges_filter);
        reset = (TextView) findViewById(R.id.reset);
        transit_group = (ChipGroup) findViewById(R.id.transit_group);
        ranking_group = (ChipGroup) findViewById(R.id.ranking_group);

        gridDeparture = (GridView) findViewById(R.id.gridDepart);
        gridArrival = (GridView) findViewById(R.id.gridArrive);

        depart_layout = (LinearLayout) findViewById(R.id.depart_layout);
        arrive_layout = (LinearLayout) findViewById(R.id.arrive_layout);

        try {
            if(intent.getStringExtra("type").equals("train") || intent.getStringExtra("type").equals("flight")) {
                arrayTimeDeparture.add(new JSONObject("{\"label\":\"00:00 - 11:00\",\"checked\":false}"));
                arrayTimeDeparture.add(new JSONObject("{\"label\":\"11:00 - 15:00\",\"checked\":false}"));
                arrayTimeDeparture.add(new JSONObject("{\"label\":\"15:00 - 18:30\",\"checked\":false}"));
                arrayTimeDeparture.add(new JSONObject("{\"label\":\"18:30 - 23:59\",\"checked\":false}"));

                arrayTimeArrive.add(new JSONObject("{\"label\":\"00:00 - 11:00\",\"checked\":false}"));
                arrayTimeArrive.add(new JSONObject("{\"label\":\"11:00 - 15:00\",\"checked\":false}"));
                arrayTimeArrive.add(new JSONObject("{\"label\":\"15:00 - 18:30\",\"checked\":false}"));
                arrayTimeArrive.add(new JSONObject("{\"label\":\"18:30 - 23:59\",\"checked\":false}"));

                if(!filter.toString().equals("{}")) {
                    for (int x = 0; x < arrayTimeDeparture.size(); x++) {
                        for (int y = 0; y < filter.getJSONArray("departure_date").length(); y++) {
                            if (arrayTimeDeparture.get(x).getString("label").equals(filter.getJSONArray("departure_date").getJSONObject(y).getString("label")))
                                arrayTimeDeparture.get(x).put("checked", true);
                        }
                    }
                    for (int x = 0; x < arrayTimeArrive.size(); x++) {
                        for (int y = 0; y < filter.getJSONArray("arrival_date").length(); y++) {
                            if (arrayTimeArrive.get(x).getString("label").equals(filter.getJSONArray("arrival_date").getJSONObject(y).getString("label")))
                                arrayTimeArrive.get(x).put("checked", true);
                        }
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapterDeparture = new TimeAdapter(arrayTimeDeparture,this);
        adapterArrival = new TimeAdapter(arrayTimeArrive,this);

        gridDeparture.setAdapter(adapterDeparture);
        gridArrival.setAdapter(adapterArrival);

        if(intent.getStringExtra("type").equals("hotel")){
            transit.setVisibility(View.GONE);
            depart_layout.setVisibility(View.GONE);
            arrive_layout.setVisibility(View.GONE);
            ranking_layout.setVisibility(View.VISIBLE);

            try {
                if(!filter.toString().equals("{}")) {
                    Log.d("rank", String.valueOf(filter.getJSONArray("ranking")));
                    for (int x = 0; x < ranking_group.getChildCount(); x++) {
                        final Chip chip = (Chip) ranking_group.getChildAt(x);
                        for (int y = 0; y < filter.getJSONArray("ranking").length(); y++) {
                            if (chip.getText().toString().equals(filter.getJSONArray("ranking").getString(y))) {
                                chip.setChecked(true);
                                ranking_array.add(filter.getJSONArray("ranking").getString(y));
                                break;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            ranking_layout.setVisibility(View.GONE);
            try {
                if(!filter.toString().equals("{}")) {
                    for (int x = 0; x < transit_group.getChildCount(); x++) {
                        final Chip chip = (Chip) transit_group.getChildAt(x);
                        for (int y = 0; y < filter.getJSONArray("transit").length(); y++) {
                            if (chip.getText().toString().equals(filter.getJSONArray("transit").getJSONObject(y).getString("label"))) {
                                chip.setChecked(true);
                                transit_array.add(filter.getJSONArray("transit").getJSONObject(y));
                                break;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int x=0;x<transit_group.getChildCount();x++){
            final Chip chip = (Chip) transit_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(from_system) {
                        from_system = false;
                        return;
                    }
                    try {
                        JSONObject json = new JSONObject("{\"label\":\""+chip.getText().toString()+"\"}");
                        if(isChecked)
                            transit_array.add(json);
                        else {
                            int index=0;
                            for(int x=0;x<transit_array.size();x++){
                                if(transit_array.get(x).toString().equals(json.toString())) {
                                    index = x;
                                    break;
                                }
                            }
                            transit_array.remove(index);
                        }
                        from_system = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=0;x<ranking_group.getChildCount();x++){
            final Chip chip = (Chip) ranking_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(from_system) {
                        from_system = false;
                        return;
                    }

                    if(isChecked)
                        ranking_array.add(chip.getText().toString());
                    else {
                        int index=0;
                        for(int x=0;x<ranking_array.size();x++){
                            if(ranking_array.get(x).toString().equals(chip.getText().toString())) {
                                index = x;
                                break;
                            }
                        }
                        ranking_array.remove(index);
                    }
                    from_system = false;
                }
            });
        }

        try {
            if(!filter.toString().equals("{}")) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(filter.getLong("min_price"));
                minPrice.setText("Rp. "+formattedNumber);

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(filter.getLong("max_price"));
                maxPrice.setText("Rp. "+formattedNumber1);

                rangeSeekbar.setMaxStartValue(filter.getLong("max_price")).apply();
                rangeSeekbar.setMinStartValue(filter.getLong("min_price")).apply();

                min_price = (Long) filter.getLong("min_price");
                max_price = (Long) filter.getLong("max_price");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(minValue);
                minPrice.setText("Rp. "+formattedNumber);

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(maxValue);
                maxPrice.setText("Rp. "+formattedNumber1);

                min_price = (Long) minValue;
                max_price = (Long) maxValue;
            }
        });

        adapterDeparture.setOnCheckedChangeListener(new TimeAdapter.ClickListener() {
            @Override
            public void onCheckedChange(int position, boolean isChecked) {
                try {
                    JSONObject json = arrayTimeDeparture.get(position);
                    json.put("checked",isChecked);
                    arrayTimeDeparture.set(position,json);
                    adapterDeparture.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        adapterArrival.setOnCheckedChangeListener(new TimeAdapter.ClickListener() {
            @Override
            public void onCheckedChange(int position, boolean isChecked) {
                try {
                    JSONObject json = arrayTimeArrive.get(position);
                    json.put("checked",isChecked);
                    arrayTimeArrive.set(position,json);
                    adapterArrival.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    filter.put("min_price",min_price);
                    filter.put("max_price",max_price);

                    if(intent.getStringExtra("type").equals("flight") || intent.getStringExtra("type").equals("train")){
                        if(arrayTimeArrive.size() > 0) {
                            ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
                            for (int x = 0; x < arrayTimeArrive.size(); x++) {
                                if (arrayTimeArrive.get(x).getBoolean("checked"))
                                    arr.add(arrayTimeArrive.get(x));
                            }
                            filter.put("arrival_date", new JSONArray(arr.toString()));
                        }
                        else
                            filter.put("arrival_date", new JSONObject());

                        if(arrayTimeDeparture.size() > 0) {
                            ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
                            for (int x = 0; x < arrayTimeDeparture.size(); x++) {
                                if (arrayTimeDeparture.get(x).getBoolean("checked"))
                                    arr.add(arrayTimeDeparture.get(x));
                            }
                            filter.put("departure_date", new JSONArray(arr.toString()));
                        }
                        else
                            filter.put("departure_date", new JSONObject());

                        filter.put("transit",new JSONArray(transit_array.toString()));
                        filter.put("max_price",max_price);
                    }
                    else if(intent.getStringExtra("type").equals("hotel")){
                        filter.put("ranking",new JSONArray(ranking_array.toString()));
                    }

                    Intent i = new Intent();
                    i.putExtra("filter", filter.toString());
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rangeSeekbar.setMinStartValue(0f).apply();
                rangeSeekbar.setMaxStartValue(300000000f).apply();

                try {
                    if(intent.getStringExtra("type").equals("flight") || intent.getStringExtra("type").equals("train")) {
                        for (int x = 0; x < arrayTimeDeparture.size(); x++) {
                            JSONObject json = arrayTimeDeparture.get(x);
                            json.put("checked", false);
                            arrayTimeDeparture.set(x, json);
                        }
                        adapterDeparture.notifyDataSetChanged();

                        for (int x = 0; x < arrayTimeArrive.size(); x++) {
                            JSONObject json = arrayTimeArrive.get(x);
                            json.put("checked", false);
                            arrayTimeArrive.set(x, json);
                        }
                        adapterArrival.notifyDataSetChanged();

                        for (int x = 0; x < transit_group.getChildCount(); x++) {
                            from_system = true;
                            Chip chip = (Chip) transit_group.getChildAt(x);
                            chip.setChecked(false);
                            transit_array.clear();
                        }
                    }
                    else if(intent.getStringExtra("type").equals("hotel")) {
                        for (int x = 0; x < ranking_group.getChildCount(); x++) {
                            from_system = true;
                            Chip chip = (Chip) ranking_group.getChildAt(x);
                            chip.setChecked(false);
                            ranking_array.clear();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }

}
