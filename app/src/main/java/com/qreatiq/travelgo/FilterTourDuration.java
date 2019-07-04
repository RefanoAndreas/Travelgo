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
import android.widget.CompoundButton;

import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

public class FilterTourDuration extends BottomSheetDialogFragment {

    String[] data = new String[4];
    MaterialButton submit;
    int duration = 1;
    FilterTour parent;
    NumberPicker duration_tour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_tour_duration, container, false);

        data[0] = "1 "+getResources().getString(R.string.day_label);
        data[1] = "2 "+getResources().getString(R.string.day_label);
        data[2] = "3 "+getResources().getString(R.string.day_label);
        data[3] = "4 "+getResources().getString(R.string.day_label);

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
                try {
                    int counter = 0;
                    for(int x=0;x<parent.time_range_array.size();x++){
                        if(parent.time_range_array.get(x).getInt("data") == duration) {
                            parent.time_range_array.get(x).put("checked", true);
                            for(int y=0;y<parent.time_range.getChildCount();y++){
                                View view = parent.time_range.getChildAt(y);
                                Chip chip = (Chip) view.findViewById(R.id.chip);
                                if(chip.getText().toString().equals(parent.time_range_array.get(x).getString("label"))) {
                                    chip.setChecked(true);
                                    break;
                                }
                            }
                            break;
                        }
                        else
                            counter++;
                    }
                    if(counter == parent.time_range_array.size()){
                        JSONObject json = new JSONObject();
                        json.put("label",duration+" "+getResources().getString(R.string.day_label)+" "+(duration-1)+" "+getResources().getString(R.string.night_label));
                        json.put("checked", true);
                        json.put("data",duration);
                        parent.time_range_array.add(json);

                        final View view = LayoutInflater.from(parent).inflate(R.layout.chip_loc_filter,null);
                        final Chip chip = (Chip) view.findViewById(R.id.chip);
                        chip.setText(json.getString("label"));
                        chip.setChecked(json.getBoolean("checked"));
                        parent.time_range.addView(view);

                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                try {
                                    for(int x=0;x<parent.time_range_array.size();x++) {
                                        if (chip.getText().toString().equals(parent.time_range_array.get(x).getString("label"))) {
                                            parent.time_range_array.get(x).put("checked", isChecked);
                                            break;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
