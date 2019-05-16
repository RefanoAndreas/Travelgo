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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationOrder extends BaseActivity {

    TextView  isiDataPeserta, specialRequestAdd, guestData;
    View layout_infoHotel, layout_infoFlight, layout_infoTrain;
    Intent intent;
    String intentString;
    LinearLayout specialRequestLinear;
    RecyclerView list_pax;
    CardView card_dataPemesan;

    MaterialButton submit;

    ConfirmationPaxAdapter adapter;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    int ADD_OR_EDIT_PAX = 10, ADD_OR_EDIT_GUEST = 11, selected_arr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        set_toolbar();

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        layout_infoHotel = (View) findViewById(R.id.infoHotelGuest);
        layout_infoFlight = (View)findViewById(R.id.infoFlight);
        layout_infoTrain = (View)findViewById(R.id.infoTrain);
        guestData = (TextView) findViewById(R.id.dataGuestTV);
        list_pax = (RecyclerView) findViewById(R.id.list_pax);
//        isiDataPeserta = (TextView)findViewById(R.id.isiDataPeserta);
        card_dataPemesan = (CardView)findViewById(R.id.data_pemesan_card);
        submit = (MaterialButton) findViewById(R.id.submit);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");



        if(intentString.equals("hotel")){
            try {
                arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            layout_infoHotel.setVisibility(View.VISIBLE);
            guestData.setText("Data Tamu");
//            isiDataPeserta.setText("Isi data tamu");
            card_dataPemesan.setVisibility(View.GONE);
        }
        else if(intentString.equals("flight")){
            try {
                for(int x=0;x<intent.getIntExtra("adult",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Dewasa "+(x+1)+"\",\"type\":\"Dewasa "+(x+1)+"\"}"));
                for(int x=0;x<intent.getIntExtra("child",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Anak "+(x+1)+"\",\"type\":\"Anak "+(x+1)+"\"}"));
                for(int x=0;x<intent.getIntExtra("infant",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Bayi "+(x+1)+"\",\"type\":\"Bayi "+(x+1)+"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            layout_infoFlight.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
//            isiDataPeserta.setText("Isi data penumpang");
        }
        else{
            layout_infoTrain.setVisibility(View.VISIBLE);
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

        Log.d("array", arrayList.toString());

        adapter.setOnItemClickListener(new ConfirmationPaxAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                selected_arr = position;
                if(intentString.equals("flight"))
                    startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                            .putExtra("packageName",intentString)
                            .putExtra("data",arrayList.get(selected_arr).toString()),
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
