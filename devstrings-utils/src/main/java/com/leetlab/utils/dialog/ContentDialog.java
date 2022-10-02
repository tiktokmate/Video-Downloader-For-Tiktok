package com.leetlab.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.leetlab.utils.Contants.Constants;
import com.daimajia.androidanimations.library.YoYo;

public class ContentDialog {

    private Dialog dialog;
    private Activity activity;
    private View contentView;

    private ContentDialog(Activity activity) {
        this.activity = activity;

        iniDialog();
    }

    public static ContentDialog init(Activity activity) {
        return new ContentDialog(activity);
    }

    public ContentDialog setContentView(View contentView) {
        this.contentView = contentView;
        dialog.setContentView(contentView);
        return this;
    }

    public ContentDialog setCancelable(boolean cancelable){
        dialog.setCancelable(cancelable);
        return this;
    }

    public ContentDialog setClickAbles(int[] viewIds, OnViewClickListeners listeners){
        for (int id: viewIds){
            View view = contentView.findViewById(id);
            view.setOnClickListener(v -> {
                listeners.onClick(dialog, id);
            });
        }
        return this;
    }

    public ContentDialog animate(){
        YoYo.with(Constants.technique).duration(Constants.duration).repeat(0).playOn(contentView);
        return this;
    }

    public void show(){
        dialog.show();
    }

    private void iniDialog(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public interface OnViewClickListeners {
        void onClick(Dialog dialog, int id);
    }
}
