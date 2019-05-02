package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

public class D2NotifikasiDetail extends AppCompatActivity {

    LinearLayout eTicketLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2_notifikasi_detail);

        link.setToolbar(this);

        eTicketLayout = (LinearLayout)findViewById(R.id.eTicketLayout);



    }
}
