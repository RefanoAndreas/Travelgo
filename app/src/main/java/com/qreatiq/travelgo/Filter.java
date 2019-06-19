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
    ChipGroup transit_group,ranking_group,train_class_group;
    ArrayList<JSONObject> transit_array = new ArrayList<JSONObject>(),train_class_array = new ArrayList<JSONObject>(),ranking_array = new ArrayList<JSONObject>();

    LinearLayout ranking_layout,transit,depart_layout,arrive_layout,train_class;

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
        train_class = (LinearLayout) findViewById(R.id.train_class);
        submitBtn = (MaterialButton)findViewById(R.id.submit_saveChanges_filter);
        reset = (TextView) findViewById(R.id.reset);
        transit_group = (ChipGroup) findViewById(R.id.transit_group);
        ranking_group = (ChipGroup) findViewById(R.id.ranking_group);
        train_class_group = (ChipGroup) findViewById(R.id.train_class_group);

        gridDeparture = (GridView) findViewById(R.id.gridDepart);
        gridArrival = (GridView) findViewById(R.id.gridArrive);

        depart_layout = (LinearLayout) findViewById(R.id.depart_layout);
        arrive_layout = (LinearLayout) findViewById(R.id.arrive_layout);

        try {
            transit_array.add(new JSONObject("{\"label\":\"Langsung\",\"data\":1,\"checked\":false}"));
            transit_array.add(new JSONObject("{\"label\":\"1 Transit\",\"data\":2,\"checked\":false}"));
            transit_array.add(new JSONObject("{\"label\":\"2+ Transit\",\"data\":3,\"checked\":false}"));

            train_class_array.add(new JSONObject("{\"label\":\"Ekonomi\",\"data\":\"ekonomi\",\"checked\":false}"));
            train_class_array.add(new JSONObject("{\"label\":\"Bisnis\",\"data\":\"bisnis\",\"checked\":false}"));
            train_class_array.add(new JSONObject("{\"label\":\"Eksekutif\",\"data\":\"eksekutif\",\"checked\":false}"));

            ranking_array.add(new JSONObject("{\"label\":\"1 star\",\"data\":1,\"checked\":false}"));
            ranking_array.add(new JSONObject("{\"label\":\"2 star\",\"data\":2,\"checked\":false}"));
            ranking_array.add(new JSONObject("{\"label\":\"3 star\",\"data\":3,\"checked\":false}"));
            ranking_array.add(new JSONObject("{\"label\":\"4 star\",\"data\":4,\"checked\":false}"));
            ranking_array.add(new JSONObject("{\"label\":\"5 star\",\"data\":5,\"checked\":false}"));

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
            train_class.setVisibility(View.GONE);
            depart_layout.setVisibility(View.GONE);
            arrive_layout.setVisibility(View.GONE);
            ranking_layout.setVisibility(View.VISIBLE);

            try {
                if(!filter.toString().equals("{}")) {
                    for (int x = 0; x < filter.getJSONArray("ranking").length(); x++) {
                        for (int y = 0; y < ranking_array.size(); y++) {
                            if (filter.getJSONArray("ranking").getJSONObject(x).getInt("data") == ranking_array.get(y).getInt("data")){
                                ranking_array.get(y).put("checked",true);
                                break;
                            }
                        }
                    }
                    update_ranking_view();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            ranking_layout.setVisibility(View.GONE);
            if(intent.getStringExtra("type").equals("flight")) {
                train_class.setVisibility(View.GONE);
                try {
                    if (!filter.toString().equals("{}")) {
                        for (int x = 0; x < filter.getJSONArray("transit").length(); x++) {
                            for (int y = 0; y < transit_array.size(); y++) {
                                if (filter.getJSONArray("transit").getJSONObject(x).getString("label").equals(
                                        transit_array.get(y).getString("label"))){
                                    transit_array.get(y).put("checked",true);
                                    break;
                                }
                            }
                        }
                        update_transit_view();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                transit.setVisibility(View.GONE);
                try {
                    if (!filter.toString().equals("{}")) {
                        for (int x = 0; x < filter.getJSONArray("class").length(); x++) {
                            for (int y = 0; y < train_class_array.size(); y++) {
                                if (filter.getJSONArray("class").getJSONObject(x).getString("label").equals(
                                        train_class_array.get(y).getString("label"))){
                                    train_class_array.get(y).put("checked",true);
                                    break;
                                }
                            }
                        }
                        update_class_view();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int x=0;x<transit_group.getChildCount();x++){
            final Chip chip = (Chip) transit_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        for (int x = 0; x < transit_array.size(); x++) {
                            if(transit_array.get(x).getString("label").equals(chip.getText().toString())) {
                                transit_array.get(x).put("checked", isChecked);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=0;x<train_class_group.getChildCount();x++){
            final Chip chip = (Chip) train_class_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        for (int x = 0; x < train_class_array.size(); x++) {
                            if(train_class_array.get(x).getString("label").equals(chip.getText().toString())) {
                                train_class_array.get(x).put("checked", isChecked);
                                break;
                            }
                        }
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
                    try {
                        for (int x = 0; x < ranking_array.size(); x++) {
                            if(ranking_array.get(x).getString("data").equals(chip.getText().toString())) {
                                ranking_array.get(x).put("checked", isChecked);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                        if(intent.getStringExtra("type").equals("flight")) {
                            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
                            for(int x=0;x<transit_array.size();x++){
                                if(transit_array.get(x).getBoolean("checked"))
                                    arrayList.add(transit_array.get(x));
                            }
                            filter.put("transit", new JSONArray(arrayList.toString()));
                        }
                        else{
                            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
                            for(int x=0;x<train_class_array.size();x++){
                                if(train_class_array.get(x).getBoolean("checked"))
                                    arrayList.add(train_class_array.get(x));
                            }
                            filter.put("class", new JSONArray(arrayList.toString()));
                        }
                    }
                    else if(intent.getStringExtra("type").equals("hotel")){
                        ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
                        for(int x=0;x<ranking_array.size();x++){
                            if(ranking_array.get(x).getBoolean("checked"))
                                arrayList.add(ranking_array.get(x));
                        }
                        filter.put("ranking", new JSONArray(arrayList.toString()));
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

    private void update_transit_view() throws JSONException {
        for (int x = 0; x < transit_group.getChildCount(); x++) {
            final Chip chip = (Chip) transit_group.getChildAt(x);
            for (int y = 0; y < transit_array.size(); y++) {
                if (chip.getText().toString().equals(transit_array.get(y).getString("label"))) {
                    chip.setChecked(transit_array.get(y).getBoolean("checked"));
                    break;
                }
            }
        }
    }

    private void update_class_view() throws JSONException {
        for (int x = 0; x < train_class_group.getChildCount(); x++) {
            final Chip chip = (Chip) train_class_group.getChildAt(x);
            for (int y = 0; y < train_class_array.size(); y++) {
                if (chip.getText().toString().equals(train_class_array.get(y).getString("label"))) {
                    chip.setChecked(train_class_array.get(y).getBoolean("checked"));
                    break;
                }
            }
        }
    }

    private void update_ranking_view() throws JSONException {
        for (int x = 0; x < ranking_group.getChildCount(); x++) {
            final Chip chip = (Chip) ranking_group.getChildAt(x);
            for (int y = 0; y < ranking_array.size(); y++) {
                if (chip.getText().toString().equals(ranking_array.get(y).getString("data"))) {
                    chip.setChecked(ranking_array.get(y).getBoolean("checked"));
                    break;
                }
            }
        }
    }
}
