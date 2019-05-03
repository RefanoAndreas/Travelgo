package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelSearch extends AppCompatActivity {

    MaterialButton searchBtn;
    TextView TV_searchHotel, tanggalCheckin, tanggalCheckout;
    private int year = 2019, month = 3, day = 10;

    int HOTEL_SEARCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);

        link.setToolbar(this);

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

        searchBtn = (MaterialButton)findViewById(R.id.search_hotel);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelSearch.this, HotelSearchResult.class).putExtra("tanggal_berangkat", tanggalCheckin.getText()));
            }
        });

        TV_searchHotel = (TextView)findViewById(R.id.TV_searchHotel);
        TV_searchHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HotelSearch.this, SearchFlight.class);
                in.putExtra("type","hotel");
                startActivityForResult(in,HOTEL_SEARCH);
            }
        });


    }

    public void startDate(View v){
        showDialog(999);
    }

    public void endDate(View v){
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");
                    Date date = new Date(year, month, dayOfMonth-1);
                    String dayOfWeek = simpledateformat.format(date);
                    String monthOfYear = simplemonth.format(date);
                    showDate(year, monthOfYear, dayOfMonth, dayOfWeek, "start");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        else if (id == 998) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");
                    Date date = new Date(year, month, dayOfMonth-1);
                    String dayOfWeek = simpledateformat.format(date);
                    String monthOfYear = simplemonth.format(date);
                    showDate(year, monthOfYear, dayOfMonth, dayOfWeek, "end");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        return null;
    }

    private void showDate(int year, String month, int date, String dayOfWeek, String type) {
        if(type == "start")
            tanggalCheckin.setText(new StringBuilder()
                    .append(dayOfWeek)
                    .append(", ")
                    .append(date < 10 ? "0"+date : date)
                    .append(" ")
                    .append(month)
                    .append(" ")
                    .append(year));
        else
            tanggalCheckout.setText(new StringBuilder()
                    .append(dayOfWeek)
                    .append(", ")
                    .append(date < 10 ? "0"+date : date)
                    .append(" ")
                    .append(month)
                    .append(" ")
                    .append(year));
    }

}
