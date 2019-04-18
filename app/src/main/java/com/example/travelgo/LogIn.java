package com.example.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    TextInputEditText email, password;
    TextView signUpBtn;
    Button loginBtn;
    SharedPreferences userID;
    String url;
    RequestQueue requestQueue;
    TextInputLayout emailLayout, passwordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);

        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        emailLayout.setHint(null);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passwordLayout.setHint(null);

        email = (TextInputEditText) findViewById(R.id.email_input);
        email.setHint("Email");

        password = (TextInputEditText) findViewById(R.id.password_input);
        password.setHint("Password");

        signUpBtn = (TextView) findViewById(R.id.btnSignUp);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(LogIn.this, SignUp.class);
                startActivity(intentSignUp);
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        url = link.C_URL+"login.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("responseLogin", response.toString());
                try {

                    if(response.getString("status").equals("success")){
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("user_id", response.getString("data"));
                        editor.apply();

                        Intent intentHome = new Intent(LogIn.this, Home.class);
                        startActivity(intentHome);
                    }
                    else if(response.getString("status").equals("invalid email")){
                        emailLayout.setError("Invalid Email");
                    }
                    else{
//                        Toast.makeText(LogIn.this, "Email/Password wrong", Toast.LENGTH_SHORT).show();
                        passwordLayout.setError("Invalid Password");
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
