package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences userID;
    String userIDGet;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, BottomNavContainer.class));
            }
        }, 1500);

//        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
//        userIDGet = userID.getString("access_token", "Data not found");
//        startActivity(new Intent(this, BottomNavContainer.class));
//        finish();

//        if(!userIDGet.equals("Data not found")){
//        }
//        else{
//            startActivity(new Intent(this, LogSign.class));
//            finish();
//        }
    }
}
