package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentProfile extends Fragment {

    ConstraintLayout btnLogout, btnEdtProfile, btnTourProfile, btnHistoryTransaction, btnListPackage;
    String userID, url;
    SharedPreferences user_ID;
    TextView name;
    RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (TextView)getActivity().findViewById(R.id.username);
        requestQueue = Volley.newRequestQueue(getActivity());

        user_ID = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = user_ID.getString("user_id", "Data not found");

        FacebookSdk.getApplicationContext();

        btnLogout = (ConstraintLayout) getActivity().findViewById(R.id.logoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = user_ID.edit();
                editor.clear().commit();

                FacebookSdk.sdkInitialize(getActivity());
                LoginManager.getInstance().logOut();

                startActivity(new Intent(getActivity(), LogIn.class));
                getActivity().finish();
            }
        });

        btnEdtProfile = (ConstraintLayout)getActivity().findViewById(R.id.editProfileBtn);
        btnEdtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileEdit.class));
            }
        });

        btnTourProfile = (ConstraintLayout)getActivity().findViewById(R.id.tourProfileBtn);
        btnTourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TourEdit.class));
            }
        });

        btnHistoryTransaction = (ConstraintLayout)getActivity().findViewById(R.id.historyTransactionBtn);
        btnHistoryTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryTransaction.class));
            }
        });

        btnListPackage = (ConstraintLayout)getActivity().findViewById(R.id.listPackageBtn);
        btnListPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TourList.class));
            }
        });

        getUserInfo();

    }

    private void getUserInfo(){
        url = link.C_URL+"profile.php?id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("user")){
                        name.setText(response.getJSONObject("user").getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
