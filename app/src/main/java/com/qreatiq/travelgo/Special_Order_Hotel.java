package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class Special_Order_Hotel extends DialogFragment {

    GridView specialOrder;
    ArrayList<String> arrayOrder = new ArrayList<String>();
    SpecialOrderHotelAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.special_order_hotel, container, false);

        specialOrder = (GridView)v.findViewById(R.id.GV_specialOrder);

        arrayOrder.add("Kamar Bebas Rokok");
        arrayOrder.add("Check-in Terlambat");
        arrayOrder.add("Check-in lebih cepat");
        arrayOrder.add("Kasur Extra");
        arrayOrder.add("Lantai Atas");

        adapter = new SpecialOrderHotelAdapter(arrayOrder, v.getContext());
        specialOrder.setAdapter(adapter);

        return v;
    }
}
