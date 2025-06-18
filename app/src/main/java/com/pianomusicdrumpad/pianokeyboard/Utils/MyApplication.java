package com.pianomusicdrumpad.pianokeyboard.Utils;


import android.app.Application;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        SharePrefUtils.init(getApplicationContext());


    }
}
