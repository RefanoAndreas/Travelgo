package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class D3Eticket extends BaseActivity {

    Intent intent;
    String type, url, sales_detail_id;
    TextView TV_title_info, TV_title_guest, TV_title_tips,TV_name, TV_info_departure, TV_info_origin,
             TV_info_duration, TV_info_arrival, TV_info_destination, TV_info_class, TV_guest_name,
             TV_guest_type, TV_hotel_name, TV_hotel_address, TV_checkin_date, TV_checkout_date,
             TV_info_total_guest, TV_tipe_bed, TV_facilitiesHotel, TV_booking_code_title, TV_bookingCode;
    View flight_train, hotel;
    ImageView typeIcon1, typeIcon2;
    String userID;
    SharedPreferences user_id;
    RecyclerView RV_penumpang_pesawat, RV_penumpang_kereta, RV_specialreq_hotel;

    FlightPassengerAdapter adapter_flight_passenger;
    TrainPassengerAdapter adapter_train_passenger;
    HotelGuestRequestAdapter adapter_guest_request;

    ArrayList<JSONObject> flightPassengerList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> trainPassengerList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> guestRequestList = new ArrayList<JSONObject>();
    ArrayList<String> tips = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager_passengerFlight;
    private RecyclerView.LayoutManager layoutManager_passengerTrain;
    private RecyclerView.LayoutManager layoutManager_guestRequest;

    ListView LV_tips;

    LinearLayout linear_tips, layout_guest_hotel;

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

        TV_booking_code_title = (TextView)findViewById(R.id.TV_booking_code_title);
        TV_bookingCode = (TextView)findViewById(R.id.TV_bookingCode);

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

        layout_guest_hotel = (LinearLayout) findViewById(R.id.layout_guest_hotel);
        TV_guest_name = (TextView)findViewById(R.id.TV_guest_name);
//        TV_guest_type = (TextView)findViewById(R.id.TV_guest_type);

        TV_hotel_name = (TextView)findViewById(R.id.TV_hotel_name);
        TV_hotel_address = (TextView)findViewById(R.id.TV_hotel_address);
        TV_checkin_date = (TextView)findViewById(R.id.TV_checkin_date);
        TV_checkout_date = (TextView)findViewById(R.id.TV_checkout_date);
        TV_info_total_guest = (TextView)findViewById(R.id.TV_info_total_guest);
        TV_facilitiesHotel = (TextView)findViewById(R.id.TV_facilitiesHotel);
//        TV_tipe_bed = (TextView)findViewById(R.id.TV_tipe_bed);

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

        RV_specialreq_hotel = (RecyclerView) findViewById(R.id.RV_specialreq_hotel);
        RV_specialreq_hotel.setHasFixedSize(true);
        layoutManager_guestRequest= new LinearLayoutManager(this);
        adapter_guest_request = new HotelGuestRequestAdapter(guestRequestList , this);
        RV_specialreq_hotel.setLayoutManager(layoutManager_guestRequest);
        RV_specialreq_hotel.setAdapter(adapter_guest_request);

//        LV_tips = (ListView)findViewById(R.id.LV_tips);

        linear_tips = (LinearLayout)findViewById(R.id.linear_tips);

        eTicket();
    }

    private void eTicket(){
        url = C_URL+"eTicket?id="+sales_detail_id+"&type="+type;
        Log.d("url",url);

        if(type.equals("flight")) {
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            RV_penumpang_pesawat.setVisibility(View.VISIBLE);
            TV_title_info.setText(getResources().getString(R.string.confirmation_flight_detail_title));
            typeIcon1.setImageResource(R.drawable.ic_flight_takeoff_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_flight_land_black_24dp);
        }
        else if(type.equals("train")){
            flight_train.setVisibility(View.VISIBLE);
            hotel.setVisibility(View.GONE);
            RV_penumpang_kereta.setVisibility(View.VISIBLE);
            TV_booking_code_title.setText(getResources().getString(R.string.e_ticket_booking_code_label));

            TV_title_info.setText(getResources().getString(R.string.confirmation_train_detail_title));
            typeIcon1.setImageResource(R.drawable.ic_train_black_24dp);
            typeIcon2.setImageResource(R.drawable.ic_train_black_24dp);
            TV_info_class.setVisibility(View.VISIBLE);
        }
        else if(type.equals("hotel")){
            TV_title_info.setText(getResources().getString(R.string.confirmation_hotel_detail_title));
            TV_title_guest.setText(getResources().getString(R.string.confirmation_guest_title));
            TV_title_tips.setText(getResources().getString(R.string.e_ticket_stay_advice_label));
            TV_booking_code_title.setText(getResources().getString(R.string.e_ticket_order_number_label));

            flight_train.setVisibility(View.GONE);
            hotel.setVisibility(View.VISIBLE);
            RV_penumpang_kereta.setVisibility(View.VISIBLE);
            layout_guest_hotel.setVisibility(View.VISIBLE);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonETicket = response.getJSONObject("eTicket");
                    JSONArray jsonTips = response.getJSONArray("tips");

                    if(type.equals("flight")){
                        TV_name.setText(jsonETicket.getJSONObject("carrier").getString("name"));
                        TV_info_departure.setText(jsonETicket.getString("departure_time")+" - "
                                +jsonETicket.getString("departure_airport")+" "+jsonETicket.getJSONObject("departure").getJSONObject("city").getString("name"));
                        TV_info_origin.setText(jsonETicket.getJSONObject("departure").getString("name"));
                        TV_info_duration.setText(jsonETicket.getString("duration"));
                        TV_info_arrival.setText(jsonETicket.getString("arrival_time")+" - "
                                +jsonETicket.getString("arrival_airport")+" "+jsonETicket.getJSONObject("arrival").getJSONObject("city").getString("name"));
                        TV_info_destination.setText(jsonETicket.getJSONObject("arrival").getString("name"));
                        TV_bookingCode.setText(jsonETicket.has("bookingCode") && !jsonETicket.isNull("bookingCode") ? jsonETicket.getString("bookingCode") : getResources().getString(R.string.e_ticket_wait_booking_code_label));

                        for(int x=0;x<jsonETicket.getJSONArray("passenger").length();x++){
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonPassenger = jsonETicket.getJSONArray("passenger").getJSONObject(x);

                            jsonObject.put("name", getResources().getStringArray(R.array.titleName)[jsonPassenger.getInt("title")]+" "+jsonPassenger.getString("name"));
                            jsonObject.put("facilities", getResources().getString(R.string.baggage_label)+" : "+jsonPassenger.getString("baggage"));
                            jsonObject.put("type", getResources().getStringArray(R.array.category)[jsonPassenger.getInt("type")]);

                            flightPassengerList.add(jsonObject);
                            if (x != 0)
                                adapter_flight_passenger.notifyItemInserted(x);
                            else
                                adapter_flight_passenger.notifyDataSetChanged();
                        }

                        String[] advice = getResources().getStringArray(R.array.flight_advice);
                        for(int x=0;x<advice.length;x++){
                            tips.add("\u2022"+advice[x]);
                            TextView TV_tips = new TextView(D3Eticket.this);
                            TV_tips.setText(tips.get(x));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,16);
                            TV_tips.setLayoutParams(params);

                            linear_tips.addView(TV_tips);
                        }
                    }
                    else if(type.equals("train")){
                        TV_name.setText(jsonETicket.getJSONObject("train").getString("name"));

                        TV_info_class.setText(jsonETicket.getString("class"));
                        TV_info_departure.setText(jsonETicket.getString("departure_time")+" - "
                                +jsonETicket.getString("departure_station")+" "+jsonETicket.getJSONObject("departure").getJSONObject("city").getString("name"));
                        TV_info_origin.setText(jsonETicket.getJSONObject("departure").getString("name"));
                        TV_info_duration.setText(jsonETicket.getString("duration"));
                        TV_info_arrival.setText(jsonETicket.getString("arrival_time")+" - "
                                +jsonETicket.getString("arrival_station")+" "+jsonETicket.getJSONObject("arrival").getJSONObject("city").getString("name"));
                        TV_info_destination.setText(jsonETicket.getJSONObject("arrival").getString("name"));
                        TV_bookingCode.setText(jsonETicket.has("bookingCode") && !jsonETicket.isNull("bookingCode") ? jsonETicket.getString("bookingCode") : getResources().getString(R.string.e_ticket_wait_booking_code_label));

                        for(int x=0;x<jsonETicket.getJSONArray("passenger").length();x++){
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonPassenger = jsonETicket.getJSONArray("passenger").getJSONObject(x);

                            jsonObject.put("name", getResources().getStringArray(R.array.titleName)[jsonPassenger.getInt("title")]+" "+jsonPassenger.getString("name"));
                            jsonObject.put("no_id", "KTP - "+jsonPassenger.getString("id_card_no"));
                            jsonObject.put("class_train", jsonETicket.getString("class"));
                            jsonObject.put("type", getResources().getStringArray(R.array.category)[jsonPassenger.getInt("type")]);

                            trainPassengerList.add(jsonObject);
                            if (x != 0)
                                adapter_train_passenger.notifyItemInserted(x);
                            else
                                adapter_train_passenger.notifyDataSetChanged();
                        }

                        String[] advice = getResources().getStringArray(R.array.train_advice);
                        for(int x=0;x<advice.length;x++){
                            tips.add("\u2022"+advice[x]);
                            TextView TV_tips = new TextView(D3Eticket.this);
                            TV_tips.setText(tips.get(x));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,16);
                            TV_tips.setLayoutParams(params);

                            linear_tips.addView(TV_tips);
                        }
                    }
                    else if(type.equals("hotel")){
                        TV_guest_name.setText(jsonETicket.getString("name_guest"));

                        TV_hotel_name.setText(jsonETicket.getJSONObject("room").getJSONObject("hotel").getString("name"));
                        TV_hotel_address.setText(jsonETicket.getJSONObject("room").getJSONObject("hotel").getString("address"));
                        TV_checkin_date.setText(jsonETicket.getString("checkin"));
                        TV_checkout_date.setText(jsonETicket.getString("checkout"));
                        TV_info_total_guest.setText(jsonETicket.getString("total_guest")+" "+getResources().getString(R.string.guest_label)+" "+
                                jsonETicket.getString("total_room")+" "+getResources().getString(R.string.room_label));
                        TV_bookingCode.setText(jsonETicket.has("reservationNo") && !jsonETicket.isNull("reservationNo") ? jsonETicket.getString("reservationNo") : getResources().getString(R.string.e_ticket_wait_booking_code_label));

                        if(jsonETicket.getJSONObject("room").getString("breakfast").equals("1")) {
                            TV_facilitiesHotel.setVisibility(View.VISIBLE);
                            TV_facilitiesHotel.setText(getResources().getString(R.string.e_ticket_breakfast_include_label));
                        }
//                        TV_tipe_bed.setText("Tipe Bed: "+ );

                        for(int x=0;x<jsonETicket.getJSONArray("special_request").length();x++){
                            JSONObject jsonRequest = jsonETicket.getJSONArray("special_request").getJSONObject(x);
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("request", jsonRequest.getJSONObject("hotel_request").getString("name"));
                            Log.d("json", jsonObject.toString());

                            guestRequestList.add(jsonObject);
                            if (x != 0)
                                adapter_guest_request.notifyItemInserted(x);
                            else
                                adapter_guest_request.notifyDataSetChanged();
                        }

                        String[] advice = getResources().getStringArray(R.array.hotel_advice);
                        for(int x=0;x<advice.length;x++){
                            tips.add("\u2022"+advice[x]);
                            TextView TV_tips = new TextView(D3Eticket.this);
                            TV_tips.setText(tips.get(x));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,16);
                            TV_tips.setLayoutParams(params);

                            linear_tips.addView(TV_tips);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
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

    }
}
