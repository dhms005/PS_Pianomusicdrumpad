package com.pianomusicdrumpad.pianokeyboard.Piano.managers;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note;
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Tune;
import com.pianomusicdrumpad.pianokeyboard.R;

public class TuneManager {
    private static TuneManager instance;
    private static Map<Integer, Integer> tunes = new HashMap();
    private Context mContext;

    private TuneManager(Context context) {
        this.mContext = context;
        addTunes();
    }

    public static TuneManager getInstance(Context context) {
        if (instance == null) {
            instance = new TuneManager(context);
        }
        return instance;
    }

    public void addTunes() {
        Integer num = 1;
        tunes.put(num, Integer.valueOf(R.xml.arabic_saba));
        Integer valueOf = Integer.valueOf(num.intValue() + 1);
        tunes.put(valueOf, Integer.valueOf(R.xml.ashiqui));
        Integer valueOf2 = Integer.valueOf(valueOf.intValue() + 1);
        tunes.put(valueOf2, Integer.valueOf(R.xml.black_parade_mcr));
        Integer valueOf3 = Integer.valueOf(valueOf2.intValue() + 1);
        tunes.put(valueOf3, Integer.valueOf(R.xml.blues_race));
        Integer valueOf4 = Integer.valueOf(valueOf3.intValue() + 1);
        tunes.put(valueOf4, Integer.valueOf(R.xml.bunny_hop_by_stephanescu));
        Integer valueOf5 = Integer.valueOf(valueOf4.intValue() + 1);
        tunes.put(valueOf5, Integer.valueOf(R.xml.chopsticks_fast));
        Integer valueOf6 = Integer.valueOf(valueOf5.intValue() + 1);
        tunes.put(valueOf6, Integer.valueOf(R.xml.christmases));
        Integer valueOf7 = Integer.valueOf(valueOf6.intValue() + 1);
        tunes.put(valueOf7, Integer.valueOf(R.xml.darck_creation));
        Integer valueOf8 = Integer.valueOf(valueOf7.intValue() + 1);
        tunes.put(valueOf8, Integer.valueOf(R.xml.dhaat));
        Integer valueOf9 = Integer.valueOf(valueOf8.intValue() + 1);
        tunes.put(valueOf9, Integer.valueOf(R.xml.eleven_twelve));
        Integer valueOf10 = Integer.valueOf(valueOf9.intValue() + 1);
        tunes.put(valueOf10, Integer.valueOf(R.xml.everyday_is_beautiful));
        Integer valueOf11 = Integer.valueOf(valueOf10.intValue() + 1);
        tunes.put(valueOf11, Integer.valueOf(R.xml.for_the_love_of_god_steve_vai_intro));
        Integer valueOf12 = Integer.valueOf(valueOf11.intValue() + 1);
        tunes.put(valueOf12, Integer.valueOf(R.xml.frightful));
        Integer valueOf13 = Integer.valueOf(valueOf12.intValue() + 1);
        tunes.put(valueOf13, Integer.valueOf(R.xml.golden));
        Integer valueOf14 = Integer.valueOf(valueOf13.intValue() + 1);
        tunes.put(valueOf14, Integer.valueOf(R.xml.hindhy));
        Integer valueOf15 = Integer.valueOf(valueOf14.intValue() + 1);
        tunes.put(valueOf15, Integer.valueOf(R.xml.hwando));
        Integer valueOf16 = Integer.valueOf(valueOf15.intValue() + 1);
        tunes.put(valueOf16, Integer.valueOf(R.xml.i_am_the_doctor));
        Integer valueOf17 = Integer.valueOf(valueOf16.intValue() + 1);
        tunes.put(valueOf17, Integer.valueOf(R.xml.i_have_a_dream));
        Integer valueOf18 = Integer.valueOf(valueOf17.intValue() + 1);
        tunes.put(valueOf18, Integer.valueOf(R.xml.jeffs_jam));
        Integer valueOf19 = Integer.valueOf(valueOf18.intValue() + 1);
        tunes.put(valueOf19, Integer.valueOf(R.xml.jg));
        Integer valueOf20 = Integer.valueOf(valueOf19.intValue() + 1);
        tunes.put(valueOf20, Integer.valueOf(R.xml.kon_listen));
        Integer valueOf21 = Integer.valueOf(valueOf20.intValue() + 1);
        tunes.put(valueOf21, Integer.valueOf(R.xml.in_the_jungle));
        Integer valueOf22 = Integer.valueOf(valueOf21.intValue() + 1);
        tunes.put(valueOf22, Integer.valueOf(R.xml.little_mary_ann));
        Integer valueOf23 = Integer.valueOf(valueOf22.intValue() + 1);
        tunes.put(valueOf23, Integer.valueOf(R.xml.love_u));
        Integer valueOf24 = Integer.valueOf(valueOf23.intValue() + 1);
        tunes.put(valueOf24, Integer.valueOf(R.xml.mary_had_a_little_lamb));
        Integer valueOf25 = Integer.valueOf(valueOf24.intValue() + 1);
        tunes.put(valueOf25, Integer.valueOf(R.xml.melodia_one));
        Integer valueOf26 = Integer.valueOf(valueOf25.intValue() + 1);
        tunes.put(valueOf26, Integer.valueOf(R.xml.ombak_rindu));
        Integer valueOf27 = Integer.valueOf(valueOf26.intValue() + 1);
        tunes.put(valueOf27, Integer.valueOf(R.xml.para_ellas));
        Integer valueOf28 = Integer.valueOf(valueOf27.intValue() + 1);
        tunes.put(valueOf28, Integer.valueOf(R.xml.quan_sere_gran));
        Integer valueOf29 = Integer.valueOf(valueOf28.intValue() + 1);
        tunes.put(valueOf29, Integer.valueOf(R.xml.rastafari_chapel));
        Integer valueOf30 = Integer.valueOf(valueOf29.intValue() + 1);
        tunes.put(valueOf30, Integer.valueOf(R.xml.rubbish));
        Integer valueOf31 = Integer.valueOf(valueOf30.intValue() + 1);
        tunes.put(valueOf31, Integer.valueOf(R.xml.song_of_healing));
        Integer valueOf32 = Integer.valueOf(valueOf31.intValue() + 1);
        tunes.put(valueOf32, Integer.valueOf(R.xml.spanish_charge));
        Integer valueOf33 = Integer.valueOf(valueOf32.intValue() + 1);
        tunes.put(valueOf33, Integer.valueOf(R.xml.thank_u_lord));
        Integer valueOf34 = Integer.valueOf(valueOf33.intValue() + 1);
        tunes.put(valueOf34, Integer.valueOf(R.xml.tum_he_ho_ashique));
        Integer valueOf35 = Integer.valueOf(valueOf34.intValue() + 1);
        tunes.put(valueOf35, Integer.valueOf(R.xml.two_chainz_im_different));
        Integer valueOf36 = Integer.valueOf(valueOf35.intValue() + 1);
        tunes.put(valueOf36, Integer.valueOf(R.xml.wavey_rocker));
        Integer valueOf37 = Integer.valueOf(valueOf36.intValue() + 1);
        tunes.put(valueOf37, Integer.valueOf(R.xml.we_wish_you_a_merry_chrismas));
        Integer.valueOf(valueOf37.intValue() + 1);
    }

    public Map<Integer, Integer> getTunes() {
        return tunes;
    }

    public String[] populateTuneNames(Resources resources) {
        String[] strArr = new String[tunes.size()];
        int i = 0;
        while (i < tunes.size()) {
            int i2 = i + 1;
            strArr[i] = readXml(tunes.get(Integer.valueOf(i2)).intValue(), resources).getTitle();
            i = i2;
        }
        return strArr;
    }

    public Tune readXml(int i, Resources resources) {
        ArrayList arrayList = new ArrayList();
        XmlResourceParser xml = resources.getXml(i);
        String str = null;
        try {
            xml.next();
            String str2 = null;
            while (xml.getEventType() != 1) {
                try {
                    if (xml.getEventType() == 2) {
                        String name = xml.getName();
                        if ("tune".equals(name)) {
                            str2 = xml.getAttributeValue((String) null, "title");
                        } else if ("note".equals(name)) {
                            arrayList.add(new Note(xml.getAttributeValue((String) null, AppMeasurementSdk.ConditionalUserProperty.NAME), xml.getAttributeValue((String) null, "duration")));
                        }
                    } else if (xml.getEventType() != 3) {
                        xml.getEventType();
                    }
                    xml.next();
                } catch (Exception unused) {
                }
            }
            str = str2;
        } catch (Exception unused2) {
        }
        return new Tune(str, arrayList);
    }
}
