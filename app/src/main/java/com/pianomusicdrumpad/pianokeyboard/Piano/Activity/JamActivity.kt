package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.ClickActivity
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ChordManager
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ScalesManager
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Arrays
import java.util.Collections
import java.util.GregorianCalendar

class JamActivity : Activity(), View.OnClickListener, OnLongClickListener,
    OnTouchListener, OnSeekBarChangeListener {
    private var chordButtonFive: Button? = null
    private var chordButtonFour: Button? = null
    private var chordButtonOne: Button? = null
    private var chordButtonSeven: Button? = null
    private var chordButtonSix: Button? = null
    private var chordButtonThree: Button? = null
    private var chordButtonTwo: Button? = null
    var content: String? = null
    var context: Context? = null
    private var firstPointerRawX = 0.0f
    private val focusedNoteInScaleSet: MutableSet<Note> = HashSet<Note>()
    private var isSongLoadedFromFile = false
    var lastFinger1Button: View? = null
    var lastFinger2Button: View? = null
    var lastFinger3Button: View? = null
    var lastFinger4Button: View? = null
    var lastFinger5Button: View? = null
    private var lastPlayedUserNote: Note? = null
    var mHapticManager: HapticManager? = null
    private var mLastTouchTime: Long = 0
    private var note2: Note? = null
    private var notesInRecording = 0
    private var notesLayout: RelativeLayout? = null
    var playStopButton: ImageView? = null
    private var recordStopButton: ImageView? = null
    private var recordingNotes = ArrayList<Note>()
    private val savedAndOpenedNotes: List<Note> = ArrayList<Note>()
    var scalesManager: ScalesManager? = null
    private var seekBar: SeekBar? = null
    private var setNoteOctaveIndifferent: MutableSet<String> = HashSet<String>()
    lateinit var soundManager: SoundManager
    var statusTextView: TextView? = null
    private var timeMSLastPlayedUserNote: Long = 0

    override fun onStartTrackingTouch(seekBar2: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar2: SeekBar) {
    }

    internal inner class StartThread : Thread() {
        internal inner class UIThread : Runnable {
            override fun run() {
                playStopButton!!.setImageDrawable(playingOff)
            }
        }

        override fun run() {
            if (isMp3Loaded) {
                this@JamActivity.playMp3()
                return
            }
            this@JamActivity.playTune()
            isPlaying = false
            isStopDesired = false
            this@JamActivity.runOnUiThread(UIThread())
        }
    }


    inner class playTuneRunnable internal constructor() : Runnable {
        override fun run() {
            Toast.makeText(
                this@JamActivity.context,
                "There are no notes in this recording.",
                Toast.LENGTH_SHORT
            )
                .show()
            this@JamActivity.setStatusText("Piano Jam Free")
        }
    }


    inner class playTuneRunnable2 internal constructor() : Runnable {
        override fun run() {
            this@JamActivity.scrollToTag(iTagFirstNote)
        }
    }


    inner class openTuneDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            throw UnsupportedOperationException("Method not decompiled: com.veitch.themelodymaster.psajf.ui.activities.JamActivity.6.onClick(android.content.DialogInterface, int):void")
        }
    }


    inner class setKeyDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            rootNotesIdx = i
            dialogInterface.dismiss()
            rootNote = rootNoteValues[rootNotesIdx]
            scalesManager!!.initScales(rootNote)
            scaleNames = scalesManager!!.scalesNamesArray
            this@JamActivity.highlightToScale()
            this@JamActivity.cleanAllChordButtons()
            this@JamActivity.populateChordButtons()
            //Log.v("themelodymaster", " Google Analytics Event rootNote:" + JamActivity.rootNote);
            val edit = sharedPrefs!!.edit()
            edit.putString(MenuActivity.KEY_ROOT_NOTE, rootNote)
            edit.apply()
        }
    }


    inner class ScaleToFocusDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            scaleNamesIdx = i
            dialogInterface.dismiss()
            this@JamActivity.highlightToScale()
            this@JamActivity.cleanAllChordButtons()
            this@JamActivity.populateChordButtons()
            //Log.v("themelodymaster", " Google Analytics Event scaleTitle:" + JamActivity.scaleTitle);
        }
    }


    inner class SaveTuneDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            openSaveIdx = i
            dialogInterface.dismiss()
            val str = openSaveValues[openSaveIdx]
            //Log.v("themelodymaster", " Google Analytics Event chosenFunction:" + str);
            if (OPEN_RECORDING == str) {
                try {
                    this@JamActivity.openTune()
                } catch (th: Throwable) {
                    th.printStackTrace()
                }
            } else if (SAVE_RECORDING == str) {
                this@JamActivity.saveTune()
            } else if (OPEN_MUSIC == str) {
                this@JamActivity.openMusic()
            }
        }
    }

    private fun addLabel(
        imageButton: ImageButton,
        imageButton2: ImageButton?,
        str: String,
        f: Float,
        z: Boolean,
        i: Int,
        i2: Int,
        i3: Int,
        i4: Int
    ) {
        var str = str
        if (setNoteOctaveIndifferent.contains(str.replace("[0-9]".toRegex(), ""))) {
            if ("SOLFEGE" == noteNamesType) {
                str = scalesManager!!.getSolfegeLabel(
                    str, rootNote,
                    this.soundManager, scaleTitle
                )
            }
            //Log.v("themelodymaster", "addLabel in set label:" + str);
            val textView = TextView(this)
            textView.text = str
            textView.textSize = f
            textView.typeface = Typeface.MONOSPACE
            if (z) {
                textView.setTextColor(-12303292)
            } else {
                textView.setTextColor(-1)
            }
            val layoutParams = RelativeLayout.LayoutParams(-2, -2)
            layoutParams.setMargins(i, i2, i3, i4)
            layoutParams.addRule(8, imageButton.id)
            if (imageButton2 != null) {
                layoutParams.addRule(1, imageButton2.id)
            }
            textView.layoutParams = layoutParams
            notesLayout!!.addView(textView)
            keyLabels!!.add(textView)
        }
    }

    private fun bringInDefaultPrefs() {
        isRecording = false
        isPlaying = false
        isStopDesired = false
        scaleFactor = sharedPrefs!!.getString(
            MenuActivity.KEY_KEYSIZE,
            MenuActivity.defaultKeysize
        )!!.toFloat()
        noteNamesType =
            sharedPrefs!!.getString(MenuActivity.KEY_NOTE_NAMES, MenuActivity.defaultNoteNames)!!
        playAlongVolume = sharedPrefs!!.getString(
            MenuActivity.KEY_PLAYALONG_VOLUME,
            MenuActivity.defaultVolume
        )!!.toInt()
        playAlongSpeed = sharedPrefs!!.getString(
            MenuActivity.KEY_PLAYALONG_SPEED,
            MenuActivity.defaultSpeed
        )!!.toInt()
        isHighlightAllNotesOn = sharedPrefs!!.getBoolean(
            MenuActivity.KEY_HIGHLIGHT_ALL_NOTES,
            MenuActivity.defaultHighlightAllNotes
        )
        isAutoscrollOn =
            sharedPrefs!!.getBoolean(MenuActivity.KEY_AUTOSCROLL, MenuActivity.defaultAutoscroll)
        isPressureOn =
            sharedPrefs!!.getBoolean(MenuActivity.KEY_PRESSURE, MenuActivity.defaultPressure)
        hapticSetting = sharedPrefs!!.getString(
            MenuActivity.KEY_HAPTIC_FEEDBACK,
            MenuActivity.defaultHapticFeedback
        )!!
        rootNote = sharedPrefs!!.getString(MenuActivity.KEY_ROOT_NOTE, MenuActivity.defaultRootNote)
        if (Arrays.asList(*rootNoteValues).contains(rootNote)) {
            rootNotesIdx = Arrays.asList(*rootNoteValues).indexOf(rootNote)
        } else {
            rootNote = "C"
            rootNotesIdx = 0
        }
        setKeyboardLayout(scaleFactor)
        scalesManager!!.initScales(rootNote)
        scaleNames = scalesManager!!.scalesNamesArray
        if ((scaleFactor.toDouble()) <= 0.5) {
            scrollToTag(1)
        } else {
            scrollToTag(iTagFirstNote)
        }
        highlightToScale()
        cleanAllChordButtons()
    }

    fun cleanAllChordButtons() {
        findViewById<View>(R.id.chord_button_one).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_two).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_three).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_four).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_six).setBackgroundResource(R.drawable.background_button_normal)
        findViewById<View>(R.id.chord_button_seven).setBackgroundResource(R.drawable.background_button_normal)
    }

    private fun cleanAllNotes(set: Set<Note>) {
        //Log.v("themelodymaster", "Unhighlighting all notes in scale:" + scaleTitle);
        for (note in set) {
            (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapClean(note))
        }
    }

    private fun cleanNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapClean(note))
    }

    fun cleanNotesInScale() {
        //Log.v("themelodymaster", "Unhighlighting all notes in scale:" + scaleTitle);
        for (note in this.focusedNoteInScaleSet) {
            (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapClean(note))
        }
        focusedNoteInScaleSet.clear()
    }

    private fun enterCompetition(str: String) {
        //Log.v("themelodymaster", "enterCompetition called filename:" + str);
        if (hasSubmittedMelody) {
            Toast.makeText(
                this,
                "You have already submitted or attempted to submit an entry. Another submission will replace the previous one.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val inflate = View.inflate(this, R.layout.comp_submit_dialog, null)
        (inflate.findViewById<View>(R.id.comp_enter_text) as TextView).text =
            """
            Submit melody '$str' to win $200.
            
            Terms and Conditions.
            $termsAndConditions
            """.trimIndent()
        val create = AlertDialog.Builder(this).setTitle("Best Melody Competition").setView(inflate)
            .setPositiveButton(
                "Submit"
            ) { dialogInterface, i ->


                /* class com.pianomusicdrumpad.pianokeyboard.Activity.JamActivity.AnonymousClass2 */
                var charSequence: CharSequence?
                var str = ""
                var str2: String
                var charSequence2: CharSequence?
                var e: Exception?
                //Log.v("themelodymaster", "Enter competition - Save selected.");
                hasSubmittedMelody = true
                //                //Log.v("themelodymaster", "Enter competition - state:" + Environment.getExternalStorageState());
                //                //Log.v("themelodymaster", "Saving to external storage fileName:" + str + " content:" + JamActivity.this.content);
                try {
                    charSequence2 = "Choose Email Provider to Submit Entry"
                    try {
                    } catch (e2: Exception) {
                        e = e2
                        str2 = "text/plain"
                        str = KEY_SUBMITTED_MELODY
                        //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                        val fromFile = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent = Intent("android.intent.action.SEND")
                        intent.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Best Melody Competition"
                        )
                        intent.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                        )
                        intent.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("competition@TheMelodyMaster.com")
                        )
                        intent.putExtra("android.intent.extra.STREAM", fromFile)
                        intent.setType(str2)
                        charSequence = charSequence2
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent,
                                charSequence
                            )
                        )
                        hasSubmittedMelody = true
                        val edit = sharedPrefs!!.edit()
                        edit.putBoolean(str, hasSubmittedMelody)
                        edit.apply()
                        val fromFile2 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent2 = Intent("android.intent.action.SEND")
                        intent2.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Best Melody Competition"
                        )
                        intent2.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                        )
                        intent2.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("competition@TheMelodyMaster.com")
                        )
                        intent2.putExtra("android.intent.extra.STREAM", fromFile2)
                        intent2.setType(str2)
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent2,
                                charSequence
                            )
                        )
                        hasSubmittedMelody = true
                        val edit2 = sharedPrefs!!.edit()
                        edit2.putBoolean(str, hasSubmittedMelody)
                        edit2.apply()
                    }
                    try {
                        val fileOutputStream = FileOutputStream(
                            File(
                                Environment.getExternalStorageDirectory().toString(), str
                            )
                        )
                        try {
                            fileOutputStream.write(content!!.toByteArray())
                            fileOutputStream.flush()
                            fileOutputStream.close()
                            str = KEY_SUBMITTED_MELODY
                            str2 = "text/plain"
                        } catch (e3: Exception) {
                            //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e3.getMessage());
                            val fromFile3 = Uri.fromFile(
                                File(
                                    Environment.getExternalStorageDirectory(),
                                    "themelodymaster"
                                )
                            )
                            val intent3 = Intent("android.intent.action.SEND")
                            intent3.putExtra(
                                "android.intent.extra.SUBJECT",
                                "Piano Scales And Jam Free Best Melody Competition"
                            )
                            intent3.putExtra(
                                "android.intent.extra.TEXT",
                                "I'm submitting '" + "themelodymaster" + "' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                            )
                            intent3.putExtra(
                                "android.intent.extra.EMAIL",
                                arrayOf("competition@TheMelodyMaster.com")
                            )
                            intent3.putExtra("android.intent.extra.STREAM", fromFile3)
                            str2 = "text/plain"
                            try {
                                intent3.setType(str2)
                            } catch (e4: Exception) {
                                e = e4
                                str = KEY_SUBMITTED_MELODY
                                //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                val fromFile4 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent4 = Intent("android.intent.action.SEND")
                                intent4.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Best Melody Competition"
                                )
                                intent4.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                )
                                intent4.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("competition@TheMelodyMaster.com")
                                )
                                intent4.putExtra("android.intent.extra.STREAM", fromFile4)
                                intent4.setType(str2)
                                charSequence = charSequence2
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent4,
                                        charSequence
                                    )
                                )
                                hasSubmittedMelody = true
                                val edit3 = sharedPrefs!!.edit()
                                edit3.putBoolean(str, hasSubmittedMelody)
                                edit3.apply()
                                val fromFile22 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent22 = Intent("android.intent.action.SEND")
                                intent22.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Best Melody Competition"
                                )
                                intent22.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                )
                                intent22.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("competition@TheMelodyMaster.com")
                                )
                                intent22.putExtra("android.intent.extra.STREAM", fromFile22)
                                intent22.setType(str2)
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent22,
                                        charSequence
                                    )
                                )
                                hasSubmittedMelody = true
                                val edit22 = sharedPrefs!!.edit()
                                edit22.putBoolean(str, hasSubmittedMelody)
                                edit22.apply()
                            }
                            try {
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent3,
                                        charSequence2
                                    )
                                )
                                hasSubmittedMelody = true
                                val edit4 = sharedPrefs!!.edit()
                                val z = hasSubmittedMelody
                                charSequence2 = charSequence2
                                str = KEY_SUBMITTED_MELODY
                                try {
                                    edit4.putBoolean(str, z)
                                    edit4.apply()
                                } catch (e5: Exception) {
                                    e = e5
                                    //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                    val fromFile42 = Uri.fromFile(
                                        File(
                                            Environment.getExternalStorageDirectory(),
                                            str
                                        )
                                    )
                                    val intent42 = Intent("android.intent.action.SEND")
                                    intent42.putExtra(
                                        "android.intent.extra.SUBJECT",
                                        "Piano Scales And Jam Free Best Melody Competition"
                                    )
                                    intent42.putExtra(
                                        "android.intent.extra.TEXT",
                                        "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                    )
                                    intent42.putExtra(
                                        "android.intent.extra.EMAIL",
                                        arrayOf("competition@TheMelodyMaster.com")
                                    )
                                    intent42.putExtra("android.intent.extra.STREAM", fromFile42)
                                    intent42.setType(str2)
                                    charSequence = charSequence2
                                    this@JamActivity.startActivity(
                                        Intent.createChooser(
                                            intent42,
                                            charSequence
                                        )
                                    )
                                    hasSubmittedMelody = true
                                    val edit32 = sharedPrefs!!.edit()
                                    edit32.putBoolean(str, hasSubmittedMelody)
                                    edit32.apply()
                                    val fromFile222 = Uri.fromFile(
                                        File(
                                            Environment.getExternalStorageDirectory(),
                                            str
                                        )
                                    )
                                    val intent222 = Intent("android.intent.action.SEND")
                                    intent222.putExtra(
                                        "android.intent.extra.SUBJECT",
                                        "Piano Scales And Jam Free Best Melody Competition"
                                    )
                                    intent222.putExtra(
                                        "android.intent.extra.TEXT",
                                        "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                    )
                                    intent222.putExtra(
                                        "android.intent.extra.EMAIL",
                                        arrayOf("competition@TheMelodyMaster.com")
                                    )
                                    intent222.putExtra("android.intent.extra.STREAM", fromFile222)
                                    intent222.setType(str2)
                                    this@JamActivity.startActivity(
                                        Intent.createChooser(
                                            intent222,
                                            charSequence
                                        )
                                    )
                                    hasSubmittedMelody = true
                                    val edit222 = sharedPrefs!!.edit()
                                    edit222.putBoolean(
                                        str,
                                        hasSubmittedMelody
                                    )
                                    edit222.apply()
                                }
                            } catch (e6: Exception) {
                                e = e6
                                charSequence2 = charSequence2
                                str = KEY_SUBMITTED_MELODY
                                //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                val fromFile422 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent422 = Intent("android.intent.action.SEND")
                                intent422.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Best Melody Competition"
                                )
                                intent422.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                )
                                intent422.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("competition@TheMelodyMaster.com")
                                )
                                intent422.putExtra("android.intent.extra.STREAM", fromFile422)
                                intent422.setType(str2)
                                charSequence = charSequence2
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent422,
                                        charSequence
                                    )
                                )
                                hasSubmittedMelody = true
                                val edit322 = sharedPrefs!!.edit()
                                edit322.putBoolean(str, hasSubmittedMelody)
                                edit322.apply()
                                val fromFile2222 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent2222 = Intent("android.intent.action.SEND")
                                intent2222.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Best Melody Competition"
                                )
                                intent2222.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                                )
                                intent2222.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("competition@TheMelodyMaster.com")
                                )
                                intent2222.putExtra("android.intent.extra.STREAM", fromFile2222)
                                intent2222.setType(str2)
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent2222,
                                        charSequence
                                    )
                                )
                                hasSubmittedMelody = true
                                val edit2222 = sharedPrefs!!.edit()
                                edit2222.putBoolean(str, hasSubmittedMelody)
                                edit2222.apply()
                            }
                        }
                        charSequence = charSequence2
                    } catch (e7: Exception) {
                        e = e7
                        str = KEY_SUBMITTED_MELODY
                        str2 = "text/plain"
                        //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                        val fromFile4222 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent4222 = Intent("android.intent.action.SEND")
                        intent4222.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Best Melody Competition"
                        )
                        intent4222.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                        )
                        intent4222.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("competition@TheMelodyMaster.com")
                        )
                        intent4222.putExtra("android.intent.extra.STREAM", fromFile4222)
                        intent4222.setType(str2)
                        charSequence = charSequence2
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent4222,
                                charSequence
                            )
                        )
                        hasSubmittedMelody = true
                        val edit3222 = sharedPrefs!!.edit()
                        edit3222.putBoolean(str, hasSubmittedMelody)
                        edit3222.apply()
                        val fromFile22222 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent22222 = Intent("android.intent.action.SEND")
                        intent22222.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Best Melody Competition"
                        )
                        intent22222.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                        )
                        intent22222.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("competition@TheMelodyMaster.com")
                        )
                        intent22222.putExtra("android.intent.extra.STREAM", fromFile22222)
                        intent22222.setType(str2)
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent22222,
                                charSequence
                            )
                        )
                        hasSubmittedMelody = true
                        val edit22222 = sharedPrefs!!.edit()
                        edit22222.putBoolean(str, hasSubmittedMelody)
                        edit22222.apply()
                    }
                } catch (e8: Exception) {
                    e = e8
                    charSequence2 = "Choose Email Provider to Submit Entry"
                    str2 = "text/plain"
                    str = KEY_SUBMITTED_MELODY
                    //Log.v("themelodymaster", "Enter competition - Exception thrown saving file to external dir Exception:" + e.getMessage());
                    val fromFile42222 = Uri.fromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            str
                        )
                    )
                    val intent42222 = Intent("android.intent.action.SEND")
                    intent42222.putExtra(
                        "android.intent.extra.SUBJECT",
                        "Piano Scales And Jam Free Best Melody Competition"
                    )
                    intent42222.putExtra(
                        "android.intent.extra.TEXT",
                        "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                    )
                    intent42222.putExtra(
                        "android.intent.extra.EMAIL",
                        arrayOf("competition@TheMelodyMaster.com")
                    )
                    intent42222.putExtra("android.intent.extra.STREAM", fromFile42222)
                    intent42222.setType(str2)
                    charSequence = charSequence2
                    this@JamActivity.startActivity(
                        Intent.createChooser(
                            intent42222,
                            charSequence
                        )
                    )
                    hasSubmittedMelody = true
                    val edit32222 = sharedPrefs!!.edit()
                    edit32222.putBoolean(str, hasSubmittedMelody)
                    edit32222.apply()
                    val fromFile222222 = Uri.fromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            str
                        )
                    )
                    val intent222222 = Intent("android.intent.action.SEND")
                    intent222222.putExtra(
                        "android.intent.extra.SUBJECT",
                        "Piano Scales And Jam Free Best Melody Competition"
                    )
                    intent222222.putExtra(
                        "android.intent.extra.TEXT",
                        "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                    )
                    intent222222.putExtra(
                        "android.intent.extra.EMAIL",
                        arrayOf("competition@TheMelodyMaster.com")
                    )
                    intent222222.putExtra("android.intent.extra.STREAM", fromFile222222)
                    intent222222.setType(str2)
                    this@JamActivity.startActivity(
                        Intent.createChooser(
                            intent222222,
                            charSequence
                        )
                    )
                    hasSubmittedMelody = true
                    val edit222222 = sharedPrefs!!.edit()
                    edit222222.putBoolean(str, hasSubmittedMelody)
                    edit222222.apply()
                }
                val fromFile2222222 = Uri.fromFile(
                    File(
                        Environment.getExternalStorageDirectory(),
                        str
                    )
                )
                val intent2222222 = Intent("android.intent.action.SEND")
                intent2222222.putExtra(
                    "android.intent.extra.SUBJECT",
                    "Piano Scales And Jam Free Best Melody Competition"
                )
                intent2222222.putExtra(
                    "android.intent.extra.TEXT",
                    "I'm submitting '$str' as my entry for the best melody competition. You can use this email address to contact me regarding the winning entry!"
                )
                intent2222222.putExtra(
                    "android.intent.extra.EMAIL",
                    arrayOf("competition@TheMelodyMaster.com")
                )
                intent2222222.putExtra("android.intent.extra.STREAM", fromFile2222222)
                intent2222222.setType(str2)
                this@JamActivity.startActivity(
                    Intent.createChooser(
                        intent2222222,
                        charSequence
                    )
                )
                hasSubmittedMelody = true
                val edit2222222 = sharedPrefs!!.edit()
                edit2222222.putBoolean(str, hasSubmittedMelody)
                edit2222222.apply()
            }.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> }.create()
        if (!isFinishing) {
            create.show()
        }
    }

    private fun focusNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapFocus(note))
    }

    private fun focusNotesInScale() {
        //Log.v("themelodymaster", "Highlighting all notes in scale:" + scaleTitle);
        val notes =
            scalesManager!!.scales[scaleNamesIdx].notes
        this.setNoteOctaveIndifferent = HashSet<String>()
        for (note in notes) {
            val replace =
                note.getScaleNoteName(scaleTitle).replace("2", "").replace("3", "").replace("4", "")
                    .replace("5", "").replace(ChordManager.TYPE_6, "")
            setNoteOctaveIndifferent.add(replace)
            //Log.v("themelodymaster", "scale:" + scaleTitle + " adding to set:" + replace);
        }
        for ((_, value) in SoundManager.sounds) {
            this.note2 = value
            val scaleNoteName = value.getScaleNoteName(scaleTitle)
            for (str in this.setNoteOctaveIndifferent) {
                var i: String? = null
                if (str.length == 1 && scaleNoteName.contains(str) && scaleNoteName.length == 2) {
                    i = "1"
                } else if (str.length == 2 && scaleNoteName.contains(str)) {
                    i = "1"
                }
                if (i != null) {
                    //Log.v("themelodymaster", "INLOOP setNoteOctaveIndifferent s:" + str + " scaleNoteName:" + scaleNoteName);
                    val sb = StringBuilder()
                    sb.append("highlighting view:")
                    sb.append(note2!!.getScaleNoteName(scaleTitle))
                    //Log.v("themelodymaster", sb.toString());
                    //Log.v("themelodymaster", "highlight drawing...");
                    (findViewById<View>(note2!!.rId) as ImageButton).setImageBitmap(
                        getBitmapFocus(
                            note2!!
                        )
                    )
                    focusedNoteInScaleSet.add(this.note2!!)
                }
            }
        }
        setKeyboardLayout(scaleFactor)
    }

    private fun getBitmapClean(note: Note): Bitmap? {
        return if (note.name.contains("#")) bitmapBlackKey else bitmapWhiteKey
    }

    private fun getBitmapFocus(note: Note): Bitmap? {
        val scaleNoteName = note.getScaleNoteName(scaleTitle)
        return if ((scaleNoteName == null || !scaleNoteName.contains("#")) && !scaleNoteName!!.contains(
                "b"
            )
        ) bitmapWhiteKeyFocused else bitmapBlackKeyFocused
    }

    private fun getBitmapSelected(note: Note): Bitmap? {
        return if (note.name.contains("#")) bitmapBlackKeySelected else bitmapWhiteKeySelected
    }

    private fun getChordText(i: Int, str: String): String {
        var i2: Int
        var i3 = 0
        var i4: Int
        var str2 = rootNote
        if (str2 == "Db") {
            str2 = "C#"
        } else if (rootNote == "Eb") {
            str2 = "D#"
        } else if (rootNote == "Gb") {
            str2 = "F#"
        } else if (rootNote == "Ab") {
            str2 = "G#"
        } else if (rootNote == "Bb") {
            str2 = "A#"
        }
        val soundManager2 = this.soundManager
        var notePosition = soundManager2!!.getNotePosition(Note(str2 + "3", "400"))
        if (i == 1) {
            i2 = notePosition
        } else {
            if (i == 2) {
                i3 = notePosition + 2
                i4 = notePosition + 1
            } else {
                if (i == 3) {
                    i3 = notePosition + 4
                } else if (i == 4) {
                    i3 = notePosition + 5
                } else if (i == 5) {
                    i3 = notePosition + 7
                    i4 = notePosition + 6
                } else if (i == 6) {
                    i3 = notePosition + 9
                    i4 = notePosition + 8
                } else if (i == 7) {
                    i3 = notePosition + 11
                    i4 = notePosition + 10
                } else {
                    notePosition = 1
                    i2 = 1
                }
                i4 = notePosition + 3
            }
            i2 = i4
            notePosition = i3
        }
        val scaleNoteName = soundManager!!.getNote(notePosition).getScaleNoteName(scaleTitle)
        val substring = scaleNoteName.substring(0, scaleNoteName.length - 1)
        val scaleNoteName2 = soundManager!!.getNote(i2).getScaleNoteName(scaleTitle)
        val substring2 = scaleNoteName2.substring(0, scaleNoteName2.length - 1)
        val sb: StringBuilder? = null
        if (str != "Minor Harmonic") {
            if (str == ChordManager.TYPE_MINOR) {
                if (i == 2) {
                    StringBuilder()
                    sb!!.append(substring)
                    sb.append("Dim")
                    return sb.toString()
                }
                if (i == 1 || i == 4 || i == 5) {
                    StringBuilder()
                }
                return substring2
            } else if (i == 7) {
                StringBuilder()
                sb!!.append(substring)
                sb.append("Dim")
                return sb.toString()
            } else if (i != 2 && i != 3 && i != 6) {
                return substring
            } else {
                return substring + "m"
            }
        } else if (i == 2 || i == 7) {
            StringBuilder()
            sb!!.append(substring)
            sb.append("Dim")
            return sb.toString()
        } else if (i == 3) {
            return substring2 + "Aug"
        } else {
            if (i == 1 || i == 4 || i == 5) {
                StringBuilder()
            }
            return substring2
        }
    }

    private fun getListOfAllMp3FilesPaths(file: File) {
        val listFiles = file.listFiles()
        if (!(listFiles == null || listFiles.size == 0)) {
            for (i in listFiles.indices) {
                val name = listFiles[i].name
                if ((name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".mid") || name.endsWith(
                        ".xmf"
                    ) || name.endsWith(".xmxf") || name.endsWith(".rtttl") || name.endsWith(".rtx") || name.endsWith(
                        ".ota"
                    ) || name.endsWith(".imy")) && listFiles[i].isFile
                ) {
                    musicFilePaths.add(listFiles[i].absolutePath)
                    musicFileNames.add(listFiles[i].name)
                }
                if (listFiles[i].isDirectory) {
                    getListOfAllMp3FilesPaths(listFiles[i])
                }
            }
        }
    }

    private fun getNoteFromPosition(f: Float, f2: Float, i: Int): View {
        val intValue = f.toInt() + i
        val intValue2 = f2.toInt()
        val applyDimension = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (heightOfTopBar + adHeight).toFloat(),
            Companion.resources!!.displayMetrics
        )
        val f3 = fBlackKeyHeightDip + applyDimension
        val f4 = intValue.toFloat()
        val f5 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f5 * 1.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f5),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f6 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f6 * 1.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f6) + ((blackKeyWidth.toFloat()) * f6),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f7 = intValue2.toFloat()
                if (f7 <= f3 && f7 >= applyDimension) {
                    return findViewById(R.id.bottom_c_s)
                }
            }
        }
        val f8 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f8 * 2.0f) + ((blackKeySecondHalfMargin.toFloat()) * f8),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f9 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f9 * 2.0f) + ((blackKeySecondHalfMargin.toFloat()) * f9) + ((blackKeyWidth.toFloat()) * f9),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f10 = intValue2.toFloat()
                if (f10 <= f3 && f10 >= applyDimension) {
                    return findViewById(R.id.bottom_d_s)
                }
            }
        }
        val f11 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f11 * 4.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f11),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f12 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f12 * 4.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f12) + ((blackKeyWidth.toFloat()) * f12),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f13 = intValue2.toFloat()
                if (f13 <= f3 && f13 >= applyDimension) {
                    return findViewById(R.id.bottom_f_s)
                }
            }
        }
        val f14 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f14 * 5.0f) + ((blackKeySecondThirdMargin.toFloat()) * f14),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f15 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f15 * 5.0f) + ((blackKeySecondThirdMargin.toFloat()) * f15) + ((blackKeyWidth.toFloat()) * f15),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f16 = intValue2.toFloat()
                if (f16 <= f3 && f16 >= applyDimension) {
                    return findViewById(R.id.bottom_g_s)
                }
            }
        }
        val f17 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f17 * 6.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f17),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f18 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f18 * 6.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f18) + ((blackKeyWidth.toFloat()) * f18),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f19 = intValue2.toFloat()
                if (f19 <= f3 && f19 >= applyDimension) {
                    return findViewById(R.id.bottom_a_s)
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 0.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 1.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_c)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 1.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 2.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_d)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 2.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 3.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_e)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 3.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 4.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_f)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 4.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 5.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_g)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 5.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 6.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_a)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 6.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 7.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.bottom_b)
        }
        val f20 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f20 * 8.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f20),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f21 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f21 * 8.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f21) + ((blackKeyWidth.toFloat()) * f21),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f22 = intValue2.toFloat()
                if (f22 <= f3 && f22 >= applyDimension) {
                    return findViewById(R.id.middle_c_s)
                }
            }
        }
        val f23 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f23 * 9.0f) + ((blackKeySecondHalfMargin.toFloat()) * f23),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f24 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f24 * 9.0f) + ((blackKeySecondHalfMargin.toFloat()) * f24) + ((blackKeyWidth.toFloat()) * f24),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f25 = intValue2.toFloat()
                if (f25 <= f3 && f25 >= applyDimension) {
                    return findViewById(R.id.middle_d_s)
                }
            }
        }
        val f26 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f26 * 11.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f26),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f27 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f27 * 11.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f27) + ((blackKeyWidth.toFloat()) * f27),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f28 = intValue2.toFloat()
                if (f28 <= f3 && f28 >= applyDimension) {
                    return findViewById(R.id.middle_f_s)
                }
            }
        }
        val f29 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f29 * 12.0f) + ((blackKeySecondThirdMargin.toFloat()) * f29),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f30 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f30 * 12.0f) + ((blackKeySecondThirdMargin.toFloat()) * f30) + ((blackKeyWidth.toFloat()) * f30),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f31 = intValue2.toFloat()
                if (f31 <= f3 && f31 >= applyDimension) {
                    return findViewById(R.id.middle_g_s)
                }
            }
        }
        val f32 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f32 * 13.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f32),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f33 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f33 * 13.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f33) + ((blackKeyWidth.toFloat()) * f33),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f34 = intValue2.toFloat()
                if (f34 <= f3 && f34 >= applyDimension) {
                    return findViewById(R.id.middle_a_s)
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 7.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 8.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_c)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 8.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 9.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_d)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 9.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 10.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_e)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 10.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 11.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_f)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 11.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 12.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_g)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 12.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 13.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_a)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 13.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 14.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.middle_b)
        }
        val f35 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f35 * 15.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f35),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f36 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f36 * 15.0f) + ((blackKeyFirstHalfMargin.toFloat()) * f36) + ((blackKeyWidth.toFloat()) * f36),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f37 = intValue2.toFloat()
                if (f37 <= f3 && f37 >= applyDimension) {
                    return findViewById(R.id.high_c_s)
                }
            }
        }
        val f38 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f38 * 16.0f) + ((blackKeySecondHalfMargin.toFloat()) * f38),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f39 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f39 * 16.0f) + ((blackKeySecondHalfMargin.toFloat()) * f39) + ((blackKeyWidth.toFloat()) * f39),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f40 = intValue2.toFloat()
                if (f40 <= f3 && f40 >= applyDimension) {
                    return findViewById(R.id.high_d_s)
                }
            }
        }
        val f41 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f41 * 18.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f41),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f42 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f42 * 18.0f) + ((blackKeyFirstThirdMargin.toFloat()) * f42) + ((blackKeyWidth.toFloat()) * f42),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f43 = intValue2.toFloat()
                if (f43 <= f3 && f43 >= applyDimension) {
                    return findViewById(R.id.high_f_s)
                }
            }
        }
        val f44 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f44 * 19.0f) + ((blackKeySecondThirdMargin.toFloat()) * f44),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f45 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f45 * 19.0f) + ((blackKeySecondThirdMargin.toFloat()) * f45) + ((blackKeyWidth.toFloat()) * f45),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f46 = intValue2.toFloat()
                if (f46 <= f3 && f46 >= applyDimension) {
                    return findViewById(R.id.high_g_s)
                }
            }
        }
        val f47 = scaleFactor
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((whiteKeyWidth.toFloat()) * f47 * 20.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f47),
                Companion.resources!!.displayMetrics
            )
        ) {
            val f48 = scaleFactor
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * f48 * 20.0f) + ((blackKeyThirdThirdMargin.toFloat()) * f48) + ((blackKeyWidth.toFloat()) * f48),
                    Companion.resources!!.displayMetrics
                )
            ) {
                val f49 = intValue2.toFloat()
                if (f49 <= f3 && f49 >= applyDimension) {
                    return findViewById(R.id.high_a_s)
                }
            }
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 14.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 15.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_c)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 15.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 16.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_d)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 16.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 17.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_e)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 17.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 18.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_f)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 18.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 19.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_g)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 19.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 20.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_a)
        }
        if (f4 >= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 20.0f,
                Companion.resources!!.displayMetrics
            ) && f4 < TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (whiteKeyWidth.toFloat()) * scaleFactor * 21.0f,
                Companion.resources!!.displayMetrics
            )
        ) {
            return findViewById(R.id.high_b)
        }
        return findViewById(
            if (f4 < TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * scaleFactor) * 21.0f,
                    Companion.resources!!.displayMetrics
                ) || f4 >= TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ((whiteKeyWidth.toFloat()) * scaleFactor) * 22.0f,
                    Companion.resources!!.displayMetrics
                )
            ) 0 else R.id.double_high_c
        )
    }

    fun getTuneContent(str: String): String {
        val stringBuffer = StringBuffer(1000)
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        stringBuffer.append("\n<tune title=\"$str\">")
        val it: Iterator<Note?> =
            recordingNotes.iterator()
        while (it.hasNext()) {
            val next = it.next()
            stringBuffer.append(
                """
<note name="${next!!.name}" duration="${next.durationMS}"/>"""
            )
        }
        stringBuffer.append("\n</tune>")
        return stringBuffer.toString()
    }

    private fun highlightToChordButton(charSequence: CharSequence) {
        val str: String
        cleanNotesInScale()
        var charSequence2 = charSequence.toString()
        val str2 = ScalesManager.TYPE_CHORD_MAJOR
        if (charSequence2.contains("Dim")) {
            str = ScalesManager.TYPE_CHORD_DIMINISHED
            charSequence2 = charSequence2.replace("Dim", "")
        } else if (charSequence2.contains("Aug")) {
            str = ScalesManager.TYPE_CHORD_AUGMENTED
            charSequence2 = charSequence2.replace("Aug", "")
        } else if (charSequence2.contains("m")) {
            str = ScalesManager.TYPE_CHORD_MINOR
            charSequence2 = charSequence2.replace("m", "")
        } else {
            str = ScalesManager.TYPE_CHORD_MAJOR
        }
        val notes =
            scalesManager!!.getScale(charSequence2, str, 0, false).notes
        this.setNoteOctaveIndifferent = HashSet<String>()
        for (note in notes) {
            val note3: Note? = null
            setNoteOctaveIndifferent.add(
                note3!!.getScaleNoteName(scaleTitle).replace("2", "").replace("3", "")
                    .replace("4", "").replace("5", "").replace(ChordManager.TYPE_6, "")
            )
        }
        for ((_, value) in SoundManager.sounds) {
            val scaleNoteName = value.getScaleNoteName(scaleTitle)
            for (str3 in this.setNoteOctaveIndifferent) {
                var z = true
                if (!(str3.length == 1 && scaleNoteName.contains(str3) && scaleNoteName.length == 2) && (str3.length != 2 || !scaleNoteName.contains(
                        str3
                    ))
                ) {
                    z = false
                }
                if (z) {
                    //Log.v("themelodymaster", "INLOOP setNoteOctaveIndifferent s:" + str3 + " scaleNoteName:" + scaleNoteName);
                    val sb = StringBuilder()
                    sb.append("highlighting view:")
                    sb.append(value.getScaleNoteName(scaleTitle))
                    //Log.v("themelodymaster", sb.toString());
                    //Log.v("themelodymaster", "highlight drawing...");
                    (findViewById<View>(value.rId) as ImageButton).setImageBitmap(
                        getBitmapFocus(
                            value
                        )
                    )
                    focusedNoteInScaleSet.add(value)
                }
            }
        }
        setKeyboardLayout(scaleFactor)
    }

    fun highlightToScale() {
        cleanNotesInScale()
        scaleTitle = scaleNames!![scaleNamesIdx]
        //Log.v("themelodymaster", "scaleTitle in setTune:" + scaleTitle);
        isAFlatScale = Note.isAFlatScale(scaleTitle)
        focusNotesInScale()
        setStatusText(scaleTitle)
    }

    private fun isInsideCompetitionTime(
        gregorianCalendar: GregorianCalendar,
        gregorianCalendar2: GregorianCalendar,
        j: Long
    ): Boolean {
        return j >= gregorianCalendar.timeInMillis && j < gregorianCalendar2.timeInMillis
    }

    fun isInsideDates(
        gregorianCalendar: GregorianCalendar,
        gregorianCalendar2: GregorianCalendar,
        j: Long
    ): Boolean {
        return j >= gregorianCalendar.timeInMillis && j < gregorianCalendar2.timeInMillis
    }

    fun openMusic() {
        if (musicFilePaths.size == 0) {
            musicFilePaths.clear()
            musicFileNames.clear()
            scanSdcard()
            Collections.sort(musicFileNames)
        }
        if (musicFilePaths.size == 0) {
            Toast.makeText(
                this.context,
                "Could not find any music files in the external memory",
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }
        val title = AlertDialog.Builder(this).setTitle("Select Background Music")
        val list: List<String?> = musicFileNames
        val create = title.setSingleChoiceItems(
            list.toTypedArray<String?>() as Array<CharSequence?>, musicFileNameIdx
        ) { dialogInterface, i ->
            isMp3Loaded = true
            musicFileNameIdx = i
            val str = musicFileNames[i]
            chosenPathToFile = ""
            val it: Iterator<String?> =
                musicFilePaths.iterator()
            while (true) {
                if (!it.hasNext()) {
                    break
                }
                val next = it.next()
                if (next != null && next.contains(str!!)) {
                    chosenPathToFile = next
                    break
                }
            }
            dialogInterface.dismiss()
            this@JamActivity.playMp3()
            this@JamActivity.cleanNotesInScale()
            this@JamActivity.cleanAllChordButtons()
            this@JamActivity.setStatusText(str!!.replace(".mp3", ""))
        }.create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
            create.window!!.setLayout(-1, -2)
        }
    }

    private fun openSaveTune() {
        isMp3Loaded = false
        val create = AlertDialog.Builder(this).setTitle("Select Open or Save").setSingleChoiceItems(
            openSaveValues, openSaveIdx, SaveTuneDialog()
        ).create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
        }
    }

    fun openTune() {
        var str: String
        this.isSongLoadedFromFile = true
        cleanNotesInScale()
        var str2 = filesDir.absolutePath + FILE_DIR
        val list = File(str2).list()
        val arrayList: ArrayList<String> = ArrayList<String>()
        if (list == null || list.size == 0) {
            Toast.makeText(this.context, "No saved files can be found.", Toast.LENGTH_SHORT).show()
            return
        }
        var fileInputStream: FileInputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        var i = 0
        while (i < list.size) {
            try {
                val str3 = list[i]
                Log.d("TEST", "file[$i]: $str3")
                val sb = StringBuilder()
                sb.append(str2)
                sb.append("/")
                sb.append(str3)
                val fileInputStream2 = FileInputStream(File(sb.toString()))
                val stringBuffer = StringBuffer()
                val inputStreamReader2 = InputStreamReader(fileInputStream2)
                val bufferedReader2 = BufferedReader(inputStreamReader2)
                while (true) {
                    val readLine = bufferedReader2.readLine() ?: break
                    stringBuffer.append(readLine)
                    stringBuffer.append("\n")
                    if (readLine.contains("title")) {
                        val indexOf = readLine.indexOf("title=\"")
                        str = str2
                        try {
                            val substring =
                                readLine.substring(indexOf + 7, readLine.indexOf("\"", indexOf + 8))
                            Log.d("themelodymaster", "text contains title:$substring")
                            arrayList.add(substring)
                        } catch (unused: Throwable) {
                        }
                    } else {
                        str = str2
                    }
                    str2 = str
                }
                fileInputStream2.close()
                val strArr = arrayOfNulls<String>(arrayList.size)
                fileNames = strArr
                fileNames = arrayList.toArray(strArr) as Array<String?>
                i++
                str = str2
                fileInputStream = fileInputStream2
                inputStreamReader = inputStreamReader2
                bufferedReader = bufferedReader2
            } catch (unused2: Throwable) {
                str = str2
            }
            str2 = str
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close()
            } catch (unused3: Exception) {
            }
        }
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close()
            } catch (unused4: Exception) {
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close()
            } catch (unused5: Exception) {
            }
        }
        if (fileNames != null) {
            Log.d("themelodymaster", "No files present")
            Toast.makeText(
                this.context,
                "You have no saved tunes. Select a scale to improvise in, record, improvise and save first!",
                Toast.LENGTH_SHORT
            ).show()
        }
        val create =
            AlertDialog.Builder(this).setTitle(getString(R.string.open_tune)).setSingleChoiceItems(
                fileNames, fileNamesIdx, openTuneDialog()
            ).create()
        if (isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
        }
    }

    fun playMp3() {
        //Log.v("themelodymaster", "chosenPathToFile:" + chosenPathToFile);
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            val mediaPlayer2 = MediaPlayer()
            mediaPlayer = mediaPlayer2
            mediaPlayer2.setDataSource(chosenPathToFile)
            mediaPlayer.setVolume(
                ((playAlongVolume.toFloat()) * 1.0f) / 100.0f,
                ((playAlongVolume.toFloat()) * 1.0f) / 100.0f
            )
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { //Log.v("themelodymaster", "onCompletion listener for mediaPlayer called");
                isStopDesired = false
                isPlaying = false
                playStopButton!!.setImageDrawable(playingOff)
            }
            isPlaying = true
            playStopButton!!.setImageDrawable(playingOn)
        } catch (e: Exception) {
            Log.e("themelodymaster", "Exception openingMusic:" + e.message)
        }
    }

    private fun playNoteByUser(view: View?, f: Float, z: Boolean) {
        if (view != null) {
            val note =
                soundManager!!.getNote(view.tag.toString().toInt())
            if (isRecording) {
                if (this.notesInRecording >= 1) {
                    lastPlayedUserNote!!.durationMS =
                        (System.currentTimeMillis() - this.timeMSLastPlayedUserNote).toInt()
                    recordingNotes.add(
                        Note(
                            lastPlayedUserNote!!.name,
                            lastPlayedUserNote!!.durationMS.toString()
                        )
                    )
                    Log.i(
                        "themelodymaster",
                        "Recording Added lastPlayedUserNote to recordingNotes note:" + lastPlayedUserNote!!.name + " duration:" + lastPlayedUserNote!!.durationMS
                    )
                } else {
                    Log.i(
                        "themelodymaster",
                        "Not Recording the last note on the first note played!"
                    )
                }
                this.timeMSLastPlayedUserNote = System.currentTimeMillis()
                notesInRecording++
            }
            selectNote(note)
            this.lastPlayedUserNote = note
            object : Thread() {
                override fun run() {
                    soundManager!!.playSound(view.tag.toString().toInt(), f, z)
                    mHapticManager!!.playHapticEffect(
                        true,
                        mHapticManager!!.getHapticEffect(note)
                    )
                }
            }.start()
        }
    }

    fun playTune() {
        val list: List<*>
        //Log.v("themelodymaster", "playTune called");
        setStatusText("playing...")
        val scale =
            scalesManager!!.scales[scaleNamesIdx]
        if (this.isSongLoadedFromFile) {
            list = this.savedAndOpenedNotes
        } else {
            val arrayList = this.recordingNotes
            if (arrayList == null || arrayList.size <= 0) {
                setStatusText("Piano Jam Free")
                return
            }
            list = this.recordingNotes
        }
        scaleTitle = scale.title
        if (list != null) {
            var i = 1
            if (list.size >= 1) {
                iTagFirstNote = findViewById<View>((list[0] as Note).rId).tag.toString().toInt()
                runOnUiThread(playTuneRunnable2())
                while (true) {
                    if (i > list.size) {
                        break
                    } else if (isStopDesired) {
                        setStatusText("Piano Jam Free")
                        break
                    } else {
                        val note = list[i - 1] as Note
                        if (isHighlightAllNotesOn) {
                            val findViewById = findViewById<View>(note.rId)
                            runOnUiThread {
                                val jamActivity = this@JamActivity
                                jamActivity.selectNote(
                                    jamActivity.soundManager!!.getNote(
                                        findViewById.tag.toString().toInt()
                                    )
                                )
                            }
                        }
                        val soundManager2 = this.soundManager
                        soundManager2!!.playSound(
                            soundManager2.getNotePosition(note),
                            (playAlongVolume.toFloat()) / 100.0f,
                            false
                        )
                        try {
                            val longValue = ((note.durationMS
                                .toFloat()) * 0.8f * (100.0f / (playAlongSpeed.toFloat())).toLong()).toLong()
                            if (longValue >= 0) {
                                Thread.sleep(longValue)
                            }
                        } catch (unused: InterruptedException) {
                        }
                        if (isHighlightAllNotesOn) {
                            val button = findViewById<View>(note.rId) as Button
                            runOnUiThread {
                                val jamActivity = this@JamActivity
                                jamActivity.resetState(
                                    jamActivity.soundManager!!.getNote(
                                        button.tag.toString().toInt()
                                    )
                                )
                            }
                        }
                        try {
                            val valueOf =
                                (note.durationMS.toFloat()) * 0.2f * (100.0f / (playAlongSpeed.toFloat()))
                            val longValue2 = valueOf.toLong()
                            //Log.v("themelodymaster", "SOUND FINISHED AFTER playAlongSpeed:" + playAlongSpeed + " fDuration:" + valueOf + " longDuration:" + longValue2);
                            Thread.sleep(longValue2)
                        } catch (unused2: InterruptedException) {
                        }
                        i++
                    }
                }
                setStatusText("Piano Jam Free")
                return
            }
        }
        runOnUiThread(playTuneRunnable())
    }

    fun populateChordButtons() {
        val str = scaleNames!![scaleNamesIdx]
        var str2 = ChordManager.TYPE_MAJOR
        if (!str.contains(str2)) {
            if (str.contains("Minor Harmonic")) {
                str2 = "Minor Harmonic"
            } else if (str.contains(ChordManager.TYPE_MINOR)) {
                str2 = ChordManager.TYPE_MINOR
            }
        }
        chordButtonOne!!.text = getChordText(1, str2)
        chordButtonTwo!!.text = getChordText(2, str2)
        chordButtonThree!!.text = getChordText(3, str2)
        chordButtonFour!!.text = getChordText(4, str2)
        chordButtonFive!!.text = getChordText(5, str2)
        chordButtonSix!!.text = getChordText(6, str2)
        chordButtonSeven!!.text = getChordText(7, str2)
    }

    fun requestPermission(str: String, i: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(str), i)
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ) != 0
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "The app needs to load and save recordings from your device",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    1
                )
            } else {
                requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", 1)
            }
        }
    }

    fun resetState(note: Note) {
        if (focusedNoteInScaleSet.contains(note)) {
            focusNote(note)
        } else {
            cleanNote(note)
        }
    }

    fun saveTune() {
        //Log.v("themelodymaster", "saveTune called");
        val inflate = View.inflate(this, R.layout.save_dialog, null)
        val create =
            AlertDialog.Builder(this).setTitle("Save Tune").setView(inflate).setPositiveButton(
                "Save",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    val trim =
                        (inflate.findViewById<View>(R.id.saveRiffTitleNameEdit) as EditText).text.toString()
                            .trim { it <= ' ' }
                    val jamActivity = this@JamActivity
                    jamActivity.content = jamActivity.getTuneContent(trim)
                    if (content!!.contains("note")) {
                        //Log.v("themelodymaster", "Saving to internal storage fileName:" + trim + " content:" + JamActivity.this.content);
                        val fileOutputStream: FileOutputStream? = null
                        try {
                            val file =
                                File(this@JamActivity.filesDir.toString() + FILE_DIR)
                            if (!file.exists()) {
                                file.mkdir()
                            }
                            val file2 = File("$file/$trim")
                            file2.createNewFile()
                            val fileOutputStream2 = FileOutputStream(file2)
                            try {
                                fileOutputStream2.write(content!!.toByteArray())
                                if (this@JamActivity.isInsideDates(
                                        GregorianCalendar(
                                            2012,
                                            9,
                                            1
                                        ),
                                        GregorianCalendar(2017, 9, 1),
                                        System.currentTimeMillis()
                                    )
                                ) {
                                    this@JamActivity.submitTune(trim)
                                }
                                fileOutputStream2.close()
                                return@OnClickListener
                            } catch (unused: Exception) {
                                return@OnClickListener
                            } catch (th: Throwable) {
                                th.printStackTrace()
                            }
                        } catch (e: Exception) {
                            Log.d(
                                "TEST",
                                "saveToInternalStorageButton Exception:" + e.message
                            )
                            try {
                                fileOutputStream!!.close()
                                return@OnClickListener
                            } catch (e2: IOException) {
                                e2.printStackTrace()
                                return@OnClickListener
                            }
                        }
                    }
                    Toast.makeText(
                        this@JamActivity.context,
                        "There are no new notes in this recording. Not saving.",
                        Toast.LENGTH_SHORT
                    ).show()
                }).setNegativeButton(
                "Cancel"
            ) { dialogInterface, i ->
                //Log.v("themelodymaster", "Cancel called");
            }.create()
        if (!isFinishing) {
            create.show()
        }
    }

    private fun scanSdcard() {
        throw UnsupportedOperationException("Method not decompiled: com.veitch.themelodymaster.psajf.ui.activities.JamActivity.scanSdcard():void")
    }

    fun scrollToTag(i: Int) {
        //Log.v("themelodymaster", "scrollToTag called tag:" + i);
        if (!isAutoscrollOn) {
            return
        }
        if (i == 0) {
            val seekBar2 = this.seekBar
            seekBar2!!.progress = seekBar2.max / 2
            val seekBar3 = this.seekBar
            onProgressChanged(seekBar3!!, seekBar3.max / 2, false)
        } else if (i <= 12) {
            seekBar!!.progress = 0
            onProgressChanged(seekBar!!, 0, false)
        } else if (i <= 12 || i > 24) {
            val seekBar4 = this.seekBar
            seekBar4!!.progress = seekBar4.max
            val seekBar5 = this.seekBar
            onProgressChanged(seekBar5!!, seekBar5.max, false)
        } else {
            val seekBar6 = this.seekBar
            seekBar6!!.progress = seekBar6.max / 2
            val seekBar7 = this.seekBar
            onProgressChanged(seekBar7!!, seekBar7.max / 2, false)
        }
    }

    fun selectNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapSelected(note))
    }

    private fun setKey() {
        val create = AlertDialog.Builder(this).setTitle("Select Root Note").setSingleChoiceItems(
            rootNoteValues, rootNotesIdx, setKeyDialog()
        ).create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
        }
    }

    private fun setKeyboardLayout(f: Float) {
        val it: Iterator<TextView> = keyLabels!!.iterator()
        while (it.hasNext()) {
            notesLayout!!.removeViewInLayout(it.next())
        }
        notesLayout!!.layoutParams = LinearLayout.LayoutParams(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1254.0f * f,
                Companion.resources!!.displayMetrics
            ).toInt(), -1
        )
        val applyDimension = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (whiteKeyWidth.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyWidth.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension2 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (whiteKeyHeight.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension3 =
            (Companion.resources!!.displayMetrics.heightPixels.toFloat()) - TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((adHeight + adAdjustment) + heightOfTopBar).toFloat(),
                Companion.resources!!.displayMetrics
            )
        if (applyDimension3 < applyDimension2) {
            fBlackKeyHeightDip = applyDimension3 * 0.6f
        } else {
            fBlackKeyHeightDip = 0.6f * applyDimension2
        }
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyFirstHalfMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeySecondHalfMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyFirstThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeySecondThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyThirdThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension4 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            whiteKeyLabelMarginBottom * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension5 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            whiteKeyLabelMarginLeft * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            blackKeyLabelMarginBottom * f,
            Companion.resources!!.displayMetrics
        )
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            blackKeyLabelMarginLeft * f,
            Companion.resources!!.displayMetrics
        )
        val imageButton = findViewById<View>(R.id.bottom_c) as ImageButton
        val i = applyDimension.toInt()
        val i2 = applyDimension2.toInt()
        imageButton.layoutParams = RelativeLayout.LayoutParams(i, i2)
        addLabel(
            imageButton,
            null,
            "C3",
            whiteKeyLabelSize * f,
            true,
            applyDimension4.toInt(),
            0,
            0,
            applyDimension5.toInt()
        )
        val imageButton2 = findViewById<View>(R.id.bottom_d) as ImageButton
        RelativeLayout.LayoutParams(i, i2)
    }

    private fun setScaleToFocus() {
        val create = AlertDialog.Builder(this).setTitle(getString(R.string.select_key_to_highlight))
            .setSingleChoiceItems(
                scaleNames, scaleNamesIdx, ScaleToFocusDialog()
            ).create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
            create.window!!.setLayout(-1, -2)
        }
    }

    fun setStatusText(str: String?) {
        runOnUiThread { //Log.v("themelodymaster", "settingText:" + ((String) null));
            statusTextView!!.text = null as CharSequence?
        }
    }

    private fun showCompetitionInfo() {
        //Log.v("themelodymaster", "showCompetitionInfo called hasCompetitionInfoAlreadyShownInGame:" + hasCompetitionInfoAlreadyShownInGame);
        if (!hasCompetitionInfoAlreadyShownInGame) {
            val gregorianCalendar = GregorianCalendar(2012, 4, 1)
            val gregorianCalendar2 = GregorianCalendar(2012, 9, 1)
            val currentTimeMillis = System.currentTimeMillis()
            val isInsideCompetitionTime =
                isInsideCompetitionTime(gregorianCalendar, gregorianCalendar2, currentTimeMillis)
            //Log.v("themelodymaster", "isInsideCompetitionTime:" + isInsideCompetitionTime + " with startGreg:" + gregorianCalendar + " endGreg:" + gregorianCalendar2 + " now:" + currentTimeMillis);
            if (isInsideCompetitionTime) {
                val inflate = View.inflate(this, R.layout.comp_info_dialog, null)
                (inflate.findViewById<View>(R.id.comp_info_text) as TextView).text =
                    "Win $200 US for the best original melody.\n\nRecord your finest melody. When saving it, follow the instructions to submit.\n\nTerms and Conditions.\nIn addition to our standard T & Cs the following terms and conditions apply to this prize.The closing date for entries is the 1st October 2012. The winning entry will need to supply a paypal account for the transfer of the prize. The supplied email address will be used for correspondence regarding the prize and will be deleted after the competition.The entry must be an original piece of work and not infringe any copyright legislation. All submitted entries will become the copyright of Learn to Master and can be used in the future in any digital media which Learn to Master sees fit. There will be 1 winner who will win the full amount. By submitting an entry you agree to the Terms and Conditions."
                val create =
                    AlertDialog.Builder(this).setTitle("Best Melody Competition").setView(inflate)
                        .create()
                create.setButton(
                    -3, "Close"
                ) { dialogInterface, i -> create.dismiss() }
                if (!isFinishing) {
                    create.show()
                }
                hasCompetitionInfoAlreadyShownInGame = true
            }
        }
    }

    private fun showExplanation(str: String, str2: String, str3: String, i: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(str).setMessage(str2).setPositiveButton(
            "yes"
        ) { dialogInterface, i ->
            this@JamActivity.requestPermission(
                str3,
                i
            )
        }
        builder.create().show()
    }

    private fun showHelp() {
        startActivity(Intent(this, JamHelpActivity::class.java))
    }

    private fun showOptions() {
        isStopDesired = true
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun showTuneInfo() {
        //Log.v("themelodymaster", "showTuneInfo called hasSubmittedTune:" + hasSubmittedTune);
        if (!hasSubmittedTune) {
            val gregorianCalendar = GregorianCalendar(2012, 9, 1)
            val gregorianCalendar2 = GregorianCalendar(2017, 9, 1)
            val currentTimeMillis = System.currentTimeMillis()
            val isInsideCompetitionTime =
                isInsideCompetitionTime(gregorianCalendar, gregorianCalendar2, currentTimeMillis)
            //Log.v("themelodymaster", "isInsideTuneTime:" + isInsideCompetitionTime + " with startGreg:" + gregorianCalendar + " endGreg:" + gregorianCalendar2 + " now:" + currentTimeMillis);
            if (isInsideCompetitionTime) {
                val inflate = View.inflate(this, R.layout.comp_info_dialog, null)
                (inflate.findViewById<View>(R.id.comp_info_text) as TextView).text =
                    "The best submitted tunes are available for you to listen.\n\nRecord your finest melody and let other users listen to it. When saving it, follow the instructions to submit.\n\nTerms and Conditions.\nIn addition to our standard T & Cs the following terms and conditions apply. The submitted tune must be an original piece of work and not infringe any copyright legislation. All submitted tunes will become the copyright of Learn to Master and can be used in the future in any digital media which Learn to Master sees fit. By submitting a tune you agree to the Terms and Conditions."
                val create =
                    AlertDialog.Builder(this).setTitle("Send Your Best Tune").setView(inflate)
                        .create()
                create.setButton(
                    -3, "Close"
                ) { dialogInterface, i -> create.dismiss() }
                if (!isFinishing) {
                    create.show()
                }
            }
        }
    }

    fun submitTune(str: String) {
        //Log.v("themelodymaster", "enterTune called filename:" + str);
        if (hasSubmittedTune) {
            Toast.makeText(
                this,
                "You have already submitted or attempted to submit a tune. Another submission will replace the previous one.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val inflate = View.inflate(this, R.layout.comp_submit_dialog, null)
        (inflate.findViewById<View>(R.id.comp_enter_text) as TextView).text =
            """
            Submit tune '$str' and let others listen to it.
            
            Terms and Conditions.
            $termsAndConditionsForTune
            """.trimIndent()
        val create =
            AlertDialog.Builder(this).setTitle("Submit Tune").setView(inflate).setPositiveButton(
                "Submit"
            ) { dialogInterface, i ->
                var charSequence: CharSequence?
                var str: String? = null
                var str2: String
                var charSequence2: CharSequence?
                var e: Exception
                //Log.v("themelodymaster", "Submit tune - Save selected.");
                hasSubmittedTune = true
                try {
                    charSequence2 = "Choose Email Provider to Submit Entry"
                    try {
                    } catch (e2: Exception) {
                        e = e2
                        str2 = "text/plain"
                        str = KEY_SUBMITTED_TUNE
                        //Log.v("themelodymaster", "Submit Tune - Exception thrown saving file to external dir Exception:" + e.getMessage());
                        val fromFile = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent = Intent("android.intent.action.SEND")
                        intent.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Submit Tune"
                        )
                        intent.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                        )
                        intent.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("submit_tune@TheMelodyMaster.com")
                        )
                        intent.putExtra("android.intent.extra.STREAM", fromFile)
                        intent.setType(str2)
                        charSequence = charSequence2
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent,
                                charSequence
                            )
                        )
                        hasSubmittedTune = true
                        val edit = sharedPrefs!!.edit()
                        edit.putBoolean(str, hasSubmittedTune)
                        edit.apply()
                        val fromFile2 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent2 = Intent("android.intent.action.SEND")
                        intent2.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Submit Tune"
                        )
                        intent2.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                        )
                        intent2.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("submit_tune@TheMelodyMaster.com")
                        )
                        intent2.putExtra("android.intent.extra.STREAM", fromFile2)
                        intent2.setType(str2)
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent2,
                                charSequence
                            )
                        )
                        hasSubmittedTune = true
                        val edit2 = sharedPrefs!!.edit()
                        edit2.putBoolean(str, hasSubmittedTune)
                        edit2.apply()
                    }
                    try {
                        val fileOutputStream = FileOutputStream(
                            File(
                                Environment.getExternalStorageDirectory().toString(), str
                            )
                        )
                        try {
                            fileOutputStream.write(content!!.toByteArray())
                            fileOutputStream.flush()
                            fileOutputStream.close()
                            str = KEY_SUBMITTED_TUNE
                            str2 = "text/plain"
                        } catch (e3: Exception) {
                            //Log.v("themelodymaster", "Submit Tune - Exception thrown saving file to external dir Exception:" + e3.getMessage());
                            val fromFile3 = Uri.fromFile(
                                File(
                                    Environment.getExternalStorageDirectory(),
                                    "themelodymaster"
                                )
                            )
                            val intent3 = Intent("android.intent.action.SEND")
                            intent3.putExtra(
                                "android.intent.extra.SUBJECT",
                                "Piano Scales And Jam Free Submit Tune"
                            )
                            intent3.putExtra(
                                "android.intent.extra.TEXT",
                                "I'm submitting '" + "themelodymaster" + "' for users to listen to. You can use this email address to contact me regarding the tune."
                            )
                            intent3.putExtra(
                                "android.intent.extra.EMAIL",
                                arrayOf("submit_tune@TheMelodyMaster.com")
                            )
                            intent3.putExtra("android.intent.extra.STREAM", fromFile3)
                            str2 = "text/plain"
                            try {
                                intent3.setType(str2)
                            } catch (e4: Exception) {
                                e = e4
                                str = KEY_SUBMITTED_TUNE
                                //Log.v("themelodymaster", "Submit Tune - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                val fromFile4 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent4 = Intent("android.intent.action.SEND")
                                intent4.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Submit Tune"
                                )
                                intent4.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                )
                                intent4.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("submit_tune@TheMelodyMaster.com")
                                )
                                intent4.putExtra("android.intent.extra.STREAM", fromFile4)
                                intent4.setType(str2)
                                charSequence = charSequence2
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent4,
                                        charSequence
                                    )
                                )
                                hasSubmittedTune = true
                                val edit3 = sharedPrefs!!.edit()
                                edit3.putBoolean(str, hasSubmittedTune)
                                edit3.apply()
                                val fromFile22 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent22 = Intent("android.intent.action.SEND")
                                intent22.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Submit Tune"
                                )
                                intent22.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                )
                                intent22.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("submit_tune@TheMelodyMaster.com")
                                )
                                intent22.putExtra("android.intent.extra.STREAM", fromFile22)
                                intent22.setType(str2)
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent22,
                                        charSequence
                                    )
                                )
                                hasSubmittedTune = true
                                val edit22 = sharedPrefs!!.edit()
                                edit22.putBoolean(str, hasSubmittedTune)
                                edit22.apply()
                            }
                            try {
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent3,
                                        charSequence2
                                    )
                                )
                                hasSubmittedTune = true
                                val edit4 = sharedPrefs!!.edit()
                                val z = hasSubmittedTune
                                charSequence2 = charSequence2
                                str = KEY_SUBMITTED_TUNE
                                try {
                                    edit4.putBoolean(str, z)
                                    edit4.apply()
                                } catch (e5: Exception) {
                                    e = e5
                                    //Log.v("themelodymaster", "Submit Tune - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                    val fromFile42 = Uri.fromFile(
                                        File(
                                            Environment.getExternalStorageDirectory(),
                                            str
                                        )
                                    )
                                    val intent42 = Intent("android.intent.action.SEND")
                                    intent42.putExtra(
                                        "android.intent.extra.SUBJECT",
                                        "Piano Scales And Jam Free Submit Tune"
                                    )
                                    intent42.putExtra(
                                        "android.intent.extra.TEXT",
                                        "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                    )
                                    intent42.putExtra(
                                        "android.intent.extra.EMAIL",
                                        arrayOf("submit_tune@TheMelodyMaster.com")
                                    )
                                    intent42.putExtra("android.intent.extra.STREAM", fromFile42)
                                    intent42.setType(str2)
                                    charSequence = charSequence2
                                    this@JamActivity.startActivity(
                                        Intent.createChooser(
                                            intent42,
                                            charSequence
                                        )
                                    )
                                    hasSubmittedTune = true
                                    val edit32 = sharedPrefs!!.edit()
                                    edit32.putBoolean(str, hasSubmittedTune)
                                    edit32.apply()
                                    val fromFile222 = Uri.fromFile(
                                        File(
                                            Environment.getExternalStorageDirectory(),
                                            str
                                        )
                                    )
                                    val intent222 = Intent("android.intent.action.SEND")
                                    intent222.putExtra(
                                        "android.intent.extra.SUBJECT",
                                        "Piano Scales And Jam Free Submit Tune"
                                    )
                                    intent222.putExtra(
                                        "android.intent.extra.TEXT",
                                        "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                    )
                                    intent222.putExtra(
                                        "android.intent.extra.EMAIL",
                                        arrayOf("submit_tune@TheMelodyMaster.com")
                                    )
                                    intent222.putExtra("android.intent.extra.STREAM", fromFile222)
                                    intent222.setType(str2)
                                    this@JamActivity.startActivity(
                                        Intent.createChooser(
                                            intent222,
                                            charSequence
                                        )
                                    )
                                    hasSubmittedTune = true
                                    val edit222 = sharedPrefs!!.edit()
                                    edit222.putBoolean(str, hasSubmittedTune)
                                    edit222.apply()
                                }
                            } catch (e6: Exception) {
                                e = e6
                                charSequence2 = charSequence2
                                str = KEY_SUBMITTED_TUNE
                                //Log.v("themelodymaster", "Submit Tune - Exception thrown saving file to external dir Exception:" + e.getMessage());
                                val fromFile422 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent422 = Intent("android.intent.action.SEND")
                                intent422.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Submit Tune"
                                )
                                intent422.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                )
                                intent422.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("submit_tune@TheMelodyMaster.com")
                                )
                                intent422.putExtra("android.intent.extra.STREAM", fromFile422)
                                intent422.setType(str2)
                                charSequence = charSequence2
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent422,
                                        charSequence
                                    )
                                )
                                hasSubmittedTune = true
                                val edit322 = sharedPrefs!!.edit()
                                edit322.putBoolean(str, hasSubmittedTune)
                                edit322.apply()
                                val fromFile2222 = Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory(),
                                        str
                                    )
                                )
                                val intent2222 = Intent("android.intent.action.SEND")
                                intent2222.putExtra(
                                    "android.intent.extra.SUBJECT",
                                    "Piano Scales And Jam Free Submit Tune"
                                )
                                intent2222.putExtra(
                                    "android.intent.extra.TEXT",
                                    "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                                )
                                intent2222.putExtra(
                                    "android.intent.extra.EMAIL",
                                    arrayOf("submit_tune@TheMelodyMaster.com")
                                )
                                intent2222.putExtra("android.intent.extra.STREAM", fromFile2222)
                                intent2222.setType(str2)
                                this@JamActivity.startActivity(
                                    Intent.createChooser(
                                        intent2222,
                                        charSequence
                                    )
                                )
                                hasSubmittedTune = true
                                val edit2222 = sharedPrefs!!.edit()
                                edit2222.putBoolean(str, hasSubmittedTune)
                                edit2222.apply()
                            }
                        }
                        charSequence = charSequence2
                    } catch (e7: Exception) {
                        e = e7
                        str = KEY_SUBMITTED_TUNE
                        str2 = "text/plain"
                        Log.v(
                            "themelodymaster",
                            "Submit Tune - Exception thrown saving file to external dir Exception:" + e.message
                        )
                        val fromFile4222 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent4222 = Intent("android.intent.action.SEND")
                        intent4222.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Submit Tune"
                        )
                        intent4222.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                        )
                        intent4222.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("submit_tune@TheMelodyMaster.com")
                        )
                        intent4222.putExtra("android.intent.extra.STREAM", fromFile4222)
                        intent4222.setType(str2)
                        charSequence = charSequence2
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent4222,
                                charSequence
                            )
                        )
                        hasSubmittedTune = true
                        val edit3222 = sharedPrefs!!.edit()
                        edit3222.putBoolean(str, hasSubmittedTune)
                        edit3222.apply()
                        val fromFile22222 = Uri.fromFile(
                            File(
                                Environment.getExternalStorageDirectory(),
                                str
                            )
                        )
                        val intent22222 = Intent("android.intent.action.SEND")
                        intent22222.putExtra(
                            "android.intent.extra.SUBJECT",
                            "Piano Scales And Jam Free Submit Tune"
                        )
                        intent22222.putExtra(
                            "android.intent.extra.TEXT",
                            "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                        )
                        intent22222.putExtra(
                            "android.intent.extra.EMAIL",
                            arrayOf("submit_tune@TheMelodyMaster.com")
                        )
                        intent22222.putExtra("android.intent.extra.STREAM", fromFile22222)
                        intent22222.setType(str2)
                        this@JamActivity.startActivity(
                            Intent.createChooser(
                                intent22222,
                                charSequence
                            )
                        )
                        hasSubmittedTune = true
                        val edit22222 = sharedPrefs!!.edit()
                        edit22222.putBoolean(str, hasSubmittedTune)
                        edit22222.apply()
                    }
                } catch (e8: Exception) {
                    e = e8
                    charSequence2 = "Choose Email Provider to Submit Entry"
                    str2 = "text/plain"
                    str = KEY_SUBMITTED_TUNE
                    Log.v(
                        "themelodymaster",
                        "Submit Tune - Exception thrown saving file to external dir Exception:" + e.message
                    )
                    val fromFile42222 = Uri.fromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            str
                        )
                    )
                    val intent42222 = Intent("android.intent.action.SEND")
                    intent42222.putExtra(
                        "android.intent.extra.SUBJECT",
                        "Piano Scales And Jam Free Submit Tune"
                    )
                    intent42222.putExtra(
                        "android.intent.extra.TEXT",
                        "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                    )
                    intent42222.putExtra(
                        "android.intent.extra.EMAIL",
                        arrayOf("submit_tune@TheMelodyMaster.com")
                    )
                    intent42222.putExtra("android.intent.extra.STREAM", fromFile42222)
                    intent42222.setType(str2)
                    charSequence = charSequence2
                    this@JamActivity.startActivity(
                        Intent.createChooser(
                            intent42222,
                            charSequence
                        )
                    )
                    hasSubmittedTune = true
                    val edit32222 = sharedPrefs!!.edit()
                    edit32222.putBoolean(str, hasSubmittedTune)
                    edit32222.apply()
                    val fromFile222222 = Uri.fromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            str
                        )
                    )
                    val intent222222 = Intent("android.intent.action.SEND")
                    intent222222.putExtra(
                        "android.intent.extra.SUBJECT",
                        "Piano Scales And Jam Free Submit Tune"
                    )
                    intent222222.putExtra(
                        "android.intent.extra.TEXT",
                        "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                    )
                    intent222222.putExtra(
                        "android.intent.extra.EMAIL",
                        arrayOf("submit_tune@TheMelodyMaster.com")
                    )
                    intent222222.putExtra("android.intent.extra.STREAM", fromFile222222)
                    intent222222.setType(str2)
                    this@JamActivity.startActivity(
                        Intent.createChooser(
                            intent222222,
                            charSequence
                        )
                    )
                    hasSubmittedTune = true
                    val edit222222 = sharedPrefs!!.edit()
                    edit222222.putBoolean(str, hasSubmittedTune)
                    edit222222.apply()
                }
                val fromFile2222222 = Uri.fromFile(
                    File(
                        Environment.getExternalStorageDirectory(),
                        str
                    )
                )
                val intent2222222 = Intent("android.intent.action.SEND")
                intent2222222.putExtra(
                    "android.intent.extra.SUBJECT",
                    "Piano Scales And Jam Free Submit Tune"
                )
                intent2222222.putExtra(
                    "android.intent.extra.TEXT",
                    "I'm submitting '$str' for users to listen to. You can use this email address to contact me regarding the tune."
                )
                intent2222222.putExtra(
                    "android.intent.extra.EMAIL",
                    arrayOf("submit_tune@TheMelodyMaster.com")
                )
                intent2222222.putExtra("android.intent.extra.STREAM", fromFile2222222)
                intent2222222.setType(str2)
                this@JamActivity.startActivity(
                    Intent.createChooser(
                        intent2222222,
                        charSequence
                    )
                )
                hasSubmittedTune = true
                val edit2222222 = sharedPrefs!!.edit()
                edit2222222.putBoolean(str, hasSubmittedTune)
                edit2222222.apply()
            }.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> }.create()
        if (!isFinishing) {
            create.show()
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        } else if (view.id == R.id.choose_key_button) {
            setKey()
        } else if (view.id == R.id.scale_highlight_button) {
            setScaleToFocus()
        } else if (view.id == R.id.play_tune_button) {
            if (isRecording) {
                Toast.makeText(
                    this,
                    "You are still in recording mode. You need to select stop recording.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (isPlaying) {
                isStopDesired = true
                val mediaPlayer2 = mediaPlayer
                if (mediaPlayer2 != null && mediaPlayer2.isPlaying) {
                    mediaPlayer.stop()
                    isStopDesired = false
                }
                isPlaying = false
                playStopButton!!.setImageDrawable(playingOff)
            } else {
                isPlaying = true
                playStopButton!!.setImageDrawable(playingOn)
                StartThread().start()
                setStatusText("Piano Jam Free")
            }
        } else if (view.id == R.id.record_stop_button) {
            if (isPlaying) {
                Toast.makeText(
                    this,
                    "You are still in playing mode. You need to select stop playing.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (isRecording) {
                Log.i("themelodymaster", "Record: Was in recording mode")
                isRecording = false
                val currentTimeMillis = System.currentTimeMillis() - this.timeMSLastPlayedUserNote
                val sb = StringBuilder()
                sb.append("Recorded long durationofLastNote:")
                sb.append(currentTimeMillis)
                sb.append("int:")
                val i = currentTimeMillis.toInt()
                sb.append(i)
                Log.i("themelodymaster", sb.toString())
                val note = this.lastPlayedUserNote
                if (note != null) {
                    note.durationMS = i
                    this.lastPlayedUserNote?.let { recordingNotes.add(it) }
                    Log.i(
                        "themelodymaster",
                        "Added lastPlayedUserNote to recordingNotes note:" + lastPlayedUserNote!!.name + " duration:" + lastPlayedUserNote!!.durationMS
                    )
                    val it: Iterator<Note?> =
                        recordingNotes.iterator()
                    while (it.hasNext()) {
                        val next = it.next()
                        Log.i(
                            "themelodymaster",
                            "Recorded notes:" + next!!.name + ":" + next.durationMS
                        )
                    }
                }
                setStatusText("Piano Jam Free")
                recordStopButton!!.setImageDrawable(recordingOff)
            } else {
                Log.i("themelodymaster", "Record: Not in recording mode. Start recording.")
                isRecording = true
                this.recordingNotes = ArrayList()
                this.notesInRecording = 0
                setStatusText("recording...")
                recordStopButton!!.setImageDrawable(recordingOn)
            }
            this.isSongLoadedFromFile = false
            this.lastPlayedUserNote = null
            this.timeMSLastPlayedUserNote = 0
        } else if (view.id == R.id.open_save_button) {
            openSaveTune()
        } else if (view.id == R.id.click_button) {
            if (disableClickButton) {
                return
            }
            if (isClicking) {
                soundManager!!.stopClick()
                isClicking = false
                return
            }
            soundManager!!.startClick(this.context, javaClass.simpleName)
            isClicking = true
        } else if (view.id == R.id.chord_button_one) {
            Log.v("themelodymaster", "Chord Button 1 pressed:" + chordButtonOne!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_one).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_two).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_three).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_four).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_six).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_seven).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonOne!!.text)
        } else if (view.id == R.id.chord_button_two) {
            Log.v("themelodymaster", "Chord Button 2 pressed:" + chordButtonTwo!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_two).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_seven).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonTwo!!.text)
        } else if (view.id == R.id.chord_button_three) {
            Log.v("themelodymaster", "Chord Button 3 pressed:" + chordButtonThree!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_three).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_four).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_six).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonThree!!.text)
        } else if (view.id == R.id.chord_button_four) {
            Log.v("themelodymaster", "Chord Button 4 pressed:" + chordButtonFour!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_four).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_one).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_two).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_seven).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonFour!!.text)
        } else if (view.id == R.id.chord_button_five) {
            Log.v("themelodymaster", "Chord Button 5 pressed:" + chordButtonFive!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_one).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_six).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonFive!!.text)
        } else if (view.id == R.id.chord_button_six) {
            Log.v("themelodymaster", "Chord Button 6 pressed:" + chordButtonSix!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_six).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_two).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_four).setBackgroundResource(R.drawable.background_button_recommended)
            findViewById<View>(R.id.chord_button_five).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonSix!!.text)
        } else if (view.id == R.id.chord_button_seven) {
            Log.v("themelodymaster", "Chord Button 7 pressed:" + chordButtonSeven!!.text)
            cleanAllChordButtons()
            findViewById<View>(R.id.chord_button_seven).setBackgroundResource(R.drawable.background_button_pressed)
            findViewById<View>(R.id.chord_button_one).setBackgroundResource(R.drawable.background_button_recommended)
            highlightToChordButton(chordButtonSeven!!.text)
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        requestWindowFeature(1)
        window.setFlags(1024, 1024)
        setContentView(R.layout.jam)
        this.statusTextView = findViewById<View>(R.id.headerText) as TextView
        setStatusText("Piano Jam Free")
        this.soundManager = SoundManager.getInstance(this)
        this.mHapticManager = HapticManager.getInstance(this)
        findViewById<View>(R.id.bottom_c).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_d).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_e).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_f).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_g).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_a).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_b).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_c_s).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_d_s).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_f_s).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_g_s).setOnTouchListener(this)
        findViewById<View>(R.id.bottom_a_s).setOnTouchListener(this)
        findViewById<View>(R.id.middle_c).setOnTouchListener(this)
        findViewById<View>(R.id.middle_d).setOnTouchListener(this)
        findViewById<View>(R.id.middle_e).setOnTouchListener(this)
        findViewById<View>(R.id.middle_f).setOnTouchListener(this)
        findViewById<View>(R.id.middle_g).setOnTouchListener(this)
        findViewById<View>(R.id.middle_a).setOnTouchListener(this)
        findViewById<View>(R.id.middle_b).setOnTouchListener(this)
        findViewById<View>(R.id.middle_c_s).setOnTouchListener(this)
        findViewById<View>(R.id.middle_d_s).setOnTouchListener(this)
        findViewById<View>(R.id.middle_f_s).setOnTouchListener(this)
        findViewById<View>(R.id.middle_g_s).setOnTouchListener(this)
        findViewById<View>(R.id.middle_a_s).setOnTouchListener(this)
        findViewById<View>(R.id.high_c).setOnTouchListener(this)
        findViewById<View>(R.id.high_d).setOnTouchListener(this)
        findViewById<View>(R.id.high_e).setOnTouchListener(this)
        findViewById<View>(R.id.high_f).setOnTouchListener(this)
        findViewById<View>(R.id.high_g).setOnTouchListener(this)
        findViewById<View>(R.id.high_a).setOnTouchListener(this)
        findViewById<View>(R.id.high_b).setOnTouchListener(this)
        findViewById<View>(R.id.high_c_s).setOnTouchListener(this)
        findViewById<View>(R.id.high_d_s).setOnTouchListener(this)
        findViewById<View>(R.id.high_f_s).setOnTouchListener(this)
        findViewById<View>(R.id.high_g_s).setOnTouchListener(this)
        findViewById<View>(R.id.high_a_s).setOnTouchListener(this)
        findViewById<View>(R.id.double_high_c).setOnTouchListener(this)
        findViewById<View>(R.id.back_arrow_view).setOnClickListener(this)
        findViewById<View>(R.id.choose_key_button).setOnClickListener(this)
        findViewById<View>(R.id.scale_highlight_button).setOnClickListener(this)
        findViewById<View>(R.id.play_tune_button).setOnClickListener(this)
        findViewById<View>(R.id.record_stop_button).setOnClickListener(this)
        findViewById<View>(R.id.open_save_button).setOnClickListener(this)
        findViewById<View>(R.id.click_button).setOnClickListener(this)
        findViewById<View>(R.id.click_button).setOnLongClickListener(this)
        val seekBar2 = findViewById<View>(R.id.seek_bar) as SeekBar
        this.seekBar = seekBar2
        seekBar2.setOnSeekBarChangeListener(this)
        this.context = applicationContext
        Companion.resources = resources
        val instance = ScalesManager.getInstance(this)
        this.scalesManager = instance
        instance.initScales(rootNote)
        scaleNames = scalesManager!!.scalesNamesArray
        val relativeLayout = findViewById<View>(R.id.notes_layout) as RelativeLayout
        this.notesLayout = relativeLayout
        relativeLayout.measure(0, 0)
        keyLabels = ArrayList()
        bitmapWhiteKey = BitmapFactory.decodeResource(Companion.resources, R.drawable.white_key)
        bitmapWhiteKeyFocused =
            BitmapFactory.decodeResource(Companion.resources, R.drawable.white_key_focused)
        bitmapWhiteKeySelected =
            BitmapFactory.decodeResource(Companion.resources, R.drawable.white_key_selected)
        bitmapBlackKey = BitmapFactory.decodeResource(Companion.resources, R.drawable.black_key)
        bitmapBlackKeyFocused =
            BitmapFactory.decodeResource(Companion.resources, R.drawable.black_key_focused)
        bitmapBlackKeySelected =
            BitmapFactory.decodeResource(Companion.resources, R.drawable.black_key_selected)
        seekBar!!.progressDrawable =
            ContextCompat.getDrawable(this, R.drawable.keyboard_400x64)
        val drawable = ContextCompat.getDrawable(this, R.drawable.thumb)
        drawable!!.alpha = 128
        seekBar!!.thumb = drawable
        cleanAllNotes(soundManager.getAllNotes())
        if (MenuActivity.isTenInchTablet) {
            strDefaultKeySize = "2.0"
        } else if (MenuActivity.isSevenInchTablet) {
            strDefaultKeySize = "1.5"
        }
        val i = phoneAdHeight
        adHeight = i
        dipAdHeight =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, i.toFloat(), resources.getDisplayMetrics()
            )
        dipHeightOfTopBar = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightOfTopBar.toFloat(),
            resources.getDisplayMetrics()
        )
        blackKeysHeightStart = heightOfTopBar + adHeight
        volumeControlStream = 3
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs = defaultSharedPreferences
        hasSubmittedMelody = defaultSharedPreferences.getBoolean(KEY_SUBMITTED_MELODY, false)
        hasSubmittedTune = sharedPrefs!!.getBoolean(KEY_SUBMITTED_TUNE, false)
        Log.v("themelodymaster", "hasSubmittedTune:" + hasSubmittedTune)
        bringInDefaultPrefs()
        window.addFlags(128)
        redMetronome = ContextCompat.getDrawable(this, R.drawable.metronome_red)
        greenMetronome = ContextCompat.getDrawable(this, R.drawable.metronome_green)
        blueMetronome = ContextCompat.getDrawable(this, R.drawable.metronome)
        clickButton = findViewById<View>(R.id.click_button) as ImageView
        this.recordStopButton = findViewById<View>(R.id.record_stop_button) as ImageView
        recordingOn = ContextCompat.getDrawable(this, R.drawable.play_btn)
        recordingOff = ContextCompat.getDrawable(this, R.drawable.pause_btn)
        this.playStopButton = findViewById<View>(R.id.play_tune_button) as ImageView
        playingOn = ContextCompat.getDrawable(this, R.drawable.play_stop_playing)
        playingOff = ContextCompat.getDrawable(this, R.drawable.play_stop)
        this.chordButtonOne = findViewById<View>(R.id.chord_button_one) as Button
        this.chordButtonTwo = findViewById<View>(R.id.chord_button_two) as Button
        this.chordButtonThree = findViewById<View>(R.id.chord_button_three) as Button
        this.chordButtonFour = findViewById<View>(R.id.chord_button_four) as Button
        this.chordButtonFive = findViewById<View>(R.id.chord_button_five) as Button
        this.chordButtonSix = findViewById<View>(R.id.chord_button_six) as Button
        this.chordButtonSeven = findViewById<View>(R.id.chord_button_seven) as Button
        chordButtonOne!!.setOnClickListener(this)
        chordButtonTwo!!.setOnClickListener(this)
        chordButtonThree!!.setOnClickListener(this)
        chordButtonFour!!.setOnClickListener(this)
        chordButtonFive!!.setOnClickListener(this)
        chordButtonSix!!.setOnClickListener(this)
        chordButtonSeven!!.setOnClickListener(this)
        requestPermissions()
    }

    public override fun onDestroy() {
        isStopDesired = true
        soundManager!!.stopClick()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onDestroy()
    }

    override fun onLongClick(view: View): Boolean {
        if (view.id == R.id.click_button) {
            soundManager!!.stopClick()
            isClicking = false
            isStopDesired = true
            disableClickButton = true
            startActivity(Intent(this, ClickActivity::class.java))
        }
        return false
    }

    override fun onMenuItemSelected(i: Int, menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.options) {
            Log.v("themelodymaster", "Options selected")
            showOptions()
            return true
        } else if (menuItem.itemId == R.id.help) {
            showHelp()
            return true
        } else if (menuItem.itemId != R.id.menu) {
            return true
        } else {
            onBackPressed()
            return true
        }
    }

    public override fun onPause() {
        Log.v("themelodymaster", "onPause called.")
        isStopDesired = true
        isClicking = false
        val soundManager2 = this.soundManager
        if (soundManager2 != null) {
            soundManager2.stopSounds()
            soundManager!!.stopClickImmediately()
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onPause()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.play_game_menu, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onProgressChanged(seekBar2: SeekBar, i: Int, z: Boolean) {
        val max = ((i.toFloat()) * 1.0f) / (seekBar2.max.toFloat())
        notesLayout!!.width
        notesLayout!!.measuredWidth
        val displayMetrics = Companion.resources!!.displayMetrics
        notesLayout!!.scrollTo(
            (max * (((((whiteKeyWidth * 22).toFloat()) * displayMetrics.density) * scaleFactor) - (displayMetrics.widthPixels.toFloat()))).toInt(),
            0
        )
    }

    override fun onRequestPermissionsResult(i: Int, strArr: Array<String>, iArr: IntArray) {
        if (i != 1) {
            return
        }
        if (iArr.size <= 0 || iArr[0] != 0) {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onResume() {
        super.onResume()
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        bringInDefaultPrefs()
        populateChordButtons()
        disableClickButton = false
    }

    public override fun onStop() {
        Log.v("themelodymaster", "onStop called.")
        isStopDesired = true
        isClicking = false
        val soundManager2 = this.soundManager
        if (soundManager2 != null) {
            soundManager2.stopSounds()
            soundManager!!.stopClickImmediately()
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onStop()
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0020, code lost:
        if (((double) r3) <= 1.0d) goto L_0x0025;
     */
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        var f: Float
        val actionIndex = motionEvent.actionIndex
        val pointerId = motionEvent.getPointerId(actionIndex)
        val actionMasked = motionEvent.actionMasked
        if (isPressureOn) {
            f = (motionEvent.pressure - 0.05f) / 0.25f
        }
        f = 1.0f
        if ((view is ImageButton) && view.getTag() != null && view.getTag().toString().length > 0) {
            if (actionMasked == 0 || actionMasked == 5) {
                Log.v(
                    "themelodymaster",
                    "onTouch - MotionEvent.ACTION_DOWN MotionEvent.ACTION_POINTER_DOWN and pointerIdx:$actionIndex pointerId:$pointerId"
                )
                this.firstPointerRawX = motionEvent.rawX
                val x = motionEvent.getX(actionIndex)
                val y = motionEvent.getY(actionIndex)
                val x2 = motionEvent.x
                motionEvent.y
                val f2 = (x + this.firstPointerRawX) - x2
                val f3 = dipHeightOfTopBar + dipAdHeight + y
                val relativeLayout = findViewById<View>(R.id.notes_layout) as RelativeLayout
                this.notesLayout = relativeLayout
                val noteFromPosition = getNoteFromPosition(f2, f3, relativeLayout.scrollX)
                playNoteByUser(noteFromPosition, f, true)
                runOnUiThread {
                    if (noteFromPosition != null) {
                        val jamActivity = this@JamActivity
                        jamActivity.selectNote(
                            jamActivity.soundManager!!.getNote(
                                noteFromPosition.tag.toString().toInt()
                            )
                        )
                    }
                }
                if (pointerId == 0) {
                    this.lastFinger1Button = noteFromPosition
                } else if (pointerId == 1) {
                    this.lastFinger2Button = noteFromPosition
                } else if (pointerId == 2) {
                    this.lastFinger3Button = noteFromPosition
                } else if (pointerId == 3) {
                    this.lastFinger4Button = noteFromPosition
                } else if (pointerId == 4) {
                    this.lastFinger5Button = noteFromPosition
                }
            } else if (actionMasked == 1 || actionMasked == 6) {
                Log.v(
                    "themelodymaster",
                    "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:$actionIndex pointerId:$pointerId"
                )
                runOnUiThread {
                    if (pointerId == 0 && this@JamActivity.lastFinger1Button != null) {
                        val jamActivity = this@JamActivity
                        jamActivity.resetState(
                            jamActivity.soundManager!!.getNote(
                                lastFinger1Button!!.tag.toString().toInt()
                            )
                        )
                    }
                    val i = pointerId
                    if (i == 1) {
                        if (this@JamActivity.lastFinger2Button != null) {
                            val jamActivity2 = this@JamActivity
                            jamActivity2.resetState(
                                jamActivity2.soundManager!!.getNote(
                                    lastFinger2Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 2) {
                        if (this@JamActivity.lastFinger3Button != null) {
                            val jamActivity3 = this@JamActivity
                            jamActivity3.resetState(
                                jamActivity3.soundManager!!.getNote(
                                    lastFinger3Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 3) {
                        if (this@JamActivity.lastFinger4Button != null) {
                            val jamActivity4 = this@JamActivity
                            jamActivity4.resetState(
                                jamActivity4.soundManager!!.getNote(
                                    lastFinger4Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 4 && this@JamActivity.lastFinger5Button != null) {
                        val jamActivity5 = this@JamActivity
                        jamActivity5.resetState(
                            jamActivity5.soundManager!!.getNote(
                                lastFinger5Button!!.tag.toString().toInt()
                            )
                        )
                    }
                }
            } else if (motionEvent.action == 2) {
                val currentTimeMillis = System.currentTimeMillis()
                if (motionEvent.action == 2 && currentTimeMillis - this.mLastTouchTime < 32) {
                    try {
                        Thread.sleep(32)
                    } catch (unused: InterruptedException) {
                    }
                }
                this.mLastTouchTime = currentTimeMillis
                val pointerCount = motionEvent.pointerCount
                for (i in 0 until pointerCount) {
                    val pointerId2 = motionEvent.getPointerId(i)
                    if (pointerId2 == 0) {
                        val noteFromPosition2 = getNoteFromPosition(
                            motionEvent.rawX, motionEvent.rawY,
                            notesLayout!!.scrollX
                        )
                        if (noteFromPosition2 !== this.lastFinger1Button) {
                            playNoteByUser(noteFromPosition2, f, true)
                            runOnUiThread {
                                if (noteFromPosition2 != null) {
                                    val jamActivity = this@JamActivity
                                    jamActivity.selectNote(
                                        jamActivity.soundManager!!.getNote(
                                            noteFromPosition2.tag.toString().toInt()
                                        )
                                    )
                                }
                                if (this@JamActivity.lastFinger1Button != null) {
                                    val jamActivity2 = this@JamActivity
                                    jamActivity2.resetState(
                                        jamActivity2.soundManager!!.getNote(
                                            lastFinger1Button!!.tag.toString()
                                                .toInt()
                                        )
                                    )
                                }
                            }
                            this.lastFinger1Button = noteFromPosition2
                        }
                    } else if (pointerId2 >= 1) {
                        this.firstPointerRawX = motionEvent.rawX
                        val x3 = motionEvent.getX(i)
                        val y2 = motionEvent.getY(i)
                        val x4 = motionEvent.x
                        motionEvent.y
                        val noteFromPosition3 = getNoteFromPosition(
                            (x3 + this.firstPointerRawX) - x4,
                            dipHeightOfTopBar + dipAdHeight + y2,
                            (findViewById<View>(R.id.notes_layout) as RelativeLayout).scrollX
                        )
                        if (pointerId2 == 1) {
                            if (noteFromPosition3 !== this.lastFinger2Button) {
                                playNoteByUser(noteFromPosition3, f, true)
                                runOnUiThread {
                                    if (this@JamActivity.lastFinger2Button != null) {
                                        val jamActivity = this@JamActivity
                                        jamActivity.resetState(
                                            jamActivity.soundManager!!.getNote(
                                                lastFinger2Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val jamActivity2 = this@JamActivity
                                        jamActivity2.selectNote(
                                            jamActivity2.soundManager!!.getNote(
                                                noteFromPosition3.tag.toString().toInt()
                                            )
                                        )
                                    }
                                }
                                this.lastFinger2Button = noteFromPosition3
                            }
                        } else if (pointerId2 == 2) {
                            if (noteFromPosition3 !== this.lastFinger3Button) {
                                playNoteByUser(noteFromPosition3, f, true)
                                runOnUiThread {
                                    if (this@JamActivity.lastFinger3Button != null) {
                                        val jamActivity = this@JamActivity
                                        jamActivity.resetState(
                                            jamActivity.soundManager!!.getNote(
                                                lastFinger3Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val jamActivity2 = this@JamActivity
                                        jamActivity2.selectNote(
                                            jamActivity2.soundManager!!.getNote(
                                                noteFromPosition3.tag.toString().toInt()
                                            )
                                        )
                                    }
                                }
                                this.lastFinger3Button = noteFromPosition3
                            }
                        } else if (pointerId2 == 3) {
                            if (noteFromPosition3 !== this.lastFinger4Button) {
                                playNoteByUser(noteFromPosition3, f, true)
                                runOnUiThread {
                                    if (this@JamActivity.lastFinger4Button != null) {
                                        val jamActivity = this@JamActivity
                                        jamActivity.resetState(
                                            jamActivity.soundManager!!.getNote(
                                                lastFinger4Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val jamActivity2 = this@JamActivity
                                        jamActivity2.selectNote(
                                            jamActivity2.soundManager!!.getNote(
                                                noteFromPosition3.tag.toString().toInt()
                                            )
                                        )
                                    }
                                }
                                this.lastFinger4Button = noteFromPosition3
                            }
                        } else if (pointerId2 == 4 && noteFromPosition3 !== this.lastFinger5Button) {
                            playNoteByUser(noteFromPosition3, f, true)
                            runOnUiThread {
                                if (this@JamActivity.lastFinger5Button != null) {
                                    val jamActivity = this@JamActivity
                                    jamActivity.resetState(
                                        jamActivity.soundManager!!.getNote(
                                            lastFinger5Button!!.tag.toString()
                                                .toInt()
                                        )
                                    )
                                }
                                if (noteFromPosition3 != null) {
                                    val jamActivity2 = this@JamActivity
                                    jamActivity2.selectNote(
                                        jamActivity2.soundManager!!.getNote(
                                            noteFromPosition3.tag.toString().toInt()
                                        )
                                    )
                                }
                            }
                            this.lastFinger5Button = noteFromPosition3
                        }
                    }
                }
            }
        }
        return false
    }

    companion object {
        var DENSITY: Float = 1.0f
        const val FILE_DIR: String = "/PianoScalesChordsJamFree"
        const val KEY_SUBMITTED_MELODY: String = "submittedMelody"
        const val KEY_SUBMITTED_TUNE: String = "submittedTune"
        const val LOG_TAG: String = "themelodymaster"
        private const val OPEN_MUSIC = "Open Music"
        private const val OPEN_RECORDING = "Open Recording"
        private const val SAVE_RECORDING = "Save Recording"
        val a: Handler = Handler()
        private const val adAdjustment = 18
        private var adHeight = 50
        private var bitmapBlackKey: Bitmap? = null
        private var bitmapBlackKeyFocused: Bitmap? = null
        private var bitmapBlackKeySelected: Bitmap? = null
        private var bitmapWhiteKey: Bitmap? = null
        private var bitmapWhiteKeyFocused: Bitmap? = null
        private var bitmapWhiteKeySelected: Bitmap? = null
        private const val blackKeyFirstHalfMargin = -21
        private const val blackKeyFirstThirdMargin = -23
        private const val blackKeyLabelMarginBottom = 25.0f
        private const val blackKeyLabelMarginLeft = 10.0f
        private const val blackKeyLabelSize = 12.0f
        private const val blackKeySecondHalfMargin = -15
        private const val blackKeySecondThirdMargin = -18
        private const val blackKeyThirdThirdMargin = -12
        private const val blackKeyWidth = 37
        private var blackKeysHeightStart = (34 + 50)
        var blueMetronome: Drawable? = null
        var chosenPathToFile: String? = null
        var clickButton: ImageView? = null
        private const val clickDelayForLight = 100
        var density: Float = 1.0f
        private var dipAdHeight = 50.0f
        private var dipHeightOfTopBar = 34.0f
        var disableClickButton: Boolean = false
        private var fBlackKeyHeightDip = 0.0f
        private var fileNames: Array<String?>? = null
        private const val fileNamesIdx = 0
        var greenMetronome: Drawable? = null
        var hapticSetting: String = "MEDIUM"
        private var hasCompetitionInfoAlreadyShownInGame = false
        var hasSubmittedMelody: Boolean = false
        var hasSubmittedTune: Boolean = false
        private const val heightOfTopBar = 34
        var iTagFirstNote: Int = 0
        var isAFlatScale: Boolean = false
        var isAutoscrollOn: Boolean = true
        var isClicking: Boolean = false
        var isHighlightAllNotesOn: Boolean = true
        var isMp3Loaded: Boolean = false
        var isPlaying: Boolean = false
        var isPressureOn: Boolean = false
        var isRecording: Boolean = false
        var isStopDesired: Boolean = false
        private var keyLabels: ArrayList<TextView>? = null
        private var mediaPlayer = MediaPlayer()
        var musicFileNameIdx: Int = 0
        var musicFileNames: MutableList<String> = ArrayList<String>()
        var musicFilePaths: MutableList<String> = ArrayList<String>()
        var noteNamesType: String = "STANDARD"
        var openSaveIdx: Int = 0
        var openSaveValues: Array<String> = arrayOf(OPEN_RECORDING, SAVE_RECORDING, OPEN_MUSIC)
        private const val phoneAdHeight = 32
        var playAlongSpeed: Int = 100
        var playAlongVolume: Int = 100
        var playingOff: Drawable? = null
        private var playingOn: Drawable? = null
        private var recordingOff: Drawable? = null
        private var recordingOn: Drawable? = null
        var redMetronome: Drawable? = null
        var resources: Resources? = null
        var rootNote: String? = "C"
        private val rootNoteArrayList = ArrayList<String>()
        var rootNoteValues: Array<String> = arrayOf(
            "C",
            "C#",
            "Db",
            "D",
            "D#",
            "Eb",
            "E",
            "F",
            "F#",
            "Gb",
            "G",
            "G#",
            "Ab",
            "A",
            "A#",
            "Bb",
            "B"
        )
        var rootNotesIdx: Int = 0
        private var scaleFactor = 1.0f
        var scaleNames: Array<String>? = null
        var scaleNamesIdx: Int = 0
        var scaleTitle: String = "C Major"
        var sharedPrefs: SharedPreferences? = null
        var strDefaultKeySize: String = "2.0"
        private const val tabletAdHeight = 50
        private const val termsAndConditions =
            "In addition to our standard T & Cs the following terms and conditions apply to this prize.The closing date for entries is the 1st October 2012. The winning entry will need to supply a paypal account for the transfer of the prize. The supplied email address will be used for correspondence regarding the prize and will be deleted after the competition.The entry must be an original piece of work and not infringe any copyright legislation. All submitted entries will become the copyright of Learn to Master and can be used in the future in any digital media which Learn to Master sees fit. There will be 1 winner who will win the full amount. By submitting an entry you agree to the Terms and Conditions."
        private const val termsAndConditionsForTune =
            "In addition to our standard T & Cs the following terms and conditions apply. The submitted tune must be an original piece of work and not infringe any copyright legislation. All submitted tunes will become the copyright of Learn to Master and can be used in the future in any digital media which Learn to Master sees fit. By submitting a tune you agree to the Terms and Conditions."
        private const val whiteKeyHeight = 350
        private const val whiteKeyLabelMarginBottom = 12.0f
        private const val whiteKeyLabelMarginLeft = 22.0f
        private const val whiteKeyLabelSize = 24.0f
        private const val whiteKeyWidth = 57

        @JvmStatic
        fun greenLight() {
            if (clickButton != null && greenMetronome != null && blueMetronome != null) {
                a.postDelayed({
                    clickButton!!.setImageDrawable(
                        greenMetronome
                    )
                }, 0)
                a.postDelayed({
                    clickButton!!.setImageDrawable(
                        blueMetronome
                    )
                }, clickDelayForLight.toLong())
            }
        }
        @JvmStatic
        fun redLight() {
            if (clickButton != null && redMetronome != null && blueMetronome != null) {
                a.postDelayed({
                    clickButton!!.setImageDrawable(
                        redMetronome
                    )
                }, 0)
                a.postDelayed({
                    clickButton!!.setImageDrawable(
                        blueMetronome
                    )
                }, clickDelayForLight.toLong())
            }
        }
    }
}
