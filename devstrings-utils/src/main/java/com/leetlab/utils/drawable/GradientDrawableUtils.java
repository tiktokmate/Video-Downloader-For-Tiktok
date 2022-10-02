package com.leetlab.utils.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class GradientDrawableUtils {

    private GradientDrawable mDrawable;

    GradientDrawableUtils(int startColor, int centerColor, int endColor) {
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, centerColor, endColor});
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.setCornerRadius(16);

    }

    GradientDrawableUtils(int startColor, int endColor) {
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, endColor});
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.setCornerRadius(16);
    }

    public GradientDrawableUtils setOrientation(GradientOrientation orientation){
        switch (orientation){
            case BOTTOM_TOP:
                mDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                break;
            case LEFT_RIGHT:
                mDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                break;
            case RIGHT_LEFT:
                mDrawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                break;
            case TOP_BOTTOM:
                mDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                break;
        }
        return this;
    }

    public GradientDrawableUtils setCornerRadius(float radius){
        mDrawable.setCornerRadius(radius);
        return this;
    }

    public GradientDrawableUtils setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft){
        mDrawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft});
        return this;
    }

    public GradientDrawableUtils setStroke(int width, int color){
        mDrawable.setStroke(width, color);
        return this;
    }

    public Drawable build(){
        return mDrawable;
    }

    public enum GradientOrientation {
        LEFT_RIGHT,
        RIGHT_LEFT,
        TOP_BOTTOM,
        BOTTOM_TOP
    }
}
