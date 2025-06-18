package com.pianomusicdrumpad.pianokeyboard.Piano.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd;
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ProgressHelper;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ScalesManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Scale;
import com.pianomusicdrumpad.pianokeyboard.R;

public class ScalesGameActivity extends Activity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    public static final int DURATION = 400;
    private static int HIGHLIGHT_HEIGHT = 0;
    public static final String LOG_TAG = "themelodymaster";
    private static final String MY_RANDOM_GROUP = "My Random Root Notes";
    public static final int NO_OF_SCALES_IN_EVERYTHING = 71;
    public static int ON_MEASURE_HEIGHT = 0;
    public static int ON_MEASURE_WIDTH = 0;
    private static final String RANDOM_ALL = "Random (ALL)";
    private static final String RANDOM_ONE = "Random (C,F,G)";
    private static final String RANDOM_SCALE_DIRECTION = "Random";
    private static final String RANDOM_TWO = "Random (C,F,G,D,A,E,Bb,Eb,Ab)";
    private static int adAdjustment = 18;
    private static int adHeight = 50;
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
    public static String calculatedRootNote = "C";
    private static String calculatedScaleDirection = "ASCENDING";
    private static int counterInChallenge = -1;
    public static float density = 1.0f;
    private static float dipAdHeight = 50.0f;
    private static float dipHeightOfTopBar = 34.0f;
    private static float fBlackKeyHeightDip = 0.0f;
    public static String hapticSetting = "MEDIUM";
    public static boolean hasResultDisplayed = true;
    private static int heightOfTopBar = 34;
    public static int iTagFirstNote = 0;
    public static boolean isAFlatScale = false;
    public static boolean isAdmobGotInventory = true;
    public static boolean isAgainWanted = false;
    public static boolean isAutoscrollOn = true;
    public static boolean isHighlightAllNotesOn = true;
    public static boolean isPlaying = false;
    public static boolean isPressureOn = false;
    public static boolean isResultDialogInView = false;
    public static boolean isStopDesired = false;
    private static ArrayList<TextView> keyLabels = null;
    public static String[] levelArray = {"Beginner", "Easy", "Medium", "Rare", "Modes", "Arpeggios", "Everything", "My Focus Group"};
    public static int levelIdx = 0;
    public static String noteNamesType = "STANDARD";
    private static int numberCorrect = 0;
    private static int phoneAdHeight = 50;
    public static int playAlongSpeed = 100;
    public static int playAlongVolume = 100;
    public static Drawable playingOff = null;
    private static Drawable playingOn = null;
    public static Resources resources = null;
    public static String rootNote = "C";
    private static String[] rootNoteRandomPoolAll = {"C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"};
    private static String[] rootNoteRandomPoolMyGroup = {"C", "G"};
    private static String[] rootNoteRandomPoolOne = {"C", "F", "G"};
    private static String[] rootNoteRandomPoolTwo = {"C", "F", "G", "D", "A", "E", "Bb", "Eb", "Ab"};
    public static String[] rootNoteValues = {RANDOM_ONE, RANDOM_TWO, RANDOM_ALL, MY_RANDOM_GROUP, "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"};
    public static int rootNotesIdx = 0;
    private static Scale scale = null;
    public static int scaleDirectionIdx = 0;
    private static String[] scaleDirectionRandomPool = {"ASCENDING", "DESCENDING", "ASCENDING - DESCENDING", "DESCENDING - ASCENDING"};
    public static String scaleDirectionSetting = "ASCENDING";
    public static String[] scaleDirectionValues = {"ASCENDING", "DESCENDING", "ASCENDING - DESCENDING", "DESCENDING - ASCENDING", RANDOM_SCALE_DIRECTION};
    public static float scaleFactor = 1.0f;
    public static String[] scaleNames = null;
    public static int scaleNamesIdx = 0;
    public static String scaleTitle = "C Major";
    private static List<Scale> scales = null;
    public static SharedPreferences sharedPrefs = null;
    public static String strDefaultKeySize = "1.1";
    private static int tabletTenAdHeight = 90;
    private static int totalNumberOfConfirms = 0;
    private static int whiteKeyHeight = 350;
    private static float whiteKeyLabelMarginBottom = 12.0f;
    private static float whiteKeyLabelMarginLeft = 22.0f;
    private static float whiteKeyLabelSize = 24.0f;
    private static int whiteKeyWidth = 57;
    String[] a;
    private StringBuffer actualNotesPlayed = new StringBuffer();
    private StringBuffer expectedNotes = new StringBuffer();
    private String firstNotePrefix = null;
    private float firstPointerRawX = 0.0f;
    private Set<Note> focusedNoteInScaleSet = new HashSet();
    Handler handler = new Handler();
    private boolean hasFacebookFailed = false;
    public TextView headerTextView;
    public View lastFinger1Button = null;
    public View lastFinger2Button = null;
    public View lastFinger3Button = null;
    public View lastFinger4Button = null;
    public View lastFinger5Button = null;
    private Note lastPlayedUserNote = null;
    public HapticManager mHapticManager;
    private long mLastTouchTime = 0;
    private Note note22;
    private List<Note> notesCopy;
    private RelativeLayout notesLayout = null;
    public ImageView playStopButton;
    private Random random;
    public ScalesManager scalesManager;
    private SeekBar seekBar;
    public boolean setNextBreak = false;
    public SoundManager soundManager;
    public boolean[] userDefinedRootNoteFlags;
    public boolean[] userDefinedScaleFlags;

    private String mediation;
    private Object full_ad;
    LinearLayout adBannerAd;

    private void showHelp() {
    }

    public void onStartTrackingTouch(SeekBar seekBar2) {
    }

    public void onStopTrackingTouch(SeekBar seekBar2) {
    }


    public class UiThreadStart implements Runnable {
        UiThreadStart() {
        }

        public void run() {
            ScalesGameActivity.this.initScroll();
        }
    }


    public class RootNoteDialog implements DialogInterface.OnClickListener {
        RootNoteDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i != ScalesGameActivity.rootNotesIdx) {
                ScalesGameActivity.this.writeScore();
            }
            ScalesGameActivity.rootNotesIdx = i;
            ScalesGameActivity.rootNote = ScalesGameActivity.rootNoteValues[ScalesGameActivity.rootNotesIdx];
            ScalesGameActivity.this.createCalculatedRootNote();
            dialogInterface.dismiss();
            if (ScalesGameActivity.rootNote.equals(ScalesGameActivity.MY_RANDOM_GROUP)) {
                ScalesGameActivity.this.designMyRandomRootNoteGroup();
                return;
            }
            ScalesGameActivity.this.createCalculatedRootNote();
            if (Arrays.asList(ScalesGameActivity.rootNoteValues).contains(ScalesGameActivity.rootNote)) {
                ScalesGameActivity.rootNotesIdx = Arrays.asList(ScalesGameActivity.rootNoteValues).indexOf(ScalesGameActivity.rootNote);
            }
            ScalesGameActivity.this.userDefinedScaleFlags = new boolean[71];
            List<Scale> scales = ScalesGameActivity.this.scalesManager.getScales(6, ScalesGameActivity.calculatedRootNote);
            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
            scalesGameActivity.a = scalesGameActivity.scalesManager.getScalesNames(scales);
            for (int i2 = 0; i2 < 71; i2++) {
                boolean[] zArr = ScalesGameActivity.this.userDefinedScaleFlags;
                SharedPreferences sharedPreferences = ScalesGameActivity.sharedPrefs;
                zArr[i2] = sharedPreferences.getBoolean("userDefinedScaleFlags" + i2, false);
            }
            ScalesGameActivity.this.setUpScales();
            ScalesGameActivity.scaleTitle = ScalesGameActivity.scaleNames[ScalesGameActivity.scaleNamesIdx];
            ScalesGameActivity.this.setHeaderText(ScalesGameActivity.scaleTitle);
            ScalesGameActivity.this.setKeyboardLayout(ScalesGameActivity.scaleFactor);
            ScalesGameActivity.this.initScroll();
            ScalesGameActivity.hasResultDisplayed = true;
            ScalesGameActivity.isAgainWanted = false;
        }
    }


    public class selectLevelDialog implements DialogInterface.OnClickListener {
        selectLevelDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i != ScalesGameActivity.levelIdx || i == ScalesGameActivity.levelArray.length - 1) {
                ScalesGameActivity.this.writeScore();
            }
            ScalesGameActivity.levelIdx = i;
            dialogInterface.dismiss();
            if (ScalesGameActivity.levelIdx == 7) {
                ScalesGameActivity.this.designMyFocusGroup();
            }
            ScalesGameActivity.this.setUpScales();
            ScalesGameActivity.scaleNamesIdx = 0;
            SharedPreferences.Editor edit = ScalesGameActivity.sharedPrefs.edit();
            edit.putInt(MenuActivity.KEY_SCALE_LEVEL_IDX, ScalesGameActivity.levelIdx);
            edit.apply();
        }
    }

    class ScaleDialog implements DialogInterface.OnClickListener {
        ScaleDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            ScalesGameActivity.scaleNamesIdx = i;
            dialogInterface.dismiss();
            ScalesGameActivity.iTagFirstNote = ScalesGameActivity.this.soundManager.getNotePosition(ScalesGameActivity.this.scalesManager.getScales().get(ScalesGameActivity.scaleNamesIdx).getNotes().get(0));
            ScalesGameActivity.this.initScroll();
            ScalesGameActivity.this.setScaleTitle();
        }
    }

    private void addLabel(ImageButton imageButton, ImageButton imageButton2, String str, float f, boolean z, int i, int i2, int i3, int i4) {
        if (!noteNamesType.equals("NONE") || (str.contains("C") && !str.contains("b") && !str.contains("#"))) {
            if ("SOLFEGE".equals(noteNamesType)) {
                str = this.scalesManager.getSolfegeLabel(str, calculatedRootNote, this.soundManager, scaleTitle);
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

    public void bringInDefaultPrefs() {
        hasResultDisplayed = true;
        isPlaying = false;
        isStopDesired = false;
        this.setNextBreak = false;
        scaleFactor = Float.parseFloat(sharedPrefs.getString(MenuActivity.KEY_KEYSIZE, MenuActivity.defaultKeysize));
        noteNamesType = sharedPrefs.getString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames);
        playAlongVolume = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_VOLUME, MenuActivity.defaultVolume));
        playAlongSpeed = Integer.parseInt(sharedPrefs.getString(MenuActivity.KEY_PLAYALONG_SPEED, MenuActivity.defaultSpeed));
        isHighlightAllNotesOn = sharedPrefs.getBoolean(MenuActivity.KEY_HIGHLIGHT_ALL_NOTES, MenuActivity.defaultHighlightAllNotes);
        isAutoscrollOn = sharedPrefs.getBoolean(MenuActivity.KEY_AUTOSCROLL, MenuActivity.defaultAutoscroll);
        isPressureOn = sharedPrefs.getBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure);
        hapticSetting = sharedPrefs.getString(MenuActivity.KEY_HAPTIC_FEEDBACK, MenuActivity.defaultHapticFeedback);
        rootNote = sharedPrefs.getString(MenuActivity.KEY_ROOT_NOTE_INCLUDING_RANDOM, MenuActivity.defaultRootNoteIncludingRandom);
        levelIdx = sharedPrefs.getInt(MenuActivity.KEY_SCALE_LEVEL_IDX, MenuActivity.defaultScaleLevelIdx);
        scaleDirectionSetting = sharedPrefs.getString(MenuActivity.KEY_SCALE_DIRECTION, MenuActivity.defaultScaleDirection);
        if (Arrays.asList(scaleDirectionValues).contains(scaleDirectionSetting)) {
            scaleDirectionIdx = Arrays.asList(scaleDirectionValues).indexOf(scaleDirectionSetting);
        }
        if (!Arrays.asList(rootNoteValues).contains(rootNote)) {
            rootNote = MenuActivity.defaultRootNoteIncludingRandom;
        }
        rootNotesIdx = Arrays.asList(rootNoteValues).indexOf(rootNote);
        this.userDefinedRootNoteFlags = new boolean[rootNoteRandomPoolAll.length];
        createCalculatedRootNote();
        for (int i = 0; i < rootNoteRandomPoolAll.length; i++) {
            boolean[] zArr = this.userDefinedRootNoteFlags;
            SharedPreferences sharedPreferences = sharedPrefs;
            zArr[i] = sharedPreferences.getBoolean("userDefinedRootNoteFlags" + i, false);
        }
        this.userDefinedScaleFlags = new boolean[71];
        ScalesManager scalesManager2 = this.scalesManager;
        this.a = scalesManager2.getScalesNames(scalesManager2.getScales(6, calculatedRootNote));
        for (int i2 = 0; i2 < 71; i2++) {
            boolean[] zArr2 = this.userDefinedScaleFlags;
            SharedPreferences sharedPreferences2 = sharedPrefs;
            zArr2[i2] = sharedPreferences2.getBoolean("userDefinedScaleFlags" + i2, false);
        }
        setKeyboardLayout(scaleFactor);
        initScroll();
        setUpScales();
    }

    public void calculateScaleDirection() {
        if (scaleDirectionSetting.equals(RANDOM_SCALE_DIRECTION)) {
            String[] strArr = scaleDirectionRandomPool;
            calculatedScaleDirection = strArr[this.random.nextInt(strArr.length)];
        } else {
            calculatedScaleDirection = scaleDirectionSetting;
        }
        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putString(MenuActivity.KEY_SCALE_DIRECTION, calculatedScaleDirection);
        edit.apply();
    }

    private void cleanNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
    }

    private void cleanNotesInScale() {
        //Log.v("themelodymaster", "Unhighlighting all notes in scale:" + scaleTitle);
        for (Note note : this.focusedNoteInScaleSet) {
            ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
        }
        this.focusedNoteInScaleSet.clear();
    }

    public void createCalculatedRootNote() {
        if (rootNote.equals(RANDOM_ONE)) {
            String[] strArr = rootNoteRandomPoolOne;
            calculatedRootNote = strArr[this.random.nextInt(strArr.length)];
        } else if (rootNote.equals(RANDOM_TWO)) {
            String[] strArr2 = rootNoteRandomPoolTwo;
            calculatedRootNote = strArr2[this.random.nextInt(strArr2.length)];
        } else if (rootNote.equals(RANDOM_ALL)) {
            String[] strArr3 = rootNoteRandomPoolAll;
            calculatedRootNote = strArr3[this.random.nextInt(strArr3.length)];
        } else if (rootNote.equals(MY_RANDOM_GROUP)) {
            ArrayList arrayList = new ArrayList();
            int i = 0;
            while (true) {
                boolean[] zArr = this.userDefinedRootNoteFlags;
                if (i >= zArr.length) {
                    break;
                }
                if (zArr[i]) {
                    arrayList.add(rootNoteRandomPoolAll[i]);
                }
                i++;
            }
            if (arrayList.size() <= 1) {
                arrayList.add("C");
                arrayList.add("G");
            }
            calculatedRootNote = (String) arrayList.get(this.random.nextInt(arrayList.size()));
        } else {
            calculatedRootNote = rootNote;
        }
        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putString(MenuActivity.KEY_ROOT_NOTE_INCLUDING_RANDOM, rootNote);
        edit.apply();
    }

    public AlertDialog createResultDialog(final boolean z) {
        String str;
        isResultDialogInView = true;
        View inflate = View.inflate(this, R.layout.result_dialog, null);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.dialog_layout);
        TextView textView = (TextView) inflate.findViewById(R.id.instructionsText);
        if (z) {
            linearLayout.setBackgroundColor(-7996780);
            str = scale.getTitle() + " " + calculatedScaleDirection;
            textView.setText("Congratulations. You know it. Press 'Next' for the next challenge.");
        } else {
            linearLayout.setBackgroundColor(-2061695);
            str = scale.getTitle() + " " + calculatedScaleDirection;
            textView.setText("Unlucky. Press 'Again' to try again.");
        }
        ((TextView) inflate.findViewById(R.id.shouldPlayText)).setText("Should play:" + this.expectedNotes.toString());
        ((TextView) inflate.findViewById(R.id.actuallyPlayedText)).setText(" You played:" + this.actualNotesPlayed.toString());
        ((TextView) inflate.findViewById(R.id.numberCorrectText)).setText("Number Correct: " + numberCorrect + ".");
        ((TextView) inflate.findViewById(R.id.totalPlaysText)).setText("Number of Goes:" + totalNumberOfConfirms + ".");
        ((TextView) inflate.findViewById(R.id.scoreText)).setText("Score: " + Math.round((((float) numberCorrect) * 100.0f) / ((float) totalNumberOfConfirms)) + "%.");
        final AlertDialog create = new AlertDialog.Builder(this).setTitle(str).setView(inflate).create();
        if (!z) {
            create.setButton(-1, "Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ScalesGameActivity.isResultDialogInView = false;
                    create.dismiss();
                    ScalesGameActivity.isAgainWanted = true;
                    ScalesGameActivity.this.playGame();
                }
            });
        }
        create.setButton(-3, "See Scale", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ScalesGameActivity.isResultDialogInView = false;
                create.dismiss();
                ScalesGameActivity.this.playScaleInit(z);
            }
        });
        create.setButton(-2, "Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ScalesGameActivity.isResultDialogInView = false;
                create.dismiss();
                ScalesGameActivity.this.playGame();
            }
        });
        return create;
    }

    public void designMyFocusGroup() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Scales").setMultiChoiceItems(this.a, this.userDefinedScaleFlags, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                ScalesGameActivity.this.userDefinedScaleFlags[i] = z;
            }
        }).setNegativeButton("None", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ScalesGameActivity.this.userDefinedScaleFlags.length; i2++) {
                    ScalesGameActivity.this.userDefinedScaleFlags[i2] = false;
                }
                ScalesGameActivity.this.designMyFocusGroup();
            }
        }).setNeutralButton("ALL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ScalesGameActivity.this.userDefinedScaleFlags.length; i2++) {
                    ScalesGameActivity.this.userDefinedScaleFlags[i2] = true;
                }
                ScalesGameActivity.this.designMyFocusGroup();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialogInterface, int i) {
                boolean[] zArr = ScalesGameActivity.this.userDefinedScaleFlags;
                int length = zArr.length;
                int i2 = 0;
                while (i2 < length && !zArr[i2]) {
                    i2++;
                }
                //Log.v("themelodymaster", "userDefinedScaleFlags length:" + ScalesGameActivity.this.userDefinedScaleFlags.length);
                SharedPreferences.Editor edit = ScalesGameActivity.sharedPrefs.edit();
                edit.putInt("userDefinedScaleFlags_length", ScalesGameActivity.this.userDefinedScaleFlags.length);
                for (int i3 = 0; i3 < ScalesGameActivity.this.userDefinedScaleFlags.length; i3++) {
                    edit.putBoolean("userDefinedScaleFlags" + i3, ScalesGameActivity.this.userDefinedScaleFlags[i3]);
                }
                edit.apply();
                ScalesGameActivity.this.bringInDefaultPrefs();
                dialogInterface.dismiss();
            }
        }).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
            create.getWindow().setLayout(-1, -2);
        }
    }

    public void designMyRandomRootNoteGroup() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Root Notes").setMultiChoiceItems(rootNoteRandomPoolAll, this.userDefinedRootNoteFlags, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                ScalesGameActivity.this.userDefinedRootNoteFlags[i] = z;
            }
        }).setNegativeButton("None", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ScalesGameActivity.this.userDefinedRootNoteFlags.length; i2++) {
                    ScalesGameActivity.this.userDefinedRootNoteFlags[i2] = false;
                }
                ScalesGameActivity.this.designMyRandomRootNoteGroup();
            }
        }).setNeutralButton("ALL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ScalesGameActivity.this.userDefinedRootNoteFlags.length; i2++) {
                    ScalesGameActivity.this.userDefinedRootNoteFlags[i2] = true;
                }
                ScalesGameActivity.this.designMyRandomRootNoteGroup();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean[] zArr = ScalesGameActivity.this.userDefinedRootNoteFlags;
                int length = zArr.length;
                int i2 = 0;
                while (i2 < length && !zArr[i2]) {
                    i2++;
                }
                //Log.v("themelodymaster", "userDefinedRootNoteFlags length:" + ScalesGameActivity.this.userDefinedRootNoteFlags.length);
                SharedPreferences.Editor edit = ScalesGameActivity.sharedPrefs.edit();
                edit.putInt("userDefinedRootNoteFlags_length", ScalesGameActivity.this.userDefinedRootNoteFlags.length);
                for (int i3 = 0; i3 < ScalesGameActivity.this.userDefinedRootNoteFlags.length; i3++) {
                    edit.putBoolean("userDefinedRootNoteFlags" + i3, ScalesGameActivity.this.userDefinedRootNoteFlags[i3]);
                }
                edit.apply();
                ScalesGameActivity.this.createCalculatedRootNote();
                if (Arrays.asList(ScalesGameActivity.rootNoteValues).contains(ScalesGameActivity.rootNote)) {
                    ScalesGameActivity.rootNotesIdx = Arrays.asList(ScalesGameActivity.rootNoteValues).indexOf(ScalesGameActivity.rootNote);
                }
                ScalesGameActivity.this.userDefinedScaleFlags = new boolean[71];
                ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                scalesGameActivity.a = scalesGameActivity.scalesManager.getScalesNames(ScalesGameActivity.this.scalesManager.getScales(6, ScalesGameActivity.calculatedRootNote));
                for (int i4 = 0; i4 < 71; i4++) {
                    boolean[] zArr2 = ScalesGameActivity.this.userDefinedScaleFlags;
                    SharedPreferences sharedPreferences = ScalesGameActivity.sharedPrefs;
                    zArr2[i4] = sharedPreferences.getBoolean("userDefinedScaleFlags" + i4, false);
                }
                ScalesGameActivity.this.setUpScales();
                ScalesGameActivity.scaleTitle = ScalesGameActivity.scaleNames[ScalesGameActivity.scaleNamesIdx];
                ScalesGameActivity.this.setHeaderText(ScalesGameActivity.scaleTitle);
                ScalesGameActivity.this.setKeyboardLayout(ScalesGameActivity.scaleFactor);
                ScalesGameActivity.this.initScroll();
                ScalesGameActivity.hasResultDisplayed = true;
                ScalesGameActivity.isAgainWanted = false;
                ScalesGameActivity.hasResultDisplayed = true;
                ScalesGameActivity.isAgainWanted = false;
                ScalesGameActivity.this.bringInDefaultPrefs();
                dialogInterface.dismiss();
            }
        }).create();
        if (!isFinishing()) {
            create.show();
        }
    }

    private void displayResult() {
        StringBuffer stringBuffer = this.expectedNotes;
        if (stringBuffer != null && stringBuffer.length() > 2) {
            isAgainWanted = false;
            isPlaying = false;
            if (this.expectedNotes.length() > 1) {
                StringBuffer stringBuffer2 = this.expectedNotes;
                stringBuffer2.delete(stringBuffer2.length() - 1, this.expectedNotes.length());
            }
            if (this.actualNotesPlayed.length() > 1) {
                StringBuffer stringBuffer3 = this.actualNotesPlayed;
                stringBuffer3.delete(stringBuffer3.length() - 1, this.actualNotesPlayed.length());
            }
            totalNumberOfConfirms++;
            if (!this.expectedNotes.toString().equals(this.actualNotesPlayed.toString()) || isFinishing()) {
                AlertDialog createResultDialog = createResultDialog(false);
                if (!isFinishing()) {
                    createResultDialog.show();
                    createResultDialog.getWindow().setLayout(-1, -2);
                }
            } else {
                numberCorrect++;
                AlertDialog createResultDialog2 = createResultDialog(true);
                if (!isFinishing()) {
                    createResultDialog2.show();
                    createResultDialog2.getWindow().setLayout(-1, -2);
                }
            }
            hasResultDisplayed = true;
        }
    }

    private void focusNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapFocus(note));
    }

    private void focusNotesInScale() {
        //Log.v("themelodymaster", "Highlighting all notes in scale:" + scaleTitle);
        for (Note note : this.scalesManager.getScales().get(scaleNamesIdx).getNotes()) {
            //Log.v("themelodymaster", "highlight drawing...");
            ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapFocus(note));
            this.focusedNoteInScaleSet.add(note);
        }
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

    private String getNoteFromStringBuffer(StringBuffer stringBuffer, int i) {
        String[] split = stringBuffer.toString().split(",");
        return i >= split.length ? "" : split[i];
    }

    private void highlightToScale() {
        cleanNotesInScale();
        scaleTitle = scaleNames[scaleNamesIdx];
        //Log.v("themelodymaster", "scaleTitle in setTune:" + scaleTitle);
        isAFlatScale = Note.isAFlatScale(scaleTitle);
        focusNotesInScale();
        setHeaderText(scaleTitle);
    }

    public void initScroll() {
        if (calculatedRootNote.contains("Eb")) {
            SeekBar seekBar2 = this.seekBar;
            seekBar2.setProgress((seekBar2.getMax() * 5) / 10);
            SeekBar seekBar3 = this.seekBar;
            onProgressChanged(seekBar3, (seekBar3.getMax() * 5) / 10, false);
        } else if (calculatedRootNote.contains("E") || calculatedRootNote.contains("F")) {
            SeekBar seekBar4 = this.seekBar;
            seekBar4.setProgress((seekBar4.getMax() * 1) / 10);
            SeekBar seekBar5 = this.seekBar;
            onProgressChanged(seekBar5, (seekBar5.getMax() * 1) / 10, false);
        } else if (calculatedRootNote.contains("G") || calculatedRootNote.contains("A")) {
            SeekBar seekBar6 = this.seekBar;
            seekBar6.setProgress((seekBar6.getMax() * 3) / 10);
            SeekBar seekBar7 = this.seekBar;
            onProgressChanged(seekBar7, (seekBar7.getMax() * 3) / 10, false);
        } else if (calculatedRootNote.contains("B") || calculatedRootNote.contains("C")) {
            SeekBar seekBar8 = this.seekBar;
            seekBar8.setProgress((seekBar8.getMax() * 4) / 10);
            SeekBar seekBar9 = this.seekBar;
            onProgressChanged(seekBar9, (seekBar9.getMax() * 4) / 10, false);
        } else {
            SeekBar seekBar10 = this.seekBar;
            seekBar10.setProgress((seekBar10.getMax() * 5) / 10);
            SeekBar seekBar11 = this.seekBar;
            onProgressChanged(seekBar11, (seekBar11.getMax() * 5) / 10, false);
        }
    }

    public void playGame() {
        this.expectedNotes = new StringBuffer();
        this.actualNotesPlayed = new StringBuffer();
        counterInChallenge = -1;
        if (hasResultDisplayed && !isAgainWanted) {
            createCalculatedRootNote();
            //Log.v("themelodymaster", " Play levelIdx:" + levelIdx + " calculatedRootNote:" + calculatedRootNote);
            List<Scale> scales2 = this.scalesManager.getScales(levelIdx, calculatedRootNote);
            scales = scales2;
            if (scales2 != null && scales2.size() != 0) {
                List<Scale> list = scales;
                scale = list.get(this.random.nextInt(list.size()));
                calculateScaleDirection();
                isAFlatScale = Note.isAFlatScale(scale.getTitle());
                setKeyboardLayout(scaleFactor);
                hasResultDisplayed = false;
            } else {
                return;
            }
        }
        List<Note> notes = scale.getNotes();
        List<Note> arrayList = new ArrayList<>();
        for (Note note : notes) {
            arrayList.add(note);
        }
        String title = scale.getTitle();
        scaleTitle = title;
        setHeaderText(title);
        if ("DESCENDING".equals(calculatedScaleDirection)) {
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale2 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale2 != null) {
                    arrayList = scale2.getNotes();
                }
            } else {
                Collections.reverse(arrayList);
            }
        } else if ("ASCENDING - DESCENDING".equals(calculatedScaleDirection)) {
            this.notesCopy = new ArrayList();
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale3 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale3 != null) {
                    this.notesCopy = scale3.getNotes();
                }
            } else {
                for (Note note2 : arrayList) {
                    this.notesCopy.add(new Note(note2.getName(), String.valueOf(note2.getDurationMS())));
                }
                Collections.reverse(this.notesCopy);
            }
            arrayList.addAll(this.notesCopy);
            arrayList.remove(this.notesCopy.size());
        } else if ("DESCENDING - ASCENDING".equals(calculatedScaleDirection)) {
            this.notesCopy = new ArrayList();
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale4 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale4 != null) {
                    this.notesCopy = scale4.getNotes();
                }
                arrayList.remove(0);
                this.notesCopy.addAll(arrayList);
                arrayList.clear();
                for (Note note3 : this.notesCopy) {
                    arrayList.add(new Note(note3.getName(), String.valueOf(note3.getDurationMS())));
                }
            } else {
                for (Note note4 : arrayList) {
                    this.notesCopy.add(new Note(note4.getName(), String.valueOf(note4.getDurationMS())));
                }
                Collections.reverse(arrayList);
                arrayList.addAll(this.notesCopy);
                arrayList.remove(this.notesCopy.size());
            }
        }
        iTagFirstNote = this.soundManager.getNotePosition(arrayList.get(0));
        initScroll();
        for (int i = 0; i < arrayList.size(); i++) {
            this.note22 = arrayList.get(i);
            if ("SOLFEGE".equals(noteNamesType)) {
                StringBuffer stringBuffer = this.expectedNotes;
                stringBuffer.append(" " + this.scalesManager.getSolfegeLabel(this.note22.getScaleNoteName(scaleTitle), calculatedRootNote, this.soundManager, scaleTitle) + ",");
            } else {
                StringBuffer stringBuffer2 = this.expectedNotes;
                stringBuffer2.append(" " + this.note22.getScaleNoteName(scaleTitle) + ",");
            }
        }
        if (!isAgainWanted) {
            View inflate = View.inflate(this, R.layout.challenge_dialog, null);
            TextView textView = (TextView) inflate.findViewById(R.id.title);
            ((LinearLayout) inflate.findViewById(R.id.dialog_layout)).setBackgroundColor(-26368);
            ((TextView) inflate.findViewById(R.id.message)).setText("Play " + scaleTitle + " " + calculatedScaleDirection + " on the keyboard and then select 'Confirm'(✔)\n");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog create = builder.setTitle("Play" + scaleTitle + " " + calculatedScaleDirection).setView(inflate).create();
            create.setButton(-3, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    create.dismiss();
                }
            });
            if (!isFinishing()) {
                create.show();
                create.getWindow().setLayout(-1, -2);
            }
        }
    }

    private void playNoteByUser(final View view, final float f, final boolean z) {
        if (view != null) {
            counterInChallenge++;
            final Note note = this.soundManager.getNote(Integer.valueOf(view.getTag().toString()).intValue());
            if ("SOLFEGE".equals(noteNamesType)) {
                this.actualNotesPlayed.append(" " + this.scalesManager.getSolfegeLabel(note.getScaleNoteName(scaleTitle), calculatedRootNote, this.soundManager, scaleTitle) + ",");
            } else {
                this.actualNotesPlayed.append(" " + note.getScaleNoteName(scaleTitle) + ",");
            }
            selectNote(note);
            this.lastPlayedUserNote = note;
            new Thread() {
                public void run() {
                    ScalesGameActivity.this.soundManager.playSound(Integer.parseInt(view.getTag().toString()), f, z);
                    ScalesGameActivity.this.mHapticManager.playHapticEffect(true, ScalesGameActivity.this.mHapticManager.getHapticEffect(note));
                }
            }.start();
            if (!isResultDialogInView) {
                if (!getNoteFromStringBuffer(this.expectedNotes, counterInChallenge).equals(getNoteFromStringBuffer(this.actualNotesPlayed, counterInChallenge))) {
                    displayResult();
                } else if (this.expectedNotes.toString().equals(this.actualNotesPlayed.toString())) {
                    displayResult();
                } else if (this.actualNotesPlayed.length() > this.expectedNotes.length()) {
                    displayResult();
                }
            }
        }
    }

    public void playScale() {
        List<Note> notes = scale.getNotes();
        List<Note> arrayList = new ArrayList<>();
        for (Note note : notes) {
            arrayList.add(note);
        }
        scaleTitle = scale.getTitle();
        Note note2 = arrayList.get(0);
        iTagFirstNote = this.soundManager.getNotePosition(note2);
        findViewById(note2.getRId());
        runOnUiThread(new UiThreadStart());
        if ("DESCENDING".equals(calculatedScaleDirection)) {
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale2 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale2 != null) {
                    arrayList = scale2.getNotes();
                }
            } else {
                Collections.reverse(arrayList);
            }
        } else if ("ASCENDING - DESCENDING".equals(calculatedScaleDirection)) {
            this.notesCopy = new ArrayList();
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale3 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale3 != null) {
                    this.notesCopy = scale3.getNotes();
                }
            } else {
                for (Note note3 : arrayList) {
                    this.notesCopy.add(new Note(note3.getName(), String.valueOf(note3.getDurationMS())));
                }
                Collections.reverse(this.notesCopy);
            }
            arrayList.addAll(this.notesCopy);
            arrayList.remove(this.notesCopy.size());
        } else if ("DESCENDING - ASCENDING".equals(calculatedScaleDirection)) {
            this.notesCopy = new ArrayList();
            if (scaleTitle.contains(ScalesManager.TYPE_SCALE_MINOR_MELODIC)) {
                Scale scale4 = this.scalesManager.getScale(rootNote, ScalesManager.TYPE_SCALE_MINOR_MELODIC_DESC, 0, false);
                if (scale4 != null) {
                    this.notesCopy = scale4.getNotes();
                }
                arrayList.remove(0);
                this.notesCopy.addAll(arrayList);
                arrayList.clear();
                for (Note note4 : this.notesCopy) {
                    arrayList.add(new Note(note4.getName(), String.valueOf(note4.getDurationMS())));
                }
            } else {
                for (Note note5 : arrayList) {
                    this.notesCopy.add(new Note(note5.getName(), String.valueOf(note5.getDurationMS())));
                }
                Collections.reverse(arrayList);
                arrayList.addAll(this.notesCopy);
                arrayList.remove(this.notesCopy.size());
            }
        }
        for (int i = 1; i <= arrayList.size(); i++) {
            if (isStopDesired || this.setNextBreak) {
                this.setNextBreak = false;
                return;
            }
            Note note6 = arrayList.get(i - 1);
            this.note22 = note6;
            if (isHighlightAllNotesOn) {
                final View findViewById = findViewById(note6.getRId());
                runOnUiThread(new Runnable() {
                   public void run() {
                        ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                        scalesGameActivity.selectNote(scalesGameActivity.soundManager.getNote(Integer.valueOf(findViewById.getTag().toString()).intValue()));
                    }
                });
            }
            SoundManager soundManager2 = this.soundManager;
            soundManager2.playSound(soundManager2.getNotePosition(this.note22), ((float) playAlongVolume) / 100.0f, true);
            try {
                Thread.sleep(Float.valueOf(((float) this.note22.getDurationMS()) * 0.8f * (100.0f / ((float) playAlongSpeed))).longValue());
            } catch (InterruptedException unused) {
            }
            if (isHighlightAllNotesOn) {
                final ImageButton imageButton = (ImageButton) findViewById(this.note22.getRId());
                runOnUiThread(new Runnable() {
                   public void run() {
                        ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                        scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(imageButton.getTag().toString()).intValue()));
                    }
                });
            }
            try {
                Float valueOf = Float.valueOf(((float) this.note22.getDurationMS()) * 0.2f * (100.0f / ((float) playAlongSpeed)));
                long longValue = valueOf.longValue();
                //Log.v("themelodymaster", "SOUND FINISHED AFTER playAlongSpeed:" + playAlongSpeed + " fDuration:" + valueOf + " longDuration:" + longValue);
                Thread.sleep(longValue);
            } catch (InterruptedException unused2) {
            }
        }
    }

    public void playScaleInit(final boolean z) {
        isPlaying = true;
        this.playStopButton.setImageDrawable(playingOn);
        new Thread() {
           class ScaleInitRunnable implements Runnable {
                ScaleInitRunnable() {
                }

                public void run() {
                    ScalesGameActivity.this.playStopButton.setImageDrawable(ScalesGameActivity.playingOff);
                    AlertDialog createResultDialog = ScalesGameActivity.this.createResultDialog(z);
                    if (!ScalesGameActivity.this.isFinishing()) {
                        createResultDialog.show();
                        createResultDialog.getWindow().setLayout(-1, -2);
                    }
                }
            }

            public void run() {
                ScalesGameActivity.this.playScale();
                ScalesGameActivity.isPlaying = false;
                ScalesGameActivity.isStopDesired = false;
                ScalesGameActivity.this.setNextBreak = false;
                ScalesGameActivity.this.runOnUiThread(new ScaleInitRunnable());
            }
        }.start();
    }

    public void resetState(Note note) {
        if (this.focusedNoteInScaleSet.contains(note)) {
            focusNote(note);
        } else {
            cleanNote(note);
        }
    }

    private void selectLevel() {
        hasResultDisplayed = true;
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Level").setSingleChoiceItems(levelArray, levelIdx, new selectLevelDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    public void selectNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapSelected(note));
    }

    public void setHeaderText(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                //Log.v("themelodymaster", "settingText:" + str);
                ScalesGameActivity.this.headerTextView.setText(str);
            }
        });
    }

    public void setKeyboardLayout(float f) {
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
        addLabel(imageButton, null, "C3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.bottom_d);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
        layoutParams.addRule(1, R.id.bottom_c);
        imageButton2.setLayoutParams(layoutParams);
        addLabel(imageButton2, imageButton, "D3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.bottom_e);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams2.addRule(1, R.id.bottom_d);
        imageButton3.setLayoutParams(layoutParams2);
        addLabel(imageButton3, imageButton2, "E3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.bottom_f);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams3.addRule(1, R.id.bottom_e);
        imageButton4.setLayoutParams(layoutParams3);
        addLabel(imageButton4, imageButton3, "F3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton5 = (ImageButton) findViewById(R.id.bottom_g);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams4.addRule(1, R.id.bottom_f);
        imageButton5.setLayoutParams(layoutParams4);
        addLabel(imageButton5, imageButton4, "G3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton6 = (ImageButton) findViewById(R.id.bottom_a);
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams5.addRule(1, R.id.bottom_g);
        imageButton6.setLayoutParams(layoutParams5);
        addLabel(imageButton6, imageButton5, "A3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.bottom_b);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams6.addRule(1, R.id.bottom_a);
        imageButton7.setLayoutParams(layoutParams6);
        addLabel(imageButton7, imageButton6, "B3", f2, true, i3, 0, 0, i4);
        ImageButton imageButton8 = (ImageButton) findViewById(R.id.middle_c);
        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams7.addRule(1, R.id.bottom_b);
        imageButton8.setLayoutParams(layoutParams7);
        addLabel(imageButton8, imageButton7, "C4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton9 = (ImageButton) findViewById(R.id.middle_d);
        RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams8.addRule(1, R.id.middle_c);
        imageButton9.setLayoutParams(layoutParams8);
        addLabel(imageButton9, imageButton8, "D4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton10 = (ImageButton) findViewById(R.id.middle_e);
        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams9.addRule(1, R.id.middle_d);
        imageButton10.setLayoutParams(layoutParams9);
        addLabel(imageButton10, imageButton9, "E4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton11 = (ImageButton) findViewById(R.id.middle_f);
        RelativeLayout.LayoutParams layoutParams10 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams10.addRule(1, R.id.middle_e);
        imageButton11.setLayoutParams(layoutParams10);
        addLabel(imageButton11, imageButton10, "F4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton12 = (ImageButton) findViewById(R.id.middle_g);
        RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams11.addRule(1, R.id.middle_f);
        imageButton12.setLayoutParams(layoutParams11);
        addLabel(imageButton12, imageButton11, "G4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton13 = (ImageButton) findViewById(R.id.middle_a);
        RelativeLayout.LayoutParams layoutParams12 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams12.addRule(1, R.id.middle_g);
        imageButton13.setLayoutParams(layoutParams12);
        addLabel(imageButton13, imageButton12, "A4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton14 = (ImageButton) findViewById(R.id.middle_b);
        RelativeLayout.LayoutParams layoutParams13 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams13.addRule(1, R.id.middle_a);
        imageButton14.setLayoutParams(layoutParams13);
        addLabel(imageButton14, imageButton13, "B4", f2, true, i3, 0, 0, i4);
        ImageButton imageButton15 = (ImageButton) findViewById(R.id.high_c);
        RelativeLayout.LayoutParams layoutParams14 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams14.addRule(1, R.id.middle_b);
        imageButton15.setLayoutParams(layoutParams14);
        addLabel(imageButton15, imageButton14, "C5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton16 = (ImageButton) findViewById(R.id.high_d);
        RelativeLayout.LayoutParams layoutParams15 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams15.addRule(1, R.id.high_c);
        imageButton16.setLayoutParams(layoutParams15);
        addLabel(imageButton16, imageButton15, "D5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton17 = (ImageButton) findViewById(R.id.high_e);
        RelativeLayout.LayoutParams layoutParams16 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams16.addRule(1, R.id.high_d);
        imageButton17.setLayoutParams(layoutParams16);
        addLabel(imageButton17, imageButton16, "E5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton18 = (ImageButton) findViewById(R.id.high_f);
        RelativeLayout.LayoutParams layoutParams17 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams17.addRule(1, R.id.high_e);
        imageButton18.setLayoutParams(layoutParams17);
        addLabel(imageButton18, imageButton17, "F5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton19 = (ImageButton) findViewById(R.id.high_g);
        RelativeLayout.LayoutParams layoutParams18 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams18.addRule(1, R.id.high_f);
        imageButton19.setLayoutParams(layoutParams18);
        addLabel(imageButton19, imageButton18, "G5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton20 = (ImageButton) findViewById(R.id.high_a);
        RelativeLayout.LayoutParams layoutParams19 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams19.addRule(1, R.id.high_g);
        imageButton20.setLayoutParams(layoutParams19);
        addLabel(imageButton20, imageButton19, "A5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton21 = (ImageButton) findViewById(R.id.high_b);
        RelativeLayout.LayoutParams layoutParams20 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams20.addRule(1, R.id.high_a);
        imageButton21.setLayoutParams(layoutParams20);
        addLabel(imageButton21, imageButton20, "B5", f2, true, i3, 0, 0, i4);
        ImageButton imageButton22 = (ImageButton) findViewById(R.id.double_high_c);
        RelativeLayout.LayoutParams layoutParams21 = new RelativeLayout.LayoutParams(i, i2);
        layoutParams21.addRule(1, R.id.high_b);
        imageButton22.setLayoutParams(layoutParams21);
        addLabel(imageButton22, imageButton21, "C6", f2, true, i3, 0, 0, i4);
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
        addLabel(imageButton23, imageButton, !isAFlatScale ? "C#3" : "Db3", f3, false, i8, 0, 0, i9);
        ImageButton imageButton24 = (ImageButton) findViewById(R.id.bottom_d_s);
        RelativeLayout.LayoutParams layoutParams23 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams23.addRule(1, R.id.bottom_d);
        int i10 = (int) applyDimension6;
        layoutParams23.setMargins(i10, 0, 0, 0);
        imageButton24.setLayoutParams(layoutParams23);
        int i11 = i10 + i7;
        addLabel(imageButton24, imageButton2, !isAFlatScale ? "D#3" : "Eb3", f3, false, i11, 0, 0, i9);
        ImageButton imageButton25 = (ImageButton) findViewById(R.id.bottom_f_s);
        RelativeLayout.LayoutParams layoutParams24 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams24.addRule(1, R.id.bottom_f);
        int i12 = (int) applyDimension7;
        layoutParams24.setMargins(i12, 0, 0, 0);
        imageButton25.setLayoutParams(layoutParams24);
        int i13 = i12 + i7;
        addLabel(imageButton25, imageButton4, !isAFlatScale ? "F#3" : "Gb3", f3, false, i13, 0, 0, i9);
        ImageButton imageButton26 = (ImageButton) findViewById(R.id.bottom_g_s);
        RelativeLayout.LayoutParams layoutParams25 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams25.addRule(1, R.id.bottom_g);
        int i14 = (int) applyDimension8;
        layoutParams25.setMargins(i14, 0, 0, 0);
        imageButton26.setLayoutParams(layoutParams25);
        int i15 = i14 + i7;
        addLabel(imageButton26, imageButton5, !isAFlatScale ? "G#3" : "Ab3", f3, false, i15, 0, 0, i9);
        ImageButton imageButton27 = (ImageButton) findViewById(R.id.bottom_a_s);
        RelativeLayout.LayoutParams layoutParams26 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams26.addRule(1, R.id.bottom_a);
        int i16 = (int) applyDimension9;
        layoutParams26.setMargins(i16, 0, 0, 0);
        imageButton27.setLayoutParams(layoutParams26);
        int i17 = i7 + i16;
        addLabel(imageButton27, imageButton6, !isAFlatScale ? "A#3" : "Bb3", f3, false, i17, 0, 0, i9);
        ImageButton imageButton28 = (ImageButton) findViewById(R.id.middle_c_s);
        RelativeLayout.LayoutParams layoutParams27 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams27.addRule(1, R.id.middle_c);
        layoutParams27.setMargins(i6, 0, 0, 0);
        imageButton28.setLayoutParams(layoutParams27);
        addLabel(imageButton28, imageButton8, !isAFlatScale ? "C#4" : "Db4", f3, false, i8, 0, 0, i9);
        ImageButton imageButton29 = (ImageButton) findViewById(R.id.middle_d_s);
        RelativeLayout.LayoutParams layoutParams28 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams28.addRule(1, R.id.middle_d);
        layoutParams28.setMargins(i10, 0, 0, 0);
        imageButton29.setLayoutParams(layoutParams28);
        addLabel(imageButton29, imageButton9, !isAFlatScale ? "D#4" : "Eb4", f3, false, i11, 0, 0, i9);
        ImageButton imageButton30 = (ImageButton) findViewById(R.id.middle_f_s);
        RelativeLayout.LayoutParams layoutParams29 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams29.addRule(1, R.id.middle_f);
        layoutParams29.setMargins(i12, 0, 0, 0);
        imageButton30.setLayoutParams(layoutParams29);
        addLabel(imageButton30, imageButton11, !isAFlatScale ? "F#4" : "Gb4", f3, false, i13, 0, 0, i9);
        ImageButton imageButton31 = (ImageButton) findViewById(R.id.middle_g_s);
        RelativeLayout.LayoutParams layoutParams30 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams30.addRule(1, R.id.middle_g);
        layoutParams30.setMargins(i14, 0, 0, 0);
        imageButton31.setLayoutParams(layoutParams30);
        addLabel(imageButton31, imageButton12, !isAFlatScale ? "G#4" : "Ab4", f3, false, i15, 0, 0, i9);
        ImageButton imageButton32 = (ImageButton) findViewById(R.id.middle_a_s);
        RelativeLayout.LayoutParams layoutParams31 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams31.addRule(1, R.id.middle_a);
        layoutParams31.setMargins(i16, 0, 0, 0);
        imageButton32.setLayoutParams(layoutParams31);
        addLabel(imageButton32, imageButton13, !isAFlatScale ? "A#4" : "Bb4", f3, false, i17, 0, 0, i9);
        ImageButton imageButton33 = (ImageButton) findViewById(R.id.high_c_s);
        RelativeLayout.LayoutParams layoutParams32 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams32.addRule(1, R.id.high_c);
        layoutParams32.setMargins(i6, 0, 0, 0);
        imageButton33.setLayoutParams(layoutParams32);
        addLabel(imageButton33, imageButton15, !isAFlatScale ? "C#5" : "Db5", f3, false, i8, 0, 0, i9);
        ImageButton imageButton34 = (ImageButton) findViewById(R.id.high_d_s);
        RelativeLayout.LayoutParams layoutParams33 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams33.addRule(1, R.id.high_d);
        layoutParams33.setMargins(i10, 0, 0, 0);
        imageButton34.setLayoutParams(layoutParams33);
        addLabel(imageButton34, imageButton16, !isAFlatScale ? "D#5" : "Eb5", f3, false, i11, 0, 0, i9);
        ImageButton imageButton35 = (ImageButton) findViewById(R.id.high_f_s);
        RelativeLayout.LayoutParams layoutParams34 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams34.addRule(1, R.id.high_f);
        layoutParams34.setMargins(i12, 0, 0, 0);
        imageButton35.setLayoutParams(layoutParams34);
        addLabel(imageButton35, imageButton18, !isAFlatScale ? "F#5" : "Gb5", f3, false, i13, 0, 0, i9);
        ImageButton imageButton36 = (ImageButton) findViewById(R.id.high_g_s);
        RelativeLayout.LayoutParams layoutParams35 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams35.addRule(1, R.id.high_g);
        layoutParams35.setMargins(i14, 0, 0, 0);
        imageButton36.setLayoutParams(layoutParams35);
        addLabel(imageButton36, imageButton19, !isAFlatScale ? "G#5" : "Ab5", f3, false, i15, 0, 0, i9);
        ImageButton imageButton37 = (ImageButton) findViewById(R.id.high_a_s);
        RelativeLayout.LayoutParams layoutParams36 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams36.addRule(1, R.id.high_a);
        layoutParams36.setMargins(i16, 0, 0, 0);
        imageButton37.setLayoutParams(layoutParams36);
        addLabel(imageButton37, imageButton20, !isAFlatScale ? "A#5" : "Bb5", f3, false, i17, 0, 0, i9);
    }

    private void setRootNote() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Root Note").setSingleChoiceItems(rootNoteValues, rootNotesIdx, new RootNoteDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    private void setScale() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R.string.select_tune)).setSingleChoiceItems(scaleNames, scaleNamesIdx, new ScaleDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    private void setScaleDirection() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Scale Direction").setSingleChoiceItems(scaleDirectionValues, scaleDirectionIdx, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialogInterface, int i) {
                ScalesGameActivity.scaleDirectionIdx = i;
                ScalesGameActivity.scaleDirectionSetting = ScalesGameActivity.scaleDirectionValues[ScalesGameActivity.scaleDirectionIdx];
                ScalesGameActivity.this.calculateScaleDirection();
                ScalesGameActivity.isAgainWanted = false;
                ScalesGameActivity.hasResultDisplayed = true;
                dialogInterface.dismiss();
            }
        }).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
            create.getWindow().setLayout(-1, -2);
        }
    }

    public void setScaleTitle() {
        scaleTitle = scaleNames[scaleNamesIdx];
        //Log.v("themelodymaster", "scaleTitle in setTune:" + scaleTitle);
        isAFlatScale = Note.isAFlatScale(scaleTitle);
        setHeaderText(scaleTitle);
        setKeyboardLayout(scaleFactor);
    }

    public void setUpScales() {
        List<Scale> scales2 = this.scalesManager.getScales(levelIdx, calculatedRootNote);
        scales = scales2;
        scaleNames = this.scalesManager.getScalesNames(scales2);
    }

    private void showOptions() {
        isStopDesired = true;
    }

    public void writeScore() {
        int i;
        int i2;
        String str;
        if (totalNumberOfConfirms != 0) {
            String str2 = " at Root Note:" + rootNoteValues[rootNotesIdx];
            int i3 = 1;
            if (str2.contains(MY_RANDOM_GROUP)) {
                String str3 = " at Root Note:Random (";
                int i4 = 0;
                while (true) {
                    boolean[] zArr = this.userDefinedRootNoteFlags;
                    if (i4 >= zArr.length) {
                        break;
                    }
                    if (zArr[i4]) {
                        str3 = str3 + rootNoteRandomPoolAll[i4] + ",";
                    }
                    i4++;
                }
                str2 = str3.substring(0, str3.length() - 1) + ")";
            }
            String str4 = levelArray[levelIdx];
            if ("My Focus Group".equals(str4)) {
                String str5 = str4 + ":";
                int i5 = 0;
                while (true) {
                    boolean[] zArr2 = this.userDefinedScaleFlags;
                    if (i5 >= zArr2.length) {
                        break;
                    }
                    if (zArr2[i5]) {
                        String[] split = this.a[i5].split(" ");
                        if (split.length == 4) {
                            str = split[1] + split[2];
                        } else if (split.length == 3) {
                            str = split[1];
                        } else {
                            str = "";
                        }
                        str5 = str5 + str + ", ";
                    }
                    i5++;
                }
                str4 = str5.substring(0, str5.length() - 2);
            }
            TreeMap treeMap = (TreeMap) ProgressHelper.openProgressMapFromPrefs(getApplicationContext());
            String str6 = "1|" + str4 + str2;
            if (treeMap.containsKey(str6)) {
                String str7 = (String) treeMap.get(str6);
                i = ProgressHelper.getNumberCorrectFromValue(str7);
                i2 = ProgressHelper.getTotalNumberFromValue(str7);
                float f = (((float) i) * 100.0f) / ((float) i2);
                float f2 = (((float) numberCorrect) * 100.0f) / ((float) totalNumberOfConfirms);
                if (f2 < f) {
                    i3 = 3;
                } else if (f2 == f) {
                    i3 = 2;
                }
            } else {
                i2 = 0;
                i = 0;
            }
            treeMap.put(str6, "" + (i + numberCorrect) + "|" + (i2 + totalNumberOfConfirms) + "|" + i3);
            ProgressHelper.saveProgressObjectViaJSON(treeMap, getApplicationContext());
            numberCorrect = 0;
            totalNumberOfConfirms = 0;
        }
    }



    public void onClick(View view) {
        if (view.getId() == R.id.back_arrow_view) {
            onBackPressed();
        } else if (view.getId() == R.id.play_button) {

                    if (isPlaying) {
                        isStopDesired = true;
                    } else {
                        playGame();
                    }

        } else if (view.getId() == R.id.check_button) {

                    displayResult();

        } else if (view.getId() == R.id.level_button) {

                    selectLevel();

        } else if (view.getId() == R.id.choose_key_button) {

                    setRootNote();

        } else if (view.getId() == R.id.scale_direction_button) {

                    setScaleDirection();

        }
    }

    public void onCreate(Bundle bundle) {
        boolean[] zArr;
        String str;
        super.onCreate(bundle);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.scales_game);

        mediation = SharePrefUtils.getString(ConstantAd.MEDIATION, "0");

        this.random = new Random(new Date().getTime());
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
        findViewById(R.id.play_button).setOnClickListener(this);
        findViewById(R.id.check_button).setOnClickListener(this);
        findViewById(R.id.level_button).setOnClickListener(this);
        findViewById(R.id.choose_key_button).setOnClickListener(this);
        findViewById(R.id.scale_direction_button).setOnClickListener(this);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seek_bar);
        this.seekBar = seekBar2;
        seekBar2.setOnSeekBarChangeListener(this);
        resources = getResources();
        this.scalesManager = ScalesManager.getInstance(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notes_layout);
        this.notesLayout = relativeLayout;
        relativeLayout.measure(0, 0);
        keyLabels = new ArrayList<>();
        this.seekBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.keyboard_400x64));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.thumb);
        drawable.setAlpha(128);
        this.seekBar.setThumb(drawable);
        this.headerTextView = (TextView) findViewById(R.id.headerText);
        setHeaderText("Piano Scales Game");
        bitmapWhiteKey = BitmapFactory.decodeResource(resources, R.drawable.white_key);
        bitmapWhiteKeyFocused = BitmapFactory.decodeResource(resources, R.drawable.white_key_focused);
        bitmapWhiteKeySelected = BitmapFactory.decodeResource(resources, R.drawable.white_key_selected);
        bitmapBlackKey = BitmapFactory.decodeResource(resources, R.drawable.black_key);
        bitmapBlackKeyFocused = BitmapFactory.decodeResource(resources, R.drawable.black_key_focused);
        bitmapBlackKeySelected = BitmapFactory.decodeResource(resources, R.drawable.black_key_selected);
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
        this.playStopButton = (ImageView) findViewById(R.id.play_button);
        playingOn = ContextCompat.getDrawable(this, R.drawable.play_stop_playing);
        playingOff = ContextCompat.getDrawable(this, R.drawable.play_stop);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString("LEVEL_FROM_PROGRESS");
            if (string == null) {
                string = "Beginner at Root Note:Random (C,F,G)";
            }
            String[] split = string.split(" at Root Note:");
            if (string.contains(RANDOM_SCALE_DIRECTION)) {
                String[] split2 = split[1].replace("Random (", "").replace(")", "").split(",");
                SharedPreferences.Editor edit = sharedPrefs.edit();
                for (String str2 : split2) {
                    edit.putBoolean("userDefinedRootNoteFlags" + Arrays.asList(rootNoteRandomPoolAll).indexOf(str2), true);
                }
                edit.apply();
                if (Arrays.asList(rootNoteValues).contains(split[1])) {
                    int indexOf = Arrays.asList(rootNoteValues).indexOf(split[1]);
                    rootNotesIdx = indexOf;
                    rootNote = rootNoteValues[indexOf];
                } else {
                    rootNotesIdx = 3;
                    rootNote = MY_RANDOM_GROUP;
                }
                edit.putString(MenuActivity.KEY_ROOT_NOTE_INCLUDING_RANDOM, rootNote);
                edit.apply();
            } else {
                rootNote = split[1];
                if (!Arrays.asList(rootNoteValues).contains(rootNote)) {
                    rootNote = "C";
                }
                rootNotesIdx = Arrays.asList(rootNoteValues).indexOf(rootNote);
            }
            createCalculatedRootNote();
            String str3 = split[0];
            SharedPreferences.Editor edit2 = sharedPrefs.edit();
            edit2.putString(MenuActivity.KEY_ROOT_NOTE_INCLUDING_RANDOM, rootNote);
            edit2.apply();
            if (string.contains("My Focus Group:")) {
                levelIdx = Arrays.asList(levelArray).indexOf("My Focus Group");
                String[] split3 = str3.split(":")[1].split(", ");
                this.userDefinedScaleFlags = new boolean[71];
                ScalesManager scalesManager2 = this.scalesManager;
                this.a = scalesManager2.getScalesNames(scalesManager2.getScales(6, calculatedRootNote));
                int i = 0;
                while (true) {
                    zArr = this.userDefinedScaleFlags;
                    if (i >= zArr.length) {
                        break;
                    }
                    String[] split4 = this.a[i].split(" ");
                    if (split4.length == 4) {
                        str = split4[1] + split4[2];
                    } else if (split4.length == 3) {
                        str = split4[1];
                    } else {
                        str = "";
                    }
                    this.userDefinedScaleFlags[i] = Arrays.asList(split3).contains(str);
                    i++;
                }
                edit2.putInt("userDefinedScaleFlags_length", zArr.length);
                for (int i2 = 0; i2 < this.userDefinedScaleFlags.length; i2++) {
                    edit2.putBoolean("userDefinedScaleFlags" + i2, this.userDefinedScaleFlags[i2]);
                }
                edit2.apply();
            } else {
                levelIdx = Arrays.asList(levelArray).indexOf(str3);
            }
            edit2.putInt(MenuActivity.KEY_SCALE_LEVEL_IDX, levelIdx);
            edit2.apply();
        }
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
        isResultDialogInView = false;
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
    }

    public void onStop() {
        //Log.v("themelodymaster", "onStop called.");
        isStopDesired = true;
        isResultDialogInView = false;
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
        if ((view instanceof ImageButton) && view.getTag() != null && view.getTag().toString().length() > 0) {
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
                        if (noteFromPosition != null) {
                            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                            scalesGameActivity.selectNote(scalesGameActivity.soundManager.getNote(Integer.valueOf(noteFromPosition.getTag().toString()).intValue()));
                        }
                    }
                });
                if (pointerId == 0) {
                    this.lastFinger1Button = noteFromPosition;
                } else if (pointerId == 1) {
                    this.lastFinger2Button = noteFromPosition;
                } else if (pointerId == 2) {
                    this.lastFinger3Button = noteFromPosition;
                } else if (pointerId == 3) {
                    this.lastFinger4Button = noteFromPosition;
                } else if (pointerId == 4) {
                    this.lastFinger5Button = noteFromPosition;
                }
            } else if (actionMasked == 1 || actionMasked == 6) {
                //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (pointerId == 0 && ScalesGameActivity.this.lastFinger1Button != null) {
                            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                            scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger1Button.getTag().toString()).intValue()));
                        }
                        int i = pointerId;
                        if (i == 1) {
                            if (ScalesGameActivity.this.lastFinger2Button != null) {
                                ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                scalesGameActivity2.resetState(scalesGameActivity2.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger2Button.getTag().toString()).intValue()));
                            }
                        } else if (i == 2) {
                            if (ScalesGameActivity.this.lastFinger3Button != null) {
                                ScalesGameActivity scalesGameActivity3 = ScalesGameActivity.this;
                                scalesGameActivity3.resetState(scalesGameActivity3.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger3Button.getTag().toString()).intValue()));
                            }
                        } else if (i == 3) {
                            if (ScalesGameActivity.this.lastFinger4Button != null) {
                                ScalesGameActivity scalesGameActivity4 = ScalesGameActivity.this;
                                scalesGameActivity4.resetState(scalesGameActivity4.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger4Button.getTag().toString()).intValue()));
                            }
                        } else if (i == 4 && ScalesGameActivity.this.lastFinger5Button != null) {
                            ScalesGameActivity scalesGameActivity5 = ScalesGameActivity.this;
                            scalesGameActivity5.resetState(scalesGameActivity5.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger5Button.getTag().toString()).intValue()));
                        }
                    }
                });
            } else if (motionEvent.getAction() == 2) {
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
                    if (pointerId2 == 0) {
                        final View noteFromPosition2 = getNoteFromPosition(motionEvent.getRawX(), motionEvent.getRawY(), this.notesLayout.getScrollX());
                        if (noteFromPosition2 != this.lastFinger1Button) {
                            playNoteByUser(noteFromPosition2, f, true);
                            runOnUiThread(new Runnable() {
                                 public void run() {
                                    if (noteFromPosition2 != null) {
                                        ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                                        scalesGameActivity.selectNote(scalesGameActivity.soundManager.getNote(Integer.valueOf(noteFromPosition2.getTag().toString()).intValue()));
                                    }
                                    if (ScalesGameActivity.this.lastFinger1Button != null) {
                                        ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                        scalesGameActivity2.resetState(scalesGameActivity2.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger1Button.getTag().toString()).intValue()));
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
                                        if (ScalesGameActivity.this.lastFinger2Button != null) {
                                            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                                            scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger2Button.getTag().toString()).intValue()));
                                        }
                                        if (noteFromPosition3 != null) {
                                            ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                            scalesGameActivity2.selectNote(scalesGameActivity2.soundManager.getNote(Integer.valueOf(noteFromPosition3.getTag().toString()).intValue()));
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
                                        if (ScalesGameActivity.this.lastFinger3Button != null) {
                                            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                                            scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger3Button.getTag().toString()).intValue()));
                                        }
                                        if (noteFromPosition3 != null) {
                                            ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                            scalesGameActivity2.selectNote(scalesGameActivity2.soundManager.getNote(Integer.valueOf(noteFromPosition3.getTag().toString()).intValue()));
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
                                        if (ScalesGameActivity.this.lastFinger4Button != null) {
                                            ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                                            scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger4Button.getTag().toString()).intValue()));
                                        }
                                        if (noteFromPosition3 != null) {
                                            ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                            scalesGameActivity2.selectNote(scalesGameActivity2.soundManager.getNote(Integer.valueOf(noteFromPosition3.getTag().toString()).intValue()));
                                        }
                                    }
                                });
                                this.lastFinger4Button = noteFromPosition3;
                            }
                        } else if (pointerId2 == 4 && noteFromPosition3 != this.lastFinger5Button) {
                            playNoteByUser(noteFromPosition3, f, true);
                            runOnUiThread(new Runnable() {
                                 public void run() {
                                    if (ScalesGameActivity.this.lastFinger5Button != null) {
                                        ScalesGameActivity scalesGameActivity = ScalesGameActivity.this;
                                        scalesGameActivity.resetState(scalesGameActivity.soundManager.getNote(Integer.valueOf(ScalesGameActivity.this.lastFinger5Button.getTag().toString()).intValue()));
                                    }
                                    if (noteFromPosition3 != null) {
                                        ScalesGameActivity scalesGameActivity2 = ScalesGameActivity.this;
                                        scalesGameActivity2.selectNote(scalesGameActivity2.soundManager.getNote(Integer.valueOf(noteFromPosition3.getTag().toString()).intValue()));
                                    }
                                }
                            });
                            this.lastFinger5Button = noteFromPosition3;
                        }
                    }
                }
            }
        }
        return false;
    }



    @Override
    public void onBackPressed() {

                isStopDesired = true;
                isResultDialogInView = false;
                SoundManager soundManager2 = soundManager;
                if (soundManager2 != null) {
                    soundManager2.stopSounds();
                }
                writeScore();
                finish();

    }


}
