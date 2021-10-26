package com.example.arrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private Button btnContinue;
    OnBoardingAdapter onboardViewPagerAdapter;
    LinearLayout mDotLayout;

    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);



        List<OnboardItem> mList = new ArrayList<>();
        mList.add(new OnboardItem("Find the right Professors for You!",
                "Find the right professors to take in your class!",
                R.drawable.main1));
        mList.add(new OnboardItem("Find Your Classes",
                "Find the right professors to take in your class!",
                R.drawable.main2));
        mList.add(new OnboardItem("Leave Reviews",
                "Help other students with their choices and leave reviews about your past classes!",
                R.drawable.main3));

        screenPager = (ViewPager) findViewById(R.id.screen_viewpager);
        onboardViewPagerAdapter = new OnBoardingAdapter(this);
        screenPager.setAdapter(onboardViewPagerAdapter);

        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        setUpIndicator(0);
        screenPager.addOnPageChangeListener(viewListener);


        this.initComponents();

    }

    private void initComponents() {
        this.btnContinue = findViewById(R.id.btn_continue);
        this.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void setUpIndicator (int position) {

        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.inactive));
            mDotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}