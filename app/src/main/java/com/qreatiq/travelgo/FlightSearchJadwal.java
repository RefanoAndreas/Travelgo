package com.qreatiq.travelgo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FlightSearchJadwal extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JSONObject> ticketList = new ArrayList<>();
    HotelListAdapter hotel_adapter;

    TextView tripInfo,title,no_data;
    LinearLayout flightSearchJadwal_menubar;
    MaterialButton dateBtn;
    String intentString,url_captcha,captcha_input = "",kelas="";
    Intent intent;
    int SORT = 10, FILTER = 20, adult_pax, child_pax, infant_pax, ROUTE = 100, selected, CAPTCHA = 30;
    int[][] states;
    int[] colors;

    Date date, check_in_date, check_out_date;
    RecyclerViewSkeletonScreen skeleton;

    JSONObject origin,destination,hotel_city,sort = new JSONObject(),filter = new JSONObject();
    MaterialButton filterBtn,sort_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search_jadwal);

        set_toolbar();

        filterBtn = (MaterialButton) findViewById(R.id.filterBtn);
        sort_button = (MaterialButton) findViewById(R.id.sort_button);
        no_data = (TextView) findViewById(R.id.no_data);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");
        if(intent.getStringExtra("origin").equals("train") || intent.getStringExtra("origin").equals("flight")) {

            Log.d("kelas", intent.getStringExtra("kelas"));

            if (!intent.getBooleanExtra("isOpposite", false))
                date = new Date(intent.getLongExtra("tanggal_berangkat", 0));
            else
                date = new Date(intent.getLongExtra("tanggal_kembali", 0));
            adult_pax = intent.getIntExtra("adult", 0);
            child_pax = intent.getIntExtra("child", 0);
            infant_pax = intent.getIntExtra("infant", 0);

            try {
                origin = new JSONObject(intent.getStringExtra("depart_data"));
                destination = new JSONObject(intent.getStringExtra("arrive_data"));
                kelas = intent.getStringExtra("kelas");

                if(intent.getStringExtra("origin").equals("train")){

                    filter.put("min_price",0);
                    filter.put("max_price",300000000);
                    filter.put("arrival_date",new JSONArray());
                    filter.put("departure_date",new JSONArray());
                    ArrayList<JSONObject> jsonArray = new ArrayList<JSONObject>();
                    jsonArray.add(new JSONObject("{\"label\":\""+kelas+"\"}"));
                    filter.put("class",new JSONArray(jsonArray.toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            tripInfo = (TextView) findViewById(R.id.tripInfo);
            title = (TextView) findViewById(R.id.title);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            tripInfo.setText(format.format(date) + ", " +
                    adult_pax + " "+getResources().getString(R.string.adult_label)+", " +
                    child_pax + " "+getResources().getString(R.string.child_label)+", " +
                    infant_pax + " "+getResources().getString(R.string.infant_label)+", " +
                    kelas);

            try {
                title.setText(origin.getString("code") + " > " + destination.getString("code"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                check_in_date = new Date(intent.getLongExtra("check_in", 0));
                check_out_date = new Date(intent.getLongExtra("check_out", 0));
                hotel_city = new JSONObject(intent.getStringExtra("city"));
                tripInfo = (TextView) findViewById(R.id.tripInfo);
                title = (TextView) findViewById(R.id.title);

                title.setText(hotel_city.getString("city_label")+", "+hotel_city.getString("poi_label"));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                tripInfo.setText(format.format(check_in_date) + " - "+format.format(check_out_date)+", " +
                        intent.getIntExtra("guest", 0) + " "+getResources().getString(R.string.guest_label)+", " +
                        intent.getIntExtra("room", 0) + " "+getResources().getString(R.string.room_label));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dateBtn = (MaterialButton)findViewById(R.id.dateBtn);
        flightSearchJadwal_menubar = (LinearLayout)findViewById(R.id.flightSearchJadwal_menubar);

        flightSearchJadwal_menubar.setWeightSum(2);
        dateBtn.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.RV_chooseDate);
        mRecyclerView = findViewById(R.id.RV_ticket_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(intent.getStringExtra("origin").equals("train") || intent.getStringExtra("origin").equals("flight")) {
            mAdapter = new TicketAdapter(ticketList, this);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListner(new TicketAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                    selected = position;
                    route(position);
                }
            });
        }
        else{
            hotel_adapter = new HotelListAdapter(ticketList,this);
            mRecyclerView.setAdapter(hotel_adapter);

            hotel_adapter.setOnItemClickListner(new HotelListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                    startActivity(new Intent(FlightSearchJadwal.this, HotelDetail.class)
                            .putExtra("origin","hotel")
                            .putExtra("city", getIntent().getStringExtra("city"))
                            .putExtra("guest", getIntent().getIntExtra("guest",0))
                            .putExtra("room", getIntent().getIntExtra("room",0))
                            .putExtra("hotel_selected",ticketList.get(position).toString())
                    );
                }
            });
        }

        try {
            no_data.setVisibility(View.GONE);
            if(intent.getStringExtra("origin").equals("flight")){

                flightData();
            }
            else if(intent.getStringExtra("origin").equals("train")){
                trainData();
            }
            else if(intent.getStringExtra("origin").equals("hotel")){

                hotelData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void viewFilter(View v){
        startActivityForResult(new Intent(FlightSearchJadwal.this, Filter.class)
                .putExtra("type", intentString)
                .putExtra("filter", filter.toString())
                , FILTER);
    }

    public void viewChangeDate(View v){
        startActivity(new Intent(FlightSearchJadwal.this, ChangeDateActivity.class));
    }

    private void route(int position){
        if(intentString.equals("flight")) {
            if(intent.getBooleanExtra("isReturn",false)) {
                if (!intent.getBooleanExtra("isOpposite", false)) {
                    startActivity(new Intent(FlightSearchJadwal.this, FlightSearchJadwal.class)
                            .putExtra("origin", intentString)
                            .putExtra("depart_data", destination.toString())
                            .putExtra("arrive_data", origin.toString())
                            .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                            .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                            .putExtra("adult", intent.getIntExtra("adult", 0))
                            .putExtra("child", intent.getIntExtra("child", 0))
                            .putExtra("infant", intent.getIntExtra("infant", 0))
                            .putExtra("kelas", intent.getStringExtra("kelas"))
                            .putExtra("isReturn", true)
                            .putExtra("isOpposite", true)
                            .putExtra("depart_ticket",ticketList.get(position).toString())
                    );
                } else {
                    startActivity(new Intent(FlightSearchJadwal.this, ConfirmationOrder.class)
                            .putExtra("origin", intentString)
                            .putExtra("depart_data", destination.toString())
                            .putExtra("arrive_data", origin.toString())
                            .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                            .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                            .putExtra("adult", intent.getIntExtra("adult", 0))
                            .putExtra("child", intent.getIntExtra("child", 0))
                            .putExtra("infant", intent.getIntExtra("infant", 0))
                            .putExtra("kelas", intent.getStringExtra("kelas"))
                            .putExtra("isReturn", true)
                            .putExtra("isOpposite", true)
                            .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                            .putExtra("return_ticket", ticketList.get(position).toString())
                    );
                }
            }
            else{
                startActivity(new Intent(FlightSearchJadwal.this, ConfirmationOrder.class)
                        .putExtra("origin", intentString)
                        .putExtra("depart_data", origin.toString())
                        .putExtra("arrive_data", destination.toString())
                        .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                        .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                        .putExtra("adult", intent.getIntExtra("adult", 0))
                        .putExtra("child", intent.getIntExtra("child", 0))
                        .putExtra("infant", intent.getIntExtra("infant", 0))
                        .putExtra("kelas", intent.getStringExtra("kelas"))
                        .putExtra("isReturn", false)
                        .putExtra("depart_ticket", ticketList.get(position).toString())
                );
            }
        }
        else if(intentString.equals("train")) {
            if(intent.getBooleanExtra("isReturn",false)) {
                if (!intent.getBooleanExtra("isOpposite", false)) {
                    startActivity(new Intent(FlightSearchJadwal.this, FlightSearchJadwal.class)
                            .putExtra("origin", intentString)
                            .putExtra("depart_data", destination.toString())
                            .putExtra("arrive_data", origin.toString())
                            .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                            .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                            .putExtra("adult", intent.getIntExtra("adult", 0))
                            .putExtra("child", intent.getIntExtra("child", 0))
                            .putExtra("infant", intent.getIntExtra("infant", 0))
                            .putExtra("kelas", intent.getStringExtra("kelas"))
                            .putExtra("isReturn", true)
                            .putExtra("isOpposite", true)
                            .putExtra("depart_ticket",ticketList.get(position).toString())
                    );
                } else {
                    startActivity(new Intent(FlightSearchJadwal.this, ConfirmationOrder.class)
                            .putExtra("origin", intentString)
                            .putExtra("depart_data", destination.toString())
                            .putExtra("arrive_data", origin.toString())
                            .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                            .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                            .putExtra("adult", intent.getIntExtra("adult", 0))
                            .putExtra("child", intent.getIntExtra("child", 0))
                            .putExtra("infant", intent.getIntExtra("infant", 0))
                            .putExtra("kelas", intent.getStringExtra("kelas"))
                            .putExtra("isReturn", true)
                            .putExtra("isOpposite", true)
                            .putExtra("depart_ticket", intent.getStringExtra("depart_ticket"))
                            .putExtra("return_ticket", ticketList.get(position).toString())
                    );
                }
            }
            else{
                startActivity(new Intent(FlightSearchJadwal.this, ConfirmationOrder.class)
                        .putExtra("origin", intentString)
                        .putExtra("depart_data", origin.toString())
                        .putExtra("arrive_data", destination.toString())
                        .putExtra("tanggal_berangkat", intent.getLongExtra("tanggal_berangkat", 0))
                        .putExtra("tanggal_kembali", intent.getLongExtra("tanggal_kembali", 0))
                        .putExtra("adult", intent.getIntExtra("adult", 0))
                        .putExtra("child", intent.getIntExtra("child", 0))
                        .putExtra("infant", intent.getIntExtra("infant", 0))
                        .putExtra("kelas", intent.getStringExtra("kelas"))
                        .putExtra("isReturn", false)
                        .putExtra("depart_ticket", ticketList.get(position).toString())
                );
            }
        }
    }

    public void flightData() throws JSONException, UnsupportedEncodingException {
        skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_jadwal_flight_train_item).show();
        String url;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        if(intent.getBooleanExtra("isReturn",false)) {
            Date depart_date = new Date(intent.getLongExtra("tanggal_berangkat", 0));
            Date return_date = new Date(intent.getLongExtra("tanggal_kembali", 0));

            url = C_URL+"flight/search" +
                    "?origin="+origin.getString("code")+
                    "&destination="+destination.getString("code")+
                    "&time="+format.format(date)+
                    "&depart_time="+format.format(depart_date)+
                    "&return_time="+format.format(return_date)+
                    "&return=true"+
                    "&is_opposite="+intent.getBooleanExtra("isOpposite", false)+
                    "&adult="+adult_pax+
                    "&child="+child_pax+
                    "&infant="+infant_pax+
                    "&airline_access_code="+captcha_input+
                    "&token="+flight_shared_pref.getString("token","")+
                    "&android_id="+android_id;
        }
        else{
            Date depart_date = new Date(intent.getLongExtra("tanggal_berangkat", 0));

            url = C_URL+"flight/search" +
                    "?origin="+origin.getString("code")+
                    "&destination="+destination.getString("code")+
                    "&time="+format.format(date)+
                    "&depart_time="+format.format(depart_date)+
                    "&return=false"+
                    "&adult="+adult_pax+
                    "&child="+child_pax+
                    "&infant="+infant_pax+
                    "&airline_access_code="+captcha_input+
                    "&token="+flight_shared_pref.getString("token","")+
                    "&android_id="+android_id;
        }


        if(sort.has("data"))
            url += "&sort="+sort.getString("data");
        if(!filter.toString().equals("{}"))
            url += "&filter=" + URLEncoder.encode(filter.toString(), "utf-8");

        Log.d("url", url);

//        ticketList.clear();
//        mAdapter.notifyDataSetChanged();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")) {

                        if(response.getJSONArray("data").length() > 0) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            int index = 0,total = 0;
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("airlines", jsonArray.getJSONObject(x).getString("airlines"));
                                jsonObject.put("flight_code", jsonArray.getJSONObject(x).getString("code"));
                                jsonObject.put("departTime", jsonArray.getJSONObject(x).getString("time_depart_label"));
                                jsonObject.put("arrivalTime", jsonArray.getJSONObject(x).getString("time_arrive_label"));
                                jsonObject.put("duration", jsonArray.getJSONObject(x).getString("duration"));
                                jsonObject.put("departAirport", origin.getString("code"));
                                jsonObject.put("arrivalAirport", destination.getString("code"));
                                jsonObject.put("departData", origin);
                                jsonObject.put("arrivalData", destination);
                                jsonObject.put("departTimeNumber", jsonArray.getJSONObject(x).getString("time_depart_number"));
                                jsonObject.put("arriveTimeNumber", jsonArray.getJSONObject(x).getString("time_arrive_number"));
                                if (jsonArray.getJSONObject(x).getInt("transit") >= 1)
                                    jsonObject.put("totalTransit", jsonArray.getJSONObject(x).getString("transit") +
                                            (jsonArray.getJSONObject(x).getInt("transit") > 2 ? " Transits" : " Transit")
                                    );
                                else
                                    jsonObject.put("totalTransit", "Langsung");
                                jsonObject.put("price", jsonArray.getJSONObject(x).getString("price"));
                                jsonObject.put("segment", jsonArray.getJSONObject(x).getJSONArray("segment"));
                                jsonObject.put("airlineID", jsonArray.getJSONObject(x).getString("airlineID"));
                                jsonObject.put("airlineCode", jsonArray.getJSONObject(x).getString("airlineCode"));
                                jsonObject.put("flightNumber", jsonArray.getJSONObject(x).getString("flightNumber"));

                                ticketList.add(jsonObject);

                                if(x == 0) {
                                    index = jsonArray.getJSONObject(x).getInt("airline_index");
                                    total = jsonArray.getJSONObject(x).getInt("total_airlines");
                                }
                            }

                            if(index == total) {
                                mAdapter.notifyDataSetChanged();
                                skeleton.hide();
                            }
                            else{
                                flightData();
                            }
                        }
                        else {
                            no_data.setVisibility(View.VISIBLE);
                            skeleton.hide();
                        }

                    }
                    else{
                        url_captcha = C_URL_IMAGES+"captcha?android_id="+android_id;
                        startActivityForResult(new Intent(FlightSearchJadwal.this,CaptchaActivity.class)
                                .putExtra("url_captcha",url_captcha),CAPTCHA);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
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
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void trainData() throws JSONException, UnsupportedEncodingException {
        skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_jadwal_flight_train_item).show();
        String kelas = intent.getStringExtra("kelas");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String url = C_URL+"train/search?origin="+origin.getString("code")+
                "&destination="+destination.getString("code")+
                "&time="+format.format(date)+
                "&adult="+adult_pax+
                "&child="+child_pax+
                "&infant="+infant_pax+
                "&class="+kelas;

        if(sort.has("data"))
            url += "&sort="+sort.getString("data");
        if(!filter.toString().equals("{}"))
            url += "&filter=" + URLEncoder.encode(filter.toString(), "utf-8");

        Log.d("url",url);
        ticketList.clear();
        mAdapter.notifyDataSetChanged();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("data");
                    if(jsonArray.length() > 0) {

                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("name", jsonArray.getJSONObject(x).getString("name"));
                            jsonObject.put("departTime", jsonArray.getJSONObject(x).getString("time_depart_label"));
                            jsonObject.put("arrivalTime", jsonArray.getJSONObject(x).getString("time_arrive_label"));
                            jsonObject.put("duration", jsonArray.getJSONObject(x).getString("duration"));
                            jsonObject.put("departStation", origin.getString("code"));
                            jsonObject.put("arrivalStation", destination.getString("code"));
                            jsonObject.put("departData", origin);
                            jsonObject.put("arrivalData", destination);
                            jsonObject.put("departTimeNumber", jsonArray.getJSONObject(x).getString("time_depart_number"));
                            jsonObject.put("arriveTimeNumber", jsonArray.getJSONObject(x).getString("time_arrive_number"));
                            jsonObject.put("price", jsonArray.getJSONObject(x).getString("price"));
                            jsonObject.put("class", jsonArray.getJSONObject(x).getString("class"));
                            jsonObject.put("sub-class", jsonArray.getJSONObject(x).getString("sub-class"));
                            jsonObject.put("train_number", jsonArray.getJSONObject(x).getString("train-number"));

                            ticketList.add(jsonObject);

                            if (x == 0)
                                mAdapter.notifyDataSetChanged();
                            else
                                mAdapter.notifyItemInserted(x);
                        }
                    }
                    else{
                        no_data.setVisibility(View.VISIBLE);
                    }
                    skeleton.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
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
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void hotelData() throws JSONException, UnsupportedEncodingException {
        skeleton = Skeleton.bind(mRecyclerView).adapter(hotel_adapter).load(R.layout.skeleton_jadwal_hotel_item).show();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String url = C_URL+"hotel/search?city="+hotel_city.getString("id")+
                "&check_in="+format.format(check_in_date)+
                "&check_out="+format.format(check_out_date);

        if(sort.has("data"))
            url += "&sort="+sort.getString("data");
        if(!filter.toString().equals("{}"))
            url += "&filter=" + URLEncoder.encode(filter.toString(), "utf-8");

        ticketList.clear();
        hotel_adapter.notifyDataSetChanged();
        Log.d("data",url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if(response.getJSONArray("data").length() > 0){
                        JSONArray jsonArray = response.getJSONArray("data");
                        for(int x=0; x<jsonArray.length(); x++){
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("name", jsonArray.getJSONObject(x).getString("name"));
                            jsonObject.put("address", jsonArray.getJSONObject(x).getString("address"));
                            jsonObject.put("rating", jsonArray.getJSONObject(x).getInt("rating"));
                            jsonObject.put("location", hotel_city.getString("city_label")+", "+hotel_city.getString("poi_label"));
                            jsonObject.put("location_id", hotel_city.getString("id"));
                            jsonObject.put("check_in", jsonArray.getJSONObject(x).getString("check_in"));
                            jsonObject.put("check_out", jsonArray.getJSONObject(x).getString("check_out"));
                            jsonObject.put("rating", jsonArray.getJSONObject(x).getDouble("rating"));
                            jsonObject.put("rooms", jsonArray.getJSONObject(x).getJSONArray("rooms"));
                            jsonObject.put("photos", jsonArray.getJSONObject(x).getJSONArray("photo"));
                            if(jsonArray.getJSONObject(x).getJSONArray("photo").length() > 0)
                                jsonObject.put("photo", C_URL+"images/hotel?" +
                                    "url="+jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(0).getString("url")+
                                    "&mime="+jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(0).getString("mime"));
                            else
                                jsonObject.put("photo","");
                            jsonObject.put("facilities", jsonArray.getJSONObject(x).getJSONArray("facilities"));
                            jsonObject.put("price", jsonArray.getJSONObject(x).getString("price"));
                            jsonObject.put("duration", jsonArray.getJSONObject(x).getString("duration"));
                            jsonObject.put("internalCode", jsonArray.getJSONObject(x).getString("internalCode"));
                            jsonObject.put("hotelID", jsonArray.getJSONObject(x).getString("ID"));

                            ticketList.add(jsonObject);

                            if (x == 0)
                                hotel_adapter.notifyDataSetChanged();
                            else
                                hotel_adapter.notifyItemInserted(x);
                        }
                    }
                    else
                        no_data.setVisibility(View.VISIBLE);
                    skeleton.hide();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    public void sortView(View v){
        startActivityForResult(new Intent(FlightSearchJadwal.this, Sort.class)
                .putExtra("origin", intentString)
                .putExtra("sort",sort.toString())
                , SORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SORT){
                try {
                    sort = new JSONObject(data.getStringExtra("sort"));
                    sort_button.setTextColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                    sort_button.setIconTint(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                    sort_button.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));

                    if(intent.getStringExtra("origin").equals("flight"))
                        flightData();
                    else if(intent.getStringExtra("origin").equals("train"))
                        trainData();
                    else if(intent.getStringExtra("origin").equals("hotel"))
                        hotelData();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == FILTER){
                try {
                    filter = new JSONObject(data.getStringExtra("filter"));


//                    Log.d("filter",filter.toString());
                    no_data.setVisibility(View.GONE);
                    if(intent.getStringExtra("origin").equals("flight")) {
                        if(filter.getLong("min_price") != 0 || filter.getLong("max_price") != 300000000 ||
                                filter.getJSONArray("arrival_date").length() > 0 ||
                                filter.getJSONArray("departure_date").length() > 0 ||
                                filter.getJSONArray("transit").length() > 0) {
                            filterBtn.setTextColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.grey));
                        }
                        flightData();
                    }
                    else if(intent.getStringExtra("origin").equals("train")) {
                        if(filter.getLong("min_price") != 0 || filter.getLong("max_price") != 300000000 ||
                                filter.getJSONArray("arrival_date").length() > 0 ||
                                filter.getJSONArray("departure_date").length() > 0 ||
                                filter.getJSONArray("class").length() > 0) {
                            filterBtn.setTextColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.grey));
                        }

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String text = format.format(date) + ", " +
                                adult_pax + " "+getResources().getString(R.string.adult_label)+", " +
                                child_pax + " "+getResources().getString(R.string.child_label)+", " +
                                infant_pax + " "+getResources().getString(R.string.infant_label);
                        for(int x=0;x<filter.getJSONArray("class").length();x++)
                            text += ", "+filter.getJSONArray("class").getJSONObject(x).getString("label");

                        tripInfo.setText(text);

                        trainData();
                    }
                    else if(intent.getStringExtra("origin").equals("hotel")) {
                        if(filter.getLong("min_price") != 0 || filter.getLong("max_price") != 300000000 ||
                                filter.getJSONArray("ranking").length() > 0) {
                            filterBtn.setTextColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.grey));
                        }
                        hotelData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == ROUTE){
                route(selected);
            }
            else if(requestCode == CAPTCHA){
                try {
                    captcha_input = data.getStringExtra("captcha");
                    flightData();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(resultCode == RESULT_CANCELED){
            if(requestCode == CAPTCHA){
                onBackPressed();
            }
        }
    }
}
