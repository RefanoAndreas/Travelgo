package com.qreatiq.travelgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import org.json.JSONException;

public class RatingCityDetailModalFragment extends BottomSheetDialogFragment {

    MaterialButton submit;
    RatingBar rating;
    CityDetail parent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rating_city_detail_modal_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (CityDetail) getActivity();

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        submit = (MaterialButton) view.findViewById(R.id.submit);
        rating = (RatingBar) view.findViewById(R.id.rating);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("data", String.valueOf(rating.getRating()));
                dismiss();
//                try {
//                    parent.submit_rating(rating.getRating());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }
}
