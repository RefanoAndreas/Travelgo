package com.qreatiq.travelgo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TourCreate extends BaseActivity {

    private DatePicker datePicker;
    private Calendar calendar = Calendar.getInstance();
    private EditText dateView;
    private int year = 2019, month = 3, day = 10;
    TextInputEditText location;
    String location_id = "";

    MaterialButton create_tour_pack, submit_saveChanges_tourCreate;

    SharedPreferences user_id;

    private RecyclerView mRecyclerView_1, mRecyclerView_2;
    private RecyclerView.Adapter mAdapter_1;
    private RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;

    ArrayList<String> keyword_array = new ArrayList<String>();
    ArrayList<JSONObject> photo_array = new ArrayList<JSONObject>();
    TourCreatePhotosAdapter photo_adapter;

    ArrayList<JSONObject> tour_pack_array = new ArrayList<JSONObject>();
    TourCreateTourPackageAdapter tour_pack_adapter;

    ArrayList<JSONObject> array = new ArrayList<JSONObject>();
    TourCreateFacilitiesAdapter adapter;
    GridView grid;

    TextInputEditText start_date,end_date, tripName, tripDesc, keyword;
    TextInputLayout name_layout,start_date_layout,end_date_layout,description_layout,location_layout,keyword_layout;
    ChipGroup keyword_chip_layout;
    Uri filePath;
    String url, userID, urlPhoto;
    RequestQueue requestQueue, requestQueue1;

    TextView no_data;
    ConstraintLayout layout;

    Intent intent;

    int CREATE_TOUR_PACKAGE = 1,EDIT_TOUR_PACKAGE = 2, START_DATE = 3, END_DATE = 4, selected_tour_package;
    Date start_date_data = new Date(),end_date_data = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_tour_create);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        20);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        set_toolbar();

        intent = getIntent();
        if(intent.getStringExtra("type").equals("edit"))
            setTitle(getResources().getString(R.string.manifest_edit_package_title));

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
        keyword = (TextInputEditText) findViewById(R.id.keyword);

        name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        start_date_layout = (TextInputLayout) findViewById(R.id.start_date_layout);
        end_date_layout = (TextInputLayout) findViewById(R.id.end_date_layout);
        description_layout = (TextInputLayout) findViewById(R.id.description_layout);
        location_layout = (TextInputLayout) findViewById(R.id.location_layout);
//        keyword_layout = (TextInputLayout) findViewById(R.id.keyword_layout);
        keyword_chip_layout = (ChipGroup) findViewById(R.id.keyword_chip_layout);

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
                else{
                    photo_array.remove(position);
                    photo_adapter.notifyItemRemoved(position);
                    photo_adapter.notifyItemRangeRemoved(position,photo_array.size());
                }
            }
        });


        mRecyclerView_2 = findViewById(R.id.RV_tourCreatePackages_2);
        mRecyclerView_2.setHasFixedSize(true);
        mLayoutManager_2 = new LinearLayoutManager(this);
        tour_pack_adapter = new TourCreateTourPackageAdapter(tour_pack_array);

        mRecyclerView_2.setLayoutManager(mLayoutManager_2);
        mRecyclerView_2.setAdapter(tour_pack_adapter);

        tour_pack_adapter.setOnClickListener(new TourCreateTourPackageAdapter.ClickListener() {
            @Override
            public void onTrashClick(int position) {
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

            @Override
            public void onItemClick(int position) {
                selected_tour_package = position;
                startActivityForResult(new Intent(TourCreate.this,TourCreatePackage.class)
                        .putExtra("type","edit")
                        .putExtra("data",tour_pack_array.get(position).toString()),
                        EDIT_TOUR_PACKAGE);
            }
        });
        check_tour_packages();

        getFacilities();

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date_layout.setError("");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                Intent in = new Intent(TourCreate.this,DatePickerActivity.class);
                in.putExtra("start_date",start_date_data.getTime());
                in.putExtra("end_date",end_date_data.getTime());
                in.putExtra("isReturn",true);
                startActivityForResult(in,START_DATE);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_date_layout.setError("");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                Intent in = new Intent(TourCreate.this,DatePickerActivity.class);
                in.putExtra("start_date",start_date_data.getTime());
                in.putExtra("end_date",end_date_data.getTime());
                in.putExtra("isReturn",true);
                startActivityForResult(in,END_DATE);
            }
        });

        create_tour_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TourCreate.this,TourCreatePackage.class)
                        .putExtra("type","add"),CREATE_TOUR_PACKAGE);
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

        tripName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name_layout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tripDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description_layout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    int counter = 0;
                    for(int x=0;x<keyword_array.size();x++) {
                        if(keyword.getText().toString().toLowerCase().equals(keyword_array.get(x).toLowerCase()))
                           break;
                        counter++;
                    }

//                    Log.d("data", String.valueOf(counter));
//                    Log.d("data", String.valueOf(keyword_array.size()-1));
                    if(counter == keyword_array.size()) {
                        final View view = LayoutInflater.from(TourCreate.this).inflate(R.layout.chip_with_exit,null);
                        Chip chip = (Chip) view.findViewById(R.id.chip);
                        chip.setText(keyword.getText().toString());
                        keyword_chip_layout.addView(view);
                        keyword_array.add(keyword.getText().toString());

                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                keyword_chip_layout.removeView(view);
                                int index=0;
                                for(int x=0;x<keyword_array.size();x++) {
                                    if (keyword_array.get(x).equals(keyword.getText().toString())) {
                                        index = x;
                                        break;
                                    }
                                }
                                keyword_array.remove(index);
                            }
                        });
                    }
                    keyword.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private void getFacilities(){
        url = C_URL+"facilities";

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

                if(intent.getStringExtra("type").equals("edit")){
                    trip_data(intent.getStringExtra("id"));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CREATE_TOUR_PACKAGE){
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    JSONObject data_from_facilities = new JSONObject(data.getStringExtra("data"));

                    JSONObject json = new JSONObject();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(data_from_facilities.getString("image")));
                    json.put("image", bitmap);
                    json.put("image_data", data_from_facilities.getString("image"));
                    json.put("is_link_image", data_from_facilities.getBoolean("is_link_image"));
                    json.put("name",data_from_facilities.getString("name"));
                    json.put("price",data_from_facilities.getString("price"));
                    json.put("start_date",format.format(start_date_data));
                    json.put("end_date",format.format(end_date_data));

                    tour_pack_array.add(json);
                    if(tour_pack_array.size() == 0)
                        tour_pack_adapter.notifyDataSetChanged();
                    else
                        tour_pack_adapter.notifyItemInserted(tour_pack_array.size());
                    check_tour_packages();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == EDIT_TOUR_PACKAGE){
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    JSONObject data_from_facilities = new JSONObject(data.getStringExtra("data"));

                    JSONObject json = new JSONObject();
                    if(!data_from_facilities.getBoolean("is_link_image")) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(data_from_facilities.getString("image")));
                        json.put("image", bitmap);
                        json.put("image_data", data_from_facilities.getString("image"));
                    }
                    else{
                        json.put("image", data_from_facilities.getString("image"));
                        json.put("image_data", data_from_facilities.getString("image"));
                        json.put("id",data_from_facilities.getString("id"));
                    }
                    json.put("is_link_image", data_from_facilities.getBoolean("is_link_image"));
                    json.put("name",data_from_facilities.getString("name"));
                    json.put("price",data_from_facilities.getString("price"));
                    json.put("start_date",format.format(start_date_data));
                    json.put("end_date",format.format(end_date_data));

                    tour_pack_array.set(selected_tour_package,json);
                    tour_pack_adapter.notifyItemChanged(selected_tour_package);
                    check_tour_packages();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode==PICK_FROM_CAMERA){
                filePath = data.getData();
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");

                JSONObject json = new JSONObject();
                try {
                    json.put("status", "add");
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
                        json.put("status", "add");
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
            else if(requestCode == START_DATE || requestCode == END_DATE){
                try {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");

                    JSONArray json = new JSONArray(data.getStringExtra("date"));

                    start_date_data = new Date(json.getLong(0));
                    end_date_data = new Date(json.getLong(json.length() - 1));

                    showDate(simpledateformat.format(start_date_data), "start");
                    showDate(simpledateformat.format(end_date_data), "end");

                    for(int x=0;x<tour_pack_array.size();x++){
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        JSONObject jsonObject = tour_pack_array.get(x);

                        jsonObject.put("start_date",format.format(start_date_data));
                        jsonObject.put("end_date",format.format(end_date_data));

                        tour_pack_array.set(x,jsonObject);
                        tour_pack_adapter.notifyItemChanged(x);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDate(String date, String type) {
        if(type == "start")
            start_date.setText(date);
        else
            end_date.setText(date);
    }

    private void createTrip(){
        try {
            url = C_URL+"trip";

            JSONObject json = new JSONObject();

            JSONArray array_background = new JSONArray(photo_array.toString());
            JSONArray array_trip_pack = new JSONArray(tour_pack_array.toString());
            JSONArray array_keyword = new JSONArray(keyword_array.toString());

            JSONArray array_facilities = new JSONArray();
            int counter = 0;
            for(int x=0;x<array.size();x++){
                if(array.get(x).getBoolean("checked")) {
                    array_facilities.put(counter, array.get(x));
                    counter++;
                }
            }

            if(array_background.length() == 1){
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_photo_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(tripName.getText().toString().equals("")){
                tripName.setError(getResources().getString(R.string.error_name_label));
                name_layout.setError(getResources().getString(R.string.error_name_label));
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.error_name_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(start_date.getText().toString().equals("")){
                start_date_layout.setError(getResources().getString(R.string.tour_create_error_start_date_label));
                start_date.setError(getResources().getString(R.string.tour_create_error_start_date_label));
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_start_date_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(end_date.getText().toString().equals("")){
                end_date_layout.setError(getResources().getString(R.string.tour_create_error_end_date_label));
                end_date.setError(getResources().getString(R.string.tour_create_error_end_date_label));
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_end_date_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(tripDesc.getText().toString().equals("")){
                description_layout.setError(getResources().getString(R.string.tour_create_error_description_label));
                tripDesc.setError(getResources().getString(R.string.tour_create_error_description_label));
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_description_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(location.getText().toString().equals("")){
                location_layout.setError(getResources().getString(R.string.tour_create_error_location_label));
                location.setError(getResources().getString(R.string.tour_create_error_location_label));
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_location_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(array_facilities.length() == 0){
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_tour_facilities_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(array_trip_pack.length() == 0){
                Snackbar snackbar=Snackbar.make(layout,getResources().getString(R.string.tour_create_error_tour_package_label),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                for(int x=0;x<array_trip_pack.length();x++){
                    if(!array_trip_pack.getJSONObject(x).getBoolean("is_link_image")) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(array_trip_pack.getJSONObject(x).getString("image_data")));
                        array_trip_pack.getJSONObject(x).put("image", BitMapToString(bitmap,100));
                    }
                }
                json.put("background", array_background);
                json.put("tripPack", array_trip_pack);
                json.put("facilities", array_facilities);
                json.put("keyword", array_keyword);
                json.put("trip_name", tripName.getText());
                json.put("trip_desc", tripDesc.getText());
                json.put("start_date", start_date.getText());
                json.put("end_date", end_date.getText());
                json.put("location_id", location_id);

                if(intent.getStringExtra("type").equals("edit")){
                    json.put("_method", "put");
                    json.put("id", intent.getStringExtra("id"));
                }
//            logLargeString(json.toString());

//                Log.d("trippack", json.toString());


                final ProgressDialog loading = new ProgressDialog(this);
                loading.setMax(100);
                if(intent.getStringExtra("type").equals("edit"))
                    loading.setTitle(getResources().getString(R.string.manifest_editing_package_title));
                else
                    loading.setTitle(getResources().getString(R.string.manifest_creating_package_title));
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.setCancelable(false);
                loading.setProgress(0);
                loading.show();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("data",response.toString());
                            loading.dismiss();
                            if (response.getString("status").equals("success")) {
                                onBackPressed();
                            }
                            else{
                                logLargeString(response.toString());
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
                }) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trip_data(String trip_id){
        url = C_URL+"trip/detail?id="+trip_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonTrip = response.getJSONObject("trip");

                    tripName.setText(jsonTrip.getString("name"));
                    tripDesc.setText(jsonTrip.getString("description"));
                    start_date.setText(jsonTrip.getString("start_date_trip"));
                    end_date.setText(jsonTrip.getString("end_date_trip"));
                    location.setText(jsonTrip.getJSONObject("city").getString("name"));
                    location_id = jsonTrip.getJSONObject("city").getString("id");

                    String[] start = jsonTrip.getString("start_date_trip").split("/");
                    String[] end = jsonTrip.getString("end_date_trip").split("/");
                    start_date_data = new Date(Integer.parseInt(start[2])-1900,Integer.parseInt(start[1])-1,Integer.parseInt(start[0]));
                    end_date_data = new Date(Integer.parseInt(end[2])-1900,Integer.parseInt(end[1])-1,Integer.parseInt(end[0]));

                    JSONArray jsonPhoto = jsonTrip.getJSONArray("photo");
                    JSONArray jsonTripPack = jsonTrip.getJSONArray("trip_pack");
                    JSONArray jsonTripFacilities = jsonTrip.getJSONArray("facilities");
                    final JSONArray jsonKeyword = jsonTrip.getJSONArray("keyword");

                    for(int x=0;x<jsonPhoto.length();x++){
                        JSONObject json = new JSONObject();

                        json.put("status", "edit");
                        urlPhoto = C_URL_IMAGES+"trip?image="
                                +jsonPhoto.getJSONObject(x).getString("urlPhoto")
                                +"&mime="+jsonPhoto.getJSONObject(x).getString("mimePhoto");

                        json.put("name_image", jsonPhoto.getJSONObject(x).getString("urlPhoto"));
                        json.put("background",urlPhoto);
                        json.put("is_button_upload",false);

                        photo_array.add(json);
                        photo_adapter.notifyItemInserted(photo_array.size());

                    }

                    for(int x=0;x<jsonTripPack.length();x++){
                        JSONObject jsonObject = new JSONObject();

                        urlPhoto = C_URL_IMAGES+"trip-pack?image="
                                +jsonTripPack.getJSONObject(x).getString("urlPhoto")
                                +"&mime="+jsonTripPack.getJSONObject(x).getString("mimePhoto");

                        jsonObject.put("status", "edit");
                        jsonObject.put("id", jsonTripPack.getJSONObject(x).getString("id"));
                        jsonObject.put("image", urlPhoto);
                        jsonObject.put("is_link_image", true);

                        jsonObject.put("name",jsonTripPack.getJSONObject(x).getString("name"));
                        jsonObject.put("price",jsonTripPack.getJSONObject(x).getString("price"));
                        jsonObject.put("start_date",jsonTrip.getString("start_date_trip"));
                        jsonObject.put("end_date",jsonTrip.getString("end_date_trip"));

                        tour_pack_array.add(jsonObject);
                        tour_pack_adapter.notifyDataSetChanged();
                        check_tour_packages();
                    }

                    for(int x=0;x<jsonTripFacilities.length();x++) {
                        for(int y=0; y<array.size();y++){
                            if(array.get(y).getString("id").equals(jsonTripFacilities.getJSONObject(x).getString("facilities_id"))){
                                array.get(y).put("checked", true);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();

                    for(int x=0;x<jsonKeyword.length();x++) {
                        final View view = LayoutInflater.from(TourCreate.this).inflate(R.layout.chip_with_exit,null);
                        Chip chip = (Chip) view.findViewById(R.id.chip);
                        chip.setText(jsonKeyword.getJSONObject(x).getString("name"));
                        keyword_chip_layout.addView(view);
                        keyword_array.add(jsonKeyword.getJSONObject(x).getString("name"));

                        final int finalX = x;
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    keyword_chip_layout.removeView(view);
                                    int index=0;
                                    for(int x=0;x<keyword_array.size();x++) {
                                        if (keyword_array.get(x).equals(jsonKeyword.getJSONObject(finalX).getString("name"))) {
                                            index = x;
                                            break;
                                        }
                                    }
                                    keyword_array.remove(index);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                String message="";
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

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("LOG", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("LOG", str); // continuation
        }
    }

}
