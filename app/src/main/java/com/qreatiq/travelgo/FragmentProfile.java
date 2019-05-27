package com.qreatiq.travelgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentProfile extends Fragment {

    ConstraintLayout btnLogout, btnEdtProfile, btnTourProfile, btnHistoryTransaction, btnListPackage, btnLanguage, btnHistoryPurchasing;
    String url;
    TextView name, saldo_kawan;
    BottomNavContainer parent;

    TextView account_profile,tour_profile,list_package,history_purchasing,history_transaction,language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (BottomNavContainer) getActivity();
        parent.toolbar.setVisibility(View.VISIBLE);
        parent.toolbar.setTitle("Profil Saya");

        account_profile = (TextView) view.findViewById(R.id.account_profile);
        tour_profile = (TextView) view.findViewById(R.id.tour_profile);
        list_package = (TextView) view.findViewById(R.id.list_package);
        history_purchasing = (TextView) view.findViewById(R.id.history_purchasing);
        history_transaction = (TextView) view.findViewById(R.id.history_transaction);
        language = (TextView) view.findViewById(R.id.language);
        saldo_kawan = (TextView)view.findViewById(R.id.TV_saldo_kawan);

//        user_ID = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
//        userID = user_ID.getString("user_id", "Data not found");

        getApplicationContext();

        btnLogout = (ConstraintLayout) getActivity().findViewById(R.id.logoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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

        btnHistoryPurchasing = (ConstraintLayout)getActivity().findViewById(R.id.historyPurchasingBtn);
        btnHistoryPurchasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), D1Notifikasi.class).putExtra("data", "purchasing"));
            }
        });

        btnHistoryTransaction = (ConstraintLayout)getActivity().findViewById(R.id.historyTransactionBtn);
        btnHistoryTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), D1Notifikasi.class).putExtra("data", "sales"));
            }
        });

        btnListPackage = (ConstraintLayout)getActivity().findViewById(R.id.listPackageBtn);
        btnListPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TourList.class));
            }
        });

        btnLanguage = (ConstraintLayout)getActivity().findViewById(R.id.languageBtn);
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                View view = View.inflate(getActivity(), R.layout.change_language_fragment, null);
                alertDialog.setView(view);
                alertDialog.show();

                LinearLayout english = view.findViewById(R.id.english);
                LinearLayout indonesia = view.findViewById(R.id.indonesia);

                english.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = LocaleManager.setLocale(getActivity(), "en");
                        Resources resources = context.getResources();
                        account_profile.setText(resources.getString(R.string.profile_edit_profile_label));
                        tour_profile.setText(resources.getString(R.string.profile_tour_profile_label));
                        list_package.setText(resources.getString(R.string.profile_list_package_label));
                        history_purchasing.setText(resources.getString(R.string.profile_history_purchasing_label));
                        history_transaction.setText(resources.getString(R.string.profile_history_transaction_label));
                        language.setText(resources.getString(R.string.profile_language_label));
                        BottomNavContainer parent = (BottomNavContainer) getActivity();
                        parent.edit_base_shared_pref.putString("lang","en");
                        parent.edit_base_shared_pref.commit();
                        alertDialog.dismiss();
                    }
                });

                indonesia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = LocaleManager.setLocale(getActivity(), "in");
                        Resources resources = context.getResources();
                        account_profile.setText(resources.getString(R.string.profile_edit_profile_label));
                        tour_profile.setText(resources.getString(R.string.profile_tour_profile_label));
                        list_package.setText(resources.getString(R.string.profile_list_package_label));
                        history_purchasing.setText(resources.getString(R.string.profile_history_purchasing_label));
                        history_transaction.setText(resources.getString(R.string.profile_history_transaction_label));
                        language.setText(resources.getString(R.string.profile_language_label));

                        BottomNavContainer parent = (BottomNavContainer) getActivity();
                        parent.edit_base_shared_pref.putString("lang","in");
                        parent.edit_base_shared_pref.commit();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        user();

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }

    public void reload() {
        Intent intent = getActivity().getIntent();
        getActivity().overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void logout(){
        url = parent.C_URL+"logout";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", parent.tokenDevice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences.Editor editor = parent.base_shared_pref.edit();
                editor.clear().commit();

                FacebookSdk.sdkInitialize(getActivity());
                LoginManager.getInstance().logOut();

                startActivity(new Intent(getActivity(), BottomNavContainer.class));
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message="";
                if (error instanceof NetworkError) {
                    message="Network Error";
                }
                else if (error instanceof ServerError) {
                    message="Server Error";
                }
                else if (error instanceof AuthFailureError) {
                    message="Authentication Error";
                }
                else if (error instanceof ParseError) {
                    message="Parse Error";
                }
                else if (error instanceof NoConnectionError) {
                    message="Connection Missing";
                }
                else if (error instanceof TimeoutError) {
                    message="Server Timeout Reached";
                }
                Snackbar snackbar=Snackbar.make(parent.layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", parent.userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        parent.requestQueue.add(jsonObjectRequest);
    }

    private void user(){
        url = parent.C_URL+"profile";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(response.getJSONObject("user").getDouble("saldo_kawan"));
                    saldo_kawan.setText("Rp. "+formattedNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message="";
                if (error instanceof NetworkError) {
                    message="Network Error";
                }
                else if (error instanceof ServerError) {
                    message="Server Error";
                }
                else if (error instanceof AuthFailureError) {
                    message="Authentication Error";
                }
                else if (error instanceof ParseError) {
                    message="Parse Error";
                }
                else if (error instanceof NoConnectionError) {
                    message="Connection Missing";
                }
                else if (error instanceof TimeoutError) {
                    message="Server Timeout Reached";
                }
                Snackbar snackbar=Snackbar.make(parent.layout,message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", parent.userID);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        parent.requestQueue.add(jsonObjectRequest);
    }

}
