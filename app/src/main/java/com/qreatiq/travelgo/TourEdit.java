package com.qreatiq.travelgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TourEdit extends AppCompatActivity {

    ImageView imageView;
    TextInputEditText name, desc;
    String url, user_ID;
    SharedPreferences userID;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextInputEditText)findViewById(R.id.TIET_tourName_tourEdit);
        desc = (TextInputEditText)findViewById(R.id.TIET_tourDesc_tourEdit);

        requestQueue = Volley.newRequestQueue(this);

        userID = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        user_ID = userID.getString("user_id", "Data not found");

        getTourInfo();
//
//        imageView = (ImageView) findViewById(R.id.image_tourEdit);
//        imageView.setClipToOutline(true);
    }

    public void getTourInfo(){
        url = link.C_URL+"getUserTour.php?id="+user_ID;
        Log.d("tourURL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("tour")){
                        name.setText(response.getJSONObject("tour").getString("name_tour"));
                        desc.setText(response.getJSONObject("tour").getString("description_tour"));
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
