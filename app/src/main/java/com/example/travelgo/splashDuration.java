package com.example.travelgo;

import android.app.Application;
import android.os.SystemClock;

public class splashDuration extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
