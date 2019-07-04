package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class DataPenumpang extends BaseActivity {
    LinearLayout linearBagasi, titleName_layout, idNo_layout, pasporNo_layout, email_layout, phone_layout;
    MaterialButton batal, submitBtn;
    TextView titleData;
    Spinner spinner_titel;
    TextInputEditText name,no_id,email,phone,no_passport;

    JSONObject data,depart_ticket,return_ticket,baggage_depart,baggage_arrive;

    RecyclerView list_baggage;
    ArrayList<JSONObject> baggage_array = new ArrayList<JSONObject>();
    PassengerBaggageAdapter baggage_adapter;

    int selected_baggage_index=0;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penumpang);

        set_toolbar();

        Intent i = getIntent();
        final String intentString = i.getStringExtra("packageName");

        if(i.hasExtra("data")) {
            try {
                data = new JSONObject(i.getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        linearBagasi = (LinearLayout)findViewById(R.id.linearBagasi);
        titleData = (TextView)findViewById(R.id.titleData);
        titleName_layout = (LinearLayout) findViewById(R.id.titleName_layout);
        idNo_layout = (LinearLayout)findViewById(R.id.idNo_layout);
        pasporNo_layout = (LinearLayout)findViewById(R.id.pasporNo_layout);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
        submitBtn = (MaterialButton)findViewById(R.id.submitBtn);
        list_baggage = (RecyclerView) findViewById(R.id.list_baggage);

        layout=(LinearLayout) findViewById(R.id.layout);

        spinner_titel = (Spinner)findViewById(R.id.spinner_titel);
        name = (TextInputEditText)findViewById(R.id.TIET_name);
        no_id = (TextInputEditText) findViewById(R.id.TIET_idNO);
        no_passport = (TextInputEditText) findViewById(R.id.TIET_pasporNo);
        email = (TextInputEditText) findViewById(R.id.email);
        phone = (TextInputEditText) findViewById(R.id.phone);

        if(intentString.equals("tour") || intentString.equals("hotel")){
            getSupportActionBar().setTitle(getResources().getString(R.string.confirmation_guest_title));
            titleData.setText(getResources().getString(R.string.confirmation_guest_title));
            linearBagasi.setVisibility(View.GONE);
            idNo_layout.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);

            if(!intentString.equals("hotel")) {
                email_layout.setVisibility(View.GONE);
                phone_layout.setVisibility(View.GONE);
            }

            if(i.hasExtra("data")) {
                try {
                    String title = data.getString("title");
                    if (!title.equals(""))
                        spinner_titel.setSelection(data.getInt("title"));
                    name.setText(data.getString("name"));
                    email.setText(data.getString("email"));
                    phone.setText(data.getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(intentString.equals("flight")){
            idNo_layout.setVisibility(View.GONE);
            email_layout.setVisibility(View.GONE);
            phone_layout.setVisibility(View.GONE);

            try {
                Log.d("dataFlight", data.toString());
                depart_ticket = new JSONObject(getIntent().getStringExtra("depart_ticket"));
                if(getIntent().getBooleanExtra("isReturn",false))
                    return_ticket = new JSONObject(getIntent().getStringExtra("return_ticket"));

                String title = data.getString("title");
                if(!title.equals(""))
                    spinner_titel.setSelection(data.getInt("title"));
                name.setText(data.getString("name"));
                no_passport.setText(data.getString("no_passport"));

                baggage_array.add(new JSONObject("{\"airlines\":\""+depart_ticket.getString("airlines")+"\"," +
                        "\"is_return\":false," +
                        "\"baggage\":"+data.getJSONArray("arr_baggage_depart")+"}"));
                baggage_depart = data.getJSONObject("baggage_depart");
                if(getIntent().getBooleanExtra("isReturn",false)) {
                    baggage_array.add(new JSONObject("{\"airlines\":\""+return_ticket.getString("airlines")+"\"," +
                            "\"is_return\":true," +
                            "\"baggage\":"+data.getJSONArray("arr_baggage_return")+"}"));
                    baggage_arrive = data.getJSONObject("baggage_return");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(intentString.equals("train")){
            linearBagasi.setVisibility(View.GONE);
            pasporNo_layout.setVisibility(View.GONE);
            email_layout.setVisibility(View.GONE);

            try {
                String title = data.getString("title");
                if(!title.equals(""))
                    spinner_titel.setSelection(getIndex(spinner_titel,title));
                name.setText(data.getString("name"));
                no_id.setText(data.getString("no_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")){
                    name.setError(getResources().getString(R.string.data_penumpang_error_name_empty_title));
                    Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_name_empty_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(email.getText().toString().equals("") && getIntent().getStringExtra("packageName").equals("hotel")){
                    email.setError(getResources().getString(R.string.data_penumpang_error_email_empty_title));
                    Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_email_empty_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(!isEmailValid(email.getText()) && getIntent().getStringExtra("packageName").equals("hotel")){
                    email.setError(getResources().getString(R.string.data_penumpang_error_email_format_title));
                    Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_email_format_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(phone.getText().toString().equals("") && getIntent().getStringExtra("packageName").equals("hotel")){
                    phone.setError(getResources().getString(R.string.data_penumpang_error_phone_empty_title));
                    Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_phone_empty_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(no_id.getText().toString().equals("") && getIntent().getStringExtra("packageName").equals("train")){
                    no_id.setError(getResources().getString(R.string.data_penumpang_error_id_empty_title));
                    Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_id_empty_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("title", spinner_titel.getSelectedItemPosition());
                        json.put("name", name.getText().toString());

                        if(getIntent().getStringExtra("packageName").equals("flight")) {
                            json.put("no_passport", no_passport.getText().toString());
                            int selected = 0;
                            for (int x = 0; x < baggage_array.get(0).getJSONArray("baggage").length(); x++) {
                                if (baggage_array.get(0).getJSONArray("baggage").getJSONObject(x).getBoolean("checked"))
                                    selected = x;
                            }
                            json.put("baggage_depart", baggage_array.get(0).getJSONArray("baggage").getJSONObject(selected));

                            if (getIntent().getBooleanExtra("isReturn", false)) {
                                for (int x = 0; x < baggage_array.get(1).getJSONArray("baggage").length(); x++) {
                                    if (baggage_array.get(1).getJSONArray("baggage").getJSONObject(x).getBoolean("checked"))
                                        selected = x;
                                }
                                json.put("baggage_return", baggage_array.get(1).getJSONArray("baggage").getJSONObject(selected));
                            }
                        }
                        else if(getIntent().getStringExtra("packageName").equals("train")) {
                            json.put("no_id", no_id.getText().toString());
                        }
                        else if(getIntent().getStringExtra("packageName").equals("hotel")) {
                            json.put("email", email.getText().toString());
                            json.put("phone", phone.getText().toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent in = new Intent();
                    in.putExtra("data", json.toString());
                    setResult(RESULT_OK, in);
                    finish();
                }
            }
        });

        batal = (MaterialButton) findViewById(R.id.batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        baggage_adapter = new PassengerBaggageAdapter(baggage_array,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list_baggage.setLayoutManager(mLayoutManager);
        list_baggage.setAdapter(baggage_adapter);

        baggage_adapter.setOnItemClickListener(new PassengerBaggageAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                PassengerSelectBaggageModal modal = new PassengerSelectBaggageModal();
                modal.selected = position;
                modal.show(getSupportFragmentManager(),"modal");
            }
        });

//        try {
//            get_baggage("depart");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}
