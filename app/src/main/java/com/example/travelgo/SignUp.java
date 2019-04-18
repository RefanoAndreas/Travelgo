package com.example.travelgo;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);

        ((TextInputLayout) findViewById(R.id.email_layout)).setHint(null);
        ((TextInputEditText) findViewById(R.id.email_input)).setHint("Email");
        ((TextInputLayout) findViewById(R.id.password_layout)).setHint(null);
        ((TextInputEditText) findViewById(R.id.password_input)).setHint("Password");
        ((TextInputLayout) findViewById(R.id.retype_password_layout)).setHint(null);
        ((TextInputEditText) findViewById(R.id.retype_password_input)).setHint("Re-Type Password");
    }
}
