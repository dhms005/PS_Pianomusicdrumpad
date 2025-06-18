package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/* renamed from: com.pianomusicdrumpad.pianokeyboard.a1.f */
public class C0932f {

    public static int m91a(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        Log.d("req size ", i + " -- " + i2);
        Log.d("origanl bit size ", i4 + " -- " + i3);
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        Log.d("sample size ", "samaple size");
        return i5;
    }


    public static Bitmap m92a(Resources resources, int i, int i2, int i3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = m91a(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }
}
