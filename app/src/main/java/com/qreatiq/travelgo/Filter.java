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
    ChipGroup kelas_group,transit_group;
    ArrayList<String> transit_array = new ArrayList<String>();
    ArrayList<String> kelas_array = new ArrayList<String>();

    LinearLayout kelas,transit;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        set_toolbar();

        intent = getIntent();

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);
        kelas = (LinearLayout) findViewById(R.id.kelas);
        transit = (LinearLayout) findViewById(R.id.transit);
        submitBtn = (MaterialButton)findViewById(R.id.submit_saveChanges_filter);
        reset = (TextView) findViewById(R.id.reset);
        kelas_group = (ChipGroup) findViewById(R.id.kelas_group);
        transit_group = (ChipGroup) findViewById(R.id.transit_group);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(minValue);
                minPrice.setText("Rp. "+formattedNumber);

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(maxValue);
                maxPrice.setText("Rp. "+formattedNumber1);
            }
        });

        gridDeparture = (GridView) findViewById(R.id.gridDepart);
        gridArrival = (GridView) findViewById(R.id.gridArrive);

        try {
            arrayTimeDeparture.add(new JSONObject("{\"label\":\"00:00 - 11:00\",\"checked\":false}"));
            arrayTimeDeparture.add(new JSONObject("{\"label\":\"11:00 - 15:00\",\"checked\":false}"));
            arrayTimeDeparture.add(new JSONObject("{\"label\":\"15:00 - 18:30\",\"checked\":false}"));
            arrayTimeDeparture.add(new JSONObject("{\"label\":\"18:30 - 23:59\",\"checked\":false}"));

            arrayTimeArrive.add(new JSONObject("{\"label\":\"00:00 - 11:00\",\"checked\":false}"));
            arrayTimeArrive.add(new JSONObject("{\"label\":\"11:00 - 15:00\",\"checked\":false}"));
            arrayTimeArrive.add(new JSONObject("{\"label\":\"15:00 - 18:30\",\"checked\":false}"));
            arrayTimeArrive.add(new JSONObject("{\"label\":\"18:30 - 23:59\",\"checked\":false}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapterDeparture = new TimeAdapter(arrayTimeDeparture,this);
        adapterArrival = new TimeAdapter(arrayTimeArrive,this);

        gridDeparture.setAdapter(adapterDeparture);
        gridArrival.setAdapter(adapterArrival);

        if(intent.getStringExtra("type").equals("flight")){
            kelas.setVisibility(View.GONE);
            transit.setVisibility(View.VISIBLE);
        }
        else{
            transit.setVisibility(View.GONE);
            kelas.setVisibility(View.VISIBLE);
        }

        for(int x=0;x<transit_group.getChildCount();x++){
            final Chip chip = (Chip) transit_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        transit_array.add(chip.getText().toString());
                    else
                        transit_array.remove(transit_array.indexOf(chip.getText().toString()));
                }
            });
        }

        for(int x=0;x<kelas_group.getChildCount();x++){
            final Chip chip = (Chip) kelas_group.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        kelas_array.add(chip.getText().toString());
                    else
                        kelas_array.remove(kelas_array.indexOf(chip.getText().toString()));
                }
            });
        }

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
//                Log.d("data",transit_array.toString());
                Intent i = new Intent();
                i.putExtra("filter", "filter");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rangeSeekbar.setMinValue(0f);
                rangeSeekbar.setMaxValue(300000000f);

                try {
                    for(int x=0;x<arrayTimeDeparture.size();x++) {
                        JSONObject json = arrayTimeDeparture.get(x);
                        json.put("checked", false);
                        arrayTimeDeparture.set(x, json);
                    }
                    adapterDeparture.notifyDataSetChanged();

                    for(int x=0;x<arrayTimeArrive.size();x++) {
                        JSONObject json = arrayTimeArrive.get(x);
                        json.put("checked", false);
                        arrayTimeArrive.set(x, json);
                    }
                    adapterArrival.notifyDataSetChanged();

                    for(int x=0;x<transit_group.getChildCount();x++){
                        Chip chip = (Chip) transit_group.getChildAt(x);
                        chip.setChecked(false);
                    }

                    for(int x=0;x<kelas_group.getChildCount();x++){
                        Chip chip = (Chip) kelas_group.getChildAt(x);
                        chip.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }

}
