package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
    TextView TV_searchHotel, tanggalCheckin, tanggalCheckout, duration, guest_label, room_label;
    private int year = 2019, month = 3, day = 10;
    Calendar checkin, checkout;
    ConstraintLayout layout;

    int HOTEL_SEARCH = 1, START_DATE = 3, END_DATE = 4, room = 1, guest = 1;
    Date start_date = new Date(),end_date = new Date();

    JSONObject city_data = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);

        set_toolbar();

        guest_label = (TextView) findViewById(R.id.guest_label);
        room_label = (TextView) findViewById(R.id.room_label);
        layout = (ConstraintLayout) findViewById(R.id.layout);

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
        checkout.setTime(end_date);
        checkout.add(Calendar.DATE, 1);
        end_date = checkout.getTime();

        searchBtn = (MaterialButton)findViewById(R.id.search_hotel);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city_data.toString().equals("{}")){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.hotel_search_error_destination_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(guest == 0){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.hotel_search_error_total_guest_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(room == 0){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.hotel_search_error_total_room_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(start_date == end_date){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.hotel_search_error_same_date_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
//                    startActivity(new Intent(HotelSearch.this, FlightSearchJadwal.class)
//                            .putExtra("origin", "hotel")
//                            .putExtra("city", city_data.toString())
//                            .putExtra("check_in", start_date.getTime())
//                            .putExtra("check_out", end_date.getTime())
//                            .putExtra("guest", guest)
//                            .putExtra("room", room)
//                    );
                }
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

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");
        showDate(simpledateformat.format(start_date),"start");
        showDate(simpledateformat.format(end_date),"end");

        int difference= ((int)((end_date.getTime()/(24*60*60*1000)) - (int)(start_date.getTime()/(24*60*60*1000))));
        duration.setText(String.valueOf(difference)+" "+getResources().getString(R.string.hotel_search_night_label));
    }

    public void startDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",true);
        in.putExtra("type","hotel");
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",true);
        in.putExtra("type","hotel");
        startActivityForResult(in,END_DATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        get_token();
    }

    public void get_token(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, C_URL+"flight/token", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    edit_flight_shared_pref.putString("token",response.getString("token"));
                    edit_flight_shared_pref.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == HOTEL_SEARCH) {
                try {
                    JSONObject json = new JSONObject(data.getStringExtra("data"));
                    city_data = json;
                    TV_searchHotel.setText(json.getString("city_label")+", "+json.getString("poi_label"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                    JSONArray json = new JSONArray(data.getStringExtra("date"));

                    start_date = new Date(json.getLong(0));
                    end_date = new Date(json.getLong(json.length() - 1));

                    showDate(simpledateformat.format(start_date), "start");
                    showDate(simpledateformat.format(end_date), "end");

                    int difference= ((int)((end_date.getTime()/(24*60*60*1000)) - (int)(start_date.getTime()/(24*60*60*1000))));
                    duration.setText(difference+" "+getResources().getString(R.string.hotel_search_night_label));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDate(String date, String type) {
        if(type == "start")
            tanggalCheckin.setText(date);
        else if(type == "end")
            tanggalCheckout.setText(date);
        else if(type == "duration")
            duration.setText(date+" "+getResources().getString(R.string.hotel_search_night_label));
    }

}
