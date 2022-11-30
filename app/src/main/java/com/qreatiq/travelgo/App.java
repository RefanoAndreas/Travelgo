package com.qreatiq.travelgo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class App extends Application {

    LocaleHelperActivityDelegate localeAppDelegate = new LocaleHelperActivityDelegate() {
        @NonNull
        @Override
        public Context getApplicationContext(@NonNull Context context) {
            return null;
        }

        @NonNull
        @Override
        public AppCompatDelegate getAppCompatDelegate(@NonNull AppCompatDelegate appCompatDelegate) {
            return null;
        }

        @Override
        public void setLocale(@NotNull Activity activity, @NotNull Locale locale) {

        }

        @NotNull
        @Override
        public Context attachBaseContext(@NotNull Context context) {
            return null;
        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onResumed(@NotNull Activity activity) {

        }

        @Override
        public void onCreate(@NotNull Activity activity) {

        }
    };
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base));
    }
}
