package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    int duration = 1;

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

                try {
                    location_arr = filter.getJSONArray("location");
                    for (int x = 0; x < location.getChildCount(); x++) {
                        final Chip chip = (Chip) location.getChildAt(x);
                        for (int y = 0; y < filter.getJSONArray("location").length(); y++) {
                            if (chip.getText().toString().equals(filter.getJSONArray("location").getJSONObject(y).getString("id"))) {
                                chip.setChecked(true);
                                location_array.add(filter.getJSONArray("location").getJSONObject(y));
                                break;
                            }
                        }
                    }

                    for (int x = 0; x < time_range.getChildCount(); x++) {
                        final Chip chip = (Chip) time_range.getChildAt(x);
                        for (int y = 0; y < filter.getJSONArray("time_range").length(); y++) {
                            if (chip.getText().toString().equals(filter.getJSONArray("time_range").getJSONObject(y).getString("id"))) {
                                chip.setChecked(true);
                                time_range_array.add(filter.getJSONArray("time_range").getJSONObject(y));
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        for(int x=0;x<location.getChildCount();x++){
            final Chip chip = (Chip) location.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(from_system) {
                        from_system = false;
                        return;
                    }
                    try {
                        JSONObject json = new JSONObject("{\"id\":\""+chip.getText().toString()+"\"}");
                        if(isChecked)
                            location_array.add(json);
                        else {
                            int index=0;
                            for(int x=0;x<location_array.size();x++){
                                if(location_array.get(x).toString().equals(json.toString())) {
                                    index = x;
                                    break;
                                }
                            }
                            location_array.remove(index);
                        }
                        from_system = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=0;x<time_range.getChildCount();x++){
            final Chip chip = (Chip) time_range.getChildAt(x);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(from_system) {
                        from_system = false;
                        return;
                    }
                    try {
                        JSONObject json = new JSONObject("{\"id\":\""+chip.getText().toString()+"\"}");
                        if(isChecked)
                            time_range_array.add(json);
                        else {
                            int index=0;
                            for(int x=0;x<time_range_array.size();x++){
                                if(time_range_array.get(x).toString().equals(json.toString())) {
                                    index = x;
                                    break;
                                }
                            }
                            time_range_array.remove(index);
                        }
                        from_system = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        seeLocation = (TextView) findViewById(R.id.see_locationBtn);
        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FilterTour.this, FilterTourLocation.class)
                        .putExtra("location",location_arr.toString()),FILTER_LOCATION_ALL);
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

//                for (int x = 0; x < location.getChildCount(); x++) {
//                    from_system = true;
//                    Chip chip = (Chip) location.getChildAt(x);
//                    chip.setChecked(false);
//                    location_array.clear();
//                }

                location.removeAllViews();
                getLocation();

                for (int x = 0; x < time_range.getChildCount(); x++) {
                    from_system = true;
                    Chip chip = (Chip) time_range.getChildAt(x);
                    chip.setChecked(false);
                    time_range_array.clear();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject json = new JSONObject();
                    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    json.put("start_date",simpleDateFormat.format(start_date));
                    json.put("end_date",simpleDateFormat.format(end_date));
                    json.put("start_date_number",start_date.getTime());
                    json.put("end_date_number",end_date.getTime());
                    json.put("max_price",max_price);
                    json.put("min_price",min_price);
                    json.put("location",new JSONArray(location_array.toString()));
                    json.put("time_range",new JSONArray(time_range_array.toString()));

                    Intent i = new Intent();
                    i.putExtra("filter", json.toString());
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
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",true);
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
                    location.removeAllViews();
                    location_arr = new JSONArray(data.getStringExtra("location"));
                    for(int x=0;x<location_arr.length();x++) {
                        location_array.add(location_arr.getJSONObject(x));
                        LayoutInflater inflater = LayoutInflater.from(FilterTour.this);
                        View chip = inflater.inflate(R.layout.chip_loc_filter,null);
                        Chip chip1 = (Chip)chip.findViewById(R.id.chip);
                        chip1.setText(location_arr.getJSONObject(x).getString("label"));
                        location.addView(chip);
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
        url = C_URL+"tour/filter-place";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    for(int x=0;x<response.getJSONArray("location").length();x++){
                        LayoutInflater inflater = LayoutInflater.from(FilterTour.this);
                        View chip = inflater.inflate(R.layout.chip_loc_filter,null);
                        Chip chip1 = (Chip)chip.findViewById(R.id.chip);
                        chip1.setText(response.getJSONArray("location").getJSONObject(x).getString("name"));
                        location.addView(chip);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                String message="";
                if (error instanceof NetworkError) {
                    message="Network Error";
                }
                else if (error instanceof ServerError) {
                    message="Server Error";
                }
                else if (error instanceof AuthFailureError) {
                    message="Authentication Error";
                }
                else if (error instanceof ParseError) {
                    message="Parse Error";
                }
                else if (error instanceof NoConnectionError) {
                    message="Connection Missing";
                }
                else if (error instanceof TimeoutError) {
                    message="Server Timeout Reached";
                }
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
