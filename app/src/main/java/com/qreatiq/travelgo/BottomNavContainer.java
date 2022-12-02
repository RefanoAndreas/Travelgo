package com.qreatiq.travelgo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class BottomNavContainer extends BaseActivity {

    String id_loc, userID, tokenDevice;
    SharedPreferences user_id, deviceToken;
    FragmentTour fragmentTour;
    FragmentHome fragmentHome;
    Fragment selectedFragment = null;
    BottomNavigationView bottomNav;
    int LOGIN = 1;

    ConstraintLayout layout;
    JSONArray home_list = new JSONArray(),tour_list = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_container);

        deviceToken = getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenDevice = deviceToken.getString("token", "Data not found");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                    },
                    1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    1);

        bottomNav = findViewById(R.id.nav_bottom);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        layout = (ConstraintLayout) findViewById(R.id.layout);

        set_toolbar();
        toolbar.setVisibility(View.GONE);

        Intent i = getIntent();
        fragmentTour = new FragmentTour();
        fragmentHome = new FragmentHome();

        if(i.hasExtra("loc_id")){
            id_loc = i.getStringExtra("loc_id");
            fragmentTour.loc_id = id_loc;
            bottomNav.setSelectedItemId(R.id.nav_tour);
        }
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentHome).commit();

        if(i.hasExtra("message")){
            Snackbar snackbar = Snackbar.make(layout,i.getStringExtra("message"),Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");
        if(selectedFragment instanceof FragmentHome){
            fragmentHome.getLocation();
        }
        else if(selectedFragment instanceof FragmentTour){
//            fragmentTour.tourFilterBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    changeLang(base_shared_pref.getString("lang","en"));
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = fragmentHome;
//                            Toolbar toolbar  = findViewById(R.id.toolbar);
//                            setSupportActionBar(toolbar);
//                            getSupportActionBar().setTitle("Test");
//                            toolbar.setVisibility(View.VISIBLE);
                            break;
                        case R.id.nav_tour:
                            selectedFragment = fragmentTour;
                            fragmentTour.filter = new JSONObject();

                            break;
                        case R.id.nav_notification:
                            if(!base_shared_pref.getString("access_token", "Data not found").equals("Data not found")) {
                                startActivity(new Intent(BottomNavContainer.this, D1Notifikasi.class).putExtra("data", "all"));
                            }
                            else{
                                startActivityForResult(new Intent(BottomNavContainer.this, LogIn.class), LOGIN);
                            }
                            return false;
                        case R.id.nav_profile:
                            if(!base_shared_pref.getString("access_token", "Data not found").equals("Data not found")) {
                                selectedFragment = new FragmentProfile();
                                break;
                            }
                            else{
                                startActivityForResult(new Intent(BottomNavContainer.this, LogIn.class), LOGIN);
                                return false;
                            }
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    public void entertainment(View v){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.home_development_title))
                .setMessage(getResources().getString(R.string.home_development_label))
                .show();
    }

    public void wifi(View v){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.home_development_title))
                .setMessage(getResources().getString(R.string.home_development_label))
                 .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == LOGIN) {
            }
        }
    }
}
