package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sort extends BaseActivity {

    RecyclerView list;
    Intent intent;
    String intentString;
    String[] sortItem;
    ArrayList<JSONObject> array = new ArrayList<JSONObject>();

    JSONObject sort;

    SortAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        set_toolbar();

        intent = getIntent();
        intentString = intent.getStringExtra("origin");
        try {
            sort = new JSONObject(intent.getStringExtra("sort"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list = (RecyclerView) findViewById(R.id.list);

        if(intentString.equals("hotel")){
            sortItem = getResources().getStringArray(R.array.sort_hotel);
            try {
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_1_label)+"\",\"data\":\"popularity\",\"checked\":false}"));
//                array.add(new JSONObject("{\"label\":\"Bintang 5-1\",\"data\":\"rank_desc\",\"checked\":false}"));
//                array.add(new JSONObject("{\"label\":\"Bintang 1-5\",\"data\":\"rank_asc\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_2_label)+"\",\"data\":\"review_desc\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_3_label)+"\",\"data\":\"price_desc\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_4_label)+"\",\"data\":\"price_asc\",\"checked\":false}"));

                if(!sort.toString().equals("{}")){
                    for(int x=0;x<array.size();x++){
                        if(sort.getString("data").equals(array.get(x).getString("data"))){
                            array.get(x).put("checked",true);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            sortItem = getResources().getStringArray(R.array.sort_flight_train);
            try {
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_flight_1_label)+"\",\"data\":\"early_depart\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_flight_2_label)+"\",\"data\":\"late_depart\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_flight_3_label)+"\",\"data\":\"early_arrive\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_flight_4_label)+"\",\"data\":\"late_arrive\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_3_label)+"\",\"data\":\"price_desc\",\"checked\":false}"));
                array.add(new JSONObject("{\"label\":\""+getResources().getString(R.string.sort_item_hotel_4_label)+"\",\"data\":\"price_asc\",\"checked\":false}"));

                if(!sort.toString().equals("{}")){
                    for(int x=0;x<array.size();x++){
                        if(sort.getString("data").equals(array.get(x).getString("data"))){
                            array.get(x).put("checked",true);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new SortAdapter(array,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(adapter);

        adapter.setOnItemClickListner(new SortAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent();
                i.putExtra("sort", array.get(position).toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
