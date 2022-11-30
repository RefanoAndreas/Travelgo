package com.qreatiq.travelgo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Special_Order_Hotel extends DialogFragment {

    GridView specialOrder;
    TextView title;

    ArrayList<JSONObject> arrayOrder = new ArrayList<JSONObject>();
    SpecialOrderHotelAdapter adapter;
    MaterialButton submit;
    ConfirmationOrder parent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.special_order_hotel, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (ConfirmationOrder) getActivity();
        specialOrder = (GridView) view.findViewById(R.id.GV_specialOrder);
        submit = (MaterialButton) view.findViewById(R.id.submit);
        title = view.findViewById(R.id.title);

        title.setText(getString(R.string.confirmation_special_request_title));

        for(int x=0;x<parent.special_request_array.size();x++){
            arrayOrder.add(parent.special_request_array.get(x));
        }

        adapter = new SpecialOrderHotelAdapter(arrayOrder, view.getContext());
        specialOrder.setAdapter(adapter);

        adapter.setOnItemClickListener(new SpecialOrderHotelAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, boolean checked) {
                try {
                    arrayOrder.get(position).put("checked",checked);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    parent.special_request_array.clear();
                    parent.special_request.removeAllViews();
                    for(int x=0;x<arrayOrder.size();x++){
                        if(arrayOrder.get(x).getBoolean("checked")){
                            TextView textView = new TextView(view.getContext());
                            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            textView.setText(arrayOrder.get(x).getString("label"));
                            parent.special_request.addView(textView);
                        }
                        parent.special_request_array.add(arrayOrder.get(x));
                    }
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
