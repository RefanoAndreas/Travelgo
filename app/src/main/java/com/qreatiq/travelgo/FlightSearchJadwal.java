package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlightSearchJadwal extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> ticketList = new ArrayList<>();
    TextView tripInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search_jadwal);

        link.setToolbar(this);

        Intent i = getIntent();
        String intentString = i.getStringExtra("origin");
        String tanggalBerangkat = i.getStringExtra("tanggal_berangkat");

        tripInfo = (TextView)findViewById(R.id.tripInfo);
        tripInfo.setText(tanggalBerangkat+", 1 Pax, Ekonomi");

        if(intentString.equals("flight")){
            flightData();
        }
        else{
            trainData();
        }

        mRecyclerView = findViewById(R.id.RV_chooseDate);
        mRecyclerView = findViewById(R.id.RV_ticket_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TicketAdapter(ticketList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new TicketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(FlightSearchJadwal.this, ConfirmationOrder.class).putExtra("origin", "flight"));
            }
        });

    }

    public void viewFilter(View v){
        startActivity(new Intent(FlightSearchJadwal.this, Filter.class));
    }

    public void viewChangeDate(View v){
        startActivity(new Intent(FlightSearchJadwal.this, ChangeDateActivity.class));
    }

    public void flightData(){
        try {
            ticketList.add(new JSONObject("{\"airlines\":\"Singapore Airlines\", " +
                    "\"departTime\":\"07:40\", " +
                    "\"duration\":\"2j 20m\", " +
                    "\"arrivalTime\":\"11:00\", " +
                    "\"departAirport\":\"SUB\", " +
                    "\"totalTransit\":\"0 Transit\", " +
                    "\"arrivalAirport\":\"SIN\", " +
                    "\"price\":\"2.500.000\"}"));
            ticketList.add(new JSONObject("{\"airlines\":\"Garuda Indonesia\", " +
                    "\"departTime\":\"07:40\", " +
                    "\"duration\":\"2j 20m\", " +
                    "\"arrivalTime\":\"11:00\", " +
                    "\"departAirport\":\"SUB\", " +
                    "\"totalTransit\":\"0 Transit\", " +
                    "\"arrivalAirport\":\"SIN\", " +
                    "\"price\":\"2.500.000\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void trainData(){
        try {
            ticketList.add(new JSONObject("{\"airlines\":\"Mutiara\", " +
                    "\"departTime\":\"07:40\", " +
                    "\"duration\":\"2j 20m\", " +
                    "\"arrivalTime\":\"11:00\", " +
                    "\"departAirport\":\"SUB\", " +
                    "\"totalTransit\":\"0 Transit\", " +
                    "\"arrivalAirport\":\"JKT\", " +
                    "\"price\":\"150.000\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
