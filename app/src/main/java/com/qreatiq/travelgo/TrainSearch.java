package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrainSearch extends BaseActivity {

    MaterialButton searchTrainBtn;
    LinearLayout flightSearch_icon;
    TextView searchStasiunBerangkat, searchStasiunTujuan, tanggalBerangkat, tanggalKembali, kelas, adult_label, infant_label;
    ConstraintLayout layout;

    private int year = 2019, month = 3, day = 10;

    int ARRIVAL_CITY = 1, DEPARTURE_CITY = 2, START_DATE = 3, END_DATE = 4, adult = 1, infant = 0;

    JSONObject depart_data = new JSONObject(),arrive_data = new JSONObject();
    Date start_date = new Date(),end_date = new Date();
    String kelas_data = "Ekonomi";
    boolean isReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);

        set_toolbar();

        flightSearch_icon = (LinearLayout) findViewById(R.id.flightSearch_icon);
        kelas = (TextView) findViewById(R.id.kelas);
        adult_label = (TextView) findViewById(R.id.adult_label);
        infant_label = (TextView) findViewById(R.id.infant_label);
        layout = (ConstraintLayout) findViewById(R.id.layout);

        final CardView jumlahpenumpang = findViewById(R.id.TrainSearch_jumlahPenumpang);
        jumlahpenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainSearchJumlahPenumpang jumlahPenumpangSheet = new TrainSearchJumlahPenumpang();
                jumlahPenumpangSheet.show(getSupportFragmentManager(), "Pilih Jumlah Penumpang");
                jumlahPenumpangSheet.setStyle(jumlahPenumpangSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        final CardView kelas = findViewById(R.id.TrainSearch_kelas);
        kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainSearchKelas kelasPesawatSheet = new TrainSearchKelas();
                kelasPesawatSheet.show(getSupportFragmentManager(), "Pilih Kelas Pesawat");
                kelasPesawatSheet.setStyle(kelasPesawatSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        Switch aSwitch = findViewById(R.id.TrainSearch_switch);
        final LinearLayout kembali = findViewById(R.id.TrainSearch_tanggalKembali);
        final LinearLayout tanggalContainer = findViewById(R.id.TrainSearch_tanggalContainer);
//        tanggalContainer.getWeightSum();
        tanggalContainer.setWeightSum(1);
        kembali.setVisibility(View.GONE);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        flightSearch_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!depart_data.toString().equals("") && !arrive_data.toString().equals("")) {
                    JSONObject json = new JSONObject();
                    json = depart_data;
                    depart_data = arrive_data;
                    arrive_data = json;

                    try {
                        searchStasiunBerangkat.setText(depart_data.getString("city_label")+" ("+depart_data.getString("code")+")");
                        searchStasiunTujuan.setText(arrive_data.getString("city_label")+" ("+arrive_data.getString("code")+")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        searchTrainBtn = (MaterialButton) findViewById(R.id.search_train);
        searchTrainBtn.setOnClickListener(new View.OnClickListener() {
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
                else if(adult == 0 && infant == 0){
                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.flight_search_error_passenger_label),Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    startActivity(new Intent(TrainSearch.this, FlightSearchJadwal.class)
                            .putExtra("origin", "train")
                            .putExtra("depart_data", depart_data.toString())
                            .putExtra("arrive_data", arrive_data.toString())
                            .putExtra("tanggal_berangkat", start_date.getTime())
                            .putExtra("tanggal_kembali", end_date.getTime())
                            .putExtra("adult", adult)
                            .putExtra("infant", infant)
                            .putExtra("kelas", kelas_data)
                            .putExtra("isReturn", isReturn)
                    );
                }
            }
        });

        searchStasiunBerangkat = (TextView)findViewById(R.id.searchStasiunBerangkat);
        searchStasiunBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TrainSearch.this, SearchFlight.class);
                in.putExtra("type","train");
                in.putExtra("data",arrive_data.toString());
                startActivityForResult(in,DEPARTURE_CITY);
            }
        });

        searchStasiunTujuan = (TextView)findViewById(R.id.searchStasiunTujuan);
        searchStasiunTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TrainSearch.this, SearchFlight.class);
                in.putExtra("type","train");
                in.putExtra("data",depart_data.toString());
                startActivityForResult(in,ARRIVAL_CITY);
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE, d MMM yyyy");
        showDate(simpledateformat.format(start_date),"start");

    }

    public void startDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        if(isReturn)
            in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",isReturn);
        in.putExtra("type","train");
        startActivityForResult(in,START_DATE);
    }

    public void endDate(View v){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Intent in = new Intent(this,DatePickerActivity.class);
        in.putExtra("start_date",start_date.getTime());
        if(isReturn)
            in.putExtra("end_date",end_date.getTime());
        in.putExtra("isReturn",isReturn);
        in.putExtra("type","train");
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
                        searchStasiunBerangkat.setText(json.getString("city_label")+" ("+json.getString("code")+")");
                    } else if (requestCode == ARRIVAL_CITY) {
                        arrive_data = json;
                        searchStasiunTujuan.setText(json.getString("city_label")+" ("+json.getString("code")+")");
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
