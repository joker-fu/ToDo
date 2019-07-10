package com.joker.core.utils;

import android.content.Context;

/**
 * SizeUtils
 *
 * @author joker
 * @date 2019/2/21.
 */
public class SizeUtils {

    private static float density(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return (int) (dp * density(context) + 0.5f);
    }

    public static float px2dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return (int) (px / density(context) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}