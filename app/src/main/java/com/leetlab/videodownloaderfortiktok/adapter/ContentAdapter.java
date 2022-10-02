package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.SelectedContent;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private Context mContext;
    private List<InstaContent> contents;
    private List<SelectedContent> selectedContent = new ArrayList<>();
    private ContentListener listener;
    private List<String> contentList = new ArrayList<>();

    public ContentAdapter(Context context, List<InstaContent> contents, ContentListener listener) {
        this.mContext = context;
        this.contents = contents;
        this.listener = listener;

        for (InstaContent content : contents) {
            contentList.add(content.url);
            selectedContent.add(new SelectedContent(content.url, content.isVideo));
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstaContent content = contents.get(position);

        String url = content.isVideo ? content.videoThumbnailUrl : content.videoThumbnailUrl;
        Glide.with(mContext)
                .load(url)
                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_content_placeholder))
                .into(holder.contentImg);

        int type = (content.isVideo) ? R.drawable.ic_video : R.drawable.ic_photo;
        holder.typeImg.setImageDrawable(mContext.getResources().getDrawable(type));

        int visibility = (contentList.indexOf(content.url) == -1) ? View.GONE : View.VISIBLE;
        holder.selectedView.setVisibility(visibility);
        holder.selectCheck.setChecked(contentList.indexOf(content.url) != -1);

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contentImg)
        ImageView contentImg;
        @BindView(R.id.selectedView)
        View selectedView;
        @BindView(R.id.typeImg)
        ImageView typeImg;
        @BindView(R.id.selectCheck)
        CheckBox selectCheck;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> selectContent(getAdapterPosition()));
            selectCheck.setOnClickListener(v -> selectContent(getAdapterPosition()));
        }
    }

    private void selectContent(int position) {
        InstaContent content = contents.get(position);
        if (contentList.indexOf(content.url) == -1) {
            selectedContent.add(new SelectedContent(content.url, content.isVideo));
            contentList.add(content.url);
        } else {
            contentList.remove(content.url);
            selectedContent.remove(position);
        }

        listener.onContentAdded(selectedContent);
        notifyItemChanged(position);
    }

    public interface ContentListener {
        void onContentAdded(List<SelectedContent> selectedContent);
    }
}
