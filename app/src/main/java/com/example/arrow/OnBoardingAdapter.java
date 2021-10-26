package com.example.arrow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class OnBoardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public OnBoardingAdapter(Context context) {
        this.context = context;
    }

    int[] images = {
            R.drawable.main1,
            R.drawable.main2,
            R.drawable.main3
    };

    int[] headings = {
            R.string.main1_heading,
            R.string.main2_heading,
            R.string.main3_heading
    };

    int[] descs = {
            R.string.main1_desc,
            R.string.main2_desc,
            R.string.main3_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_screen, container, false);

        ImageView imageView = view.findViewById(R.id.iv_mainpic);
        TextView heading = view.findViewById(R.id.tv_header);
        TextView desc = view.findViewById(R.id.tv_description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
