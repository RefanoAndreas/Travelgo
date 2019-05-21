package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D3Eticket extends BaseActivity {

    Intent intent;
    String type, url, sales_detail_id;
    TextView TV_title_info, TV_title_guest, TV_title_tips,TV_name, TV_info_departure, TV_info_origin,
             TV_info_duration, TV_info_arrival, TV_info_destination, TV_info_class;
    View flight_train, hotel, tamu_hotel;
    ImageView typeIcon1, typeIcon2;
    String userID;
    SharedPreferences user_id;
    RecyclerView RV_penumpang_pesawat, RV_penumpang_kereta;

    FlightPassengerAdapter adapter_flight_passenger;
    TrainPassengerAdapter adapter_train_passenger;

    ArrayList<JSONObject> flightPassengerList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> trainPassengerList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> hotelGuestList = new ArrayList<JSONObject>();
    ArrayList<String> tips = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager_passengerFlight;
    private RecyclerView.LayoutManager layoutManager_passengerTrain;
    private RecyclerView.LayoutManager layoutManager_guestHotel;

    ListView LV_tips;

    LinearLayout linear_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d3_eticket);

        set_toolbar();

        intent = getIntent();
        type = intent.getStringExtra("type");
        sales_detail_id = intent.getStringExtra("id");

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

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

        tamu_hotel = (View)findViewById(R.id.data_tamu_hotel);

        RV_penumpang_pesawat = (RecyclerView) findViewById(R.id.data_penumpang_pesawat);
        RV_penumpang_pesawat.setHasFixedSize(true);
        layoutManager_passengerFlight= new LinearLayoutManager(this);
        adapter_flight_passenger = new FlightPassengerAdapter(flightPassengerList, this);
        RV_penumpang_pesawat.setLayoutManager(layoutManager_passengerFlight);
        RV_penumpang_pesawat.setAdapter(adapter_flight_passenger);

        RV_penumpang_kereta = (RecyclerView) findViewById(R.id.data_penumpang_kereta);
        RV_penumpang_kereta.setHasFixedSize(true);
        layoutManager_passengerTrain= new LinearLayoutManager(this);
        adapter_train_passenger = new TrainPassengerAdapter(trainPassengerList, this);
        RV_penumpang_kereta.setLayoutManager(layoutManager_passengerTrain);
        RV_penumpang_kereta.setAdapter(adapter_train_passenger);

//        LV_tips = (ListView)findViewById(R.id.LV_tips);

        linear_tips = (LinearLayout)findViewById(R.id.linear_tips);

        eTicket();
    }

    private void eTicket(){
        url = link.C_URL+"eTicket?id="+sales_detail_id+"&type="+type;

        if(type.equals("flight")) {
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            RV_penumpang_pesawat.setVisibility(View.VISIBLE);
            TV_title_info.setText("Informasi Pesawat");
            typeIcon1.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_flight_land_black_24dp);
        }
        else if(type.equals("train")){
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            RV_penumpang_kereta.setVisibility(View.VISIBLE);

            TV_title_info.setText("Informasi Kereta");
            typeIcon1.setImageResource(R.drawable.ic_train_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_train_black_24dp);
            TV_info_class.setVisibility(View.VISIBLE);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonETicket = response.getJSONObject("eTicket");
                    JSONArray jsonTips = response.getJSONArray("tips");

                    if(type.equals("flight")){
                        TV_name.setText(jsonETicket.getJSONObject("carrier").getString("name"));
                        TV_info_departure.setText("Jumat, 17 Mei 2019 . 10:10 - SUB Surabaya");
                        TV_info_departure.setText(jsonETicket.getString("departure_time")+" - "
                                +jsonETicket.getString("departure_airport")+" "+jsonETicket.getJSONObject("departure").getJSONObject("city").getString("name"));
                        TV_info_origin.setText(jsonETicket.getJSONObject("departure").getString("airport_name"));
                        TV_info_duration.setText("2 Jam 20 Menit");
                        TV_info_arrival.setText(jsonETicket.getString("arrival_time")+" - "
                                +jsonETicket.getString("arrival_airport")+" "+jsonETicket.getJSONObject("arrival").getJSONObject("city").getString("name"));
                        TV_info_destination.setText(jsonETicket.getJSONObject("arrival").getString("airport_name"));

                        for(int x=0;x<jsonETicket.getJSONArray("passenger").length();x++){
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonPassenger = jsonETicket.getJSONArray("passenger").getJSONObject(x);

                            jsonObject.put("name", jsonPassenger.getString("title")+" "+jsonPassenger.getString("name"));
                            jsonObject.put("facilities", "Bagasi: "+jsonPassenger.getString("baggage"));


                            flightPassengerList.add(jsonObject);
                            if (x != 0)
                                adapter_flight_passenger.notifyItemInserted(x);
                            else
                                adapter_flight_passenger.notifyDataSetChanged();
                        }
                    }
                    else if(type.equals("train")){
                        TV_name.setText(jsonETicket.getJSONObject("train").getString("name"));

                        TV_info_class.setText(jsonETicket.getString("class"));
                        TV_info_departure.setText(jsonETicket.getString("departure_time")+" - "
                                +jsonETicket.getString("departure_station")+" "+jsonETicket.getJSONObject("departure").getJSONObject("city").getString("name"));
                        TV_info_origin.setText(jsonETicket.getJSONObject("departure").getString("station_name"));
                        TV_info_duration.setText("2 Jam 20 Menit");
                        TV_info_arrival.setText(jsonETicket.getString("arrival_time")+" - "
                                +jsonETicket.getString("arrival_station")+" "+jsonETicket.getJSONObject("arrival").getJSONObject("city").getString("name"));
                        TV_info_destination.setText(jsonETicket.getJSONObject("arrival").getString("station_name"));

                        for(int x=0;x<jsonETicket.getJSONArray("passenger").length();x++){
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonPassenger = jsonETicket.getJSONArray("passenger").getJSONObject(x);

                            jsonObject.put("name", jsonPassenger.getString("title")+" "+jsonPassenger.getString("name"));
                            jsonObject.put("no_id", "KTP - "+jsonPassenger.getString("id_card_no"));
                            jsonObject.put("class_train", jsonETicket.getString("class"));


                            trainPassengerList.add(jsonObject);
                            if (x != 0)
                                adapter_train_passenger.notifyItemInserted(x);
                            else
                                adapter_train_passenger.notifyDataSetChanged();
                        }
                    }

                    for(int x=0;x<jsonTips.length();x++){
                        tips.add("\u2022 Bullet"+jsonTips.getJSONObject(x).getString("tips"));
                        TextView TV_tips = new TextView(D3Eticket.this);
                        TV_tips.setText(tips.get(x));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,0,16);
                        TV_tips.setLayoutParams(params);

                        linear_tips.addView(TV_tips);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                String message="";
                if (error instanceof NetworkError) {
                    message="Network Error";
                }
                else if (error instanceof ServerError) {
                    message="Server Error";
                }
                else if (error instanceof AuthFailureError) {
                    message="Authentication Error";
                }
                else if (error instanceof ParseError) {
                    message="Parse Error";
                }
                else if (error instanceof NoConnectionError) {
                    message="Connection Missing";
                }
                else if (error instanceof TimeoutError) {
                    message="Server Timeout Reached";
                }
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

//        if(type.equals("flight")){
//            flight_train.setVisibility(View.VISIBLE);
//            hotel.setVisibility(View.GONE);
//            penumpang_pesawat.setVisibility(View.VISIBLE);
//
//            TV_title_info.setText("Informasi Pesawat");
//            typeIcon1.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
//            typeIcon2.setImageResource(R.drawable.ic_flight_land_black_24dp);
//            TV_name.setText("Singapore Airlines");
//            TV_info_departure.setText("Jumat, 17 Mei 2019 . 10:10 - SUB Surabaya");
//            TV_info_origin.setText("Bandara Juanda International Airport");
//            TV_info_duration.setText("2 Jam 20 Menit");
//            TV_info_arrival.setText("Jumat, 17 Mei 2019 . 13:30 - SIN Singapore");
//            TV_info_destination.setText("Changi International Airport");
//        }
//        else if(type.equals("train")) {
//
//            TV_name.setText("ARGO PARAHYANGAN");
//
//            TV_info_class.setVisibility(View.VISIBLE);
//            TV_info_class.setText("Kelas Ekonomi (D)");
//
//            TV_info_departure.setText("Jumat, 17 Mei 2019 . 10:10 - BD Bandung");
//            TV_info_origin.setText("Stasiun Bandung");
//            TV_info_duration.setText("2 Jam 20 Menit");
//            TV_info_arrival.setText("Jumat, 17 Mei 2019 . 13:30 - GMR Gambir");
//            TV_info_destination.setText("Stasiun Gambir");
//        }
//        else if(type.equals("hotel")){
//            TV_title_info.setText("Informasi Hotel");
//            TV_title_guest.setText("Data Tamu");
//            TV_title_tips.setText("Tips Penginapan");
//
//            flight_train.setVisibility(View.GONE);
//            hotel.setVisibility(View.VISIBLE);
//            penumpang_kereta.setVisibility(View.VISIBLE);
//        }
    }
}
