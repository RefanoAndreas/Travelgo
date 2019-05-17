package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationOrder extends BaseActivity {

    TextView  TV_routeInfo, TV_routeType, specialRequestAdd, guestData, sub_total, pajak, total;
    RecyclerView list_flight, list_hotel, list_train;
    Intent intent;
    String intentString;
    LinearLayout specialRequestLinear,hotel_price;
    RecyclerView list_pax;
    CardView card_dataPemesan;

    MaterialButton submit;

    ConfirmationPaxAdapter adapter;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>(),flight_list_array = new ArrayList<JSONObject>();
    ConfirmationFlightListAdapter flight_list_adapter;

    int ADD_OR_EDIT_PAX = 10, ADD_OR_EDIT_GUEST = 11, selected_arr = 0;
    double sub_total_data = 0, pajak_data, total_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        set_toolbar();

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        list_flight = (RecyclerView) findViewById(R.id.list_flight);
        list_hotel = (RecyclerView)findViewById(R.id.list_hotel);
        list_train = (RecyclerView)findViewById(R.id.list_train);
        guestData = (TextView) findViewById(R.id.dataGuestTV);
        list_pax = (RecyclerView) findViewById(R.id.list_pax);
//        isiDataPeserta = (TextView)findViewById(R.id.isiDataPeserta);
        card_dataPemesan = (CardView)findViewById(R.id.data_pemesan_card);
        submit = (MaterialButton) findViewById(R.id.submit);
        TV_routeInfo = (TextView) findViewById(R.id.TV_routeInfo);
        TV_routeType = (TextView) findViewById(R.id.TV_routeType);
        hotel_price = (LinearLayout) findViewById(R.id.hotel_price);
        sub_total = (TextView) findViewById(R.id.sub_total);
        pajak = (TextView) findViewById(R.id.pajak);
        total = (TextView) findViewById(R.id.total);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        if(intentString.equals("hotel")){
            try {
                arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list_hotel.setVisibility(View.VISIBLE);
            guestData.setText("Data Tamu");
//            isiDataPeserta.setText("Isi data tamu");
            card_dataPemesan.setVisibility(View.GONE);
            hotel_price.setVisibility(View.VISIBLE);
        }
        else if(intentString.equals("flight")){
            try {
                for(int x=0;x<intent.getIntExtra("adult",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Dewasa "+(x+1)+"\",\"type\":\"Dewasa "+(x+1)+"\",\"baggage_depart\":{},\"baggage_return\":{}}"));
                for(int x=0;x<intent.getIntExtra("child",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Anak "+(x+1)+"\",\"type\":\"Anak "+(x+1)+"\",\"baggage_depart\":{},\"baggage_return\":{}}"));
                for(int x=0;x<intent.getIntExtra("infant",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Bayi "+(x+1)+"\",\"type\":\"Bayi "+(x+1)+"\",\"baggage_depart\":{},\"baggage_return\":{}}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list_flight.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");

            try {
                JSONObject depart_data = new JSONObject(intent.getStringExtra("depart_data"));
                JSONObject arrive_data = new JSONObject(intent.getStringExtra("arrive_data"));
                TV_routeInfo.setText(depart_data.getString("city")+" -> "+arrive_data.getString("city"));
                if(intent.getBooleanExtra("isReturn",false))
                    TV_routeType.setText("Pulang Pergi");
                else
                    TV_routeType.setText("Sekali Jalan");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if(intent.getBooleanExtra("isReturn",false)){
                    flight_detail("depart_ticket");
                    flight_detail("return_ticket");
                }
                else
                    flight_detail("depart_ticket");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            flight_list_adapter = new ConfirmationFlightListAdapter(flight_list_array,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            list_flight.setLayoutManager(mLayoutManager);
            list_flight.setAdapter(flight_list_adapter);

            NumberFormat double_formatter = new DecimalFormat("#,###");
            sub_total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data)));
            pajak.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data*0.1)));
            total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data+(sub_total_data*0.1))));

//            isiDataPeserta.setText("Isi data penumpang");
        }
        else{
            list_train.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
//            isiDataPeserta.setText("Isi data penumpang");
        }

        adapter = new ConfirmationPaxAdapter(arrayList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list_pax.setLayoutManager(mLayoutManager);
        list_pax.setAdapter(adapter);

        specialRequestAdd = (TextView) findViewById(R.id.specialRequestAdd);
        specialRequestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new Special_Order_Hotel();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

        adapter.setOnItemClickListener(new ConfirmationPaxAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                selected_arr = position;
                if(intentString.equals("flight"))
                    if(intent.getBooleanExtra("isReturn",false))
                        startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                                .putExtra("packageName",intentString)
                                .putExtra("data",arrayList.get(selected_arr).toString())
                                .putExtra("depart_ticket",intent.getStringExtra("depart_ticket"))
                                .putExtra("return_ticket",intent.getStringExtra("return_ticket"))
                                .putExtra("isReturn",intent.getBooleanExtra("isReturn",false)),
                                ADD_OR_EDIT_PAX
                        );
                    else
                        startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                                        .putExtra("packageName",intentString)
                                        .putExtra("data",arrayList.get(selected_arr).toString())
                                        .putExtra("depart_ticket",intent.getStringExtra("depart_ticket"))
                                        .putExtra("isReturn",intent.getBooleanExtra("isReturn",false)),
                                ADD_OR_EDIT_PAX
                        );
                else if(intentString.equals("hotel"))
                    startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                                    .putExtra("packageName",intentString)
                                    .putExtra("data",arrayList.get(selected_arr).toString()),
                            ADD_OR_EDIT_GUEST
                    );
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intentString.equals("flight")) {
                    try {
                        submit_flight();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void flight_detail(String str) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject ticket = new JSONObject(intent.getStringExtra(str));

        SimpleDateFormat format = new SimpleDateFormat("EEEE d MMM yyyy . k:m");
        String[] depart_str = ticket.getString("departTimeNumber").split(" ");
        String[] arrive_str = ticket.getString("arriveTimeNumber").split(" ");

        Date depart_date = new Date(Integer.parseInt(depart_str[0])-1900,
                Integer.parseInt(depart_str[1]),
                Integer.parseInt(depart_str[2]),
                Integer.parseInt(depart_str[3]),
                Integer.parseInt(depart_str[4]),
                Integer.parseInt(depart_str[5]));
        Date arrive_date = new Date(Integer.parseInt(arrive_str[0])-1900,
                Integer.parseInt(arrive_str[1]),
                Integer.parseInt(arrive_str[2]),
                Integer.parseInt(arrive_str[3]),
                Integer.parseInt(arrive_str[4]),
                Integer.parseInt(arrive_str[5]));

        json.put("airlines",ticket.getString("airlines"));
        json.put("depart_date_place",format.format(depart_date)+" - "+ticket.getJSONObject("departData").getString("code")+" "+ticket.getJSONObject("departData").getString("city_label"));
        json.put("depart_airport",ticket.getJSONObject("departData").getString("poi_label"));
        json.put("duration",ticket.getString("duration"));
        json.put("arrive_date_place",format.format(arrive_date)+" - "+ticket.getJSONObject("arrivalData").getString("code")+" "+ticket.getJSONObject("arrivalData").getString("city_label"));
        json.put("arrive_airport",ticket.getJSONObject("arrivalData").getString("poi_label"));

        flight_list_array.add(json);
        sub_total_data += ticket.getInt("price");
    }

    private void submit_flight() throws JSONException {
        boolean allow = true;
        for(int x=0;x<arrayList.size();x++){
            if(!arrayList.get(x).getBoolean("edit")){
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                Snackbar snackbar = Snackbar.make(layout, "Data "+arrayList.get(x).getString("type")+" belum diisi", Snackbar.LENGTH_LONG);
                snackbar.show();
                allow = false;
            }
        }

        if(allow) {
            JSONObject json = new JSONObject();
            json.put("depart_ticket", getIntent().getStringExtra("depart_ticket"));
            json.put("return_ticket", getIntent().getStringExtra("return_ticket"));
            json.put("pax", new JSONArray(arrayList.toString()));

            String url = C_URL + "flight";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent in = new Intent(ConfirmationOrder.this, BottomNavContainer.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(in);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                    String message = "";
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
                    Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", base_shared_pref.getString("access_token", ""));
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == ADD_OR_EDIT_PAX){

                JSONObject json = arrayList.get(selected_arr);
                try {
                    JSONObject data_from_intent = new JSONObject(data.getStringExtra("data"));
                    json.put("edit",true);
                    json.put("title",data_from_intent.getString("title"));
                    json.put("name",data_from_intent.getString("name"));
                    json.put("baggage_depart",new JSONObject(data_from_intent.getString("baggage_depart")));
                    if(intent.getBooleanExtra("isReturn",false))
                        json.put("baggage_return",new JSONObject(data_from_intent.getString("baggage_return")));
                    adapter.notifyItemChanged(selected_arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == ADD_OR_EDIT_GUEST){

                JSONObject json = arrayList.get(selected_arr);
                try {
                    JSONObject data_from_intent = new JSONObject(data.getStringExtra("data"));
                    json.put("edit",true);
                    json.put("title",data_from_intent.getString("title"));
                    json.put("name",data_from_intent.getString("name"));

                    adapter.notifyItemChanged(selected_arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
