package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmationOrder extends BaseActivity {

    TextView  isiDataPeserta, specialRequestAdd, guestData;
    View layout_infoHotel, layout_infoFlight, layout_infoTrain;
    Intent intent;
    String intentString;
    LinearLayout specialRequestLinear;
    RecyclerView list_pax;
    CardView card_dataPemesan;

    ConfirmationPaxAdapter adapter;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    int ADD_OR_EDIT_PAX = 10, ADD_OR_EDIT_GUEST = 11, selected_arr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);

        set_toolbar();

        specialRequestLinear = (LinearLayout)findViewById(R.id.specialRequestLinear);

        layout_infoHotel = (View) findViewById(R.id.infoHotelGuest);
        layout_infoFlight = (View)findViewById(R.id.infoFlight);
        layout_infoTrain = (View)findViewById(R.id.infoTrain);
        guestData = (TextView) findViewById(R.id.dataGuestTV);
        list_pax = (RecyclerView) findViewById(R.id.list_pax);
//        isiDataPeserta = (TextView)findViewById(R.id.isiDataPeserta);
        card_dataPemesan = (CardView)findViewById(R.id.data_pemesan_card);

        intent = getIntent();
        intentString = intent.getStringExtra("origin");



        if(intentString.equals("hotel")){
            try {
                arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            layout_infoHotel.setVisibility(View.VISIBLE);
            guestData.setText("Data Tamu");
//            isiDataPeserta.setText("Isi data tamu");
            card_dataPemesan.setVisibility(View.GONE);
        }
        else if(intentString.equals("flight")){
            try {
                for(int x=0;x<intent.getIntExtra("adult",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Dewasa "+(x+1)+"\"}"));
                for(int x=0;x<intent.getIntExtra("child",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Anak "+(x+1)+"\"}"));
                for(int x=0;x<intent.getIntExtra("infant",0);x++)
                    arrayList.add(new JSONObject("{\"edit\":false,\"title\":\"\",\"name\":\"\",\"label\":\"Isi Data Bayi "+(x+1)+"\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            layout_infoFlight.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
//            isiDataPeserta.setText("Isi data penumpang");
        }
        else{
            layout_infoTrain.setVisibility(View.VISIBLE);
            specialRequestLinear.setVisibility(View.GONE);
            guestData.setText("Data Penumpang");
//            isiDataPeserta.setText("Isi data penumpang");
        }

        adapter = new ConfirmationPaxAdapter(arrayList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list_pax.setLayoutManager(mLayoutManager);
        list_pax.setAdapter(adapter);

        specialRequestAdd = (TextView) findViewById(R.id.specialRequestAdd);
        specialRequestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new Special_Order_Hotel();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

        Log.d("array", arrayList.toString());

        adapter.setOnItemClickListener(new ConfirmationPaxAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                selected_arr = position;
                if(intentString.equals("flight"))
                    startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                            .putExtra("packageName",intentString)
                            .putExtra("data",arrayList.get(selected_arr).toString()),
                            ADD_OR_EDIT_PAX
                    );
                else if(intentString.equals("hotel"))
                    startActivityForResult(new Intent(ConfirmationOrder.this,DataPenumpang.class)
                                    .putExtra("packageName",intentString)
                                    .putExtra("data",arrayList.get(selected_arr).toString()),
                            ADD_OR_EDIT_GUEST
                    );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == ADD_OR_EDIT_PAX){

                JSONObject json = arrayList.get(selected_arr);
                try {
                    JSONObject data_from_intent = new JSONObject(data.getStringExtra("data"));
                    json.put("edit",true);
                    json.put("title",data_from_intent.getString("title"));
                    json.put("name",data_from_intent.getString("name"));
                    adapter.notifyItemChanged(selected_arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == ADD_OR_EDIT_GUEST){

                JSONObject json = arrayList.get(selected_arr);
                try {
                    JSONObject data_from_intent = new JSONObject(data.getStringExtra("data"));
                    json.put("edit",true);
                    json.put("title",data_from_intent.getString("title"));
                    json.put("name",data_from_intent.getString("name"));
                    adapter.notifyItemChanged(selected_arr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
