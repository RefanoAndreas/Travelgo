package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConfirmationOrder extends AppCompatActivity {

    TextView isiDataPeserta, specialRequestAdd;
    View layout_infoHotel, layout_infoFlight;
    Intent intent;
    String intentString;
    LinearLayout specialRequestLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        link.setToolbar(this);

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        layout_infoHotel = (View) findViewById(R.id.infoHotelGuest);
        layout_infoFlight = (View)findViewById(R.id.infoFlightPassenger);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        if(intentString.equals("hotel")){
            layout_infoHotel.setVisibility(View.VISIBLE);
        }
        else if(intentString.equals("flight")){
            layout_infoFlight.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
        }


        isiDataPeserta = (TextView) findViewById(R.id.isiDataPeserta);
        isiDataPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmationOrder.this, FlightIsiDataPenumpang.class).putExtra("packageName", "flight"));
            }
        });

        specialRequestAdd = (TextView) findViewById(R.id.specialRequestAdd);
        specialRequestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new Special_Order_Hotel();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

    }
}
