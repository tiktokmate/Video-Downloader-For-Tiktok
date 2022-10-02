package com.leetlab.utils.drawable;

import android.graphics.drawable.GradientDrawable;

public class DrawableUtil {

    private GradientDrawable mDrawable;

    public static GradientDrawableUtils setGradient(int startColor, int endColor) {
        return new GradientDrawableUtils(startColor, endColor);
    }

    public static GradientDrawableUtils setGradient(int startColor, int centerColor, int endColor) {
        return new GradientDrawableUtils(startColor, centerColor, endColor);
    }

    public static SolidDrawableUtils setSolid(int color) {
        return new SolidDrawableUtils(color);
    }

}
