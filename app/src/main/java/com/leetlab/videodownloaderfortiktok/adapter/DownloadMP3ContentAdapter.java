package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.DownloadClickListener;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadMP3ContentAdapter extends RecyclerView.Adapter<DownloadMP3ContentAdapter.ContentViewHolder> {


    private Context mContext;
    private List<InstaContent> mContents;
    private DownloadClickListener listener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private boolean isSelectableActive = false;

    public DownloadMP3ContentAdapter(Context mContext, List<InstaContent> mContents, DownloadClickListener listener) {
        this.mContext = mContext;
        this.mContents = mContents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_download_content, parent, false);
        return new ContentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        InstaContent content = mContents.get(position);
        if (!content.isVideo)
            ((ContentViewHolder) holder).ivPlay.setVisibility(View.GONE);


        holder.ivContent.setText(content.url.substring(content.url.lastIndexOf("/")+1));

        holder.checkBox.setVisibility((isSelectableActive) ? View.VISIBLE : View.GONE);
        holder.checkBox.setChecked((selectedItems.get(position, false)));
        if (selectedItems.get(position, false))
            holder.lytShadow.setVisibility(View.VISIBLE);
        else
            holder.lytShadow.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onClick(content, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClickListener(content, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_content)
        TextView ivContent;
        @BindView(R.id.iv_play)
        ImageView ivPlay;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.lyt_shadow)
        ConstraintLayout lytShadow;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public boolean isSelectableActive() {
        return isSelectableActive;
    }

    public void enableSelectionMode() {
        isSelectableActive = true;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return selectedItems.size();
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void clearSelections() {
        selectedItems.clear();
        isSelectableActive = false;
        notifyDataSetChanged();
    }


}
