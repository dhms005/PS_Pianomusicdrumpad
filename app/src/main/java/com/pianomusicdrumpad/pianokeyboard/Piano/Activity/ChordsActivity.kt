package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ChordManager
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.HapticManager
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager
import com.pianomusicdrumpad.pianokeyboard.Piano.models.Note
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale
import java.util.Arrays

class ChordsActivity : Activity(), View.OnClickListener, OnTouchListener,
    OnSeekBarChangeListener {
    private var actualNotesPlayed = StringBuffer()
    var chordManager: ChordManager? = null
    private var expectedNotes = StringBuffer()
    private val firstNotePrefix: String? = null
    private var firstPointerRawX = 0.0f
    private val focusedNoteInChordSet: MutableSet<Note> = HashSet<Note>()
    private val hasFacebookFailed = false
    var headerTextView: TextView? = null
    var lastFinger1Button: View? = null
    var lastFinger2Button: View? = null
    var lastFinger3Button: View? = null
    var lastFinger4Button: View? = null
    var lastFinger5Button: View? = null
    var mHapticManager: HapticManager? = null
    private var mLastTouchTime: Long = 0
    private var notesLayout: RelativeLayout? = null
    var playStopButton: ImageView? = null
    private var referenceButton: ImageView? = null
    private var seekBar: SeekBar? = null
    var setNextBreak: Boolean = false
    var soundManager: SoundManager? = null

    private var mediation: String? = null
    private val full_ad: Any? = null
    var adBannerAd: LinearLayout? = null


    override fun onStartTrackingTouch(seekBar2: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar2: SeekBar) {
    }


    inner class UiThread internal constructor() : Runnable {
        override fun run() {
            this@ChordsActivity.scrollToTag(iTagFirstNote)
        }
    }


    inner class RootNoteDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            rootNotesIdx = i
            dialogInterface.dismiss()
            rootNote = rootNoteValues[rootNotesIdx]
            chordManager!!.initChords(rootNote)
            chordNames = chordManager!!.chordNamesArray
            if (isShowPatternOn) {
                //Log.v("themelodymaster", " isShowPatternOn unhighlighting and highlighting chordNamesIdx:" + ChordsActivity.chordNamesIdx);
                this@ChordsActivity.cleanNotesInChord()
                this@ChordsActivity.focusNotesInChord()
            }
            iTagFirstNote =
                soundManager!!.getNotePosition(
                    chordManager!!.chords[chordNamesIdx].notes[0]
                )
            this@ChordsActivity.scrollToTag(iTagFirstNote)
            this@ChordsActivity.setChordTitle()
        }
    }


    inner class ChordDialog internal constructor() : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            chordNamesIdx = i
            dialogInterface.dismiss()
            if (isShowPatternOn) {
                //Log.v("themelodymaster", " isShowPatternOn unhighlighting and highlighting chordNamesIdx:" + ChordsActivity.chordNamesIdx);
                this@ChordsActivity.cleanNotesInChord()
                this@ChordsActivity.focusNotesInChord()
            }
            iTagFirstNote =
                soundManager!!.getNotePosition(
                    chordManager!!.chords[chordNamesIdx].notes[0]
                )
            this@ChordsActivity.scrollToTag(iTagFirstNote)
            this@ChordsActivity.setChordTitle()
        }
    }

    internal inner class StartThread : Thread() {
        internal inner class RunnableThread : Runnable {
            override fun run() {
                playStopButton!!.setImageDrawable(playingOff)
            }
        }

        override fun run() {
            this@ChordsActivity.playTune()
            isPlaying = false
            isStopDesired = false
            this@ChordsActivity.setNextBreak = false
            this@ChordsActivity.runOnUiThread(RunnableThread())
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
        if (noteNamesType != "NONE" || (str.contains("C") && !str.contains("b") && !str.contains("#"))) {
            if ("SOLFEGE" == noteNamesType) {
                str = chordManager!!.getSolfegeLabel(
                    str, rootNote,
                    this.soundManager, chordTitle
                )
            }
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
        isPlaying = false
        isStopDesired = false
        this.setNextBreak = false
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
        isShowPatternOn =
            sharedPrefs!!.getBoolean(MenuActivity.KEY_SHOW_PATTERN, MenuActivity.defaultShowPattern)
        rootNote = sharedPrefs!!.getString(MenuActivity.KEY_ROOT_NOTE, MenuActivity.defaultRootNote)
        if (Arrays.asList(*rootNoteValues).contains(rootNote)) {
            rootNotesIdx = Arrays.asList(*rootNoteValues).indexOf(rootNote)
        } else {
            rootNote = "C"
            rootNotesIdx = 0
        }
        setKeyboardLayout(scaleFactor)
        scrollToTag(iTagFirstNote)
    }

    private fun checkTune() {
        if (expectedNotes.length > 1) {
            val stringBuffer = this.expectedNotes
            stringBuffer.delete(stringBuffer.length - 1, expectedNotes.length)
        }
        if (actualNotesPlayed.length > 1) {
            val stringBuffer2 = this.actualNotesPlayed
            stringBuffer2.delete(stringBuffer2.length - 1, actualNotesPlayed.length)
        }
        val hashSet: HashSet<String> = HashSet<String>()
        for (str in expectedNotes.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) {
            hashSet.add(str)
        }
        val hashSet2: HashSet<String> = HashSet<String>()
        for (str2 in actualNotesPlayed.toString().split(",".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()) {
            hashSet2.add(str2)
        }
        if (hashSet != hashSet2 || isFinishing) {
            val createResultDialog = createResultDialog(false)
            createResultDialog.show()
            createResultDialog.window!!.setLayout(-1, -2)
        }
    }

    private fun cleanNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapClean(note))
    }

    fun cleanNotesInChord() {
        //Log.v("themelodymaster", "Unhighlighting all notes in chord:" + chordTitle);
        for (note in this.focusedNoteInChordSet) {
            (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapClean(note))
        }
        focusedNoteInChordSet.clear()
    }

    private fun createResultDialog(z: Boolean): AlertDialog {
        val str: String
        val inflate = View.inflate(this, R.layout.result_dialog_no_score, null)
        val linearLayout = inflate.findViewById<View>(R.id.dialog_layout) as LinearLayout
        val textView = inflate.findViewById<View>(R.id.instructionsText) as TextView
        val textView2 = inflate.findViewById<View>(R.id.actuallyPlayedText) as TextView
        val textView3 = inflate.findViewById<View>(R.id.shouldPlayText) as TextView
        if (z) {
            linearLayout.setBackgroundColor(-7996780)
            str = getString(R.string.correct)
            textView.text =
                "Congratulations. You know it. Press 'Choose Chord' to choose a different chord."
            textView2.text = " You played:" + expectedNotes.toString()
            textView3.text = "Should play:" + expectedNotes.toString()
        } else {
            linearLayout.setBackgroundColor(-2061695)
            str = getString(R.string.wrong)
            textView.text = "Unlucky. Press 'Play' and try again."
            textView2.text = " You played:" + actualNotesPlayed.toString()
            textView3.text = "Should play:" + expectedNotes.toString()
        }
        val create = AlertDialog.Builder(this).setTitle(str).setView(inflate).create()
        create.setButton(-2, "Again", object : DialogInterface.OnClickListener {
            inner class C06171 : Thread() {
                override fun run() {
                    this@ChordsActivity.playTune()
                }
            }

            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                create.dismiss()
                C06171().start()
            }
        })
        create.setButton(
            -3, "Change Root Note"
        ) { dialogInterface, i ->
            create.dismiss()
            this@ChordsActivity.setRootNote()
        }
        create.setButton(
            -1, "Change Chord"
        ) { dialogInterface, i ->
            create.dismiss()
            this@ChordsActivity.setChord()
        }
        return create
    }

    private fun focusNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapFocus(note))
    }

    fun focusNotesInChord() {
        //Log.v("themelodymaster", "Highlighting all notes in scale:" + chordTitle);
        for (note in chordManager!!.chords[chordNamesIdx].notes) {
            //Log.v("themelodymaster", "highlight drawing...");
            (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapFocus(note))
            focusedNoteInChordSet.add(note)
        }
    }

    private fun getBitmapClean(note: Note): Bitmap? {
        return if (note.name.contains("#")) bitmapBlackKey else bitmapWhiteKey
    }

    private fun getBitmapFocus(note: Note): Bitmap? {
        val isAFlatChord2 = Note.isAFlatChord(
            chordTitle
        )
        isAFlatChord = isAFlatChord2
        val chordNoteName = note.getChordNoteName(isAFlatChord2)
        return if ((chordNoteName == null || !chordNoteName.contains("#")) && !chordNoteName!!.contains(
                "b"
            )
        ) bitmapWhiteKeyFocused else bitmapBlackKeyFocused
    }

    private fun getBitmapSelected(note: Note): Bitmap? {
        return if (note.name.contains("#")) bitmapBlackKeySelected else bitmapWhiteKeySelected
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

    private fun highlightToScale() {
        cleanNotesInChord()
        chordTitle = chordNames!![chordNamesIdx]
        //Log.v("themelodymaster", "chordTitle in setTune:" + chordTitle);
        isAFlatChord = Note.isAFlatChord(chordTitle)
        focusNotesInChord()
        setHeaderText(chordTitle)
    }

    private fun playNoteByUser(view: View?, f: Float, z: Boolean) {
        if (view != null) {
            val note =
                soundManager!!.getNote(view.tag.toString().toInt())
            isAFlatChord = Note.isAFlatChord(
                chordTitle
            )
            if ("SOLFEGE" == noteNamesType) {
                val stringBuffer = this.actualNotesPlayed
                stringBuffer.append(
                    " " + chordManager!!.getSolfegeLabel(
                        note.getChordNoteName(
                            isAFlatChord
                        ), rootNote,
                        this.soundManager, chordTitle
                    ) + ","
                )
            } else {
                val stringBuffer2 = this.actualNotesPlayed
                stringBuffer2.append(" " + note.getChordNoteName(isAFlatChord) + ",")
            }
            selectNote(note)
            object : Thread() {
                /* class com.pianomusicdrumpad.pianokeyboard.Activity.ChordsActivity.AnonymousClass4 */
                override fun run() {
                    if (view != null) {
                        soundManager!!.playSound(view.tag.toString().toInt(), f, z)
                        mHapticManager!!.playHapticEffect(
                            true,
                            mHapticManager!!.getHapticEffect(note)
                        )
                    }
                }
            }.start()
        }
    }

    fun playTune() {
        //Log.v("themelodymaster", "chordNamesIdx:" + chordNamesIdx + " chordSize:" + this.chordManager.getChords().size() + " chordTitleSize:" + this.chordManager.getChordNamesArray().length);
        val chord = chordManager!!.chords[chordNamesIdx]
        this.expectedNotes = StringBuffer()
        this.actualNotesPlayed = StringBuffer()
        val notes = chord.notes
        val arrayList: ArrayList<Note> = ArrayList<Note>()
        for (note in notes) {
            arrayList.add(note)
        }
        chordTitle = chord.title
        //Log.v("themelodymaster", " Google Analytics Event chordTitle:" + chordTitle);
        val note2 = arrayList[0] as Note
        iTagFirstNote = soundManager!!.getNotePosition(note2)
        findViewById<View>(note2.rId)
        runOnUiThread(UiThread())
        var i = 1
        while (i <= arrayList.size && i <= 150) {
            if (isStopDesired || this.setNextBreak) {
                this.setNextBreak = false
            } else {
                val note3 = arrayList[i - 1] as Note
                if (isHighlightAllNotesOn) {
                    val findViewById = findViewById<View>(note3.rId)
                    runOnUiThread {
                        val chordsActivity = this@ChordsActivity
                        chordsActivity.selectNote(
                            chordsActivity.soundManager!!.getNote(
                                findViewById.tag.toString().toInt()
                            )
                        )
                    }
                }
                val soundManager2 = this.soundManager
                soundManager2!!.playSound(
                    soundManager2.getNotePosition(note3),
                    (playAlongVolume.toFloat()) / 100.0f,
                    true
                )
                isAFlatChord = Note.isAFlatChord(
                    chordTitle
                )
                if ("SOLFEGE" == noteNamesType) {
                    val stringBuffer = this.expectedNotes
                    stringBuffer.append(
                        " " + chordManager!!.getSolfegeLabel(
                            note3.getChordNoteName(
                                isAFlatChord
                            ), rootNote,
                            this.soundManager, chordTitle
                        ) + ","
                    )
                } else {
                    val stringBuffer2 = this.expectedNotes
                    stringBuffer2.append(" " + note3.getChordNoteName(isAFlatChord) + ",")
                }
                i++
            }
        }
        this.setNextBreak = false
        try {
            Thread.sleep(1000)
        } catch (unused: InterruptedException) {
        }
        val it: Iterator<*> = arrayList.iterator()
        while (it.hasNext()) {
            val note4 = it.next() as Note
            if (isHighlightAllNotesOn) {
                val imageButton = findViewById<View>(note4.rId) as ImageButton
                runOnUiThread {
                    val chordsActivity = this@ChordsActivity
                    chordsActivity.resetState(
                        chordsActivity.soundManager!!.getNote(
                            imageButton.tag.toString().toInt()
                        )
                    )
                }
            }
        }
    }

    fun resetState(note: Note) {
        if (focusedNoteInChordSet.contains(note)) {
            focusNote(note)
        } else {
            cleanNote(note)
        }
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
        } else if (i == 5) {
            val seekBar4 = this.seekBar
            seekBar4!!.progress = seekBar4.max / 10
            val seekBar5 = this.seekBar
            onProgressChanged(seekBar5!!, seekBar5.max / 10, false)
        } else if (i >= 6 && i <= 7) {
            val seekBar6 = this.seekBar
            seekBar6!!.progress = seekBar6.max / 5
            val seekBar7 = this.seekBar
            onProgressChanged(seekBar7!!, seekBar7.max / 5, false)
        } else if (i >= 8 && i <= 9) {
            val seekBar8 = this.seekBar
            seekBar8!!.progress = seekBar8.max / 4
            val seekBar9 = this.seekBar
            onProgressChanged(seekBar9!!, seekBar9.max / 4, false)
        } else if (i >= 10 && i <= 11) {
            val seekBar10 = this.seekBar
            seekBar10!!.progress = seekBar10.max / 3
            val seekBar11 = this.seekBar
            onProgressChanged(seekBar11!!, seekBar11.max / 3, false)
        } else if (i == 12) {
            val seekBar12 = this.seekBar
            seekBar12!!.progress = (seekBar12.max * 2) / 5
            val seekBar13 = this.seekBar
            onProgressChanged(seekBar13!!, (seekBar13.max * 2) / 5, false)
        } else if (i == 13 || i == 14) {
            val seekBar14 = this.seekBar
            seekBar14!!.progress = seekBar14.max / 2
            val seekBar15 = this.seekBar
            onProgressChanged(seekBar15!!, seekBar15.max / 2, false)
        } else if (i > 14) {
            val seekBar16 = this.seekBar
            seekBar16!!.progress = (seekBar16.max * 3) / 5
            val seekBar17 = this.seekBar
            onProgressChanged(seekBar17!!, (seekBar17.max * 3) / 5, false)
        }
    }

    fun selectNote(note: Note) {
        (findViewById<View>(note.rId) as ImageButton).setImageBitmap(getBitmapSelected(note))
    }

    fun setChord() {
        val create = AlertDialog.Builder(this).setTitle("Select Chord").setSingleChoiceItems(
            chordNames, chordNamesIdx, ChordDialog()
        ).create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
        }
    }

    fun setChordTitle() {
        chordTitle = chordNames!![chordNamesIdx]
        //Log.v("themelodymaster", "chordTitle in setTune:" + chordTitle);
        isAFlatChord = Note.isAFlatChord(chordTitle)
        setHeaderText(chordTitle)
        setKeyboardLayout(scaleFactor)
    }

    private fun setHeaderText(str: String) {
        runOnUiThread { //Log.v("themelodymaster", "settingText:" + str);
            headerTextView!!.text = str
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
        val applyDimension2 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyWidth.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension3 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (whiteKeyHeight.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension4 =
            (Companion.resources!!.displayMetrics.heightPixels.toFloat()) - TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((adHeight + adAdjustment) + heightOfTopBar).toFloat(),
                Companion.resources!!.displayMetrics
            )
        if (applyDimension4 < applyDimension3) {
            fBlackKeyHeightDip = applyDimension4 * 0.6f
        } else {
            fBlackKeyHeightDip = 0.6f * applyDimension3
        }
        val applyDimension5 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyFirstHalfMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension6 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeySecondHalfMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension7 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyFirstThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension8 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeySecondThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension9 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            (blackKeyThirdThirdMargin.toFloat()) * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension10 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            whiteKeyLabelMarginBottom * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension11 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            whiteKeyLabelMarginLeft * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension12 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            blackKeyLabelMarginBottom * f,
            Companion.resources!!.displayMetrics
        )
        val applyDimension13 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            blackKeyLabelMarginLeft * f,
            Companion.resources!!.displayMetrics
        )
        val f2 = whiteKeyLabelSize * f
        val f3 = blackKeyLabelSize * f
        val imageButton = findViewById<View>(R.id.bottom_c) as ImageButton
        val i = applyDimension.toInt()
        val i2 = applyDimension3.toInt()
        imageButton.layoutParams = RelativeLayout.LayoutParams(i, i2)
        val i3 = applyDimension10.toInt()
        val i4 = applyDimension11.toInt()
        addLabel(imageButton, null, "C3", f2, true, i3, 0, 0, i4)
        val imageButton2 = findViewById<View>(R.id.bottom_d) as ImageButton
        val layoutParams = RelativeLayout.LayoutParams(i, i2)
        layoutParams.addRule(1, R.id.bottom_c)
        imageButton2.layoutParams = layoutParams
        addLabel(imageButton2, imageButton, "D3", f2, true, i3, 0, 0, i4)
        val imageButton3 = findViewById<View>(R.id.bottom_e) as ImageButton
        val layoutParams2 = RelativeLayout.LayoutParams(i, i2)
        layoutParams2.addRule(1, R.id.bottom_d)
        imageButton3.layoutParams = layoutParams2
        addLabel(imageButton3, imageButton2, "E3", f2, true, i3, 0, 0, i4)
        val imageButton4 = findViewById<View>(R.id.bottom_f) as ImageButton
        val layoutParams3 = RelativeLayout.LayoutParams(i, i2)
        layoutParams3.addRule(1, R.id.bottom_e)
        imageButton4.layoutParams = layoutParams3
        addLabel(imageButton4, imageButton3, "F3", f2, true, i3, 0, 0, i4)
        val imageButton5 = findViewById<View>(R.id.bottom_g) as ImageButton
        val layoutParams4 = RelativeLayout.LayoutParams(i, i2)
        layoutParams4.addRule(1, R.id.bottom_f)
        imageButton5.layoutParams = layoutParams4
        addLabel(imageButton5, imageButton4, "G3", f2, true, i3, 0, 0, i4)
        val imageButton6 = findViewById<View>(R.id.bottom_a) as ImageButton
        val layoutParams5 = RelativeLayout.LayoutParams(i, i2)
        layoutParams5.addRule(1, R.id.bottom_g)
        imageButton6.layoutParams = layoutParams5
        addLabel(imageButton6, imageButton5, "A3", f2, true, i3, 0, 0, i4)
        val imageButton7 = findViewById<View>(R.id.bottom_b) as ImageButton
        val layoutParams6 = RelativeLayout.LayoutParams(i, i2)
        layoutParams6.addRule(1, R.id.bottom_a)
        imageButton7.layoutParams = layoutParams6
        addLabel(imageButton7, imageButton6, "B3", f2, true, i3, 0, 0, i4)
        val imageButton8 = findViewById<View>(R.id.middle_c) as ImageButton
        val layoutParams7 = RelativeLayout.LayoutParams(i, i2)
        layoutParams7.addRule(1, R.id.bottom_b)
        imageButton8.layoutParams = layoutParams7
        addLabel(imageButton8, imageButton7, "C4", f2, true, i3, 0, 0, i4)
        val imageButton9 = findViewById<View>(R.id.middle_d) as ImageButton
        val layoutParams8 = RelativeLayout.LayoutParams(i, i2)
        layoutParams8.addRule(1, R.id.middle_c)
        imageButton9.layoutParams = layoutParams8
        addLabel(imageButton9, imageButton8, "D4", f2, true, i3, 0, 0, i4)
        val imageButton10 = findViewById<View>(R.id.middle_e) as ImageButton
        val layoutParams9 = RelativeLayout.LayoutParams(i, i2)
        layoutParams9.addRule(1, R.id.middle_d)
        imageButton10.layoutParams = layoutParams9
        addLabel(imageButton10, imageButton9, "E4", f2, true, i3, 0, 0, i4)
        val imageButton11 = findViewById<View>(R.id.middle_f) as ImageButton
        val layoutParams10 = RelativeLayout.LayoutParams(i, i2)
        layoutParams10.addRule(1, R.id.middle_e)
        imageButton11.layoutParams = layoutParams10
        addLabel(imageButton11, imageButton10, "F4", f2, true, i3, 0, 0, i4)
        val imageButton12 = findViewById<View>(R.id.middle_g) as ImageButton
        val layoutParams11 = RelativeLayout.LayoutParams(i, i2)
        layoutParams11.addRule(1, R.id.middle_f)
        imageButton12.layoutParams = layoutParams11
        addLabel(imageButton12, imageButton11, "G4", f2, true, i3, 0, 0, i4)
        val imageButton13 = findViewById<View>(R.id.middle_a) as ImageButton
        val layoutParams12 = RelativeLayout.LayoutParams(i, i2)
        layoutParams12.addRule(1, R.id.middle_g)
        imageButton13.layoutParams = layoutParams12
        addLabel(imageButton13, imageButton12, "A4", f2, true, i3, 0, 0, i4)
        val imageButton14 = findViewById<View>(R.id.middle_b) as ImageButton
        val layoutParams13 = RelativeLayout.LayoutParams(i, i2)
        layoutParams13.addRule(1, R.id.middle_a)
        imageButton14.layoutParams = layoutParams13
        addLabel(imageButton14, imageButton13, "B4", f2, true, i3, 0, 0, i4)
        val imageButton15 = findViewById<View>(R.id.high_c) as ImageButton
        val layoutParams14 = RelativeLayout.LayoutParams(i, i2)
        layoutParams14.addRule(1, R.id.middle_b)
        imageButton15.layoutParams = layoutParams14
        addLabel(imageButton15, imageButton14, "C5", f2, true, i3, 0, 0, i4)
        val imageButton16 = findViewById<View>(R.id.high_d) as ImageButton
        val layoutParams15 = RelativeLayout.LayoutParams(i, i2)
        layoutParams15.addRule(1, R.id.high_c)
        imageButton16.layoutParams = layoutParams15
        addLabel(imageButton16, imageButton15, "D5", f2, true, i3, 0, 0, i4)
        val imageButton17 = findViewById<View>(R.id.high_e) as ImageButton
        val layoutParams16 = RelativeLayout.LayoutParams(i, i2)
        layoutParams16.addRule(1, R.id.high_d)
        imageButton17.layoutParams = layoutParams16
        addLabel(imageButton17, imageButton16, "E5", f2, true, i3, 0, 0, i4)
        val imageButton18 = findViewById<View>(R.id.high_f) as ImageButton
        val layoutParams17 = RelativeLayout.LayoutParams(i, i2)
        layoutParams17.addRule(1, R.id.high_e)
        imageButton18.layoutParams = layoutParams17
        addLabel(imageButton18, imageButton17, "F5", f2, true, i3, 0, 0, i4)
        val imageButton19 = findViewById<View>(R.id.high_g) as ImageButton
        val layoutParams18 = RelativeLayout.LayoutParams(i, i2)
        layoutParams18.addRule(1, R.id.high_f)
        imageButton19.layoutParams = layoutParams18
        addLabel(imageButton19, imageButton18, "G5", f2, true, i3, 0, 0, i4)
        val imageButton20 = findViewById<View>(R.id.high_a) as ImageButton
        val layoutParams19 = RelativeLayout.LayoutParams(i, i2)
        layoutParams19.addRule(1, R.id.high_g)
        imageButton20.layoutParams = layoutParams19
        addLabel(imageButton20, imageButton19, "A5", f2, true, i3, 0, 0, i4)
        val imageButton21 = findViewById<View>(R.id.high_b) as ImageButton
        val layoutParams20 = RelativeLayout.LayoutParams(i, i2)
        layoutParams20.addRule(1, R.id.high_a)
        imageButton21.layoutParams = layoutParams20
        addLabel(imageButton21, imageButton20, "B5", f2, true, i3, 0, 0, i4)
        val imageButton22 = findViewById<View>(R.id.double_high_c) as ImageButton
        val layoutParams21 = RelativeLayout.LayoutParams(i, i2)
        layoutParams21.addRule(1, R.id.high_b)
        imageButton22.layoutParams = layoutParams21
        addLabel(imageButton22, imageButton21, "C6", f2, true, i3, 0, 0, i4)
        val imageButton23 = findViewById<View>(R.id.bottom_c_s) as ImageButton
        val i5 = applyDimension2.toInt()
        val layoutParams22 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams22.addRule(1, R.id.bottom_c)
        val i6 = applyDimension5.toInt()
        layoutParams22.setMargins(i6, 0, 0, 0)
        imageButton23.layoutParams = layoutParams22
        val i7 = applyDimension13.toInt()
        val i8 = i6 + i7
        val i9 = applyDimension12.toInt()
        addLabel(
            imageButton23,
            imageButton,
            if (!isAFlatChord) "C#3" else "Db3",
            f3,
            false,
            i8,
            0,
            0,
            i9
        )
        val imageButton24 = findViewById<View>(R.id.bottom_d_s) as ImageButton
        val layoutParams23 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams23.addRule(1, R.id.bottom_d)
        val i10 = applyDimension6.toInt()
        layoutParams23.setMargins(i10, 0, 0, 0)
        imageButton24.layoutParams = layoutParams23
        val i11 = i10 + i7
        addLabel(
            imageButton24,
            imageButton2,
            if (!isAFlatChord) "D#3" else "Eb3",
            f3,
            false,
            i11,
            0,
            0,
            i9
        )
        val imageButton25 = findViewById<View>(R.id.bottom_f_s) as ImageButton
        val layoutParams24 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams24.addRule(1, R.id.bottom_f)
        val i12 = applyDimension7.toInt()
        layoutParams24.setMargins(i12, 0, 0, 0)
        imageButton25.layoutParams = layoutParams24
        val i13 = i12 + i7
        addLabel(
            imageButton25,
            imageButton4,
            if (!isAFlatChord) "F#3" else "Gb3",
            f3,
            false,
            i13,
            0,
            0,
            i9
        )
        val imageButton26 = findViewById<View>(R.id.bottom_g_s) as ImageButton
        val layoutParams25 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams25.addRule(1, R.id.bottom_g)
        val i14 = applyDimension8.toInt()
        layoutParams25.setMargins(i14, 0, 0, 0)
        imageButton26.layoutParams = layoutParams25
        val i15 = i14 + i7
        addLabel(
            imageButton26,
            imageButton5,
            if (!isAFlatChord) "G#3" else "Ab3",
            f3,
            false,
            i15,
            0,
            0,
            i9
        )
        val imageButton27 = findViewById<View>(R.id.bottom_a_s) as ImageButton
        val layoutParams26 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams26.addRule(1, R.id.bottom_a)
        val i16 = applyDimension9.toInt()
        layoutParams26.setMargins(i16, 0, 0, 0)
        imageButton27.layoutParams = layoutParams26
        val i17 = i7 + i16
        addLabel(
            imageButton27,
            imageButton6,
            if (!isAFlatChord) "A#3" else "Bb3",
            f3,
            false,
            i17,
            0,
            0,
            i9
        )
        val imageButton28 = findViewById<View>(R.id.middle_c_s) as ImageButton
        val layoutParams27 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams27.addRule(1, R.id.middle_c)
        layoutParams27.setMargins(i6, 0, 0, 0)
        imageButton28.layoutParams = layoutParams27
        addLabel(
            imageButton28,
            imageButton8,
            if (!isAFlatChord) "C#4" else "Db4",
            f3,
            false,
            i8,
            0,
            0,
            i9
        )
        val imageButton29 = findViewById<View>(R.id.middle_d_s) as ImageButton
        val layoutParams28 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams28.addRule(1, R.id.middle_d)
        layoutParams28.setMargins(i10, 0, 0, 0)
        imageButton29.layoutParams = layoutParams28
        addLabel(
            imageButton29,
            imageButton9,
            if (!isAFlatChord) "D#4" else "Eb4",
            f3,
            false,
            i11,
            0,
            0,
            i9
        )
        val imageButton30 = findViewById<View>(R.id.middle_f_s) as ImageButton
        val layoutParams29 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams29.addRule(1, R.id.middle_f)
        layoutParams29.setMargins(i12, 0, 0, 0)
        imageButton30.layoutParams = layoutParams29
        addLabel(
            imageButton30,
            imageButton11,
            if (!isAFlatChord) "F#4" else "Gb4",
            f3,
            false,
            i13,
            0,
            0,
            i9
        )
        val imageButton31 = findViewById<View>(R.id.middle_g_s) as ImageButton
        val layoutParams30 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams30.addRule(1, R.id.middle_g)
        layoutParams30.setMargins(i14, 0, 0, 0)
        imageButton31.layoutParams = layoutParams30
        addLabel(
            imageButton31,
            imageButton12,
            if (!isAFlatChord) "G#4" else "Ab4",
            f3,
            false,
            i15,
            0,
            0,
            i9
        )
        val imageButton32 = findViewById<View>(R.id.middle_a_s) as ImageButton
        val layoutParams31 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams31.addRule(1, R.id.middle_a)
        layoutParams31.setMargins(i16, 0, 0, 0)
        imageButton32.layoutParams = layoutParams31
        addLabel(
            imageButton32,
            imageButton13,
            if (!isAFlatChord) "A#4" else "Bb4",
            f3,
            false,
            i17,
            0,
            0,
            i9
        )
        val imageButton33 = findViewById<View>(R.id.high_c_s) as ImageButton
        val layoutParams32 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams32.addRule(1, R.id.high_c)
        layoutParams32.setMargins(i6, 0, 0, 0)
        imageButton33.layoutParams = layoutParams32
        addLabel(
            imageButton33,
            imageButton15,
            if (!isAFlatChord) "C#5" else "Db5",
            f3,
            false,
            i8,
            0,
            0,
            i9
        )
        val imageButton34 = findViewById<View>(R.id.high_d_s) as ImageButton
        val layoutParams33 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams33.addRule(1, R.id.high_d)
        layoutParams33.setMargins(i10, 0, 0, 0)
        imageButton34.layoutParams = layoutParams33
        addLabel(
            imageButton34,
            imageButton16,
            if (!isAFlatChord) "D#5" else "Eb5",
            f3,
            false,
            i11,
            0,
            0,
            i9
        )
        val imageButton35 = findViewById<View>(R.id.high_f_s) as ImageButton
        val layoutParams34 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams34.addRule(1, R.id.high_f)
        layoutParams34.setMargins(i12, 0, 0, 0)
        imageButton35.layoutParams = layoutParams34
        addLabel(
            imageButton35,
            imageButton18,
            if (!isAFlatChord) "F#5" else "Gb5",
            f3,
            false,
            i13,
            0,
            0,
            i9
        )
        val imageButton36 = findViewById<View>(R.id.high_g_s) as ImageButton
        val layoutParams35 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams35.addRule(1, R.id.high_g)
        layoutParams35.setMargins(i14, 0, 0, 0)
        imageButton36.layoutParams = layoutParams35
        addLabel(
            imageButton36,
            imageButton19,
            if (!isAFlatChord) "G#5" else "Ab5",
            f3,
            false,
            i15,
            0,
            0,
            i9
        )
        val imageButton37 = findViewById<View>(R.id.high_a_s) as ImageButton
        val layoutParams36 = RelativeLayout.LayoutParams(i5, fBlackKeyHeightDip.toInt())
        layoutParams36.addRule(1, R.id.high_a)
        layoutParams36.setMargins(i16, 0, 0, 0)
        imageButton37.layoutParams = layoutParams36
        addLabel(
            imageButton37,
            imageButton20,
            if (!isAFlatChord) "A#5" else "Bb5",
            f3,
            false,
            i17,
            0,
            0,
            i9
        )
    }

    fun setRootNote() {
        val create = AlertDialog.Builder(this).setTitle("Select Root Note").setSingleChoiceItems(
            rootNoteValues, rootNotesIdx, RootNoteDialog()
        ).create()
        if (!isFinishing) {
            create.listView.isFastScrollEnabled = true
            create.show()
        }
    }

    private fun showHelp() {
        startActivity(Intent(this, ChordsHelpActivity::class.java))
    }

    private fun showOptions() {
        isStopDesired = true
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        } else if (view.id == R.id.choose_key_button) {
            setRootNote()
        } else if (view.id == R.id.choose_tune_button) {
            setChord()
        } else if (view.id == R.id.play_tune_button) {
            if (isPlaying) {
                isStopDesired = true
                return
            }
            isPlaying = true
            playStopButton!!.setImageDrawable(playingOn)
            StartThread().start()
        } else if (view.id == R.id.check_button) {
            checkTune()
        } else if (view.id != R.id.reference_button) {
        } else {
            if (isShowPatternOn) {
                isShowPatternOn = false
                referenceButton!!.setImageDrawable(referenceOff)
                val edit = sharedPrefs!!.edit()
                edit.putBoolean(MenuActivity.KEY_SHOW_PATTERN, false)
                edit.apply()
                cleanNotesInChord()
                return
            }
            isShowPatternOn = true
            referenceButton!!.setImageDrawable(referenceOn)
            val edit2 = sharedPrefs!!.edit()
            edit2.putBoolean(MenuActivity.KEY_SHOW_PATTERN, true)
            edit2.apply()
            cleanNotesInChord()
            focusNotesInChord()
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
        setContentView(R.layout.chords)

        mediation = getString(ConstantAd.MEDIATION, "0")

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
        findViewById<View>(R.id.choose_tune_button).setOnClickListener(this)
        findViewById<View>(R.id.play_tune_button).setOnClickListener(this)
        findViewById<View>(R.id.check_button).setOnClickListener(this)
        findViewById<View>(R.id.reference_button).setOnClickListener(this)
        val seekBar2 = findViewById<View>(R.id.seek_bar) as SeekBar
        this.seekBar = seekBar2
        seekBar2.setOnSeekBarChangeListener(this)
        Companion.resources = resources
        val instance = ChordManager.getInstance(this)
        this.chordManager = instance
        instance.initChords(rootNote)
        chordNames = chordManager!!.chordNamesArray
        val relativeLayout = findViewById<View>(R.id.notes_layout) as RelativeLayout
        this.notesLayout = relativeLayout
        relativeLayout.measure(0, 0)
        keyLabels = ArrayList()
        seekBar!!.progressDrawable =
            ContextCompat.getDrawable(this, R.drawable.keyboard_400x64)
        val drawable = ContextCompat.getDrawable(this, R.drawable.thumb)
        drawable!!.alpha = 128
        seekBar!!.thumb = drawable
        this.headerTextView = findViewById<View>(R.id.headerText) as TextView
        setHeaderText("Piano Chords")
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
        if (MenuActivity.isTenInchTablet) {
            adHeight = tabletTenAdHeight
            strDefaultKeySize = "2.0"
            headerTextView!!.setTextSize(2, 24.0f)
            headerTextView!!.setPadding(
                TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 740.0f,resources.getDisplayMetrics())
                    .toInt(),
                TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20.0f, resources.getDisplayMetrics())
                    .toInt(),
                headerTextView!!.paddingRight,
                headerTextView!!.paddingBottom
            )
        } else if (MenuActivity.isSevenInchTablet) {
            adHeight = phoneAdHeight
            strDefaultKeySize = "1.5"
        } else {
            adHeight = phoneAdHeight
        }
        dipAdHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            adHeight.toFloat(),
            resources.getDisplayMetrics()
        )
        dipHeightOfTopBar = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
            heightOfTopBar.toFloat(),
            resources.getDisplayMetrics()
        )
        blackKeysHeightStart = heightOfTopBar + adHeight
        RelativeLayout.LayoutParams(-2, -2).addRule(5)
        volumeControlStream = 3
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        bringInDefaultPrefs()
        window.addFlags(128)
        this.playStopButton = findViewById<View>(R.id.play_tune_button) as ImageView
        playingOn = ContextCompat.getDrawable(this, R.drawable.play_stop_playing)
        playingOff = ContextCompat.getDrawable(this, R.drawable.play_stop)
        this.referenceButton = findViewById<View>(R.id.reference_button) as ImageView
        referenceOn = ContextCompat.getDrawable(this, R.drawable.reference_on)
        referenceOff = ContextCompat.getDrawable(this, R.drawable.reference)
    }

    public override fun onDestroy() {
        isStopDesired = true
        super.onDestroy()
    }

    override fun onMenuItemSelected(i: Int, menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.options) {
            //Log.v("themelodymaster", "Options selected");
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
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        isStopDesired = true
        val soundManager2 = this.soundManager
        soundManager2?.stopSounds()
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

    public override fun onResume() {
        super.onResume()
        putString(ConstantAd.AD_CHECK_RESUME, "1")
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        bringInDefaultPrefs()
        chordManager!!.initChords(rootNote)
        chordNames = chordManager!!.chordNamesArray
        setChordTitle()
        if ((scaleFactor.toDouble()) <= 0.5) {
            scrollToTag(1)
        } else {
            scrollToTag(iTagFirstNote)
        }
        if (isShowPatternOn) {
            highlightToScale()
            referenceButton!!.setImageDrawable(referenceOn)
            return
        }
        cleanNotesInChord()
        referenceButton!!.setImageDrawable(referenceOff)
    }

    public override fun onStop() {
        //Log.v("themelodymaster", "onStop called.");
        isStopDesired = true
        val soundManager2 = this.soundManager
        soundManager2?.stopSounds()
        super.onStop()
    }

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
                //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_DOWN MotionEvent.ACTION_POINTER_DOWN and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
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
                        val chordsActivity = this@ChordsActivity
                        chordsActivity.selectNote(
                            chordsActivity.soundManager!!.getNote(
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
                //Log.v("themelodymaster", "onTouch - MotionEvent.ACTION_UP MotionEvent.ACTION_POINTER_UP and pointerIdx:" + actionIndex + " pointerId:" + pointerId);
                runOnUiThread {
                    if (pointerId == 0 && this@ChordsActivity.lastFinger1Button != null) {
                        val chordsActivity = this@ChordsActivity
                        chordsActivity.resetState(
                            chordsActivity.soundManager!!.getNote(
                                lastFinger1Button!!.tag.toString().toInt()
                            )
                        )
                    }
                    val i = pointerId
                    if (i == 1) {
                        if (this@ChordsActivity.lastFinger2Button != null) {
                            val chordsActivity2 = this@ChordsActivity
                            chordsActivity2.resetState(
                                chordsActivity2.soundManager!!.getNote(
                                    lastFinger2Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 2) {
                        if (this@ChordsActivity.lastFinger3Button != null) {
                            val chordsActivity3 = this@ChordsActivity
                            chordsActivity3.resetState(
                                chordsActivity3.soundManager!!.getNote(
                                    lastFinger3Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 3) {
                        if (this@ChordsActivity.lastFinger4Button != null) {
                            val chordsActivity4 = this@ChordsActivity
                            chordsActivity4.resetState(
                                chordsActivity4.soundManager!!.getNote(
                                    lastFinger4Button!!.tag.toString().toInt()
                                )
                            )
                        }
                    } else if (i == 4 && this@ChordsActivity.lastFinger5Button != null) {
                        val chordsActivity5 = this@ChordsActivity
                        chordsActivity5.resetState(
                            chordsActivity5.soundManager!!.getNote(
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
                                    val chordsActivity = this@ChordsActivity
                                    chordsActivity.selectNote(
                                        chordsActivity.soundManager!!.getNote(
                                            noteFromPosition2.tag.toString().toInt()
                                        )
                                    )
                                }
                                if (this@ChordsActivity.lastFinger1Button != null) {
                                    val chordsActivity2 = this@ChordsActivity
                                    chordsActivity2.resetState(
                                        chordsActivity2.soundManager!!.getNote(
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
                                    if (this@ChordsActivity.lastFinger2Button != null) {
                                        val chordsActivity = this@ChordsActivity
                                        chordsActivity.resetState(
                                            chordsActivity.soundManager!!.getNote(
                                                lastFinger2Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val chordsActivity2 = this@ChordsActivity
                                        chordsActivity2.selectNote(
                                            chordsActivity2.soundManager!!.getNote(
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
                                    if (this@ChordsActivity.lastFinger3Button != null) {
                                        val chordsActivity = this@ChordsActivity
                                        chordsActivity.resetState(
                                            chordsActivity.soundManager!!.getNote(
                                                lastFinger3Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val chordsActivity2 = this@ChordsActivity
                                        chordsActivity2.selectNote(
                                            chordsActivity2.soundManager!!.getNote(
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
                                    if (this@ChordsActivity.lastFinger4Button != null) {
                                        val chordsActivity = this@ChordsActivity
                                        chordsActivity.resetState(
                                            chordsActivity.soundManager!!.getNote(
                                                lastFinger4Button!!.tag.toString()
                                                    .toInt()
                                            )
                                        )
                                    }
                                    if (noteFromPosition3 != null) {
                                        val chordsActivity2 = this@ChordsActivity
                                        chordsActivity2.selectNote(
                                            chordsActivity2.soundManager!!.getNote(
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
                                if (this@ChordsActivity.lastFinger5Button != null) {
                                    val chordsActivity = this@ChordsActivity
                                    chordsActivity.resetState(
                                        chordsActivity.soundManager!!.getNote(
                                            lastFinger5Button!!.tag.toString()
                                                .toInt()
                                        )
                                    )
                                }
                                if (noteFromPosition3 != null) {
                                    val chordsActivity2 = this@ChordsActivity
                                    chordsActivity2.selectNote(
                                        chordsActivity2.soundManager!!.getNote(
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


    override fun onBackPressed() {
        finish()
    }

    companion object {
        const val DURATION: Int = 400
        private const val HIGHLIGHT_HEIGHT = 0
        const val LOG_TAG: String = "themelodymaster"
        var ON_MEASURE_HEIGHT: Int = 0
        var ON_MEASURE_WIDTH: Int = 0
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
        var chordNames: Array<String>? = null
        var chordNamesIdx: Int = 0
        var chordTitle: String = "C Major"
        var density: Float = 1.0f
        private var dipAdHeight = 50.0f
        private var dipHeightOfTopBar = 34.0f
        private var fBlackKeyHeightDip = 0.0f
        var hapticSetting: String = "MEDIUM"
        private const val heightOfTopBar = 34
        var iTagFirstNote: Int = 0
        var isAFlatChord: Boolean = false
        var isAutoscrollOn: Boolean = true
        var isHighlightAllNotesOn: Boolean = true
        var isPlaying: Boolean = false
        var isPressureOn: Boolean = false
        var isShowPatternOn: Boolean = false
        var isStopDesired: Boolean = false
        private var keyLabels: ArrayList<TextView>? = null
        var noteNamesType: String = "STANDARD"
        private const val phoneAdHeight = 50
        var playAlongSpeed: Int = 100
        var playAlongVolume: Int = 100
        var playingOff: Drawable? = null
        private var playingOn: Drawable? = null
        private var referenceOff: Drawable? = null
        private var referenceOn: Drawable? = null
        var resources: Resources? = null
        var rootNote: String? = "C"
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
        private var sharedPrefs: SharedPreferences? = null
        var strDefaultKeySize: String = "1.1"
        private const val tabletTenAdHeight = 90
        private const val whiteKeyHeight = 350
        private const val whiteKeyLabelMarginBottom = 12.0f
        private const val whiteKeyLabelMarginLeft = 22.0f
        private const val whiteKeyLabelSize = 24.0f
        private const val whiteKeyWidth = 57
    }
}
