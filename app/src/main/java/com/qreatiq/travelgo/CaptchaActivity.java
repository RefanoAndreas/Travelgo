package com.qreatiq.travelgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.qreatiq.travelgo.Utils.BaseActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class CaptchaActivity extends BaseActivity {

    ImageView captcha;
    TextInputEditText captcha_edit;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
        set_toolbar();

        captcha = (ImageView) findViewById(R.id.captcha);
        captcha_edit = (TextInputEditText) findViewById(R.id.captcha_edit);
        submit = (MaterialButton) findViewById(R.id.submit);

        Picasso.get()
                .load(getIntent().getStringExtra("url_captcha"))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(captcha);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.putExtra("captcha",captcha_edit.getText().toString());
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }
}
