package com.pianomusicdrumpad.pianokeyboard.Piano.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd;
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ScalesManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.TuneManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Tune;
import com.pianomusicdrumpad.pianokeyboard.R;

public class UserSolosActivity extends Activity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    private static int HIGHLIGHT_HEIGHT = 0;
    public static final String LOG_TAG = "themelodymaster";
    public static int ON_MEASURE_HEIGHT = 0;
    public static int ON_MEASURE_WIDTH = 0;
    private static int adAdjustment = 18;
    private static int adHeight = 50;
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
    public static int focusNoteIdx = 0;
    private static String[] focusNoteValues = {"ALL", "1-5", "1-10", "5-15", "10-20", "15-25", "20-30", "25-35", "30-40", "35-45", "35-45", "35-45", "40-50", "45-55", "50-60", "55-65", "60-70", "65-75", "70-80", "75-85", "80-90", "85-95", "90-100", "95-105", "100-110", "105-115", "110-120", "115-125", "120-130", "125-135", "130-140", "135-145", "140-150", "150-160", "155-165", "160-170", "165-175", "170-180", "175-185", "180-190", "185-195", "190-200", "195-205", "200-210", "200-210", "205-215", "210-220", "215-225", "220-230", "225-235", "230-240", "235-245", "240-250", "ALL"};
    public static String hapticSetting = "MEDIUM";
    private static int heightOfTopBar = 34;
    public static int iTagFirstNote = 0;
    public static boolean isAdmobGotInventory = true;
    public static boolean isAutoscrollOn = true;
    public static boolean isHighlightAllNotesOn = true;
    public static boolean isHighlightFirstNoteOn = false;
    public static boolean isInPlayAlongMode = false;
    public static boolean isPlaying = false;
    public static boolean isPressureOn = false;
    public static boolean isStopDesired = false;
    private static ArrayList<TextView> keyLabels = null;
    public static String noteNamesType = "STANDARD";
    private static int phoneAdHeight = 50;
    public static int playAlongSpeed = 100;
    public static int playAlongVolume = 100;
    public static Drawable playingOff = null;
    private static Drawable playingOn = null;
    public static Resources resources = null;
    private static float scaleFactor = 1.0f;
    private static SharedPreferences sharedPrefs = null;
    public static String strDefaultKeySize = "1.1";
    private static int tabletTenAdHeight = 90;
    public static String[] tuneNames = null;
    public static int tuneNamesIdx = 0;
    private static int whiteKeyHeight = 350;
    private static float whiteKeyLabelMarginBottom = 12.0f;
    private static float whiteKeyLabelMarginLeft = 22.0f;
    private static float whiteKeyLabelSize = 24.0f;
    private static int whiteKeyWidth = 57;
    public StringBuffer actualNotesPlayed = new StringBuffer();
    public Context context;
    private StringBuffer expectedNotes = new StringBuffer();
    private float firstPointerRawX = 0.0f;
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
    public ImageView playStopButton;
    public ScalesManager scalesManager;
    private SeekBar seekBar;
    public SoundManager soundManager;

    private String mediation;
    private Object full_ad;


    public void onStartTrackingTouch(SeekBar seekBar2) {
    }

    public void onStopTrackingTouch(SeekBar seekBar2) {
    }


    public class UiThreadStart implements Runnable {
        UiThreadStart() {
        }

        public void run() {
            UserSolosActivity.focusNoteIdx = 0;
            Toast.makeText(UserSolosActivity.this.context, "The melody ain't this long. 'ALL' notes has been set or select 'Focus' to choose another range of notes.", Toast.LENGTH_SHORT).show();
        }
    }


    public class UiThreadRunnable implements Runnable {
        UiThreadRunnable() {
        }

        public void run() {
            UserSolosActivity.this.scrollToTag(UserSolosActivity.iTagFirstNote);
        }
    }


    public class NoteCountDialog implements DialogInterface.OnClickListener {
        NoteCountDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            UserSolosActivity.focusNoteIdx = i;
            dialogInterface.dismiss();
        }
    }


    public class setTuneDialog implements DialogInterface.OnClickListener {
        setTuneDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            UserSolosActivity.tuneNamesIdx = i;
            UserSolosActivity.this.setHeaderText(UserSolosActivity.tuneNames[UserSolosActivity.tuneNamesIdx]);
            dialogInterface.dismiss();
        }
    }

    private void addLabel(ImageButton imageButton, ImageButton imageButton2, String str, float f, boolean z, int i, int i2, int i3, int i4) {
        String str2;
        String str3;
        if (!noteNamesType.equals("NONE") || str.contains("c")) {
            if ("SOLFEGE".equals(noteNamesType)) {
                if (str.matches("[A-Z][3-6]")) {
                    //Log.v("LOG_TAG", "Matches label:" + str);
                    str3 = str.charAt(0) + "#" + str.charAt(1);
                } else {
                    //Log.v("LOG_TAG", "No Match label:" + str);
                    str3 = str.toUpperCase();
                }
                str2 = this.scalesManager.getSolfegeLabel(str3, "C", this.soundManager, "C Major");
            } else {
                str2 = Note.getSoundName(str);
            }
            TextView textView = new TextView(this);
            textView.setText(str2);
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
        isPlaying = false;
        isStopDesired = false;
        scaleFactor = Float.parseFloat(sharedPrefs.getString(MenuActivity.KEY_KEYSIZE, MenuActivity.defaultKeysize));
        noteNamesType = sharedPrefs.getString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames);
        playAlongVolume = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_VOLUME, MenuActivity.defaultVolume));
        playAlongSpeed = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_SPEED, MenuActivity.defaultSpeed));
        isHighlightAllNotesOn = sharedPrefs.getBoolean(MenuActivity.KEY_HIGHLIGHT_ALL_NOTES, MenuActivity.defaultHighlightAllNotes);
        isAutoscrollOn = sharedPrefs.getBoolean(MenuActivity.KEY_AUTOSCROLL, MenuActivity.defaultAutoscroll);
        isPressureOn = sharedPrefs.getBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure);
        hapticSetting = sharedPrefs.getString(MenuActivity.KEY_HAPTIC_FEEDBACK, MenuActivity.defaultHapticFeedback);
        setKeyboardLayout(scaleFactor);
        setHeaderText(tuneNames[tuneNamesIdx]);
        if (((double) scaleFactor) <= 0.5d) {
            scrollToTag(1);
        } else {
            scrollToTag(iTagFirstNote);
        }
    }

    private void checkTune() {
        if (this.expectedNotes.length() > 1) {
            StringBuffer stringBuffer = this.expectedNotes;
            stringBuffer.delete(stringBuffer.length() - 1, this.expectedNotes.length());
        }
        if (this.actualNotesPlayed.length() > 1) {
            StringBuffer stringBuffer2 = this.actualNotesPlayed;
            stringBuffer2.delete(stringBuffer2.length() - 1, this.actualNotesPlayed.length());
        }
        if (!this.expectedNotes.toString().equals(this.actualNotesPlayed.toString()) || isFinishing()) {
            AlertDialog createResultDialog = createResultDialog(false);
            if (!isFinishing()) {
                createResultDialog.show();
                createResultDialog.getWindow().setLayout(-1, -2);
                return;
            }
            return;
        }
        AlertDialog createResultDialog2 = createResultDialog(true);
        if (!isFinishing()) {
            createResultDialog2.show();
            createResultDialog2.getWindow().setLayout(-1, -2);
        }
    }

    private AlertDialog createResultDialog(boolean z) {
        String str;
        View inflate = View.inflate(this, R.layout.result_dialog, null);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.dialog_layout);
        TextView textView = (TextView) inflate.findViewById(R.id.instructionsText);
        String str2 = focusNoteValues[focusNoteIdx];
        if (z) {
            linearLayout.setBackgroundColor(-7996780);
            str = getString(R.string.correct);
            if ("ALL".equals(str2)) {
                textView.setText("Congratulations. You know it. Press 'Choose Tune' to choose a different tune.");
            } else {
                focusNoteIdx++;
                textView.setText("Well done. You've mastered that one. Select 'Play' for the next level: " + focusNoteValues[focusNoteIdx] + " or select 'Focus' for others.");
            }
        } else {
            linearLayout.setBackgroundColor(-2061695);
            str = getString(R.string.wrong);
            textView.setText("Unlucky. Press 'Play' and try again.");
        }
        ((TextView) inflate.findViewById(R.id.shouldPlayText)).setText("Should play :" + this.expectedNotes.toString());
        ((TextView) inflate.findViewById(R.id.actuallyPlayedText)).setText("You played:" + this.actualNotesPlayed.toString());
        final AlertDialog create = new AlertDialog.Builder(this).setTitle(str).setView(inflate).create();
        create.setButton(-3, getText(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                create.dismiss();
            }
        });
        return create;
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

    private void playNoteByUser(final View view, final float f, final boolean z) {
        new Thread() {
            public void run() {
                if (view != null) {
                    Note note = UserSolosActivity.this.soundManager.getNote(Integer.valueOf(view.getTag().toString()).intValue());
                    if ("SOLFEGE".equals(UserSolosActivity.noteNamesType)) {
                        StringBuffer stringBuffer = UserSolosActivity.this.actualNotesPlayed;
                        stringBuffer.append(" " + UserSolosActivity.this.scalesManager.getSolfegeLabel(note.getName(), "C", UserSolosActivity.this.soundManager, "C Major") + ",");
                    } else {
                        StringBuffer stringBuffer2 = UserSolosActivity.this.actualNotesPlayed;
                        stringBuffer2.append(" " + note.getName() + ",");
                    }
                    UserSolosActivity.this.soundManager.playSound(Integer.parseInt(view.getTag().toString()), f, z);
                    UserSolosActivity.this.mHapticManager.playHapticEffect(true, UserSolosActivity.this.mHapticManager.getHapticEffect(note));
                }
            }
        }.start();
    }

    public void playTune() {
        int i;
        int i2;
        String str = focusNoteValues[focusNoteIdx];
        boolean z = false;
        int i3 = 1;
        if ("ALL".equals(str)) {
            i2 = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
            i = 1;
        } else {
            String[] split = str.split("-");
            i = Integer.parseInt(split[0]);
            i2 = Integer.parseInt(split[1]);
        }
        TuneManager instance = TuneManager.getInstance(this);
        Tune readXml = instance.readXml(instance.getTunes().get(Integer.valueOf(tuneNamesIdx + 1)).intValue(), resources);
        //Log.v("themelodymaster", " Google Analytics Event tune:" + readXml.getTitle());
        this.expectedNotes = new StringBuffer();
        this.actualNotesPlayed = new StringBuffer();
        List<Note> notes = readXml.getNotes();
        if (i >= notes.size()) {
            runOnUiThread(new UiThreadStart());
            return;
        }
        final View findViewById = findViewById(notes.get(0).getRId());
        iTagFirstNote = Integer.parseInt(findViewById.getTag().toString());
        runOnUiThread(new UiThreadRunnable());
        int i4 = 1;
        while (i4 <= notes.size() && i4 <= i2) {
            if (i4 >= i) {
                if (isStopDesired) {
                    break;
                }
                Note note = notes.get(i4 - 1);
                if (isHighlightAllNotesOn || (isHighlightFirstNoteOn && i4 == i3)) {
                    final View findViewById2 = findViewById(note.getRId());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            findViewById2.setPressed(true);
                        }
                    });
                }
                SoundManager soundManager2 = this.soundManager;
                soundManager2.playSound(soundManager2.getNotePosition(note), ((float) playAlongVolume) / 100.0f, z);
                if ("SOLFEGE".equals(noteNamesType)) {
                    StringBuffer stringBuffer = this.expectedNotes;
                    stringBuffer.append(" " + this.scalesManager.getSolfegeLabel(note.getName(), "C", this.soundManager, "C Major") + ",");
                } else {
                    StringBuffer stringBuffer2 = this.expectedNotes;
                    stringBuffer2.append(" " + note.getName() + ",");
                }
                try {
                    Thread.sleep(Float.valueOf(((float) note.getDurationMS()) * 0.8f * (100.0f / ((float) playAlongSpeed))).longValue());
                } catch (InterruptedException unused) {
                }
                boolean z2 = isHighlightAllNotesOn;
                try {
                    Float valueOf = Float.valueOf(((float) note.getDurationMS()) * 0.2f * (100.0f / ((float) playAlongSpeed)));
                    long longValue = valueOf.longValue();
                    //Log.v("themelodymaster", "SOUND FINISHED AFTER playAlongSpeed:" + playAlongSpeed + " fDuration:" + valueOf + " longDuration:" + longValue);
                    Thread.sleep(longValue);
                } catch (InterruptedException unused2) {
                }
            }
            i4++;
            z = false;
            i3 = 1;
        }
        if (isHighlightFirstNoteOn) {
            runOnUiThread(new Runnable() {
                public void run() {
                    findViewById.setPressed(true);
                }
            });
        }
        if (!isInPlayAlongMode) {
            this.actualNotesPlayed = new StringBuffer();
        }
    }

    public void scrollToTag(int i) {
        //Log.v("themelodymaster", "scrollToTag called tag:" + i);
        if (!isAutoscrollOn) {
            return;
        }
        if (i == 0) {
            SeekBar seekBar2 = this.seekBar;
            seekBar2.setProgress(seekBar2.getMax() / 2);
            SeekBar seekBar3 = this.seekBar;
            onProgressChanged(seekBar3, seekBar3.getMax() / 2, false);
        } else if (i <= 12) {
            this.seekBar.setProgress(0);
            onProgressChanged(this.seekBar, 0, false);
        } else if (i <= 12 || i > 24) {
            SeekBar seekBar4 = this.seekBar;
            seekBar4.setProgress(seekBar4.getMax());
            SeekBar seekBar5 = this.seekBar;
            onProgressChanged(seekBar5, seekBar5.getMax(), false);
        } else {
            SeekBar seekBar6 = this.seekBar;
            seekBar6.setProgress(seekBar6.getMax() / 2);
            SeekBar seekBar7 = this.seekBar;
            onProgressChanged(seekBar7, seekBar7.getMax() / 2, false);
        }
    }

    public void setHeaderText(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                //Log.v("themelodymaster", "settingText:" + str);
                UserSolosActivity.this.headerTextView.setText(str);
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
        int i3 = (int) applyDimension10;
        int i4 = (int) applyDimension11;
        addLabel(imageButton, null, "c3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.bottom_d);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
        layoutParams.addRule(1, R.id.bottom_c);
        imageButton2.setLayoutParams(layoutParams);
        addLabel(imageButton2, imageButton, "d3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.bottom_e);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams2.addRule(1, R.id.bottom_d);
        imageButton3.setLayoutParams(layoutParams2);
        addLabel(imageButton3, imageButton2, "e3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.bottom_f);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams3.addRule(1, R.id.bottom_e);
        imageButton4.setLayoutParams(layoutParams3);
        addLabel(imageButton4, imageButton3, "f3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.bottom_g);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams4.addRule(1, R.id.bottom_f);
        imageButton5.setLayoutParams(layoutParams4);
        addLabel(imageButton5, imageButton4, "g3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton6 = (ImageButton) findViewById(R.id.bottom_a);
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams5.addRule(1, R.id.bottom_g);
        imageButton6.setLayoutParams(layoutParams5);
        addLabel(imageButton6, imageButton5, "a3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.bottom_b);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams6.addRule(1, R.id.bottom_a);
        imageButton7.setLayoutParams(layoutParams6);
        addLabel(imageButton7, imageButton6, "b3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton8 = (ImageButton) findViewById(R.id.middle_c);
        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams7.addRule(1, R.id.bottom_b);
        imageButton8.setLayoutParams(layoutParams7);
        addLabel(imageButton8, imageButton7, "c4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton9 = (ImageButton) findViewById(R.id.middle_d);
        RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams8.addRule(1, R.id.middle_c);
        imageButton9.setLayoutParams(layoutParams8);
        addLabel(imageButton9, imageButton8, "d4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.middle_e);
        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams9.addRule(1, R.id.middle_d);
        imageButton10.setLayoutParams(layoutParams9);
        addLabel(imageButton10, imageButton9, "e4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton11 = (ImageButton) findViewById(R.id.middle_f);
        RelativeLayout.LayoutParams layoutParams10 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams10.addRule(1, R.id.middle_e);
        imageButton11.setLayoutParams(layoutParams10);
        addLabel(imageButton11, imageButton10, "f4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton12 = (ImageButton) findViewById(R.id.middle_g);
        RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams11.addRule(1, R.id.middle_f);
        imageButton12.setLayoutParams(layoutParams11);
        addLabel(imageButton12, imageButton11, "g4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton13 = (ImageButton) findViewById(R.id.middle_a);
        RelativeLayout.LayoutParams layoutParams12 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams12.addRule(1, R.id.middle_g);
        imageButton13.setLayoutParams(layoutParams12);
        addLabel(imageButton13, imageButton12, "a4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton14 = (ImageButton) findViewById(R.id.middle_b);
        RelativeLayout.LayoutParams layoutParams13 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams13.addRule(1, R.id.middle_a);
        imageButton14.setLayoutParams(layoutParams13);
        addLabel(imageButton14, imageButton13, "b4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton15 = (ImageButton) findViewById(R.id.high_c);
        RelativeLayout.LayoutParams layoutParams14 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams14.addRule(1, R.id.middle_b);
        imageButton15.setLayoutParams(layoutParams14);
        addLabel(imageButton15, imageButton14, "c5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton16 = (ImageButton) findViewById(R.id.high_d);
        RelativeLayout.LayoutParams layoutParams15 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams15.addRule(1, R.id.high_c);
        imageButton16.setLayoutParams(layoutParams15);
        addLabel(imageButton16, imageButton15, "d5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton17 = (ImageButton) findViewById(R.id.high_e);
        RelativeLayout.LayoutParams layoutParams16 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams16.addRule(1, R.id.high_d);
        imageButton17.setLayoutParams(layoutParams16);
        addLabel(imageButton17, imageButton16, "e5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton18 = (ImageButton) findViewById(R.id.high_f);
        RelativeLayout.LayoutParams layoutParams17 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams17.addRule(1, R.id.high_e);
        imageButton18.setLayoutParams(layoutParams17);
        addLabel(imageButton18, imageButton17, "f5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton19 = (ImageButton) findViewById(R.id.high_g);
        RelativeLayout.LayoutParams layoutParams18 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams18.addRule(1, R.id.high_f);
        imageButton19.setLayoutParams(layoutParams18);
        addLabel(imageButton19, imageButton18, "g5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton20 = (ImageButton) findViewById(R.id.high_a);
        RelativeLayout.LayoutParams layoutParams19 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams19.addRule(1, R.id.high_g);
        imageButton20.setLayoutParams(layoutParams19);
        addLabel(imageButton20, imageButton19, "a5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton21 = (ImageButton) findViewById(R.id.high_b);
        RelativeLayout.LayoutParams layoutParams20 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams20.addRule(1, R.id.high_a);
        imageButton21.setLayoutParams(layoutParams20);
        addLabel(imageButton21, imageButton20, "b5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton22 = (ImageButton) findViewById(R.id.double_high_c);
        RelativeLayout.LayoutParams layoutParams21 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams21.addRule(1, R.id.high_b);
        imageButton22.setLayoutParams(layoutParams21);
        addLabel(imageButton22, imageButton21, "c6", f2, true, i3, 0, 0, i4);
        ImageButton imageButton23 = (ImageButton) findViewById(R.id.bottom_c_s);
        int i5 = (int) applyDimension2;
        RelativeLayout.LayoutParams layoutParams22 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams22.addRule(1, R.id.bottom_c);
        int i6 = (int) applyDimension5;
        layoutParams22.setMargins(i6, 0, 0, 0);
        imageButton23.setLayoutParams(layoutParams22);
        int i7 = (int) applyDimension13;
        int i8 = i6 + i7;
        int i9 = (int) applyDimension12;
        addLabel(imageButton23, imageButton, "C3", f3, false, i8, 0, 0, i9);
        ImageButton imageButton24 = (ImageButton) findViewById(R.id.bottom_d_s);
        RelativeLayout.LayoutParams layoutParams23 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams23.addRule(1, R.id.bottom_d);
        int i10 = (int) applyDimension6;
        layoutParams23.setMargins(i10, 0, 0, 0);
        imageButton24.setLayoutParams(layoutParams23);
        int i11 = i10 + i7;
        addLabel(imageButton24, imageButton2, "D3", f3, false, i11, 0, 0, i9);
        ImageButton imageButton25 = (ImageButton) findViewById(R.id.bottom_f_s);
        RelativeLayout.LayoutParams layoutParams24 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams24.addRule(1, R.id.bottom_f);
        int i12 = (int) applyDimension7;
        layoutParams24.setMargins(i12, 0, 0, 0);
        imageButton25.setLayoutParams(layoutParams24);
        int i13 = i12 + i7;
        addLabel(imageButton25, imageButton4, "F3", f3, false, i13, 0, 0, i9);
        ImageButton imageButton26 = (ImageButton) findViewById(R.id.bottom_g_s);
        RelativeLayout.LayoutParams layoutParams25 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams25.addRule(1, R.id.bottom_g);
        int i14 = (int) applyDimension8;
        layoutParams25.setMargins(i14, 0, 0, 0);
        imageButton26.setLayoutParams(layoutParams25);
        int i15 = i14 + i7;
        addLabel(imageButton26, imageButton5, "G3", f3, false, i15, 0, 0, i9);
        ImageButton imageButton27 = (ImageButton) findViewById(R.id.bottom_a_s);
        RelativeLayout.LayoutParams layoutParams26 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams26.addRule(1, R.id.bottom_a);
        int i16 = (int) applyDimension9;
        layoutParams26.setMargins(i16, 0, 0, 0);
        imageButton27.setLayoutParams(layoutParams26);
        int i17 = i7 + i16;
        addLabel(imageButton27, imageButton6, "A3", f3, false, i17, 0, 0, i9);
        ImageButton imageButton28 = (ImageButton) findViewById(R.id.middle_c_s);
        RelativeLayout.LayoutParams layoutParams27 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams27.addRule(1, R.id.middle_c);
        layoutParams27.setMargins(i6, 0, 0, 0);
        imageButton28.setLayoutParams(layoutParams27);
        addLabel(imageButton28, imageButton8, "C4", f3, false, i8, 0, 0, i9);
        ImageButton imageButton29 = (ImageButton) findViewById(R.id.middle_d_s);
        RelativeLayout.LayoutParams layoutParams28 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams28.addRule(1, R.id.middle_d);
        layoutParams28.setMargins(i10, 0, 0, 0);
        imageButton29.setLayoutParams(layoutParams28);
        addLabel(imageButton29, imageButton9, "D4", f3, false, i11, 0, 0, i9);
        ImageButton imageButton30 = (ImageButton) findViewById(R.id.middle_f_s);
        RelativeLayout.LayoutParams layoutParams29 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams29.addRule(1, R.id.middle_f);
        layoutParams29.setMargins(i12, 0, 0, 0);
        imageButton30.setLayoutParams(layoutParams29);
        addLabel(imageButton30, imageButton11, "F4", f3, false, i13, 0, 0, i9);
        ImageButton imageButton31 = (ImageButton) findViewById(R.id.middle_g_s);
        RelativeLayout.LayoutParams layoutParams30 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams30.addRule(1, R.id.middle_g);
        layoutParams30.setMargins(i14, 0, 0, 0);
        imageButton31.setLayoutParams(layoutParams30);
        addLabel(imageButton31, imageButton12, "G4", f3, false, i15, 0, 0, i9);
        ImageButton imageButton32 = (ImageButton) findViewById(R.id.middle_a_s);
        RelativeLayout.LayoutParams layoutParams31 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams31.addRule(1, R.id.middle_a);
        layoutParams31.setMargins(i16, 0, 0, 0);
        imageButton32.setLayoutParams(layoutParams31);
        addLabel(imageButton32, imageButton13, "A4", f3, false, i17, 0, 0, i9);
        ImageButton imageButton33 = (ImageButton) findViewById(R.id.high_c_s);
        RelativeLayout.LayoutParams layoutParams32 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams32.addRule(1, R.id.high_c);
        layoutParams32.setMargins(i6, 0, 0, 0);
        imageButton33.setLayoutParams(layoutParams32);
        addLabel(imageButton33, imageButton15, "C5", f3, false, i8, 0, 0, i9);
        ImageButton imageButton34 = (ImageButton) findViewById(R.id.high_d_s);
        RelativeLayout.LayoutParams layoutParams33 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams33.addRule(1, R.id.high_d);
        layoutParams33.setMargins(i10, 0, 0, 0);
        imageButton34.setLayoutParams(layoutParams33);
        addLabel(imageButton34, imageButton16, "D5", f3, false, i11, 0, 0, i9);
        ImageButton imageButton35 = (ImageButton) findViewById(R.id.high_f_s);
        RelativeLayout.LayoutParams layoutParams34 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams34.addRule(1, R.id.high_f);
        layoutParams34.setMargins(i12, 0, 0, 0);
        imageButton35.setLayoutParams(layoutParams34);
        addLabel(imageButton35, imageButton18, "F5", f3, false, i13, 0, 0, i9);
        ImageButton imageButton36 = (ImageButton) findViewById(R.id.high_g_s);
        RelativeLayout.LayoutParams layoutParams35 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams35.addRule(1, R.id.high_g);
        layoutParams35.setMargins(i14, 0, 0, 0);
        imageButton36.setLayoutParams(layoutParams35);
        addLabel(imageButton36, imageButton19, "G5", f3, false, i15, 0, 0, i9);
        ImageButton imageButton37 = (ImageButton) findViewById(R.id.high_a_s);
        RelativeLayout.LayoutParams layoutParams36 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams36.addRule(1, R.id.high_a);
        layoutParams36.setMargins(i16, 0, 0, 0);
        imageButton37.setLayoutParams(layoutParams36);
        addLabel(imageButton37, imageButton20, "A5", f3, false, i17, 0, 0, i9);
    }

    private void setNoteCount() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R.string.select_note_count)).setSingleChoiceItems(focusNoteValues, focusNoteIdx, new NoteCountDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    private void setTune() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R.string.select_user_tune)).setSingleChoiceItems(tuneNames, tuneNamesIdx, new setTuneDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    private void showHelp() {
        startActivity(new Intent(this, UserSolosHelpActivity.class));
    }

    private void showOptions() {
        isStopDesired = true;
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_arrow_view) {
            onBackPressed();
        } else if (view.getId() == R.id.choose_tune_button) {

                    setTune();

        } else if (view.getId() == R.id.play_tune_button) {

                    if (isPlaying) {
                        isStopDesired = true;
                        return;
                    }
                    isPlaying = true;
                    playStopButton.setImageDrawable(playingOn);
                    new Thread() {
                        class RunnableThread implements Runnable {
                            RunnableThread() {
                            }

                            public void run() {
                                UserSolosActivity.this.playStopButton.setImageDrawable(UserSolosActivity.playingOff);
                            }
                        }

                        public void run() {
                            UserSolosActivity.this.playTune();
                            UserSolosActivity.isPlaying = false;
                            UserSolosActivity.isStopDesired = false;
                            UserSolosActivity.this.runOnUiThread(new RunnableThread());
                        }
                    }.start();

        } else if (view.getId() == R.id.check_button) {

                    checkTune();

        } else if (view.getId() == R.id.note_count_button) {

                    setNoteCount();

        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.user_solos);


        mediation = SharePrefUtils.getString(ConstantAd.MEDIATION, "0");

        this.soundManager = SoundManager.getInstance(this);
        this.mHapticManager = HapticManager.getInstance(this);
        this.scalesManager = ScalesManager.getInstance(this);
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
        findViewById(R.id.choose_tune_button).setOnClickListener(this);
        findViewById(R.id.play_tune_button).setOnClickListener(this);
        findViewById(R.id.check_button).setOnClickListener(this);
        findViewById(R.id.note_count_button).setOnClickListener(this);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seek_bar);
        this.seekBar = seekBar2;
        seekBar2.setOnSeekBarChangeListener(this);
        this.context = getApplicationContext();
        resources = getResources();
        tuneNames = TuneManager.getInstance(this).populateTuneNames(resources);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notes_layout);
        this.notesLayout = relativeLayout;
        relativeLayout.measure(0, 0);
        keyLabels = new ArrayList<>();
        this.seekBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.keyboard_400x64));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.thumb);
        drawable.setAlpha(128);
        this.seekBar.setThumb(drawable);
        this.headerTextView = (TextView) findViewById(R.id.headerText);
        setHeaderText("User Solos");
        if (MenuActivity.isTenInchTablet) {
            adHeight = tabletTenAdHeight;
            strDefaultKeySize = "2.0";
            this.headerTextView.setTextSize(2, 24.0f);
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
        playingOn = ContextCompat.getDrawable(this, R.drawable.play_stop_playing);
        playingOff = ContextCompat.getDrawable(this, R.drawable.play_stop);
    }

    public void onDestroy() {
        isStopDesired = true;
        super.onDestroy();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.options) {
            //Log.v("themelodymaster", "Options selected");
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

    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "1");
        bringInDefaultPrefs();
        this.expectedNotes = new StringBuffer();
        this.actualNotesPlayed = new StringBuffer();
    }

    public void onStop() {
        //Log.v("themelodymaster", "onStop called.");
        isStopDesired = true;
        SoundManager soundManager2 = this.soundManager;
        if (soundManager2 != null) {
            soundManager2.stopSounds();
        }
        super.onStop();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        float f;
        int actionIndex = motionEvent.getActionIndex();
        final int pointerId = motionEvent.getPointerId(actionIndex);
        int actionMasked = motionEvent.getActionMasked();
        if (isPressureOn) {
            f = (motionEvent.getPressure() - 0.05f) / 0.25f;
        }
        f = 1.0f;
        if (!(view instanceof ImageButton) || view.getTag() == null || view.getTag().toString().length() <= 0) {
            return false;
        }
        if (actionMasked == 0 || actionMasked == 5) {
            //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_DOWN MotionEvent.ACTION_POINTER_DOWN and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
            this.firstPointerRawX = motionEvent.getRawX();
            float x = motionEvent.getX(actionIndex);
            float y = motionEvent.getY(actionIndex);
            float x2 = motionEvent.getX();
            motionEvent.getY();
            float f2 = (x + this.firstPointerRawX) - x2;
            float f3 = dipHeightOfTopBar + dipAdHeight + y;
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notes_layout);
            this.notesLayout = relativeLayout;
            final View noteFromPosition = getNoteFromPosition(f2, f3, relativeLayout.getScrollX());
            playNoteByUser(noteFromPosition, f, true);
            runOnUiThread(new Runnable() {
                public void run() {
                    View view = noteFromPosition;
                    if (view != null) {
                        view.setPressed(true);
                    }
                }
            });
            if (pointerId == 0) {
                this.lastFinger1Button = noteFromPosition;
                return false;
            } else if (pointerId == 1) {
                this.lastFinger2Button = noteFromPosition;
                return false;
            } else if (pointerId == 2) {
                this.lastFinger3Button = noteFromPosition;
                return false;
            } else if (pointerId == 3) {
                this.lastFinger4Button = noteFromPosition;
                return false;
            } else if (pointerId != 4) {
                return false;
            } else {
                this.lastFinger5Button = noteFromPosition;
                return false;
            }
        } else if (actionMasked == 1 || actionMasked == 6) {
            //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (pointerId == 0 && UserSolosActivity.this.lastFinger1Button != null) {
                        UserSolosActivity.this.lastFinger1Button.setPressed(false);
                    }
                    int i = pointerId;
                    if (i == 1) {
                        if (UserSolosActivity.this.lastFinger2Button != null) {
                            UserSolosActivity.this.lastFinger2Button.setPressed(false);
                        }
                    } else if (i == 2) {
                        if (UserSolosActivity.this.lastFinger3Button != null) {
                            UserSolosActivity.this.lastFinger3Button.setPressed(false);
                        }
                    } else if (i == 3) {
                        if (UserSolosActivity.this.lastFinger4Button != null) {
                            UserSolosActivity.this.lastFinger4Button.setPressed(false);
                        }
                    } else if (i == 4 && UserSolosActivity.this.lastFinger5Button != null) {
                        UserSolosActivity.this.lastFinger5Button.setPressed(false);
                    }
                }
            });
            return false;
        } else if (motionEvent.getAction() != 2) {
            return false;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (motionEvent.getAction() == 2 && currentTimeMillis - this.mLastTouchTime < 32) {
                try {
                    Thread.sleep(32);
                } catch (InterruptedException unused) {
                }
            }
            this.mLastTouchTime = currentTimeMillis;
            int pointerCount = motionEvent.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                int pointerId2 = motionEvent.getPointerId(i);
                //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_MOVE pointerIdx:" + i + " pointerId:" + pointerId2);
                if (pointerId2 == 0) {
                    final View noteFromPosition2 = getNoteFromPosition(motionEvent.getRawX(), motionEvent.getRawY(), this.notesLayout.getScrollX());
                    if (noteFromPosition2 != this.lastFinger1Button) {
                        playNoteByUser(noteFromPosition2, f, true);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                View view = noteFromPosition2;
                                if (view != null) {
                                    view.setPressed(true);
                                }
                                if (UserSolosActivity.this.lastFinger1Button != null) {
                                    UserSolosActivity.this.lastFinger1Button.setPressed(false);
                                }
                            }
                        });
                        this.lastFinger1Button = noteFromPosition2;
                    }
                } else if (pointerId2 >= 1) {
                    this.firstPointerRawX = motionEvent.getRawX();
                    float x3 = motionEvent.getX(i);
                    float y2 = motionEvent.getY(i);
                    float x4 = motionEvent.getX();
                    motionEvent.getY();
                    final View noteFromPosition3 = getNoteFromPosition((x3 + this.firstPointerRawX) - x4, dipHeightOfTopBar + dipAdHeight + y2, ((RelativeLayout) findViewById(R.id.notes_layout)).getScrollX());
                    if (pointerId2 == 1) {
                        if (noteFromPosition3 != this.lastFinger2Button) {
                            playNoteByUser(noteFromPosition3, f, true);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (UserSolosActivity.this.lastFinger2Button != null) {
                                        UserSolosActivity.this.lastFinger2Button.setPressed(false);
                                    }
                                    View view = noteFromPosition3;
                                    if (view != null) {
                                        view.setPressed(true);
                                    }
                                }
                            });
                            this.lastFinger2Button = noteFromPosition3;
                        }
                    } else if (pointerId2 == 2) {
                        if (noteFromPosition3 != this.lastFinger3Button) {
                            playNoteByUser(noteFromPosition3, f, true);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (UserSolosActivity.this.lastFinger3Button != null) {
                                        UserSolosActivity.this.lastFinger3Button.setPressed(false);
                                    }
                                    View view = noteFromPosition3;
                                    if (view != null) {
                                        view.setPressed(true);
                                    }
                                }
                            });
                            this.lastFinger3Button = noteFromPosition3;
                        }
                    } else if (pointerId2 == 3) {
                        if (noteFromPosition3 != this.lastFinger4Button) {
                            playNoteByUser(noteFromPosition3, f, true);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (UserSolosActivity.this.lastFinger4Button != null) {
                                        UserSolosActivity.this.lastFinger4Button.setPressed(false);
                                    }
                                    View view = noteFromPosition3;
                                    if (view != null) {
                                        view.setPressed(true);
                                    }
                                }
                            });
                            this.lastFinger4Button = noteFromPosition3;
                        }
                    } else if (pointerId2 == 4 && noteFromPosition3 != this.lastFinger5Button) {
                        playNoteByUser(noteFromPosition3, f, true);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (UserSolosActivity.this.lastFinger5Button != null) {
                                    UserSolosActivity.this.lastFinger5Button.setPressed(false);
                                }
                                View view = noteFromPosition3;
                                if (view != null) {
                                    view.setPressed(true);
                                }
                            }
                        });
                        this.lastFinger5Button = noteFromPosition3;
                    }
                }
            }
            return false;
        }
    }


    @Override
    public void onBackPressed() {

                finish();

    }
}
