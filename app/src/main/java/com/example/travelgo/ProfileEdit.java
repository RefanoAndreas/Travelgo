package com.example.travelgo;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ProfileEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextInputLayout) findViewById(R.id.TIL_name_profileEdit)).setHint(null);
        ((TextInputEditText) findViewById(R.id.TIET_name_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_email_profileEdit)).setHint(null);
        ((TextInputEditText) findViewById(R.id.TIET_email_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_phone_profileEdit)).setHint(null);
        ((TextInputEditText) findViewById(R.id.TIET_phone_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_password_profileEdit)).setHint(null);
        ((TextInputEditText) findViewById(R.id.TIET_password_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_rePassword_profileEdit)).setHint(null);
        ((TextInputEditText) findViewById(R.id.TIET_rePassword_profileEdit)).setHint(null);
    }

}
