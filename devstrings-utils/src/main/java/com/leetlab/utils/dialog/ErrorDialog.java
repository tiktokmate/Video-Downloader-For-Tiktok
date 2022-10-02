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

public class ErrorDialog {
    private Dialog dialog;
    private Activity activity;
    private View contentView;
    private TryAgainListener tryAgainListener;
    private CancelListener cancelListener;
    private CardView errorDialog;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView btnTryAgain;
    private TextView btnCancel;

    private ErrorDialog(Activity activity) {
        this.activity = activity;
        initViews();
        iniDialog();
    }

    public static ErrorDialog init(Activity activity) {
        return new ErrorDialog(activity);
    }

    public ErrorDialog setContentTextColor(int color) {
        tvTitle.setTextColor(activity.getResources().getColor(color));
        tvDescription.setTextColor(activity.getResources().getColor(color));
        return this;
    }

    public ErrorDialog setBackgroundColor(int color) {
        errorDialog.setCardBackgroundColor(activity.getResources().getColor(color));
        return this;
    }

    public ErrorDialog setRadius(float radius) {
        errorDialog.setRadius(radius);
        return this;
    }

    public ErrorDialog setOnTryAgain(TryAgainListener listener) {
        this.tryAgainListener = listener;
        return this;
    }

    public ErrorDialog setOnTryAgain(int textColor, TryAgainListener listener) {
        this.tryAgainListener = listener;
        btnTryAgain.setTextColor(activity.getResources().getColor(textColor));
        return this;
    }

    public ErrorDialog setTryAgainLayout(int textColor, int background) {
        btnTryAgain.setTextColor(activity.getResources().getColor(textColor));
        btnTryAgain.setBackground(activity.getResources().getDrawable(background));
        return this;
    }

    public ErrorDialog setOnCancel(CancelListener listener) {
        this.cancelListener = listener;
        return this;
    }

    public ErrorDialog setCancelLayout(int textColor, int background) {
        btnCancel.setTextColor(activity.getResources().getColor(textColor));
        btnCancel.setBackground(activity.getResources().getDrawable(background));
        return this;
    }

    public ErrorDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    private void iniDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setContentView(contentView);

    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.error_dialog, null);
        errorDialog = contentView.findViewById(R.id.card);
        tvTitle = contentView.findViewById(R.id.tv_title);
        tvDescription = contentView.findViewById(R.id.tv_description);
        btnTryAgain = contentView.findViewById(R.id.btnPositive);
        btnCancel = contentView.findViewById(R.id.btnNegative);

        btnTryAgain.setOnClickListener(v -> {
            if (tryAgainListener != null) {
                tryAgainListener.onTryAgain(dialog);
            }
        });
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            if (cancelListener != null) cancelListener.onCancel();
        });
    }

    public ErrorDialog animate() {
        YoYo.with(Constants.technique).duration(Constants.duration).repeat(0).playOn(contentView);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public interface TryAgainListener {
        void onTryAgain(Dialog dialog);
    }

    public interface CancelListener {
        void onCancel();
    }
}
