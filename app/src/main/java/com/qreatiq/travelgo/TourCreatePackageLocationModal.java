package com.qreatiq.travelgo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TourCreatePackageLocationModal extends DialogFragment {

    ArrayList<String> arrayList = new ArrayList<String>();
    RecyclerView list;
    TourCreatePackageLocationAdapter adapter;
    Context context;

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

        arrayList.add("Bali");
        arrayList.add("Surabaya");
        arrayList.add("Malang");
        arrayList.add("Medan");
        arrayList.add("Makassar");

        adapter = new TourCreatePackageLocationAdapter(arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }
}
