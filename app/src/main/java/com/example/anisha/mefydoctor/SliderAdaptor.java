package com.example.anisha.mefydoctor;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdaptor extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdaptor(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.mefy_logo,
            R.drawable.mefy_logo,
            R.drawable.mefy_logo,
            R.drawable.mefy_logo,
    };

    public String[] slide_headings ={
            " PROACTIVE CARE",
            " YOUR ASSET",
            " MADE POSSIBLE",
            " DOCTORS"
    };

    public  String[] slide_descs = {
            " Switch from\n Reactive to" ,
            " Your Health\n Reccord ",
            " Chronic Care\n Management ",
            " Anytime Anywhere\n Consult Global"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout , container ,false);
        ImageView slideImage = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);
//        Typeface typefaces = ResourcesCompat.getFont(context,R.font.sl);
//        slideDescription.setTypeface(typefaces);
//        Typeface typeface = ResourcesCompat.getFont(context,R.font.cap);
//        slideHeading.setTypeface(typeface);

        slideImage.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);
        container.addView(view);
        System.out.println("view---------------->>>>>>>>>>>>>>");
        return view;    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
