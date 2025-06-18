package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString

class SettingsActivity : Activity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    OnSeekBarChangeListener {
    private var autoscrollCheckBox: CheckBox? = null
    private var autoscrollFromPrefs = false
    var editor: SharedPreferences.Editor? = null
    private var highlightAllNotesCheckBox: CheckBox? = null
    private var highlightAllNotesFromPrefs = false
    private var keySizeFromPrefs: String? = null
    private var keySizePercentageTV: TextView? = null
    private var keySizeSeekBar: SeekBar? = null
    private var metronomeSettingsButton: Button? = null
    private var noteNamesChangeButton: Button? = null
    var noteNamesFromPrefs: String? = null
    var noteNamesTV: TextView? = null
    private var pressureCheckBox: CheckBox? = null
    private var pressureFromPrefs = false
    private var proButton: Button? = null
    private var resetButton: Button? = null
    private var resources: Resources? = null
    private var sharedPrefs: SharedPreferences? = null
    private var speedFromPrefs: String? = null
    private var speedPercentageTV: TextView? = null
    private var speedSeekBar: SeekBar? = null
    private var suggestButton: Button? = null
    private var vibrationChangeButton: Button? = null
    var vibrationFromPrefs: String? = null
    var vibrationTV: TextView? = null
    private var volumeFromPrefs: String? = null
    private var volumePercentageTV: TextView? = null
    private var volumeSeekBar: SeekBar? = null

    private val mediation: String? = null
    private val full_ad: Any? = null
    var adBannerAd: LinearLayout? = null


    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }


    //    class C06741 implements DialogInterface.OnClickListener {
    //        C06741() {
    //        }
    //
    //        public void onClick(DialogInterface dialogInterface, int i) {
    //            String str = MenuActivity.noteNamesValues[i];
    //            SettingsActivity.this.noteNamesTV.setText(str);
    //            SettingsActivity.this.noteNamesFromPrefs = str;
    //            SettingsActivity.this.editor.putString(MenuActivity.KEY_NOTE_NAMES, str);
    //            SettingsActivity.this.editor.commit();
    //            dialogInterface.dismiss();
    //        }
    //    }
    //    class C06752 implements DialogInterface.OnClickListener {
    //        C06752() {
    //        }
    //
    //        public void onClick(DialogInterface dialogInterface, int i) {
    //            String str = MenuActivity.hapticSettingValues[i];
    //            SettingsActivity.this.vibrationTV.setText(str);
    //            SettingsActivity.this.vibrationFromPrefs = str;
    //            SettingsActivity.this.editor.putString(MenuActivity.KEY_HAPTIC_FEEDBACK, str);
    //            SettingsActivity.this.editor.commit();
    //            dialogInterface.dismiss();
    //        }
    //    }
    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        putString(ConstantAd.AD_CHECK_RESUME, "1")
    }


    override fun onCheckedChanged(compoundButton: CompoundButton, z: Boolean) {
        if (compoundButton.id == R.id.highlightAllNotesCheckBox) {
            highlightAllNotesCheckBox!!.isChecked = z
            this.highlightAllNotesFromPrefs = z
            editor!!.putBoolean(MenuActivity.KEY_HIGHLIGHT_ALL_NOTES, z)
            editor!!.commit()
        } else if (compoundButton.id == R.id.autoscrollCheckBox) {
            autoscrollCheckBox!!.isChecked = z
            this.autoscrollFromPrefs = z
            editor!!.putBoolean(MenuActivity.KEY_AUTOSCROLL, z)
            editor!!.commit()
        } else if (compoundButton.id == R.id.pressureCheckBox) {
            pressureCheckBox!!.isChecked = z
            this.pressureFromPrefs = z
            editor!!.putBoolean(MenuActivity.KEY_PRESSURE, z)
            editor!!.commit()
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        } else if (view.id == R.id.resetButton) {
            noteNamesTV!!.text = MenuActivity.defaultNoteNames
            editor!!.putString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames)
            editor!!.commit()
            keySizeSeekBar!!.progress =
                ((MenuActivity.defaultKeysize.toFloat() * 100.0f) / 2.0f).toInt()
            volumeSeekBar!!.progress = MenuActivity.defaultVolume.toInt()
            speedSeekBar!!.progress =
                ((MenuActivity.defaultSpeed.toInt().toFloat()) / 2.0f).toInt()
            vibrationTV!!.text = MenuActivity.defaultHapticFeedback
            editor!!.putString(MenuActivity.KEY_HAPTIC_FEEDBACK, MenuActivity.defaultHapticFeedback)
            editor!!.commit()
            highlightAllNotesCheckBox!!.isChecked =
                MenuActivity.defaultHighlightAllNotes
            editor!!.putBoolean(
                MenuActivity.KEY_HIGHLIGHT_ALL_NOTES,
                MenuActivity.defaultHighlightAllNotes
            )
            autoscrollCheckBox!!.isChecked = MenuActivity.defaultAutoscroll
            editor!!.putBoolean(MenuActivity.KEY_AUTOSCROLL, MenuActivity.defaultAutoscroll)
            pressureCheckBox!!.isChecked = MenuActivity.defaultPressure
            editor!!.putBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure)
            editor!!.commit()
        } else if (view.id == R.id.noteNamesChangebutton) {
        } else {
            if (view.id == R.id.clickSettingsButton) {
                startActivity(Intent(this, ClickActivity::class.java))
            } else if (view.id != R.id.proButton) {
                view.id
            }
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        if (getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        setContentView(R.layout.settings)
        this.resources = getResources()
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        this.sharedPrefs = defaultSharedPreferences
        this.editor = defaultSharedPreferences.edit()
        val seekBar = findViewById<View>(R.id.keySizeSeekBar) as SeekBar
        this.keySizeSeekBar = seekBar
        seekBar.setOnSeekBarChangeListener(this)
        this.keySizePercentageTV = findViewById<View>(R.id.keySizePercentage) as TextView
        this.volumeSeekBar = findViewById<View>(R.id.volumeSeekBar) as SeekBar
        this.volumePercentageTV = findViewById<View>(R.id.volumePercentage) as TextView
        volumeSeekBar!!.setOnSeekBarChangeListener(this)
        this.speedSeekBar = findViewById<View>(R.id.speedSeekBar) as SeekBar
        this.speedPercentageTV = findViewById<View>(R.id.speedPercentage) as TextView
        speedSeekBar!!.setOnSeekBarChangeListener(this)
        val button = findViewById<View>(R.id.noteNamesChangebutton) as Button
        this.noteNamesChangeButton = button
        button.setOnClickListener(this)
        this.noteNamesTV = findViewById<View>(R.id.noteNamesTextView) as TextView
        val button2 = findViewById<View>(R.id.vibrationChangebutton) as Button
        this.vibrationChangeButton = button2
        button2.setOnClickListener(this)
        this.vibrationTV = findViewById<View>(R.id.vibrationTextView) as TextView
        val checkBox = findViewById<View>(R.id.highlightAllNotesCheckBox) as CheckBox
        this.highlightAllNotesCheckBox = checkBox
        checkBox.setOnCheckedChangeListener(this)
        val checkBox2 = findViewById<View>(R.id.autoscrollCheckBox) as CheckBox
        this.autoscrollCheckBox = checkBox2
        checkBox2.setOnCheckedChangeListener(this)
        val checkBox3 = findViewById<View>(R.id.pressureCheckBox) as CheckBox
        this.pressureCheckBox = checkBox3
        checkBox3.setOnCheckedChangeListener(this)
        val button3 = findViewById<View>(R.id.clickSettingsButton) as Button
        this.metronomeSettingsButton = button3
        button3.setOnClickListener(this)
        val button4 = findViewById<View>(R.id.resetButton) as Button
        this.resetButton = button4
        button4.setOnClickListener(this)
        val button5 = findViewById<View>(R.id.proButton) as Button
        this.proButton = button5
        button5.setOnClickListener(this)
        val button6 = findViewById<View>(R.id.suggestButton) as Button
        this.suggestButton = button6
        button6.setOnClickListener(this)
        findViewById<View>(R.id.back_arrow_view).setOnClickListener(this)
        if (sharedPrefs!!.contains(MenuActivity.KEY_KEYSIZE)) {
            val string =
                sharedPrefs!!.getString(MenuActivity.KEY_KEYSIZE, MenuActivity.defaultKeysize)
            this.keySizeFromPrefs = string
            keySizeSeekBar!!.progress =
                ((string!!.toFloat() * 100.0f) / 2.0f).toInt()
        } else {
            val str = MenuActivity.defaultKeysize
            this.keySizeFromPrefs = str
            keySizeSeekBar!!.progress = ((str.toFloat() * 100.0f) / 2.0f).toInt()
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_PLAYALONG_VOLUME)) {
            val string2 =
                sharedPrefs!!.getString(
                    MenuActivity.KEY_PLAYALONG_VOLUME,
                    MenuActivity.defaultVolume
                )
            this.volumeFromPrefs = string2
            volumeSeekBar!!.progress = string2!!.toInt()
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_PLAYALONG_SPEED)) {
            val string3 =
                sharedPrefs!!.getString(MenuActivity.KEY_PLAYALONG_SPEED, MenuActivity.defaultSpeed)
            this.speedFromPrefs = string3
            speedSeekBar!!.progress =
                ((string3!!.toInt().toFloat()) / 2.0f).toInt()
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_NOTE_NAMES)) {
            val string4 =
                sharedPrefs!!.getString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames)
            this.noteNamesFromPrefs = string4
            noteNamesTV!!.text = string4
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_HAPTIC_FEEDBACK)) {
            val string5 =
                sharedPrefs!!.getString(
                    MenuActivity.KEY_HAPTIC_FEEDBACK,
                    MenuActivity.defaultHapticFeedback
                )
            this.vibrationFromPrefs = string5
            vibrationTV!!.text = string5
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_HIGHLIGHT_ALL_NOTES)) {
            val z =
                sharedPrefs!!.getBoolean(
                    MenuActivity.KEY_HIGHLIGHT_ALL_NOTES,
                    MenuActivity.defaultHighlightAllNotes
                )
            this.highlightAllNotesFromPrefs = z
            highlightAllNotesCheckBox!!.isChecked = z
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_AUTOSCROLL)) {
            val z2 =
                sharedPrefs!!.getBoolean(
                    MenuActivity.KEY_AUTOSCROLL,
                    MenuActivity.defaultAutoscroll
                )
            this.autoscrollFromPrefs = z2
            autoscrollCheckBox!!.isChecked = z2
        }
        if (sharedPrefs!!.contains(MenuActivity.KEY_PRESSURE)) {
            val z3 =
                sharedPrefs!!.getBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure)
            this.pressureFromPrefs = z3
            pressureCheckBox!!.isChecked = z3
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
        val id = seekBar.id
        if (id == keySizeSeekBar!!.id) {
            val max = seekBar.max
            val f = ((i.toFloat()) * 1.0f) / (max.toFloat())
            var f2 = 2.0f * f
            if ((f2.toDouble()) < 0.3) {
                f2 = 0.3f
            }
            //Log.v("SettingsActivity", "SettingsActivity keySizeSeekBar seekBarId:" + id + " max:" + max + " progressRatio:" + f);
            val textView = this.keySizePercentageTV
            val sb = StringBuilder()
            sb.append("")
            sb.append((100.0f * f2).toInt())
            sb.append("%")
            textView!!.text = sb.toString()
            val editor2 = this.editor
            editor2!!.putString(MenuActivity.KEY_KEYSIZE, "" + f2)
            editor!!.commit()
            return
        }
        var i2 = 10
        if (id == volumeSeekBar!!.id) {
            val max2 = seekBar.max
            val progress = seekBar.progress
            if (progress >= 10) {
                i2 = progress
            }
            //Log.v("SettingsActivity", "SettingsActivity volumeSeekBar seekBarId:" + id + " max:" + max2 + " adjustedVolProgress:" + i2);
            val textView2 = this.volumePercentageTV
            val sb2 = StringBuilder()
            sb2.append("")
            sb2.append(i2)
            sb2.append("%")
            textView2!!.text = sb2.toString()
            val editor3 = this.editor
            editor3!!.putString(MenuActivity.KEY_PLAYALONG_VOLUME, "" + i2)
            editor!!.commit()
        } else if (id == speedSeekBar!!.id) {
            val max3 = seekBar.max
            val progress2 = seekBar.progress * 2
            if (progress2 >= 10) {
                i2 = progress2
            }
            //Log.v("SettingsActivity", "SettingsActivity speedSeekBar seekBarId:" + id + " max:" + max3 + " adjustedSpeedProgress:" + i2);
            val textView3 = this.speedPercentageTV
            val sb3 = StringBuilder()
            sb3.append("")
            sb3.append(i2)
            sb3.append("%")
            textView3!!.text = sb3.toString()
            val editor4 = this.editor
            editor4!!.putString(MenuActivity.KEY_PLAYALONG_SPEED, "" + i2)
            editor!!.commit()
        }
    }
}
