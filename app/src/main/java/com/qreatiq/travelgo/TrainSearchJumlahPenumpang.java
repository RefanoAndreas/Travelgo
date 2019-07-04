package com.qreatiq.travelgo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shawnlin.numberpicker.NumberPicker;

public class TrainSearchJumlahPenumpang extends BottomSheetDialogFragment {

    NumberPicker adult,infant;
    TrainSearch parent;
    MaterialButton submit;
    CoordinatorLayout layout;
    int adult_data = 1, infant_data = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.train_search_jumlah_penumpang, container, false);
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

        parent = (TrainSearch) getActivity();

        adult = (NumberPicker) view.findViewById(R.id.adult);
        infant = (NumberPicker) view.findViewById(R.id.infant);
        submit = (MaterialButton) view.findViewById(R.id.submit);
        layout = (CoordinatorLayout) view.findViewById(R.id.layout);

        adult_data = parent.adult;
        infant_data = parent.infant;

        adult.setValue(parent.adult);
        infant.setValue(parent.infant);

        adult.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                adult_data = newVal;
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
                    parent.infant = infant_data;

                    parent.adult_label.setText(parent.adult + " " + getResources().getString(R.string.adult_label));
                    parent.infant_label.setText(parent.infant + " " + getResources().getString(R.string.infant_label));

                    dismiss();
                }
                else{
                    adult.setValue(parent.adult);
                    infant.setValue(parent.infant);

                    Snackbar snackbar = Snackbar.make(layout,getResources().getString(R.string.data_penumpang_error_adult_infant_title),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
