package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.ServiceRVClickListener;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.bumptech.glide.Glide;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {


    private Context context;
    private List<InstaContent> contents;
    private ServiceRVClickListener onclickListner;

    public ServiceAdapter(Context context, List<InstaContent> contents, ServiceRVClickListener onclickListner) {
        this.context = context;
        this.contents = contents;
        this.onclickListner = onclickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (contents.get(position).isVideo) {
            holder.isVideoIcon.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(contents.get(position).videoThumbnailUrl)
                    .placeholder(context.getDrawable(R.drawable.ic_photo_placeholder))
                    .into(holder.contentImg);
        } else {
            holder.isImgIcon.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(contents.get(position).videoThumbnailUrl)
                    .placeholder(context.getDrawable(R.drawable.ic_photo_placeholder))
                    .into(holder.contentImg);
        }

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content_img)
        ImageView contentImg;
        @BindView(R.id.drak_view)
        ImageView drakView;
        @BindView(R.id.check_box)
        CustomCheckBox checkBox;
        @BindView(R.id.is_video_icon)
        ImageView isVideoIcon;
        @BindView(R.id.is_img_icon)
        ImageView isImgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    drakView.setVisibility(View.GONE);
                } else {
                    checkBox.setChecked(true);
                    drakView.setVisibility(View.VISIBLE);
                }
                onclickListner.onClick(getAdapterPosition(), true);
            });

        }
    }
}
