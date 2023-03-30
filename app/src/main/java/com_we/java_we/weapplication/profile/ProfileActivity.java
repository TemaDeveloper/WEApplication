package com_we.java_we.weapplication.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.adapters.AdapterMyDonations;
import com_we.java_we.weapplication.auth.LoginActivity;
import com_we.java_we.weapplication.bottomFragments.SettingsFragment;
import com_we.java_we.weapplication.main.MainActivity;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.models.Donation;
import com_we.java_we.weapplication.models.User;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private ExtendedFloatingActionButton editButton;
    private ImageView settingsImage, backImageView;
    private CircleImageView profileImageView;
    private LinearLayout signOutLayout;
    private RecyclerView myDonationsRecyclerView;
    private MaterialCardView loginCardView;
    private TextView nameTextView, emailTextView, loginTextView, noDonationsTextView;
    //user data
    private int id = 0;
    private String username = null, userEmail = null;
    private String imageActualPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        provideRecyclerViewWithLayoutManager();

        backImageView.setOnClickListener(this);
        settingsImage.setOnClickListener(this);
        editButton.setOnClickListener(this);
        signOutLayout.setOnClickListener(this);
        loginCardView.setOnClickListener(this);
    }

    private void provideRecyclerViewWithLayoutManager() {
        myDonationsRecyclerView.setHasFixedSize(true);
        myDonationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyDonations(int id) {
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<List<Donation>> callMyDonations = apiInterface.getMyDonations(id);
        callMyDonations.enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                //get list from database
                List<Donation> donations = response.body();
                //reverse the list
                Collections.reverse(donations);
                if (donations.isEmpty()) {
                    noDonationsTextView.setVisibility(View.VISIBLE);
                    myDonationsRecyclerView.setVisibility(View.GONE);
                } else {
                    noDonationsTextView.setVisibility(View.GONE);
                    myDonationsRecyclerView.setVisibility(View.VISIBLE);
                    myDonationsRecyclerView.setAdapter(new AdapterMyDonations(donations));
                }

            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                Log.e("GET_MY_DONATIONS_ERROR", t.getMessage());
            }
        });
    }

    private void init() {
        //initialize fields
        myDonationsRecyclerView = findViewById(R.id.recycler_view_my_donations);
        editButton = findViewById(R.id.button_update);
        settingsImage = findViewById(R.id.image_view_settings);
        backImageView = findViewById(R.id.image_view_back);
        signOutLayout = findViewById(R.id.sign_out_layout);
        loginCardView = findViewById(R.id.card_login);
        nameTextView = findViewById(R.id.text_view_name);
        emailTextView = findViewById(R.id.text_view_email);
        loginTextView = findViewById(R.id.text_login);
        profileImageView = findViewById(R.id.image_view_profile);
        noDonationsTextView = findViewById(R.id.text_view_no_donations);
        getUserInfoFromDatabase();
    }

    private void getUserInfoFromDatabase() {
        String email = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        if (!email.equals("")) {
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<User> callUserInfo = apiInterface.getUser(email);
            callUserInfo.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    //get data from database
                    userEmail = response.body().getEmail();
                    username = response.body().getName();
                    imageActualPath = response.body().getImage();
                    id = response.body().getId();
                    //load image
                    Picasso.get().load(Uri.parse(imageActualPath)).into(profileImageView);
                    //set texts with values of database
                    nameTextView.setText(username);
                    emailTextView.setText(userEmail);
                    //get my donations
                    getMyDonations(id);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("FAIL_USER", t.getMessage());
                }
            });

        } else {
            editButton.setEnabled(false);
            Picasso.get().load(Uri.parse("https://wetorontohomelessness.000webhostapp.com/we/user_images/img_we_app_logo.png")).into(profileImageView);
            emailTextView.setText("#user" + (int) Math.floor(Math.random() * (999 - 100 + 1) + 100));
        }

        if (checkLogin(email)) {
            loginCardView.setEnabled(false);
            loginTextView.setText(getResources().getString(R.string.text_logged_in));
        } else {
            loginCardView.setEnabled(true);
            loginTextView.setText(getResources().getString(R.string.text_log_in_button));
        }

    }

    private boolean checkLogin(String email) {
        if (!email.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_login:
                startActivity(goToLogin());
                break;
            case R.id.image_view_back:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.image_view_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.show(getSupportFragmentManager(), "MyCustomDialog");
                break;
            case R.id.button_update:
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class)
                        .putExtra("ID", id)
                        .putExtra("name", username)
                        .putExtra("email", userEmail)
                        .putExtra("image", imageActualPath));
                break;
            case R.id.sign_out_layout:
                //making new alert dialog
                final Dialog dialog = new Dialog(ProfileActivity.this, R.style.ThemeDialog);

                //alert dialog options builder
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog_sign_out);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                //buttons on alert dialog initialization
                MaterialButton btnNo = dialog.findViewById(R.id.btn_no),
                        btnYes = dialog.findViewById(R.id.btn_yes);

                //buttons on alert dialog listeners
                btnNo.setOnClickListener(v -> dialog.dismiss());
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManager.getInstance(getApplicationContext()).setUserEmail("");
                        startActivity(goToLogin());
                        dialog.cancel();
                    }
                });

                dialog.show();
                break;
        }
    }

    private Intent goToLogin() {
        return new Intent(getApplicationContext(), LoginActivity.class);
    }
}