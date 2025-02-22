package com.qreatiq.travelgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends BaseActivity {

    TextInputEditText email, password, retypePass;
    Button btnSignUp;
    String url, tokenDevice, selectedPack, cityID;
    SharedPreferences userID, deviceToken, selected_package, city_id;
    TextView btnLogin;
    TextInputLayout emailLayout, passwordLayout, retypePassLayout;
    ConstraintLayout layout;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        city_id = getSharedPreferences("city_id", Context.MODE_PRIVATE);
        cityID = city_id.getString("city_id", "Data not found");

        selected_package = getSharedPreferences("selected_pack", Context.MODE_PRIVATE);
        selectedPack = selected_package.getString("selected_pack", "Data not found");

        getWindow().setBackgroundDrawableResource(R.drawable.background_splash);
        layout=(ConstraintLayout) findViewById(R.id.layout);

        deviceToken = getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenDevice = deviceToken.getString("token", "Data not found");

        Log.d("token", tokenDevice);

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
                finish();
            }
        });

        email = (TextInputEditText) findViewById(R.id.email_input);
        email.setHint("Email");

        password = (TextInputEditText) findViewById(R.id.password_input);
        password.setHint("Password");

        retypePass = ((TextInputEditText) findViewById(R.id.retype_password_input));
        retypePass.setHint("Re-Type Password");

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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void signUp() {
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
            loading.setTitle("Signing Up...");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setCancelable(false);
            loading.setProgress(0);
            loading.show();

            url = C_URL + "signup";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email.getText().toString());
                jsonObject.put("password", password.getText().toString());
                jsonObject.put("token", tokenDevice);
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

                            setResult(RESULT_OK, new Intent());
                            finish();
                        } else {
                            emailLayout.setError("Email already exist");
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

    public void facebook(View v){
        loginButton.performClick();
    }

    private void loginFB(JSONObject object){
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMax(100);
        loading.setTitle("Signing Up via Facebook");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setCancelable(false);
        loading.setProgress(0);
        loading.show();

        url = C_URL+"loginFB";

        try {
            object.put("token", tokenDevice);
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

                        setResult(RESULT_OK, new Intent());
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

}
