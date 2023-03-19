package com_we.java_we.weapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.models.User;
import com_we.java_we.weapplication.payment.PayPalConfig;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DonationFragment extends BottomSheetDialogFragment{

    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);//using sandbox because testing
    int amount = 0;
    //widgets
    private TextInputEditText amountEditText;
    private MaterialButton confirmationMaterialButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);

        init(view);
        confirmationMaterialButton.setOnClickListener(view1 -> {
            donate();
            getPayment();
        });


        view.findViewById(R.id.card_view_ten_dollars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountEditText.setText("10");
            }
        });
        view.findViewById(R.id.card_view_twenty_five_dollars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountEditText.setText("25");
            }
        });
        view.findViewById(R.id.card_view_fifty_dollars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountEditText.setText("50");
            }
        });
        return view;
    }


    private void init(View view) {
        amountEditText = view.findViewById(R.id.edit_text_amount_money);
        confirmationMaterialButton = view.findViewById(R.id.button_confirm);
    }

    private void donate() {
        String email = SharedPrefManager.getInstance(getContext()).getUserEmail();
        if (!email.equals("")) {
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<User> callUserInfo = apiInterface.getUser(email);
            callUserInfo.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    amount = Integer.parseInt(amountEditText.getText().toString().trim());

                    if (response.isSuccessful()) {
                        ApiInterface apiInterfaceDonation = ApiClient.getApiService();
                        Call<ResponseBody> callDonation = apiInterfaceDonation.addDonation(response.body().getId(), amount, getCurrentDate());
                        callDonation.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("DONATION_ERROR", t.getMessage());
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("FAIL_USER", t.getMessage());
                }
            });

        }
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void getPayment() {
        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amountEditText.getText().toString().trim()), "CAD", "Charitable program for homeless people",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Creating Paypal Payment activity intent
        Intent intent = new Intent(getContext(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        Toast.makeText(getContext(), "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}