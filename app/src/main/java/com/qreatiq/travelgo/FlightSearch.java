package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

public class FlightSearch extends BaseActivity {

    MaterialButton searchTicketBtn;
    Switch flightSwitch;
    LinearLayout tanggalContainer, kembali, flightSearch_icon;
    TextView DepartCitySearch, ArrivalCitySearch, tanggalBerangkat, tanggalKembali;
    CardView chooseDate;
    private int year = 2019, month = 3, day = 10;

    int ARRIVAL_CITY = 1, DEPARTURE_CITY = 2;

    JSONObject depart_data,arrive_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        link.setToolbar(this);

        flightSearch_icon = (LinearLayout) findViewById(R.id.flightSearch_icon);

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

        searchTicketBtn = (MaterialButton) findViewById(R.id.search_flight);
        searchTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlightSearch.this, FlightSearchJadwal.class));
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

        flightSearch_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    showDate(year, month+1, dayOfMonth, "start");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        else if (id == 998) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    showDate(year, month+1, dayOfMonth, "end");
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
                    DepartCitySearch.setText(json.getString("poi_label"));
                }
                else if(requestCode == ARRIVAL_CITY){
                    arrive_data = json;
                    ArrivalCitySearch.setText(json.getString("poi_label"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDate(int year, int month, int day, String type) {
        if(type == "start")
            tanggalBerangkat.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append("/")
                    .append(month < 10 ? "0"+month : month)
                    .append("/")
                    .append(year));
        else
            tanggalKembali.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append("/")
                    .append(month < 10 ? "0"+month : month)
                    .append("/")
                    .append(year));
    }
}
