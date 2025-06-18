package com.pianomusicdrumpad.pianokeyboard.Piano.a1;

import android.content.Context;

public class a {
    public static float a(float f, Context context) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
