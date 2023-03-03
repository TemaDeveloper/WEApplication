package com_we.java_we.weapplication.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

import com_we.java_we.weapplication.main.MainActivity;
import com_we.java_we.weapplication.R;

public class SignupActivity extends AppCompatActivity {

    private MaterialButton signUpButton;
    private ImageView backImageView;

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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    private void init(){
        signUpButton = findViewById(R.id.button_sign_up);
        backImageView = findViewById(R.id.image_view_back);
    }
}