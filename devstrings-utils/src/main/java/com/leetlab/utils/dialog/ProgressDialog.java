package com.leetlab.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.leetlab.utils.R;

public class ProgressDialog {

    private Activity activity;
    private Dialog dialog;
    private ProgressType type;
    private CardView progressCard;
    private View contentView;
    private ProgressBar progress;
    private TextView tvMessage;

    public ProgressDialog(Activity activity, ProgressType type) {
        this.activity = activity;
        this.type = type;

        initViews();

        initDialog();
    }

    public ProgressDialog setMessage(String description) {
        tvMessage.setText(description);
        return this;
    }

    public ProgressDialog setTextColor(int color) {
        tvMessage.setTextColor(activity.getResources().getColor(color));
        return this;
    }

    public ProgressDialog setBackgroundColor(int color) {
        progressCard.setCardBackgroundColor(activity.getResources().getColor(color));
        return this;
    }

    public ProgressDialog setRadius(float radius) {
        progressCard.setRadius(radius);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void initDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setContentView(contentView);
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (type == ProgressType.HORIZONTAL)
            contentView = inflater.inflate(R.layout.progess_dailog_horizontal, null);
        else
            contentView = inflater.inflate(R.layout.progess_dailog_vertical, null);
        progressCard = contentView.findViewById(R.id.card);
        progress = contentView.findViewById(R.id.progress);
        tvMessage = contentView.findViewById(R.id.tv_message);
    }

}
