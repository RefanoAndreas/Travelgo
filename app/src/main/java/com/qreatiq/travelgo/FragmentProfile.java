package com.qreatiq.travelgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class FragmentProfile extends Fragment {

    ConstraintLayout btnLogout, btnEdtProfile;
    String userID;
    SharedPreferences user_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FacebookSdk.getApplicationContext();

        btnLogout = (ConstraintLayout) getActivity().findViewById(R.id.logoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_ID = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = user_ID.edit();
                editor.clear().commit();

                FacebookSdk.sdkInitialize(getActivity());
                LoginManager.getInstance().logOut();

                startActivity(new Intent(getActivity(), LogIn.class));
            }
        });

        btnEdtProfile = (ConstraintLayout)getActivity().findViewById(R.id.editProfileBtn);
        btnEdtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileEdit.class));
            }
        });

    }

}
