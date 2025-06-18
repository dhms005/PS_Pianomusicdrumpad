package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.Context;

public class DensityDpi {

    public static float m82a(float f, Context context) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
