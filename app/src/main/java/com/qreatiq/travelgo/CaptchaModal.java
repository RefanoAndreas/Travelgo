package com.qreatiq.travelgo;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class CaptchaModal extends DialogFragment {
    ImageView captcha;
    TextInputEditText captcha_edit;
    MaterialButton submit;
    FlightSearchJadwal parent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.captcha_modal,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        parent = (FlightSearchJadwal) getActivity();

        captcha = (ImageView) view.findViewById(R.id.captcha);
        captcha_edit = (TextInputEditText) view.findViewById(R.id.captcha_edit);
        submit = (MaterialButton) view.findViewById(R.id.submit);

        Picasso.get()
                .load(parent.url_captcha)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(captcha);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.captcha_input = captcha_edit.getText().toString();
                try {
                    parent.flightData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }
}
