package com_we.java_we.weapplication.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.adapters.ViewPager2Adapter;
import com_we.java_we.weapplication.bottomFragments.LearnMoreFragment;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.models.Donation;
import com_we.java_we.weapplication.models.Person;
import com_we.java_we.weapplication.models.User;
import com_we.java_we.weapplication.profile.ProfileActivity;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private ViewPager2 viewPager2Images;
    private TextView donateButton, donorsNumberText, raisedAmountText, goalAmountText;
    private MaterialButton learnMoreButton;
    private CircleImageView profileImage;
    private SeekBar donationsSeekBar;
    //Adapter View Pager 2
    private ViewPager2Adapter viewPager2Adapter;
    //List for View Pager 2
    private List<Person> people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        profileImage.setOnClickListener(this);
        learnMoreButton.setOnClickListener(this);
        donateButton.setOnClickListener(this);

        setUpList();
        viewPager2Adapter = new ViewPager2Adapter(people, this);
        transformViewPager();
        viewPager2Images.setAdapter(viewPager2Adapter);

        getPrefs();
        loadProfileImage();
        getNumberOfDonors();
        getRaisedMoney();


    }

    private void loadProfileImage() {
        ApiInterface apiInterface = ApiClient.getApiService();
        String email = SharedPrefManager.getInstance(getApplicationContext()).getUserEmail();
        if (email.equals("")) {
            Picasso.get().load(Uri.parse("https://wetorontohomelessness.000webhostapp.com/we/user_images/img_we_app_logo.png")).into(profileImage);
        } else {
            Call<User> callImage = apiInterface.getUser(email);
            callImage.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    String imagePath = response.body().getImage();
                    Picasso.get().load(Uri.parse(imagePath)).into(profileImage);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }

    }

    private void getRaisedMoney(){
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<List<Donation>> donationsCall = apiInterface.getAllRaisedMoney();
        donationsCall.enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                float raisedAmount = 0;
                float sum = 0;
                for(int i = 0; i < response.body().size(); i++){
                    raisedAmount = response.body().get(i).getValue();
                    sum += raisedAmount;
                }
                raisedAmountText.setText("$ " + String.format("%,.1f", sum));
                donationsSeekBar.setProgress((int)sum);
                animateProgression((int)sum);
            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                Log.e("Donations_ERROR", t.getMessage());
            }
        });
    }


    private void getNumberOfDonors() {
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<List<Donation>> donationsCall = apiInterface.getAllDonors();
        donationsCall.enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                donorsNumberText.setText("" + response.body().size());
            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                Log.e("Donations_ERROR", t.getMessage());
            }
        });
    }

    private void getPrefs() {
        Boolean mode = SharedPrefManager.getInstance(this).getMode();
        if (mode == false) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        String language = SharedPrefManager.getInstance(this).getLanguage();

        if (language.equals("English")) {
            setLocal(this, "en");
        } else {
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

    private void setUpList() {
        people.add(new Person("Kevin", "54", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_1_homelessness));
        people.add(new Person("Alex", "43", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_2_homelessness));
        people.add(new Person("Plamya", "74", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_3_homelessness));
        people.add(new Person("Edvord", "23", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_4_homelessness));
        people.add(new Person("Bill", "45", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_5_homelessness));
    }

    private void init() {
        people = new ArrayList<>();
        viewPager2Images = findViewById(R.id.view_pager_images);
        profileImage = findViewById(R.id.image_view_profile);
        donateButton = findViewById(R.id.button_donate);
        learnMoreButton = findViewById(R.id.button_learn_more);
        donorsNumberText = findViewById(R.id.text_view_donors_number);
        raisedAmountText = findViewById(R.id.text_view_raised_amount);
        goalAmountText = findViewById(R.id.text_view_goal_amount);
        donationsSeekBar = findViewById(R.id.seek_bar_amount_of_donations);
    }

    private void transformViewPager() {
        viewPager2Images.setClipToPadding(false);
        viewPager2Images.setClipChildren(false);
        viewPager2Images.setOffscreenPageLimit(3);
        viewPager2Images.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(16));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2Images.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.button_donate:
                DonationFragment bottomDonationFragment = new DonationFragment();
                bottomDonationFragment.show(getSupportFragmentManager(), "DonationFragment");
                break;
            case R.id.button_learn_more:
                LearnMoreFragment bottomSheetDialogAdditionCheckFragment = new LearnMoreFragment();
                bottomSheetDialogAdditionCheckFragment.show(getSupportFragmentManager(), "LearnMoreFragment");
                break;
        }
    }
    private void animateProgression(int progress) {
        final ObjectAnimator animation = ObjectAnimator.ofInt(donationsSeekBar, "progress", 0, progress);
        animation.setDuration(3500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        donationsSeekBar.clearAnimation();
    }
}