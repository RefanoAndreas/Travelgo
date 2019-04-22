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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    TextInputEditText email, password, retypePass;
    Button btnSignUp;
    String url;
    RequestQueue requestQueue;
    SharedPreferences userID;
    TextView btnLogin;
    TextInputLayout emailLayout, passwordLayout, retypePassLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);

        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        emailLayout.setHint(null);

        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passwordLayout.setHint(null);

        retypePassLayout = (TextInputLayout) findViewById(R.id.retype_password_layout);
        retypePassLayout.setHint(null);

        btnLogin = (TextView)findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SignUp.this, LogIn.class);
                startActivity(intentLogin);
            }
        });

        email = (TextInputEditText) findViewById(R.id.email_input);
        email.setHint("Email");

        password = (TextInputEditText) findViewById(R.id.password_input);
        password.setHint("Password");

        retypePass = ((TextInputEditText) findViewById(R.id.retype_password_input));
        retypePass.setHint("Re-Type Password");

        requestQueue = Volley.newRequestQueue(this);

        btnSignUp = (Button) findViewById(R.id.signUpBtn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retypePass.getText().toString().equals(password.getText().toString())) {
                    signUp();
                }
                else{
                    retypePassLayout.setError("Password doesn't match");
                }
            }
        });

    }

    private void signUp() {
        url = link.C_URL + "signup.php";

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

                    if (response.getString("status").equals("success")) {
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("user_id", response.getString("data"));
                        editor.apply();

                        Intent intentHome = new Intent(SignUp.this, Home.class);
                        startActivity(intentHome);
                    } else {
//                        Toast.makeText(SignUp.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        emailLayout.setError("Email already exist");
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
