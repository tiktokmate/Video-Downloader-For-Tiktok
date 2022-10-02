package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.UserObject;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<UserObject> userObjectList;
    private StorySelectListener storySelectListener;
    private boolean isFav = false;

    public StoriesAdapter(Context context, List<UserObject> userObjectList, StorySelectListener storySelectListener, boolean isFav) {
        this.context = context;
        this.userObjectList = userObjectList;
        this.storySelectListener = storySelectListener;
        this.isFav = isFav;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (userObjectList.size() == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_no_result, parent, false);
            return new NoResultHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_stories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            UserObject user = userObjectList.get(position);
            ((ViewHolder) holder).txtViewName.setText(user.getRealName());
            ((ViewHolder) holder).txtViewUsername.setText(user.getUserName());
            Picasso.with(context).load(user.getImage()).into(((ViewHolder) holder).profileImg);

        }

        if (holder instanceof NoResultHolder) {
            if (isFav) {
                ((NoResultHolder) holder).txtViewMessage.setText("No Favorite Stories");
            } else {
                ((NoResultHolder) holder).txtViewMessage.setText("No Stories");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (userObjectList.size() == 0) {
            return 1;
        }
        return userObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtViewUsername)
        TextView txtViewUsername;
        @BindView(R.id.txtViewName)
        TextView txtViewName;
        @BindView(R.id.profile_img)
        RoundedImage profileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            View.OnClickListener storyClickListener = v -> storySelectListener.onStorySelect(userObjectList.get(getAdapterPosition()));
            itemView.setOnClickListener(storyClickListener);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class NoResultHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgViewNoResult)
        ImageView imgViewNoResult;
        @BindView(R.id.txtViewMessage)
        TextView txtViewMessage;

        public NoResultHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface StorySelectListener {
        void onStorySelect(UserObject userObject);
    }
}
