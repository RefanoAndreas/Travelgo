package com.example.travelgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView listMenu;
    ArrayList<String> menu;
    MainList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMenu =  (RecyclerView) findViewById(R.id.list);

        adapter = new MainList(menu, this);

        RecyclerView.LayoutManager mLayoutmanager  = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        listMenu.setLayoutManager(mLayoutmanager);
        listMenu.setItemAnimator(new DefaultItemAnimator());
        listMenu.setAdapter(adapter)
        listMenu.setNestedScrollingEnabled(false);

//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listMenu);
//        listMenu.setAdapter(listAdapter);
    }


}
