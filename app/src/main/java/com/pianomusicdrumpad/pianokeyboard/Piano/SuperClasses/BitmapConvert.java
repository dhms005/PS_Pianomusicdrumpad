package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Hashtable;

import com.pianomusicdrumpad.pianokeyboard.R;


public class BitmapConvert {


    private static Hashtable<String, Bitmap> f179a = new Hashtable<>();


    private static Hashtable<String, Bitmap> f180b = new Hashtable<>();


    public static Bitmap m83a(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f179a;
        if (hashtable.containsKey("" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f179a;
            return hashtable2.get("" + i);
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        if (decodeResource != null) {
            Hashtable<String, Bitmap> hashtable3 = f179a;
            hashtable3.put("" + i, decodeResource);
        }
        return decodeResource;
    }


    public static Bitmap m84b(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f179a;
        if (hashtable.containsKey("" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f179a;
            return hashtable2.get("" + i);
        }
        Bitmap d = m86d(i, context);
        if (d != null) {
            Hashtable<String, Bitmap> hashtable3 = f179a;
            hashtable3.put("" + i, d);
        }
        return d;
    }


    public static Bitmap m85c(int i, Context context) {
        Hashtable<String, Bitmap> hashtable = f179a;
        if (hashtable.containsKey("black" + i)) {
            Hashtable<String, Bitmap> hashtable2 = f179a;
            return hashtable2.get("black" + i);
        }
        Bitmap e = m87e(i, context);
        if (e != null) {
            Hashtable<String, Bitmap> hashtable3 = f179a;
            hashtable3.put("black" + i, e);
        }
        return e;
    }


    private static Bitmap m86d(int i, Context context) {
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

    
    private static Bitmap m87e(int i, Context context) {
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
