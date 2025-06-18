package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Scale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class ScalesManager {
    private static final int BOTTOM_A = 9;
    private static final int BOTTOM_ASHARP = 10;
    private static final int BOTTOM_B = 11;
    private static final int BOTTOM_E = 4;
    private static final int BOTTOM_F = 5;
    private static final int BOTTOM_FSHARP = 6;
    private static final int BOTTOM_G = 7;
    private static final int BOTTOM_GSHARP = 8;
    private static final String DURATION = "400";
    public static final int INVERSION_FIRST = 1;
    public static final int INVERSION_ROOT = 0;
    public static final int INVERSION_SECOND = 2;
    public static int JAM_ACTIVITY_RESOURCES = 2;
    private static final int LEVEL_ARPEGGIOS = 5;
    private static final int LEVEL_BEGINNER = 0;
    private static final int LEVEL_EASY = 1;
    public static final int LEVEL_EVERYTHING = 6;
    private static final int LEVEL_MEDIUM = 2;
    private static final int LEVEL_MODES = 4;
    public static final int LEVEL_MY_FOCUS_GROUP = 7;
    private static final int LEVEL_RARE = 3;
    private static final int MIDDLE_C = 12;
    private static final int MIDDLE_CSHARP = 13;
    private static final int MIDDLE_D = 14;
    private static final int MIDDLE_DSHARP = 15;
    public static int SCALES_ACTIVITY_RESOURCES = 1;
    public static String TYPE_CHORD_11 = "11 arpeggio";
    public static String TYPE_CHORD_13 = "13 arpeggio";
    public static String TYPE_CHORD_6 = "6 arpeggio";
    public static String TYPE_CHORD_6_ADD_9 = "6 add 9 arpeggio";
    public static String TYPE_CHORD_7_6 = "7/6 arpeggio";
    public static String TYPE_CHORD_9 = "9 arpeggio";
    public static String TYPE_CHORD_ADD_9 = "Add 9 arpeggio";
    public static String TYPE_CHORD_AUGMENTED = "Augmented arpeggio";
    public static String TYPE_CHORD_AUGMENTED_7 = "Augmented 7 arpeggio";
    public static String TYPE_CHORD_AUGMENTED_MAJOR_7 = "Major 7(#5) arpeggio";
    public static String TYPE_CHORD_DIMINISHED = "Diminished arpeggio";
    public static String TYPE_CHORD_DIMINISHED_7 = "Dim 7 arpeggio";
    public static String TYPE_CHORD_DIMINISHED_MAJOR_7 = "Diminished Major 7 arpeggio";
    public static String TYPE_CHORD_DOMINANT_7 = "7 arpeggio";
    public static String TYPE_CHORD_DOMINANT_7_FLAT_5 = "7(b5) arpeggio";
    public static String TYPE_CHORD_HALF_DIMINISHED_7 = "Minor 7(b5) arpeggio";
    public static String TYPE_CHORD_MAJOR = "Major arpeggio";
    public static String TYPE_CHORD_MAJOR_7 = "Major 7 arpeggio";
    public static String TYPE_CHORD_MAJOR_9 = "Major 9 arpeggio";
    public static String TYPE_CHORD_MINOR = "Minor arpeggio";
    public static String TYPE_CHORD_MINOR_11 = "Minor 11 arpeggio";
    public static String TYPE_CHORD_MINOR_6 = "Minor 6 arpeggio";
    public static String TYPE_CHORD_MINOR_7 = "Minor 7 arpeggio";
    public static String TYPE_CHORD_MINOR_9 = "Minor 9 arpeggio";
    public static String TYPE_CHORD_MINOR_MAJOR_7 = "Minor Major 7 arpeggio";
    public static String TYPE_CHORD_SUSPENDED_2 = "Suspended 2 arpeggio";
    public static String TYPE_CHORD_SUSPENDED_4 = "Suspended 4 arpeggio";
    public static String TYPE_SCALE_ACOUSTIC = "Acoustic scale";
    public static String TYPE_SCALE_ALGERIAN = "Algerian scale";
    public static String TYPE_SCALE_ALTERED = "Altered scale";
    public static String TYPE_SCALE_AUGMENTED = "Augmented scale";
    public static String TYPE_SCALE_BEPOP_DOMINANT = "Bepop Dominant scale";
    public static String TYPE_SCALE_BLUES = "Blues scale";
    public static String TYPE_SCALE_CHROMATIC = "Chromatic scale";
    public static String TYPE_SCALE_DOUBLE_HARMONIC = "Double Harmonic scale";
    public static String TYPE_SCALE_ENIGMATIC = "Enigmatic scale";
    public static String TYPE_SCALE_GYPSY = "Gypsy scale";
    public static String TYPE_SCALE_HALF_DIMINISHED = "Half Diminished scale";
    public static String TYPE_SCALE_HALF_WHOLE_TONE = "Half-Whole Tone scale";
    public static String TYPE_SCALE_HARMONICS = "Harmonics scale";
    public static String TYPE_SCALE_HARMONIC_MAJOR = "Harmonic Major scale";
    public static String TYPE_SCALE_HARMONIC_MINOR = "Harmonic Minor scale";
    public static String TYPE_SCALE_HIRAJOSHI = "Hirajoshi scale";
    public static String TYPE_SCALE_HUNGARIAN_GYPSY = "Hungarian Gypsy scale";
    public static String TYPE_SCALE_HUNGARIAN_MINOR = "Hungarian Minor scale";
    public static String TYPE_SCALE_INSEN = "Insen scale";
    public static String TYPE_SCALE_IWATO = "Iwato scale";
    public static String TYPE_SCALE_MAJOR = "Major scale";
    public static String TYPE_SCALE_MAJOR_PENTATONIC = "Major Pentatonic scale";
    public static String TYPE_SCALE_MINOR = "Minor Natural scale";
    public static String TYPE_SCALE_MINOR_HARMONIC = "Minor Harmonic scale";
    public static String TYPE_SCALE_MINOR_MELODIC = "Minor Melodic scale";
    public static String TYPE_SCALE_MINOR_MELODIC_DESC = "Minor Melodic scale descending";
    public static String TYPE_SCALE_MINOR_PENTATONIC = "Minor Pentatonic scale";
    public static String TYPE_SCALE_NEOPOLITAN_MAJOR = "Neopolitan Major scale";
    public static String TYPE_SCALE_NEOPOLITAN_MINOR = "Neopolitan Minor scale";
    public static String TYPE_SCALE_PERSIAN = "Persian scale";
    public static String TYPE_SCALE_PROMETHEUS = "Prometheus scale";
    public static String TYPE_SCALE_TRITONE = "Tritone scale";
    public static String TYPE_SCALE_UKRANIAN_DORIAN = "Ukranian Dorian scale";
    public static String TYPE_SCALE_WHOLE_HALF_TONE = "Whole-Half Tone scale";
    public static String TYPE_SCALE_WHOLE_TONE = "Whole Tone scale";
    static String a = " 1st Inversion";
    static String b = " 2nd Inversion";
    static String c = "Adonai Malakh mode";
    static String d = "Aeolian mode";
    static String e = "Dorian mode";
    static String f = "Flamenco mode";
    static String g = "Ionian mode";
    static String h = "Locrian mode";
    static String i = "Lydian mode";
    private static ScalesManager instance = null;
    static String j = "Mixolydian mode";
    static String k = "Phrygian mode";
    static String l = "Phrygian Dominant mode";
    private static String[] noteNames = {"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5", "C6"};
    private static List<Scale> scales = new ArrayList();
    private static List<String> scalesNamesList = new ArrayList();
    private Context mContext;

    private ScalesManager(Context context) {
        this.mContext = context;
    }

    public static ScalesManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScalesManager(context);
        }
        return instance;
    }

    public int getFingerPosition(int i2, Note note, List<Note> list) {
        if (i2 != 0) {
            int i3 = i2 - 1;
            int fingerPosition = getFingerPosition(i3, list.get(i3), list);
            if (fingerPosition == 1) {
                return 2;
            }
            if (fingerPosition == 2) {
                return 3;
            }
            if ((fingerPosition != 3 || i2 + 2 < list.size()) && ((fingerPosition != 3 || note.getName().contains("#")) && ((fingerPosition != 3 || !note.getName().contains("#")) && ((fingerPosition != 4 || i2 + 1 < list.size()) && fingerPosition == 4)))) {
                note.getName().contains("#");
            }
            return 4;
        } else if (!note.getName().contains("#")) {
            return 1;
        } else {
            if (!list.get(1).getName().contains("#")) {
                return 3;
            }
            if (!list.get(2).getName().contains("#") || !list.get(3).getName().contains("#")) {
                return 2;
            }
            return 1;
        }
    }

    public Scale getScale(String str, String str2, int i2, boolean z) {
        int i3;
        int i4;
        StringBuilder sb;
        String str3;
        String str4;
        String str5;
        String str6 = str2;
        ArrayList arrayList = new ArrayList();
        if ("E".equals(str)) {
            i3 = 4;
        } else if ("F".equals(str)) {
            i3 = 5;
        } else if ("F#".equals(str) || "Gb".equals(str)) {
            i3 = 6;
        } else if ("G".equals(str)) {
            i3 = 7;
        } else if ("G#".equals(str) || "Ab".equals(str)) {
            i3 = 8;
        } else if ("A".equals(str)) {
            i3 = 9;
        } else if ("A#".equals(str) || "Bb".equals(str)) {
            i3 = 10;
        } else if ("B".equals(str)) {
            i3 = 11;
        } else if ("C".equals(str)) {
            i3 = 12;
        } else if ("C#".equals(str) || "Db".equals(str)) {
            i3 = 13;
        } else if ("D".equals(str)) {
            i3 = 14;
        } else if (!"D#".equals(str) && !"Eb".equals(str)) {
            return null;
        } else {
            i3 = 15;
        }
        if (TYPE_SCALE_MAJOR.equals(str6)) {
            String[] strArr = noteNames;
            String str7 = strArr[i3];
            String str8 = strArr[i3 + 2];
            String str9 = strArr[i3 + 4];
            String str10 = strArr[i3 + 5];
            String str11 = strArr[i3 + 7];
            String str12 = strArr[i3 + 9];
            String str13 = strArr[i3 + 11];
            String str14 = strArr[i3 + 12];
            arrayList.add(new Note(str7, DURATION));
            arrayList.add(new Note(str8, DURATION));
            arrayList.add(new Note(str9, DURATION));
            arrayList.add(new Note(str10, DURATION));
            arrayList.add(new Note(str11, DURATION));
            arrayList.add(new Note(str12, DURATION));
            arrayList.add(new Note(str13, DURATION));
            arrayList.add(new Note(str14, DURATION));
        } else if (TYPE_SCALE_MINOR.equals(str6)) {
            String[] strArr2 = noteNames;
            String str15 = strArr2[i3];
            String str16 = strArr2[i3 + 2];
            String str17 = strArr2[i3 + 3];
            String str18 = strArr2[i3 + 5];
            String str19 = strArr2[i3 + 7];
            String str20 = strArr2[i3 + 8];
            String str21 = strArr2[i3 + 10];
            String str22 = strArr2[i3 + 12];
            arrayList.add(new Note(str15, DURATION));
            arrayList.add(new Note(str16, DURATION));
            arrayList.add(new Note(str17, DURATION));
            arrayList.add(new Note(str18, DURATION));
            arrayList.add(new Note(str19, DURATION));
            arrayList.add(new Note(str20, DURATION));
            arrayList.add(new Note(str21, DURATION));
            arrayList.add(new Note(str22, DURATION));
        } else if (TYPE_SCALE_MINOR_HARMONIC.equals(str6)) {
            String[] strArr3 = noteNames;
            String str23 = strArr3[i3];
            String str24 = strArr3[i3 + 2];
            String str25 = strArr3[i3 + 3];
            String str26 = strArr3[i3 + 5];
            String str27 = strArr3[i3 + 7];
            String str28 = strArr3[i3 + 8];
            String str29 = strArr3[i3 + 11];
            String str30 = strArr3[i3 + 12];
            arrayList.add(new Note(str23, DURATION));
            arrayList.add(new Note(str24, DURATION));
            arrayList.add(new Note(str25, DURATION));
            arrayList.add(new Note(str26, DURATION));
            arrayList.add(new Note(str27, DURATION));
            arrayList.add(new Note(str28, DURATION));
            arrayList.add(new Note(str29, DURATION));
            arrayList.add(new Note(str30, DURATION));
        } else if (TYPE_SCALE_MINOR_MELODIC.equals(str6)) {
            String[] strArr4 = noteNames;
            String str31 = strArr4[i3];
            String str32 = strArr4[i3 + 2];
            String str33 = strArr4[i3 + 3];
            String str34 = strArr4[i3 + 5];
            String str35 = strArr4[i3 + 7];
            String str36 = strArr4[i3 + 9];
            String str37 = strArr4[i3 + 11];
            String str38 = strArr4[i3 + 12];
            arrayList.add(new Note(str31, DURATION));
            arrayList.add(new Note(str32, DURATION));
            arrayList.add(new Note(str33, DURATION));
            arrayList.add(new Note(str34, DURATION));
            arrayList.add(new Note(str35, DURATION));
            arrayList.add(new Note(str36, DURATION));
            arrayList.add(new Note(str37, DURATION));
            arrayList.add(new Note(str38, DURATION));
        } else if (TYPE_SCALE_MINOR_MELODIC_DESC.equals(str6)) {
            String[] strArr5 = noteNames;
            String str39 = strArr5[i3 + 12];
            String str40 = strArr5[i3 + 10];
            String str41 = strArr5[i3 + 8];
            String str42 = strArr5[i3 + 7];
            String str43 = strArr5[i3 + 5];
            String str44 = strArr5[i3 + 3];
            String str45 = strArr5[i3 + 2];
            String str46 = strArr5[i3 + 0];
            arrayList.add(new Note(str39, DURATION));
            arrayList.add(new Note(str40, DURATION));
            arrayList.add(new Note(str41, DURATION));
            arrayList.add(new Note(str42, DURATION));
            arrayList.add(new Note(str43, DURATION));
            arrayList.add(new Note(str44, DURATION));
            arrayList.add(new Note(str45, DURATION));
            arrayList.add(new Note(str46, DURATION));
        } else if (TYPE_SCALE_MAJOR_PENTATONIC.equals(str6)) {
            String[] strArr6 = noteNames;
            String str47 = strArr6[i3];
            String str48 = strArr6[i3 + 2];
            String str49 = strArr6[i3 + 4];
            String str50 = strArr6[i3 + 7];
            String str51 = strArr6[i3 + 9];
            String str52 = strArr6[i3 + 12];
            arrayList.add(new Note(str47, DURATION));
            arrayList.add(new Note(str48, DURATION));
            arrayList.add(new Note(str49, DURATION));
            arrayList.add(new Note(str50, DURATION));
            arrayList.add(new Note(str51, DURATION));
            arrayList.add(new Note(str52, DURATION));
        } else if (TYPE_SCALE_MINOR_PENTATONIC.equals(str6)) {
            String[] strArr7 = noteNames;
            String str53 = strArr7[i3];
            String str54 = strArr7[i3 + 3];
            String str55 = strArr7[i3 + 5];
            String str56 = strArr7[i3 + 7];
            String str57 = strArr7[i3 + 10];
            String str58 = strArr7[i3 + 12];
            arrayList.add(new Note(str53, DURATION));
            arrayList.add(new Note(str54, DURATION));
            arrayList.add(new Note(str55, DURATION));
            arrayList.add(new Note(str56, DURATION));
            arrayList.add(new Note(str57, DURATION));
            arrayList.add(new Note(str58, DURATION));
        } else if (TYPE_SCALE_BLUES.equals(str6)) {
            String[] strArr8 = noteNames;
            String str59 = strArr8[i3];
            String str60 = strArr8[i3 + 3];
            String str61 = strArr8[i3 + 5];
            String str62 = strArr8[i3 + 6];
            String str63 = strArr8[i3 + 7];
            String str64 = strArr8[i3 + 10];
            String str65 = strArr8[i3 + 12];
            arrayList.add(new Note(str59, DURATION));
            arrayList.add(new Note(str60, DURATION));
            arrayList.add(new Note(str61, DURATION));
            arrayList.add(new Note(str62, DURATION));
            arrayList.add(new Note(str63, DURATION));
            arrayList.add(new Note(str64, DURATION));
            arrayList.add(new Note(str65, DURATION));
        } else if (TYPE_SCALE_ACOUSTIC.equals(str6)) {
            String[] strArr9 = noteNames;
            String str66 = strArr9[i3];
            String str67 = strArr9[i3 + 2];
            String str68 = strArr9[i3 + 4];
            String str69 = strArr9[i3 + 6];
            String str70 = strArr9[i3 + 7];
            String str71 = strArr9[i3 + 9];
            String str72 = strArr9[i3 + 10];
            String str73 = strArr9[i3 + 12];
            arrayList.add(new Note(str66, DURATION));
            arrayList.add(new Note(str67, DURATION));
            arrayList.add(new Note(str68, DURATION));
            arrayList.add(new Note(str69, DURATION));
            arrayList.add(new Note(str70, DURATION));
            arrayList.add(new Note(str71, DURATION));
            arrayList.add(new Note(str72, DURATION));
            arrayList.add(new Note(str73, DURATION));
        } else if (TYPE_SCALE_ALGERIAN.equals(str6)) {
            String[] strArr10 = noteNames;
            String str74 = strArr10[i3];
            String str75 = strArr10[i3 + 2];
            String str76 = strArr10[i3 + 3];
            String str77 = strArr10[i3 + 6];
            String str78 = strArr10[i3 + 7];
            String str79 = strArr10[i3 + 8];
            String str80 = strArr10[i3 + 11];
            String str81 = strArr10[i3 + 12];
            arrayList.add(new Note(str74, DURATION));
            arrayList.add(new Note(str75, DURATION));
            arrayList.add(new Note(str76, DURATION));
            arrayList.add(new Note(str77, DURATION));
            arrayList.add(new Note(str78, DURATION));
            arrayList.add(new Note(str79, DURATION));
            arrayList.add(new Note(str80, DURATION));
            arrayList.add(new Note(str81, DURATION));
        } else if (TYPE_SCALE_ALTERED.equals(str6)) {
            String[] strArr11 = noteNames;
            String str82 = strArr11[i3];
            String str83 = strArr11[i3 + 1];
            String str84 = strArr11[i3 + 3];
            String str85 = strArr11[i3 + 4];
            String str86 = strArr11[i3 + 6];
            String str87 = strArr11[i3 + 8];
            String str88 = strArr11[i3 + 10];
            String str89 = strArr11[i3 + 12];
            arrayList.add(new Note(str82, DURATION));
            arrayList.add(new Note(str83, DURATION));
            arrayList.add(new Note(str84, DURATION));
            arrayList.add(new Note(str85, DURATION));
            arrayList.add(new Note(str86, DURATION));
            arrayList.add(new Note(str87, DURATION));
            arrayList.add(new Note(str88, DURATION));
            arrayList.add(new Note(str89, DURATION));
        } else if (TYPE_SCALE_AUGMENTED.equals(str6)) {
            String[] strArr12 = noteNames;
            String str90 = strArr12[i3];
            String str91 = strArr12[i3 + 3];
            String str92 = strArr12[i3 + 4];
            String str93 = strArr12[i3 + 7];
            String str94 = strArr12[i3 + 8];
            String str95 = strArr12[i3 + 11];
            String str96 = strArr12[i3 + 12];
            arrayList.add(new Note(str90, DURATION));
            arrayList.add(new Note(str91, DURATION));
            arrayList.add(new Note(str92, DURATION));
            arrayList.add(new Note(str93, DURATION));
            arrayList.add(new Note(str94, DURATION));
            arrayList.add(new Note(str95, DURATION));
            arrayList.add(new Note(str96, DURATION));
        } else if (TYPE_SCALE_BEPOP_DOMINANT.equals(str6)) {
            String[] strArr13 = noteNames;
            String str97 = strArr13[i3];
            String str98 = strArr13[i3 + 2];
            String str99 = strArr13[i3 + 4];
            String str100 = strArr13[i3 + 5];
            String str101 = strArr13[i3 + 7];
            String str102 = strArr13[i3 + 9];
            String str103 = strArr13[i3 + 10];
            String str104 = strArr13[i3 + 11];
            String str105 = strArr13[i3 + 12];
            arrayList.add(new Note(str97, DURATION));
            arrayList.add(new Note(str98, DURATION));
            arrayList.add(new Note(str99, DURATION));
            arrayList.add(new Note(str100, DURATION));
            arrayList.add(new Note(str101, DURATION));
            arrayList.add(new Note(str102, DURATION));
            arrayList.add(new Note(str103, DURATION));
            arrayList.add(new Note(str104, DURATION));
            arrayList.add(new Note(str105, DURATION));
        } else {
            if (TYPE_SCALE_CHROMATIC.equals(str6)) {
                String[] strArr14 = noteNames;
                String str106 = strArr14[i3];
                String str107 = strArr14[i3 + 1];
                String str108 = strArr14[i3 + 2];
                String str109 = strArr14[i3 + 3];
                String str110 = strArr14[i3 + 4];
                String str111 = strArr14[i3 + 5];
                String str112 = strArr14[i3 + 6];
                String str113 = strArr14[i3 + 7];
                String str114 = strArr14[i3 + 8];
                String str115 = strArr14[i3 + 9];
                String str116 = strArr14[i3 + 10];
                String str117 = strArr14[i3 + 11];
                String str118 = strArr14[i3 + 12];
                arrayList.add(new Note(str106, DURATION));
                arrayList.add(new Note(str107, DURATION));
                arrayList.add(new Note(str108, DURATION));
                arrayList.add(new Note(str109, DURATION));
                arrayList.add(new Note(str110, DURATION));
                arrayList.add(new Note(str111, DURATION));
                arrayList.add(new Note(str112, DURATION));
                arrayList.add(new Note(str113, DURATION));
                arrayList.add(new Note(str114, DURATION));
                arrayList.add(new Note(str115, DURATION));
                arrayList.add(new Note(str116, DURATION));
                arrayList.add(new Note(str117, DURATION));
                arrayList.add(new Note(str118, DURATION));
                str6 = str2;
            } else {
                str6 = str2;
                if (TYPE_SCALE_DOUBLE_HARMONIC.equals(str6)) {
                    String[] strArr15 = noteNames;
                    String str119 = strArr15[i3];
                    String str120 = strArr15[i3 + 1];
                    String str121 = strArr15[i3 + 4];
                    String str122 = strArr15[i3 + 5];
                    String str123 = strArr15[i3 + 7];
                    String str124 = strArr15[i3 + 8];
                    String str125 = strArr15[i3 + 11];
                    String str126 = strArr15[i3 + 12];
                    arrayList.add(new Note(str119, DURATION));
                    arrayList.add(new Note(str120, DURATION));
                    arrayList.add(new Note(str121, DURATION));
                    arrayList.add(new Note(str122, DURATION));
                    arrayList.add(new Note(str123, DURATION));
                    arrayList.add(new Note(str124, DURATION));
                    arrayList.add(new Note(str125, DURATION));
                    arrayList.add(new Note(str126, DURATION));
                } else if (TYPE_SCALE_ENIGMATIC.equals(str6)) {
                    String[] strArr16 = noteNames;
                    String str127 = strArr16[i3];
                    String str128 = strArr16[i3 + 1];
                    String str129 = strArr16[i3 + 4];
                    String str130 = strArr16[i3 + 6];
                    String str131 = strArr16[i3 + 8];
                    String str132 = strArr16[i3 + 10];
                    String str133 = strArr16[i3 + 11];
                    String str134 = strArr16[i3 + 12];
                    arrayList.add(new Note(str127, DURATION));
                    arrayList.add(new Note(str128, DURATION));
                    arrayList.add(new Note(str129, DURATION));
                    arrayList.add(new Note(str130, DURATION));
                    arrayList.add(new Note(str131, DURATION));
                    arrayList.add(new Note(str132, DURATION));
                    arrayList.add(new Note(str133, DURATION));
                    arrayList.add(new Note(str134, DURATION));
                } else if (TYPE_SCALE_GYPSY.equals(str6)) {
                    String[] strArr17 = noteNames;
                    String str135 = strArr17[i3];
                    String str136 = strArr17[i3 + 2];
                    String str137 = strArr17[i3 + 3];
                    String str138 = strArr17[i3 + 6];
                    String str139 = strArr17[i3 + 7];
                    String str140 = strArr17[i3 + 8];
                    String str141 = strArr17[i3 + 10];
                    String str142 = strArr17[i3 + 12];
                    arrayList.add(new Note(str135, DURATION));
                    arrayList.add(new Note(str136, DURATION));
                    arrayList.add(new Note(str137, DURATION));
                    arrayList.add(new Note(str138, DURATION));
                    arrayList.add(new Note(str139, DURATION));
                    arrayList.add(new Note(str140, DURATION));
                    arrayList.add(new Note(str141, DURATION));
                    arrayList.add(new Note(str142, DURATION));
                } else if (TYPE_SCALE_HALF_DIMINISHED.equals(str6)) {
                    String[] strArr18 = noteNames;
                    String str143 = strArr18[i3];
                    String str144 = strArr18[i3 + 2];
                    String str145 = strArr18[i3 + 3];
                    String str146 = strArr18[i3 + 5];
                    String str147 = strArr18[i3 + 6];
                    String str148 = strArr18[i3 + 8];
                    String str149 = strArr18[i3 + 10];
                    String str150 = strArr18[i3 + 12];
                    arrayList.add(new Note(str143, DURATION));
                    arrayList.add(new Note(str144, DURATION));
                    arrayList.add(new Note(str145, DURATION));
                    arrayList.add(new Note(str146, DURATION));
                    arrayList.add(new Note(str147, DURATION));
                    arrayList.add(new Note(str148, DURATION));
                    arrayList.add(new Note(str149, DURATION));
                    arrayList.add(new Note(str150, DURATION));
                } else if (TYPE_SCALE_HALF_WHOLE_TONE.equals(str6)) {
                    String[] strArr19 = noteNames;
                    String str151 = strArr19[i3];
                    String str152 = strArr19[i3 + 1];
                    String str153 = strArr19[i3 + 3];
                    String str154 = strArr19[i3 + 4];
                    String str155 = strArr19[i3 + 6];
                    String str156 = strArr19[i3 + 7];
                    String str157 = strArr19[i3 + 9];
                    String str158 = strArr19[i3 + 10];
                    String str159 = strArr19[i3 + 12];
                    arrayList.add(new Note(str151, DURATION));
                    arrayList.add(new Note(str152, DURATION));
                    arrayList.add(new Note(str153, DURATION));
                    arrayList.add(new Note(str154, DURATION));
                    arrayList.add(new Note(str155, DURATION));
                    arrayList.add(new Note(str156, DURATION));
                    arrayList.add(new Note(str157, DURATION));
                    arrayList.add(new Note(str158, DURATION));
                    arrayList.add(new Note(str159, DURATION));
                } else if (TYPE_SCALE_HARMONIC_MAJOR.equals(str6)) {
                    String[] strArr20 = noteNames;
                    String str160 = strArr20[i3];
                    String str161 = strArr20[i3 + 2];
                    String str162 = strArr20[i3 + 4];
                    String str163 = strArr20[i3 + 5];
                    String str164 = strArr20[i3 + 7];
                    String str165 = strArr20[i3 + 8];
                    String str166 = strArr20[i3 + 11];
                    String str167 = strArr20[i3 + 12];
                    arrayList.add(new Note(str160, DURATION));
                    arrayList.add(new Note(str161, DURATION));
                    arrayList.add(new Note(str162, DURATION));
                    arrayList.add(new Note(str163, DURATION));
                    arrayList.add(new Note(str164, DURATION));
                    arrayList.add(new Note(str165, DURATION));
                    arrayList.add(new Note(str166, DURATION));
                    arrayList.add(new Note(str167, DURATION));
                } else if (TYPE_SCALE_HARMONIC_MINOR.equals(str6)) {
                    String[] strArr21 = noteNames;
                    String str168 = strArr21[i3];
                    String str169 = strArr21[i3 + 2];
                    String str170 = strArr21[i3 + 3];
                    String str171 = strArr21[i3 + 5];
                    String str172 = strArr21[i3 + 7];
                    String str173 = strArr21[i3 + 8];
                    String str174 = strArr21[i3 + 11];
                    String str175 = strArr21[i3 + 12];
                    arrayList.add(new Note(str168, DURATION));
                    arrayList.add(new Note(str169, DURATION));
                    arrayList.add(new Note(str170, DURATION));
                    arrayList.add(new Note(str171, DURATION));
                    arrayList.add(new Note(str172, DURATION));
                    arrayList.add(new Note(str173, DURATION));
                    arrayList.add(new Note(str174, DURATION));
                    arrayList.add(new Note(str175, DURATION));
                } else if (TYPE_SCALE_HARMONICS.equals(str6)) {
                    String[] strArr22 = noteNames;
                    String str176 = strArr22[i3];
                    String str177 = strArr22[i3 + 3];
                    String str178 = strArr22[i3 + 4];
                    String str179 = strArr22[i3 + 5];
                    String str180 = strArr22[i3 + 7];
                    String str181 = strArr22[i3 + 9];
                    String str182 = strArr22[i3 + 12];
                    arrayList.add(new Note(str176, DURATION));
                    arrayList.add(new Note(str177, DURATION));
                    arrayList.add(new Note(str178, DURATION));
                    arrayList.add(new Note(str179, DURATION));
                    arrayList.add(new Note(str180, DURATION));
                    arrayList.add(new Note(str181, DURATION));
                    arrayList.add(new Note(str182, DURATION));
                } else if (TYPE_SCALE_HIRAJOSHI.equals(str6)) {
                    String[] strArr23 = noteNames;
                    String str183 = strArr23[i3];
                    String str184 = strArr23[i3 + 4];
                    String str185 = strArr23[i3 + 6];
                    String str186 = strArr23[i3 + 7];
                    String str187 = strArr23[i3 + 11];
                    String str188 = strArr23[i3 + 12];
                    arrayList.add(new Note(str183, DURATION));
                    arrayList.add(new Note(str184, DURATION));
                    arrayList.add(new Note(str185, DURATION));
                    arrayList.add(new Note(str186, DURATION));
                    arrayList.add(new Note(str187, DURATION));
                    arrayList.add(new Note(str188, DURATION));
                } else if (TYPE_SCALE_HUNGARIAN_GYPSY.equals(str6)) {
                    String[] strArr24 = noteNames;
                    String str189 = strArr24[i3];
                    String str190 = strArr24[i3 + 2];
                    String str191 = strArr24[i3 + 3];
                    String str192 = strArr24[i3 + 6];
                    String str193 = strArr24[i3 + 7];
                    String str194 = strArr24[i3 + 8];
                    String str195 = strArr24[i3 + 11];
                    String str196 = strArr24[i3 + 12];
                    arrayList.add(new Note(str189, DURATION));
                    arrayList.add(new Note(str190, DURATION));
                    arrayList.add(new Note(str191, DURATION));
                    arrayList.add(new Note(str192, DURATION));
                    arrayList.add(new Note(str193, DURATION));
                    arrayList.add(new Note(str194, DURATION));
                    arrayList.add(new Note(str195, DURATION));
                    arrayList.add(new Note(str196, DURATION));
                } else if (TYPE_SCALE_HUNGARIAN_MINOR.equals(str6)) {
                    String[] strArr25 = noteNames;
                    String str197 = strArr25[i3];
                    String str198 = strArr25[i3 + 2];
                    String str199 = strArr25[i3 + 3];
                    String str200 = strArr25[i3 + 6];
                    String str201 = strArr25[i3 + 7];
                    String str202 = strArr25[i3 + 8];
                    String str203 = strArr25[i3 + 11];
                    String str204 = strArr25[i3 + 12];
                    arrayList.add(new Note(str197, DURATION));
                    arrayList.add(new Note(str198, DURATION));
                    arrayList.add(new Note(str199, DURATION));
                    arrayList.add(new Note(str200, DURATION));
                    arrayList.add(new Note(str201, DURATION));
                    arrayList.add(new Note(str202, DURATION));
                    arrayList.add(new Note(str203, DURATION));
                    arrayList.add(new Note(str204, DURATION));
                } else if (TYPE_SCALE_INSEN.equals(str6)) {
                    String[] strArr26 = noteNames;
                    String str205 = strArr26[i3];
                    String str206 = strArr26[i3 + 1];
                    String str207 = strArr26[i3 + 5];
                    String str208 = strArr26[i3 + 7];
                    String str209 = strArr26[i3 + 10];
                    String str210 = strArr26[i3 + 12];
                    arrayList.add(new Note(str205, DURATION));
                    arrayList.add(new Note(str206, DURATION));
                    arrayList.add(new Note(str207, DURATION));
                    arrayList.add(new Note(str208, DURATION));
                    arrayList.add(new Note(str209, DURATION));
                    arrayList.add(new Note(str210, DURATION));
                } else if (TYPE_SCALE_IWATO.equals(str6)) {
                    String[] strArr27 = noteNames;
                    String str211 = strArr27[i3];
                    String str212 = strArr27[i3 + 1];
                    String str213 = strArr27[i3 + 5];
                    String str214 = strArr27[i3 + 6];
                    String str215 = strArr27[i3 + 10];
                    String str216 = strArr27[i3 + 12];
                    arrayList.add(new Note(str211, DURATION));
                    arrayList.add(new Note(str212, DURATION));
                    arrayList.add(new Note(str213, DURATION));
                    arrayList.add(new Note(str214, DURATION));
                    arrayList.add(new Note(str215, DURATION));
                    arrayList.add(new Note(str216, DURATION));
                } else if (TYPE_SCALE_NEOPOLITAN_MAJOR.equals(str6)) {
                    String[] strArr28 = noteNames;
                    String str217 = strArr28[i3];
                    String str218 = strArr28[i3 + 1];
                    String str219 = strArr28[i3 + 3];
                    String str220 = strArr28[i3 + 5];
                    String str221 = strArr28[i3 + 7];
                    String str222 = strArr28[i3 + 9];
                    String str223 = strArr28[i3 + 11];
                    String str224 = strArr28[i3 + 12];
                    arrayList.add(new Note(str217, DURATION));
                    arrayList.add(new Note(str218, DURATION));
                    arrayList.add(new Note(str219, DURATION));
                    arrayList.add(new Note(str220, DURATION));
                    arrayList.add(new Note(str221, DURATION));
                    arrayList.add(new Note(str222, DURATION));
                    arrayList.add(new Note(str223, DURATION));
                    arrayList.add(new Note(str224, DURATION));
                } else if (TYPE_SCALE_NEOPOLITAN_MINOR.equals(str6)) {
                    String[] strArr29 = noteNames;
                    String str225 = strArr29[i3];
                    String str226 = strArr29[i3 + 1];
                    String str227 = strArr29[i3 + 3];
                    String str228 = strArr29[i3 + 5];
                    String str229 = strArr29[i3 + 7];
                    String str230 = strArr29[i3 + 8];
                    String str231 = strArr29[i3 + 11];
                    String str232 = strArr29[i3 + 12];
                    arrayList.add(new Note(str225, DURATION));
                    arrayList.add(new Note(str226, DURATION));
                    arrayList.add(new Note(str227, DURATION));
                    arrayList.add(new Note(str228, DURATION));
                    arrayList.add(new Note(str229, DURATION));
                    arrayList.add(new Note(str230, DURATION));
                    arrayList.add(new Note(str231, DURATION));
                    arrayList.add(new Note(str232, DURATION));
                } else if (TYPE_SCALE_PERSIAN.equals(str6)) {
                    String[] strArr30 = noteNames;
                    String str233 = strArr30[i3];
                    String str234 = strArr30[i3 + 1];
                    String str235 = strArr30[i3 + 4];
                    String str236 = strArr30[i3 + 5];
                    String str237 = strArr30[i3 + 6];
                    String str238 = strArr30[i3 + 8];
                    String str239 = strArr30[i3 + 11];
                    String str240 = strArr30[i3 + 12];
                    arrayList.add(new Note(str233, DURATION));
                    arrayList.add(new Note(str234, DURATION));
                    arrayList.add(new Note(str235, DURATION));
                    arrayList.add(new Note(str236, DURATION));
                    arrayList.add(new Note(str237, DURATION));
                    arrayList.add(new Note(str238, DURATION));
                    arrayList.add(new Note(str239, DURATION));
                    arrayList.add(new Note(str240, DURATION));
                } else if (TYPE_SCALE_PROMETHEUS.equals(str6)) {
                    String[] strArr31 = noteNames;
                    String str241 = strArr31[i3];
                    String str242 = strArr31[i3 + 2];
                    String str243 = strArr31[i3 + 4];
                    String str244 = strArr31[i3 + 6];
                    String str245 = strArr31[i3 + 9];
                    String str246 = strArr31[i3 + 10];
                    String str247 = strArr31[i3 + 12];
                    arrayList.add(new Note(str241, DURATION));
                    arrayList.add(new Note(str242, DURATION));
                    arrayList.add(new Note(str243, DURATION));
                    arrayList.add(new Note(str244, DURATION));
                    arrayList.add(new Note(str245, DURATION));
                    arrayList.add(new Note(str246, DURATION));
                    arrayList.add(new Note(str247, DURATION));
                } else if (TYPE_SCALE_TRITONE.equals(str6)) {
                    String[] strArr32 = noteNames;
                    String str248 = strArr32[i3];
                    String str249 = strArr32[i3 + 1];
                    String str250 = strArr32[i3 + 4];
                    String str251 = strArr32[i3 + 6];
                    String str252 = strArr32[i3 + 7];
                    String str253 = strArr32[i3 + 10];
                    String str254 = strArr32[i3 + 12];
                    arrayList.add(new Note(str248, DURATION));
                    arrayList.add(new Note(str249, DURATION));
                    arrayList.add(new Note(str250, DURATION));
                    arrayList.add(new Note(str251, DURATION));
                    arrayList.add(new Note(str252, DURATION));
                    arrayList.add(new Note(str253, DURATION));
                    arrayList.add(new Note(str254, DURATION));
                } else if (TYPE_SCALE_UKRANIAN_DORIAN.equals(str6)) {
                    String[] strArr33 = noteNames;
                    String str255 = strArr33[i3];
                    String str256 = strArr33[i3 + 2];
                    String str257 = strArr33[i3 + 3];
                    String str258 = strArr33[i3 + 6];
                    String str259 = strArr33[i3 + 7];
                    String str260 = strArr33[i3 + 9];
                    String str261 = strArr33[i3 + 10];
                    String str262 = strArr33[i3 + 12];
                    arrayList.add(new Note(str255, DURATION));
                    arrayList.add(new Note(str256, DURATION));
                    arrayList.add(new Note(str257, DURATION));
                    arrayList.add(new Note(str258, DURATION));
                    arrayList.add(new Note(str259, DURATION));
                    arrayList.add(new Note(str260, DURATION));
                    arrayList.add(new Note(str261, DURATION));
                    arrayList.add(new Note(str262, DURATION));
                } else if (TYPE_SCALE_WHOLE_TONE.equals(str6)) {
                    String[] strArr34 = noteNames;
                    String str263 = strArr34[i3];
                    String str264 = strArr34[i3 + 2];
                    String str265 = strArr34[i3 + 4];
                    String str266 = strArr34[i3 + 6];
                    String str267 = strArr34[i3 + 8];
                    String str268 = strArr34[i3 + 10];
                    String str269 = strArr34[i3 + 12];
                    arrayList.add(new Note(str263, DURATION));
                    arrayList.add(new Note(str264, DURATION));
                    arrayList.add(new Note(str265, DURATION));
                    arrayList.add(new Note(str266, DURATION));
                    arrayList.add(new Note(str267, DURATION));
                    arrayList.add(new Note(str268, DURATION));
                    arrayList.add(new Note(str269, DURATION));
                } else if (TYPE_SCALE_WHOLE_HALF_TONE.equals(str6)) {
                    String[] strArr35 = noteNames;
                    String str270 = strArr35[i3];
                    String str271 = strArr35[i3 + 2];
                    String str272 = strArr35[i3 + 3];
                    String str273 = strArr35[i3 + 5];
                    String str274 = strArr35[i3 + 6];
                    String str275 = strArr35[i3 + 8];
                    String str276 = strArr35[i3 + 9];
                    String str277 = strArr35[i3 + 11];
                    String str278 = strArr35[i3 + 12];
                    arrayList.add(new Note(str270, DURATION));
                    arrayList.add(new Note(str271, DURATION));
                    arrayList.add(new Note(str272, DURATION));
                    arrayList.add(new Note(str273, DURATION));
                    arrayList.add(new Note(str274, DURATION));
                    arrayList.add(new Note(str275, DURATION));
                    arrayList.add(new Note(str276, DURATION));
                    arrayList.add(new Note(str277, DURATION));
                    arrayList.add(new Note(str278, DURATION));
                } else if (c.equals(str6)) {
                    String[] strArr36 = noteNames;
                    String str279 = strArr36[i3];
                    String str280 = strArr36[i3 + 2];
                    String str281 = strArr36[i3 + 4];
                    String str282 = strArr36[i3 + 5];
                    String str283 = strArr36[i3 + 7];
                    String str284 = strArr36[i3 + 8];
                    String str285 = strArr36[i3 + 10];
                    String str286 = strArr36[i3 + 12];
                    arrayList.add(new Note(str279, DURATION));
                    arrayList.add(new Note(str280, DURATION));
                    arrayList.add(new Note(str281, DURATION));
                    arrayList.add(new Note(str282, DURATION));
                    arrayList.add(new Note(str283, DURATION));
                    arrayList.add(new Note(str284, DURATION));
                    arrayList.add(new Note(str285, DURATION));
                    arrayList.add(new Note(str286, DURATION));
                } else if (d.equals(str6)) {
                    String[] strArr37 = noteNames;
                    String str287 = strArr37[i3];
                    String str288 = strArr37[i3 + 2];
                    String str289 = strArr37[i3 + 3];
                    String str290 = strArr37[i3 + 5];
                    String str291 = strArr37[i3 + 7];
                    String str292 = strArr37[i3 + 8];
                    String str293 = strArr37[i3 + 10];
                    String str294 = strArr37[i3 + 12];
                    arrayList.add(new Note(str287, DURATION));
                    arrayList.add(new Note(str288, DURATION));
                    arrayList.add(new Note(str289, DURATION));
                    arrayList.add(new Note(str290, DURATION));
                    arrayList.add(new Note(str291, DURATION));
                    arrayList.add(new Note(str292, DURATION));
                    arrayList.add(new Note(str293, DURATION));
                    arrayList.add(new Note(str294, DURATION));
                } else if (e.equals(str6)) {
                    String[] strArr38 = noteNames;
                    String str295 = strArr38[i3];
                    String str296 = strArr38[i3 + 2];
                    String str297 = strArr38[i3 + 3];
                    String str298 = strArr38[i3 + 5];
                    String str299 = strArr38[i3 + 7];
                    String str300 = strArr38[i3 + 9];
                    String str301 = strArr38[i3 + 10];
                    String str302 = strArr38[i3 + 12];
                    arrayList.add(new Note(str295, DURATION));
                    arrayList.add(new Note(str296, DURATION));
                    arrayList.add(new Note(str297, DURATION));
                    arrayList.add(new Note(str298, DURATION));
                    arrayList.add(new Note(str299, DURATION));
                    arrayList.add(new Note(str300, DURATION));
                    arrayList.add(new Note(str301, DURATION));
                    arrayList.add(new Note(str302, DURATION));
                } else if (f.equals(str6)) {
                    String[] strArr39 = noteNames;
                    String str303 = strArr39[i3];
                    String str304 = strArr39[i3 + 1];
                    String str305 = strArr39[i3 + 4];
                    String str306 = strArr39[i3 + 5];
                    String str307 = strArr39[i3 + 7];
                    String str308 = strArr39[i3 + 8];
                    String str309 = strArr39[i3 + 11];
                    String str310 = strArr39[i3 + 12];
                    arrayList.add(new Note(str303, DURATION));
                    arrayList.add(new Note(str304, DURATION));
                    arrayList.add(new Note(str305, DURATION));
                    arrayList.add(new Note(str306, DURATION));
                    arrayList.add(new Note(str307, DURATION));
                    arrayList.add(new Note(str308, DURATION));
                    arrayList.add(new Note(str309, DURATION));
                    arrayList.add(new Note(str310, DURATION));
                } else if (g.equals(str6)) {
                    String[] strArr40 = noteNames;
                    String str311 = strArr40[i3];
                    String str312 = strArr40[i3 + 2];
                    String str313 = strArr40[i3 + 4];
                    String str314 = strArr40[i3 + 5];
                    String str315 = strArr40[i3 + 7];
                    String str316 = strArr40[i3 + 9];
                    String str317 = strArr40[i3 + 11];
                    String str318 = strArr40[i3 + 12];
                    arrayList.add(new Note(str311, DURATION));
                    arrayList.add(new Note(str312, DURATION));
                    arrayList.add(new Note(str313, DURATION));
                    arrayList.add(new Note(str314, DURATION));
                    arrayList.add(new Note(str315, DURATION));
                    arrayList.add(new Note(str316, DURATION));
                    arrayList.add(new Note(str317, DURATION));
                    arrayList.add(new Note(str318, DURATION));
                } else if (h.equals(str6)) {
                    String[] strArr41 = noteNames;
                    String str319 = strArr41[i3];
                    String str320 = strArr41[i3 + 1];
                    String str321 = strArr41[i3 + 3];
                    String str322 = strArr41[i3 + 5];
                    String str323 = strArr41[i3 + 6];
                    String str324 = strArr41[i3 + 8];
                    String str325 = strArr41[i3 + 10];
                    String str326 = strArr41[i3 + 12];
                    arrayList.add(new Note(str319, DURATION));
                    arrayList.add(new Note(str320, DURATION));
                    arrayList.add(new Note(str321, DURATION));
                    arrayList.add(new Note(str322, DURATION));
                    arrayList.add(new Note(str323, DURATION));
                    arrayList.add(new Note(str324, DURATION));
                    arrayList.add(new Note(str325, DURATION));
                    arrayList.add(new Note(str326, DURATION));
                } else if (i.equals(str6)) {
                    String[] strArr42 = noteNames;
                    String str327 = strArr42[i3];
                    String str328 = strArr42[i3 + 2];
                    String str329 = strArr42[i3 + 4];
                    String str330 = strArr42[i3 + 6];
                    String str331 = strArr42[i3 + 7];
                    String str332 = strArr42[i3 + 9];
                    String str333 = strArr42[i3 + 11];
                    String str334 = strArr42[i3 + 12];
                    arrayList.add(new Note(str327, DURATION));
                    arrayList.add(new Note(str328, DURATION));
                    arrayList.add(new Note(str329, DURATION));
                    arrayList.add(new Note(str330, DURATION));
                    arrayList.add(new Note(str331, DURATION));
                    arrayList.add(new Note(str332, DURATION));
                    arrayList.add(new Note(str333, DURATION));
                    arrayList.add(new Note(str334, DURATION));
                } else if (j.equals(str6)) {
                    String[] strArr43 = noteNames;
                    String str335 = strArr43[i3];
                    String str336 = strArr43[i3 + 2];
                    String str337 = strArr43[i3 + 4];
                    String str338 = strArr43[i3 + 5];
                    String str339 = strArr43[i3 + 7];
                    String str340 = strArr43[i3 + 9];
                    String str341 = strArr43[i3 + 10];
                    String str342 = strArr43[i3 + 12];
                    arrayList.add(new Note(str335, DURATION));
                    arrayList.add(new Note(str336, DURATION));
                    arrayList.add(new Note(str337, DURATION));
                    arrayList.add(new Note(str338, DURATION));
                    arrayList.add(new Note(str339, DURATION));
                    arrayList.add(new Note(str340, DURATION));
                    arrayList.add(new Note(str341, DURATION));
                    arrayList.add(new Note(str342, DURATION));
                } else if (k.equals(str6)) {
                    String[] strArr44 = noteNames;
                    String str343 = strArr44[i3];
                    String str344 = strArr44[i3 + 1];
                    String str345 = strArr44[i3 + 3];
                    String str346 = strArr44[i3 + 5];
                    String str347 = strArr44[i3 + 7];
                    String str348 = strArr44[i3 + 8];
                    String str349 = strArr44[i3 + 10];
                    String str350 = strArr44[i3 + 12];
                    arrayList.add(new Note(str343, DURATION));
                    arrayList.add(new Note(str344, DURATION));
                    arrayList.add(new Note(str345, DURATION));
                    arrayList.add(new Note(str346, DURATION));
                    arrayList.add(new Note(str347, DURATION));
                    arrayList.add(new Note(str348, DURATION));
                    arrayList.add(new Note(str349, DURATION));
                    arrayList.add(new Note(str350, DURATION));
                } else if (l.equals(str6)) {
                    String[] strArr45 = noteNames;
                    String str351 = strArr45[i3];
                    String str352 = strArr45[i3 + 1];
                    String str353 = strArr45[i3 + 4];
                    String str354 = strArr45[i3 + 5];
                    String str355 = strArr45[i3 + 7];
                    String str356 = strArr45[i3 + 8];
                    String str357 = strArr45[i3 + 10];
                    String str358 = strArr45[i3 + 12];
                    arrayList.add(new Note(str351, DURATION));
                    arrayList.add(new Note(str352, DURATION));
                    arrayList.add(new Note(str353, DURATION));
                    arrayList.add(new Note(str354, DURATION));
                    arrayList.add(new Note(str355, DURATION));
                    arrayList.add(new Note(str356, DURATION));
                    arrayList.add(new Note(str357, DURATION));
                    arrayList.add(new Note(str358, DURATION));
                } else {
                    if (TYPE_CHORD_MAJOR.equals(str6)) {
                        i4 = i2;
                        if (i4 == 1) {
                            String[] strArr46 = noteNames;
                            str4 = strArr46[i3 + 4];
                            str3 = strArr46[i3 + 7];
                            str5 = strArr46[i3 + 12];
                        } else if (i4 == 2) {
                            String[] strArr47 = noteNames;
                            str4 = strArr47[i3 + 7];
                            str3 = strArr47[i3 + 12];
                            str5 = strArr47[i3 + 16];
                        } else {
                            String[] strArr48 = noteNames;
                            str4 = strArr48[i3];
                            str3 = strArr48[i3 + 4];
                            str5 = strArr48[i3 + 7];
                        }
                        arrayList.add(new Note(str4, DURATION));
                        arrayList.add(new Note(str3, DURATION));
                        arrayList.add(new Note(str5, DURATION));
                    } else {
                        i4 = i2;
                        if (TYPE_CHORD_MINOR.equals(str6)) {
                            String[] strArr49 = noteNames;
                            String str359 = strArr49[i3];
                            String str360 = strArr49[i3 + 3];
                            String str361 = strArr49[i3 + 7];
                            arrayList.add(new Note(str359, DURATION));
                            arrayList.add(new Note(str360, DURATION));
                            arrayList.add(new Note(str361, DURATION));
                        } else if (TYPE_CHORD_AUGMENTED.equals(str6)) {
                            String[] strArr50 = noteNames;
                            String str362 = strArr50[i3];
                            String str363 = strArr50[i3 + 4];
                            String str364 = strArr50[i3 + 8];
                            arrayList.add(new Note(str362, DURATION));
                            arrayList.add(new Note(str363, DURATION));
                            arrayList.add(new Note(str364, DURATION));
                        } else if (TYPE_CHORD_DIMINISHED.equals(str6)) {
                            String[] strArr51 = noteNames;
                            String str365 = strArr51[i3];
                            String str366 = strArr51[i3 + 3];
                            String str367 = strArr51[i3 + 6];
                            arrayList.add(new Note(str365, DURATION));
                            arrayList.add(new Note(str366, DURATION));
                            arrayList.add(new Note(str367, DURATION));
                        } else if (TYPE_CHORD_SUSPENDED_4.equals(str6)) {
                            String[] strArr52 = noteNames;
                            String str368 = strArr52[i3];
                            String str369 = strArr52[i3 + 5];
                            String str370 = strArr52[i3 + 7];
                            arrayList.add(new Note(str368, DURATION));
                            arrayList.add(new Note(str369, DURATION));
                            arrayList.add(new Note(str370, DURATION));
                        } else if (TYPE_CHORD_SUSPENDED_2.equals(str6)) {
                            String[] strArr53 = noteNames;
                            String str371 = strArr53[i3];
                            String str372 = strArr53[i3 + 2];
                            String str373 = strArr53[i3 + 7];
                            arrayList.add(new Note(str371, DURATION));
                            arrayList.add(new Note(str372, DURATION));
                            arrayList.add(new Note(str373, DURATION));
                        } else if (TYPE_CHORD_MINOR_7.equals(str6)) {
                            String[] strArr54 = noteNames;
                            String str374 = strArr54[i3];
                            String str375 = strArr54[i3 + 3];
                            String str376 = strArr54[i3 + 7];
                            String str377 = strArr54[i3 + 10];
                            arrayList.add(new Note(str374, DURATION));
                            arrayList.add(new Note(str375, DURATION));
                            arrayList.add(new Note(str376, DURATION));
                            arrayList.add(new Note(str377, DURATION));
                        } else if (TYPE_CHORD_MAJOR_7.equals(str6)) {
                            String[] strArr55 = noteNames;
                            String str378 = strArr55[i3];
                            String str379 = strArr55[i3 + 4];
                            String str380 = strArr55[i3 + 7];
                            String str381 = strArr55[i3 + 11];
                            arrayList.add(new Note(str378, DURATION));
                            arrayList.add(new Note(str379, DURATION));
                            arrayList.add(new Note(str380, DURATION));
                            arrayList.add(new Note(str381, DURATION));
                        } else if (TYPE_CHORD_DOMINANT_7.equals(str6)) {
                            String[] strArr56 = noteNames;
                            String str382 = strArr56[i3];
                            String str383 = strArr56[i3 + 4];
                            String str384 = strArr56[i3 + 7];
                            String str385 = strArr56[i3 + 10];
                            arrayList.add(new Note(str382, DURATION));
                            arrayList.add(new Note(str383, DURATION));
                            arrayList.add(new Note(str384, DURATION));
                            arrayList.add(new Note(str385, DURATION));
                        } else if (TYPE_CHORD_DOMINANT_7_FLAT_5.equals(str6)) {
                            String[] strArr57 = noteNames;
                            String str386 = strArr57[i3];
                            String str387 = strArr57[i3 + 4];
                            String str388 = strArr57[i3 + 6];
                            String str389 = strArr57[i3 + 10];
                            arrayList.add(new Note(str386, DURATION));
                            arrayList.add(new Note(str387, DURATION));
                            arrayList.add(new Note(str388, DURATION));
                            arrayList.add(new Note(str389, DURATION));
                        } else if (TYPE_CHORD_DIMINISHED_7.equals(str6)) {
                            String[] strArr58 = noteNames;
                            String str390 = strArr58[i3];
                            String str391 = strArr58[i3 + 3];
                            String str392 = strArr58[i3 + 6];
                            String str393 = strArr58[i3 + 9];
                            arrayList.add(new Note(str390, DURATION));
                            arrayList.add(new Note(str391, DURATION));
                            arrayList.add(new Note(str392, DURATION));
                            arrayList.add(new Note(str393, DURATION));
                        } else if (TYPE_CHORD_HALF_DIMINISHED_7.equals(str6)) {
                            String[] strArr59 = noteNames;
                            String str394 = strArr59[i3];
                            String str395 = strArr59[i3 + 3];
                            String str396 = strArr59[i3 + 6];
                            String str397 = strArr59[i3 + 10];
                            arrayList.add(new Note(str394, DURATION));
                            arrayList.add(new Note(str395, DURATION));
                            arrayList.add(new Note(str396, DURATION));
                            arrayList.add(new Note(str397, DURATION));
                        } else if (TYPE_CHORD_DIMINISHED_MAJOR_7.equals(str6)) {
                            String[] strArr60 = noteNames;
                            String str398 = strArr60[i3];
                            String str399 = strArr60[i3 + 3];
                            String str400 = strArr60[i3 + 6];
                            String str401 = strArr60[i3 + 11];
                            arrayList.add(new Note(str398, DURATION));
                            arrayList.add(new Note(str399, DURATION));
                            arrayList.add(new Note(str400, DURATION));
                            arrayList.add(new Note(str401, DURATION));
                        } else if (TYPE_CHORD_MINOR_MAJOR_7.equals(str6)) {
                            String[] strArr61 = noteNames;
                            String str402 = strArr61[i3];
                            String str403 = strArr61[i3 + 3];
                            String str404 = strArr61[i3 + 7];
                            String str405 = strArr61[i3 + 11];
                            arrayList.add(new Note(str402, DURATION));
                            arrayList.add(new Note(str403, DURATION));
                            arrayList.add(new Note(str404, DURATION));
                            arrayList.add(new Note(str405, DURATION));
                        } else if (TYPE_CHORD_AUGMENTED_MAJOR_7.equals(str6)) {
                            String[] strArr62 = noteNames;
                            String str406 = strArr62[i3];
                            String str407 = strArr62[i3 + 4];
                            String str408 = strArr62[i3 + 8];
                            String str409 = strArr62[i3 + 11];
                            arrayList.add(new Note(str406, DURATION));
                            arrayList.add(new Note(str407, DURATION));
                            arrayList.add(new Note(str408, DURATION));
                            arrayList.add(new Note(str409, DURATION));
                        } else if (TYPE_CHORD_AUGMENTED_7.equals(str6)) {
                            String[] strArr63 = noteNames;
                            String str410 = strArr63[i3];
                            String str411 = strArr63[i3 + 4];
                            String str412 = strArr63[i3 + 8];
                            String str413 = strArr63[i3 + 10];
                            arrayList.add(new Note(str410, DURATION));
                            arrayList.add(new Note(str411, DURATION));
                            arrayList.add(new Note(str412, DURATION));
                            arrayList.add(new Note(str413, DURATION));
                        } else if (TYPE_CHORD_6.equals(str6)) {
                            String[] strArr64 = noteNames;
                            String str414 = strArr64[i3];
                            String str415 = strArr64[i3 + 4];
                            String str416 = strArr64[i3 + 7];
                            String str417 = strArr64[i3 + 9];
                            arrayList.add(new Note(str414, DURATION));
                            arrayList.add(new Note(str415, DURATION));
                            arrayList.add(new Note(str416, DURATION));
                            arrayList.add(new Note(str417, DURATION));
                        } else if (TYPE_CHORD_MINOR_6.equals(str6)) {
                            String[] strArr65 = noteNames;
                            String str418 = strArr65[i3];
                            String str419 = strArr65[i3 + 3];
                            String str420 = strArr65[i3 + 7];
                            String str421 = strArr65[i3 + 9];
                            arrayList.add(new Note(str418, DURATION));
                            arrayList.add(new Note(str419, DURATION));
                            arrayList.add(new Note(str420, DURATION));
                            arrayList.add(new Note(str421, DURATION));
                        } else if (TYPE_CHORD_7_6.equals(str6)) {
                            String[] strArr66 = noteNames;
                            String str422 = strArr66[i3];
                            String str423 = strArr66[i3 + 4];
                            String str424 = strArr66[i3 + 7];
                            String str425 = strArr66[i3 + 9];
                            String str426 = strArr66[i3 + 10];
                            arrayList.add(new Note(str422, DURATION));
                            arrayList.add(new Note(str423, DURATION));
                            arrayList.add(new Note(str424, DURATION));
                            arrayList.add(new Note(str425, DURATION));
                            arrayList.add(new Note(str426, DURATION));
                        } else if (TYPE_CHORD_6_ADD_9.equals(str6)) {
                            String[] strArr67 = noteNames;
                            String str427 = strArr67[i3];
                            String str428 = strArr67[i3 + 4];
                            String str429 = strArr67[i3 + 7];
                            String str430 = strArr67[i3 + 9];
                            String str431 = strArr67[i3 + 14];
                            arrayList.add(new Note(str427, DURATION));
                            arrayList.add(new Note(str428, DURATION));
                            arrayList.add(new Note(str429, DURATION));
                            arrayList.add(new Note(str430, DURATION));
                            arrayList.add(new Note(str431, DURATION));
                        } else if (TYPE_CHORD_9.equals(str6)) {
                            String[] strArr68 = noteNames;
                            String str432 = strArr68[i3];
                            String str433 = strArr68[i3 + 4];
                            String str434 = strArr68[i3 + 7];
                            String str435 = strArr68[i3 + 10];
                            String str436 = strArr68[i3 + 14];
                            arrayList.add(new Note(str432, DURATION));
                            arrayList.add(new Note(str433, DURATION));
                            arrayList.add(new Note(str434, DURATION));
                            arrayList.add(new Note(str435, DURATION));
                            arrayList.add(new Note(str436, DURATION));
                        } else if (TYPE_CHORD_MINOR_9.equals(str6)) {
                            String[] strArr69 = noteNames;
                            String str437 = strArr69[i3];
                            String str438 = strArr69[i3 + 3];
                            String str439 = strArr69[i3 + 7];
                            String str440 = strArr69[i3 + 10];
                            String str441 = strArr69[i3 + 14];
                            arrayList.add(new Note(str437, DURATION));
                            arrayList.add(new Note(str438, DURATION));
                            arrayList.add(new Note(str439, DURATION));
                            arrayList.add(new Note(str440, DURATION));
                            arrayList.add(new Note(str441, DURATION));
                        } else if (TYPE_CHORD_MAJOR_9.equals(str6)) {
                            String[] strArr70 = noteNames;
                            String str442 = strArr70[i3];
                            String str443 = strArr70[i3 + 4];
                            String str444 = strArr70[i3 + 7];
                            String str445 = strArr70[i3 + 11];
                            String str446 = strArr70[i3 + 14];
                            arrayList.add(new Note(str442, DURATION));
                            arrayList.add(new Note(str443, DURATION));
                            arrayList.add(new Note(str444, DURATION));
                            arrayList.add(new Note(str445, DURATION));
                            arrayList.add(new Note(str446, DURATION));
                        } else if (TYPE_CHORD_ADD_9.equals(str6)) {
                            String[] strArr71 = noteNames;
                            String str447 = strArr71[i3];
                            String str448 = strArr71[i3 + 4];
                            String str449 = strArr71[i3 + 7];
                            String str450 = strArr71[i3 + 14];
                            arrayList.add(new Note(str447, DURATION));
                            arrayList.add(new Note(str448, DURATION));
                            arrayList.add(new Note(str449, DURATION));
                            arrayList.add(new Note(str450, DURATION));
                        } else if (TYPE_CHORD_11.equals(str6)) {
                            String[] strArr72 = noteNames;
                            String str451 = strArr72[i3];
                            String str452 = strArr72[i3 + 4];
                            String str453 = strArr72[i3 + 7];
                            String str454 = strArr72[i3 + 10];
                            String str455 = strArr72[i3 + 14];
                            String str456 = strArr72[i3 + 17];
                            arrayList.add(new Note(str451, DURATION));
                            arrayList.add(new Note(str452, DURATION));
                            arrayList.add(new Note(str453, DURATION));
                            arrayList.add(new Note(str454, DURATION));
                            arrayList.add(new Note(str455, DURATION));
                            arrayList.add(new Note(str456, DURATION));
                        } else if (TYPE_CHORD_MINOR_11.equals(str6)) {
                            String[] strArr73 = noteNames;
                            String str457 = strArr73[i3];
                            String str458 = strArr73[i3 + 3];
                            String str459 = strArr73[i3 + 7];
                            String str460 = strArr73[i3 + 10];
                            String str461 = strArr73[i3 + 14];
                            String str462 = strArr73[i3 + 17];
                            arrayList.add(new Note(str457, DURATION));
                            arrayList.add(new Note(str458, DURATION));
                            arrayList.add(new Note(str459, DURATION));
                            arrayList.add(new Note(str460, DURATION));
                            arrayList.add(new Note(str461, DURATION));
                            arrayList.add(new Note(str462, DURATION));
                        } else if (TYPE_CHORD_13.equals(str6)) {
                            String[] strArr74 = noteNames;
                            String str463 = strArr74[i3];
                            String str464 = strArr74[i3 + 4];
                            String str465 = strArr74[i3 + 7];
                            String str466 = strArr74[i3 + 10];
                            String str467 = strArr74[i3 + 14];
                            String str468 = strArr74[i3 + 17];
                            String str469 = strArr74[i3 + 21];
                            arrayList.add(new Note(str463, DURATION));
                            arrayList.add(new Note(str464, DURATION));
                            arrayList.add(new Note(str465, DURATION));
                            arrayList.add(new Note(str466, DURATION));
                            arrayList.add(new Note(str467, DURATION));
                            arrayList.add(new Note(str468, DURATION));
                            arrayList.add(new Note(str469, DURATION));
                        }
                    }
                    if (z) {
                        return new Scale("", arrayList);
                    }
                    if (i4 == 1) {
                        sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" ");
                        sb.append(str6);
                        str6 = " 1st Inversion";
                    } else if (i4 == 2) {
                        sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" ");
                        sb.append(str6);
                        str6 = " 2nd Inversion";
                    } else {
                        sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" ");
                    }
                    sb.append(str6);
                    String sb2 = sb.toString();
                    scalesNamesList.add(sb2);
                    return new Scale(sb2, arrayList);
                }
            }
            i4 = i2;
            if (z) {
            }
        }
        i4 = i2;
      //  if (z) {
            return new Scale("", arrayList);
      //  }
      //  return null;
    }

    public List<Scale> getScales() {
        return scales;
    }

    public List<Scale> getScales(int i2, String str) {
        ArrayList arrayList = new ArrayList();
        switch (i2) {
            case 0:
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
                break;
            case 1:
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR, 0, false));
                break;
            case 2:
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_HARMONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_MELODIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR_PENTATONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_PENTATONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_BLUES, 0, false));
                break;
            case 3:
                arrayList.add(getScale(str, TYPE_SCALE_ACOUSTIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ALGERIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ALTERED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_AUGMENTED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_BEPOP_DOMINANT, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_CHROMATIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_DOUBLE_HARMONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ENIGMATIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_GYPSY, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HALF_DIMINISHED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HALF_WHOLE_TONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONICS, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HIRAJOSHI, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_GYPSY, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_INSEN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_IWATO, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_PERSIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_PROMETHEUS, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_TRITONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_UKRANIAN_DORIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_WHOLE_TONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_WHOLE_HALF_TONE, 0, false));
                break;
            case 4:
                arrayList.add(getScale(str, c, 0, false));
                arrayList.add(getScale(str, d, 0, false));
                arrayList.add(getScale(str, e, 0, false));
                arrayList.add(getScale(str, f, 0, false));
                arrayList.add(getScale(str, g, 0, false));
                arrayList.add(getScale(str, h, 0, false));
                arrayList.add(getScale(str, i, 0, false));
                arrayList.add(getScale(str, j, 0, false));
                arrayList.add(getScale(str, k, 0, false));
                arrayList.add(getScale(str, l, 0, false));
                break;
            case 5:
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_2, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_4, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_7_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_6_ADD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7_FLAT_5, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_HALF_DIMINISHED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_ADD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_11, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_11, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_13, 0, false));
                break;
            case 6:
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_HARMONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_MELODIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MAJOR_PENTATONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_MINOR_PENTATONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_BLUES, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ACOUSTIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ALGERIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ALTERED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_AUGMENTED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_BEPOP_DOMINANT, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_CHROMATIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_DOUBLE_HARMONIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_ENIGMATIC, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_GYPSY, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HALF_DIMINISHED, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HALF_WHOLE_TONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HARMONICS, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HIRAJOSHI, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_GYPSY, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_INSEN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_IWATO, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_PERSIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_PROMETHEUS, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_TRITONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_UKRANIAN_DORIAN, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_WHOLE_TONE, 0, false));
                arrayList.add(getScale(str, TYPE_SCALE_WHOLE_HALF_TONE, 0, false));
                arrayList.add(getScale(str, c, 0, false));
                arrayList.add(getScale(str, d, 0, false));
                arrayList.add(getScale(str, e, 0, false));
                arrayList.add(getScale(str, f, 0, false));
                arrayList.add(getScale(str, g, 0, false));
                arrayList.add(getScale(str, h, 0, false));
                arrayList.add(getScale(str, i, 0, false));
                arrayList.add(getScale(str, j, 0, false));
                arrayList.add(getScale(str, k, 0, false));
                arrayList.add(getScale(str, l, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_2, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_4, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_7_6, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_6_ADD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7_FLAT_5, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_HALF_DIMINISHED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_MAJOR_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_7, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_ADD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MAJOR_9, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_11, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_MINOR_11, 0, false));
                arrayList.add(getScale(str, TYPE_CHORD_13, 0, false));
                break;
            case 7:
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                defaultSharedPreferences.getInt("userDefinedScaleFlags_length", 71);
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags0", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags1", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MINOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags2", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MINOR_HARMONIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags3", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MINOR_MELODIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags4", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MAJOR_PENTATONIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags5", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_MINOR_PENTATONIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags6", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_BLUES, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags7", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_ACOUSTIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags8", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_ALGERIAN, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags9", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_ALTERED, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags10", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_AUGMENTED, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags11", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_BEPOP_DOMINANT, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags12", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_CHROMATIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags13", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_DOUBLE_HARMONIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags14", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_ENIGMATIC, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags15", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_GYPSY, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags16", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HALF_DIMINISHED, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags17", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HALF_WHOLE_TONE, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags18", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MAJOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags19", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HARMONIC_MINOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags20", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HARMONICS, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags21", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HIRAJOSHI, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags22", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_GYPSY, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags23", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_HUNGARIAN_MINOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags24", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_INSEN, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags25", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_IWATO, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags26", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MAJOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags27", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MINOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags28", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_PERSIAN, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags29", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_PROMETHEUS, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags30", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_TRITONE, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags31", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_UKRANIAN_DORIAN, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags32", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_WHOLE_TONE, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags33", false)) {
                    arrayList.add(getScale(str, TYPE_SCALE_WHOLE_HALF_TONE, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags34", false)) {
                    arrayList.add(getScale(str, c, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags35", false)) {
                    arrayList.add(getScale(str, d, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags36", false)) {
                    arrayList.add(getScale(str, e, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags37", false)) {
                    arrayList.add(getScale(str, f, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags38", false)) {
                    arrayList.add(getScale(str, g, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags39", false)) {
                    arrayList.add(getScale(str, h, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags40", false)) {
                    arrayList.add(getScale(str, i, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags41", false)) {
                    arrayList.add(getScale(str, j, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags42", false)) {
                    arrayList.add(getScale(str, k, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags43", false)) {
                    arrayList.add(getScale(str, l, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags44", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MAJOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags45", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags46", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags47", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags48", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_2, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags49", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_SUSPENDED_4, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags50", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_6, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags51", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR_6, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags52", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_7_6, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags53", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_6_ADD_9, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags54", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MAJOR_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags55", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags56", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags57", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR_MAJOR_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags58", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_DOMINANT_7_FLAT_5, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags59", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags60", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_HALF_DIMINISHED_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags61", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_DIMINISHED_MAJOR_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags62", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_MAJOR_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags63", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_AUGMENTED_7, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags64", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_ADD_9, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags65", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_9, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags66", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR_9, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags67", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MAJOR_9, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags68", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_11, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags69", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_MINOR_11, 0, false));
                }
                if (defaultSharedPreferences.getBoolean("userDefinedScaleFlags70", false)) {
                    arrayList.add(getScale(str, TYPE_CHORD_13, 0, false));
                    break;
                }
                break;
        }
        return arrayList;
    }

    public String[] getScalesNames(List<Scale> list) {
        String[] strArr = new String[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            strArr[i2] = list.get(i2).getTitle();
        }
        return strArr;
    }

    public String[] getScalesNamesArray() {
        List<String> list = scalesNamesList;
        return (String[]) list.toArray(new String[list.size()]);
    }

    public String getSolfegeLabel(String str, String str2, SoundManager soundManager, String str3) {
        Log.v("SCALES_MANAGER", "getSolfegeLabel scaleNoteName:" + str + " rootNote:" + str2 + " scaleTitle:" + str3);
        StringBuilder sb = new StringBuilder();
        sb.append("scaleNoteNameWithoutOctave:");
        sb.append(str.substring(0, str.length() - 1));
        Log.v("SCALES_MANAGER", sb.toString());
        int notePosition = soundManager.getNotePosition(new Note(Note.getNoteName(str), DURATION)) - soundManager.getNotePosition(new Note(Note.getNoteName(str2 + "3"), DURATION));
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
        return str.contains("Altered") || str.contains("Half Diminished") || str.contains("Istrian") || str.contains("Iwato") || str.contains("Locrian") || str.contains("Persian") || str.contains("Tritone") || str.contains(ChordManager.TYPE_DIMINISHED);
    }

    public boolean hasALoweredSecond(String str) {
        return str.contains("Altered") || str.contains("Double Harmonic") || str.contains("Enigmatic") || str.contains("Flamenco") || str.contains("Insen") || str.contains("Neapolitan Major") || str.contains("Neapolitan Minor") || str.contains("Persian") || str.contains("Phrygian") || str.contains("Tritone");
    }

    public boolean hasALoweredSeventh(String str) {
        return str.contains("Acoustic") || str.contains(ChordManager.TYPE_MINOR) || str.contains("Adonai") || str.contains("Aeolian") || str.contains("Altered") || str.contains("Blues") || str.contains("Dorian") || str.contains("Insen") || str.contains("Iwato") || str.contains("Locrian") || str.contains("Minor Pentatonic") || str.contains("Mixolydian") || str.contains("Prometheus") || str.contains("Tritone") || str.contains(ChordManager.TYPE_AUGMENTED);
    }

    public boolean hasALoweredSixth(String str) {
        return str.contains("Adonai") || str.contains(ChordManager.TYPE_MINOR) || str.contains("Altered") || str.contains("Double Harmonic") || str.contains("Flamenco") || str.contains("Gypsy") || str.contains("Half Diminished") || str.contains("Harmonic Major") || str.contains("Harmonic Minor") || str.contains("Hirajoshi") || str.contains("Hungarian") || str.contains("Locrian") || str.contains("Major Bepop") || str.contains("Neapolitan Minor") || str.contains("Persian");
    }

    public boolean hasALoweredThird(String str) {
        return str.contains(ChordManager.TYPE_MINOR) || str.contains("Aeolian") || str.contains("Algerian") || str.contains("Altered") || str.contains(ChordManager.TYPE_AUGMENTED) || str.contains("Blues") || str.contains("Dorian") || str.contains("Gypsy") || str.contains("Half Diminished") || str.contains("Hirajoshi") || str.contains("Locrian") || str.contains("Neapolitan") || str.contains("Phrygian") || str.contains("Ukrainian") || str.contains("Yo") || str.contains(ChordManager.TYPE_AUGMENTED);
    }

    public List<Scale> initScales(String str) {
        List<Scale> list = scales;
        list.removeAll(list);
        List<String> list2 = scalesNamesList;
        list2.removeAll(list2);
        scales.add(getScale(str, TYPE_SCALE_MAJOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_MINOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_MINOR_HARMONIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_MINOR_MELODIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_MAJOR_PENTATONIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_MINOR_PENTATONIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_BLUES, 0, false));
        scales.add(getScale(str, TYPE_SCALE_ACOUSTIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_ALGERIAN, 0, false));
        scales.add(getScale(str, TYPE_SCALE_ALTERED, 0, false));
        scales.add(getScale(str, TYPE_SCALE_AUGMENTED, 0, false));
        scales.add(getScale(str, TYPE_SCALE_BEPOP_DOMINANT, 0, false));
        scales.add(getScale(str, TYPE_SCALE_CHROMATIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_DOUBLE_HARMONIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_ENIGMATIC, 0, false));
        scales.add(getScale(str, TYPE_SCALE_GYPSY, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HALF_DIMINISHED, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HALF_WHOLE_TONE, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HARMONIC_MAJOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HARMONIC_MINOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HARMONICS, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HIRAJOSHI, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HUNGARIAN_GYPSY, 0, false));
        scales.add(getScale(str, TYPE_SCALE_HUNGARIAN_MINOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_INSEN, 0, false));
        scales.add(getScale(str, TYPE_SCALE_IWATO, 0, false));
        scales.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MAJOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_NEOPOLITAN_MINOR, 0, false));
        scales.add(getScale(str, TYPE_SCALE_PERSIAN, 0, false));
        scales.add(getScale(str, TYPE_SCALE_PROMETHEUS, 0, false));
        scales.add(getScale(str, TYPE_SCALE_TRITONE, 0, false));
        scales.add(getScale(str, TYPE_SCALE_UKRANIAN_DORIAN, 0, false));
        scales.add(getScale(str, TYPE_SCALE_WHOLE_TONE, 0, false));
        scales.add(getScale(str, TYPE_SCALE_WHOLE_HALF_TONE, 0, false));
        scales.add(getScale(str, c, 0, false));
        scales.add(getScale(str, d, 0, false));
        scales.add(getScale(str, e, 0, false));
        scales.add(getScale(str, f, 0, false));
        scales.add(getScale(str, g, 0, false));
        scales.add(getScale(str, h, 0, false));
        scales.add(getScale(str, i, 0, false));
        scales.add(getScale(str, j, 0, false));
        scales.add(getScale(str, k, 0, false));
        scales.add(getScale(str, l, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MAJOR, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR, 0, false));
        scales.add(getScale(str, TYPE_CHORD_AUGMENTED, 0, false));
        scales.add(getScale(str, TYPE_CHORD_DIMINISHED, 0, false));
        scales.add(getScale(str, TYPE_CHORD_SUSPENDED_2, 0, false));
        scales.add(getScale(str, TYPE_CHORD_SUSPENDED_4, 0, false));
        scales.add(getScale(str, TYPE_CHORD_6, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR_6, 0, false));
        scales.add(getScale(str, TYPE_CHORD_7_6, 0, false));
        scales.add(getScale(str, TYPE_CHORD_6_ADD_9, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MAJOR_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_DOMINANT_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR_MAJOR_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_DOMINANT_7_FLAT_5, 0, false));
        scales.add(getScale(str, TYPE_CHORD_DIMINISHED_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_HALF_DIMINISHED_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_DIMINISHED_MAJOR_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_AUGMENTED_MAJOR_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_AUGMENTED_7, 0, false));
        scales.add(getScale(str, TYPE_CHORD_ADD_9, 0, false));
        scales.add(getScale(str, TYPE_CHORD_9, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR_9, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MAJOR_9, 0, false));
        scales.add(getScale(str, TYPE_CHORD_11, 0, false));
        scales.add(getScale(str, TYPE_CHORD_MINOR_11, 0, false));
        scales.add(getScale(str, TYPE_CHORD_13, 0, false));
        Log.v("ScalesManager", "initScales finished with scalesSize:" + scales.size());
        return scales;
    }

    public String recogniseScale(TreeSet<Note> treeSet, boolean z) {
        String scaleNoteName = treeSet.first().getScaleNoteName(z ? "b " : "");
        List<Scale> initScales = initScales(scaleNoteName.substring(0, scaleNoteName.length() - 1));
        TreeSet treeSet2 = new TreeSet();
        Iterator<Note> it = treeSet.iterator();
        while (it.hasNext()) {
            String name = it.next().getName();
            treeSet2.add(name.substring(0, name.length() - 1));
        }
        for (Scale scale : initScales) {
            List<Note> notes = scale.getNotes();
            TreeSet treeSet3 = new TreeSet();
            for (Note note : notes) {
                String name2 = note.getName();
                treeSet3.add(name2.substring(0, name2.length() - 1));
            }
            if (treeSet3.containsAll(treeSet2) && treeSet3.size() == treeSet2.size()) {
                return scale.getTitle();
            }
        }
        return null;
    }
}
