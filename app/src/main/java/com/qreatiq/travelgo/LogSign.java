package com.qreatiq.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogSign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sign);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);
    }
}
