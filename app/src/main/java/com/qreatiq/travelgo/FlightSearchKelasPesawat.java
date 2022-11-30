package com.qreatiq.travelgo;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.shawnlin.numberpicker.NumberPicker;

public class FlightSearchKelasPesawat extends BottomSheetDialogFragment {

    String[] data = new String[3];
    MaterialButton submit;
    NumberPicker numberPicker;
    FlightSearch parent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.flight_search_kelas_pesawat, container, false);

        submit = (MaterialButton) v.findViewById(R.id.submit);
        parent = (FlightSearch) getActivity();

        data[0] = getResources().getString(R.string.flight_search_class_economy_label);
        data[1] = getResources().getString(R.string.flight_search_class_business_label);
        data[2] = getResources().getString(R.string.flight_search_class_executive_label);

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
                        getDialog().findViewById(R.id.design_bottom_sheet);
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
