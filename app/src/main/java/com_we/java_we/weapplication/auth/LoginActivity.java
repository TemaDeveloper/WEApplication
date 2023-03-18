package com_we.java_we.weapplication.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.main.MainActivity;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.models.User;
import com_we.java_we.weapplication.profile.ProfileActivity;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private MaterialButton loginButton;
    private TextView forgetPasswordText, signUpText;
    private TextInputEditText emailEditText, passwordEditText;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        loginButton.setOnClickListener(this);
        forgetPasswordText.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        backImageView.setOnClickListener(this);

    }

    private void login() {
        final String EMAIL = emailEditText.getText().toString();
        final String PASSWORD = passwordEditText.getText().toString();
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<ResponseBody> call = apiInterface.loginUser(EMAIL, PASSWORD);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getApplicationContext()).setUserEmail(EMAIL);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra("email", EMAIL));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ResponseMessage", t.getMessage() + " message" + " " + t.getCause());
            }
        });
    }

    private void init() {
        loginButton = findViewById(R.id.button_log_in);
        forgetPasswordText = findViewById(R.id.text_view_forget_password);
        signUpText = findViewById(R.id.text_view_sign_up);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        backImageView = findViewById(R.id.image_view_back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.button_log_in:
                login();
                break;
            case R.id.text_view_sign_up:
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                break;
            case R.id.text_view_forget_password:
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                break;
        }
    }
}