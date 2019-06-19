package com.qreatiq.travelgo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shawnlin.numberpicker.NumberPicker;

public class TrainSearchKelas extends BottomSheetDialogFragment {

    String[] data = {"Ekonomi", "Eksekutif"};
    MaterialButton submit;
    NumberPicker numberPicker;
    TrainSearch parent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.train_search_kelas, container, false);

        submit = (MaterialButton) v.findViewById(R.id.submit);
        parent = (TrainSearch) getActivity();

        numberPicker = (NumberPicker) v.findViewById(R.id.flightSearch_kelaspesawat_np1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        for(int x=0;x<data.length;x++){
            if(data[x] == parent.kelas.getText()){
                numberPicker.setValue(x+1);
                break;
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.kelas_data = data[numberPicker.getValue()-1];
                parent.kelas.setText(data[numberPicker.getValue()-1]);
                dismiss();
            }
        });

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = (FrameLayout)
                        getDialog().findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
