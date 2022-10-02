package com.leetlab.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.leetlab.utils.Contants.Constants;
import com.leetlab.utils.R;
import com.daimajia.androidanimations.library.YoYo;

public class AlertDialog {

    private Activity activity;
    private Dialog dialog;
    private AlertPositiveClickListener positiveClickListener;
    private AlertNegativeClickListener negativeClickListener;
    private CardView alertCard;
    private View contentView;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView btnPositive;
    private TextView btnNegative;

    private AlertDialog(Activity activity) {
        this.activity = activity;
        initViews();
        initDialog();
    }

    public static AlertDialog init(Activity activity) {
        return new AlertDialog(activity);
    }

    public AlertDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public AlertDialog setMessage(String description) {
        tvDescription.setText(description);
        return this;
    }

    public AlertDialog setContentTextColor(int color){
        tvTitle.setTextColor(activity.getResources().getColor(color));
        tvDescription.setTextColor(activity.getResources().getColor(color));
        return this;
    }

    public AlertDialog setPositiveClick(String positiveText, AlertPositiveClickListener positiveClickListener) {
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setText(positiveText);
        btnPositive.setOnClickListener(v -> {
            positiveClickListener.onClick(dialog);
        });
        return this;
    }

    public AlertDialog setPositiveClick(String positiveText, int textColor, AlertPositiveClickListener positiveClickListener) {
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setTextColor(activity.getResources().getColor(textColor));
        btnPositive.setText(positiveText);
        btnPositive.setOnClickListener(v -> {
            positiveClickListener.onClick(dialog);
        });
        return this;
    }

    public AlertDialog setPositiveClick(String positiveText, int textColor, int background, AlertPositiveClickListener positiveClickListener) {
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setTextColor(activity.getResources().getColor(textColor));
        btnPositive.setBackground(activity.getResources().getDrawable(background));
        btnPositive.setText(positiveText);
        btnPositive.setOnClickListener(v -> {
            positiveClickListener.onClick(dialog);
        });
        return this;
    }

    public AlertDialog setNegativeClick(String negativeText, AlertNegativeClickListener negativeClickListener) {
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setText(negativeText);
        btnNegative.setOnClickListener(v -> {
            negativeClickListener.onClick(dialog);
        });

        return this;
    }

    public AlertDialog setNegativeClick(String negativeText, int textColor, AlertNegativeClickListener negativeClickListener) {
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setTextColor(activity.getResources().getColor(textColor));
        btnNegative.setText(negativeText);
        btnNegative.setOnClickListener(v -> {
            negativeClickListener.onClick(dialog);
        });

        return this;
    }

    public AlertDialog setNegativeClick(String negativeText, int textColor, int background, AlertNegativeClickListener negativeClickListener) {
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setTextColor(activity.getResources().getColor(textColor));
        btnNegative.setBackground(activity.getResources().getDrawable(background));
        btnNegative.setText(negativeText);
        btnNegative.setOnClickListener(v -> {
            negativeClickListener.onClick(dialog);
        });

        return this;
    }

    public AlertDialog setBackgroundColor(int color){
        alertCard.setCardBackgroundColor(activity.getResources().getColor(color));
        return this;
    }

    public AlertDialog setRadius(float radius){
        alertCard.setRadius(radius);
        return this;
    }

    public AlertDialog animate(){
        YoYo.with(Constants.technique).duration(Constants.duration).repeat(0).playOn(contentView);
        return this;
    }


    public void show() {
        dialog.show();
    }

    private void initDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(contentView);
    }

    public AlertDialog setCancelable(boolean cancelable){
        dialog.setCancelable(cancelable);
        return this;
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.alert_dialog, null);
        alertCard = contentView.findViewById(R.id.card);
        tvTitle = contentView.findViewById(R.id.tv_title);
        tvDescription = contentView.findViewById(R.id.tv_description);
        btnPositive = contentView.findViewById(R.id.btnPositive);
        btnNegative = contentView.findViewById(R.id.btnNegative);
        tvTitle.setText("Lorem Ipsum");
        tvDescription.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        btnPositive.setText("Got it");
        btnPositive.setOnClickListener(v -> dialog.dismiss());
        btnNegative.setVisibility(View.GONE);

    }

    public interface AlertPositiveClickListener {
        void onClick(Dialog dialog);
    }

    public interface AlertNegativeClickListener {
        void onClick(Dialog dialog);
    }

}
