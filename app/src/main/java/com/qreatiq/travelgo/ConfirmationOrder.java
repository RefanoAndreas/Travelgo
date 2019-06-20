package com.qreatiq.travelgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.DefaultRetryPolicy;
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

    TextView  TV_routeInfo, TV_routeType, specialRequestAdd, guestData, sub_total, pajak, total, titleData,
    TV_namaPemesan, TV_emailPemesan, TV_teleponPemesan;
    RecyclerView flight_depart,flight_return, list_hotel, list_train;
    LinearLayout list_flight;
    View border;
    Intent intent;
    String intentString, userID, url, durasi;
    LinearLayout specialRequestLinear,special_request;
    RecyclerView list_pax;
    CardView card_dataPemesan;
    SharedPreferences user_id;

    MaterialButton submit;

    ConfirmationPaxAdapter adapter;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>(),
            flight_list_depart_array = new ArrayList<JSONObject>(),
            flight_list_return_array = new ArrayList<JSONObject>(),
            train_list_array = new ArrayList<JSONObject>(),hotel_list_array = new ArrayList<JSONObject>(),
            special_request_array = new ArrayList<JSONObject>();
    ConfirmationFlightListAdapter flight_list_depart_adapter,flight_list_return_adapter;

    ConfirmationTrainListAdapter train_list_adapter;
    ConfirmationHotelListAdapter hotel_list_adapter;

    int ADD_OR_EDIT_PAX = 10, ADD_OR_EDIT_GUEST = 11, selected_arr = 0;
    double sub_total_data = 0, sub_total_per_pax_data = 0, pajak_data, total_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        set_toolbar();

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        TV_namaPemesan = (TextView)findViewById(R.id.TV_namaPemesan);
        TV_emailPemesan = (TextView)findViewById(R.id.TV_emailPemesan);
        TV_teleponPemesan = (TextView)findViewById(R.id.TV_teleponPemesan);

        getUser();

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        list_flight = (LinearLayout) findViewById(R.id.list_flight);
        flight_depart = (RecyclerView) findViewById(R.id.flight_depart);
        flight_return = (RecyclerView) findViewById(R.id.flight_return);
        list_hotel = (RecyclerView)findViewById(R.id.list_hotel);
        list_train = (RecyclerView)findViewById(R.id.list_train);
        guestData = (TextView) findViewById(R.id.dataGuestTV);
        list_pax = (RecyclerView) findViewById(R.id.list_pax);
//        isiDataPeserta = (TextView)findViewById(R.id.isiDataPeserta);
        card_dataPemesan = (CardView)findViewById(R.id.data_pemesan_card);
        submit = (MaterialButton) findViewById(R.id.submit);
        TV_routeInfo = (TextView) findViewById(R.id.TV_routeInfo);
        TV_routeType = (TextView) findViewById(R.id.TV_routeType);
        sub_total = (TextView) findViewById(R.id.sub_total);
        pajak = (TextView) findViewById(R.id.pajak);
        total = (TextView) findViewById(R.id.total);
        titleData = (TextView) findViewById(R.id.titleData);
        special_request = (LinearLayout) findViewById(R.id.special_request);
        border = (View) findViewById(R.id.border);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");

        if(intentString.equals("hotel")){
            try {
                arrayList.add(new JSONObject("{" +
                        "\"edit\":false," +
                        "\"title\":\"\"," +
                        "\"name\":\"\"," +
                        "\"label\":\"Isi Data Tamu\"," +
                        "\"type\":\"Tamu\"," +
                        "\"for\":\"hotel\"}"));

                special_request_array.add(new JSONObject("{\"label\":\"Kamar Bebas Rokok\",\"checked\":false}"));
                special_request_array.add(new JSONObject("{\"label\":\"Check-in Terlambat\",\"checked\":false}"));
                special_request_array.add(new JSONObject("{\"label\":\"Check-in lebih cepat\",\"checked\":false}"));
                special_request_array.add(new JSONObject("{\"label\":\"Kasur Extra\",\"checked\":false}"));
                special_request_array.add(new JSONObject("{\"label\":\"Lantai Atas\",\"checked\":false}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list_hotel.setVisibility(View.VISIBLE);
            guestData.setText("Data Tamu");
            TV_routeType.setVisibility(View.GONE);
//            isiDataPeserta.setText("Isi data tamu");
            card_dataPemesan.setVisibility(View.GONE);

            try {
                hotel_detail();
                JSONObject city = new JSONObject(intent.getStringExtra("city"));
                TV_routeInfo.setText(city.getString("city_label"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            hotel_list_adapter = new ConfirmationHotelListAdapter(hotel_list_array,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            list_hotel.setLayoutManager(mLayoutManager);
            list_hotel.setAdapter(hotel_list_adapter);

            try {
                JSONObject hotel = new JSONObject(getIntent().getStringExtra("hotel_selected"));


                NumberFormat double_formatter = new DecimalFormat("#,###");
                sub_total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_per_pax_data))+" x "+
                        String.valueOf(getIntent().getIntExtra("room",0))+" Kamar x "+
                        hotel.getString("duration")+" Hari"+
                        "\nRp. "+String.valueOf(double_formatter.format(sub_total_data)));
                pajak.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data*0.1)));
                total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data+(sub_total_data*0.1))));
                titleData.setText("Rincian Hotel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(intentString.equals("flight")){
            try {
                for(int x=0;x<intent.getIntExtra("adult",0);x++) {
                    JSONObject json = new JSONObject();
                    json.put("edit",false);
                    json.put("title","");
                    json.put("name","");
                    json.put("no_passport","");
                    json.put("label","Isi Data Dewasa "+(x+1));
                    json.put("type","Dewasa "+(x+1));
                    json.put("category","adult");
                    json.put("baggage_depart",new JSONObject());
                    json.put("baggage_return",new JSONObject());
                    json.put("for","flight");
                    arrayList.add(json);
                }
                for(int x=0;x<intent.getIntExtra("child",0);x++) {
                    JSONObject json = new JSONObject();
                    json.put("edit",false);
                    json.put("title","");
                    json.put("name","");
                    json.put("no_passport","");
                    json.put("label","Isi Data Anak "+(x+1));
                    json.put("type","Anak "+(x+1));
                    json.put("category","child");
                    json.put("baggage_depart",new JSONObject());
                    json.put("baggage_return",new JSONObject());
                    json.put("for","flight");
                    arrayList.add(json);
                }
                for(int x=0;x<intent.getIntExtra("infant",0);x++) {
                    JSONObject json = new JSONObject();
                    json.put("edit",false);
                    json.put("title","");
                    json.put("name","");
                    json.put("no_passport","");
                    json.put("label","Isi Data Bayi "+(x+1));
                    json.put("type","Bayi "+(x+1));
                    json.put("category","infant");
                    json.put("baggage_depart",new JSONObject());
                    json.put("baggage_return",new JSONObject());
                    json.put("for","flight");
                    arrayList.add(json);
                }
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
                else {
                    border.setVisibility(View.GONE);
                    flight_return.setVisibility(View.GONE);
                    flight_detail("depart_ticket");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NumberFormat double_formatter = new DecimalFormat("#,###");
            sub_total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_per_pax_data))+" x "+String.valueOf(intent.getIntExtra("adult",0)+intent.getIntExtra("child",0)+intent.getIntExtra("infant",0))+
                    "\nRp. "+String.valueOf(double_formatter.format(sub_total_data)));
            pajak.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data*0.1)));
            total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data+(sub_total_data*0.1))));
            titleData.setText("Rincian Pesawat");
        }
        else if(intentString.equals("train")){
            list_train.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");

            try {
                for(int x=0;x<intent.getIntExtra("adult",0);x++)
                    arrayList.add(new JSONObject("{" +
                            "\"edit\":false," +
                            "\"title\":\"\"," +
                            "\"name\":\"\"," +
                            "\"label\":\"Isi Data Dewasa "+(x+1)+"\"," +
                            "\"type\":\"Dewasa "+(x+1)+"\"," +
                            "\"category\":\"adult\"," +
                            "\"for\":\"train\"}"));
                for(int x=0;x<intent.getIntExtra("child",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Anak "+(x+1)+"\",\"type\":\"Anak "+(x+1)+"\",\"category\":\"child\",\"for\":\"train\"}"));
                for(int x=0;x<intent.getIntExtra("infant",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Bayi "+(x+1)+"\",\"type\":\"Bayi "+(x+1)+"\",\"category\":\"infant\",\"for\":\"train\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject depart_data = new JSONObject(intent.getStringExtra("depart_data"));
                JSONObject arrive_data = new JSONObject(intent.getStringExtra("arrive_data"));
                TV_routeInfo.setText(depart_data.getString("city")+" -> "+arrive_data.getString("city"));
                if(intent.getBooleanExtra("isReturn",false))
                    TV_routeType.setText("Pulang Pergi");
                else
                    TV_routeType.setText("Sekali Jalan");

                if(intent.getBooleanExtra("isReturn",false)){
                    train_detail("depart_ticket");
                    train_detail("return_ticket");
                }
                else
                    train_detail("depart_ticket");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            train_list_adapter = new ConfirmationTrainListAdapter(train_list_array,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            list_train.setLayoutManager(mLayoutManager);
            list_train.setAdapter(train_list_adapter);

            NumberFormat double_formatter = new DecimalFormat("#,###");
            sub_total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_per_pax_data))+" x "+String.valueOf(intent.getIntExtra("adult",0)+intent.getIntExtra("infant",0))+
                    "\nRp. "+String.valueOf(double_formatter.format(sub_total_data)));
            pajak.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data*0.1)));
            total.setText("Rp. "+String.valueOf(double_formatter.format(sub_total_data+(sub_total_data*0.1))));
            titleData.setText("Rincian Kereta");
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
                if(intentString.equals("flight")) {
                    if (intent.getBooleanExtra("isReturn", false))
                        startActivityForResult(new Intent(ConfirmationOrder.this, DataPenumpang.class)
                                        .putExtra("packageName", intentString)
                                        .putExtra("data", arrayList.get(selected_arr).toString())
                                        .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                                        .putExtra("return_ticket", intent.getStringExtra("return_ticket"))
                                        .putExtra("isReturn", intent.getBooleanExtra("isReturn", false)),
                                ADD_OR_EDIT_PAX
                        );
                    else
                        startActivityForResult(new Intent(ConfirmationOrder.this, DataPenumpang.class)
                                        .putExtra("packageName", intentString)
                                        .putExtra("data", arrayList.get(selected_arr).toString())
                                        .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                                        .putExtra("isReturn", intent.getBooleanExtra("isReturn", false)),
                                ADD_OR_EDIT_PAX
                        );
                }
                else if(intentString.equals("train")) {
                    if (intent.getBooleanExtra("isReturn", false))
                        startActivityForResult(new Intent(ConfirmationOrder.this, DataPenumpang.class)
                                        .putExtra("packageName", intentString)
                                        .putExtra("data", arrayList.get(selected_arr).toString())
                                        .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                                        .putExtra("return_ticket", intent.getStringExtra("return_ticket"))
                                        .putExtra("isReturn", intent.getBooleanExtra("isReturn", false)),
                                ADD_OR_EDIT_PAX
                        );
                    else
                        startActivityForResult(new Intent(ConfirmationOrder.this, DataPenumpang.class)
                                        .putExtra("packageName", intentString)
                                        .putExtra("data", arrayList.get(selected_arr).toString())
                                        .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                                        .putExtra("isReturn", intent.getBooleanExtra("isReturn", false)),
                                ADD_OR_EDIT_PAX
                        );
                }
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
                try {
                    if(intentString.equals("flight")) {
                        submit_flight();
                    }
                    else if(intentString.equals("train")) {
                        submit_train();
                    }
                    else if(intentString.equals("hotel")) {
                        submit_hotel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hotel_detail() throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject hotel_selected = new JSONObject(intent.getStringExtra("hotel_selected"));
        JSONObject room_selected = new JSONObject(intent.getStringExtra("room_selected"));

        SimpleDateFormat format = new SimpleDateFormat("EEEE d MMM yyyy");
        String[] check_in = hotel_selected.getString("check_in").split(" ");
        String[] check_out = hotel_selected.getString("check_out").split(" ");

        Date check_in_date = new Date(Integer.parseInt(check_in[0])-1900,
                Integer.parseInt(check_in[1])-1,
                Integer.parseInt(check_in[2]));
        Date check_out_date = new Date(Integer.parseInt(check_out[0])-1900,
                Integer.parseInt(check_out[1])-1,
                Integer.parseInt(check_out[2]));

        json.put("name",hotel_selected.getString("name"));
        json.put("room_name",room_selected.getString("name"));
        json.put("check_in",format.format(check_in_date));
        json.put("check_out",format.format(check_out_date));
        json.put("total_guest",intent.getIntExtra("guest",0)+" Tamu");

        hotel_list_array.add(json);

        JSONObject hotel = new JSONObject(getIntent().getStringExtra("hotel_selected"));
        sub_total_per_pax_data += room_selected.getInt("price");
        sub_total_data += room_selected.getInt("price")*getIntent().getIntExtra("room",0)*hotel.getInt("duration");
    }

    private void train_detail(String str) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject ticket = new JSONObject(intent.getStringExtra(str));

        SimpleDateFormat format = new SimpleDateFormat("EEEE d MMM yyyy . kk:mm");
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

        json.put("name",ticket.getString("name"));
        json.put("class",ticket.getString("class")+" ("+ticket.getString("sub-class")+")");
        json.put("depart_date_place",format.format(depart_date)+" - "+ticket.getJSONObject("departData").getString("code")+" "+ticket.getJSONObject("departData").getString("city_label"));
        json.put("depart_airport",ticket.getJSONObject("departData").getString("poi_label"));
        json.put("duration",ticket.getString("duration"));
        json.put("arrive_date_place",format.format(arrive_date)+" - "+ticket.getJSONObject("arrivalData").getString("code")+" "+ticket.getJSONObject("arrivalData").getString("city_label"));
        json.put("arrive_airport",ticket.getJSONObject("arrivalData").getString("poi_label"));

        durasi = ticket.getString("duration");

        train_list_array.add(json);
        sub_total_per_pax_data += ticket.getInt("price");
        sub_total_data += ticket.getInt("price")*(intent.getIntExtra("adult",0)+intent.getIntExtra("child",0)+intent.getIntExtra("infant",0));
    }

    private void flight_detail(String str) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject ticket = new JSONObject(intent.getStringExtra(str));

        for(int x=0;x<ticket.getJSONArray("segment").length();x++) {
            json = new JSONObject();
            if(x == 0 && str.equals("depart_ticket"))
                json.put("title", "Tiket berangkat menggunakan "+ticket.getString("airlines"));
            if(x == 0 && str.equals("return_ticket"))
                json.put("title", "Tiket kembali menggunakan "+ticket.getString("airlines"));
            JSONObject jsonObject = ticket.getJSONArray("segment").getJSONObject(x).getJSONArray("flightDetail").getJSONObject(0);
//            Log.d("data",jsonObject.toString());
            SimpleDateFormat format = new SimpleDateFormat("EEEE d MMM yyyy . kk:mm");
            String[] depart_str = jsonObject.getString("time_depart").split(" ");
            String[] arrive_str = jsonObject.getString("time_arrive").split(" ");

            Date depart_date = new Date(Integer.parseInt(depart_str[0]) - 1900,
                    Integer.parseInt(depart_str[1]) - 1,
                    Integer.parseInt(depart_str[2]),
                    Integer.parseInt(depart_str[3]),
                    Integer.parseInt(depart_str[4]),
                    Integer.parseInt(depart_str[5]));
            Date arrive_date = new Date(Integer.parseInt(arrive_str[0]) - 1900,
                    Integer.parseInt(arrive_str[1]) - 1,
                    Integer.parseInt(arrive_str[2]),
                    Integer.parseInt(arrive_str[3]),
                    Integer.parseInt(arrive_str[4]),
                    Integer.parseInt(arrive_str[5]));

            json.put("airlines", jsonObject.getString("airlineCode")+jsonObject.getString("flightNumber"));
            json.put("depart_date_place", format.format(depart_date) + " - " + jsonObject.getString("fdOrigin") + " " + jsonObject.getJSONObject("origin_airport").getJSONObject("city").getString("name"));
            json.put("depart_airport", jsonObject.getJSONObject("origin_airport").getString("name"));
            json.put("duration", jsonObject.getString("duration"));
            json.put("arrive_date_place", format.format(arrive_date) + " - " + jsonObject.getString("fdDestination") + " " + jsonObject.getJSONObject("destination_airport").getJSONObject("city").getString("name"));
            json.put("arrive_airport", jsonObject.getJSONObject("destination_airport").getString("name"));
            if(jsonObject.has("transit"))
                json.put("transit", "Transit "+jsonObject.getString("transit"));

            if(str.equals("depart_ticket"))
                flight_list_depart_array.add(json);
            else
                flight_list_return_array.add(json);
        }
        sub_total_per_pax_data += ticket.getInt("price");
        sub_total_data += ticket.getInt("price")*(intent.getIntExtra("adult",0)+intent.getIntExtra("child",0)+intent.getIntExtra("infant",0));

        if(str.equals("depart_ticket")){
            flight_list_depart_adapter = new ConfirmationFlightListAdapter(flight_list_depart_array,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            flight_depart.setLayoutManager(mLayoutManager);
            flight_depart.setAdapter(flight_list_depart_adapter);
        }
        else{
            flight_list_return_adapter = new ConfirmationFlightListAdapter(flight_list_return_array,this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            flight_return.setLayoutManager(mLayoutManager);
            flight_return.setAdapter(flight_list_return_adapter);
        }

        if(str.equals("depart_ticket"))
            get_baggage("depart");
        else
            get_baggage("return");
    }

    private void get_baggage(final String type) throws JSONException {
        String url;
        JSONObject ticket;
        if(type.equals("depart")) {
            ticket = new JSONObject(intent.getStringExtra("depart_ticket"));
            url = C_URL + "flight/baggage?origin=" + ticket.getJSONObject("departData").getString("code") + "&destination=" + ticket.getJSONObject("arrivalData").getString("code");
        }
        else {
            ticket = new JSONObject(intent.getStringExtra("return_ticket"));
            url = C_URL + "flight/baggage?origin=" + ticket.getJSONObject("departData").getString("code") + "&destination=" + ticket.getJSONObject("arrivalData").getString("code");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _array = response.getJSONArray("data");

                    for(int x=0;x<arrayList.size();x++) {
                        for(int y=0;y<_array.length();y++){
                            if(y==0)
                                _array.getJSONObject(y).put("checked",true);
                            else
                                _array.getJSONObject(y).put("checked",false);
                        }
                        if(type.equals("depart")) {
                            arrayList.get(x).put("arr_baggage_depart", _array);
                        }
                        else {
                            arrayList.get(x).put("arr_baggage_return", _array);
                        }
                        adapter.notifyItemChanged(x);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        });
        requestQueue.add(jsonObjectRequest);
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
            final ProgressDialog loading = new ProgressDialog(this);
            loading.setMax(100);
            loading.setTitle("Booking is in progress");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);
            loading.setProgress(0);
            loading.show();

            final JSONObject json = new JSONObject();
            json.put("depart_ticket", new JSONObject(getIntent().getStringExtra("depart_ticket")));
            if(intent.getBooleanExtra("isReturn",false))
                json.put("return_ticket", new JSONObject(getIntent().getStringExtra("return_ticket")));
            json.put("is_return", getIntent().getBooleanExtra("isReturn",false));
            json.put("class", getIntent().getStringExtra("kelas"));
            json.put("pax", new JSONArray(arrayList.toString()));
            json.put("adult", getIntent().getIntExtra("adult", 0));
            json.put("child", getIntent().getIntExtra("child", 0));
            json.put("infant", getIntent().getIntExtra("infant", 0));

            Log.d("data",json.toString());
            Log.d("auth",base_shared_pref.getString("access_token", ""));

            String url = C_URL + "flight";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        loading.dismiss();
                        Intent in = new Intent(ConfirmationOrder.this, Payment.class);
//                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("type","flight");
                        in.putExtra("id",response.getString("id"));
                        in.putExtra("data",json.toString());
    //                    finish();
                        startActivity(in);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                    error_exception(error,layout);
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void submit_train() throws JSONException {
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
            final ProgressDialog loading = new ProgressDialog(this);
            loading.setMax(100);
            loading.setTitle("Booking is in progress");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);
            loading.setProgress(0);
            loading.show();

            final JSONObject json = new JSONObject();
            json.put("depart_ticket", new JSONObject(getIntent().getStringExtra("depart_ticket")));
            if(intent.getBooleanExtra("isReturn",false))
                json.put("return_ticket", new JSONObject(getIntent().getStringExtra("return_ticket")));
            json.put("is_return", getIntent().getBooleanExtra("isReturn",false));
            json.put("class", getIntent().getStringExtra("kelas"));
            json.put("pax", new JSONArray(arrayList.toString()));
            json.put("adult", getIntent().getIntExtra("adult", 0));
            json.put("child", getIntent().getIntExtra("child", 0));
            json.put("infant", getIntent().getIntExtra("infant", 0));
            json.put("duration", durasi);

            Log.d("data",json.toString());
            Log.d("auth",base_shared_pref.getString("access_token", ""));

            String url = C_URL + "train";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        loading.dismiss();
                        Intent in = new Intent(ConfirmationOrder.this, Payment.class);
//                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("type","train");
                        in.putExtra("id",response.getString("id"));
                        in.putExtra("data",json.toString());
    //                    finish();
                        startActivity(in);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                    error_exception(error,layout);
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void submit_hotel() throws JSONException {
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
            final ProgressDialog loading = new ProgressDialog(this);
            loading.setMax(100);
            loading.setTitle("Booking is in progress");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);
            loading.setProgress(0);
            loading.show();

            final JSONObject json = new JSONObject();
            json.put("hotel_selected", new JSONObject(getIntent().getStringExtra("hotel_selected")));
            json.put("room_selected", new JSONObject(getIntent().getStringExtra("room_selected")));
            json.put("total_guest", getIntent().getIntExtra("guest",0));
            json.put("total_room", getIntent().getIntExtra("room",0));
            json.put("special_request", new JSONArray(special_request_array.toString()));
            json.put("guest_info", new JSONArray(arrayList.toString()));

            Log.d("data",json.toString());
            Log.d("auth",base_shared_pref.getString("access_token", ""));

            String url = C_URL + "hotel";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        loading.dismiss();
                        Intent in = new Intent(ConfirmationOrder.this, Payment.class);
//                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("type","hotel");
                        in.putExtra("id",response.getString("id"));
                        in.putExtra("data",json.toString());
//                        finish();
                        startActivity(in);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                    error_exception(error,layout);
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

                    if(intentString.equals("flight")) {
                        json.put("no_passport",data_from_intent.getString("no_passport"));
                        json.put("baggage_depart", new JSONObject(data_from_intent.getString("baggage_depart")));

                        for (int y = 0; y < json.getJSONArray("arr_baggage_depart").length(); y++) {
                            if ((!json.getJSONObject("baggage_depart").toString().equals("{}") && json.getJSONObject("baggage_depart").getString("code").equals(json.getJSONArray("arr_baggage_depart").getJSONObject(y).getString("code"))) ||
                                    (json.getJSONObject("baggage_depart").toString().equals("{}") && y == 0))
                                json.getJSONArray("arr_baggage_depart").getJSONObject(y).put("checked", true);
                            else
                                json.getJSONArray("arr_baggage_depart").getJSONObject(y).put("checked", false);
                        }

                        if (intent.getBooleanExtra("isReturn", false)) {
                            json.put("baggage_return", new JSONObject(data_from_intent.getString("baggage_return")));

                            for (int y = 0; y < json.getJSONArray("arr_baggage_return").length(); y++) {
                                if ((!json.getJSONObject("baggage_return").toString().equals("{}") && json.getJSONObject("baggage_return").getString("code").equals(json.getJSONArray("arr_baggage_return").getJSONObject(y).getString("code"))) ||
                                        (json.getJSONObject("baggage_return").toString().equals("{}") && y == 0))
                                    json.getJSONArray("arr_baggage_return").getJSONObject(y).put("checked", true);
                                else
                                    json.getJSONArray("arr_baggage_return").getJSONObject(y).put("checked", false);
                            }
                        }
                    }
                    else if(intentString.equals("train")) {
                        json.put("no_id",data_from_intent.getString("no_id"));
                        json.put("phone",data_from_intent.getString("no_id"));
                    }
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
                    json.put("email",data_from_intent.getString("email"));
                    json.put("phone",data_from_intent.getString("phone"));
                    adapter.notifyItemChanged(selected_arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getUser(){
        url = C_URL+"profile";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("user")){
                        TV_namaPemesan.setText(!response.getJSONObject("user").isNull("name") ? response.getJSONObject("user").getString("name") : "");
                        TV_emailPemesan.setText(!response.getJSONObject("user").isNull("email") ? response.getJSONObject("user").getString("email") : "");
                        if(!response.getJSONObject("user").isNull("phone_number"))
                            TV_teleponPemesan.setText(response.getJSONObject("user").getString("phone_number"));


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
