package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlightSearchJadwal extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> ticketList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search_jadwal);

        link.setToolbar(this);

        mRecyclerView = findViewById(R.id.RV_chooseDate);
        try {
            ticketList.add(new JSONObject("{\"airlines\":\"Singapore Airlines\", " +
                    "\"departTime\":\"07.40\", " +
                    "\"duration\":\"2j 20m\", " +
                    "\"arrivalTime\":\"11.00\", " +
                    "\"departAirport\":\"SUB\", " +
                    "\"totalTransit\":\"0 Transit\", " +
                    "\"arrivalAirport\":\"SIN\", " +
                    "\"price\":\"2.500.000\"}"));
            ticketList.add(new JSONObject("{\"airlines\":\"Garuda Indonesia\", " +
                    "\"departTime\":\"07.40\", " +
                    "\"duration\":\"1j 05m\", " +
                    "\"arrivalTime\":\"08.05\", " +
                    "\"departAirport\":\"SUB\", " +
                    "\"totalTransit\":\"0 Transit\", " +
                    "\"arrivalAirport\":\"CGK\", " +
                    "\"price\":\"950.000\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.RV_ticket_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new TicketAdapter(ticketList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void viewFilter(View v){
        startActivity(new Intent(FlightSearchJadwal.this, Filter.class));
    }

    public void viewChangeDate(View v){
        startActivity(new Intent(FlightSearchJadwal.this, ChangeDateActivity.class));
    }
}
