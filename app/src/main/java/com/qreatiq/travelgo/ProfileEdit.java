package com.qreatiq.travelgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileEdit extends AppCompatActivity {

    TextInputEditText name, email, phone, password, retypePass;
    String userID, url;
    SharedPreferences user_id;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextInputLayout) findViewById(R.id.TIL_name_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_email_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_phone_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_password_profileEdit)).setHint(null);
        ((TextInputLayout) findViewById(R.id.TIL_rePassword_profileEdit)).setHint(null);

        name = (TextInputEditText) findViewById(R.id.TIET_name_profileEdit);
        email = (TextInputEditText) findViewById(R.id.TIET_email_profileEdit);
        phone = (TextInputEditText) findViewById(R.id.TIET_phone_profileEdit);
        password = (TextInputEditText) findViewById(R.id.TIET_password_profileEdit);
        retypePass = (TextInputEditText) findViewById(R.id.TIET_rePassword_profileEdit);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("user_id", "Data not found");

        requestQueue = Volley.newRequestQueue(this);

        getUserInfo();
    }

    private void getUserInfo(){
        url = link.C_URL+"profile.php?id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("user")){
                        name.setText(response.getJSONObject("user").getString("name"));
                        email.setText(response.getJSONObject("user").getString("email"));
                        if(!response.getJSONObject("user").isNull("phone"))
                            phone.setText(response.getJSONObject("user").getString("phone"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);


    }

}
