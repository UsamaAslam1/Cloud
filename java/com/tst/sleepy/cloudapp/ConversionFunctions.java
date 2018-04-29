package com.tst.sleepy.cloudapp;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ConversionFunctions {
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px * scale + 0.5f);
        return dp;
    }
}
