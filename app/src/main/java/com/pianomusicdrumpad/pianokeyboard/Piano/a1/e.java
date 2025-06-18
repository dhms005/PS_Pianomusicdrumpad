package com.pianomusicdrumpad.pianokeyboard.Piano.a1;

import android.content.Context;
import android.content.SharedPreferences;

public class e {
    private static final String a = "com.pianomusicdrumpad.pianokeyboard.a1.e";

    public static Object a(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(a, 4);
        if (str.equalsIgnoreCase("soundValumeKey")) {
            return Integer.valueOf(sharedPreferences.getInt(str, 90));
        }
        return null;
    }

    public static void a(Context context, String str, Object obj) {
        SharedPreferences.Editor edit = context.getSharedPreferences(a, 4).edit();
        if (str.equalsIgnoreCase("soundValumeKey")) {
            edit.putInt(str, ((Integer) obj).intValue());
        }
        edit.commit();
    }
}
