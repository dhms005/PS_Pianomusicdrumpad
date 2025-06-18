package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.JamActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.R;

public class SoundManager {
    public static final String LOG_TAG = "themelodymaster";
    public static final String SOUND_CLICK = "Click";
    public static final String SOUND_KICK = "Kick";
    public static final String SOUND_LOW_VIBRATION = "Low Vibration Only";
    public static final String SOUND_SNARE = "Snare";
    public static final String SOUND_TICK = "Tick";
    public static final String SOUND_TRIANGLE = "Triangle";
    public static final String SOUND_VIBRATION = "Vibration Only";
    public static int clickBeatsInBar = 1;
    public static int clickBpm = 100;
    public static String clickInBarSound = "tick";
    public static String clickOnBarSound = "click";
    public static int clickStreamId = 0;
    private static Thread clickThread = null;
    public static float clickVolume = 1.0f;
    private static int currentStreamId = 0;
    public static boolean hasLoadedPercussionSounds = false;
    public static boolean hasLoadedSounds = false;
    private static SoundManager instance = null;
    public static boolean isClickDesired = false;
    private static int lastNotPos;
    private static long lastPlayedTimestamp;
    public static SoundPool mPercussionSoundPool;
    private static SoundPool mSoundPool;
    public static HashMap<Note, Integer> noteRIdMapping = new HashMap<>();
    public static HashMap<String, Integer> percussionSounds = new HashMap<>();
    public static HashMap<String, Integer> percussionSoundsLoaded = new HashMap<>();
    public static HashMap<Integer, Note> sounds = new HashMap<>();
    private static HashMap<Integer, Integer> soundsLoaded = new HashMap<>();
    private AudioManager mAudioManager;
    private Context mContext;
    public HapticManager mHapticManager;
    public float musicStreamVolume = 1.0f;

    private SoundManager(Context context) {
        this.mContext = context;
        if (mSoundPool != null) {
            //Log.v("themelodymaster", "SoundManager - What it wasnt null!");
            mSoundPool.release();
            mSoundPool = null;
        }
        SoundPool soundPool = mPercussionSoundPool;
        if (soundPool != null) {
            soundPool.release();
            mPercussionSoundPool = null;
        }
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (Build.VERSION.SDK_INT >= 21) {
            mSoundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(2).build()).setMaxStreams(37).build();
        } else {
            mSoundPool = new SoundPool(37, 3, 0);
        }
        addSounds();
        float streamVolume = (float) this.mAudioManager.getStreamVolume(3);
        this.musicStreamVolume = streamVolume;
        this.musicStreamVolume = streamVolume / ((float) this.mAudioManager.getStreamMaxVolume(3));
        loadSounds();
        if (Build.VERSION.SDK_INT >= 21) {
            mPercussionSoundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(2).build()).setMaxStreams(10).build();
        } else {
            mPercussionSoundPool = new SoundPool(10, 3, 0);
        }
        addPercussionSounds();
        loadPercussionSounds();
    }

    private void addPercussionSounds() {
        percussionSounds = null;
        HashMap<String, Integer> hashMap = new HashMap<>();
        percussionSounds = hashMap;
        hashMap.put(SOUND_TRIANGLE, Integer.valueOf(R.raw.triangle_adjusted));
        percussionSounds.put(SOUND_TICK, Integer.valueOf(R.raw.tic_adjusted));
        percussionSounds.put(SOUND_CLICK, Integer.valueOf(R.raw.rvbclick_adjusted));
        percussionSounds.put(SOUND_SNARE, Integer.valueOf(R.raw.snare_two_adjusted));
        percussionSounds.put(SOUND_KICK, Integer.valueOf(R.raw.kick_two_adjusted));
    }

    private String getInBarSound(String str) {
        if (SOUND_LOW_VIBRATION.equals(str)) {
            return SOUND_LOW_VIBRATION;
        }
        if (SOUND_VIBRATION.equals(str)) {
            return SOUND_VIBRATION;
        }
        if (SOUND_TRIANGLE.equals(str)) {
            return SOUND_TRIANGLE;
        }
        if (SOUND_TICK.equals(str)) {
            return SOUND_TICK;
        }
        if (SOUND_CLICK.equals(str)) {
            return SOUND_CLICK;
        }
        return SOUND_SNARE;
    }

    public static SoundManager getInstance(Context context) {
        if (instance == null || mSoundPool == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    private String getOnBarSound(String str) {
        if (SOUND_LOW_VIBRATION.equals(str)) {
            return SOUND_LOW_VIBRATION;
        }
        if (SOUND_VIBRATION.equals(str)) {
            return SOUND_VIBRATION;
        }
        if (SOUND_TRIANGLE.equals(str)) {
            return SOUND_TICK;
        }
        if (SOUND_TICK.equals(str)) {
            return SOUND_CLICK;
        }
        return SOUND_CLICK.equals(str) ? SOUND_SNARE : SOUND_KICK;
    }

    private void loadPercussionSounds() {
        if (mPercussionSoundPool == null) {
            Log.e("themelodymaster", "mPercussionSoundPool is null. Reinstantiating audio");
            instance = new SoundManager(this.mContext);
            return;
        }
        percussionSoundsLoaded = new HashMap<>();
        for (Map.Entry next : percussionSounds.entrySet()) {
            int intValue = ((Integer) next.getValue()).intValue();
            String str = (String) next.getKey();
            SoundPool soundPool = mPercussionSoundPool;
            if (soundPool != null) {
                int load = soundPool.load(this.mContext, intValue, 1);
                HashMap<String, Integer> hashMap = percussionSoundsLoaded;
                if (!(hashMap == null || str == null || mPercussionSoundPool == null)) {
                    hashMap.put(str, Integer.valueOf(load));
                    mPercussionSoundPool.play(load, 0.0f, 0.0f, 1, 0, 1.0f);
                }
            }
        }
        hasLoadedPercussionSounds = true;
    }

    private void loadSounds() {
        int load;
        for (int i = 1; i <= sounds.size(); i++) {
            Note note = sounds.get(Integer.valueOf(i));
            SoundPool soundPool = mSoundPool;
            if (!(soundPool == null || note == null || (load = soundPool.load(this.mContext, note.getSound(), i)) == 0)) {
                SoundPool soundPool2 = mSoundPool;
                float f = this.musicStreamVolume * 0.1f;
                soundPool2.play(load, f, f, 1, 0, 1.0f);
                soundsLoaded.put(Integer.valueOf(i), Integer.valueOf(load));
            }
        }
        hasLoadedSounds = true;
    }

    public void addSounds() {
        sounds.put(1, new Note(this.mContext.getString(R.string.BottomC), R.raw.bottom_c, Note.DEFAULT_DURATION_MS, R.id.bottom_c));
        sounds.put(2, new Note(this.mContext.getString(R.string.BottomCSharp), R.raw.bottom_c_sharp, Note.DEFAULT_DURATION_MS, R.id.bottom_c_s));
        sounds.put(3, new Note(this.mContext.getString(R.string.BottomD), R.raw.bottom_d, Note.DEFAULT_DURATION_MS, R.id.bottom_d));
        sounds.put(4, new Note(this.mContext.getString(R.string.BottomDSharp), R.raw.bottom_d_sharp, Note.DEFAULT_DURATION_MS, R.id.bottom_d_s));
        sounds.put(5, new Note(this.mContext.getString(R.string.BottomE), R.raw.bottom_e, Note.DEFAULT_DURATION_MS, R.id.bottom_e));
        sounds.put(6, new Note(this.mContext.getString(R.string.BottomF), R.raw.bottom_f, Note.DEFAULT_DURATION_MS, R.id.bottom_f));
        sounds.put(7, new Note(this.mContext.getString(R.string.BottomFSharp), R.raw.bottom_f_sharp, Note.DEFAULT_DURATION_MS, R.id.bottom_f_s));
        sounds.put(8, new Note(this.mContext.getString(R.string.BottomG), R.raw.bottom_g, Note.DEFAULT_DURATION_MS, R.id.bottom_g));
        sounds.put(9, new Note(this.mContext.getString(R.string.BottomGSharp), R.raw.bottom_g_sharp, Note.DEFAULT_DURATION_MS, R.id.bottom_g_s));
        sounds.put(10, new Note(this.mContext.getString(R.string.BottomA), R.raw.bottom_a, Note.DEFAULT_DURATION_MS, R.id.bottom_a));
        sounds.put(11, new Note(this.mContext.getString(R.string.BottomASharp), R.raw.bottom_a_sharp, Note.DEFAULT_DURATION_MS, R.id.bottom_a_s));
        sounds.put(12, new Note(this.mContext.getString(R.string.BottomB), R.raw.bottom_b, Note.DEFAULT_DURATION_MS, R.id.bottom_b));
        sounds.put(13, new Note(this.mContext.getString(R.string.MiddleC), R.raw.middle_c, Note.DEFAULT_DURATION_MS, R.id.middle_c));
        sounds.put(14, new Note(this.mContext.getString(R.string.MiddleCSharp), R.raw.middle_c_sharp, Note.DEFAULT_DURATION_MS, R.id.middle_c_s));
        sounds.put(15, new Note(this.mContext.getString(R.string.MiddleD), R.raw.middle_d, Note.DEFAULT_DURATION_MS, R.id.middle_d));
        sounds.put(16, new Note(this.mContext.getString(R.string.MiddleDSharp), R.raw.middle_d_sharp, Note.DEFAULT_DURATION_MS, R.id.middle_d_s));
        sounds.put(17, new Note(this.mContext.getString(R.string.MiddleE), R.raw.middle_e, Note.DEFAULT_DURATION_MS, R.id.middle_e));
        sounds.put(18, new Note(this.mContext.getString(R.string.MiddleF), R.raw.middle_f, Note.DEFAULT_DURATION_MS, R.id.middle_f));
        sounds.put(19, new Note(this.mContext.getString(R.string.MiddleFSharp), R.raw.middle_f_sharp, Note.DEFAULT_DURATION_MS, R.id.middle_f_s));
        sounds.put(20, new Note(this.mContext.getString(R.string.MiddleG), R.raw.middle_g, Note.DEFAULT_DURATION_MS, R.id.middle_g));
        sounds.put(21, new Note(this.mContext.getString(R.string.MiddleGSharp), R.raw.middle_g_sharp, Note.DEFAULT_DURATION_MS, R.id.middle_g_s));
        sounds.put(22, new Note(this.mContext.getString(R.string.MiddleA), R.raw.middle_a, Note.DEFAULT_DURATION_MS, R.id.middle_a));
        sounds.put(23, new Note(this.mContext.getString(R.string.MiddleASharp), R.raw.middle_a_sharp, Note.DEFAULT_DURATION_MS, R.id.middle_a_s));
        sounds.put(24, new Note(this.mContext.getString(R.string.MiddleB), R.raw.middle_b, Note.DEFAULT_DURATION_MS, R.id.middle_b));
        sounds.put(25, new Note(this.mContext.getString(R.string.HighC), R.raw.high_c, Note.DEFAULT_DURATION_MS, R.id.high_c));
        sounds.put(26, new Note(this.mContext.getString(R.string.HighCSharp), R.raw.high_c_sharp, Note.DEFAULT_DURATION_MS, R.id.high_c_s));
        sounds.put(27, new Note(this.mContext.getString(R.string.HighD), R.raw.high_d, Note.DEFAULT_DURATION_MS, R.id.high_d));
        sounds.put(28, new Note(this.mContext.getString(R.string.HighDSharp), R.raw.high_d_sharp, Note.DEFAULT_DURATION_MS, R.id.high_d_s));
        sounds.put(29, new Note(this.mContext.getString(R.string.HighE), R.raw.high_e, Note.DEFAULT_DURATION_MS, R.id.high_e));
        sounds.put(30, new Note(this.mContext.getString(R.string.HighF), R.raw.high_f, Note.DEFAULT_DURATION_MS, R.id.high_f));
        sounds.put(31, new Note(this.mContext.getString(R.string.HighFSharp), R.raw.high_f_sharp, Note.DEFAULT_DURATION_MS, R.id.high_f_s));
        sounds.put(32, new Note(this.mContext.getString(R.string.HighG), R.raw.high_g, Note.DEFAULT_DURATION_MS, R.id.high_g));
        sounds.put(33, new Note(this.mContext.getString(R.string.HighGSharp), R.raw.high_g_sharp, Note.DEFAULT_DURATION_MS, R.id.high_g_s));
        sounds.put(34, new Note(this.mContext.getString(R.string.HighA), R.raw.high_a, Note.DEFAULT_DURATION_MS, R.id.high_a));
        sounds.put(35, new Note(this.mContext.getString(R.string.HighASharp), R.raw.high_a_sharp, Note.DEFAULT_DURATION_MS, R.id.high_a_s));
        sounds.put(36, new Note(this.mContext.getString(R.string.HighB), R.raw.high_b, Note.DEFAULT_DURATION_MS, R.id.high_b));
        sounds.put(37, new Note(this.mContext.getString(R.string.DoubleHighC), R.raw.double_high_c, Note.DEFAULT_DURATION_MS, R.id.double_high_c));
    }

    public Set<Note> getAllNotes() {
        HashSet hashSet = new HashSet();
        for (Map.Entry<Integer, Note> value : sounds.entrySet()) {
            hashSet.add(value.getValue());
        }
        return hashSet;
    }

    public int getHapticEffect(String str) {
        if ("OFF".equals(str)) {
            return 999;
        }
        if ("VERY LIGHT".equals(str)) {
            return 26;
        }
        if ("LIGHT".equals(str)) {
            return 2;
        }
        if ("MEDIUM".equals(str)) {
            return 1;
        }
        if ("STRONG".equals(str)) {
            return 0;
        }
        return 999;
    }

    public Note getNote(int i) {
        return sounds.get(Integer.valueOf(i));
    }

    public int getNotePosition(Note note) {
        int i = 1;
        for (Map.Entry next : sounds.entrySet()) {
            if (((Note) next.getValue()).equals(note)) {
                i = ((Integer) next.getKey()).intValue();
            }
        }
        return i;
    }

    public void playSound(int i, float f, boolean z) {
        int i2;
        int i3;
        HashMap<Integer, Integer> hashMap = soundsLoaded;
        if (hashMap == null || mSoundPool == null || hashMap.size() < 1) {
            Log.e("SoundManager", "Sound not already instantiated soundsLoaded:" + soundsLoaded + " mSoundPool:" + mSoundPool + " reinstantiaing with context:" + this.mContext);
            instance = new SoundManager(this.mContext);
        }
        if (soundsLoaded.containsKey(Integer.valueOf(i))) {
            i2 = soundsLoaded.get(Integer.valueOf(i)).intValue();
        } else {
            i2 = mSoundPool.load(this.mContext, sounds.get(Integer.valueOf(i)).getSound(), i);
            soundsLoaded.put(Integer.valueOf(i), Integer.valueOf(i2));
        }
        int i4 = i2;
        long currentTimeMillis = System.currentTimeMillis();
        Log.v("SoundManager", "timeBetweenNotes:" + (currentTimeMillis - lastPlayedTimestamp));
        SoundPool soundPool = mSoundPool;
        if (!(soundPool == null || (i3 = currentStreamId) == 0)) {
            soundPool.stop(i3 - 3);
            mSoundPool.stop(currentStreamId - 4);
            mSoundPool.stop(currentStreamId - 5);
            mSoundPool.stop(currentStreamId - 6);
        }
        SoundPool soundPool2 = mSoundPool;
        if (soundPool2 != null && i4 != 0) {
            float f2 = this.musicStreamVolume * f;
            currentStreamId = soundPool2.play(i4, f2, f2, 1, 0, 1.0f);
            lastNotPos = i;
            lastPlayedTimestamp = currentTimeMillis;
        }
    }

    public void releaseSoundPool() {
        if (mSoundPool != null) {
            Log.v("SoundManager", "releasing soundPools");
            mSoundPool.release();
            mSoundPool = null;
        }
        if (mPercussionSoundPool != null) {
            Log.v("SoundManager", "releasing soundPools");
            mPercussionSoundPool.release();
            mPercussionSoundPool = null;
        }
    }

    public void startClick(Context context, final String str) {
        this.mHapticManager = HapticManager.getInstance(context);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clickBpm = Integer.parseInt(defaultSharedPreferences.getString(MenuActivity.KEY_CLICK_BPM, "100"));
        clickVolume = Float.parseFloat(defaultSharedPreferences.getString(MenuActivity.KEY_CLICK_VOLUME, "0.5"));
        clickBeatsInBar = Integer.parseInt(defaultSharedPreferences.getString(MenuActivity.KEY_CLICK_BEAT, "4-4").split("-")[0]);
        String string = defaultSharedPreferences.getString(MenuActivity.KEY_CLICK_SOUND, SOUND_CLICK);
        clickOnBarSound = getOnBarSound(string);
        clickInBarSound = getInBarSound(string);
        Thread thread = clickThread;
        if (thread != null && thread.isAlive()) {
            isClickDesired = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread r4 = new Thread() {
            public void run() {
                int i;
                boolean z = true;
                SoundManager.isClickDesired = true;
                int i2 = 999;
                boolean z2 = false;
                long j = 0;
                long j2 = 0;
                int i3 = 999;
                int i4 = 0;
                int i5 = 0;
                while (SoundManager.isClickDesired) {
                    Integer num = null;
                    if (SoundManager.clickOnBarSound.equals(SoundManager.SOUND_LOW_VIBRATION) || SoundManager.clickOnBarSound.equals(SoundManager.SOUND_VIBRATION)) {
                        num = Integer.valueOf(z ? 1 : 0);
                    }
                    Log.v("themelodymaster", "clickOnBarSound=" + SoundManager.clickOnBarSound + " clickInBarSound=" + SoundManager.clickInBarSound);
                    if (num != null) {
                        i3 = SoundManager.this.mHapticManager.getClickHapticEffect(z, SoundManager.clickOnBarSound);
                        i2 = SoundManager.this.mHapticManager.getClickHapticEffect(z2, SoundManager.clickInBarSound);
                    } else if (SoundManager.percussionSoundsLoaded != null) {
                        i5 = SoundManager.percussionSoundsLoaded.get(SoundManager.clickOnBarSound).intValue();
                        i4 = SoundManager.percussionSoundsLoaded.get(SoundManager.clickInBarSound).intValue();
                    }
                    if (SoundManager.clickBeatsInBar == 0) {
                        if (num != null) {
                            SoundManager.this.mHapticManager.playHapticEffect(z, i2);
                        } else {
                            SoundManager.clickStreamId = SoundManager.mPercussionSoundPool.play(i4, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, 1, 0, 1.0f);
                        }
                        if ("JamActivity".equals(str)) {
                            JamActivity.greenLight();
                        }
                    } else {
                        if (SoundManager.clickBeatsInBar != 0) {
                            i = i2;
                            if (j % ((long) SoundManager.clickBeatsInBar) == 0) {
                                if (num != null) {
                                    SoundManager.this.mHapticManager.playHapticEffect(true, i3);
                                } else {
                                    SoundManager.clickStreamId = SoundManager.mPercussionSoundPool.play(i5, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, 1, 0, 1.0f);
                                }
                                if ("JamActivity".equals(str)) {
                                    JamActivity.redLight();
                                }
                            }
                        } else {
                            i = i2;
                        }
                        if (num != null) {
                            i2 = i;
                            SoundManager.this.mHapticManager.playHapticEffect(true, i2);
                        } else {
                            i2 = i;
                            SoundManager.clickStreamId = SoundManager.mPercussionSoundPool.play(i4, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, SoundManager.this.musicStreamVolume * SoundManager.clickVolume, 1, 0, 1.0f);
                        }
                        if ("JamActivity".equals(str)) {
                            JamActivity.greenLight();
                        }
                    }
                    float f = 60000.0f / ((float) SoundManager.clickBpm);
                    Log.v("themelodymaster", "Click timeBetweenBeats:" + (System.currentTimeMillis() - j2) + " gapBetweenBeats:" + f + " clickBpm:" + SoundManager.clickBpm);
                    j2 = System.currentTimeMillis();
                    try {
                        Thread.sleep((long) f);
                    } catch (InterruptedException unused) {
                    }
                    j++;
                    z = true;
                    z2 = false;
                }
            }
        };
        clickThread = r4;
        r4.start();
    }

    public void stopClick() {
        isClickDesired = false;
    }

    public void stopClickImmediately() {
        stopClick();
        SoundPool soundPool = mSoundPool;
        if (soundPool != null) {
            soundPool.autoPause();
            mSoundPool.stop(clickStreamId);
            mSoundPool.stop(clickStreamId - 1);
            mSoundPool.stop(clickStreamId - 2);
            mSoundPool.stop(clickStreamId - 3);
            mSoundPool.stop(clickStreamId - 3);
            mSoundPool.stop(clickStreamId - 4);
            mSoundPool.stop(clickStreamId - 5);
            mSoundPool.stop(clickStreamId - 6);
        }
    }

    public void stopSounds() {
        SoundPool soundPool = mSoundPool;
        if (soundPool != null) {
            soundPool.autoPause();
            mSoundPool.stop(currentStreamId);
            mSoundPool.stop(currentStreamId - 1);
            mSoundPool.stop(currentStreamId - 2);
            mSoundPool.stop(currentStreamId - 3);
            mSoundPool.stop(currentStreamId - 3);
            mSoundPool.stop(currentStreamId - 4);
            mSoundPool.stop(currentStreamId - 5);
            mSoundPool.stop(currentStreamId - 6);
        }
    }

    public void updateClick(int i, float f, int i2, String str, String str2) {
        clickBpm = i;
        clickVolume = f;
        clickBeatsInBar = i2;
        clickOnBarSound = getOnBarSound(str);
        clickInBarSound = getInBarSound(str);
        Thread thread = clickThread;
        if (thread != null && thread.getState() == Thread.State.TERMINATED) {
            startClick(this.mContext, str2);
        } else if (clickThread == null) {
            startClick(this.mContext, str2);
        }
    }
}
