package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences userID;
    String userIDGet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userIDGet = userID.getString("user_id", "Data not found");

        if(!userIDGet.equals("Data not found")){
            startActivity(new Intent(this, BottomNavContainer.class));
            finish();
        }
        else{
            startActivity(new Intent(this, LogSign.class));
            finish();
        }
    }
}
