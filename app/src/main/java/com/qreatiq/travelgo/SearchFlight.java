package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFlight extends BaseActivity {

    RecyclerView list;
    SearchProductAdapter adapter;
    ArrayList<JSONObject> array = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayTemp = new ArrayList<JSONObject>();

    EditText tour_search;
    LinearLayout headline;

    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        in = getIntent();
        tour_search = (EditText) findViewById(R.id.tour_search);
        headline = (LinearLayout) findViewById(R.id.headline);
        list = (RecyclerView) findViewById(R.id.list);

        if(in.getStringExtra("type").equals("flight")){
            tour_search.setHint("Cari kota atau bandara");

            try {
                array.add(new JSONObject("{\"city_label\":\"Yogyakarta\",\"poi_label\":\"Bandara Internasional Adisucipto\",\"city\":\"yogyakarta\",\"poi\":\"bandara internasional adisucipto\"}"));
                array.add(new JSONObject("{\"city_label\":\"Surabaya\",\"poi_label\":\"Bandara Internasional Juanda\",\"city\":\"surabaya\",\"poi\":\"bandara internasional juanda\"}"));
                array.add(new JSONObject("{\"city_label\":\"Jakarta\",\"poi_label\":\"Bandara Internasional Soekarno-Hatta\",\"city\":\"jakarta\",\"poi\":\"bandara internasional soekarno-hatta\"}"));
                array.add(new JSONObject("{\"city_label\":\"Bali\",\"poi_label\":\"Bandara Internasional Ngurah Rai\",\"city\":\"bali\",\"poi\":\"bandara internasional ngurah rai\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(in.getStringExtra("type").equals("hotel")){
            tour_search.setHint("Cari kota atau hotel");

            try {
                array.add(new JSONObject("{\"city_label\":\"Surabaya\",\"poi_label\":\"Shangri-La Hotel\",\"city\":\"surabaya\",\"poi\":\"shangri-la hotel\"}"));
                array.add(new JSONObject("{\"city_label\":\"Surabaya\",\"poi_label\":\"J.W. Marriot\",\"city\":\"surabaya\",\"poi\":\"jw marriot\"}"));
                array.add(new JSONObject("{\"city_label\":\"Jakarta\",\"poi_label\":\"Shangri-La Hotel\",\"city\":\"jakarta\",\"poi\":\"shangri-la hotel\"}"));
                array.add(new JSONObject("{\"city_label\":\"Jakarta\",\"poi_label\":\"J.W. Marriot\",\"city\":\"jakarta\",\"poi\":\"jw marriot\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(in.getStringExtra("type").equals("train")){
            tour_search.setHint("Cari kota atau stasiun");

            try {
                array.add(new JSONObject("{\"city_label\":\"Surabaya\",\"poi_label\":\"Gubeng\",\"city\":\"surabaya\",\"poi\":\"gubeng\"}"));
                array.add(new JSONObject("{\"city_label\":\"Surabaya\",\"poi_label\":\"Pasar Turi\",\"city\":\"surabaya\",\"poi\":\"pasar turi\"}"));
                array.add(new JSONObject("{\"city_label\":\"Jakarta\",\"poi_label\":\"Gambir\",\"city\":\"jakarta\",\"poi\":\"gambir\"}"));
                array.add(new JSONObject("{\"city_label\":\"Jakarta\",\"poi_label\":\"Kota\",\"city\":\"jakarta\",\"poi\":\"kota\"}"));
                array.add(new JSONObject("{\"city_label\":\"Yogyakarta\",\"poi_label\":\"Lempuyangan\",\"city\":\"yogyakarta\",\"poi\":\"lempuyangan\"}"));
                array.add(new JSONObject("{\"city_label\":\"Yogyakarta\",\"poi_label\":\"Tugu\",\"city\":\"yogyakarta\",\"poi\":\"tugu\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int x=0;x<array.size();x++)
            arrayTemp.add(array.get(x));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        adapter = new SearchProductAdapter(array);
        list.setAdapter(adapter);

        adapter.setOnItemClickListner(new SearchProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent in = new Intent();
                in.putExtra("data", array.get(position).toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });

        ((ImageView) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tour_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    list.setVisibility(View.VISIBLE);
                    headline.setVisibility(View.GONE);

                    Boolean flag = false;
                    array.clear();
                    for (int x = 0; x < arrayTemp.size(); x++)
                        array.add(arrayTemp.get(x));
                    try {
                        while (!flag) {
                            flag = true;
                            for (int x = 0; x < array.size(); x++) {
                                if (!array.get(x).getString("poi").contains(s.toString().toLowerCase()) &&
                                        !array.get(x).getString("city").contains(s.toString().toLowerCase())) {
                                    array.remove(x);
//                                    adapter.notifyItemRemoved(x);
//                                    adapter.notifyItemRangeChanged(x, array.size());
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    list.setVisibility(View.GONE);
                    headline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
