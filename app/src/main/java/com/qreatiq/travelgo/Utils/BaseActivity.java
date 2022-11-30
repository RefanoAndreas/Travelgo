package com.qreatiq.travelgo.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.qreatiq.travelgo.LocaleManager;
import com.qreatiq.travelgo.R;
import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static android.content.pm.PackageManager.GET_META_DATA;

public class BaseActivity extends AppCompatActivity {

    public int PICK_FROM_CAMERA = 1000, PICK_FROM_GALLERY = 1001;
    public BottomSheetDialog bottomSheetDialog;
    public Toolbar toolbar;

    public SharedPreferences base_shared_pref,flight_shared_pref;
    public SharedPreferences.Editor edit_base_shared_pref,edit_flight_shared_pref;
    public String android_id;

    public RequestQueue requestQueue;
    public String C_URL = "http://travelgolaravel.quantumtri.com/api/";
    public String C_URL_IMAGES = "http://travelgolaravel.quantumtri.com/api/images/";

//    public String C_URL = "http://travelgolaravel.propertigo.id/api/";
//    public String C_URL_IMAGES = "http://travelgolaravel.propertigo.id/api/images/";

    public boolean production = true;
    public String version = "3.7";

    public Uri camera_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        base_shared_pref = getApplicationContext().getSharedPreferences("user_id",MODE_PRIVATE);
        edit_base_shared_pref = base_shared_pref.edit();

        flight_shared_pref = getApplicationContext().getSharedPreferences("flight",MODE_PRIVATE);
        edit_flight_shared_pref = flight_shared_pref.edit();
//        edit_base_shared_pref.remove("flight.last_search").commit();
//        LocaleManager.setLocale(this,"in");


        if (Build.VERSION.SDK_INT >= 24) {
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale locale = new Locale(base_shared_pref.getString("lang", "en"));
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        else{
            Configuration config = getResources().getConfiguration();
            Locale locale = new Locale(base_shared_pref.getString("lang", "en"));
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

//        context = LocaleManager.setLocale(this,base_shared_pref.getString("lang","en_US"));
        resetTitle();

//        attachBaseContext(context);

//        changeLang(base_shared_pref.getString("lang","id"));

        requestQueue = Volley.newRequestQueue(this);
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

//        Log.d("FCM", FirebaseInstanceId.getInstance().getToken());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notification", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("travelgo")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if(!task.isSuccessful()){
                            msg = "Failed";
                        }
                        Log.d("message", msg);
                    }
                });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        20);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void set_toolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void resetTitle() {
        try {
            int label = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    public String capitalizeString(String myString){
        return myString.substring(0,1).toUpperCase() + myString.substring(1);
    }

    public String BitMapToString(Bitmap bitmap,int percentage){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,percentage, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public byte[] BitMapToByte(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return b;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap ByteToBitmap(byte[] encodeByte){
        try {
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
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

    protected float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    protected float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    protected int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    protected void call_media_picker(){
        View view = LayoutInflater.from(this).inflate(R.layout.media_picker_fragment, null);
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    private void capture_photo() {
        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(m_intent, PICK_FROM_CAMERA);
    }

    public void camera(View v){
        capture_photo();
    }

    public void gallery(View v){
        Intent in =new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in,PICK_FROM_GALLERY);
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("data", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("data", str); // continuation
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void error_exception(VolleyError error,View layout) {
        String message="";
        if(!production) {
            if (error instanceof NetworkError) {
                message = "Network Error";
            } else if (error instanceof ServerError) {
                message = "Server Error";
            } else if (error instanceof AuthFailureError) {
                message = "Authentication Error";
            } else if (error instanceof ParseError) {
                message = "Parse Error";
            } else if (error instanceof NoConnectionError) {
                message = "Connection Missing";
            } else if (error instanceof TimeoutError) {
                message = "Server Timeout Reached";
            }
        }
        else{
            message = "Server timeout";
        }
        Snackbar snackbar= Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
