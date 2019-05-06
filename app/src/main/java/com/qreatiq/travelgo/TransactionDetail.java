package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransactionDetail extends AppCompatActivity {

    TextView isiDataPeserta;
    Intent intent;
    String intentString;
    MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        link.setToolbar(this);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        saveButton = (MaterialButton)findViewById(R.id.submit_transaction);

        isiDataPeserta = (TextView) findViewById(R.id.isiDataPeserta);
        isiDataPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionDetail.this, FlightIsiDataPenumpang.class)
                        .putExtra("packageName", "tour"));
            }
        });

        if (intentString.equals("history")){
            isiDataPeserta.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }


    }
}
