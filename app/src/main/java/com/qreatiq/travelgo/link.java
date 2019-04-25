package com.qreatiq.travelgo;

import android.app.Activity;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class link {

    public static String C_URL = "http://travelgo.propertigo.id/api/";
    public static String C_URL_IMAGES = "http://travelgo.propertigo.id/images/";

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
