package com.example.arrow;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private ViewPager screenPager;
//    OnboardViewPagerAdapter onboardViewPagerAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_onboard);
//
//        List<OnboardItem> mList = new ArrayList<>();
//        mList.add(new OnboardItem("Find the right Professors for You!",
//                "Find the right professors to take in your class!",
//                R.drawable.main1));
//        mList.add(new OnboardItem("Find Your Classes",
//                "Find the right professors to take in your class!",
//                R.drawable.main2));
//        mList.add(new OnboardItem("Leave Reviews",
//                "Help other students with their choices and leave reviews about your past classes!",
//                R.drawable.main3));
//
//        screenPager = findViewById(R.id.screen_viewpager);
//        onboardViewPagerAdapter = new OnboardViewPagerAdapter(this, mList);
//        screenPager.setAdapter(onboardViewPagerAdapter);
//    }

    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initComponents();

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#2A8A55"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

    }

    private void initComponents() {
        this.btnContinue = findViewById(R.id.btn_continue);
        this.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}