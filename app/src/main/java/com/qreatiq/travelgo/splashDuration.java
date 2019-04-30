package com.qreatiq.travelgo;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

public class splashDuration extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.onAttach(base, "en"));
    }
}
