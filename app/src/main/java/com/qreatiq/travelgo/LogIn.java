package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends BaseActivity {

    TextInputEditText email, password;
    TextView signUpBtn;
    Button loginBtn;
    SharedPreferences userID, deviceToken;
    String url, user_id, tokenDevice;
    RequestQueue requestQueue;
    TextInputLayout emailLayout, passwordLayout;
    String userIDGet, tokenFCM;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);
        layout=(ConstraintLayout) findViewById(R.id.main_login);

        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        emailLayout.setHint(null);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passwordLayout.setHint(null);

//        link.setupUI(layout,this);

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
                finish();
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    emailLayout.setError("");
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    passwordLayout.setError("");
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LogIn.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenFCM = instanceIdResult.getToken();

                Log.d("token",tokenFCM);

                deviceToken = getSharedPreferences("token", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = deviceToken.edit();
                editor1.putString("token", tokenFCM);
                editor1.apply();
            }
        });

        requestQueue = Volley.newRequestQueue(this);


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

    public void facebook(View v){
        loginButton.performClick();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginFB(JSONObject object){
        url = C_URL+"loginFB";

        try {
            object.put("token", tokenFCM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success")){
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("access_token", response.getJSONObject("access_token").getString("token_type")+" "+response.getJSONObject("access_token").getString("access_token"));
                        editor.apply();

                        Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
                        startActivity(intentHome);
                        finish();
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void login(View v){

        if(email.getText().toString().equals("")){
            emailLayout.setError("Email is empty");
        }
        else if(password.getText().toString().equals("")){
            passwordLayout.setError("Password is empty");
        }
        else if(!isValidEmail(email.getText().toString())){
            emailLayout.setError("Email is not in email format");
        }
        else {
            url = C_URL + "login";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email.getText().toString());
                jsonObject.put("password", password.getText().toString());
                jsonObject.put("token", tokenFCM);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getString("status").equals("success")) {
                            userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userID.edit();
                            editor.putString("access_token", response.getJSONObject("access_token").getString("token_type")+" "+response.getJSONObject("access_token").getString("access_token"));
                            editor.apply();

                            Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
                            startActivity(intentHome);
                            finish();
                        } else if (response.getString("status").equals("invalid email")) {
                            emailLayout.setError("Invalid Email");
                        } else {
                            passwordLayout.setError("Invalid Password");
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
