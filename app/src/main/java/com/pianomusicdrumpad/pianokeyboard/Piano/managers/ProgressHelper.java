package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ProgressHelper {
    public static final int EX_CHORDS_GAME = 2;
    public static final int EX_SCALES_GAME = 1;
    public static final String KEY_PROGRESS_JSON_STRING = "Key_Progress_Json_String";
    public static final int TREND_DECLINING = 3;
    public static final int TREND_IMPROVING = 1;
    public static final int TREND_SAME = 2;

    public static String getColorFromPercentage(int i) {
        return i >= 95 ? "#10FF00" : (i >= 95 || i < 90) ? (i >= 90 || i < 85) ? (i >= 85 || i < 80) ? (i >= 80 || i < 75) ? (i >= 75 || i < 70) ? (i >= 70 || i < 65) ? (i >= 65 || i < 60) ? (i >= 60 || i < 55) ? (i >= 55 || i < 50) ? (i >= 50 || i < 45) ? (i >= 45 || i < 40) ? (i >= 40 || i < 35) ? (i >= 35 || i < 30) ? (i >= 30 || i < 25) ? (i >= 25 || i < 20) ? (i >= 20 || i < 15) ? (i >= 15 || i < 10) ? (i >= 10 || i < 5) ? (i >= 5 || i < 0) ? "#10FF00" : "#FF0000" : "#FF2000" : "#FF4000" : "#FF6000" : "#FF8000" : "#FF9000" : "#FFB000" : "#FFD000" : "#FFF000" : "#FFFF00" : "#E0FF00" : "#D0FF00" : "#C0FF00" : "#A0FF00" : "#80FF00" : "#65FF00" : "#55FF00" : "#40FF00" : "#20FF00";
    }

    public static String getExerciseText(int i) {
        return i != 1 ? i != 2 ? "" : "Chords Game" : "Scales Game";
    }

    static class C06881 implements Comparator<String> {
        C06881() {
        }

        public int compare(String str, String str2) {
            String str3;
            String str4;
            String str5;
            String[] split = str.split("[|]");
            String str6 = "";
            if (split != null) {
                str3 = split[0];
                str4 = split.length >= 1 ? split[1] : str6;
            } else {
                str4 = str6;
                str3 = str4;
            }
            String[] split2 = str2.split("[|]");
            if (split2 != null) {
                String str7 = split2[0];
                if (split2.length >= 1) {
                    str6 = split2[1];
                }
                str5 = str6;
                str6 = str7;
            } else {
                str5 = str6;
            }
            int parseInt = Integer.parseInt(str3);
            int parseInt2 = Integer.parseInt(str6);
            if (parseInt < parseInt2) {
                return -1;
            }
            if (parseInt > parseInt2) {
                return 1;
            }
            if (!str4.contains("Time Interval ") || !str5.contains("Time Interval ")) {
                return str.compareTo(str2);
            }
            String substring = str4.substring(14);
            String substring2 = str5.substring(14);
            int parseInt3 = Integer.parseInt(substring);
            int parseInt4 = Integer.parseInt(substring2);
            if (parseInt3 != parseInt4) {
                return parseInt3 < parseInt4 ? -1 : 1;
            }
            return 0;
        }
    }

    public static String getExerciseFromKey(String str) {
        String[] split = str.split("[|]");
        if (split != null) {
            return getExerciseText(Integer.parseInt(split[0]));
        }
        return null;
    }

    public static String getLevelFromKey(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 2) {
            return null;
        }
        return split[1];
    }

    public static int getNumberCorrectFromValue(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 2) {
            return 0;
        }
        return Integer.parseInt(split[0]);
    }

    public static String getPercentageFromValue(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 2) {
            return null;
        }
        return "" + ((int) ((Float.parseFloat(split[0]) * 100.0f) / Float.parseFloat(split[1]))) + "%";
    }

    public static int getPercentageIntFromValue(String str) {
        String[] split = str.split("[|]");
        return (int) ((split == null || split.length < 2) ? 0.0f : (Float.parseFloat(split[0]) * 100.0f) / Float.parseFloat(split[1]));
    }

    public static String getScoreFromValue(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 2) {
            return null;
        }
        return split[0] + "/" + split[1];
    }

    public static int getTotalNumberFromValue(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 2) {
            return 0;
        }
        return Integer.parseInt(split[1]);
    }

    public static int getTrendFromValue(String str) {
        String[] split = str.split("[|]");
        if (split == null || split.length < 3) {
            return 0;
        }
        return Integer.parseInt(split[2]);
    }

    public static Map<String, String> openProgressMapFromPrefs(Context context) {
        String string;
        TreeMap treeMap = new TreeMap(new C06881());
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences.contains(KEY_PROGRESS_JSON_STRING) && (string = defaultSharedPreferences.getString(KEY_PROGRESS_JSON_STRING, (String) null)) != null) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                try {
                    Iterator<String> keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        treeMap.put(next, jSONObject.getString(next));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return treeMap;
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return treeMap;
    }

    public static void saveProgressObjectViaJSON(Map<String, String> map, Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry next : map.entrySet()) {
                Log.v("saveProgress", "Key = " + ((String) next.getKey()) + ", Value = " + ((String) next.getValue()));
                jSONObject.put((String) next.getKey(), next.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jSONObject2 = jSONObject.toString();
        Log.v("Progress", "strProgressJson:" + jSONObject2);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(KEY_PROGRESS_JSON_STRING, jSONObject2);
        edit.apply();
    }
}
