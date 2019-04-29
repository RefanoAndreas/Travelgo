package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

public class FlightSearchKelasPesawat extends BottomSheetDialogFragment {

    String[] data = {"Ekonomi", "Bisnis", "First Class"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.flight_search_kelas_pesawat, container, false);

        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.flightSearch_kelaspesawat_np1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(2);
        return v;
    }
}
