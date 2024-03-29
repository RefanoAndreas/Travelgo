package com.qreatiq.travelgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends BaseActivity {

    TextInputEditText email, password;
    TextView signUpBtn;
    Button loginBtn;
    MaterialButton btnLogin;
    SharedPreferences userID, deviceToken, selected_package, city_id;
    String url, user_id, tokenDevice, selectedPack, cityID;
    TextInputLayout emailLayout, passwordLayout;
    String userIDGet, tokenFCM;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ConstraintLayout layout;

    int REGISTER = 10;
    boolean is_by_facebook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);
        layout=(ConstraintLayout) findViewById(R.id.main_login);

        selected_package = getSharedPreferences("selected_pack", Context.MODE_PRIVATE);
        selectedPack = selected_package.getString("selected_pack", "Data not found");

        city_id = getSharedPreferences("city_id", Context.MODE_PRIVATE);
        cityID = city_id.getString("city_id", "Data not found");
        btnLogin = findViewById(R.id.btnLogin);

        Log.d("city", cityID);

//        SharedPreferences.Editor editor1 = getSharedPreferences("city_id", Context.MODE_PRIVATE).edit();
//        editor1.clear().apply();

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
                startActivityForResult(intentSignUp,REGISTER);
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void facebook(View v){
        loginButton.performClick();
        is_by_facebook = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(is_by_facebook)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else{
            if(resultCode == RESULT_OK){
                if(requestCode == REGISTER){
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        }
    }

    private void loginFB(JSONObject object){
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMax(100);
        loading.setTitle(getResources().getString(R.string.login_via_facebook_dialog_title));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setCancelable(false);
        loading.setProgress(0);
        loading.show();
        url = C_URL+"loginFB";

        try {
            object.put("token", tokenFCM);
            object.put("android_id", android_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.dismiss();
                    if(response.getString("status").equals("success")){
                        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userID.edit();
                        editor.putString("access_token", response.getJSONObject("access_token").getString("token_type")+" "+response.getJSONObject("access_token").getString("access_token"));
                        editor.apply();

//                        if(!selectedPack.equals("Data not found")){
//                            startActivity(new Intent(LogIn.this, TransactionDetail.class));
//                            finish();
//                        }
//                        else if(!cityID.equals("Data not found")){
//                            startActivity(new Intent(LogIn.this, CityDetail.class));
//                            finish();
//                        }
//                        else {
//                            Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
//                            startActivity(intentHome);
//                            finish();
//                        }

                        Intent in = new Intent();
                        setResult(RESULT_OK, in);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error_exception(error,layout);
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void login(){

        if(email.getText().toString().equals("")){
            emailLayout.setError(getResources().getString(R.string.data_penumpang_error_email_empty_title));
        }
        else if(password.getText().toString().equals("")){
            passwordLayout.setError(getResources().getString(R.string.error_password_label));
        }
        else if(!isValidEmail(email.getText().toString())){
            emailLayout.setError(getResources().getString(R.string.data_penumpang_error_email_format_title));
        }
        else {
            final ProgressDialog loading = new ProgressDialog(this);
            loading.setMax(100);
            loading.setTitle(getResources().getString(R.string.login_via_system_dialog_title));
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);
            loading.setProgress(0);
            loading.show();

            url = C_URL + "login";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email.getText().toString());
                jsonObject.put("password", password.getText().toString());
                jsonObject.put("token", tokenFCM);
                jsonObject.put("android_id", android_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        loading.dismiss();
                        if (response.getString("status").equals("success")) {
                            userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userID.edit();
                            editor.putString("access_token", response.getJSONObject("access_token").getString("token_type")+" "+response.getJSONObject("access_token").getString("access_token"));
                            editor.apply();

//                            if(!selectedPack.equals("Data not found")){
//                                startActivity(new Intent(LogIn.this, TransactionDetail.class));
//                                finish();
//                            }
//                            else if(!cityID.equals("Data not found")){
//                                startActivity(new Intent(LogIn.this, CityDetail.class));
//                                finish();
//                            }
//                            else {
//                                Intent intentHome = new Intent(LogIn.this, BottomNavContainer.class);
//                                startActivity(intentHome);
//                                finish();
//                            }

                            Intent in = new Intent();
                            setResult(RESULT_OK, in);
                            finish();


                        } else if (response.getString("status").equals("invalid email")) {
                            emailLayout.setError(getResources().getString(R.string.login_error_email_label));
                        } else {
                            passwordLayout.setError(getResources().getString(R.string.login_error_password_label));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    error_exception(error,layout);
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
    }

}
