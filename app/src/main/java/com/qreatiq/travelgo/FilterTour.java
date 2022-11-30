package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterTour extends BaseActivity {

    TextView minPrice, maxPrice, seeLocation, seeDuration, startDate, endDate, reset;
    CrystalRangeSeekbar rangeSeekbar;
    MaterialButton submit;
    private int year = 2019, month = 3, day = 10, START_DATE = 3, END_DATE = 4, FILTER_LOCATION_ALL = 1;
    Long min_price = Long.valueOf(0),max_price = Long.valueOf(30000000);
    JSONObject filter;
    JSONArray location_arr = new JSONArray();
    ChipGroup location,time_range;
    ArrayList<JSONObject> location_array = new ArrayList<JSONObject>(),time_range_array = new ArrayList<JSONObject>();
    boolean from_system = false;
    String url;
    int duration = -1;
    Intent intent;

    Date start_date = new Date(),end_date = new Date();

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour);

        set_toolbar();

        getLocation();

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);
        reset = (TextView)findViewById(R.id.reset);
        location = (ChipGroup) findViewById(R.id.location);
        time_range = (ChipGroup) findViewById(R.id.time_range);

        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        submit = (MaterialButton) findViewById(R.id.submit);

        try {
            filter = new JSONObject(getIntent().getStringExtra("filter"));
            if(!filter.toString().equals("{}")){
                start_date = new Date(filter.getLong("start_date_number"));
                end_date = new Date(filter.getLong("end_date_number"));

                rangeSeekbar.setMaxStartValue(filter.getLong("max_price"))
                        .setMinStartValue(filter.getLong("min_price"))
                        .apply();

                min_price = filter.getLong("min_price");
                max_price = filter.getLong("max_price");


            }

            for(int x=0;x<4;x++){
                final JSONObject json = new JSONObject();
                json.put("label",(x+1)+" "+getResources().getString(R.string.day_label)+" "+x+" "+getResources().getString(R.string.night_label));
                if(!filter.toString().equals("{}")) {
                    int counter = 0;
                    for(int y=0;y<filter.getJSONArray("time_range").length();y++) {
                        if(filter.getJSONArray("time_range").getJSONObject(y).getInt("data") == x+1) {
                            json.put("checked", true);
                            break;
                        }
                        else
                            counter++;
                    }

                    if(counter == filter.getJSONArray("time_range").length())
                        json.put("checked", false);
                }
                else
                    json.put("checked",false);
                json.put("data",x+1);
                time_range_array.add(json);

                final View view = LayoutInflater.from(FilterTour.this).inflate(R.layout.chip_loc_filter,null);
                final Chip chip = (Chip) view.findViewById(R.id.chip);
                chip.setText(json.getString("label"));
                chip.setChecked(json.getBoolean("checked"));
                time_range.addView(view);

                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            for(int x=0;x<time_range_array.size();x++) {
                                if (chip.getText().toString().equals(time_range_array.get(x).getString("label"))) {
                                    time_range_array.get(x).put("checked", isChecked);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        showDate(simpleDateFormat.format(start_date),"start");
        showDate(simpleDateFormat.format(end_date),"end");

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(minValue);
                minPrice.setText("Rp. "+formattedNumber);
                min_price = (Long) minValue;

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(maxValue);
                maxPrice.setText("Rp. "+formattedNumber1);
                max_price = (Long) maxValue;
            }
        });

        seeLocation = (TextView) findViewById(R.id.see_locationBtn);
        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
                    for(int x=0;x<location_array.size();x++){
                        if(location_array.get(x).getBoolean("checked")) {
                            JSONObject json = new JSONObject();
                            json.put("id",location_array.get(x).getString("id"));
                            json.put("label",location_array.get(x).getString("label"));
                            jsonObjectArrayList.add(json);
                        }
                    }
                    startActivityForResult(new Intent(FilterTour.this, FilterTourLocation.class)
                            .putExtra("city_id", getIntent().getStringExtra("city_id"))
                            .putExtra("location",jsonObjectArrayList.toString()),FILTER_LOCATION_ALL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        seeDuration = (TextView) findViewById(R.id.see_durationBtn);
        seeDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterTourDuration filterTourDuration = new FilterTourDuration();
                filterTourDuration.show(getSupportFragmentManager(), "Pilih Jangka Waktu");
                filterTourDuration.setStyle(filterTourDuration.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rangeSeekbar.setMinStartValue(0f).apply();
                rangeSeekbar.setMaxStartValue(300000000f).apply();

                start_date = new Date();
                end_date = new Date();

                showDate(simpleDateFormat.format(start_date),"start");
                showDate(simpleDateFormat.format(end_date),"end");

                for (int x = 0; x < location.getChildCount(); x++) {
                    from_system = true;
                    View view = location.getChildAt(x);
                    Chip chip = (Chip) view.findViewById(R.id.chip);
                    chip.setChecked(false);
                }

                for (int x = 0; x < time_range.getChildCount(); x++) {
                    from_system = true;
                    View view = time_range.getChildAt(x);
                    Chip chip = (Chip) view.findViewById(R.id.chip);
                    chip.setChecked(false);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<JSONObject> duration_array = new ArrayList<JSONObject>();
                    for(int x=0;x<time_range_array.size();x++){
                        if(time_range_array.get(x).getBoolean("checked"))
                            duration_array.add(time_range_array.get(x));
                    }

                    ArrayList<JSONObject> loc_array = new ArrayList<JSONObject>();
                    for(int x=0;x<location_array.size();x++){
                        if(location_array.get(x).getBoolean("checked"))
                            loc_array.add(location_array.get(x));
                    }

                    JSONObject json = new JSONObject();
                    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    json.put("start_date",simpleDateFormat.format(start_date));
                    json.put("end_date",simpleDateFormat.format(end_date));
                    json.put("start_date_number",start_date.getTime());
                    json.put("end_date_number",end_date.getTime());
                    json.put("max_price",max_price);
                    json.put("min_price",min_price);
                    json.put("location",new JSONArray(loc_array.toString()));
                    json.put("time_range",new JSONArray(duration_array.toString()));

                    Intent i = new Intent();
                    i.putExtra("filter", json.toString());
                    i.putExtra("city_id", getIntent().getStringExtra("city_id"));
                    setResult(RESULT_OK, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void startDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",true);
        in.putExtra("type","tour");
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",true);
        in.putExtra("type","tour");
        startActivityForResult(in,END_DATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                    JSONArray json = new JSONArray(data.getStringExtra("date"));

                    start_date = new Date(json.getLong(0));
                    end_date = new Date(json.getLong(json.length() - 1));

                    showDate(simpledateformat.format(start_date), "start");
                    showDate(simpledateformat.format(end_date), "end");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == FILTER_LOCATION_ALL) {
                try {
                    location_arr = new JSONArray(data.getStringExtra("location"));
                    if(location_arr.length() > 0) {
                        for (int x = 0; x < location_arr.length(); x++) {
                            int counter = 0;
                            for(int z=0;z<location_array.size();z++){
                                if(location_array.get(z).getString("id").equals(
                                        location_arr.getJSONObject(x).getString("id"))) {
                                    location_array.get(z).put("checked", true);
                                    for(int y=0;y<location.getChildCount();y++){
                                        View view = location.getChildAt(y);
                                        Chip chip = (Chip) view.findViewById(R.id.chip);
                                        if(chip.getText().toString().equals(location_array.get(z).getString("label"))) {
                                            chip.setChecked(true);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                else
                                    counter++;
                            }
                            if(counter == location_array.size()){
                                JSONObject json = new JSONObject();
                                json.put("label",location_arr.getJSONObject(x).getString("label"));
                                json.put("checked", true);
                                json.put("id",location_arr.getJSONObject(x).getString("id"));
                                location_array.add(json);

                                final View view = LayoutInflater.from(this).inflate(R.layout.chip_loc_filter,null);
                                final Chip chip = (Chip) view.findViewById(R.id.chip);
                                chip.setText(json.getString("label"));
                                chip.setChecked(json.getBoolean("checked"));
                                location.addView(view);

                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        try {
                                            for(int x=0;x<location_array.size();x++) {
                                                if (chip.getText().toString().equals(location_array.get(x).getString("label"))) {
                                                    location_array.get(x).put("checked", isChecked);
                                                    break;
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    else{
                        for(int x=0;x<location.getChildCount();x++){
                            View view = location.getChildAt(x);
                            Chip chip = (Chip) view.findViewById(R.id.chip);
                            chip.setChecked(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDate(String date, String type) {
        if(type == "start")
            startDate.setText(date);
        else if(type == "end")
            endDate.setText(date);
    }

    private void getLocation(){
        url = C_URL+"tour/filter-place?city_id="+getIntent().getStringExtra("city_id");
        Log.d("url", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    for(int x=0;x<response.getJSONArray("location").length();x++){
                        final JSONObject json = new JSONObject();
                        json.put("label",response.getJSONArray("location").getJSONObject(x).getString("name"));
                        if(!filter.toString().equals("{}")) {
                            int counter = 0;
                            for(int y=0;y<filter.getJSONArray("location").length();y++) {
                                if(filter.getJSONArray("location").getJSONObject(y).getString("id").equals(
                                        response.getJSONArray("location").getJSONObject(x).getString("id"))) {
                                    json.put("checked", true);
                                    break;
                                }
                                else
                                    counter++;
                            }

                            if(counter == filter.getJSONArray("location").length())
                                json.put("checked", false);
                        }
                        else
                            json.put("checked",false);
                        json.put("id",response.getJSONArray("location").getJSONObject(x).getString("id"));
                        location_array.add(json);

                        final View view = LayoutInflater.from(FilterTour.this).inflate(R.layout.chip_loc_filter,null);
                        final Chip chip = (Chip)view.findViewById(R.id.chip);
                        chip.setText(json.getString("label"));
                        chip.setChecked(json.getBoolean("checked"));
                        location.addView(view);

                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                try {
                                    for(int x=0;x<location_array.size();x++) {
                                        if (chip.getText().toString().equals(location_array.get(x).getString("label"))) {
                                            location_array.get(x).put("checked", isChecked);
                                            break;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
