package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightSearch extends BaseActivity {

    MaterialButton searchTicketBtn;
    Switch flightSwitch;
    LinearLayout tanggalContainer, kembali, flightSearch_icon;
    TextView DepartCitySearch, ArrivalCitySearch, tanggalBerangkat, tanggalKembali, kelas;
    CardView chooseDate;
    private int year = 2019, month = 3, day = 10;

    int ARRIVAL_CITY = 1, DEPARTURE_CITY = 2, START_DATE = 3, END_DATE = 4;

    JSONObject depart_data = new JSONObject(),arrive_data = new JSONObject();

    Date start_date = new Date(),end_date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        set_toolbar();

        flightSearch_icon = (LinearLayout) findViewById(R.id.flightSearch_icon);
        kelas = (TextView) findViewById(R.id.kelas);

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
                Intent in = new Intent(FlightSearch.this, SearchFlight.class);
                in.putExtra("type","flight");
                startActivityForResult(in,DEPARTURE_CITY);
            }
        });

        ArrivalCitySearch = (TextView)findViewById(R.id.ArrivalCitySearch);
        ArrivalCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FlightSearch.this, SearchFlight.class);
                in.putExtra("type","flight");
                startActivityForResult(in,ARRIVAL_CITY);
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        searchTicketBtn = (MaterialButton) findViewById(R.id.search_flight);
        searchTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlightSearch.this, FlightSearchJadwal.class)
                        .putExtra("origin", "flight")
                        .putExtra("tanggal_berangkat", tanggalBerangkat.getText()));
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
                if (isChecked){
                    tanggalContainer.setWeightSum(2);
                    kembali.setVisibility(View.VISIBLE);
                }else{
                    tanggalContainer.setWeightSum(1);
                    kembali.setVisibility(View.GONE);
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
                        DepartCitySearch.setText(depart_data.getString("poi_label"));
                        ArrivalCitySearch.setText(arrive_data.getString("poi_label"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == DEPARTURE_CITY || requestCode == ARRIVAL_CITY) {
                try {
                    JSONObject json = new JSONObject(data.getStringExtra("data"));
                    if (requestCode == DEPARTURE_CITY) {
                        depart_data = json;
                        DepartCitySearch.setText(json.getString("poi_label"));
                    } else if (requestCode == ARRIVAL_CITY) {
                        arrive_data = json;
                        ArrivalCitySearch.setText(json.getString("poi_label"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");

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

                    showDate(start_date.getYear(),start_monthOfYear,start_date.getDate(),start_dayOfWeek,"start");
                    showDate(end_date.getYear(),end_monthOfYear,end_date.getDate(),end_dayOfWeek,"end");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDate(int year, String month, int date, String dayOfWeek, String type) {
        if(type == "start")
            tanggalBerangkat.setText(new StringBuilder()
                    .append(dayOfWeek)
                    .append(", ")
                    .append(date < 10 ? "0"+date : date)
                    .append(" ")
                    .append(month)
                    .append(" ")
                    .append(year));
        else
            tanggalKembali.setText(new StringBuilder()
                    .append(dayOfWeek)
                    .append(", ")
                    .append(date < 10 ? "0"+date : date)
                    .append(" ")
                    .append(month)
                    .append(" ")
                    .append(year));
    }
}
