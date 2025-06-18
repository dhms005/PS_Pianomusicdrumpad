package com.pianomusicdrumpad.pianokeyboard.Piano.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Hashtable;

import com.pianomusicdrumpad.pianokeyboard.R;


public class C0885b {


    private static Hashtable<String, Bitmap> f143a = new Hashtable<>();


    private static Hashtable<String, Bitmap> f144b = new Hashtable<>();


    public static int m75a(BitmapFactory.Options options, int i, int i2) {
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


    public static Bitmap m76a(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f143a;
        if (hashtable.containsKey("" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f143a;
            return hashtable2.get("" + i);
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        if (decodeResource != null) {
            Hashtable<String, Bitmap> hashtable3 = f143a;
            hashtable3.put("" + i, decodeResource);
        }
        return decodeResource;
    }


    public static Bitmap m77a(Resources resources, int i, int i2, int i3) {
        Hashtable<String, Bitmap> hashtable = f143a;
        if (hashtable.containsKey("" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f143a;
            return hashtable2.get("" + i);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = m75a(options, i2, i3);
        options.inJustDecodeBounds = false;
        Bitmap decodeResource = BitmapFactory.decodeResource(resources, i, options);
        Hashtable<String, Bitmap> hashtable3 = f143a;
        hashtable3.put("" + i, decodeResource);
        return decodeResource;
    }


    public static Bitmap m78b(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f143a;
        if (hashtable.containsKey("" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f143a;
            return hashtable2.get("" + i);
        }
        Bitmap d = m80d(i, context);
        if (d != null) {
            Hashtable<String, Bitmap> hashtable3 = f143a;
            hashtable3.put("" + i, d);
        }
        return d;
    }


    public static Bitmap m79c(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f143a;
        if (hashtable.containsKey("black" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f143a;
            return hashtable2.get("black" + i);
        }
        Bitmap e = m81e(i, context);
        if (e != null) {
            Hashtable<String, Bitmap> hashtable3 = f143a;
            hashtable3.put("black" + i, e);
        }
        return e;
    }


    private static Bitmap m80d(int i, Context context) {
        int i2;
        Resources resources;
        if (i == 1) {
            resources = context.getResources();
            i2 = R.drawable.white_key_right_ts;
        } else if (i == 2) {
            resources = context.getResources();
            i2 = R.drawable.white_key_both_ts;
        } else if (i == 3) {
            resources = context.getResources();
            i2 = R.drawable.white_key_left_ts;
        } else if (i != 4) {
            return null;
        } else {
            resources = context.getResources();
            i2 = R.drawable.full_key_white;
        }
        return BitmapFactory.decodeResource(resources, i2);
    }


    private static Bitmap m81e(int i, Context context) {
        int i2;
        Resources resources;
        if (i == 1) {
            resources = context.getResources();
            i2 = R.drawable.white_key_right_ts_touch;
        } else if (i == 2) {
            resources = context.getResources();
            i2 = R.drawable.white_key_both_ts_touch;
        } else if (i == 3) {
            resources = context.getResources();
            i2 = R.drawable.white_key_left_ts_touch;
        } else if (i != 4) {
            return null;
        } else {
            resources = context.getResources();
            i2 = R.drawable.full_key_touch;
        }
        return BitmapFactory.decodeResource(resources, i2);
    }
}
