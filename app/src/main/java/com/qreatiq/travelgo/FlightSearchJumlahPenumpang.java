package com.qreatiq.travelgo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.shawnlin.numberpicker.NumberPicker;

public class FlightSearchJumlahPenumpang extends BottomSheetDialogFragment {

    NumberPicker adult,child,infant;
    FlightSearch parent;
    MaterialButton submit;
    CoordinatorLayout layout;
    int adult_data = 1, child_data = 0, infant_data = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.flight_search_jumlah_penumpang, container, false);
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

        parent = (FlightSearch) getActivity();

        adult = (NumberPicker) view.findViewById(R.id.adult);
        child = (NumberPicker) view.findViewById(R.id.child);
        infant = (NumberPicker) view.findViewById(R.id.infant);
        submit = (MaterialButton) view.findViewById(R.id.submit);
        layout = (CoordinatorLayout) view.findViewById(R.id.layout);

        adult_data = parent.adult;
        child_data = parent.child;
        infant_data = parent.infant;

        adult.setValue(parent.adult);
        child.setValue(parent.child);
        infant.setValue(parent.infant);

        adult.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                adult_data = newVal;

                if(newVal == 9)
                    child.setMaxValue(0);
                else if(newVal == 8)
                    child.setMaxValue(1);
                else if(newVal == 7)
                    child.setMaxValue(2);
                else if(newVal == 6)
                    child.setMaxValue(3);
                else if(newVal == 5)
                    child.setMaxValue(4);
                else if(newVal == 4)
                    child.setMaxValue(5);
                else if(newVal == 3)
                    child.setMaxValue(6);
                else if(newVal == 2)
                    child.setMaxValue(7);
                else if(newVal == 1)
                    child.setMaxValue(8);
            }
        });

        child.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                child_data = newVal;
            }
        });

        infant.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                infant_data = newVal;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adult_data >= infant_data) {
                    parent.adult = adult_data;
                    parent.child = child_data;
                    parent.infant = infant_data;

                    parent.adult_label.setText(String.valueOf(parent.adult) + " " + getResources().getString(R.string.adult_label));
                    parent.child_label.setText(String.valueOf(parent.child) + " " + getResources().getString(R.string.child_label));
                    parent.infant_label.setText(String.valueOf(parent.infant) + " " + getResources().getString(R.string.infant_label));

                    dismiss();
                }
                else{
                    adult.setValue(parent.adult);
                    child.setValue(parent.child);
                    infant.setValue(parent.infant);

                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_adult_infant_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
