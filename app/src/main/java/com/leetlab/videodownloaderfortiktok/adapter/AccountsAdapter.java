package com.leetlab.videodownloaderfortiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private Context context;
    private List<InstaUserModel> userModelList;
    private AccountSelectListener listener;

    public AccountsAdapter(Context context, List<InstaUserModel> userModelList, AccountSelectListener listener) {
        this.context = context;
        this.userModelList = userModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstaUserModel user = userModelList.get(position);
        Picasso.with(context).load(user.getImage_url()).into(holder.profileImg);
        holder.txtViewName.setText(user.getName());
        holder.txtViewUsername.setText("@" + user.getUsername());
    }

    @Override
    public int getItemCount() {
        return (userModelList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtViewName)
        TextView txtViewName;
        @BindView(R.id.txtViewUsername)
        TextView txtViewUsername;
        @BindView(R.id.profile_img)
        RoundedImage profileImg;
        @BindView(R.id.item_account)
        ConstraintLayout itemAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> listener.onSelect(userModelList.get(getAdapterPosition())));
        }
    }

    public interface AccountSelectListener {
        void onSelect(InstaUserModel instaUserModel);
    }
}
