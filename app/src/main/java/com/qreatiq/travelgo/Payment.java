package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.qreatiq.travelgo.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Payment extends BaseActivity implements TransactionFinishedCallback {

    Button buttonUiKit, buttonDirectCreditCard, buttonDirectBcaVa, buttonDirectMandiriVa,
            buttonDirectBniVa, buttonDirectAtmBersamaVa, buttonDirectPermataVa;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        layout = (ConstraintLayout) findViewById(R.id.layout);

        bindViews();
        // SDK initiation for UIflow
        initMidtransSdk();
        initActionButtons();
    }

    private TransactionRequest initTransactionRequest() {
        double gross_amount = 0;
        try {
            if(getIntent().getStringExtra("type").equals("tour")) {
                JSONArray json = new JSONArray(getIntent().getStringExtra("trip_pack"));
                for (int x = 0; x < json.length(); x++) {
                    if (json.getJSONObject(x).getInt("qty") > 0) {
                        gross_amount += json.getJSONObject(x).getDouble("price");
                    }
                }
            }
            else if(getIntent().getStringExtra("type").equals("flight") || getIntent().getStringExtra("type").equals("train")) {
                JSONObject json = new JSONObject(getIntent().getStringExtra("data"));
                gross_amount += json.getJSONObject("depart_ticket").getDouble("price");
                if(json.getBoolean("is_return"))
                    gross_amount += json.getJSONObject("return_ticket").getDouble("price");
            }
            else if(getIntent().getStringExtra("type").equals("hotel")) {
                JSONObject json = new JSONObject(getIntent().getStringExtra("data"));
                gross_amount += json.getJSONObject("room_selected").getDouble("price");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", gross_amount);

        //set customer details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());


        // set item details
//        ItemDetails itemDetails = new ItemDetails("1", 20000, 1, "Trekking Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        try {
            if(getIntent().getStringExtra("type").equals("tour")) {
                JSONArray json = new JSONArray(getIntent().getStringExtra("trip_pack"));
                for (int x = 0; x < json.length(); x++) {
                    if (json.getJSONObject(x).getInt("qty") > 0) {
                        itemDetailsArrayList.add(new ItemDetails(String.valueOf(x),
                                json.getJSONObject(x).getInt("price"),
                                json.getJSONObject(x).getInt("qty"),
                                json.getJSONObject(x).getString("name")));
                    }
                }
            }
            else if(getIntent().getStringExtra("type").equals("flight")) {
                JSONObject json = new JSONObject(getIntent().getStringExtra("data"));
                itemDetailsArrayList.add(new ItemDetails("1",
                        json.getJSONObject("depart_ticket").getInt("price"),
                        1,
                        json.getJSONObject("depart_ticket").getString("departAirport")+" > "+json.getJSONObject("depart_ticket").getString("arrivalAirport")));
                if(json.getBoolean("is_return"))
                    itemDetailsArrayList.add(new ItemDetails("2",
                            json.getJSONObject("return_ticket").getInt("price"),
                            1,
                            json.getJSONObject("return_ticket").getString("departAirport")+" > "+json.getJSONObject("return_ticket").getString("arrivalAirport")));
            }
            else if(getIntent().getStringExtra("type").equals("train")) {
                JSONObject json = new JSONObject(getIntent().getStringExtra("data"));
                itemDetailsArrayList.add(new ItemDetails("1",
                        json.getJSONObject("depart_ticket").getInt("price"),
                        1,
                        json.getJSONObject("depart_ticket").getString("departStation")+" > "+json.getJSONObject("depart_ticket").getString("arrivalStation")));
                if(json.getBoolean("is_return"))
                    itemDetailsArrayList.add(new ItemDetails("2",
                            json.getJSONObject("return_ticket").getInt("price"),
                            1,
                            json.getJSONObject("return_ticket").getString("departStation")+" > "+json.getJSONObject("return_ticket").getString("arrivalStation")));
            }
            else if(getIntent().getStringExtra("type").equals("hotel")) {
                JSONObject json = new JSONObject(getIntent().getStringExtra("data"));
                itemDetailsArrayList.add(new ItemDetails("1",
                        json.getJSONObject("room_selected").getInt("price"),
                        1,
                        json.getJSONObject("room_selected").getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);


        // Create creditcard options for payment
        CreditCard creditCard = new CreditCard();

        creditCard.setSaveCard(false); // when using one/two click set to true and if normal set to  false

//        this methode deprecated use setAuthentication instead
//        creditCard.setSecure(true); // when using one click must be true, for normal and two click (optional)

        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);

        // noted !! : channel migs is needed if bank type is BCA, BRI or MyBank
//        creditCard.setChannel(CreditCard.MIGS); //set channel migs
        creditCard.setBank(BankType.BCA); //set spesific acquiring bank

        transactionRequestNew.setCreditCard(creditCard);

        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("085310102020");
        mCustomerDetails.setFirstName("user fullname");
        mCustomerDetails.setEmail("mail@mail.com");
        return mCustomerDetails;
    }

    private void initMidtransSdk() {
        String client_key = SdkConfig.MERCHANT_CLIENT_KEY;
        String base_url = SdkConfig.MERCHANT_BASE_CHECKOUT_URL;

        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    set_transaction_reference(result.getResponse().getTransactionId(),"success");
                    break;
                case TransactionResult.STATUS_PENDING:
                    set_transaction_reference(result.getResponse().getTransactionId(),"pending");
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bindViews() {
        buttonUiKit = (Button) findViewById(R.id.button_uikit);
        buttonDirectCreditCard = (Button) findViewById(R.id.button_direct_credit_card);
        buttonDirectBcaVa = (Button) findViewById(R.id.button_direct_bca_va);
        buttonDirectMandiriVa = (Button) findViewById(R.id.button_direct_mandiri_va);
        buttonDirectBniVa = (Button) findViewById(R.id.button_direct_bni_va);
        buttonDirectPermataVa = (Button) findViewById(R.id.button_direct_permata_va);
        buttonDirectAtmBersamaVa = (Button) findViewById(R.id.button_direct_atm_bersama_va);

    }

    private void initActionButtons() {
        buttonUiKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this);
            }
        });

        buttonDirectCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().UiCardRegistration(Payment.this, new CardRegistrationCallback() {
                    @Override
                    public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                        Toast.makeText(Payment.this, "register card token success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
                        Toast.makeText(Payment.this, "register card token Failed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        });


        buttonDirectBcaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this, PaymentMethod.BANK_TRANSFER_BCA);
            }
        });


        buttonDirectBniVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this, PaymentMethod.BANK_TRANSFER_BNI);
            }
        });

        buttonDirectMandiriVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
            }
        });


        buttonDirectPermataVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this, PaymentMethod.BANK_TRANSFER_PERMATA);
            }
        });

        buttonDirectAtmBersamaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this, PaymentMethod.BCA_KLIKPAY);
            }
        });

    }

    public void set_transaction_reference(String reference_id,String status){
        String url = "";
        if(getIntent().getStringExtra("type").equals("tour"))
            url = C_URL+"sales/tour/reference";
        else if(getIntent().getStringExtra("type").equals("hotel"))
            url = C_URL+"sales/hotel/reference";
        else if(getIntent().getStringExtra("type").equals("flight"))
            url = C_URL+"sales/flight/reference";
        else if(getIntent().getStringExtra("type").equals("train"))
            url = C_URL+"sales/train/reference";

        JSONObject json = new JSONObject();
        try {
            json.put("id",getIntent().getStringExtra("id"));
            json.put("reference_id",reference_id);
            json.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent in = new Intent(Payment.this, BottomNavContainer.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error_exception(error,layout);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", base_shared_pref.getString("access_token",""));
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
    
}
