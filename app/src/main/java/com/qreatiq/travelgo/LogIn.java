package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    TextInputEditText email, password;
    TextView signUpBtn;
    Button loginBtn;
    SharedPreferences userID;
    String url, user_id;
    RequestQueue requestQueue;
    TextInputLayout emailLayout, passwordLayout;
    String userIDGet;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userIDGet = userID.getString("user_id", "Data not found");

//        if(!userIDGet.equals("Data not found")){
//            startActivity(new Intent(LogIn.this, BottomNavContainer.class));
//        }

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



        loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        loginButton.setReadPermissions("email");


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        loginFB(object);
                                    }
                                }
                        );

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, link, email");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginFB(JSONObject object){
        url = link.C_URL+"login.php";

        try {
            object.put("login", "facebook");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("responseLogin", response.toString());
                try {

                    if(response.getString("status").equals("success")){
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("user_id", response.getString("data"));
                        editor.apply();

                        Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
                        startActivity(intentHome);
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


    private void login(){
        url = link.C_URL+"login.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("login", "form");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if(response.getString("status").equals("success")){
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("user_id", response.getString("data"));
                        editor.apply();

                        Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
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