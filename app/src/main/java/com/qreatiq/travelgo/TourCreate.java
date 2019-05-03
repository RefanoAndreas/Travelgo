package com.qreatiq.travelgo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TourCreate extends BaseActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year = 2019, month = 3, day = 10;

    private RecyclerView mRecyclerView_1, mRecyclerView_2;
    private RecyclerView.Adapter mAdapter_1;
    private RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;

    ArrayList<JSONObject> photo_array = new ArrayList<JSONObject>();
    TourCreatePhotosAdapter photo_adapter;

    ArrayList<JSONObject> tour_pack_array = new ArrayList<JSONObject>();
    TourCreateTourPackageAdapter tour_pack_adapter;

    ArrayList<String> array = new ArrayList<String>();
    TourCreateFacilitiesAdapter adapter;
    GridView grid;

    TextInputEditText start_date,end_date;
    Uri filePath;

    TextView no_data;

    int CREATE_TOUR_PACKAGE = 1, PICK_FROM_GALLERY = 2, PICK_FROM_CAMERA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create);

        link.setToolbar(this);

        grid = (GridView) findViewById(R.id.grid);
        start_date = (TextInputEditText) findViewById(R.id.start_date);
        end_date = (TextInputEditText) findViewById(R.id.end_date);
        no_data = (TextView) findViewById(R.id.no_data);

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

        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");
        array.add("Kolam Renang");

        adapter = new TourCreateFacilitiesAdapter(array,this);
        grid.setAdapter(adapter);
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



    public void startDate(View v){
        showDialog(999);
    }

    public void endDate(View v){
        showDialog(998);
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

    public void createTourPack(View v){
        startActivityForResult(new Intent(this,TourCreatePackage.class),CREATE_TOUR_PACKAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CREATE_TOUR_PACKAGE){
                try {
                    JSONObject data_from_facilities = new JSONObject(data.getStringExtra("data"));

                    JSONObject json = new JSONObject();
                    if(data_from_facilities.has("image"))
                        json.put("image",(Bitmap) link.StringToBitMap(data_from_facilities.getString("image")));
                    else
                        json.put("image", BitmapFactory.decodeResource(getResources(),R.drawable.background1));
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
}
