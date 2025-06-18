package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;


public class HapticManager {


    static final Handler f226a = new Handler();
    private static HapticManager instance;
    private Context mContext;

    private HapticManager(Context context) {
        this.mContext = context;
    }

    public static HapticManager getInstance(Context context) {
        if (instance == null) {
            instance = new HapticManager(context);
        }
        return instance;
    }

    public int getClickHapticEffect(boolean z, String str) {
        if (SoundManager.SOUND_LOW_VIBRATION.equals(str) && z) {
            return 10;
        }
        if (SoundManager.SOUND_LOW_VIBRATION.equals(str) && !z) {
            return 7;
        }
        if (!SoundManager.SOUND_VIBRATION.equals(str) || !z) {
            return (!SoundManager.SOUND_VIBRATION.equals(str) || z) ? 999 : 6;
        }
        return 9;
    }

    public int getHapticEffect(Note note) {
        String string = PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(MenuActivity.KEY_HAPTIC_FEEDBACK, MenuActivity.defaultHapticFeedback);
        if ("OFF".equals(string)) {
            return 999;
        }
        if ("VERY LIGHT".equals(string)) {
            return 26;
        }
        if ("LIGHT".equals(string)) {
            return 2;
        }
        if ("MEDIUM".equals(string)) {
            return 1;
        }
        if ("STRONG".equals(string)) {
            return 0;
        }
        return 999;
    }

    public void playHapticEffect(boolean z, int i) {
        Log.v("HapticManager", "play playHapticEffect stopBefore:" + z + " hapticEffect:" + i);
        if (i != 999) {
            f226a.postDelayed(new Runnable() {
                public void run() {
                }
            }, 0);
        }
    }
}
