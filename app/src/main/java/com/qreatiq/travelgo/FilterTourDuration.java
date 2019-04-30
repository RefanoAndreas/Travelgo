package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

public class FilterTourDuration extends BottomSheetDialogFragment {

    String[] data = {"4 Hari", "3 Hari", "2 Hari"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_tour_duration, container, false);

        NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.tour_duration_np1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(2);
        return v;
    }
}
