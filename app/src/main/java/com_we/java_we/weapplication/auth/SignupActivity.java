package com_we.java_we.weapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.profile.ProfileActivity;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    //widgets
    private MaterialButton signUpButton;
    private ImageView backImageView;
    private TextInputEditText nameEditText, emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp() {

        ApiInterface apiInterface = ApiClient.getApiService();
        String email = emailEditText.getText().toString();

        Call<ResponseBody> callRegister = apiInterface.registerUser(nameEditText.getText().toString(),
                email, passwordEditText.getText().toString(),
                "https://wetorontohomelessness.000webhostapp.com/we/user_images/img_we_app_logo.png");
        callRegister.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getApplicationContext()).setUserEmail(email);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra("email", email));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("FAIL_REGISTER", t.getMessage());
            }
        });


    }

    private void init() {
        signUpButton = findViewById(R.id.button_sign_up);
        backImageView = findViewById(R.id.image_view_back);
        nameEditText = findViewById(R.id.edit_text_name);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
    }
}