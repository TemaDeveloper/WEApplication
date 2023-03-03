package com_we.java_we.weapplication.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.auth.LoginActivity;
import com_we.java_we.weapplication.bottomFragments.SettingsFragment;
import com_we.java_we.weapplication.adapters.AdapterMyDonations;
import com_we.java_we.weapplication.models.Donation;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private ExtendedFloatingActionButton editButton;
    private ImageView settingsImage, backImageView;
    private LinearLayout signOutLayout;
    private RecyclerView myDonationsRecyclerView;
    //List
    private List<Donation> donations;
    //Adapter
    private AdapterMyDonations adapterMyDonations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        myDonationsRecyclerView.setHasFixedSize(true);
        myDonationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addDonations();
        adapterMyDonations = new AdapterMyDonations(donations, getApplicationContext());
        myDonationsRecyclerView.setAdapter(adapterMyDonations);

        backImageView.setOnClickListener(this);
        settingsImage.setOnClickListener(this);
        editButton.setOnClickListener(this);
        signOutLayout.setOnClickListener(this);

    }

    private void addDonations(){
        donations.add(new Donation("12.04.22", 123));
        donations.add(new Donation("12.04.22", 34));
        donations.add(new Donation("12.04.22", 27));
        donations.add(new Donation("12.04.22", 71));
        donations.add(new Donation("12.04.22", 574));
    }

    private void init(){
        myDonationsRecyclerView = findViewById(R.id.recycler_view_my_donations);
        editButton = findViewById(R.id.button_update);
        settingsImage = findViewById(R.id.image_view_settings);
        backImageView = findViewById(R.id.image_view_back);
        signOutLayout = findViewById(R.id.sign_out_layout);
        donations = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.image_view_back:
                onBackPressed();
                break;
            case R.id.image_view_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.show(getSupportFragmentManager(), "MyCustomDialog");
                break;
            case R.id.button_update:
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                break;
            case R.id.sign_out_layout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
    }
}