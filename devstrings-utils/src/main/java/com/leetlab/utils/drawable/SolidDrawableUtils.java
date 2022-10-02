package com.leetlab.utils.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class SolidDrawableUtils {
    private GradientDrawable mDrawable;

    SolidDrawableUtils(int color) {
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{color, color});
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.setCornerRadius(16);
    }

    public SolidDrawableUtils setCornerRadius(float radius){
        mDrawable.setCornerRadius(radius);
        return this;
    }

    public SolidDrawableUtils setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft){
        mDrawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft});
        return this;
    }

    public SolidDrawableUtils setStroke(int width, int color){
        mDrawable.setStroke(width, color);
        return this;
    }

    public Drawable build(){
        return mDrawable;
    }


}
