package com.pianomusicdrumpad.pianokeyboard.Piano.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd;
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils;

import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ChordManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ScalesManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.R;
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility;

public class RecognitionActivity extends Activity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    public static final int DURATION = 400;
    private static int HIGHLIGHT_HEIGHT = 0;
    public static final String LOG_TAG = "themelodymaster";
    public static int ON_MEASURE_HEIGHT = 0;
    public static int ON_MEASURE_WIDTH = 0;
    private static int adAdjustment = 18;
    private static int adHeight = 50;
    private static boolean areFlatsOn = false;
    private static Bitmap bitmapBlackKey = null;
    private static Bitmap bitmapBlackKeyFocused = null;
    private static Bitmap bitmapBlackKeySelected = null;
    private static Bitmap bitmapWhiteKey = null;
    private static Bitmap bitmapWhiteKeyFocused = null;
    private static Bitmap bitmapWhiteKeySelected = null;
    private static int blackKeyFirstHalfMargin = -21;
    private static int blackKeyFirstThirdMargin = -23;
    private static float blackKeyLabelMarginBottom = 25.0f;
    private static float blackKeyLabelMarginLeft = 10.0f;
    private static float blackKeyLabelSize = 12.0f;
    private static int blackKeySecondHalfMargin = -15;
    private static int blackKeySecondThirdMargin = -18;
    private static int blackKeyThirdThirdMargin = -12;
    private static int blackKeyWidth = 37;
    private static int blackKeysHeightStart = (34 + 50);
    public static float density = 1.0f;
    private static float dipAdHeight = 50.0f;
    private static float dipHeightOfTopBar = 34.0f;
    private static float fBlackKeyHeightDip = 0.0f;
    private static Drawable flatsOff = null;
    private static Drawable flatsOn = null;
    public static String hapticSetting = "MEDIUM";
    private static int heightOfTopBar = 34;
    private static int iTagFirstNote = 0;
    public static boolean isAdmobGotInventory = true;
    public static boolean isAutoscrollOn = true;
    public static boolean isHighlightAllNotesOn = true;
    public static boolean isPlaying = true;
    public static boolean isPressureOn = false;
    public static boolean isShowPatternOn = false;
    public static boolean isStopDesired = false;
    private static ArrayList<TextView> keyLabels = null;
    public static String noteNamesType = "STANDARD";
    private static int phoneAdHeight = 50;
    public static int playAlongSpeed = 100;
    public static int playAlongVolume = 100;
    private static Drawable playingOff = null;
    public static Drawable playingOn = null;
    public static Resources resources = null;
    private static String rootNote = "C";
    private static String[] rootNoteValues = {"C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"};
    private static int rootNotesIdx = 0;
    private static float scaleFactor = 1.0f;
    public static String scaleTitle = "C Major";
    private static SharedPreferences sharedPrefs = null;
    public static String strDefaultKeySize = "1.1";
    private static int tabletTenAdHeight = 90;
    private static int whiteKeyHeight = 350;
    private static float whiteKeyLabelMarginBottom = 12.0f;
    private static float whiteKeyLabelMarginLeft = 22.0f;
    private static float whiteKeyLabelSize = 24.0f;
    private static int whiteKeyWidth = 57;
    private ChordManager chordManager;
    private float firstPointerRawX = 0.0f;
    private Set<Note> focusedNoteInScaleSet = new HashSet();
    private boolean hasFacebookFailed = false;
    public TextView headerTextView;
    public View lastFinger1Button = null;
    public View lastFinger2Button = null;
    public View lastFinger3Button = null;
    public View lastFinger4Button = null;
    public View lastFinger5Button = null;
    public HapticManager mHapticManager;
    private long mLastTouchTime = 0;
    private RelativeLayout notesLayout = null;
    public TreeSet<Note> notesToRecognise = new TreeSet<>(new NoteNameComparator());
    public ImageView playStopButton;
    private ScalesManager scalesManager;
    private SeekBar seekBar;
    private ImageView sharpFlatButton;
    public SoundManager soundManager;

    public void onStartTrackingTouch(SeekBar seekBar2) {
    }

    public void onStopTrackingTouch(SeekBar seekBar2) {
    }

    class NoteNameComparator implements Comparator<Note> {
        NoteNameComparator() {
        }

        public int compare(Note note, Note note2) {
            return RecognitionActivity.this.soundManager.getNotePosition(note) - RecognitionActivity.this.soundManager.getNotePosition(note2);
        }
    }

    private void addLabel(ImageButton imageButton, ImageButton imageButton2, String str, float f, boolean z, int i, int i2, int i3, int i4) {
        if (!noteNamesType.equals("None") || (str.contains("C") && !str.contains("b") && !str.contains("#"))) {
            if ("SOLFEGE".equals(noteNamesType)) {
                str = this.scalesManager.getSolfegeLabel(str, rootNote, this.soundManager, scaleTitle);
            }
            TextView textView = new TextView(this);
            textView.setText(str);
            textView.setTextSize(f);
            textView.setTypeface(Typeface.MONOSPACE);
            if (z) {
                textView.setTextColor(-12303292);
            } else {
                textView.setTextColor(-1);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(i, i2, i3, i4);
            layoutParams.addRule(8, imageButton.getId());
            if (imageButton2 != null) {
                layoutParams.addRule(1, imageButton2.getId());
            }
            textView.setLayoutParams(layoutParams);
            this.notesLayout.addView(textView);
            keyLabels.add(textView);
        }
    }

    private void bringInDefaultPrefs() {
        isPlaying = true;
        isStopDesired = false;
        scaleFactor = Float.parseFloat(sharedPrefs.getString(MenuActivity.KEY_KEYSIZE, MenuActivity.defaultKeysize));
        noteNamesType = sharedPrefs.getString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames);
        playAlongVolume = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_VOLUME, MenuActivity.defaultVolume));
        playAlongSpeed = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_SPEED, MenuActivity.defaultSpeed));
        isHighlightAllNotesOn = sharedPrefs.getBoolean(MenuActivity.KEY_HIGHLIGHT_ALL_NOTES, MenuActivity.defaultHighlightAllNotes);
        isAutoscrollOn = sharedPrefs.getBoolean(MenuActivity.KEY_AUTOSCROLL, MenuActivity.defaultAutoscroll);
        isPressureOn = sharedPrefs.getBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure);
        hapticSetting = sharedPrefs.getString(MenuActivity.KEY_HAPTIC_FEEDBACK, MenuActivity.defaultHapticFeedback);
        isShowPatternOn = sharedPrefs.getBoolean(MenuActivity.KEY_SHOW_PATTERN, MenuActivity.defaultShowPattern);
        rootNote = sharedPrefs.getString(MenuActivity.KEY_ROOT_NOTE, MenuActivity.defaultRootNote);
        if (Arrays.asList(rootNoteValues).contains(rootNote)) {
            rootNotesIdx = Arrays.asList(rootNoteValues).indexOf(rootNote);
        } else {
            rootNote = "C";
            rootNotesIdx = 0;
        }
        setKeyboardLayout(scaleFactor);
        this.scalesManager.initScales(rootNote);
        scrollToTag(iTagFirstNote);
        setHeaderText("Chord/Scale Recognition");
    }

    private void cleanNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
    }

    private void focusNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapFocus(note));
    }

    private Bitmap getBitmapClean(Note note) {
        return note.getName().contains("#") ? bitmapBlackKey : bitmapWhiteKey;
    }

    private Bitmap getBitmapFocus(Note note) {
        String scaleNoteName = note.getScaleNoteName(scaleTitle);
        return ((scaleNoteName == null || !scaleNoteName.contains("#")) && !scaleNoteName.contains("b")) ? bitmapWhiteKeyFocused : bitmapBlackKeyFocused;
    }

    private Bitmap getBitmapSelected(Note note) {
        return note.getName().contains("#") ? bitmapBlackKeySelected : bitmapWhiteKeySelected;
    }

    private View getNoteFromPosition(float f, float f2, int i) {
        int intValue = Float.valueOf(f).intValue() + i;
        int intValue2 = Float.valueOf(f2).intValue();
        float applyDimension = TypedValue.applyDimension(1, (float) (heightOfTopBar + adHeight), resources.getDisplayMetrics());
        float f3 = fBlackKeyHeightDip + applyDimension;
        float f4 = (float) intValue;
        float f5 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f5 * 1.0f) + (((float) blackKeyFirstHalfMargin) * f5), resources.getDisplayMetrics())) {
            float f6 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f6 * 1.0f) + (((float) blackKeyFirstHalfMargin) * f6) + (((float) blackKeyWidth) * f6), resources.getDisplayMetrics())) {
                float f7 = (float) intValue2;
                if (f7 <= f3 && f7 >= applyDimension) {
                    return findViewById(R.id.bottom_c_s);
                }
            }
        }
        float f8 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f8 * 2.0f) + (((float) blackKeySecondHalfMargin) * f8), resources.getDisplayMetrics())) {
            float f9 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f9 * 2.0f) + (((float) blackKeySecondHalfMargin) * f9) + (((float) blackKeyWidth) * f9), resources.getDisplayMetrics())) {
                float f10 = (float) intValue2;
                if (f10 <= f3 && f10 >= applyDimension) {
                    return findViewById(R.id.bottom_d_s);
                }
            }
        }
        float f11 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f11 * 4.0f) + (((float) blackKeyFirstThirdMargin) * f11), resources.getDisplayMetrics())) {
            float f12 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f12 * 4.0f) + (((float) blackKeyFirstThirdMargin) * f12) + (((float) blackKeyWidth) * f12), resources.getDisplayMetrics())) {
                float f13 = (float) intValue2;
                if (f13 <= f3 && f13 >= applyDimension) {
                    return findViewById(R.id.bottom_f_s);
                }
            }
        }
        float f14 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f14 * 5.0f) + (((float) blackKeySecondThirdMargin) * f14), resources.getDisplayMetrics())) {
            float f15 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f15 * 5.0f) + (((float) blackKeySecondThirdMargin) * f15) + (((float) blackKeyWidth) * f15), resources.getDisplayMetrics())) {
                float f16 = (float) intValue2;
                if (f16 <= f3 && f16 >= applyDimension) {
                    return findViewById(R.id.bottom_g_s);
                }
            }
        }
        float f17 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f17 * 6.0f) + (((float) blackKeyThirdThirdMargin) * f17), resources.getDisplayMetrics())) {
            float f18 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f18 * 6.0f) + (((float) blackKeyThirdThirdMargin) * f18) + (((float) blackKeyWidth) * f18), resources.getDisplayMetrics())) {
                float f19 = (float) intValue2;
                if (f19 <= f3 && f19 >= applyDimension) {
                    return findViewById(R.id.bottom_a_s);
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 0.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 1.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_c);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 1.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 2.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_d);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 2.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 3.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_e);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 3.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 4.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_f);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 4.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 5.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_g);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 5.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 6.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_a);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 6.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 7.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.bottom_b);
        }
        float f20 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f20 * 8.0f) + (((float) blackKeyFirstHalfMargin) * f20), resources.getDisplayMetrics())) {
            float f21 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f21 * 8.0f) + (((float) blackKeyFirstHalfMargin) * f21) + (((float) blackKeyWidth) * f21), resources.getDisplayMetrics())) {
                float f22 = (float) intValue2;
                if (f22 <= f3 && f22 >= applyDimension) {
                    return findViewById(R.id.middle_c_s);
                }
            }
        }
        float f23 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f23 * 9.0f) + (((float) blackKeySecondHalfMargin) * f23), resources.getDisplayMetrics())) {
            float f24 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f24 * 9.0f) + (((float) blackKeySecondHalfMargin) * f24) + (((float) blackKeyWidth) * f24), resources.getDisplayMetrics())) {
                float f25 = (float) intValue2;
                if (f25 <= f3 && f25 >= applyDimension) {
                    return findViewById(R.id.middle_d_s);
                }
            }
        }
        float f26 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f26 * 11.0f) + (((float) blackKeyFirstThirdMargin) * f26), resources.getDisplayMetrics())) {
            float f27 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f27 * 11.0f) + (((float) blackKeyFirstThirdMargin) * f27) + (((float) blackKeyWidth) * f27), resources.getDisplayMetrics())) {
                float f28 = (float) intValue2;
                if (f28 <= f3 && f28 >= applyDimension) {
                    return findViewById(R.id.middle_f_s);
                }
            }
        }
        float f29 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f29 * 12.0f) + (((float) blackKeySecondThirdMargin) * f29), resources.getDisplayMetrics())) {
            float f30 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f30 * 12.0f) + (((float) blackKeySecondThirdMargin) * f30) + (((float) blackKeyWidth) * f30), resources.getDisplayMetrics())) {
                float f31 = (float) intValue2;
                if (f31 <= f3 && f31 >= applyDimension) {
                    return findViewById(R.id.middle_g_s);
                }
            }
        }
        float f32 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f32 * 13.0f) + (((float) blackKeyThirdThirdMargin) * f32), resources.getDisplayMetrics())) {
            float f33 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f33 * 13.0f) + (((float) blackKeyThirdThirdMargin) * f33) + (((float) blackKeyWidth) * f33), resources.getDisplayMetrics())) {
                float f34 = (float) intValue2;
                if (f34 <= f3 && f34 >= applyDimension) {
                    return findViewById(R.id.middle_a_s);
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 7.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 8.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_c);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 8.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 9.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_d);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 9.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 10.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_e);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 10.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 11.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_f);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 11.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 12.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_g);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 12.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 13.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_a);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 13.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 14.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.middle_b);
        }
        float f35 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f35 * 15.0f) + (((float) blackKeyFirstHalfMargin) * f35), resources.getDisplayMetrics())) {
            float f36 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f36 * 15.0f) + (((float) blackKeyFirstHalfMargin) * f36) + (((float) blackKeyWidth) * f36), resources.getDisplayMetrics())) {
                float f37 = (float) intValue2;
                if (f37 <= f3 && f37 >= applyDimension) {
                    return findViewById(R.id.high_c_s);
                }
            }
        }
        float f38 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f38 * 16.0f) + (((float) blackKeySecondHalfMargin) * f38), resources.getDisplayMetrics())) {
            float f39 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f39 * 16.0f) + (((float) blackKeySecondHalfMargin) * f39) + (((float) blackKeyWidth) * f39), resources.getDisplayMetrics())) {
                float f40 = (float) intValue2;
                if (f40 <= f3 && f40 >= applyDimension) {
                    return findViewById(R.id.high_d_s);
                }
            }
        }
        float f41 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f41 * 18.0f) + (((float) blackKeyFirstThirdMargin) * f41), resources.getDisplayMetrics())) {
            float f42 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f42 * 18.0f) + (((float) blackKeyFirstThirdMargin) * f42) + (((float) blackKeyWidth) * f42), resources.getDisplayMetrics())) {
                float f43 = (float) intValue2;
                if (f43 <= f3 && f43 >= applyDimension) {
                    return findViewById(R.id.high_f_s);
                }
            }
        }
        float f44 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f44 * 19.0f) + (((float) blackKeySecondThirdMargin) * f44), resources.getDisplayMetrics())) {
            float f45 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f45 * 19.0f) + (((float) blackKeySecondThirdMargin) * f45) + (((float) blackKeyWidth) * f45), resources.getDisplayMetrics())) {
                float f46 = (float) intValue2;
                if (f46 <= f3 && f46 >= applyDimension) {
                    return findViewById(R.id.high_g_s);
                }
            }
        }
        float f47 = scaleFactor;
        if (f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f47 * 20.0f) + (((float) blackKeyThirdThirdMargin) * f47), resources.getDisplayMetrics())) {
            float f48 = scaleFactor;
            if (f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * f48 * 20.0f) + (((float) blackKeyThirdThirdMargin) * f48) + (((float) blackKeyWidth) * f48), resources.getDisplayMetrics())) {
                float f49 = (float) intValue2;
                if (f49 <= f3 && f49 >= applyDimension) {
                    return findViewById(R.id.high_a_s);
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 14.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 15.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_c);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 15.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 16.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_d);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 16.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 17.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_e);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 17.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 18.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_f);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 18.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 19.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_g);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 19.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 20.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_a);
        }
        if (f4 >= TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 20.0f, resources.getDisplayMetrics()) && f4 < TypedValue.applyDimension(1, ((float) whiteKeyWidth) * scaleFactor * 21.0f, resources.getDisplayMetrics())) {
            return findViewById(R.id.high_b);
        }
        return findViewById((f4 < TypedValue.applyDimension(1, (((float) whiteKeyWidth) * scaleFactor) * 21.0f, resources.getDisplayMetrics()) || f4 >= TypedValue.applyDimension(1, (((float) whiteKeyWidth) * scaleFactor) * 22.0f, resources.getDisplayMetrics())) ? 0 : R.id.double_high_c);
    }

    private void playNoteByUser(View view, float f, boolean z) {
        if (view != null) {
            final Note note = this.soundManager.getNote(Integer.valueOf(view.getTag().toString()).intValue());
            selectNote(note);
            this.notesToRecognise.add(note);
            final View view2 = view;
            final float f2 = f;
            final boolean z2 = z;
            new Thread() {
                public void run() {
                    RecognitionActivity.this.soundManager.playSound(Integer.parseInt(view2.getTag().toString()), f2, z2);
                    RecognitionActivity.this.mHapticManager.playHapticEffect(true, RecognitionActivity.this.mHapticManager.getHapticEffect(note));
                }
            }.start();
        }
    }

    private void recognise() {
        String str;
        String str2;
        if (this.notesToRecognise.size() != 0) {
            Log.v("themelodymaster", "notesToRecognise.size() :" + this.notesToRecognise.size());
            Iterator<Note> it = this.notesToRecognise.iterator();
            while (it.hasNext()) {
                Log.v("themelodymaster", "recognise noteName:" + it.next().getName());
            }
            String recogniseChord = this.chordManager.recogniseChord(this.notesToRecognise, areFlatsOn);
            if (recogniseChord == null) {
                str = this.scalesManager.recogniseScale(this.notesToRecognise, areFlatsOn);
                if (str == null) {
                    str = "Unknown Chord/Scale";
                }
            } else {
                str = "Chord: " + recogniseChord;
            }
            setHeaderText(str);
            if (areFlatsOn) {
                Iterator<Note> it2 = this.notesToRecognise.iterator();
                String str3 = "[";
                while (it2.hasNext()) {
                    str3 = str3 + it2.next().getScaleNoteName("b ") + ", ";
                }
                if (this.notesToRecognise.size() >= 1) {
                    str3 = str3.substring(0, str3.length() - 2);
                }
                str2 = str3 + "]";
            } else {
                str2 = this.notesToRecognise.toString();
            }
            View inflate = View.inflate(this, R.layout.challenge_dialog, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.title);
            ((LinearLayout) inflate.findViewById(R.id.dialog_layout)).setBackgroundColor(-26368);
            ((TextView) inflate.findViewById(R.id.message)).setText("You played the notes " + str2 + "\n");
            final AlertDialog create = new AlertDialog.Builder(this).setTitle(str).setView(inflate).create();
            create.setButton(-1, "Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    create.dismiss();
                }
            });
            create.setButton(-3, "Next", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RecognitionActivity.isPlaying = true;
                    RecognitionActivity.this.playStopButton.setImageDrawable(RecognitionActivity.playingOn);
                    RecognitionActivity.this.unhighlightAllNotes();
                    RecognitionActivity.this.notesToRecognise.clear();
                    RecognitionActivity.this.setHeaderText("Chord/Scales Recognition");
                    create.dismiss();
                }
            });
            if (!isFinishing()) {
                create.show();
            }
        }
    }

    private void resetState(Note note) {
        if (this.focusedNoteInScaleSet.contains(note)) {
            focusNote(note);
        } else {
            cleanNote(note);
        }
    }

    private void scrollToTag(int i) {
        Log.v("themelodymaster", "scrollToTag called tag:" + i);
        if (!isAutoscrollOn) {
            return;
        }
        if (i == 0) {
            SeekBar seekBar2 = this.seekBar;
            seekBar2.setProgress(seekBar2.getMax() / 2);
            SeekBar seekBar3 = this.seekBar;
            onProgressChanged(seekBar3, seekBar3.getMax() / 2, false);
        } else if (i == 5) {
            SeekBar seekBar4 = this.seekBar;
            seekBar4.setProgress(seekBar4.getMax() / 10);
            SeekBar seekBar5 = this.seekBar;
            onProgressChanged(seekBar5, seekBar5.getMax() / 10, false);
        } else if (i >= 6 && i <= 7) {
            SeekBar seekBar6 = this.seekBar;
            seekBar6.setProgress(seekBar6.getMax() / 5);
            SeekBar seekBar7 = this.seekBar;
            onProgressChanged(seekBar7, seekBar7.getMax() / 5, false);
        } else if (i >= 8 && i <= 9) {
            SeekBar seekBar8 = this.seekBar;
            seekBar8.setProgress(seekBar8.getMax() / 4);
            SeekBar seekBar9 = this.seekBar;
            onProgressChanged(seekBar9, seekBar9.getMax() / 4, false);
        } else if (i >= 10 && i <= 11) {
            SeekBar seekBar10 = this.seekBar;
            seekBar10.setProgress(seekBar10.getMax() / 3);
            SeekBar seekBar11 = this.seekBar;
            onProgressChanged(seekBar11, seekBar11.getMax() / 3, false);
        } else if (i == 12) {
            SeekBar seekBar12 = this.seekBar;
            seekBar12.setProgress((seekBar12.getMax() * 2) / 5);
            SeekBar seekBar13 = this.seekBar;
            onProgressChanged(seekBar13, (seekBar13.getMax() * 2) / 5, false);
        } else if (i == 13 || i == 14) {
            SeekBar seekBar14 = this.seekBar;
            seekBar14.setProgress(seekBar14.getMax() / 2);
            SeekBar seekBar15 = this.seekBar;
            onProgressChanged(seekBar15, seekBar15.getMax() / 2, false);
        } else if (i > 14) {
            SeekBar seekBar16 = this.seekBar;
            seekBar16.setProgress((seekBar16.getMax() * 3) / 5);
            SeekBar seekBar17 = this.seekBar;
            onProgressChanged(seekBar17, (seekBar17.getMax() * 3) / 5, false);
        }
    }

    private void selectNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapSelected(note));
    }

    public void setHeaderText(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.v("themelodymaster", "settingText:" + str);
                RecognitionActivity.this.headerTextView.setText(str);
            }
        });
    }

    private void setKeyboardLayout(float f) {
        Iterator<TextView> it = keyLabels.iterator();
        while (it.hasNext()) {
            this.notesLayout.removeViewInLayout(it.next());
        }
        this.notesLayout.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(1, 1254.0f * f, resources.getDisplayMetrics()), -1));
        float applyDimension = TypedValue.applyDimension(1, ((float) whiteKeyWidth) * f, resources.getDisplayMetrics());
        float applyDimension2 = TypedValue.applyDimension(1, ((float) blackKeyWidth) * f, resources.getDisplayMetrics());
        float applyDimension3 = TypedValue.applyDimension(1, ((float) whiteKeyHeight) * f, resources.getDisplayMetrics());
        float applyDimension4 = ((float) resources.getDisplayMetrics().heightPixels) - TypedValue.applyDimension(1, (float) ((adHeight + adAdjustment) + heightOfTopBar), resources.getDisplayMetrics());
        if (applyDimension4 < applyDimension3) {
            fBlackKeyHeightDip = applyDimension4 * 0.6f;
        } else {
            fBlackKeyHeightDip = 0.6f * applyDimension3;
        }
        float applyDimension5 = TypedValue.applyDimension(1, ((float) blackKeyFirstHalfMargin) * f, resources.getDisplayMetrics());
        float applyDimension6 = TypedValue.applyDimension(1, ((float) blackKeySecondHalfMargin) * f, resources.getDisplayMetrics());
        float applyDimension7 = TypedValue.applyDimension(1, ((float) blackKeyFirstThirdMargin) * f, resources.getDisplayMetrics());
        float applyDimension8 = TypedValue.applyDimension(1, ((float) blackKeySecondThirdMargin) * f, resources.getDisplayMetrics());
        float applyDimension9 = TypedValue.applyDimension(1, ((float) blackKeyThirdThirdMargin) * f, resources.getDisplayMetrics());
        float applyDimension10 = TypedValue.applyDimension(1, whiteKeyLabelMarginBottom * f, resources.getDisplayMetrics());
        float applyDimension11 = TypedValue.applyDimension(1, whiteKeyLabelMarginLeft * f, resources.getDisplayMetrics());
        float applyDimension12 = TypedValue.applyDimension(1, blackKeyLabelMarginBottom * f, resources.getDisplayMetrics());
        float applyDimension13 = TypedValue.applyDimension(1, blackKeyLabelMarginLeft * f, resources.getDisplayMetrics());
        float f2 = whiteKeyLabelSize * f;
        float f3 = blackKeyLabelSize * f;
        ImageButton imageButton = (ImageButton) findViewById(R.id.bottom_c);
        int i = (int) applyDimension;
        int i2 = (int) applyDimension3;
        imageButton.setLayoutParams(new RelativeLayout.LayoutParams(i, i2));
        int i3 = i;
        int i4 = (int) applyDimension11;
        ImageButton imageButton2 = imageButton;
        float f4 = f2;
        int i5 = (int) applyDimension10;
        float f5 = applyDimension13;
        float f6 = applyDimension12;
        float f7 = applyDimension9;
        float f8 = applyDimension8;
        addLabel(imageButton, (ImageButton) null, "C3", f4, true, i5, 0, 0, i4);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.bottom_d);
        int i6 = i2;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i3, i6);
        layoutParams.addRule(1, R.id.bottom_c);
        imageButton3.setLayoutParams(layoutParams);
        int i7 = i5;
        ImageButton imageButton4 = imageButton3;
        float f9 = applyDimension7;
        addLabel(imageButton3, imageButton2, "D3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.bottom_e);
        int i8 = i6;
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(i3, i8);
        layoutParams2.addRule(1, R.id.bottom_d);
        imageButton5.setLayoutParams(layoutParams2);
        int i9 = i8;
        ImageButton imageButton6 = imageButton5;
        float f10 = applyDimension6;
        addLabel(imageButton5, imageButton4, "E3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.bottom_f);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams3.addRule(1, R.id.bottom_e);
        imageButton7.setLayoutParams(layoutParams3);
        ImageButton imageButton8 = imageButton6;
        ImageButton imageButton9 = imageButton7;
        addLabel(imageButton7, imageButton8, "F3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.bottom_g);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams4.addRule(1, R.id.bottom_f);
        imageButton10.setLayoutParams(layoutParams4);
        ImageButton imageButton11 = imageButton10;
        addLabel(imageButton10, imageButton9, "G3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton12 = (ImageButton) findViewById(R.id.bottom_a);
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams5.addRule(1, R.id.bottom_g);
        imageButton12.setLayoutParams(layoutParams5);
        ImageButton imageButton13 = imageButton12;
        addLabel(imageButton12, imageButton11, "A3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton14 = (ImageButton) findViewById(R.id.bottom_b);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams6.addRule(1, R.id.bottom_a);
        imageButton14.setLayoutParams(layoutParams6);
        ImageButton imageButton15 = imageButton14;
        addLabel(imageButton14, imageButton13, "B3", f4, true, i7, 0, 0, i4);
        ImageButton imageButton16 = (ImageButton) findViewById(R.id.middle_c);
        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams7.addRule(1, R.id.bottom_b);
        imageButton16.setLayoutParams(layoutParams7);
        ImageButton imageButton17 = imageButton15;
        ImageButton imageButton18 = imageButton16;
        addLabel(imageButton16, imageButton17, "C4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton19 = (ImageButton) findViewById(R.id.middle_d);
        RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams8.addRule(1, R.id.middle_c);
        imageButton19.setLayoutParams(layoutParams8);
        ImageButton imageButton20 = imageButton19;
        addLabel(imageButton19, imageButton18, "D4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton21 = (ImageButton) findViewById(R.id.middle_e);
        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams9.addRule(1, R.id.middle_d);
        imageButton21.setLayoutParams(layoutParams9);
        ImageButton imageButton22 = imageButton21;
        addLabel(imageButton21, imageButton20, "E4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton23 = (ImageButton) findViewById(R.id.middle_f);
        RelativeLayout.LayoutParams layoutParams10 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams10.addRule(1, R.id.middle_e);
        imageButton23.setLayoutParams(layoutParams10);
        ImageButton imageButton24 = imageButton22;
        ImageButton imageButton25 = imageButton23;
        addLabel(imageButton23, imageButton24, "F4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton26 = (ImageButton) findViewById(R.id.middle_g);
        RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams11.addRule(1, R.id.middle_f);
        imageButton26.setLayoutParams(layoutParams11);
        ImageButton imageButton27 = imageButton26;
        addLabel(imageButton26, imageButton25, "G4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton28 = (ImageButton) findViewById(R.id.middle_a);
        RelativeLayout.LayoutParams layoutParams12 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams12.addRule(1, R.id.middle_g);
        imageButton28.setLayoutParams(layoutParams12);
        ImageButton imageButton29 = imageButton28;
        addLabel(imageButton28, imageButton27, "A4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton30 = (ImageButton) findViewById(R.id.middle_b);
        RelativeLayout.LayoutParams layoutParams13 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams13.addRule(1, R.id.middle_a);
        imageButton30.setLayoutParams(layoutParams13);
        ImageButton imageButton31 = imageButton30;
        addLabel(imageButton30, imageButton29, "B4", f4, true, i7, 0, 0, i4);
        ImageButton imageButton32 = (ImageButton) findViewById(R.id.high_c);
        RelativeLayout.LayoutParams layoutParams14 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams14.addRule(1, R.id.middle_b);
        imageButton32.setLayoutParams(layoutParams14);
        ImageButton imageButton33 = imageButton31;
        ImageButton imageButton34 = imageButton32;
        addLabel(imageButton32, imageButton33, "C5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton35 = (ImageButton) findViewById(R.id.high_d);
        RelativeLayout.LayoutParams layoutParams15 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams15.addRule(1, R.id.high_c);
        imageButton35.setLayoutParams(layoutParams15);
        ImageButton imageButton36 = imageButton35;
        addLabel(imageButton35, imageButton34, "D5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton37 = (ImageButton) findViewById(R.id.high_e);
        RelativeLayout.LayoutParams layoutParams16 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams16.addRule(1, R.id.high_d);
        imageButton37.setLayoutParams(layoutParams16);
        addLabel(imageButton37, imageButton36, "E5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton38 = (ImageButton) findViewById(R.id.high_f);
        RelativeLayout.LayoutParams layoutParams17 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams17.addRule(1, R.id.high_e);
        imageButton38.setLayoutParams(layoutParams17);
        ImageButton imageButton39 = imageButton38;
        addLabel(imageButton38, imageButton37, "F5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton40 = (ImageButton) findViewById(R.id.high_g);
        RelativeLayout.LayoutParams layoutParams18 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams18.addRule(1, R.id.high_f);
        imageButton40.setLayoutParams(layoutParams18);
        ImageButton imageButton41 = imageButton40;
        addLabel(imageButton40, imageButton39, "G5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton42 = (ImageButton) findViewById(R.id.high_a);
        RelativeLayout.LayoutParams layoutParams19 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams19.addRule(1, R.id.high_g);
        imageButton42.setLayoutParams(layoutParams19);
        ImageButton imageButton43 = imageButton42;
        addLabel(imageButton42, imageButton41, "A5", f4, true, i7, 0, 0, i4);
        ImageButton imageButton44 = (ImageButton) findViewById(R.id.high_b);
        RelativeLayout.LayoutParams layoutParams20 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams20.addRule(1, R.id.high_a);
        imageButton44.setLayoutParams(layoutParams20);
        ImageButton imageButton45 = imageButton44;
        int i10 = i4;
        addLabel(imageButton44, imageButton43, "B5", f4, true, i7, 0, 0, i10);
        ImageButton imageButton46 = (ImageButton) findViewById(R.id.double_high_c);
        RelativeLayout.LayoutParams layoutParams21 = new RelativeLayout.LayoutParams(i3, i9);
        layoutParams21.addRule(1, R.id.high_b);
        imageButton46.setLayoutParams(layoutParams21);
        addLabel(imageButton46, imageButton45, "C6", f4, true, i7, 0, 0, i10);
        ImageButton imageButton47 = (ImageButton) findViewById(R.id.bottom_c_s);
        int i11 = (int) applyDimension2;
        RelativeLayout.LayoutParams layoutParams22 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams22.addRule(1, R.id.bottom_c);
        int i12 = (int) applyDimension5;
        layoutParams22.setMargins(i12, 0, 0, 0);
        imageButton47.setLayoutParams(layoutParams22);
        int i13 = (int) f5;
        int i14 = i12 + i13;
        int i15 = (int) f6;
        int i16 = i15;
        addLabel(imageButton47, imageButton2, !areFlatsOn ? "C#3" : "Db3", f3, false, i14, 0, 0, i15);
        ImageButton imageButton48 = (ImageButton) findViewById(R.id.bottom_d_s);
        RelativeLayout.LayoutParams layoutParams23 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams23.addRule(1, R.id.bottom_d);
        int i17 = (int) f10;
        layoutParams23.setMargins(i17, 0, 0, 0);
        imageButton48.setLayoutParams(layoutParams23);
        int i18 = i17 + i13;
        int i19 = i17;
        addLabel(imageButton48, imageButton4, !areFlatsOn ? "D#3" : "Eb3", f3, false, i18, 0, 0, i16);
        ImageButton imageButton49 = (ImageButton) findViewById(R.id.bottom_f_s);
        RelativeLayout.LayoutParams layoutParams24 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams24.addRule(1, R.id.bottom_f);
        int i20 = (int) f9;
        layoutParams24.setMargins(i20, 0, 0, 0);
        imageButton49.setLayoutParams(layoutParams24);
        int i21 = i20 + i13;
        int i22 = i20;
        addLabel(imageButton49, imageButton9, !areFlatsOn ? "F#3" : "Gb3", f3, false, i21, 0, 0, i16);
        ImageButton imageButton50 = (ImageButton) findViewById(R.id.bottom_g_s);
        RelativeLayout.LayoutParams layoutParams25 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams25.addRule(1, R.id.bottom_g);
        int i23 = (int) f8;
        layoutParams25.setMargins(i23, 0, 0, 0);
        imageButton50.setLayoutParams(layoutParams25);
        int i24 = i23 + i13;
        int i25 = i23;
        addLabel(imageButton50, imageButton11, !areFlatsOn ? "G#3" : "Ab3", f3, false, i24, 0, 0, i16);
        ImageButton imageButton51 = (ImageButton) findViewById(R.id.bottom_a_s);
        RelativeLayout.LayoutParams layoutParams26 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams26.addRule(1, R.id.bottom_a);
        int i26 = (int) f7;
        layoutParams26.setMargins(i26, 0, 0, 0);
        imageButton51.setLayoutParams(layoutParams26);
        int i27 = i13 + i26;
        int i28 = i26;
        addLabel(imageButton51, imageButton13, !areFlatsOn ? "A#3" : "Bb3", f3, false, i27, 0, 0, i16);
        ImageButton imageButton52 = (ImageButton) findViewById(R.id.middle_c_s);
        RelativeLayout.LayoutParams layoutParams27 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams27.addRule(1, R.id.middle_c);
        layoutParams27.setMargins(i12, 0, 0, 0);
        imageButton52.setLayoutParams(layoutParams27);
        addLabel(imageButton52, imageButton18, !areFlatsOn ? "C#4" : "Db4", f3, false, i14, 0, 0, i16);
        ImageButton imageButton53 = (ImageButton) findViewById(R.id.middle_d_s);
        RelativeLayout.LayoutParams layoutParams28 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams28.addRule(1, R.id.middle_d);
        layoutParams28.setMargins(i19, 0, 0, 0);
        imageButton53.setLayoutParams(layoutParams28);
        addLabel(imageButton53, imageButton20, !areFlatsOn ? "D#4" : "Eb4", f3, false, i18, 0, 0, i16);
        ImageButton imageButton54 = (ImageButton) findViewById(R.id.middle_f_s);
        RelativeLayout.LayoutParams layoutParams29 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams29.addRule(1, R.id.middle_f);
        int i29 = i22;
        layoutParams29.setMargins(i29, 0, 0, 0);
        imageButton54.setLayoutParams(layoutParams29);
        int i30 = i29;
        addLabel(imageButton54, imageButton25, !areFlatsOn ? "F#4" : "Gb4", f3, false, i21, 0, 0, i16);
        ImageButton imageButton55 = (ImageButton) findViewById(R.id.middle_g_s);
        RelativeLayout.LayoutParams layoutParams30 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams30.addRule(1, R.id.middle_g);
        int i31 = i25;
        layoutParams30.setMargins(i31, 0, 0, 0);
        imageButton55.setLayoutParams(layoutParams30);
        int i32 = i31;
        addLabel(imageButton55, imageButton27, !areFlatsOn ? "G#4" : "Ab4", f3, false, i24, 0, 0, i16);
        ImageButton imageButton56 = (ImageButton) findViewById(R.id.middle_a_s);
        RelativeLayout.LayoutParams layoutParams31 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams31.addRule(1, R.id.middle_a);
        int i33 = i28;
        layoutParams31.setMargins(i33, 0, 0, 0);
        imageButton56.setLayoutParams(layoutParams31);
        int i34 = i33;
        addLabel(imageButton56, imageButton29, !areFlatsOn ? "A#4" : "Bb4", f3, false, i27, 0, 0, i16);
        ImageButton imageButton57 = (ImageButton) findViewById(R.id.high_c_s);
        RelativeLayout.LayoutParams layoutParams32 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams32.addRule(1, R.id.high_c);
        layoutParams32.setMargins(i12, 0, 0, 0);
        imageButton57.setLayoutParams(layoutParams32);
        addLabel(imageButton57, imageButton34, !areFlatsOn ? "C#5" : "Db5", f3, false, i14, 0, 0, i16);
        ImageButton imageButton58 = (ImageButton) findViewById(R.id.high_d_s);
        RelativeLayout.LayoutParams layoutParams33 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams33.addRule(1, R.id.high_d);
        layoutParams33.setMargins(i19, 0, 0, 0);
        imageButton58.setLayoutParams(layoutParams33);
        addLabel(imageButton58, imageButton36, !areFlatsOn ? "D#5" : "Eb5", f3, false, i18, 0, 0, i16);
        ImageButton imageButton59 = (ImageButton) findViewById(R.id.high_f_s);
        RelativeLayout.LayoutParams layoutParams34 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams34.addRule(1, R.id.high_f);
        layoutParams34.setMargins(i30, 0, 0, 0);
        imageButton59.setLayoutParams(layoutParams34);
        addLabel(imageButton59, imageButton39, !areFlatsOn ? "F#5" : "Gb5", f3, false, i21, 0, 0, i16);
        ImageButton imageButton60 = (ImageButton) findViewById(R.id.high_g_s);
        RelativeLayout.LayoutParams layoutParams35 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams35.addRule(1, R.id.high_g);
        layoutParams35.setMargins(i32, 0, 0, 0);
        imageButton60.setLayoutParams(layoutParams35);
        addLabel(imageButton60, imageButton41, !areFlatsOn ? "G#5" : "Ab5", f3, false, i24, 0, 0, i16);
        ImageButton imageButton61 = (ImageButton) findViewById(R.id.high_a_s);
        RelativeLayout.LayoutParams layoutParams36 = new RelativeLayout.LayoutParams(i11, (int) fBlackKeyHeightDip);
        layoutParams36.addRule(1, R.id.high_a);
        layoutParams36.setMargins(i34, 0, 0, 0);
        imageButton61.setLayoutParams(layoutParams36);
        addLabel(imageButton61, imageButton43, !areFlatsOn ? "A#5" : "Bb5", f3, false, i27, 0, 0, i16);
    }

    private void showHelp() {
        startActivity(new Intent(this, RecognitionHelpActivity.class));
    }

    private void showOptions() {
        isStopDesired = true;
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void unhighlightAllNotes() {
        Log.v("themelodymaster", "Unhighlighting all notes in scale:" + scaleTitle);
        Iterator<Note> it = this.notesToRecognise.iterator();
        while (it.hasNext()) {
            Note next = it.next();
            ((ImageButton) findViewById(next.getRId())).setImageBitmap(getBitmapClean(next));
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_arrow_view) {
            onBackPressed();
        } else if (view.getId() == R.id.play_tune_button) {

                    if (isPlaying) {
                        playStopButton.setImageDrawable(playingOff);
                        isPlaying = false;
                        recognise();
                        return;
                    }
                    isPlaying = true;
                    playStopButton.setImageDrawable(playingOn);
                    unhighlightAllNotes();
                    notesToRecognise.clear();

        } else if (view.getId() == R.id.sharp_flat_button) {

                    if (areFlatsOn) {
                        areFlatsOn = false;
                        sharpFlatButton.setImageDrawable(flatsOff);
                        setKeyboardLayout(scaleFactor);
                        return;
                    }
                    areFlatsOn = true;
                    sharpFlatButton.setImageDrawable(flatsOn);
                    setKeyboardLayout(scaleFactor);

        } else if (view.getId() == R.id.refresh_button) {

                    unhighlightAllNotes();
                    notesToRecognise.clear();
                    setHeaderText("Chord/Scales Recognition");

        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.recognition);
        this.soundManager = SoundManager.getInstance(this);
        this.mHapticManager = HapticManager.getInstance(this);
        findViewById(R.id.bottom_c).setOnTouchListener(this);
        findViewById(R.id.bottom_d).setOnTouchListener(this);
        findViewById(R.id.bottom_e).setOnTouchListener(this);
        findViewById(R.id.bottom_f).setOnTouchListener(this);
        findViewById(R.id.bottom_g).setOnTouchListener(this);
        findViewById(R.id.bottom_a).setOnTouchListener(this);
        findViewById(R.id.bottom_b).setOnTouchListener(this);
        findViewById(R.id.bottom_c_s).setOnTouchListener(this);
        findViewById(R.id.bottom_d_s).setOnTouchListener(this);
        findViewById(R.id.bottom_f_s).setOnTouchListener(this);
        findViewById(R.id.bottom_g_s).setOnTouchListener(this);
        findViewById(R.id.bottom_a_s).setOnTouchListener(this);
        findViewById(R.id.middle_c).setOnTouchListener(this);
        findViewById(R.id.middle_d).setOnTouchListener(this);
        findViewById(R.id.middle_e).setOnTouchListener(this);
        findViewById(R.id.middle_f).setOnTouchListener(this);
        findViewById(R.id.middle_g).setOnTouchListener(this);
        findViewById(R.id.middle_a).setOnTouchListener(this);
        findViewById(R.id.middle_b).setOnTouchListener(this);
        findViewById(R.id.middle_c_s).setOnTouchListener(this);
        findViewById(R.id.middle_d_s).setOnTouchListener(this);
        findViewById(R.id.middle_f_s).setOnTouchListener(this);
        findViewById(R.id.middle_g_s).setOnTouchListener(this);
        findViewById(R.id.middle_a_s).setOnTouchListener(this);
        findViewById(R.id.high_c).setOnTouchListener(this);
        findViewById(R.id.high_d).setOnTouchListener(this);
        findViewById(R.id.high_e).setOnTouchListener(this);
        findViewById(R.id.high_f).setOnTouchListener(this);
        findViewById(R.id.high_g).setOnTouchListener(this);
        findViewById(R.id.high_a).setOnTouchListener(this);
        findViewById(R.id.high_b).setOnTouchListener(this);
        findViewById(R.id.high_c_s).setOnTouchListener(this);
        findViewById(R.id.high_d_s).setOnTouchListener(this);
        findViewById(R.id.high_f_s).setOnTouchListener(this);
        findViewById(R.id.high_g_s).setOnTouchListener(this);
        findViewById(R.id.high_a_s).setOnTouchListener(this);
        findViewById(R.id.double_high_c).setOnTouchListener(this);
        findViewById(R.id.back_arrow_view).setOnClickListener(this);
        findViewById(R.id.play_tune_button).setOnClickListener(this);
        findViewById(R.id.sharp_flat_button).setOnClickListener(this);
        findViewById(R.id.refresh_button).setOnClickListener(this);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seek_bar);
        this.seekBar = seekBar2;
        seekBar2.setOnSeekBarChangeListener(this);
        resources = getResources();
        ScalesManager instance = ScalesManager.getInstance(this);
        this.scalesManager = instance;
        instance.initScales(rootNote);
        ChordManager instance2 = ChordManager.getInstance(this);
        this.chordManager = instance2;
        instance2.initChords(rootNote);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notes_layout);
        this.notesLayout = relativeLayout;
        relativeLayout.measure(0, 0);
        keyLabels = new ArrayList<>();
        this.seekBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.keyboard_400x64));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.thumb);
        drawable.setAlpha(128);
        this.seekBar.setThumb(drawable);
        this.headerTextView = (TextView) findViewById(R.id.headerText);
        setHeaderText("Recognition");
        bitmapWhiteKey = BitmapFactory.decodeResource(resources, R.drawable.white_key);
        bitmapWhiteKeyFocused = BitmapFactory.decodeResource(resources, R.drawable.white_key_focused);
        bitmapWhiteKeySelected = BitmapFactory.decodeResource(resources, R.drawable.white_key_selected);
        bitmapBlackKey = BitmapFactory.decodeResource(resources, R.drawable.black_key);
        bitmapBlackKeyFocused = BitmapFactory.decodeResource(resources, R.drawable.black_key_focused);
        bitmapBlackKeySelected = BitmapFactory.decodeResource(resources, R.drawable.black_key_selected);
        if (MenuActivity.isTenInchTablet) {
            adHeight = tabletTenAdHeight;
            strDefaultKeySize = "2.0";
            this.headerTextView.setPadding((int) TypedValue.applyDimension(1, 740.0f, resources.getDisplayMetrics()), (int) TypedValue.applyDimension(1, 20.0f, resources.getDisplayMetrics()), this.headerTextView.getPaddingRight(), this.headerTextView.getPaddingBottom());
        } else if (MenuActivity.isSevenInchTablet) {
            adHeight = phoneAdHeight;
            strDefaultKeySize = "1.5";
        } else {
            adHeight = phoneAdHeight;
        }
        dipAdHeight = TypedValue.applyDimension(1, (float) adHeight, resources.getDisplayMetrics());
        dipHeightOfTopBar = TypedValue.applyDimension(1, (float) heightOfTopBar, resources.getDisplayMetrics());
        blackKeysHeightStart = heightOfTopBar + adHeight;
        new RelativeLayout.LayoutParams(-2, -2).addRule(5);
        setVolumeControlStream(3);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        bringInDefaultPrefs();
        getWindow().addFlags(128);
        this.playStopButton = (ImageView) findViewById(R.id.play_tune_button);
        playingOn = ContextCompat.getDrawable(this, R.drawable.play_btn);
        playingOff = ContextCompat.getDrawable(this, R.drawable.pause_btn);
        this.sharpFlatButton = (ImageView) findViewById(R.id.sharp_flat_button);
        flatsOff = ContextCompat.getDrawable(this, R.drawable.sharps_on);
        flatsOn = ContextCompat.getDrawable(this, R.drawable.flats_on);
        isPlaying = true;
        this.playStopButton.setImageDrawable(playingOn);
    }

    public void onDestroy() {
        isStopDesired = true;
        super.onDestroy();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.options) {
            showOptions();
            return true;
        } else if (menuItem.getItemId() == R.id.help) {
            showHelp();
            return true;
        } else if (menuItem.getItemId() != R.id.menu) {
            return true;
        } else {
            onBackPressed();
            return true;
        }
    }

    public void onPause() {
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "0");
        Utility.setLocale(this, SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en"));
        isStopDesired = true;
        SoundManager soundManager2 = this.soundManager;
        if (soundManager2 != null) {
            soundManager2.stopSounds();
        }
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.play_game_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
        float max = (((float) i) * 1.0f) / ((float) seekBar2.getMax());
        this.notesLayout.getWidth();
        this.notesLayout.getMeasuredWidth();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.notesLayout.scrollTo((int) (max * (((((float) (whiteKeyWidth * 22)) * displayMetrics.density) * scaleFactor) - ((float) displayMetrics.widthPixels))), 0);
    }


    @Override
    public void onBackPressed() {

                finish();


    }



    public void onResume() {
        super.onResume(); SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "1");
        bringInDefaultPrefs();
        this.scalesManager.initScales(rootNote);
        if (((double) scaleFactor) <= 0.5d) {
            scrollToTag(1);
        } else {
            scrollToTag(iTagFirstNote);
        }
    }

    public void onStop() {
        Log.v("themelodymaster", "onStop called.");
        isStopDesired = true;
        SoundManager soundManager2 = this.soundManager;
        if (soundManager2 != null) {
            soundManager2.stopSounds();
        }
        super.onStop();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0024, code lost:
        if (((double) r5) <= 1.0d) goto L_0x0029;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(View r19, android.view.MotionEvent r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r20
            int r2 = r20.getActionIndex()
            int r3 = r1.getPointerId(r2)
            int r4 = r20.getActionMasked()
            boolean r5 = isPressureOn
            if (r5 == 0) goto L_0x0027
            float r5 = r20.getPressure()
            r6 = 1028443341(0x3d4ccccd, float:0.05)
            float r5 = r5 - r6
            r6 = 1048576000(0x3e800000, float:0.25)
            float r5 = r5 / r6
            double r6 = (double) r5
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 > 0) goto L_0x0027
            goto L_0x0029
        L_0x0027:
            r5 = 1065353216(0x3f800000, float:1.0)
        L_0x0029:
            r6 = r19
            boolean r7 = r6 instanceof android.widget.ImageButton
            if (r7 == 0) goto L_0x01da
            java.lang.Object r7 = r19.getTag()
            if (r7 == 0) goto L_0x01da
            java.lang.Object r6 = r19.getTag()
            java.lang.String r6 = r6.toString()
            int r6 = r6.length()
            if (r6 <= 0) goto L_0x01da
            r6 = 4
            r7 = 3
            r9 = 2131231054(0x7f08014e, float:1.8078178E38)
            java.lang.String r10 = "themelodymaster"
            java.lang.String r11 = " pointerId:"
            r12 = 2
            r13 = 1
            if (r4 == 0) goto L_0x016e
            r14 = 5
            if (r4 != r14) goto L_0x0055
            goto L_0x016e
        L_0x0055:
            if (r4 == r13) goto L_0x014b
            r14 = 6
            if (r4 != r14) goto L_0x005c
            goto L_0x014b
        L_0x005c:
            int r2 = r20.getAction()
            if (r2 != r12) goto L_0x01da
            long r2 = java.lang.System.currentTimeMillis()
            int r4 = r20.getAction()
            if (r4 != r12) goto L_0x0079
            long r14 = r0.mLastTouchTime
            long r14 = r2 - r14
            r16 = 32
            int r4 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r4 >= 0) goto L_0x0079
            java.lang.Thread.sleep(r16)     // Catch:{ InterruptedException -> 0x0079 }
        L_0x0079:
            r0.mLastTouchTime = r2
            int r2 = r20.getPointerCount()
            r3 = 0
        L_0x0080:
            if (r3 >= r2) goto L_0x01da
            int r4 = r1.getPointerId(r3)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "onTouch - MotionEvent.ACTION_MOVE pointerIdx:"
            r14.append(r15)
            r14.append(r3)
            r14.append(r11)
            r14.append(r4)
            java.lang.String r14 = r14.toString()
            android.util.Log.v(r10, r14)
            if (r4 != 0) goto L_0x00c7
            float r4 = r20.getRawX()
            float r14 = r20.getRawY()
            android.widget.RelativeLayout r15 = r0.notesLayout
            int r15 = r15.getScrollX()
            android.view.View r4 = r0.getNoteFromPosition(r4, r14, r15)
            android.view.View r14 = r0.lastFinger1Button
            if (r4 == r14) goto L_0x0147
            r0.playNoteByUser(r4, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$7 r14 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$7
            r14.<init>(r4)
            r0.runOnUiThread(r14)
            r0.lastFinger1Button = r4
            goto L_0x0147
        L_0x00c7:
            if (r4 < r13) goto L_0x0147
            float r14 = r20.getRawX()
            r0.firstPointerRawX = r14
            float r14 = r1.getX(r3)
            float r15 = r1.getY(r3)
            float r16 = r20.getX()
            r20.getY()
            float r8 = r0.firstPointerRawX
            float r14 = r14 + r8
            float r14 = r14 - r16
            float r8 = dipHeightOfTopBar
            float r16 = dipAdHeight
            float r8 = r8 + r16
            float r8 = r8 + r15
            android.view.View r15 = r0.findViewById(r9)
            android.widget.RelativeLayout r15 = (android.widget.RelativeLayout) r15
            int r15 = r15.getScrollX()
            android.view.View r8 = r0.getNoteFromPosition(r14, r8, r15)
            if (r4 != r13) goto L_0x010c
            android.view.View r4 = r0.lastFinger2Button
            if (r8 == r4) goto L_0x0147
            r0.playNoteByUser(r8, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$8 r4 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$8
            r4.<init>(r8)
            r0.runOnUiThread(r4)
            r0.lastFinger2Button = r8
            goto L_0x0147
        L_0x010c:
            if (r4 != r12) goto L_0x0120
            android.view.View r4 = r0.lastFinger3Button
            if (r8 == r4) goto L_0x0147
            r0.playNoteByUser(r8, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$9 r4 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$9
            r4.<init>(r8)
            r0.runOnUiThread(r4)
            r0.lastFinger3Button = r8
            goto L_0x0147
        L_0x0120:
            if (r4 != r7) goto L_0x0134
            android.view.View r4 = r0.lastFinger4Button
            if (r8 == r4) goto L_0x0147
            r0.playNoteByUser(r8, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$10 r4 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$10
            r4.<init>(r8)
            r0.runOnUiThread(r4)
            r0.lastFinger4Button = r8
            goto L_0x0147
        L_0x0134:
            if (r4 != r6) goto L_0x0147
            android.view.View r4 = r0.lastFinger5Button
            if (r8 == r4) goto L_0x0147
            r0.playNoteByUser(r8, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$11 r4 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$11
            r4.<init>(r8)
            r0.runOnUiThread(r4)
            r0.lastFinger5Button = r8
        L_0x0147:
            int r3 = r3 + 1
            goto L_0x0080
        L_0x014b:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:"
            r1.append(r4)
            r1.append(r2)
            r1.append(r11)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r10, r1)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$6 r1 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$6
            r1.<init>(r3)
            r0.runOnUiThread(r1)
            goto L_0x01da
        L_0x016e:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r8 = "onTouch - MotionEvent.ACTION_DOWN MotionEvent.ACTION_POINTER_DOWN and pointerIdx:"
            r4.append(r8)
            r4.append(r2)
            r4.append(r11)
            r4.append(r3)
            java.lang.String r4 = r4.toString()
            android.util.Log.v(r10, r4)
            float r4 = r20.getRawX()
            r0.firstPointerRawX = r4
            float r4 = r1.getX(r2)
            float r2 = r1.getY(r2)
            float r8 = r20.getX()
            r20.getY()
            float r1 = r0.firstPointerRawX
            float r4 = r4 + r1
            float r4 = r4 - r8
            float r1 = dipHeightOfTopBar
            float r8 = dipAdHeight
            float r1 = r1 + r8
            float r1 = r1 + r2
            android.view.View r2 = r0.findViewById(r9)
            android.widget.RelativeLayout r2 = (android.widget.RelativeLayout) r2
            r0.notesLayout = r2
            int r2 = r2.getScrollX()
            android.view.View r1 = r0.getNoteFromPosition(r4, r1, r2)
            r0.playNoteByUser(r1, r5, r13)
            com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$5 r2 = new com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity$5
            r2.<init>(r1)
            r0.runOnUiThread(r2)
            if (r3 != 0) goto L_0x01c7
            r0.lastFinger1Button = r1
            goto L_0x01da
        L_0x01c7:
            if (r3 != r13) goto L_0x01cc
            r0.lastFinger2Button = r1
            goto L_0x01da
        L_0x01cc:
            if (r3 != r12) goto L_0x01d1
            r0.lastFinger3Button = r1
            goto L_0x01da
        L_0x01d1:
            if (r3 != r7) goto L_0x01d6
            r0.lastFinger4Button = r1
            goto L_0x01da
        L_0x01d6:
            if (r3 != r6) goto L_0x01da
            r0.lastFinger5Button = r1
        L_0x01da:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pianomusicdrumpad.pianokeyboard.Activity.RecognitionActivity.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }
}
