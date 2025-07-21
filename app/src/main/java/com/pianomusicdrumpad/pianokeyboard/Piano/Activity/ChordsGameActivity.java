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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
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

import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd;
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils;

import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ChordManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ProgressHelper;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Chord;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.R;
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class ChordsGameActivity extends Activity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    public static final int DURATION = 400;
    private static int HIGHLIGHT_HEIGHT = 0;
    public static final String LOG_TAG = "themelodymaster";
    private static final String MY_RANDOM_GROUP = "My Random Root Notes";
    public static final int NO_OF_CHORDS_IN_EVERYTHING = 31;
    public static int ON_MEASURE_HEIGHT = 0;
    public static int ON_MEASURE_WIDTH = 0;
    private static final String RANDOM_ALL = "Random (ALL)";
    private static final String RANDOM_ONE = "Random (C,F,G)";
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
    public static Chord chord = null;
    public static String[] chordNames = null;
    public static int chordNamesIdx = 0;
    public static String chordTitle = "C Major";
    private static List<Chord> chords = null;
    public static float density = 1.0f;
    private static float dipAdHeight = 50.0f;
    private static float dipHeightOfTopBar = 34.0f;
    private static float fBlackKeyHeightDip = 0.0f;
    public static String hapticSetting = "MEDIUM";
    public static boolean hasResultDisplayed = true;
    private static int heightOfTopBar = 34;
    public static int iTagFirstNote = 0;
    public static boolean isAFlatChord = false;
    public static boolean isAdmobGotInventory = true;
    public static boolean isAgainWanted = false;
    public static boolean isAutoscrollOn = true;
    public static boolean isHighlightAllNotesOn = true;
    public static boolean isPlaying = false;
    public static boolean isPressureOn = false;
    public static boolean isResultDialogInView = false;
    public static boolean isStopDesired = false;
    private static ArrayList<TextView> keyLabels = null;
    public static String[] levelArray = {"Beginner", "Quality", "Medium", "Major Inversion Only", "Minor Inversion Only", "6s Only", "7s Only", "9s Only", "Other Extended", "Intervals", "Advanced", "Everything", "My Focus Group"};
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
    public static float scaleFactor = 1.0f;
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
    private Set<String> actualNotesPlayedSet = new HashSet();
    public ChordManager chordsManager;
    private StringBuffer expectedNotes = new StringBuffer();
    private Set<String> expectedNotesSet = new HashSet();
    private String firstNotePrefix = null;
    private float firstPointerRawX = 0.0f;
    private Set<Note> focusedNoteInChordSet = new HashSet();
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
    private RelativeLayout notesLayout = null;
    public ImageView playStopButton;
    private Random random;
    private SeekBar seekBar;
    public boolean setNextBreak = false;
    public SoundManager soundManager;
    public boolean[] userDefinedChordFlags;
    public boolean[] userDefinedRootNoteFlags;


    private String mediation;
    private Object full_ad;

    public void onStartTrackingTouch(SeekBar seekBar2) {
    }

    public void onStopTrackingTouch(SeekBar seekBar2) {
    }


    public class UiTread implements Runnable {
        UiTread() {
        }

        public void run() {
            ChordsGameActivity.this.scrollToTag(ChordsGameActivity.iTagFirstNote);
        }
    }


    public class RootNoteDialog implements DialogInterface.OnClickListener {
        RootNoteDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i != ChordsGameActivity.rootNotesIdx) {
                ChordsGameActivity.this.writeScore();
            }
            ChordsGameActivity.rootNotesIdx = i;
            ChordsGameActivity.rootNote = ChordsGameActivity.rootNoteValues[ChordsGameActivity.rootNotesIdx];
            ChordsGameActivity.this.createCalculatedRootNote();
            dialogInterface.dismiss();
            if (ChordsGameActivity.rootNote.equals(ChordsGameActivity.MY_RANDOM_GROUP)) {
                ChordsGameActivity.this.designMyRandomRootNoteGroup();
                return;
            }
            ChordsGameActivity.this.createCalculatedRootNote();
            if (Arrays.asList(ChordsGameActivity.rootNoteValues).contains(ChordsGameActivity.rootNote)) {
                ChordsGameActivity.rootNotesIdx = Arrays.asList(ChordsGameActivity.rootNoteValues).indexOf(ChordsGameActivity.rootNote);
            }
            ChordsGameActivity.this.userDefinedChordFlags = new boolean[31];
            List<Chord> chords = ChordsGameActivity.this.chordsManager.getChords(11, ChordsGameActivity.calculatedRootNote);
            ChordsGameActivity chordsGameActivity = ChordsGameActivity.this;
            chordsGameActivity.a = chordsGameActivity.chordsManager.getChordNames(chords);
            for (int i2 = 0; i2 < 31; i2++) {
                boolean[] zArr = ChordsGameActivity.this.userDefinedChordFlags;
                SharedPreferences sharedPreferences = ChordsGameActivity.sharedPrefs;
                zArr[i2] = sharedPreferences.getBoolean("userDefinedChordFlags" + i2, false);
            }
            ChordsGameActivity.this.setKeyboardLayout(ChordsGameActivity.scaleFactor);
            if (ChordsGameActivity.chord != null) {
                ChordsGameActivity.iTagFirstNote = ChordsGameActivity.this.soundManager.getNotePosition(ChordsGameActivity.chord.getNotes().get(0));
                ChordsGameActivity.this.scrollToTag(ChordsGameActivity.iTagFirstNote);
            } else {
                ChordsGameActivity.this.scrollToTag(0);
            }
            ChordsGameActivity.this.setUpChords();
            ChordsGameActivity.chordTitle = ChordsGameActivity.chordNames[ChordsGameActivity.chordNamesIdx];
            ChordsGameActivity.this.setHeaderText(ChordsGameActivity.chordTitle);
            ChordsGameActivity.hasResultDisplayed = true;
            ChordsGameActivity.isAgainWanted = false;
        }
    }


    public class SelectLevelDialog implements DialogInterface.OnClickListener {
        SelectLevelDialog() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (i != ChordsGameActivity.levelIdx || i == ChordsGameActivity.levelArray.length - 1) {
                ChordsGameActivity.this.writeScore();
            }
            ChordsGameActivity.levelIdx = i;
            dialogInterface.dismiss();
            if (ChordsGameActivity.levelIdx == 12) {
                ChordsGameActivity.this.designMyFocusGroup();
            }
            ChordsGameActivity.this.setUpChords();
            ChordsGameActivity.chordNamesIdx = 0;
            SharedPreferences.Editor edit = ChordsGameActivity.sharedPrefs.edit();
            edit.putInt(MenuActivity.KEY_CHORD_LEVEL_IDX, ChordsGameActivity.levelIdx);
            edit.apply();
        }
    }

    private void addLabel(ImageButton imageButton, ImageButton imageButton2, String str, float f, boolean z, int i, int i2, int i3, int i4) {
        if (!noteNamesType.equals("NONE") || (str.contains("C") && !str.contains("b") && !str.contains("#"))) {
            if ("SOLFEGE".equals(noteNamesType)) {
                str = this.chordsManager.getSolfegeLabel(str, calculatedRootNote, this.soundManager, chordTitle);
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
        isResultDialogInView = false;
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
        levelIdx = sharedPrefs.getInt(MenuActivity.KEY_CHORD_LEVEL_IDX, MenuActivity.defaultChordLevelIdx);
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
        this.userDefinedChordFlags = new boolean[31];
        ChordManager chordManager = this.chordsManager;
        this.a = chordManager.getChordNames(chordManager.getChords(11, calculatedRootNote));
        for (int i2 = 0; i2 < 31; i2++) {
            boolean[] zArr2 = this.userDefinedChordFlags;
            SharedPreferences sharedPreferences2 = sharedPrefs;
            zArr2[i2] = sharedPreferences2.getBoolean("userDefinedChordFlags" + i2, false);
        }
        setKeyboardLayout(scaleFactor);
        Chord chord2 = chord;
        if (chord2 != null) {
            int notePosition = this.soundManager.getNotePosition(chord2.getNotes().get(0));
            iTagFirstNote = notePosition;
            scrollToTag(notePosition);
        } else {
            scrollToTag(0);
        }
        setUpChords();
    }

    private void cleanNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
    }

    private void cleanNotesInChord() {
        Log.v("themelodymaster", "Unhighlighting all notes in chord:" + chordTitle);
        for (Note note : this.focusedNoteInChordSet) {
            ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
        }
        this.focusedNoteInChordSet.clear();
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
            str = chordTitle;
            textView.setText("Congratulations. You know it. Press 'Next' for the next challenge.");
        } else {
            linearLayout.setBackgroundColor(-2061695);
            str = chordTitle;
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
                    ChordsGameActivity.isResultDialogInView = false;
                    create.dismiss();
                    ChordsGameActivity.isAgainWanted = true;
                    ChordsGameActivity.this.playGame();
                }
            });
        }
        create.setButton(-3, "See Chord", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChordsGameActivity.isResultDialogInView = false;
                create.dismiss();
                ChordsGameActivity.this.unhighlightAllNotes();
                ChordsGameActivity.this.playChordInit(z);
            }
        });
        create.setButton(-2, "Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChordsGameActivity.isResultDialogInView = false;
                create.dismiss();
                ChordsGameActivity.this.playGame();
            }
        });
        create.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return true;
                }
                ChordsGameActivity.isResultDialogInView = false;
                dialogInterface.dismiss();
                return true;
            }
        });
        return create;
    }

    public void designMyFocusGroup() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Chords").setMultiChoiceItems(this.a, this.userDefinedChordFlags, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                ChordsGameActivity.this.userDefinedChordFlags[i] = z;
            }
        }).setNegativeButton("None", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ChordsGameActivity.this.userDefinedChordFlags.length; i2++) {
                    ChordsGameActivity.this.userDefinedChordFlags[i2] = false;
                }
                ChordsGameActivity.this.designMyFocusGroup();
            }
        }).setNeutralButton("ALL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ChordsGameActivity.this.userDefinedChordFlags.length; i2++) {
                    ChordsGameActivity.this.userDefinedChordFlags[i2] = true;
                }
                ChordsGameActivity.this.designMyFocusGroup();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean[] zArr = ChordsGameActivity.this.userDefinedChordFlags;
                int length = zArr.length;
                int i2 = 0;
                while (i2 < length && !zArr[i2]) {
                    i2++;
                }
                Log.v("themelodymaster", "userDefinedChordFlags length:" + ChordsGameActivity.this.userDefinedChordFlags.length);
                SharedPreferences.Editor edit = ChordsGameActivity.sharedPrefs.edit();
                edit.putInt("userDefinedChordFlags_length", ChordsGameActivity.this.userDefinedChordFlags.length);
                for (int i3 = 0; i3 < ChordsGameActivity.this.userDefinedChordFlags.length; i3++) {
                    edit.putBoolean("userDefinedChordFlags" + i3, ChordsGameActivity.this.userDefinedChordFlags[i3]);
                }
                edit.apply();
                ChordsGameActivity.this.bringInDefaultPrefs();
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
                ChordsGameActivity.this.userDefinedRootNoteFlags[i] = z;
            }
        }).setNegativeButton("None", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ChordsGameActivity.this.userDefinedRootNoteFlags.length; i2++) {
                    ChordsGameActivity.this.userDefinedRootNoteFlags[i2] = false;
                }
                ChordsGameActivity.this.designMyRandomRootNoteGroup();
            }
        }).setNeutralButton("ALL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int i2 = 0; i2 < ChordsGameActivity.this.userDefinedRootNoteFlags.length; i2++) {
                    ChordsGameActivity.this.userDefinedRootNoteFlags[i2] = true;
                }
                ChordsGameActivity.this.designMyRandomRootNoteGroup();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean[] zArr = ChordsGameActivity.this.userDefinedRootNoteFlags;
                int length = zArr.length;
                int i2 = 0;
                while (i2 < length && !zArr[i2]) {
                    i2++;
                }
                Log.v("themelodymaster", "userDefinedRootNoteFlags length:" + ChordsGameActivity.this.userDefinedRootNoteFlags.length);
                SharedPreferences.Editor edit = ChordsGameActivity.sharedPrefs.edit();
                edit.putInt("userDefinedRootNoteFlags_length", ChordsGameActivity.this.userDefinedRootNoteFlags.length);
                for (int i3 = 0; i3 < ChordsGameActivity.this.userDefinedRootNoteFlags.length; i3++) {
                    edit.putBoolean("userDefinedRootNoteFlags" + i3, ChordsGameActivity.this.userDefinedRootNoteFlags[i3]);
                }
                edit.apply();
                ChordsGameActivity.this.createCalculatedRootNote();
                if (Arrays.asList(ChordsGameActivity.rootNoteValues).contains(ChordsGameActivity.rootNote)) {
                    ChordsGameActivity.rootNotesIdx = Arrays.asList(ChordsGameActivity.rootNoteValues).indexOf(ChordsGameActivity.rootNote);
                }
                ChordsGameActivity.this.userDefinedChordFlags = new boolean[31];
                ChordsGameActivity chordsGameActivity = ChordsGameActivity.this;
                chordsGameActivity.a = chordsGameActivity.chordsManager.getChordNames(ChordsGameActivity.this.chordsManager.getChords(11, ChordsGameActivity.calculatedRootNote));
                for (int i4 = 0; i4 < 31; i4++) {
                    boolean[] zArr2 = ChordsGameActivity.this.userDefinedChordFlags;
                    SharedPreferences sharedPreferences = ChordsGameActivity.sharedPrefs;
                    zArr2[i4] = sharedPreferences.getBoolean("userDefinedChordFlags" + i4, false);
                }
                ChordsGameActivity.this.setKeyboardLayout(ChordsGameActivity.scaleFactor);
                if (ChordsGameActivity.chord != null) {
                    ChordsGameActivity.iTagFirstNote = ChordsGameActivity.this.soundManager.getNotePosition(ChordsGameActivity.chord.getNotes().get(0));
                    ChordsGameActivity.this.scrollToTag(ChordsGameActivity.iTagFirstNote);
                } else {
                    ChordsGameActivity.this.scrollToTag(0);
                }
                ChordsGameActivity.this.setUpChords();
                ChordsGameActivity.chordTitle = ChordsGameActivity.chordNames[ChordsGameActivity.chordNamesIdx];
                ChordsGameActivity.this.setHeaderText(ChordsGameActivity.chordTitle);
                ChordsGameActivity.hasResultDisplayed = true;
                ChordsGameActivity.isAgainWanted = false;
                ChordsGameActivity.hasResultDisplayed = true;
                ChordsGameActivity.isAgainWanted = false;
                ChordsGameActivity.this.bringInDefaultPrefs();
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
            if (!this.expectedNotesSet.equals(this.actualNotesPlayedSet) || isFinishing()) {
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

    private Bitmap getBitmapClean(Note note) {
        return note.getName().contains("#") ? bitmapBlackKey : bitmapWhiteKey;
    }

    private Bitmap getBitmapFocus(Note note) {
        String chordNoteName = note.getChordNoteName(isAFlatChord);
        return ((chordNoteName == null || !chordNoteName.contains("#")) && !chordNoteName.contains("b")) ? bitmapWhiteKeyFocused : bitmapBlackKeyFocused;
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

    public void playChord() {
        List<Note> notes = chord.getNotes();
        Note note = notes.get(0);
        iTagFirstNote = this.soundManager.getNotePosition(note);
        findViewById(note.getRId());
        runOnUiThread(new UiTread());
        int i = 1;
        while (i <= notes.size()) {
            if (isStopDesired || this.setNextBreak) {
                this.setNextBreak = false;
            } else {
                Note note2 = notes.get(i - 1);
                if (isHighlightAllNotesOn) {
                    final View findViewById = findViewById(note2.getRId());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ChordsGameActivity chordsGameActivity = ChordsGameActivity.this;
                            chordsGameActivity.selectNote(chordsGameActivity.soundManager.getNote(Integer.valueOf(findViewById.getTag().toString()).intValue()));
                        }
                    });
                }
                SoundManager soundManager2 = this.soundManager;
                soundManager2.playSound(soundManager2.getNotePosition(note2), ((float) playAlongVolume) / 100.0f, true);
                i++;
            }
        }
        this.setNextBreak = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException unused) {
        }
        for (Note note3 : notes) {
            if (isHighlightAllNotesOn) {
                final ImageButton imageButton = (ImageButton) findViewById(note3.getRId());
                runOnUiThread(new Runnable() {
                    public void run() {
                        ChordsGameActivity chordsGameActivity = ChordsGameActivity.this;
                        chordsGameActivity.resetState(chordsGameActivity.soundManager.getNote(Integer.valueOf(imageButton.getTag().toString()).intValue()));
                    }
                });
            }
        }
    }

    public void playChordInit(final boolean z) {
        isPlaying = true;
        this.playStopButton.setImageDrawable(playingOn);
        new Thread() {
            class ChordRunnable implements Runnable {
                ChordRunnable() {
                }

                public void run() {
                    ChordsGameActivity.this.playStopButton.setImageDrawable(ChordsGameActivity.playingOff);
                    AlertDialog createResultDialog = ChordsGameActivity.this.createResultDialog(z);
                    if (!ChordsGameActivity.this.isFinishing()) {
                        createResultDialog.show();
                        createResultDialog.getWindow().setLayout(-1, -2);
                    }
                }
            }

            public void run() {
                ChordsGameActivity.this.playChord();
                ChordsGameActivity.isPlaying = false;
                ChordsGameActivity.isStopDesired = false;
                ChordsGameActivity.this.setNextBreak = false;
                ChordsGameActivity.this.runOnUiThread(new ChordRunnable());
            }
        }.start();
    }

    public void playGame() {
        unhighlightAllNotes();
        this.expectedNotes = new StringBuffer();
        this.actualNotesPlayed = new StringBuffer();
        this.expectedNotesSet = new HashSet();
        this.actualNotesPlayedSet = new HashSet();
        if (hasResultDisplayed && !isAgainWanted) {
            createCalculatedRootNote();
            Log.v("themelodymaster", " Play levelIdx:" + levelIdx + " calculatedRootNote:" + calculatedRootNote);
            List<Chord> chords2 = this.chordsManager.getChords(levelIdx, calculatedRootNote);
            chords = chords2;
            if (chords2 != null && chords2.size() != 0) {
                List<Chord> list = chords;
                chord = list.get(this.random.nextInt(list.size()));
                isAFlatChord = Note.isAFlatChord(chordTitle);
                setKeyboardLayout(scaleFactor);
                hasResultDisplayed = false;
            } else {
                return;
            }
        }
        List<Note> notes = chord.getNotes();
        String title = chord.getTitle();
        chordTitle = title;
        setHeaderText(title);
        int notePosition = this.soundManager.getNotePosition(notes.get(0));
        iTagFirstNote = notePosition;
        scrollToTag(notePosition);
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            isAFlatChord = Note.isAFlatChord(chordTitle);
            if ("SOLFEGE".equals(noteNamesType)) {
                String chordNoteName = note.getChordNoteName(isAFlatChord);
                StringBuffer stringBuffer = this.expectedNotes;
                stringBuffer.append(" " + this.chordsManager.getSolfegeLabel(chordNoteName, calculatedRootNote, this.soundManager, chordTitle) + ",");
                this.expectedNotesSet.add(this.chordsManager.getSolfegeLabel(chordNoteName, calculatedRootNote, this.soundManager, chordTitle));
            } else {
                StringBuffer stringBuffer2 = this.expectedNotes;
                stringBuffer2.append(" " + note.getChordNoteName(isAFlatChord) + ",");
                this.expectedNotesSet.add(note.getChordNoteName(isAFlatChord));
            }
        }
        if (!isAgainWanted) {
            View inflate = View.inflate(this, R.layout.challenge_dialog, null);
            TextView textView = (TextView) inflate.findViewById(R.id.title);
            ((LinearLayout) inflate.findViewById(R.id.dialog_layout)).setBackgroundColor(-26368);
            ((TextView) inflate.findViewById(R.id.message)).setText("Play " + chordTitle + " on the keyboard and then select 'Confirm'(✔)\n");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog create = builder.setTitle("Play " + chordTitle).setView(inflate).create();
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
            final Note note = this.soundManager.getNote(Integer.valueOf(view.getTag().toString()).intValue());
            isAFlatChord = Note.isAFlatChord(chordTitle);
            if ("SOLFEGE".equals(noteNamesType)) {
                String chordNoteName = note.getChordNoteName(isAFlatChord);
                StringBuffer stringBuffer = this.actualNotesPlayed;
                stringBuffer.append(" " + this.chordsManager.getSolfegeLabel(chordNoteName, calculatedRootNote, this.soundManager, chordTitle) + ",");
                this.actualNotesPlayedSet.add(this.chordsManager.getSolfegeLabel(chordNoteName, calculatedRootNote, this.soundManager, chordTitle));
            } else {
                StringBuffer stringBuffer2 = this.actualNotesPlayed;
                stringBuffer2.append(" " + note.getChordNoteName(isAFlatChord) + ",");
                this.actualNotesPlayedSet.add(note.getChordNoteName(isAFlatChord));
            }
            selectNote(note);
            this.lastPlayedUserNote = note;
            new Thread() {
                public void run() {
                    ChordsGameActivity.this.soundManager.playSound(Integer.parseInt(view.getTag().toString()), f, z);
                    ChordsGameActivity.this.mHapticManager.playHapticEffect(true, ChordsGameActivity.this.mHapticManager.getHapticEffect(note));
                }
            }.start();
            Log.v("themelodymaster", "expectedNotesSet.size():" + this.expectedNotesSet.size() + " actualNotesPlayedSet.size():" + this.actualNotesPlayedSet.size() + " isResultDialogInView:" + isResultDialogInView);
            if (this.expectedNotesSet.equals(this.actualNotesPlayedSet) && !isResultDialogInView) {
                displayResult();
            } else if (this.actualNotesPlayedSet.size() >= this.expectedNotesSet.size() && !isResultDialogInView) {
                displayResult();
            }
        }
    }

    public void resetState(Note note) {
        Log.v("themelodymaster", "resetState called");
        if (this.focusedNoteInChordSet.contains(note)) {
            focusNote(note);
        } else {
            cleanNote(note);
        }
    }

    public void scrollToTag(int i) {
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

    private void selectLevel() {
        hasResultDisplayed = true;
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Level").setSingleChoiceItems(levelArray, levelIdx, new SelectLevelDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    public void selectNote(Note note) {
        ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapSelected(note));
    }

    private void setChordTitle() {
        chordTitle = chordNames[chordNamesIdx];
        Log.v("themelodymaster", "chordTitle in setTune:" + chordTitle);
        isAFlatChord = Note.isAFlatChord(chordTitle);
        setHeaderText(chordTitle);
        setKeyboardLayout(scaleFactor);
    }

    public void setHeaderText(final String str) {
        runOnUiThread(new Runnable() {
            /* class com.pianomusicdrumpad.pianokeyboard.Activity.ChordsGameActivity.AnonymousClass18 */

            public void run() {
                Log.v("themelodymaster", "settingText:" + str);
                ChordsGameActivity.this.headerTextView.setText(str);
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
        addLabel(imageButton23, imageButton, !isAFlatChord ? "C#3" : "Db3", f3, false, i8, 0, 0, i9);
        ImageButton imageButton24 = (ImageButton) findViewById(R.id.bottom_d_s);
        RelativeLayout.LayoutParams layoutParams23 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams23.addRule(1, R.id.bottom_d);
        int i10 = (int) applyDimension6;
        layoutParams23.setMargins(i10, 0, 0, 0);
        imageButton24.setLayoutParams(layoutParams23);
        int i11 = i10 + i7;
        addLabel(imageButton24, imageButton2, !isAFlatChord ? "D#3" : "Eb3", f3, false, i11, 0, 0, i9);
        ImageButton imageButton25 = (ImageButton) findViewById(R.id.bottom_f_s);
        RelativeLayout.LayoutParams layoutParams24 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams24.addRule(1, R.id.bottom_f);
        int i12 = (int) applyDimension7;
        layoutParams24.setMargins(i12, 0, 0, 0);
        imageButton25.setLayoutParams(layoutParams24);
        int i13 = i12 + i7;
        addLabel(imageButton25, imageButton4, !isAFlatChord ? "F#3" : "Gb3", f3, false, i13, 0, 0, i9);
        ImageButton imageButton26 = (ImageButton) findViewById(R.id.bottom_g_s);
        RelativeLayout.LayoutParams layoutParams25 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams25.addRule(1, R.id.bottom_g);
        int i14 = (int) applyDimension8;
        layoutParams25.setMargins(i14, 0, 0, 0);
        imageButton26.setLayoutParams(layoutParams25);
        int i15 = i14 + i7;
        addLabel(imageButton26, imageButton5, !isAFlatChord ? "G#3" : "Ab3", f3, false, i15, 0, 0, i9);
        ImageButton imageButton27 = (ImageButton) findViewById(R.id.bottom_a_s);
        RelativeLayout.LayoutParams layoutParams26 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams26.addRule(1, R.id.bottom_a);
        int i16 = (int) applyDimension9;
        layoutParams26.setMargins(i16, 0, 0, 0);
        imageButton27.setLayoutParams(layoutParams26);
        int i17 = i7 + i16;
        addLabel(imageButton27, imageButton6, !isAFlatChord ? "A#3" : "Bb3", f3, false, i17, 0, 0, i9);
        ImageButton imageButton28 = (ImageButton) findViewById(R.id.middle_c_s);
        RelativeLayout.LayoutParams layoutParams27 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams27.addRule(1, R.id.middle_c);
        layoutParams27.setMargins(i6, 0, 0, 0);
        imageButton28.setLayoutParams(layoutParams27);
        addLabel(imageButton28, imageButton8, !isAFlatChord ? "C#4" : "Db4", f3, false, i8, 0, 0, i9);
        ImageButton imageButton29 = (ImageButton) findViewById(R.id.middle_d_s);
        RelativeLayout.LayoutParams layoutParams28 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams28.addRule(1, R.id.middle_d);
        layoutParams28.setMargins(i10, 0, 0, 0);
        imageButton29.setLayoutParams(layoutParams28);
        addLabel(imageButton29, imageButton9, !isAFlatChord ? "D#4" : "Eb4", f3, false, i11, 0, 0, i9);
        ImageButton imageButton30 = (ImageButton) findViewById(R.id.middle_f_s);
        RelativeLayout.LayoutParams layoutParams29 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams29.addRule(1, R.id.middle_f);
        layoutParams29.setMargins(i12, 0, 0, 0);
        imageButton30.setLayoutParams(layoutParams29);
        addLabel(imageButton30, imageButton11, !isAFlatChord ? "F#4" : "Gb4", f3, false, i13, 0, 0, i9);
        ImageButton imageButton31 = (ImageButton) findViewById(R.id.middle_g_s);
        RelativeLayout.LayoutParams layoutParams30 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams30.addRule(1, R.id.middle_g);
        layoutParams30.setMargins(i14, 0, 0, 0);
        imageButton31.setLayoutParams(layoutParams30);
        addLabel(imageButton31, imageButton12, !isAFlatChord ? "G#4" : "Ab4", f3, false, i15, 0, 0, i9);
        ImageButton imageButton32 = (ImageButton) findViewById(R.id.middle_a_s);
        RelativeLayout.LayoutParams layoutParams31 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams31.addRule(1, R.id.middle_a);
        layoutParams31.setMargins(i16, 0, 0, 0);
        imageButton32.setLayoutParams(layoutParams31);
        addLabel(imageButton32, imageButton13, !isAFlatChord ? "A#4" : "Bb4", f3, false, i17, 0, 0, i9);
        ImageButton imageButton33 = (ImageButton) findViewById(R.id.high_c_s);
        RelativeLayout.LayoutParams layoutParams32 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams32.addRule(1, R.id.high_c);
        layoutParams32.setMargins(i6, 0, 0, 0);
        imageButton33.setLayoutParams(layoutParams32);
        addLabel(imageButton33, imageButton15, !isAFlatChord ? "C#5" : "Db5", f3, false, i8, 0, 0, i9);
        ImageButton imageButton34 = (ImageButton) findViewById(R.id.high_d_s);
        RelativeLayout.LayoutParams layoutParams33 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams33.addRule(1, R.id.high_d);
        layoutParams33.setMargins(i10, 0, 0, 0);
        imageButton34.setLayoutParams(layoutParams33);
        addLabel(imageButton34, imageButton16, !isAFlatChord ? "D#5" : "Eb5", f3, false, i11, 0, 0, i9);
        ImageButton imageButton35 = (ImageButton) findViewById(R.id.high_f_s);
        RelativeLayout.LayoutParams layoutParams34 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams34.addRule(1, R.id.high_f);
        layoutParams34.setMargins(i12, 0, 0, 0);
        imageButton35.setLayoutParams(layoutParams34);
        addLabel(imageButton35, imageButton18, !isAFlatChord ? "F#5" : "Gb5", f3, false, i13, 0, 0, i9);
        ImageButton imageButton36 = (ImageButton) findViewById(R.id.high_g_s);
        RelativeLayout.LayoutParams layoutParams35 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams35.addRule(1, R.id.high_g);
        layoutParams35.setMargins(i14, 0, 0, 0);
        imageButton36.setLayoutParams(layoutParams35);
        addLabel(imageButton36, imageButton19, !isAFlatChord ? "G#5" : "Ab5", f3, false, i15, 0, 0, i9);
        ImageButton imageButton37 = (ImageButton) findViewById(R.id.high_a_s);
        RelativeLayout.LayoutParams layoutParams36 = new RelativeLayout.LayoutParams(i5, (int) fBlackKeyHeightDip);
        layoutParams36.addRule(1, R.id.high_a);
        layoutParams36.setMargins(i16, 0, 0, 0);
        imageButton37.setLayoutParams(layoutParams36);
        addLabel(imageButton37, imageButton20, !isAFlatChord ? "A#5" : "Bb5", f3, false, i17, 0, 0, i9);
    }

    private void setRootNote() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("Select Root Note").setSingleChoiceItems(rootNoteValues, rootNotesIdx, new RootNoteDialog()).create();
        if (!isFinishing()) {
            create.getListView().setFastScrollEnabled(true);
            create.show();
        }
    }

    public void setUpChords() {
        List<Chord> chords2 = this.chordsManager.getChords(levelIdx, calculatedRootNote);
        chords = chords2;
        chordNames = this.chordsManager.getChordNames(chords2);
    }

    private void showHelp() {
        startActivity(new Intent(this, ChordsGameHelpActivity.class));
    }

    private void showOptions() {
        isStopDesired = true;
        isResultDialogInView = false;
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void unhighlightAllNotes() {
        Log.v("themelodymaster", "Unhighlighting all notes");
        for (Note note : this.soundManager.getAllNotes()) {
            ((ImageButton) findViewById(note.getRId())).setImageBitmap(getBitmapClean(note));
        }
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
                    boolean[] zArr2 = this.userDefinedChordFlags;
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
            String str6 = "2|" + str4 + str2;
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
        setContentView(R.layout.chords_game);

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
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seek_bar);
        this.seekBar = seekBar2;
        seekBar2.setOnSeekBarChangeListener(this);
        resources = getResources();
        this.chordsManager = ChordManager.getInstance(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.notes_layout);
        this.notesLayout = relativeLayout;
        relativeLayout.measure(0, 0);
        keyLabels = new ArrayList<>();
        this.seekBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.keyboard_400x64));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.thumb);
        drawable.setAlpha(128);
        this.seekBar.setThumb(drawable);
        this.headerTextView = (TextView) findViewById(R.id.headerText);
        setHeaderText("Piano Chords Game");
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
            String[] split = string.split(" at Root Note:");
            if (string.contains("Random")) {
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
                this.userDefinedChordFlags = new boolean[31];
                ChordManager chordManager = this.chordsManager;
                this.a = chordManager.getChordNames(chordManager.getChords(11, calculatedRootNote));
                int i = 0;
                while (true) {
                    zArr = this.userDefinedChordFlags;
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
                    this.userDefinedChordFlags[i] = Arrays.asList(split3).contains(str);
                    i++;
                }
                edit2.putInt("userDefinedChordFlags_length", zArr.length);
                for (int i2 = 0; i2 < this.userDefinedChordFlags.length; i2++) {
                    edit2.putBoolean("userDefinedChordFlags" + i2, this.userDefinedChordFlags[i2]);
                }
                edit2.apply();
            } else {
                levelIdx = Arrays.asList(levelArray).indexOf(str3);
            }
            edit2.putInt(MenuActivity.KEY_CHORD_LEVEL_IDX, levelIdx);
            edit2.apply();
        }
    }

    public void onDestroy() {
        isStopDesired = true;
        super.onDestroy();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.options) {
            Log.v("themelodymaster", "Options selected");
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
        Utility.setLocale(this, SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE, "en"));
        bringInDefaultPrefs();
    }

    public void onStop() {
        Log.v("themelodymaster", "onStop called.");
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
        if (!(view instanceof ImageButton) || view.getTag() == null || view.getTag().toString().length() <= 0) {
            return false;
        }
        if (actionMasked == 0 || actionMasked == 5) {
            Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_DOWN MotionEvent.ACTION_POINTER_DOWN and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
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
            Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (pointerId == 0 && ChordsGameActivity.this.lastFinger1Button != null) {
                        ChordsGameActivity.this.lastFinger1Button.setPressed(false);
                    }
                    int i = pointerId;
                    if (i == 1) {
                        if (ChordsGameActivity.this.lastFinger2Button != null) {
                            ChordsGameActivity.this.lastFinger2Button.setPressed(false);
                        }
                    } else if (i == 2) {
                        if (ChordsGameActivity.this.lastFinger3Button != null) {
                            ChordsGameActivity.this.lastFinger3Button.setPressed(false);
                        }
                    } else if (i == 3) {
                        if (ChordsGameActivity.this.lastFinger4Button != null) {
                            ChordsGameActivity.this.lastFinger4Button.setPressed(false);
                        }
                    } else if (i == 4 && ChordsGameActivity.this.lastFinger5Button != null) {
                        ChordsGameActivity.this.lastFinger5Button.setPressed(false);
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
                Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_MOVE pointerIdx:" + i + " pointerId:" + pointerId2);
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
                                if (ChordsGameActivity.this.lastFinger1Button != null) {
                                    ChordsGameActivity.this.lastFinger1Button.setPressed(false);
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
                                    if (ChordsGameActivity.this.lastFinger2Button != null) {
                                        ChordsGameActivity.this.lastFinger2Button.setPressed(false);
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
                                    if (ChordsGameActivity.this.lastFinger3Button != null) {
                                        ChordsGameActivity.this.lastFinger3Button.setPressed(false);
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
                                    if (ChordsGameActivity.this.lastFinger4Button != null) {
                                        ChordsGameActivity.this.lastFinger4Button.setPressed(false);
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
                                if (ChordsGameActivity.this.lastFinger5Button != null) {
                                    ChordsGameActivity.this.lastFinger5Button.setPressed(false);
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
