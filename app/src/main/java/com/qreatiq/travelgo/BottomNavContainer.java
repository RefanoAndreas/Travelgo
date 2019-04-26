package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BottomNavContainer extends AppCompatActivity {

    SharedPreferences userID;
    String id_loc;
    FragmentTour fragmentTour;
    Fragment selectedFragment = null;
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_container);

        bottomNav = findViewById(R.id.nav_bottom);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        Intent i = getIntent();
        fragmentTour = new FragmentTour();

        if(i.hasExtra("loc_id")){
            id_loc = i.getStringExtra("loc_id");
            fragmentTour.loc_id = id_loc;
            bottomNav.setSelectedItemId(R.id.nav_tour);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        }
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new FragmentHome();
                            Toolbar toolbar  = findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle("Test");
                            toolbar.setVisibility(View.VISIBLE);
                            break;
                        case R.id.nav_tour:
                            selectedFragment = fragmentTour;
                            break;
                        case R.id.nav_notification:
                            selectedFragment = new FragmentNotification();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new FragmentProfile();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    public void entertainment(View v){
        new AlertDialog.Builder(this)
                .setTitle("Notice")
                .setMessage("Fitur ini dalam tahap pengembangan")
                .show();
    }

    public void wifi(View v){
        new AlertDialog.Builder(this)
                .setTitle("Notice")
                .setMessage("Fitur ini dalam tahap pengembangan")
                .show();
    }
}
