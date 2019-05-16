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

    TextView minPrice, maxPrice, seeLocation, seeDuration, startDate, endDate;
    CrystalRangeSeekbar rangeSeekbar;
    private int year = 2019, month = 3, day = 10, START_DATE = 3, END_DATE = 4;

    Date start_date = new Date(),end_date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour);

        set_toolbar();

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);

        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);

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

    }

    public void startDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",format.format(start_date));
        in.putExtra("end_date",format.format(end_date));
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",format.format(start_date));
        in.putExtra("end_date",format.format(end_date));
        startActivityForResult(in,END_DATE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");
                    Date date = new Date(year, month, dayOfMonth-1);
                    String monthOfYear = simplemonth.format(date);
                    showDate(year, month+1, dayOfMonth, monthOfYear, "start");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        else if (id == 998) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");
                    Date date = new Date(year, month, dayOfMonth-1);
                    String monthOfYear = simplemonth.format(date);
                    showDate(year, month+1, dayOfMonth, monthOfYear, "end");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        return null;
    }

    private void showDate(int year, int month, int day, String month1, String type) {
        if(type == "start")
            startDate.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append(" ")
                    .append(month1)
                    .append(" ")
                    .append(year));
        else
            endDate.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append(" ")
                    .append(month1)
                    .append(" ")
                    .append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                    SimpleDateFormat simplemonth = new SimpleDateFormat("mmm");

                    JSONArray json = new JSONArray(data.getStringExtra("date"));

                    String array0 = json.getString(0);
                    start_date = new Date(Integer.parseInt(array0.split("-")[2]),
                            Integer.parseInt(array0.split("-")[1])-1,
                            Integer.parseInt(array0.split("-")[0]));
                    String start_dayOfWeek = simpledateformat.format(start_date);
                    String start_monthOfYear = simplemonth.format(start_date);

                    String last_array = json.getString(json.length()-1);
                    end_date = new Date(Integer.parseInt(last_array.split("-")[2]),
                            Integer.parseInt(last_array.split("-")[1])-1,
                            Integer.parseInt(last_array.split("-")[0]));
                    String end_dayOfWeek = simpledateformat.format(end_date);
                    String end_monthOfYear = simplemonth.format(end_date);

                    showDate(start_date.getYear(),Integer.parseInt(start_monthOfYear),start_date.getDate(),start_monthOfYear,"start");
                    showDate(end_date.getYear(),Integer.parseInt(end_monthOfYear),end_date.getDate(),end_monthOfYear,"end");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
