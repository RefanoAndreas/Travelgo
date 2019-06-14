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
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class D2NotifikasiDetail extends BaseActivity {

    LinearLayout eTicketLayout, eTicketLayout1;
    Intent i;
    String type, url, sales_id;
    TextView TV_eTicket, TV_price, TV_order_date, TV_no_order, TV_status_order;

    RecyclerView RV_eTicket;
    ETicketAdapter adapter_eTicket;
    ArrayList<JSONObject> eTicketList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager layoutManager_eTicket;
    String userID;
    SharedPreferences user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2_notifikasi_detail);

        set_toolbar();

        TV_price = (TextView)findViewById(R.id.TV_price);
        TV_order_date = (TextView)findViewById(R.id.TV_order_date);
        TV_no_order = (TextView)findViewById(R.id.TV_no_order);
        TV_status_order = (TextView)findViewById(R.id.TV_status_order);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        i = getIntent();
        type = i.getStringExtra("salesType");
        sales_id = i.getStringExtra("sales_id");

        RV_eTicket = findViewById(R.id.RV_eTicket);
        RV_eTicket.setHasFixedSize(true);
        layoutManager_eTicket = new LinearLayoutManager(this);
        adapter_eTicket = new ETicketAdapter(eTicketList, this);

        RV_eTicket.setLayoutManager(layoutManager_eTicket);
        RV_eTicket.setAdapter(adapter_eTicket);

        adapter_eTicket.setOnItemClickListner(new ETicketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    startActivity(new Intent(D2NotifikasiDetail.this, D3Eticket.class)
                                .putExtra("type", type)
                                .putExtra("id", eTicketList.get(position).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        detail();

    }

    private void detail(){

        url = C_URL+"history/detail?id="+sales_id+"&type="+type;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonDetail = response.getJSONObject("detail");

                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(response.getDouble("price"));
                    TV_price.setText("Rp. "+formattedNumber);
                    TV_order_date.setText(jsonDetail.getString("order_date"));
                    TV_no_order.setText(jsonDetail.getString("id"));

                    if(jsonDetail.getString("status").equals("pending")){
                        TV_status_order.setText("Pesanan Belum Dibayar");
                    }
                    else if(jsonDetail.getString("status").equals("berhasil")){
                        TV_status_order.setText("Pesanan Berhasil");
                    }

                    if(type.equals("flight") || type.equals("train")) {
                        for (int x = 0; x < jsonDetail.getJSONArray("detail").length(); x++) {
                            JSONObject jsonObject1 = new JSONObject();
                            JSONObject jsonETicket = jsonDetail.getJSONArray("detail").getJSONObject(x);

                            try {
                                jsonObject1.put("id", jsonETicket.getString("id"));
                                jsonObject1.put("info_eticket", jsonETicket.getJSONObject("departure").getJSONObject("city").getString("name")+" > "+
                                        jsonETicket.getJSONObject("arrival").getJSONObject("city").getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            eTicketList.add(jsonObject1);
                            if (x != 0)
                                adapter_eTicket.notifyItemInserted(x);
                            else
                                adapter_eTicket.notifyDataSetChanged();
                        }
                    }
                    else{
                        JSONObject jsonObject1 = new JSONObject();

                        try {
                            jsonObject1.put("id", jsonDetail.getString("id"));
                            jsonObject1.put("info_eticket", jsonDetail.getJSONObject("room").getJSONObject("hotel").getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        eTicketList.add(jsonObject1);
                        adapter_eTicket.notifyDataSetChanged();
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
