package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qreatiq.travelgo.Utils.BaseActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TourCreate extends BaseActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year = 2019, month = 3, day = 10;
    TextInputEditText location;
    String location_id;

    MaterialButton create_tour_pack, submit_saveChanges_tourCreate;

    SharedPreferences user_id;

    private RecyclerView mRecyclerView_1, mRecyclerView_2;
    private RecyclerView.Adapter mAdapter_1;
    private RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;

    ArrayList<JSONObject> photo_array = new ArrayList<JSONObject>();
    TourCreatePhotosAdapter photo_adapter;

    ArrayList<JSONObject> tour_pack_array = new ArrayList<JSONObject>();
    TourCreateTourPackageAdapter tour_pack_adapter;

    ArrayList<JSONObject> array = new ArrayList<JSONObject>();
    TourCreateFacilitiesAdapter adapter;
    GridView grid;

    TextInputEditText start_date,end_date, tripName, tripDesc;
    Uri filePath;
    String url, userID;
    RequestQueue requestQueue;

    TextView no_data;
    ConstraintLayout layout;

    int CREATE_TOUR_PACKAGE = 1, PICK_FROM_GALLERY = 2, PICK_FROM_CAMERA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create);

        set_toolbar();

        user_id = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_id.getString("access_token", "Data not found");

        requestQueue = Volley.newRequestQueue(this);

        grid = (GridView) findViewById(R.id.grid);
        start_date = (TextInputEditText) findViewById(R.id.start_date);
        end_date = (TextInputEditText) findViewById(R.id.end_date);
        no_data = (TextView) findViewById(R.id.no_data);
        create_tour_pack = (MaterialButton) findViewById(R.id.create_tour_pack);
        location = (TextInputEditText) findViewById(R.id.location);
        submit_saveChanges_tourCreate = (MaterialButton)findViewById(R.id.submit_saveChanges_tourCreate);
        layout=(ConstraintLayout) findViewById(R.id.layout);
        tripName = (TextInputEditText)findViewById(R.id.name);
        tripDesc = (TextInputEditText)findViewById(R.id.tripDescription);

        adapter = new TourCreateFacilitiesAdapter(array,TourCreate.this);
        grid.setAdapter(adapter);

        try {
            photo_array.add(new JSONObject("{\"background\": "+R.drawable.upload_photo+", \"is_button_upload\": true}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView_1 = findViewById(R.id.RV_tourCreatePackages_1);
        mRecyclerView_1.setHasFixedSize(true);
        mLayoutManager_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo_adapter = new TourCreatePhotosAdapter(photo_array);

        mRecyclerView_1.setLayoutManager(mLayoutManager_1);
        mRecyclerView_1.setAdapter(photo_adapter);

        photo_adapter.setOnItemClickListner(new TourCreatePhotosAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == 0)
                    call_media_picker();
            }
        });


//        try {
//            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background2+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
//            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background3+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
//            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background4+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
//            tour_pack_array.add(new JSONObject("{\"image\": "+R.drawable.background5+", \"name\": \"Rodex Tour\", \"start_date\": \"11/04/2019\", \"end_date\": \"12/04/2019\"}"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        mRecyclerView_2 = findViewById(R.id.RV_tourCreatePackages_2);
        mRecyclerView_2.setHasFixedSize(true);
        mLayoutManager_2 = new LinearLayoutManager(this);
        tour_pack_adapter = new TourCreateTourPackageAdapter(tour_pack_array);

        mRecyclerView_2.setLayoutManager(mLayoutManager_2);
        mRecyclerView_2.setAdapter(tour_pack_adapter);

        tour_pack_adapter.setOnTrashClickListner(new TourCreateTourPackageAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                tour_pack_array.remove(position);
                tour_pack_adapter.notifyItemRemoved(position);
                tour_pack_adapter.notifyItemRangeChanged(position,tour_pack_array.size());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check_tour_packages();
                    }
                }, 500);

            }
        });

        getFacilities();

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(998);
            }
        });

        create_tour_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TourCreate.this,TourCreatePackage.class),CREATE_TOUR_PACKAGE);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourCreatePackageLocationModal modal = new TourCreatePackageLocationModal();
                modal.show(getSupportFragmentManager(),"modal");
            }
        });

        submit_saveChanges_tourCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrip();
            }
        });

        adapter.setOnItemClickListner(new TourCreateFacilitiesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, boolean isChecked) {
                JSONObject json = array.get(position);
                try {
                    json.put("checked",isChecked);
                    array.set(position,json);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFacilities(){
        url = link.C_URL+"facilities";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    array.clear();
                    for(int x=0; x<response.getJSONArray("facilities").length(); x++){
                        JSONObject json = new JSONObject();
                        json.put("name",response.getJSONArray("facilities").getJSONObject(x).getString("name"));
                        json.put("id",response.getJSONArray("facilities").getJSONObject(x).getString("id"));
                        json.put("checked",false);
                        array.add(json);
                    }
                    adapter.notifyDataSetChanged();
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

    public void check_tour_packages(){
        if(tour_pack_array.size() > 0) {
            mRecyclerView_2.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);
        }
        else{
            mRecyclerView_2.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    showDate(year, month+1, dayOfMonth, "start");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        else if (id == 998) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    showDate(year, month+1, dayOfMonth, "end");
                }
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
        }
        return null;
    }

    private void showDate(int year, int month, int day, String type) {
        if(type == "start")
            start_date.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append("/")
                    .append(month < 10 ? "0"+month : month)
                    .append("/")
                    .append(year));
        else
            end_date.setText(new StringBuilder()
                    .append(day < 10 ? "0"+day : day)
                    .append("/")
                    .append(month < 10 ? "0"+month : month)
                    .append("/")
                    .append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CREATE_TOUR_PACKAGE){
                try {
                    JSONObject data_from_facilities = new JSONObject(data.getStringExtra("data"));

                    JSONObject json = new JSONObject();
                    if(data_from_facilities.has("image")) {
                        json.put("image", (Bitmap) link.StringToBitMap(data_from_facilities.getString("image")));
                        json.put("image_data", data_from_facilities.getString("image"));
                    }
                    else {
                        json.put("image", null);
                        json.put("image_data", null);
                    }
                    json.put("name",data_from_facilities.getString("name"));
                    json.put("price",data_from_facilities.getString("price"));
                    json.put("start_date","11/04/2019");
                    json.put("end_date","12/04/2019");

                    tour_pack_array.add(json);
                    tour_pack_adapter.notifyDataSetChanged();
                    check_tour_packages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode==PICK_FROM_CAMERA){
                filePath = data.getData();
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");

                JSONObject json = new JSONObject();
                try {
                    json.put("background",bitmap);
                    json.put("background_data", link.BitMapToString(bitmap));
                    json.put("is_button_upload",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                photo_array.add(json);
                photo_adapter.notifyItemInserted(photo_array.size());

                bottomSheetDialog.dismiss();
            }
            else if(requestCode==PICK_FROM_GALLERY){

                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                    JSONObject json = new JSONObject();
                    try {
                        json.put("background",bitmap);
                        json.put("background_data", link.BitMapToString(bitmap));
                        json.put("is_button_upload",false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    photo_array.add(json);
                    photo_adapter.notifyItemInserted(photo_array.size());

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
                bottomSheetDialog.dismiss();
            }


        }
    }

    private void createTrip(){
        url = link.C_URL+"trip";

        JSONObject json = new JSONObject();
        try {
            JSONArray array_background = new JSONArray(photo_array.toString());
            JSONArray array_trip_pack = new JSONArray(tour_pack_array.toString());

            JSONArray array_facilities = new JSONArray();
            int counter = 0;
            for(int x=0;x<array.size();x++){
                if(array.get(x).getBoolean("checked")) {
                    array_facilities.put(counter, array.get(x));
                    counter++;
                }
            }

            json.put("background", array_background);
            json.put("tripPack", array_trip_pack);
            json.put("facilities", array_facilities);
            json.put("trip_name", tripName.getText());
            json.put("trip_desc", tripDesc.getText());
            json.put("start_date", start_date.getText());
            json.put("end_date", end_date.getText());
            json.put("location_id", location_id);
//            logLargeString(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMax(100);
        loading.setTitle("Registering");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setCancelable(false);
        loading.setProgress(0);
        loading.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.dismiss();
                    if(response.getString("status").equals("success")){
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

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
                Snackbar snackbar=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", userID);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("LOG", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("LOG", str); // continuation
        }
    }

}
