package com.qreatiq.travelgo;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ConfirmationOrder extends AppCompatActivity {

    LinearLayout guestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        link.setToolbar(this);

        guestData = (LinearLayout) findViewById(R.id.guestData);
        guestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new Special_Order_Hotel();
                newFragment.show(getSupportFragmentManager(), "missiles");

            }
        });
    }
}
