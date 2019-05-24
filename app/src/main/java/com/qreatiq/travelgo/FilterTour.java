package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilterTour extends BaseActivity {

    TextView minPrice, maxPrice, seeLocation, seeDuration, startDate, endDate, reset;
    CrystalRangeSeekbar rangeSeekbar;
    private int year = 2019, month = 3, day = 10, START_DATE = 3, END_DATE = 4;

    Date start_date = new Date(),end_date = new Date();

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour);

        set_toolbar();

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);
        reset = (TextView)findViewById(R.id.reset);

        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);

        simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        showDate(simpleDateFormat.format(start_date),"start");
        showDate(simpleDateFormat.format(end_date),"end");

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

        seeLocation = (TextView) findViewById(R.id.see_locationBtn);
        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterTour.this, FilterTourLocation.class));
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

                showDate(simpleDateFormat.format(new Date()),"start");
                showDate(simpleDateFormat.format(new Date()),"end");
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
        }
    }

    private void showDate(String date, String type) {
        if(type == "start")
            startDate.setText(date);
        else if(type == "end")
            endDate.setText(date);
    }
}
