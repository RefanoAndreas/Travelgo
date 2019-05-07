package com.qreatiq.travelgo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.qreatiq.travelgo.R;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public int PICK_FROM_CAMERA = 3, PICK_FROM_GALLERY = 2;
    public BottomSheetDialog bottomSheetDialog;

    public SharedPreferences base_shared_pref;
    public SharedPreferences.Editor edit_base_shared_pref;

    public RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        base_shared_pref = getApplicationContext().getSharedPreferences("user_id",MODE_PRIVATE);
        edit_base_shared_pref = base_shared_pref.edit();
        changeLang(base_shared_pref.getString("lang","en"));

        requestQueue = Volley.newRequestQueue(this);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                (v instanceof EditText ||
                v instanceof TextInputEditText) &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {

                hideSoftKeyboard(this);
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void call_media_picker(){
        View view = LayoutInflater.from(this).inflate(R.layout.media_picker_fragment, null);
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    public void camera(View v){
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),PICK_FROM_CAMERA);
    }

    public void gallery(View v){
        Intent in =new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in,PICK_FROM_GALLERY);
    }
}
