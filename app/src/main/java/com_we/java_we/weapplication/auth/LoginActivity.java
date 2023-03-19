package com_we.java_we.weapplication.auth;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;


import java.util.List;

import com_we.java_we.weapplication.R;
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
    private MaterialCardView guestCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        loginButton.setOnClickListener(this);
        forgetPasswordText.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        guestCardView.setOnClickListener(this);

    }

    private void checkLoginForAllUsers() {
        final String EMAIL = emailEditText.getText().toString().trim();
        final String PASSWORD = passwordEditText.getText().toString().trim();
        if(checkFields(EMAIL, PASSWORD)){
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<List<User>> callUsers = apiInterface.getAllUsers();
            callUsers.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    for(User u : response.body()){
                        if(u.getEmail().equals(EMAIL) && u.getPassword().equals(PASSWORD)){
                            login(EMAIL, PASSWORD);
                        }else{
                            Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
        }
    }

    private void login(String EMAIL, String PASSWORD){
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<ResponseBody> callLogin = apiInterface.loginUser(EMAIL, PASSWORD);
        callLogin.enqueue(new Callback<ResponseBody>() {
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
        guestCardView = findViewById(R.id.card_guest);
    }

    private boolean checkFields(String email, String password){
        boolean checkFields;
        if(email.isEmpty()){
            emailEditText.setError("Enter your email");
            checkFields = false;
        }else if(password.isEmpty()){
            passwordEditText.setError("Enter your password");
            checkFields = false;
        }else if (email.isEmpty() && password.isEmpty()){
            emailEditText.setError("Enter your email");
            passwordEditText.setError("Enter your password");
            checkFields = false;
        }else{
            checkFields = true;
        }
        return checkFields;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_guest:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.button_log_in:
                checkLoginForAllUsers();
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