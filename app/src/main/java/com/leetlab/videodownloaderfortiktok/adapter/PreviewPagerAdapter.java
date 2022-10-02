package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.bumptech.glide.Glide;

import java.util.List;

public class PreviewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<InstaContent> mContent;

    public PreviewPagerAdapter(Context mContext, List<InstaContent> mContent) {
        this.mContext = mContext;
        this.mContent = mContent;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_preview, container, false);
        container.addView(view);

        InstaContent content = mContent.get(position);

        ImageView previewImg = view.findViewById(R.id.previewImg);
        ImageView playBtn = view.findViewById(R.id.iv_play);

        playBtn.setVisibility((content.isVideo) ? View.VISIBLE : View.GONE);
        Glide.with(mContext)
                .load((content.isVideo) ? content.videoThumbnailUrl : content.videoThumbnailUrl)
                .placeholder(mContext.getDrawable(R.drawable.ic_content_placeholder))
                .into(previewImg);

        return view;
    }

    @Override
    public int getCount() {
        return mContent.size();
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
