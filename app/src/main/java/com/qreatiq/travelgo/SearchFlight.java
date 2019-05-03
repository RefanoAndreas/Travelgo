package com.qreatiq.travelgo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        try {
            array.add(new JSONObject("{\"city\":\"Yogyakarta\",\"poi\":\"Bandara Internasional Adisucipto\"}"));
            array.add(new JSONObject("{\"city\":\"Surabaya\",\"poi\":\"Bandara Internasional Juanda\"}"));
            array.add(new JSONObject("{\"city\":\"Jakarta\",\"poi\":\"Bandara Internasional Soekarno-Hatta\"}"));
            array.add(new JSONObject("{\"city\":\"Bali\",\"poi\":\"Bandara Internasional Ngurah Rai\"}"));

            arrayTemp.add(new JSONObject("{\"city\":\"Yogyakarta\",\"poi\":\"Bandara Internasional Adisucipto\"}"));
            arrayTemp.add(new JSONObject("{\"city\":\"Surabaya\",\"poi\":\"Bandara Internasional Juanda\"}"));
            arrayTemp.add(new JSONObject("{\"city\":\"Jakarta\",\"poi\":\"Bandara Internasional Soekarno-Hatta\"}"));
            arrayTemp.add(new JSONObject("{\"city\":\"Bali\",\"poi\":\"Bandara Internasional Ngurah Rai\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tour_search = (EditText) findViewById(R.id.tour_search);
        headline = (LinearLayout) findViewById(R.id.headline);
        list = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        adapter = new SearchProductAdapter(array);
        list.setAdapter(adapter);

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
                                if (!array.get(x).getString("city").contains(s)) {
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
