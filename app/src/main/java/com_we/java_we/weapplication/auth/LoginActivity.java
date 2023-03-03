package com_we.java_we.weapplication.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import com_we.java_we.weapplication.main.MainActivity;
import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.managers.SharedPrefManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton loginButton;
    private TextView forgetPasswordText, signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        loginButton.setOnClickListener(this);
        forgetPasswordText.setOnClickListener(this);
        signUpText.setOnClickListener(this);

        Boolean mode = SharedPrefManager.getInstance(this).getMode();
        if(mode == false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        String language = SharedPrefManager.getInstance(this).getLanguage();

        if(language.equals("English")){
            setLocal(this, "en");
        }else{
            setLocal(this, "fr");
        }

    }

    public void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void init(){
        loginButton = findViewById(R.id.button_log_in);
        forgetPasswordText = findViewById(R.id.text_view_forget_password);
        signUpText = findViewById(R.id.text_view_sign_up);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_log_in:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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