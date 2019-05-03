package com.qreatiq.travelgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

public class TourCreatePackage extends BaseActivity {

    TextInputEditText name,price;
    TextInputLayout name_layout,price_layout;
    ImageView image;
    Bitmap bitmap;

    Uri filePath;

    int PICK_FROM_GALLERY = 2, PICK_FROM_CAMERA = 3;
    Double price_data= Double.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_create_package);

        link.setToolbar(this);

        name = (TextInputEditText) findViewById(R.id.name);
        price = (TextInputEditText) findViewById(R.id.price);
        name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        price_layout = (TextInputLayout) findViewById(R.id.price_layout);
        image = (ImageView) findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_media_picker();
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            boolean isManualChange = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isManualChange) {
//                    isManualChange = false;
                    return;
                }
                String input = s.toString().replace("Rp. ","");

                if(!input.equals("")) {


                    if(input.charAt(0) == '0')
                        input = input.substring(0);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String yourFormattedString = formatter.format(Double.parseDouble(input.replace(".", "")));
                    isManualChange = true;
                    price.setText(yourFormattedString);
                    price.setSelection(yourFormattedString.length());
                    price_data = Double.parseDouble(input.replace(".", ""));
//                Log.d("data",yourFormattedString);
                }
                else{
                    isManualChange = true;
                    price.setText("0");
                    price.setSelection(1);
                    price_data = Double.valueOf(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                isManualChange = true;
                price.setText("Rp. "+price.getText().toString());
                price.setSelection(price.getText().toString().length());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode==PICK_FROM_CAMERA){
                filePath = data.getData();
                bitmap=(Bitmap) data.getExtras().get("data");

                image.setImageBitmap(bitmap);
                bottomSheetDialog.hide();
            }
            else if(requestCode==PICK_FROM_GALLERY){

                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
                bottomSheetDialog.hide();
            }
        }
    }

    public void submit(View v){
        if(name.getText().toString().equals(""))
            name_layout.setError("Name is empty");
        else if(price.getText().toString().equals(""))
            price_layout.setError("Price is empty");
        else {
            JSONObject json = new JSONObject();
            try {
                if(bitmap != null)
                    json.put("image", link.BitMapToString(bitmap));
                json.put("name", name.getText().toString());
                json.put("price", price_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent in = new Intent();
            in.putExtra("data", json.toString());
            setResult(RESULT_OK, in);
            finish();
        }
    }
}
