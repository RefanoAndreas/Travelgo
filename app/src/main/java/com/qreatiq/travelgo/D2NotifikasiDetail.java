package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

public class D2NotifikasiDetail extends BaseActivity {

    LinearLayout eTicketLayout, eTicketLayout1;
    Intent i;
    String intentString;
    TextView TV_eTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2_notifikasi_detail);

        set_toolbar();

        eTicketLayout = (LinearLayout)findViewById(R.id.eTicketLayout);
        eTicketLayout1 = (LinearLayout)findViewById(R.id.eTicketLayout1);
        TV_eTicket = (TextView)findViewById(R.id.TV_eTicket);

        i = getIntent();
        intentString = i.getStringExtra("routeType");

        if(i.getStringExtra("type").equals("train") || i.getStringExtra("type").equals("flight")){
            if(intentString.equals("Sekali Jalan")){
                eTicketLayout1.setVisibility(View.GONE);
            }
        }
        else{
            TV_eTicket.setText("Hotel ABCDE, Labuan Bajo");
            eTicketLayout1.setVisibility(View.GONE);
        }


    }
}
