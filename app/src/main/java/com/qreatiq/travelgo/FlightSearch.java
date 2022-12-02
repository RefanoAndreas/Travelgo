package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FlightSearch extends BaseActivity {

    MaterialButton searchTicketBtn;
    Switch flightSwitch;
    LinearLayout tanggalContainer, kembali, flightSearch_icon;
    TextView DepartCitySearch, ArrivalCitySearch, tanggalBerangkat, tanggalKembali, kelas, adult_label, child_label, infant_label;
    CardView chooseDate;
    Calendar calendar = Calendar.getInstance();
    private int year = 2019, month = 3, day = 10;

    int ARRIVAL_CITY = 1, DEPARTURE_CITY = 2, START_DATE = 3, END_DATE = 4, adult = 1, child = 0, infant = 0;
    boolean isReturn = false;

    JSONObject depart_data = new JSONObject(),arrive_data = new JSONObject();
    String kelas_data = "Ekonomi";

    ConstraintLayout layout;

    Date start_date = new Date(),end_date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        set_toolbar();

        flightSearch_icon = (LinearLayout) findViewById(R.id.flightSearch_icon);
        kelas = (TextView) findViewById(R.id.kelas);
        adult_label = (TextView) findViewById(R.id.adult_label);
        child_label = (TextView) findViewById(R.id.child_label);
        infant_label = (TextView) findViewById(R.id.infant_label);
        layout = (ConstraintLayout) findViewById(R.id.layout);

        final CardView jumlahpenumpang = findViewById(R.id.flightSearch_jumlahPenumpang);
        jumlahpenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightSearchJumlahPenumpang jumlahPenumpangSheet = new FlightSearchJumlahPenumpang();
                jumlahPenumpangSheet.show(getSupportFragmentManager(), "Pilih Jumlah Penumpang");
                jumlahPenumpangSheet.setStyle(jumlahPenumpangSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        final CardView kelaspesawat = findViewById(R.id.flightSearch_kelaspesawat);
        kelaspesawat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightSearchKelasPesawat kelasPesawatSheet = new FlightSearchKelasPesawat();
                kelasPesawatSheet.show(getSupportFragmentManager(), "Pilih Kelas Pesawat");
                kelasPesawatSheet.setStyle(kelasPesawatSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        DepartCitySearch = (TextView)findViewById(R.id.DepartCitySearch);
        DepartCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(FlightSearch.this, SearchFlight.class);
//                in.putExtra("type","flight");
//                in.putExtra("data",arrive_data.toString());
//                startActivityForResult(in,DEPARTURE_CITY);
            }
        });

        ArrivalCitySearch = (TextView)findViewById(R.id.ArrivalCitySearch);
        ArrivalCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FlightSearch.this, SearchFlight.class);
                in.putExtra("type","flight");
                in.putExtra("data",depart_data.toString());
                startActivityForResult(in,ARRIVAL_CITY);
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        searchTicketBtn = (MaterialButton) findViewById(R.id.search_flight);
        searchTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(depart_data.toString().equals("{}")){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.flight_search_error_departure_city_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(arrive_data.toString().equals("{}")){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.flight_search_error_arrival_city_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(adult == 0 && child == 0 && infant == 0){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.flight_search_error_passenger_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    startActivity(new Intent(FlightSearch.this, FlightSearchJadwal.class)
                            .putExtra("origin", "flight")
                            .putExtra("depart_data", depart_data.toString())
                            .putExtra("arrive_data", arrive_data.toString())
                            .putExtra("tanggal_berangkat", start_date.getTime())
                            .putExtra("tanggal_kembali", end_date.getTime())
                            .putExtra("adult", adult)
                            .putExtra("child", child)
                            .putExtra("infant", infant)
                            .putExtra("kelas", kelas_data)
                            .putExtra("isReturn", isReturn)
                    );
                }
            }
        });

        kembali = findViewById(R.id.FlightSearch_tanggalKembali);
        tanggalContainer = findViewById(R.id.FlightSearch_tanggalContainer);

        tanggalContainer.setWeightSum(1);
        kembali.setVisibility(View.GONE);
        flightSwitch = (Switch)findViewById(R.id.switch_flightSearch);
        flightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isReturn = isChecked;
                start_date = new Date();
                end_date = new Date();

                if (isChecked){
                    tanggalContainer.setWeightSum(2);
                    kembali.setVisibility(View.VISIBLE);
                }else{
                    tanggalContainer.setWeightSum(1);
                    kembali.setVisibility(View.GONE);
                }

                if(isReturn) {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                    showDate(simpledateformat.format(start_date),"start");
                    showDate(simpledateformat.format(end_date),"end");
                }
                else{
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                    showDate(simpledateformat.format(start_date),"start");
                }
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        flightSearch_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!depart_data.toString().equals("") && !arrive_data.toString().equals("")) {
                    JSONObject json = new JSONObject();
                    json = depart_data;
                    depart_data = arrive_data;
                    arrive_data = json;

                    try {
                        DepartCitySearch.setText(depart_data.getString("city_label")+" ("+depart_data.getString("code")+")");
                        ArrivalCitySearch.setText(arrive_data.getString("city_label")+" ("+arrive_data.getString("code")+")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");
        showDate(simpledateformat.format(start_date),"start");

//        if(flight_shared_pref.getString("token","").equals(""))

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
                    Log.d("token",response.getString("token"));
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

    public void startDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        if(isReturn)
            in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",isReturn);
        in.putExtra("type","flight");
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        if(isReturn)
            in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",isReturn);
        in.putExtra("type","flight");
        startActivityForResult(in,END_DATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == DEPARTURE_CITY || requestCode == ARRIVAL_CITY) {
                try {
                    JSONObject json = new JSONObject(data.getStringExtra("data"));
                    if (requestCode == DEPARTURE_CITY) {
                        depart_data = json;
                        DepartCitySearch.setText(json.getString("city_label")+" ("+json.getString("code")+")");
                    } else if (requestCode == ARRIVAL_CITY) {
                        arrive_data = json;
                        ArrivalCitySearch.setText(json.getString("city_label")+" ("+json.getString("code")+")");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    if(isReturn) {
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                        JSONArray json = new JSONArray(data.getStringExtra("date"));

                        start_date = new Date(json.getLong(0));
                        end_date = new Date(json.getLong(json.length() - 1));

                        Log.d("start_date",simpledateformat.format(start_date));
                        Log.d("end_date",simpledateformat.format(end_date));

                        showDate(simpledateformat.format(start_date), "start");
                        showDate(simpledateformat.format(end_date), "end");
                    }
                    else{
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");

                        JSONArray json = new JSONArray(data.getStringExtra("date"));

                        start_date = new Date(json.getLong(0));

                        showDate(simpledateformat.format(start_date), "start");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDate(String date, String type) {
        if(type == "start")
            tanggalBerangkat.setText(date);
        else
            tanggalKembali.setText(date);
    }
}
