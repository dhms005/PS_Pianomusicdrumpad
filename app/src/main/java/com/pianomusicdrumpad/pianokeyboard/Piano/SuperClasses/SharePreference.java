package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.Context;
import android.content.SharedPreferences;

/* renamed from: com.pianomusicdrumpad.pianokeyboard.a1.e */
public class SharePreference {


    private static final String f182a = "com.pianomusicdrumpad.pianokeyboard.a1.e";


    public static Object m89a(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(f182a, 4);
        if (str.equalsIgnoreCase("soundValumeKey")) {
            return Integer.valueOf(sharedPreferences.getInt(str, 90));
        }
        return null;
    }


    public static void m90a(Context context, String str, Object obj) {
        SharedPreferences.Editor edit = context.getSharedPreferences(f182a, 4).edit();
        if (str.equalsIgnoreCase("soundValumeKey")) {
            edit.putInt(str, ((Integer) obj).intValue());
        }
        edit.commit();
    }
}
