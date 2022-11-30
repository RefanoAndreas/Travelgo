package com.qreatiq.travelgo;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.shawnlin.numberpicker.NumberPicker;

public class TamuHotel extends BottomSheetDialogFragment {

    NumberPicker guest,room;
    HotelSearch parent;
    MaterialButton submit;
    int guest_data = 1, room_data = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tamu_hotel, container, false);
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

        parent = (HotelSearch) getActivity();

        guest = (NumberPicker) view.findViewById(R.id.guest);
        room = (NumberPicker) view.findViewById(R.id.room);
        submit = (MaterialButton) view.findViewById(R.id.submit);

        guest_data = parent.guest;
        room_data = parent.room;

        guest.setValue(parent.guest);
        room.setValue(parent.room);

        guest.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                guest_data = newVal;
            }
        });

        room.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                room_data = newVal;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.guest = guest_data;
                parent.room = room_data;

                parent.guest_label.setText(String.valueOf(parent.guest)+" "+getResources().getString(R.string.guest_label));
                parent.room_label.setText(String.valueOf(parent.room)+" "+getResources().getString(R.string.room_label));

                dismiss();
            }
        });
    }
}
