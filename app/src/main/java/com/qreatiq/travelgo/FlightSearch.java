package com.qreatiq.travelgo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.shawnlin.numberpicker.NumberPicker;

public class FlightSearch extends AppCompatActivity {

    MaterialButton searchTicketBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        link.setToolbar(this);

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

        searchTicketBtn = (MaterialButton) findViewById(R.id.search_flight);
        searchTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlightSearch.this, FlightSearchJadwal.class));
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                v instanceof TextInputEditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {

                link.hideSoftKeyboard(this);
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
