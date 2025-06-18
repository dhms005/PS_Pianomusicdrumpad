package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pianomusicdrumpad.pianokeyboard.Piano.models.Chord;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class ChordManager {
    private static final int BOTTOM_A = 10;
    private static final int BOTTOM_ASHARP = 11;
    private static final int BOTTOM_B = 12;
    private static final int BOTTOM_E = 5;
    private static final int BOTTOM_F = 6;
    private static final int BOTTOM_FSHARP = 7;
    private static final int BOTTOM_G = 8;
    private static final int BOTTOM_GSHARP = 9;
    private static final int INVERSION_FIRST = 1;
    private static final int INVERSION_ROOT = 0;
    private static final int INVERSION_SECOND = 2;
    private static final int LEVEL_6S_ONLY = 5;
    private static final int LEVEL_7S_ONLY = 6;
    private static final int LEVEL_9S_ONLY = 7;
    private static final int LEVEL_ADVANCED = 10;
    private static final int LEVEL_BEGINNER = 0;
    public static final int LEVEL_EVERYTHING = 11;
    private static final int LEVEL_INTERVALS = 9;
    private static final int LEVEL_MAJOR_INVERSION_ONLY = 3;
    private static final int LEVEL_MEDIUM = 2;
    private static final int LEVEL_MINOR_INVERSION_ONLY = 4;
    public static final int LEVEL_MY_FOCUS_GROUP = 12;
    private static final int LEVEL_OTHER_EXTENDED = 8;
    private static final int LEVEL_QUALITY = 1;
    private static final int MIDDLE_C = 13;
    private static final int MIDDLE_CSHARP = 14;
    private static final int MIDDLE_D = 15;
    private static final int MIDDLE_DSHARP = 16;
    private static final String SUFFIX_INVERSION_FIRST = " 1st Inversion";
    private static final String SUFFIX_INVERSION_SECOND = " 2nd Inversion";
    public static final String TYPE_11 = "11";
    public static final String TYPE_13 = "13";
    public static final String TYPE_6 = "6";
    public static final String TYPE_6_ADD_9 = "6 add 9";
    public static final String TYPE_7_6 = "7/6";
    public static final String TYPE_9 = "9";
    public static final String TYPE_ADD_9 = "Add 9";
    public static final String TYPE_AUGMENTED = "Augmented";
    public static final String TYPE_AUGMENTED_7 = "Augmented 7";
    public static final String TYPE_AUGMENTED_MAJOR_7 = "Major 7(#5)";
    public static final String TYPE_DIMINISHED = "Diminished";
    public static final String TYPE_DIMINISHED_7 = "Dim 7";
    public static final String TYPE_DIMINISHED_MAJOR_7 = "Diminished Major 7";
    public static final String TYPE_DOMINANT_7 = "7";
    public static final String TYPE_DOMINANT_7_FLAT_5 = "7(b5)";
    public static final String TYPE_HALF_DIMINISHED_7 = "Minor 7(b5)";
    public static final String TYPE_MAJOR = "Major";
    public static final String TYPE_MAJOR_7 = "Major 7";
    public static final String TYPE_MAJOR_9 = "Major 9";
    public static final String TYPE_MINOR = "Minor";
    public static final String TYPE_MINOR_11 = "Minor 11";
    public static final String TYPE_MINOR_6 = "Minor 6";
    public static final String TYPE_MINOR_7 = "Minor 7";
    public static final String TYPE_MINOR_9 = "Minor 9";
    public static final String TYPE_MINOR_MAJOR_7 = "Minor Major 7";
    public static final String TYPE_SUSPENDED_2 = "Suspended 2";
    public static final String TYPE_SUSPENDED_4 = "Suspended 4";
    private static List<String> chordNamesList = new ArrayList();
    private static List<Chord> chords = new ArrayList();
    private static ChordManager instance = null;
    private Context mContext;

    public boolean hasALoweredSecond(String str) {
        return false;
    }

    private ChordManager(Context context) {
        this.mContext = context;
    }

    private Chord getChord(String str, String str2, int i) {
        int i2;
        StringBuilder sb;
        String str3;
        Note note;
        Note note2;
        Note note3;
        Note note4;
        Note note5;
        Note note6;
        if ("E".equals(str)) {
            i2 = 5;
        } else if ("F".equals(str)) {
            i2 = 6;
        } else if ("F#".equals(str) || "Gb".equals(str)) {
            i2 = 7;
        } else if ("G".equals(str)) {
            i2 = 8;
        } else if ("G#".equals(str) || "Ab".equals(str)) {
            i2 = 9;
        } else if ("A".equals(str)) {
            i2 = 10;
        } else if ("A#".equals(str) || "Bb".equals(str)) {
            i2 = 11;
        } else if ("B".equals(str)) {
            i2 = 12;
        } else if ("C".equals(str)) {
            i2 = 13;
        } else if ("C#".equals(str) || "Db".equals(str)) {
            i2 = 14;
        } else if ("D".equals(str)) {
            i2 = 15;
        } else if (!"D#".equals(str) && !"Eb".equals(str)) {
            return null;
        } else {
            i2 = 16;
        }
        ArrayList arrayList = new ArrayList();
        if (TYPE_MAJOR.equals(str2)) {
            if (i == 1) {
                note5 = SoundManager.sounds.get(Integer.valueOf(i2 + 4));
                note4 = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
                note6 = SoundManager.sounds.get(Integer.valueOf(i2 + 12));
            } else if (i == 2) {
                note5 = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
                note6 = SoundManager.sounds.get(Integer.valueOf(i2 + 16));
                note4 = SoundManager.sounds.get(Integer.valueOf(i2 + 12));
            } else {
                note5 = SoundManager.sounds.get(Integer.valueOf(i2));
                note4 = SoundManager.sounds.get(Integer.valueOf(i2 + 4));
                note6 = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
            }
            arrayList.add(note5);
            arrayList.add(note4);
            arrayList.add(note6);
        } else if (TYPE_MINOR.equals(str2)) {
            if (i == 1) {
                note2 = SoundManager.sounds.get(Integer.valueOf(i2 + 3));
                note = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
                note3 = SoundManager.sounds.get(Integer.valueOf(i2 + 12));
            } else if (i == 2) {
                note3 = SoundManager.sounds.get(Integer.valueOf(i2 + 15));
                note2 = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
                note = SoundManager.sounds.get(Integer.valueOf(i2 + 12));
            } else {
                note2 = SoundManager.sounds.get(Integer.valueOf(i2));
                note = SoundManager.sounds.get(Integer.valueOf(i2 + 3));
                note3 = SoundManager.sounds.get(Integer.valueOf(i2 + 7));
            }
            arrayList.add(note2);
            arrayList.add(note);
            arrayList.add(note3);
        } else if (TYPE_AUGMENTED.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 8)));
        } else if (TYPE_DIMINISHED.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 6)));
        } else if (TYPE_SUSPENDED_4.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 5)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
        } else if (TYPE_SUSPENDED_2.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
        } else if (TYPE_MINOR_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_MAJOR_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 11)));
        } else if (TYPE_DOMINANT_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_DOMINANT_7_FLAT_5.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 6)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_DIMINISHED_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 6)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 9)));
        } else if (TYPE_HALF_DIMINISHED_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 6)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_DIMINISHED_MAJOR_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 6)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 11)));
        } else if (TYPE_MINOR_MAJOR_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 11)));
        } else if (TYPE_AUGMENTED_MAJOR_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 8)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 11)));
        } else if (TYPE_AUGMENTED_7.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 8)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_6.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 9)));
        } else if (TYPE_MINOR_6.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 9)));
        } else if (TYPE_7_6.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 9)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
        } else if (TYPE_6_ADD_9.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 9)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
        } else if (TYPE_9.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
        } else if (TYPE_MINOR_9.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
        } else if (TYPE_MAJOR_9.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 11)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
        } else if (TYPE_ADD_9.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
        } else if (TYPE_11.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 17)));
        } else if (TYPE_MINOR_11.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 3)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 17)));
        } else if (TYPE_13.equals(str2)) {
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 4)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 7)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 10)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 14)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 17)));
            arrayList.add(SoundManager.sounds.get(Integer.valueOf(i2 + 21)));
        }
        if (i == 1) {
            sb = new StringBuilder();
            sb.append(str);
            sb.append(" ");
            sb.append(str2);
            str3 = SUFFIX_INVERSION_FIRST;
        } else if (i == 2) {
            sb = new StringBuilder();
            sb.append(str);
            sb.append(" ");
            sb.append(str2);
            str3 = SUFFIX_INVERSION_SECOND;
        } else {
            String str4 = str + " " + str2;
            chordNamesList.add(str4);
            return new Chord(str4, arrayList);
        }
        sb.append(str3);
        String sb2 = sb.toString();
        chordNamesList.add(sb2);
        return new Chord(sb2, arrayList);
    }

    public static ChordManager getInstance(Context context) {
        if (instance == null) {
            instance = new ChordManager(context);
        }
        return instance;
    }

    public String[] getChordNames(List<Chord> list) {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strArr[i] = list.get(i).getTitle();
        }
        return strArr;
    }

    public String[] getChordNamesArray() {
        List<String> list = chordNamesList;
        return (String[]) list.toArray(new String[list.size()]);
    }

    public List<Chord> getChords() {
        return chords;
    }

    public List<Chord> getChords(int i, String str) {
        String str2 = str;
        ArrayList arrayList = new ArrayList();
        String str3 = TYPE_13;
        String str4 = TYPE_11;
        String str5 = TYPE_9;
        String str6 = TYPE_DIMINISHED_7;
        String str7 = TYPE_DOMINANT_7_FLAT_5;
        String str8 = TYPE_MINOR_MAJOR_7;
        String str9 = TYPE_DOMINANT_7;
        switch (i) {
            case 0:
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                break;
            case 1:
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_4, 0));
                break;
            case 2:
                String str10 = TYPE_MINOR_7;
                String str11 = TYPE_MAJOR_7;
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_2, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_4, 0));
                arrayList.add(getChord(str2, TYPE_6, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_6, 0));
                arrayList.add(getChord(str2, str10, 0));
                arrayList.add(getChord(str2, str11, 0));
                break;
            case 3:
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MAJOR, 1));
                arrayList.add(getChord(str2, TYPE_MAJOR, 2));
                break;
            case 4:
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 1));
                arrayList.add(getChord(str2, TYPE_MINOR, 2));
                break;
            case 5:
                arrayList.add(getChord(str2, TYPE_6, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_6, 0));
                arrayList.add(getChord(str2, TYPE_7_6, 0));
                arrayList.add(getChord(str2, TYPE_6_ADD_9, 0));
                break;
            case 6:
                String str12 = TYPE_MINOR_7;
                arrayList.add(getChord(str2, TYPE_MAJOR_7, 0));
                arrayList.add(getChord(str2, str12, 0));
                arrayList.add(getChord(str2, str9, 0));
                arrayList.add(getChord(str2, str8, 0));
                arrayList.add(getChord(str2, str7, 0));
                arrayList.add(getChord(str2, str6, 0));
                arrayList.add(getChord(str2, TYPE_HALF_DIMINISHED_7, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_7, 0));
                break;
            case 7:
                arrayList.add(getChord(str2, TYPE_ADD_9, 0));
                arrayList.add(getChord(str2, str5, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_9, 0));
                arrayList.add(getChord(str2, TYPE_MAJOR_9, 0));
                break;
            case 8:
                arrayList.add(getChord(str2, str4, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_11, 0));
                arrayList.add(getChord(str2, str3, 0));
                break;
            case 9:
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_6, 0));
                arrayList.add(getChord(str2, str9, 0));
                arrayList.add(getChord(str2, str5, 0));
                arrayList.add(getChord(str2, str4, 0));
                arrayList.add(getChord(str2, str3, 0));
                break;
            case 10:
                String str13 = TYPE_MINOR_7;
                String str14 = TYPE_MAJOR_7;
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_2, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_4, 0));
                arrayList.add(getChord(str2, TYPE_6, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_6, 0));
                arrayList.add(getChord(str2, TYPE_7_6, 0));
                arrayList.add(getChord(str2, TYPE_6_ADD_9, 0));
                arrayList.add(getChord(str2, str14, 0));
                arrayList.add(getChord(str2, str13, 0));
                arrayList.add(getChord(str2, str9, 0));
                arrayList.add(getChord(str2, str8, 0));
                arrayList.add(getChord(str2, str7, 0));
                arrayList.add(getChord(str2, str6, 0));
                arrayList.add(getChord(str2, TYPE_HALF_DIMINISHED_7, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_7, 0));
                arrayList.add(getChord(str2, TYPE_ADD_9, 0));
                arrayList.add(getChord(str2, str5, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_9, 0));
                arrayList.add(getChord(str2, TYPE_MAJOR_9, 0));
                arrayList.add(getChord(str2, str4, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_11, 0));
                arrayList.add(getChord(str2, str3, 0));
                break;
            case 11:
                String str15 = TYPE_MINOR_7;
                String str16 = TYPE_MAJOR_7;
                arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                arrayList.add(getChord(str2, TYPE_MINOR, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_2, 0));
                arrayList.add(getChord(str2, TYPE_SUSPENDED_4, 0));
                arrayList.add(getChord(str2, TYPE_6, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_6, 0));
                arrayList.add(getChord(str2, TYPE_7_6, 0));
                arrayList.add(getChord(str2, TYPE_6_ADD_9, 0));
                arrayList.add(getChord(str2, str16, 0));
                arrayList.add(getChord(str2, str15, 0));
                arrayList.add(getChord(str2, str9, 0));
                arrayList.add(getChord(str2, str8, 0));
                arrayList.add(getChord(str2, str7, 0));
                arrayList.add(getChord(str2, str6, 0));
                arrayList.add(getChord(str2, TYPE_HALF_DIMINISHED_7, 0));
                arrayList.add(getChord(str2, TYPE_DIMINISHED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_MAJOR_7, 0));
                arrayList.add(getChord(str2, TYPE_AUGMENTED_7, 0));
                arrayList.add(getChord(str2, TYPE_ADD_9, 0));
                arrayList.add(getChord(str2, str5, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_9, 0));
                arrayList.add(getChord(str2, TYPE_MAJOR_9, 0));
                arrayList.add(getChord(str2, str4, 0));
                arrayList.add(getChord(str2, TYPE_MINOR_11, 0));
                arrayList.add(getChord(str2, str3, 0));
                arrayList.add(getChord(str2, TYPE_MAJOR, 1));
                arrayList.add(getChord(str2, TYPE_MAJOR, 2));
                arrayList.add(getChord(str2, TYPE_MINOR, 1));
                arrayList.add(getChord(str2, TYPE_MINOR, 2));
                break;
            case 12:
                String str17 = TYPE_MINOR_7;
                //Log.v("ChordManager", "getChords - LevelEverything called");
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                String str18 = TYPE_MAJOR_7;
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags0", false)) {
                    arrayList.add(getChord(str2, TYPE_MAJOR, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags1", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags2", false)) {
                    arrayList.add(getChord(str2, TYPE_AUGMENTED, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags3", false)) {
                    arrayList.add(getChord(str2, TYPE_DIMINISHED, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags4", false)) {
                    arrayList.add(getChord(str2, TYPE_SUSPENDED_2, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags5", false)) {
                    arrayList.add(getChord(str2, TYPE_SUSPENDED_4, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags6", false)) {
                    arrayList.add(getChord(str2, TYPE_6, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags7", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR_6, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags8", false)) {
                    arrayList.add(getChord(str2, TYPE_7_6, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags9", false)) {
                    arrayList.add(getChord(str2, TYPE_6_ADD_9, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags10", false)) {
                    arrayList.add(getChord(str2, str18, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags11", false)) {
                    arrayList.add(getChord(str2, str17, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags12", false)) {
                    arrayList.add(getChord(str2, str9, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags13", false)) {
                    arrayList.add(getChord(str2, str8, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags14", false)) {
                    arrayList.add(getChord(str2, str7, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags15", false)) {
                    arrayList.add(getChord(str2, str6, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags16", false)) {
                    arrayList.add(getChord(str2, TYPE_HALF_DIMINISHED_7, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags17", false)) {
                    arrayList.add(getChord(str2, TYPE_DIMINISHED_MAJOR_7, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags18", false)) {
                    arrayList.add(getChord(str2, TYPE_AUGMENTED_MAJOR_7, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags19", false)) {
                    arrayList.add(getChord(str2, TYPE_AUGMENTED_7, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags20", false)) {
                    arrayList.add(getChord(str2, TYPE_ADD_9, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags21", false)) {
                    arrayList.add(getChord(str2, str5, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags22", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR_9, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags23", false)) {
                    arrayList.add(getChord(str2, TYPE_MAJOR_9, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags24", false)) {
                    arrayList.add(getChord(str2, str4, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags25", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR_11, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags26", false)) {
                    arrayList.add(getChord(str2, str3, 0));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags27", false)) {
                    arrayList.add(getChord(str2, TYPE_MAJOR, 1));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags28", false)) {
                    arrayList.add(getChord(str2, TYPE_MAJOR, 2));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags29", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR, 1));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedChordFlags30", false)) {
                    arrayList.add(getChord(str2, TYPE_MINOR, 2));
                    break;
                }
                break;
        }
        return arrayList;
    }

    public String getSolfegeLabel(String str, String str2, SoundManager soundManager, String str3) {
        //Log.v("CHORD_MANAGER", "getSolfegeLabel chordNoteName:" + str + " rootNote:" + str2 + " chordTitle:" + str3);
        StringBuilder sb = new StringBuilder();
        sb.append("chordNoteNameWithoutOctave:");
        sb.append(str.substring(0, str.length() - 1));
        //Log.v("CHORD_MANAGER", sb.toString());
        int notePosition = soundManager.getNotePosition(new Note(Note.getNoteName(str), "400")) - soundManager.getNotePosition(new Note(Note.getNoteName(str2 + "3"), "400"));
        while (notePosition >= 12) {
            notePosition -= 12;
        }
        if (notePosition == 0) {
            return "Do";
        }
        if (notePosition == 1) {
            return hasALoweredSecond(str3) ? "Ra" : "Di";
        }
        if (notePosition == 2) {
            return "Re";
        }
        if (notePosition == 3) {
            return hasALoweredThird(str3) ? "Me" : "Ri";
        }
        if (notePosition == 4) {
            return "Mi";
        }
        if (notePosition == 5) {
            return "Fa";
        }
        if (notePosition == 6) {
            return hasALoweredFifth(str3) ? "Se" : "Fi";
        }
        if (notePosition == 7) {
            return "Sol";
        }
        if (notePosition == 8) {
            return hasALoweredSixth(str3) ? "Le" : "Si";
        }
        if (notePosition == 9) {
            return "La";
        }
        return notePosition == 10 ? hasALoweredSeventh(str3) ? "Te" : "Li" : notePosition == 11 ? "Ti" : str;
    }

    public boolean hasALoweredFifth(String str) {
        return str.contains("b5") || str.contains(TYPE_DIMINISHED);
    }

    public boolean hasALoweredSeventh(String str) {
        return str.contains(TYPE_MINOR) || str.contains(TYPE_AUGMENTED);
    }

    public boolean hasALoweredSixth(String str) {
        return str.contains(TYPE_MINOR);
    }

    public boolean hasALoweredThird(String str) {
        return str.contains(TYPE_MINOR) || str.contains(TYPE_DIMINISHED);
    }

    public List<Chord> initChords(String str) {
        List<Chord> list = chords;
        list.removeAll(list);
        List<String> list2 = chordNamesList;
        list2.removeAll(list2);
        chords.add(getChord(str, TYPE_MAJOR, 0));
        chords.add(getChord(str, TYPE_MINOR, 0));
        chords.add(getChord(str, TYPE_AUGMENTED, 0));
        chords.add(getChord(str, TYPE_DIMINISHED, 0));
        chords.add(getChord(str, TYPE_SUSPENDED_2, 0));
        chords.add(getChord(str, TYPE_SUSPENDED_4, 0));
        chords.add(getChord(str, TYPE_6, 0));
        chords.add(getChord(str, TYPE_MINOR_6, 0));
        chords.add(getChord(str, TYPE_7_6, 0));
        chords.add(getChord(str, TYPE_6_ADD_9, 0));
        chords.add(getChord(str, TYPE_MAJOR_7, 0));
        chords.add(getChord(str, TYPE_MINOR_7, 0));
        chords.add(getChord(str, TYPE_DOMINANT_7, 0));
        chords.add(getChord(str, TYPE_MINOR_MAJOR_7, 0));
        chords.add(getChord(str, TYPE_DOMINANT_7_FLAT_5, 0));
        chords.add(getChord(str, TYPE_DIMINISHED_7, 0));
        chords.add(getChord(str, TYPE_HALF_DIMINISHED_7, 0));
        chords.add(getChord(str, TYPE_DIMINISHED_MAJOR_7, 0));
        chords.add(getChord(str, TYPE_AUGMENTED_MAJOR_7, 0));
        chords.add(getChord(str, TYPE_AUGMENTED_7, 0));
        chords.add(getChord(str, TYPE_ADD_9, 0));
        chords.add(getChord(str, TYPE_9, 0));
        chords.add(getChord(str, TYPE_MINOR_9, 0));
        chords.add(getChord(str, TYPE_MAJOR_9, 0));
        chords.add(getChord(str, TYPE_11, 0));
        chords.add(getChord(str, TYPE_MINOR_11, 0));
        chords.add(getChord(str, TYPE_13, 0));
        chords.add(getChord(str, TYPE_MAJOR, 1));
        chords.add(getChord(str, TYPE_MAJOR, 2));
        chords.add(getChord(str, TYPE_MINOR, 1));
        chords.add(getChord(str, TYPE_MINOR, 2));
        return chords;
    }

    public String recogniseChord(TreeSet<Note> treeSet, boolean z) {
        String scaleNoteName = treeSet.first().getScaleNoteName(z ? "b " : "");
        List<Chord> initChords = initChords(scaleNoteName.substring(0, scaleNoteName.length() - 1));
        TreeSet treeSet2 = new TreeSet();
        Iterator<Note> it = treeSet.iterator();
        while (it.hasNext()) {
            String name = it.next().getName();
            treeSet2.add(name.substring(0, name.length() - 1));
        }
        for (Chord next : initChords) {
            List<Note> notes = next.getNotes();
            TreeSet treeSet3 = new TreeSet();
            for (Note name2 : notes) {
                String name3 = name2.getName();
                treeSet3.add(name3.substring(0, name3.length() - 1));
            }
            if (treeSet3.containsAll(treeSet2)) {
                return next.getTitle();
            }
        }
        return null;
    }
}
