package com.qreatiq.travelgo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;

import com.qreatiq.travelgo.Utils.BaseActivity;

import java.util.ArrayList;

public class FilterTourLocation extends BaseActivity {

    GridView gridViewLocation;
    ArrayList<String> arrayLocation = new ArrayList<String>();
    ArrayList<String> arrayLocationTemp = new ArrayList<String>();
    FilterTourLocationAdapter adapter;

    EditText tour_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tour_location);

        set_toolbar();

        gridViewLocation = (GridView) findViewById(R.id.GV_selectLocation);
        tour_search = (EditText) findViewById(R.id.tour_search);

        arrayLocation.add("Ubud");
        arrayLocation.add("Kuta");
        arrayLocation.add("Jimbaran");
        arrayLocation.add("Legian");
        arrayLocation.add("Uluwatu");
        arrayLocation.add("Kintamani");
        arrayLocation.add("GWK");
        arrayLocation.add("Bedugul");
        arrayLocation.add("Denpasar");

        for(int x=0;x<arrayLocation.size();x++)
            arrayLocationTemp.add(arrayLocation.get(x));

        adapter = new FilterTourLocationAdapter(arrayLocation,this);
        gridViewLocation.setAdapter(adapter);

        tour_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Boolean flag = false;
                arrayLocation.clear();
                for (int x = 0; x < arrayLocationTemp.size(); x++)
                    arrayLocation.add(arrayLocationTemp.get(x));
                while (!flag) {
                    flag = true;
                    for (int x = 0; x < arrayLocation.size(); x++) {

                        if (!arrayLocation.get(x).toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayLocation.remove(x);
//                                    adapter.notifyItemRemoved(x);
//                                    adapter.notifyItemRangeChanged(x, array.size());
                            flag = false;
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
