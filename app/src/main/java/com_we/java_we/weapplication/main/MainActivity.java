package com_we.java_we.weapplication.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.adapters.ViewPager2Adapter;
import com_we.java_we.weapplication.bottomFragments.LearnMoreFragment;
import com_we.java_we.weapplication.models.Person;
import com_we.java_we.weapplication.profile.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private ViewPager2 viewPager2Images;
    private TextView donateButton;
    private MaterialButton learnMoreButton;
    private CircleImageView profileImage;
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
        viewPager2Adapter = new ViewPager2Adapter(people,this);
        transformViewPager();
        viewPager2Images.setAdapter(viewPager2Adapter);

    }

    private void setUpList(){
        people.add(new Person("Kevin", "54", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_1_homelessness));
        people.add(new Person("Alex", "43", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_2_homelessness));
        people.add(new Person("Plamya", "74", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_3_homelessness));
        people.add(new Person("Edvord", "23", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_4_homelessness));
        people.add(new Person("Bill", "45", "I have been homeless for 7 years, since my wife broke up with me, she grabed everything that I got, my children, money, home and so on", R.drawable.img_5_homelessness));
    }

    private void init(){
        people = new ArrayList<>();
        viewPager2Images = findViewById(R.id.view_pager_images);
        profileImage = findViewById(R.id.image_view_profile);
        donateButton = findViewById(R.id.button_donate);
        learnMoreButton = findViewById(R.id.button_learn_more);
    }

    private void transformViewPager(){
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
        switch(view.getId()){
            case R.id.image_view_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.button_donate:
                startActivity(new Intent(MainActivity.this, DonationActivity.class));
                break;
            case R.id.button_learn_more:
                LearnMoreFragment bottomSheetDialogAdditionCheckFragment = new LearnMoreFragment();
                bottomSheetDialogAdditionCheckFragment.show(getSupportFragmentManager(), "LearnMoreFragment");
                break;
        }
    }
}