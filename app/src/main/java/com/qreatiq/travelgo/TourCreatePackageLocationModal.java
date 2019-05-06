package com.qreatiq.travelgo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourCreatePackageLocationModal extends DialogFragment {

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> arrayTemp = new ArrayList<String>();
    RecyclerView list;
    TourCreatePackageLocationAdapter adapter;
    Context context;

    TextInputEditText search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_package_location_modal,container,false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = view.findViewById(R.id.list);
        search = view.findViewById(R.id.search);

        arrayList.add("Bali");
        arrayList.add("Surabaya");
        arrayList.add("Malang");
        arrayList.add("Medan");
        arrayList.add("Makassar");

        adapter = new TourCreatePackageLocationAdapter(arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        for(int x=0;x<arrayList.size();x++)
            arrayTemp.add(arrayList.get(x));

        adapter.setOnItemClickListner(new TourCreatePackageLocationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                TourCreate parent = (TourCreate) getActivity();
                parent.location.setText(arrayList.get(position));
                dismiss();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Boolean flag = false;
                arrayList.clear();
                adapter.notifyDataSetChanged();
                for (int x = 0; x < arrayTemp.size(); x++) {
                    arrayList.add(arrayTemp.get(x));
                    adapter.notifyItemInserted(x);
                }
                while (!flag) {
                    flag = true;
                    for (int x = 0; x < arrayList.size(); x++) {
                        if (!arrayList.get(x).toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayList.remove(x);
                            adapter.notifyItemRemoved(x);
                            adapter.notifyItemRangeChanged(x, arrayList.size());
                            flag = false;
                            break;
                        }
                    }
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
