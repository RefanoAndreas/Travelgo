package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

public class FilterTourDuration extends BottomSheetDialogFragment {

    String[] data = {"1 Hari", "2 Hari", "3 Hari", "4 Hari"};
    MaterialButton submit;
    int duration = 1;
    FilterTour parent;
    NumberPicker duration_tour;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (FilterTour)getActivity();

        submit = (MaterialButton)view.findViewById(R.id.save_button);
        duration_tour = (NumberPicker)view.findViewById(R.id.tour_duration_np1);

        duration = parent.duration;

        duration_tour.setValue(parent.duration);

        duration_tour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                duration = newVal;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.duration = duration;

//                parent.time_range.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(parent);
                View chip = inflater.inflate(R.layout.chip_loc_filter,null);
                Chip chip1 = (Chip)chip.findViewById(R.id.chip);
                chip1.setText(duration+" Hari "+(duration - 1)+" Malam");
                chip1.setChecked(true);
                parent.time_range.addView(chip);

//                parent.adult_label.setText(String.valueOf(parent.adult)+" Dewasa");
                Log.d("duration", String.valueOf(parent.duration));
                dismiss();
            }
        });

    }
}
