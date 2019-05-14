package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.qreatiq.travelgo.Utils.BaseActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TourEdit extends BaseActivity {

    ImageView imageView;
    TextInputEditText name, desc, telp;
    TextInputLayout name_layout, desc_layout, telp_layout;
    String url, user_ID, tour_id;
    SharedPreferences userID;
    Bitmap bitmap;

    Uri filePath;

    ConstraintLayout layout;

    ViewSkeletonScreen skeletonScreen;
    CardView image_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_edit);

        set_toolbar();

        name = (TextInputEditText)findViewById(R.id.TIET_tourName_tourEdit);
//        desc = (TextInputEditText)findViewById(R.id.TIET_tourDesc_tourEdit);
        telp = (TextInputEditText)findViewById(R.id.TIET_tourPhone_tourEdit);
        name_layout = (TextInputLayout) findViewById(R.id.TIL_tourName_tourEdit);
//        desc_layout = (TextInputLayout)findViewById(R.id.TIL_tourDescription_tourEdit);
        telp_layout = (TextInputLayout)findViewById(R.id.TIL_tourPhone_tourEdit);
        layout=(ConstraintLayout) findViewById(R.id.layout);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        user_ID = userID.getString("access_token", "Data not found");

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    name_layout.setError("");
            }
        });

        telp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    telp_layout.setError("");
            }
        });

        getTourInfo();
//
        imageView = (ImageView) findViewById(R.id.image);
        image_container = (CardView) findViewById(R.id.image_container);
//        imageView.setClipToOutline(true);
        skeletonScreen = Skeleton.bind(image_container).load(R.layout.skeleton_tour_edit).show();
    }

    public void getTourInfo(){
        url = C_URL+"tourProfile";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("tour")){
                        JSONObject respon = response.getJSONObject("tour");

                        name.setText(respon.getString("name"));
                        telp.setText(respon.getString("phone"));
                        tour_id = respon.getString("id");

                        Picasso.get()
                                .load(link.C_URL_IMAGES + "tour?image=" + respon.getString("urlPhoto")+"&mime="+respon.getString("mimePhoto"))
                                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        skeletonScreen.hide();
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        skeletonScreen.hide();
                                    }
                                });
                    }
                    else
                        skeletonScreen.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
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
                skeletonScreen.hide();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", user_ID);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    public String getStringFile(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void cancel(View v){
        onBackPressed();
    }

    public void submit(View v){
        if(name.getText().toString().equals(""))
            name_layout.setError("Name is empty");
        else if(telp.getText().toString().equals(""))
            telp_layout.setError("Telp is empty");
        else {
            JSONObject json = new JSONObject();
            try {
                json.put("id", tour_id);
                json.put("name", name.getText().toString());
                json.put("phone", telp.getText().toString());
                if (bitmap != null)
                    json.put("image", getStringFile(bitmap));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            logLargeString(json.toString());

            url = C_URL + "tourProfile";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    onBackPressed();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = "";
                    if (error instanceof NetworkError) {
                        message = "Network Error";
                    } else if (error instanceof ServerError) {
                        message = "Server Detect Error";
                    } else if (error instanceof AuthFailureError) {
                        message = "Authentication Detect Error";
                    } else if (error instanceof ParseError) {
                        message = "Parse Detect Error";
                    } else if (error instanceof NoConnectionError) {
                        message = "Connection Missing";
                    } else if (error instanceof TimeoutError) {
                        message = "Server Detect Timeout Reached";
                    }
                    Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", user_ID);
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        }

    }

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("LOG", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("LOG", str); // continuation
        }
    }

    public void addMedia(View v){
        call_media_picker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==PICK_FROM_CAMERA){
                filePath = data.getData();
                bitmap=(Bitmap) data.getExtras().get("data");

                imageView.setImageBitmap(bitmap);
            }
            else if(requestCode==PICK_FROM_GALLERY){

                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
            }

            bottomSheetDialog.dismiss();
        }
    }
}
