package com.leetlab.videodownloaderfortiktok.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.IntroModel;

import java.util.List;

public class IntroAdapter extends PagerAdapter {

    private Activity activity;
    private List<IntroModel> intros;

    public IntroAdapter(Activity context, List<IntroModel> intros) {
        this.activity = context;
        this.intros = intros;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_intro, container, false);
        ImageView ivIntro = view.findViewById(R.id.intro_img);
        TextView titleTxt = view.findViewById(R.id.title_txt);
        titleTxt.setText(intros.get(position).getTitle());
        TextView titleDescription = view.findViewById(R.id.description_txt);
        titleDescription.setText(intros.get(position).getDescription());
        ivIntro.setImageDrawable(activity.getResources().getDrawable(intros.get(position).getImage()));
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return intros.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

