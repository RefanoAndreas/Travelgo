package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class D3Eticket extends BaseActivity {

    Intent intent;
    String intentData, url;
    TextView TV_title_info, TV_title_guest, TV_title_tips,TV_name, TV_info_departure, TV_info_origin,
             TV_info_duration, TV_info_arrival, TV_info_destination, TV_info_class;
    View flight_train, hotel, penumpang_pesawat, penumpang_kereta, tamu_hotel;
    ImageView typeIcon1, typeIcon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d3_eticket);

        set_toolbar();

        intent = getIntent();
        intentData = intent.getStringExtra("type");

//        RV_flight_train = (RecyclerView)findViewById(R.id.RV_flight_train);
//        RV_hotel = (RecyclerView)findViewById(R.id.RV_hotel);

        flight_train = (View)findViewById(R.id.eticketFlight);
        hotel = (View)findViewById(R.id.eticketHotel);

        TV_title_info = (TextView)findViewById(R.id.TV_title_info);
        TV_title_guest = (TextView)findViewById(R.id.TV_title_guest);
        TV_title_tips = (TextView)findViewById(R.id.TV_title_tips);

        typeIcon1 = (ImageView)findViewById(R.id.image1);
        typeIcon2 = (ImageView)findViewById(R.id.image2);
        TV_name = (TextView)findViewById(R.id.TV_name);
        TV_info_departure = (TextView)findViewById(R.id.TV_info_departure);
        TV_info_origin = (TextView)findViewById(R.id.TV_info_origin);
        TV_info_duration = (TextView)findViewById(R.id.TV_info_duration);
        TV_info_arrival = (TextView)findViewById(R.id.TV_info_arrival);
        TV_info_destination = (TextView)findViewById(R.id.TV_info_destination);
        TV_info_class = (TextView)findViewById(R.id.TV_info_class);

        penumpang_pesawat = (View)findViewById(R.id.data_penumpang_pesawat);
        penumpang_kereta = (View)findViewById(R.id.data_penumpang_kereta);
        tamu_hotel = (View)findViewById(R.id.data_tamu_hotel);

        eTicket();
    }

    private void eTicket(){


        if(intentData.equals("flight")){
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            penumpang_pesawat.setVisibility(View.VISIBLE);

            TV_title_info.setText("Informasi Pesawat");
            typeIcon1.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_flight_land_black_24dp);
            TV_name.setText("Singapore Airlines");
            TV_info_departure.setText("Jumat, 17 Mei 2019 . 10:10 - SUB Surabaya");
            TV_info_origin.setText("Bandara Juanda International Airport");
            TV_info_duration.setText("2 Jam 20 Menit");
            TV_info_arrival.setText("Jumat, 17 Mei 2019 . 13:30 - SIN Singapore");
            TV_info_destination.setText("Changi International Airport");
        }
        else if(intentData.equals("train")) {
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            penumpang_kereta.setVisibility(View.VISIBLE);

            TV_title_info.setText("Informasi Kereta");
            typeIcon1.setImageResource(R.drawable.ic_train_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_train_black_24dp);
            TV_name.setText("ARGO PARAHYANGAN");

            TV_info_class.setVisibility(View.VISIBLE);
            TV_info_class.setText("Kelas Ekonomi (D)");

            TV_info_departure.setText("Jumat, 17 Mei 2019 . 10:10 - BD Bandung");
            TV_info_origin.setText("Stasiun Bandung");
            TV_info_duration.setText("2 Jam 20 Menit");
            TV_info_arrival.setText("Jumat, 17 Mei 2019 . 13:30 - GMR Gambir");
            TV_info_destination.setText("Stasiun Gambir");
        }
        else if(intentData.equals("hotel")){
            TV_title_info.setText("Informasi Hotel");
            TV_title_guest.setText("Data Tamu");
            TV_title_tips.setText("Tips Penginapan");

            flight_train.setVisibility(View.GONE);
            hotel.setVisibility(View.VISIBLE);
            penumpang_kereta.setVisibility(View.VISIBLE);
        }
    }
}
