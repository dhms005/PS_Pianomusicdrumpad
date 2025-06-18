package com.pianomusicdrumpad.pianokeyboard.Piano.models;

import androidx.annotation.Keep;

import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ChordManager;
import com.pianomusicdrumpad.pianokeyboard.R;

@Keep
public class Note {
    public static final int DEFAULT_DURATION_MS = 500;
    private int durationMS = DEFAULT_DURATION_MS;
    private String name;
    private int rId;
    private int sound;

    public Note(String str, int i) {
        this.name = str;
        this.sound = i;
    }

    public Note(String str, int i, int i2, int i3) {
        this.name = str;
        this.sound = i;
        this.durationMS = i2;
        this.rId = i3;
    }

    public Note(String str, String str2) {
        int parseInt = Integer.parseInt(str2);
        if ("C3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_c;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_c;
        } else if ("C#3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_c_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_c_s;
        } else if ("D3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_d;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_d;
        } else if ("D#3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_d_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_d_s;
        } else if ("E3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_e;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_e;
        } else if ("F3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_f;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_f;
        } else if ("F#3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_f_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_f_s;
        } else if ("G3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_g;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_g;
        } else if ("G#3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_g_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_g_s;
        } else if ("A3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_a;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_a;
        } else if ("A#3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_a_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_a_s;
        } else if ("B3".equals(str)) {
            this.name = str;
            this.sound = R.raw.bottom_b;
            this.durationMS = parseInt;
            this.rId = R.id.bottom_b;
        } else if ("C4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_c;
            this.durationMS = parseInt;
            this.rId = R.id.middle_c;
        } else if ("C#4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_c_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.middle_c_s;
        } else if ("D4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_d;
            this.durationMS = parseInt;
            this.rId = R.id.middle_d;
        } else if ("D#4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_d_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.middle_d_s;
        } else if ("E4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_e;
            this.durationMS = parseInt;
            this.rId = R.id.middle_e;
        } else if ("F4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_f;
            this.durationMS = parseInt;
            this.rId = R.id.middle_f;
        } else if ("F#4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_f_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.middle_f_s;
        } else if ("G4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_g;
            this.durationMS = parseInt;
            this.rId = R.id.middle_g;
        } else if ("G#4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_g_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.middle_g_s;
        } else if ("A4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_a;
            this.durationMS = parseInt;
            this.rId = R.id.middle_a;
        } else if ("A#4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_a_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.middle_a_s;
        } else if ("B4".equals(str)) {
            this.name = str;
            this.sound = R.raw.middle_b;
            this.durationMS = parseInt;
            this.rId = R.id.middle_b;
        }
        if ("C5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_c;
            this.durationMS = parseInt;
            this.rId = R.id.high_c;
        } else if ("C#5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_c_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.high_c_s;
        } else if ("D5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_d;
            this.durationMS = parseInt;
            this.rId = R.id.high_d;
        } else if ("D#5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_d_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.high_d_s;
        } else if ("E5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_e;
            this.durationMS = parseInt;
            this.rId = R.id.high_e;
        } else if ("F5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_f;
            this.durationMS = parseInt;
            this.rId = R.id.high_f;
        } else if ("F#5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_f_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.high_f_s;
        } else if ("G5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_g;
            this.durationMS = parseInt;
            this.rId = R.id.high_g;
        } else if ("G#5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_g_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.high_g_s;
        } else if ("A5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_a;
            this.durationMS = parseInt;
            this.rId = R.id.high_a;
        } else if ("A#5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_a_sharp;
            this.durationMS = parseInt;
            this.rId = R.id.high_a_s;
        } else if ("B5".equals(str)) {
            this.name = str;
            this.sound = R.raw.high_b;
            this.durationMS = parseInt;
            this.rId = R.id.high_b;
        } else if ("C6".equals(str)) {
            this.name = str;
            this.sound = R.raw.double_high_c;
            this.durationMS = parseInt;
            this.rId = R.id.double_high_c;
        }
    }

    public static String getNoteName(String str) {
        StringBuilder sb;
        String str2;
        if (!str.contains("b")) {
            return str;
        }
        String str3 = "2";
        if (!str.contains(str3)) {
            if (str.contains("3")) {
                str3 = "3";
            } else if (str.contains("4")) {
                str3 = "4";
            } else if (str.contains("5")) {
                str3 = "5";
            } else if (str.contains(ChordManager.TYPE_6)) {
                str3 = ChordManager.TYPE_6;
            }
        }
        if (str.contains("Db")) {
            sb = new StringBuilder();
            str2 = "C#";
        } else if (str.contains("Eb")) {
            sb = new StringBuilder();
            str2 = "D#";
        } else if (str.contains("Gb")) {
            sb = new StringBuilder();
            str2 = "F#";
        } else if (str.contains("Ab")) {
            sb = new StringBuilder();
            str2 = "G#";
        } else if (!str.contains("Bb")) {
            return "C4";
        } else {
            new StringBuilder();
            return "C4";
        }
        sb.append(str2);
        sb.append(str3);
        return sb.toString();
    }

    public static String getSoundName(String str) {
        String substring = str.substring(str.length() - 1, str.length());
        str.substring(0, str.length() - 1);
        String str2 = "A";
        if (str.contains("c")) {
            str2 = "C";
        } else if (str.contains("C")) {
            str2 = "C#";
        } else if (str.contains("d")) {
            str2 = "D";
        } else if (str.contains("D")) {
            str2 = "D#";
        } else if (str.contains("e")) {
            str2 = "E";
        } else if (str.contains("f")) {
            str2 = "F";
        } else if (str.contains("F")) {
            str2 = "F#";
        } else if (str.contains("g")) {
            str2 = "G";
        } else if (str.contains("G")) {
            str2 = "G#";
        } else if (!str.contains("a")) {
            str2 = str.contains(str2) ? "A#" : str.contains("b") ? "B" : "";
        }
        return str2 + substring;
    }

    public static boolean isAFlatChord(String str) {
        return str.contains("b ") || str.contains("F Major") || str.contains("F Minor") || str.contains("D Minor") || str.contains("G Minor") || str.contains("C Minor") || str.contains("C Diminished") || (str.contains("C") && str.contains(ChordManager.TYPE_DOMINANT_7));
    }

    public static boolean isAFlatScale(String str) {
        return str.contains("b ") || str.contains("F Major") || str.contains("F Minor") || str.contains("D Minor") || str.contains("G Minor") || str.contains("C Minor") || str.contains("C Adonai Malakh") || str.contains("C Aeolian") || str.contains("C Altered") || str.contains("C Bepop Dominant") || str.contains("C Dorian") || str.contains("C Double Harmonic") || str.contains("C Flamenco") || str.contains("C Half Diminished") || str.contains("C Harmonic Major") || str.contains("C Harmonic Minor") || str.contains("C Insen") || str.contains("C Iwato") || str.contains("C Locrian") || str.contains("C Mixolydian") || str.contains("C Neopolitan") || str.contains("C Persian") || str.contains("C Phrygian");
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof Note) && this.sound == ((Note) obj).getSound());
    }

    public String getChordNoteName(boolean z) {
        StringBuilder sb;
        String str;
        if (!z) {
            return this.name;
        }
        String str2 = "2";
        if (!this.name.contains(str2)) {
            if (this.name.contains("3")) {
                str2 = "3";
            } else if (this.name.contains("4")) {
                str2 = "4";
            } else if (this.name.contains("5")) {
                str2 = "5";
            } else if (this.name.contains(ChordManager.TYPE_6)) {
                str2 = ChordManager.TYPE_6;
            }
        }
        if (this.name.contains("C#")) {
            sb = new StringBuilder();
            str = "Db";
        } else if (this.name.contains("D#")) {
            sb = new StringBuilder();
            str = "Eb";
        } else if (this.name.contains("F#")) {
            sb = new StringBuilder();
            str = "Gb";
        } else if (this.name.contains("G#")) {
            sb = new StringBuilder();
            str = "Ab";
        } else if (!this.name.contains("A#")) {
            return this.name;
        } else {
            sb = new StringBuilder();
            str = "Bb";
        }
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public int getDurationMS() {
        return this.durationMS;
    }

    public String getName() {
        return this.name;
    }

    public int getRId() {
        return this.rId;
    }

    public String getScaleNoteName(String str) {
        StringBuilder sb;
        String str2;
        if (!str.contains("b ") && !str.contains("F Major") && !str.contains("D Minor") && !str.contains("G Minor") && !str.contains("C Minor") && !str.contains("F Minor")) {
            return this.name;
        }
        String str3 = "2";
        if (!this.name.contains(str3)) {
            if (this.name.contains("3")) {
                str3 = "3";
            } else if (this.name.contains("4")) {
                str3 = "4";
            } else if (this.name.contains("5")) {
                str3 = "5";
            } else if (this.name.contains(ChordManager.TYPE_6)) {
                str3 = ChordManager.TYPE_6;
            }
        }
        if (this.name.contains("C#")) {
            sb = new StringBuilder();
            str2 = "Db";
        } else if (this.name.contains("D#")) {
            sb = new StringBuilder();
            str2 = "Eb";
        } else if (this.name.contains("F#")) {
            sb = new StringBuilder();
            str2 = "Gb";
        } else if (this.name.contains("G#")) {
            sb = new StringBuilder();
            str2 = "Ab";
        } else if (!this.name.contains("A#")) {
            return this.name;
        } else {
            sb = new StringBuilder();
            str2 = "Bb";
        }
        sb.append(str2);
        sb.append(str3);
        return sb.toString();
    }

    public int getSound() {
        return this.sound;
    }

    public String getTabName() {
        String str = this.name;
        return str.contains("#") ? str.replace("#", "") : str.toLowerCase();
    }

    public int hashCode() {
        return this.sound;
    }

    public void setDurationMS(int i) {
        this.durationMS = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setRId(int i) {
        this.rId = i;
    }

    public void setSound(int i) {
        this.sound = i;
    }

    public String toString() {
        return this.name;
    }
}
