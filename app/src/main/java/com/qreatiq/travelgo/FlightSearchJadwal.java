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
    String intentString,url_captcha,captcha_input = "";
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

            if (!intent.getBooleanExtra("isOpposite", false))
                date = new Date(intent.getLongExtra("tanggal_berangkat", 0));
            else
                date = new Date(intent.getLongExtra("tanggal_kembali", 0));
            adult_pax = intent.getIntExtra("adult", 0);
            child_pax = intent.getIntExtra("child", 0);
            infant_pax = intent.getIntExtra("infant", 0);
            String kelas = intent.getStringExtra("kelas");
            try {
                origin = new JSONObject(intent.getStringExtra("depart_data"));
                destination = new JSONObject(intent.getStringExtra("arrive_data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tripInfo = (TextView) findViewById(R.id.tripInfo);
            title = (TextView) findViewById(R.id.title);

            SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
            tripInfo.setText(format.format(date) + ", " +
                    String.valueOf(adult_pax) + " Dewasa, " +
                    String.valueOf(child_pax) + " Anak, " +
                    String.valueOf(infant_pax) + " Bayi, " +
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
                SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
                tripInfo.setText(format.format(check_in_date) + " - "+format.format(check_out_date)+", " +
                        String.valueOf(intent.getIntExtra("guest", 0)) + " Tamu, " +
                        String.valueOf(intent.getIntExtra("room", 0)) + " Kamar");
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
            hotel_adapter = new HotelListAdapter(ticketList);
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
                skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_jadwal_flight_train_item).show();
                flightData();
            }
            else if(intent.getStringExtra("origin").equals("train")){
                skeleton = Skeleton.bind(mRecyclerView).adapter(mAdapter).load(R.layout.skeleton_jadwal_flight_train_item).show();
                trainData();
            }
            else if(intent.getStringExtra("origin").equals("hotel")){
                skeleton = Skeleton.bind(mRecyclerView).adapter(hotel_adapter).load(R.layout.skeleton_jadwal_hotel_item).show();
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
                    if(base_shared_pref.getString("access_token","Data not found").equals("Data not found")){
                        startActivityForResult(new Intent(FlightSearchJadwal.this, LogIn.class), ROUTE);
                    }
                    else {
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
            }
            else{
                if(base_shared_pref.getString("access_token","Data not found").equals("Data not found")){
                    startActivityForResult(new Intent(FlightSearchJadwal.this, LogIn.class), ROUTE);
                }
                else {
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
                    if(base_shared_pref.getString("access_token","Data not found").equals("Data not found")){
                        startActivityForResult(new Intent(FlightSearchJadwal.this, LogIn.class), ROUTE);
                    }
                    else {
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
            }
            else{
                if(base_shared_pref.getString("access_token","Data not found").equals("Data not found")){
                    startActivityForResult(new Intent(FlightSearchJadwal.this, LogIn.class), ROUTE);
                }
                else {
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
    }

    public void flightData() throws JSONException, UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
        String url = C_URL+"flight/search?origin="+origin.getString("code")+
                    "&destination="+destination.getString("code")+
                    "&time="+format.format(date)+
                    "&adult="+adult_pax+
                    "&child="+child_pax+
                    "&infant="+infant_pax+
                    "&airline_access_code="+captcha_input+
                    "&android_id="+android_id;

        if(sort.has("data"))
            url += "&sort="+sort.getString("data");
        if(!filter.toString().equals("{}"))
            url += "&filter=" + URLEncoder.encode(filter.toString(), "utf-8");

        Log.d("url", url);

        ticketList.clear();
        mAdapter.notifyDataSetChanged();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")) {

                        if(response.getJSONArray("data").length() > 0) {
                            JSONArray jsonArray = response.getJSONArray("data");
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
                                if (jsonArray.getJSONObject(x).getInt("transit") > 1)
                                    jsonObject.put("totalTransit", jsonArray.getJSONObject(x).getString("transit") +
                                            (jsonArray.getJSONObject(x).getInt("transit") > 2 ? " Transits" : " Transit")
                                    );
                                else
                                    jsonObject.put("totalTransit", "Langsung");
                                jsonObject.put("price", jsonArray.getJSONObject(x).getString("price"));

                                ticketList.add(jsonObject);

                                if (x == 0)
                                    mAdapter.notifyDataSetChanged();
                                else
                                    mAdapter.notifyItemInserted(x);
                            }

                        }
                        else
                            no_data.setVisibility(View.VISIBLE);
                        skeleton.hide();
                    }
                    else{
                        url_captcha = C_URL_IMAGES+"captcha?android_id="+android_id;
                        startActivityForResult(new Intent(FlightSearchJadwal.this,CaptchaActivity.class)
                                .putExtra("url_captcha",url_captcha),CAPTCHA);
                    }
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
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void trainData() throws JSONException, UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
        String url = C_URL+"train/search?origin="+origin.getString("code")+
                "&destination="+destination.getString("code")+
                "&time="+format.format(date)+
                "&adult="+adult_pax+
                "&child="+child_pax+
                "&infant="+infant_pax;

        if(sort.has("data"))
            url += "&sort="+sort.getString("data");
        if(!filter.toString().equals("{}"))
            url += "&filter=" + URLEncoder.encode(filter.toString(), "utf-8");

        ticketList.clear();
        mAdapter.notifyDataSetChanged();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("data");
                    if(jsonArray.length() > 0) {

                        for (int x = 0; x < jsonArray.length(); x++) {
                            for (int y = 0; y < jsonArray.getJSONObject(x).getJSONArray("availibilityClasses").length(); y++) {
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("name", jsonArray.getJSONObject(x).getString("trainName"));
                                jsonObject.put("departTime", jsonArray.getJSONObject(x).getString("time_depart_label"));
                                jsonObject.put("arrivalTime", jsonArray.getJSONObject(x).getString("time_arrive_label"));
                                jsonObject.put("duration", jsonArray.getJSONObject(x).getString("duration"));
                                jsonObject.put("departStation", origin.getString("code"));
                                jsonObject.put("arrivalStation", destination.getString("code"));
                                jsonObject.put("departData", origin);
                                jsonObject.put("arrivalData", destination);
                                jsonObject.put("departTimeNumber", jsonArray.getJSONObject(x).getString("time_depart_number"));
                                jsonObject.put("arriveTimeNumber", jsonArray.getJSONObject(x).getString("time_arrive_number"));
                                jsonObject.put("price", jsonArray.getJSONObject(x).getJSONArray("availibilityClasses").getJSONObject(y).getString("price"));
                                jsonObject.put("class", jsonArray.getJSONObject(x).getJSONArray("availibilityClasses").getJSONObject(y).getString("availabilityClass"));
                                jsonObject.put("sub-class", jsonArray.getJSONObject(x).getJSONArray("availibilityClasses").getJSONObject(y).getString("subClass"));

                                jsonObject.put("train_number", jsonArray.getJSONObject(x).getString("trainNumber"));

                                ticketList.add(jsonObject);

                                if (x == 0)
                                    mAdapter.notifyDataSetChanged();
                                else
                                    mAdapter.notifyItemInserted(x);
                            }
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
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void hotelData() throws JSONException, UnsupportedEncodingException {
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
                            jsonObject.put("photo", C_URL+"images/hotel?" +
                                    "url="+jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(0).getString("url")+
                                    "&mime="+jsonArray.getJSONObject(x).getJSONArray("photo").getJSONObject(0).getString("mime"));
                            jsonObject.put("facilities", jsonArray.getJSONObject(x).getJSONArray("facilities"));
                            jsonObject.put("price", jsonArray.getJSONObject(x).getString("price"));

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
                60000,
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
                    sort_button.setTextColor(getResources().getColor(R.color.white));
                    sort_button.setIconTint(ContextCompat.getColorStateList(this, R.color.white));
                    sort_button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));

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
                                filter.getJSONArray("departure_date").length() > 0) {
                            filterBtn.setTextColor(getResources().getColor(R.color.white));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.white));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
                        }
                        flightData();
                    }
                    else if(intent.getStringExtra("origin").equals("train")) {
                        if(filter.getLong("min_price") != 0 || filter.getLong("max_price") != 300000000 ||
                                filter.getJSONArray("arrival_date").length() > 0 ||
                                filter.getJSONArray("departure_date").length() > 0) {
                            filterBtn.setTextColor(getResources().getColor(R.color.white));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.white));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
                        }
                        trainData();
                    }
                    else if(intent.getStringExtra("origin").equals("hotel")) {
                        if(filter.getLong("min_price") != 0 || filter.getLong("max_price") != 300000000 ||
                                filter.getJSONArray("ranking").length() > 0) {
                            filterBtn.setTextColor(getResources().getColor(R.color.white));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.white));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        else{
                            filterBtn.setTextColor(getResources().getColor(R.color.grey));
                            filterBtn.setIconTint(ContextCompat.getColorStateList(this, R.color.grey));
                            filterBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
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
                    captcha_input = intent.getStringExtra("captcha");
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
