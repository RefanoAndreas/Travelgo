package com.qreatiq.travelgo;

import android.os.Bundle;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FilterHotel extends BaseActivity {

    TextView minPrice, maxPrice;
//    CrystalRangeSeekbar rangeSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_hotel);

        set_toolbar();

//        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbarPrice);
        minPrice = (TextView)findViewById(R.id.minimumPrice);
        maxPrice = (TextView)findViewById(R.id.maximumPrice);

//        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                NumberFormat formatter = new DecimalFormat("#,###");
//                String formattedNumber = formatter.format(minValue);
//                minPrice.setText("Rp. "+formattedNumber);
//
//                NumberFormat formatter1 = new DecimalFormat("#,###");
//                String formattedNumber1 = formatter1.format(maxValue);
//                maxPrice.setText("Rp. "+formattedNumber1);
//            }
//        });
    }
}
