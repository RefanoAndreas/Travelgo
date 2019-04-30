package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TourEdit extends AppCompatActivity {

    ImageView imageView;
    TextInputEditText name, desc, telp;
    TextInputLayout name_layout, desc_layout, telp_layout;
    String url, user_ID, tour_id;
    SharedPreferences userID;
    RequestQueue requestQueue;
    Bitmap bitmap;

    BottomSheetDialog bottomSheetDialog;
    Uri filePath;

    ConstraintLayout layout;

    ViewSkeletonScreen skeletonScreen;
    CardView image_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_edit);

        link.setToolbar(this);

        name = (TextInputEditText)findViewById(R.id.TIET_tourName_tourEdit);
//        desc = (TextInputEditText)findViewById(R.id.TIET_tourDesc_tourEdit);
        telp = (TextInputEditText)findViewById(R.id.TIET_tourPhone_tourEdit);
        name_layout = (TextInputLayout) findViewById(R.id.TIL_tourName_tourEdit);
//        desc_layout = (TextInputLayout)findViewById(R.id.TIL_tourDescription_tourEdit);
        telp_layout = (TextInputLayout)findViewById(R.id.TIL_tourPhone_tourEdit);
        layout=(ConstraintLayout) findViewById(R.id.layout);

        requestQueue = Volley.newRequestQueue(this);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        user_ID = userID.getString("user_id", "Data not found");

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
        url = link.C_URL+"getUserTour.php?id="+user_ID;
        Log.d("tourURL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("tour")){
                        name.setText(response.getJSONObject("tour").getString("name"));
//                        desc.setText(response.getJSONObject("tour").getString("description"));
                        telp.setText(response.getJSONObject("tour").getString("phone"));
                        tour_id = response.getJSONObject("tour").getString("id");
                        Picasso.get()
                                .load(link.C_URL_IMAGES + "tour/" + response.getJSONObject("tour").getString("url_photo"))
                                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        skeletonScreen.hide();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
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
                json.put("id", user_ID);
                json.put("name", name.getText().toString());
                json.put("phone", telp.getText().toString());
                json.put("description", desc.getText().toString());
                if (bitmap != null)
                    json.put("image", getStringFile(bitmap));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            url = link.C_URL + "saveTour.php";
//        Log.d("data", json.toString());
//        Log.d("url", url);

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
            });

            requestQueue.add(jsonObjectRequest);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                v instanceof TextInputEditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {

                link.hideSoftKeyboard(this);
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void addMedia(View v){
        bottomSheetDialog=new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.media_picker_fragment, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
//        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),1);
    }

    public void camera(View v){
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),1);
    }

    public void gallery(View v){
        Intent in =new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==1){
                filePath = data.getData();
                bitmap=(Bitmap) data.getExtras().get("data");

                imageView.setImageBitmap(bitmap);
            }
            else if(requestCode==0){

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
