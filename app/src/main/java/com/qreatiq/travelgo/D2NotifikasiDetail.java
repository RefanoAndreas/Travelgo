package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class D2NotifikasiDetail extends BaseActivity {

    LinearLayout eTicketLayout, eTicketLayout1;
    Intent i;
    String intentString, url;
    TextView TV_eTicket;

    RecyclerView RV_eTicket;
    ETicketAdapter adapter_eTicket;
    ArrayList<JSONObject> eTicketList = new ArrayList<JSONObject>();
    private RecyclerView.LayoutManager layoutManager_eTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2_notifikasi_detail);

        set_toolbar();

//        eTicketLayout = (LinearLayout)findViewById(R.id.eTicketLayout);
//        eTicketLayout1 = (LinearLayout)findViewById(R.id.eTicketLayout1);
//        TV_eTicket = (TextView)findViewById(R.id.TV_eTicket);

        i = getIntent();
        intentString = i.getStringExtra("info");

        RV_eTicket = findViewById(R.id.RV_eTicket);
        RV_eTicket.setHasFixedSize(true);
        layoutManager_eTicket = new LinearLayoutManager(this);
        adapter_eTicket = new ETicketAdapter(eTicketList, this);

        RV_eTicket.setLayoutManager(layoutManager_eTicket);
        RV_eTicket.setAdapter(adapter_eTicket);

        adapter_eTicket.setOnItemClickListner(new ETicketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                    startActivity(new Intent(D2NotifikasiDetail.this, D3Eticket.class));
            }
        });

        eTicket();

    }

    private void eTicket(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("origin", "Surabaya");
            jsonObject.put("destination", "Jakarta");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        eTicketList.add(jsonObject);
        adapter_eTicket.notifyDataSetChanged();
    }
}
