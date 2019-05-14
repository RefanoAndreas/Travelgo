package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

public class ConfirmationOrder extends BaseActivity {

    TextView  isiDataPeserta, specialRequestAdd, guestData;
    View layout_infoHotel, layout_infoFlight, layout_infoTrain;
    Intent intent;
    String intentString;
    LinearLayout specialRequestLinear, linear_dataPeserta;
    CardView card_dataPemesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        set_toolbar();

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        layout_infoHotel = (View) findViewById(R.id.infoHotelGuest);
        layout_infoFlight = (View)findViewById(R.id.infoFlight);
        layout_infoTrain = (View)findViewById(R.id.infoTrain);
        guestData = (TextView) findViewById(R.id.dataGuestTV);
        linear_dataPeserta = (LinearLayout) findViewById(R.id.linear_dataPeserta);
        isiDataPeserta = (TextView)findViewById(R.id.isiDataPeserta);
        card_dataPemesan = (CardView)findViewById(R.id.data_pemesan_card);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        if(intentString.equals("hotel")){
            layout_infoHotel.setVisibility(View.VISIBLE);
            guestData.setText("Data Tamu");
            isiDataPeserta.setText("Isi data tamu");
            card_dataPemesan.setVisibility(View.GONE);
        }
        else if(intentString.equals("flight")){
            layout_infoFlight.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
            isiDataPeserta.setText("Isi data penumpang");
        }
        else{
            layout_infoTrain.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
            isiDataPeserta.setText("Isi data penumpang");
        }


        linear_dataPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmationOrder.this, DataPenumpang.class).putExtra("packageName", intentString));
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
