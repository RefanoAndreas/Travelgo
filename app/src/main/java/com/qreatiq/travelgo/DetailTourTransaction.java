package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailTourTransaction extends BaseActivity {

    Intent intent;
    String dataIntent, url, userID;
    SharedPreferences user_id;
    TextView name, phone, location, trip, trip_date, total_price;
    RecyclerView mRecyclerViewParticipant;
    ParticipantListAdapter mAdapterParticipant;
    ArrayList<JSONObject> ParticipantList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager mLayoutManagerParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour_transaction);

        set_toolbar();

        intent = getIntent();
        dataIntent = intent.getStringExtra("sales_id");

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        name = (TextView)findViewById(R.id.TV_name);
        phone = (TextView)findViewById(R.id.TV_phone);
        location = (TextView)findViewById(R.id.TV_location);
        trip = (TextView)findViewById(R.id.TV_trip);
        trip_date = (TextView)findViewById(R.id.TV_date);
        total_price = (TextView)findViewById(R.id.TV_price_total);


        mRecyclerViewParticipant = findViewById(R.id.RV_participant);
        mRecyclerViewParticipant.setHasFixedSize(true);
        mLayoutManagerParticipant = new LinearLayoutManager(this);
        mAdapterParticipant = new ParticipantListAdapter(ParticipantList, this);
        mAdapterParticipant.setOnItemClickListener(new ParticipantListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        mRecyclerViewParticipant.setLayoutManager(mLayoutManagerParticipant);
        mRecyclerViewParticipant.setAdapter(mAdapterParticipant);

        detail();
    }

    private void detail(){
        url = C_URL+"history/detail?id="+dataIntent+"&type=tour";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jsonDetail= response.getJSONObject("detail");

                    name.setText(jsonDetail.getJSONObject("user").getString("name"));
                    if(!jsonDetail.getJSONObject("user").isNull("phone_number"))
                        phone.setText(jsonDetail.getJSONObject("user").getString("phone_number"));
                    else
                        phone.setText("");

                    for(int x=0; x<jsonDetail.getJSONArray("participant").length();x++){
                        JSONObject jsonParticipant = jsonDetail.getJSONArray("participant").getJSONObject(x);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("title", jsonParticipant.getString("title"));
                        jsonObject.put("name", jsonParticipant.getString("name"));

                        ParticipantList.add(jsonObject);

                        if(x == 0)
                            mAdapterParticipant.notifyItemInserted(ParticipantList.size());
                        else
                            mAdapterParticipant.notifyDataSetChanged();
                    }


                    JSONObject jsonTripPack = jsonDetail.getJSONArray("detail")
                                        .getJSONObject(0)
                                        .getJSONObject("trip_pack");

                    trip_date.setText(jsonTripPack.getJSONObject("trip").getString("trip_date"));
                    location.setText(jsonTripPack.getJSONObject("trip").getJSONObject("city").getString("name"));
                    trip.setText(jsonTripPack.getJSONObject("trip").getString("name"));

                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(response.getDouble("price"));
                    total_price.setText("Rp. "+formattedNumber);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConstraintLayout layout=(ConstraintLayout) findViewById(R.id.layout);
                error_exception(error,layout);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
