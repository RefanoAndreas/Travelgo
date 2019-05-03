package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FilterTour extends AppCompatActivity {

    TextView minPrice, maxPrice, seeLocation, seeDuration;
    CrystalRangeSeekbar rangeSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour);

        link.setToolbar(this);

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(minValue);
                minPrice.setText("Rp. "+formattedNumber);

                NumberFormat formatter1 = new DecimalFormat("#,###");
                String formattedNumber1 = formatter1.format(maxValue);
                maxPrice.setText("Rp. "+formattedNumber1);
            }
        });

        seeLocation = (TextView) findViewById(R.id.see_locationBtn);
        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterTour.this, FilterTourLocation.class));
            }
        });

        seeDuration = (TextView) findViewById(R.id.see_durationBtn);
        seeDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterTourDuration filterTourDuration = new FilterTourDuration();
                filterTourDuration.show(getSupportFragmentManager(), "Pilih Jangka Waktu");
                filterTourDuration.setStyle(filterTourDuration.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

    }
}
