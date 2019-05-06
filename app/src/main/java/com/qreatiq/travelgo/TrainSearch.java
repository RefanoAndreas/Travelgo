package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class TrainSearch extends AppCompatActivity {

    MaterialButton searchTrainBtn;
    LinearLayout flightSearch_icon;
    TextView searchStasiunBerangkat, searchStasiunTujuan, tanggalBerangkat, tanggalKembali, kelas;
    private int year = 2019, month = 3, day = 10;

    int ARRIVAL_CITY = 1, DEPARTURE_CITY = 2;

    JSONObject depart_data = new JSONObject(),arrive_data = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);

        link.setToolbar(this);

        flightSearch_icon = (LinearLayout) findViewById(R.id.flightSearch_icon);
        kelas = (TextView) findViewById(R.id.kelas);

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
                if (isChecked){
//                    tanggalContainer.getWeightSum();
                    tanggalContainer.setWeightSum(2);
                    kembali.setVisibility(View.VISIBLE);
                }else{
//                    tanggalContainer.getWeightSum();
                    tanggalContainer.setWeightSum(1);
                    kembali.setVisibility(View.GONE);
                }
            }
        });

        tanggalBerangkat = (TextView)findViewById(R.id.TV_tanggalBerangkat);
        tanggalKembali = (TextView)findViewById(R.id.TV_tanggalKembali);

        searchTrainBtn = (MaterialButton) findViewById(R.id.search_train);
        searchTrainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainSearch.this, FlightSearchJadwal.class)
                        .putExtra("origin", "train")
                        .putExtra("tanggal_berangkat", tanggalBerangkat.getText()));
            }
        });

        searchStasiunBerangkat = (TextView)findViewById(R.id.searchStasiunBerangkat);
        searchStasiunBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TrainSearch.this, SearchFlight.class);
                in.putExtra("type","train");
                startActivityForResult(in,DEPARTURE_CITY);
            }
        });

        searchStasiunTujuan = (TextView)findViewById(R.id.searchStasiunTujuan);
        searchStasiunTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TrainSearch.this, SearchFlight.class);
                in.putExtra("type","train");
                startActivityForResult(in,ARRIVAL_CITY);
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
                        searchStasiunBerangkat.setText(depart_data.getString("poi_label"));
                        searchStasiunTujuan.setText(arrive_data.getString("poi_label"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            try {
                JSONObject json = new JSONObject(data.getStringExtra("data"));
                if(requestCode == DEPARTURE_CITY){
                    depart_data = json;
                    searchStasiunBerangkat.setText(json.getString("poi_label"));
                }
                else if(requestCode == ARRIVAL_CITY){
                    arrive_data = json;
                    searchStasiunTujuan.setText(json.getString("poi_label"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
