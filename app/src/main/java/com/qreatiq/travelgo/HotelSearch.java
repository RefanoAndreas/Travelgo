package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HotelSearch extends BaseActivity {

    MaterialButton searchBtn;
    TextView TV_searchHotel, tanggalCheckin, tanggalCheckout, duration;
    private int year = 2019, month = 3, day = 10;
    Calendar checkin, checkout;

    int HOTEL_SEARCH = 1, START_DATE = 3, END_DATE = 4;
    Date start_date = new Date(),end_date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);

        set_toolbar();

        final CardView tamuHotel = findViewById(R.id.hotelSearch_TamuKamar);
        tamuHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TamuHotel tamuHotelSheet = new TamuHotel();
                tamuHotelSheet.show(getSupportFragmentManager(), "Pilih Jumlah Penumpang");
                tamuHotelSheet.setStyle(tamuHotelSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });


        tanggalCheckin = (TextView)findViewById(R.id.tanggalCheckin);
        tanggalCheckout = (TextView)findViewById(R.id.tanggalCheckout);

        checkin = Calendar.getInstance();
        checkout = Calendar.getInstance();

        searchBtn = (MaterialButton)findViewById(R.id.search_hotel);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelSearch.this, HotelSearchResult.class)
                        .putExtra("tanggal_berangkat", tanggalCheckin.getText()));
            }
        });

        TV_searchHotel = (TextView)findViewById(R.id.TV_searchHotel);
        TV_searchHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HotelSearch.this, SearchFlight.class);
                in.putExtra("type","hotel");
                in.putExtra("data",new JSONObject().toString());
                startActivityForResult(in,HOTEL_SEARCH);
            }
        });

        duration = (TextView) findViewById(R.id.TV_duration);
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
            tanggalCheckin.setText(date);
        else
            tanggalCheckout.setText(date);
    }

}
