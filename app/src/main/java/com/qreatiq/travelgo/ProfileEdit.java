package com.qreatiq.travelgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class ProfileEdit extends BaseActivity {

    TextInputLayout nameLayout, emailLayout, phoneLayout, passwordLayout, retypePassLayout;
    TextInputEditText name, email, phone, password, retypePass;
    String userID, url;
    SharedPreferences user_id;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        link.setToolbar(this);

        nameLayout = (TextInputLayout) findViewById(R.id.TIL_name_profileEdit);
        nameLayout.setHint(null);

        emailLayout = (TextInputLayout) findViewById(R.id.TIL_email_profileEdit);
        emailLayout.setHint(null);

        phoneLayout = (TextInputLayout) findViewById(R.id.TIL_phone_profileEdit);
        phoneLayout.setHint(null);

        passwordLayout = (TextInputLayout) findViewById(R.id.TIL_password_profileEdit);
        passwordLayout.setHint(null);

        retypePassLayout = (TextInputLayout) findViewById(R.id.TIL_rePassword_profileEdit);
        retypePassLayout.setHint(null);

        name = (TextInputEditText) findViewById(R.id.TIET_name_profileEdit);
        email = (TextInputEditText) findViewById(R.id.TIET_email_profileEdit);
        phone = (TextInputEditText) findViewById(R.id.TIET_phone_profileEdit);
        password = (TextInputEditText) findViewById(R.id.TIET_password_profileEdit);
        retypePass = (TextInputEditText) findViewById(R.id.TIET_rePassword_profileEdit);

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("user_id", "Data not found");

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    nameLayout.setError("");
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    phoneLayout.setError("");
            }
        });

        retypePass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    retypePassLayout.setError("");
            }
        });

        getUserInfo();

        saveBtn = (Button)findViewById(R.id.submit_profileEdit);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(retypePass.getText().toString())) {
                    saveEditProfile();
                }
                else{
                    retypePassLayout.setError("Password doesn't match");
                }
            }
        });

    }

    public void goBack(View view){
        super.onBackPressed();
    }

    private void getUserInfo(){
        url = link.C_URL+"profile.php?id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("user")){
                        name.setText(!response.getJSONObject("user").isNull("name") ? response.getJSONObject("user").getString("name") : "");
                        email.setText(!response.getJSONObject("user").isNull("email") ? response.getJSONObject("user").getString("email") : "");
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
                CoordinatorLayout layout=(CoordinatorLayout) findViewById(R.id.layout);
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

        requestQueue.add(jsonObjectRequest);
    }

    private void saveEditProfile(){
        if(name.getText().toString().equals(""))
            nameLayout.setError("Name is empty");
        else if(phone.getText().toString().equals(""))
            phoneLayout.setError("Phone is empty");
        else {
            url = link.C_URL + "saveProfile.php";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", userID);
                jsonObject.put("name", name.getText().toString());
                jsonObject.put("phone", phone.getText().toString());

                if (!password.getText().toString().isEmpty())
                    jsonObject.put("password", password.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equals("success")) {
                            ProfileEdit.super.onBackPressed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.getMessage());
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
    }

}
