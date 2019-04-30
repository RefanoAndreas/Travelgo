package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class HotelSearch extends AppCompatActivity {

    MaterialButton searchBtn;

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

        searchBtn = (MaterialButton)findViewById(R.id.search_hotel);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelSearch.this, HotelSearchResult.class));
            }
        });
    }

}
