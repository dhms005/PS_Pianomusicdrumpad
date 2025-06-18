package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager
import com.pianomusicdrumpad.pianokeyboard.R
import java.util.Arrays

class ClickActivity : Activity(), View.OnClickListener, OnLongClickListener,
    OnSeekBarChangeListener {
    var beatTextView: TextView? = null
    private var bpmTextView: TextView? = null
    private var changeBeatButton: Button? = null
    private var changeSoundButton: Button? = null
    private var clickVolumeTextView: TextView? = null
    var delayMsecs: Long = 50
    private var isDecrementing = false
    var isDecrementingRunnable: Runnable? = null
    private var isIncrementing = false
    var isIncrementingRunnable: Runnable? = null
    var mHandler: Handler? = null
    private var minusButton: Button? = null
    private var plusButton: Button? = null
    private var resources: Resources? = null
    lateinit var soundManager: SoundManager
    var soundTextView: TextView? = null
    private var volumeSeekBar: SeekBar? = null

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    internal inner class IncrementingRunnable : Runnable {
        override fun run() {
            this@ClickActivity.incrementBpm()
            mHandler!!.postDelayed(isIncrementingRunnable!!, this@ClickActivity.delayMsecs)
        }
    }

    internal inner class DecrementingRunnable : Runnable {
        override fun run() {
            this@ClickActivity.decrementBpm()
            mHandler!!.postDelayed(isDecrementingRunnable!!, this@ClickActivity.delayMsecs)
        }
    }

    internal inner class changeBeatDialog : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            beatIdx = i
            dialogInterface.dismiss()
            val str = beatValues[beatIdx]
            beatTextView!!.text = str
            clickBeatsInBar = str.split("-".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
            soundManager!!.updateClick(
                clickBpm,
                clickVolume,
                clickBeatsInBar,
                sound,
                javaClass.simpleName
            )
        }
    }

    internal inner class changeSoundDialog : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            soundIdx = i
            dialogInterface.dismiss()
            sound = soundValues[soundIdx]
            soundTextView!!.text = sound
            soundManager!!.updateClick(
                clickBpm,
                clickVolume,
                clickBeatsInBar,
                sound,
                javaClass.simpleName
            )
        }
    }

    private fun bringInDefaultPrefs() {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs = defaultSharedPreferences
        clickBpm = defaultSharedPreferences.getString(
            MenuActivity.KEY_CLICK_BPM,
            "100"
        )!!
            .toInt()
        clickVolume = sharedPrefs!!.getString(
            MenuActivity.KEY_CLICK_VOLUME,
            "0.5"
        )!!
            .toFloat()
        val string =
            sharedPrefs!!.getString(MenuActivity.KEY_CLICK_BEAT, "4-4")!!
        clickBeatsInBar = string.split("-".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
        beatIdx = Arrays.asList(*beatValues).indexOf(string)
        sound = sharedPrefs!!.getString(MenuActivity.KEY_CLICK_SOUND, SoundManager.SOUND_CLICK)
        soundIdx = Arrays.asList(*soundValues).indexOf(sound)
        val textView = this.bpmTextView
        textView!!.text = "" + clickBpm
        val textView2 = this.clickVolumeTextView
        textView2!!.text = "" + ((clickVolume * 100.0f).toInt()) + "%"
        volumeSeekBar!!.progress =
            (clickVolume * 100.0f).toInt()
        beatTextView!!.text = string
        soundTextView!!.text = sound
    }

    private fun changeBeat() {
        val create = AlertDialog.Builder(this).setTitle(getString(R.string.select_beat))
            .setSingleChoiceItems(
                beatValues, beatIdx, changeBeatDialog()
            ).create()
        if (!isFinishing) {
            create.show()
        }
    }

    private fun changeSound() {
        val create = AlertDialog.Builder(this).setTitle(getString(R.string.select_sound))
            .setSingleChoiceItems(
                soundValues, soundIdx, changeSoundDialog()
            ).create()
        if (!isFinishing) {
            create.show()
        }
    }

    fun decrementBpm() {
        val i = clickBpm
        if (i > 20) {
            clickBpm = i - 1
            val textView = this.bpmTextView
            textView!!.text = "" + clickBpm
            soundManager!!.updateClick(
                clickBpm,
                clickVolume,
                clickBeatsInBar,
                sound,
                javaClass.simpleName
            )
        }
    }

    fun incrementBpm() {
        val i = clickBpm
        if (i < 400) {
            clickBpm = i + 1
            val textView = this.bpmTextView
            textView!!.text = "" + clickBpm
            soundManager!!.updateClick(
                clickBpm,
                clickVolume,
                clickBeatsInBar,
                sound,
                javaClass.simpleName
            )
        }
    }

    override fun onBackPressed() {
        Log.v("themelodymaster", "onBackPressed pressed. Closing app and releasing soundPool")
        super.onBackPressed()
        val edit = sharedPrefs!!.edit()
        edit.putString(MenuActivity.KEY_CLICK_BPM, "" + clickBpm)
        edit.putString(MenuActivity.KEY_CLICK_VOLUME, "" + clickVolume)
        edit.putString(
            MenuActivity.KEY_CLICK_BEAT,
            beatValues[beatIdx]
        )
        edit.putString(
            MenuActivity.KEY_CLICK_SOUND,
            soundValues[soundIdx]
        )
        edit.apply()
        soundManager!!.stopClick()
    }

    override fun onClick(view: View) {
        if (this.isIncrementing) {
            mHandler!!.removeCallbacks(isIncrementingRunnable!!)
            this.isIncrementing = false
        } else if (this.isDecrementing) {
            mHandler!!.removeCallbacks(isDecrementingRunnable!!)
            this.isDecrementing = false
        }
        if (view.id == R.id.minus_button) {
            decrementBpm()
        } else if (view.id == R.id.plus_button) {
            incrementBpm()
        } else if (view.id == R.id.change_beat_button) {
            changeBeat()
        } else if (view.id == R.id.change_sound_button) {
            changeSound()
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        setContentView(R.layout.click)
        this.bpmTextView = findViewById<View>(R.id.bpmText) as TextView
        val button = findViewById<View>(R.id.minus_button) as Button
        this.minusButton = button
        button.setOnClickListener(this)
        minusButton!!.setOnLongClickListener(this)
        val button2 = findViewById<View>(R.id.plus_button) as Button
        this.plusButton = button2
        button2.setOnClickListener(this)
        plusButton!!.setOnLongClickListener(this)
        val seekBar = findViewById<View>(R.id.click_volume_seek_bar) as SeekBar
        this.volumeSeekBar = seekBar
        seekBar.setOnSeekBarChangeListener(this)
        this.clickVolumeTextView = findViewById<View>(R.id.click_volume_text) as TextView
        this.beatTextView = findViewById<View>(R.id.beat_text) as TextView
        val button3 = findViewById<View>(R.id.change_beat_button) as Button
        this.changeBeatButton = button3
        button3.setOnClickListener(this)
        this.soundTextView = findViewById<View>(R.id.sound_text) as TextView
        val button4 = findViewById<View>(R.id.change_sound_button) as Button
        this.changeSoundButton = button4
        button4.setOnClickListener(this)
        this.mHandler = Handler()
        this.isIncrementingRunnable = IncrementingRunnable()
        this.isDecrementingRunnable = DecrementingRunnable()
        this.resources = getResources()
        this.soundManager = SoundManager.getInstance(this)
        bringInDefaultPrefs()
        soundManager.startClick(this, javaClass.simpleName)
        window.addFlags(128)
    }

    public override fun onDestroy() {
        Log.v("themelodymaster", "onDestroy selected")
        super.onDestroy()
        val soundManager2 = this.soundManager
        soundManager2?.stopClick()
    }

    override fun onLongClick(view: View): Boolean {
        if (view.id == R.id.minus_button) {
            mHandler!!.post(isDecrementingRunnable!!)
            this.isDecrementing = true
            return false
        } else if (view.id != R.id.plus_button) {
            return false
        } else {
            mHandler!!.post(isIncrementingRunnable!!)
            this.isIncrementing = true
            return false
        }
    }

    public override fun onPause() {
        Log.v("themelodymaster", "onPause selected")
        soundManager!!.stopClickImmediately()
        super.onPause()
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
        clickVolume = ((i.toFloat()) * 1.0f) / (seekBar.max.toFloat())
        val textView = this.clickVolumeTextView
        textView!!.text = "" + ((clickVolume * 100.0f).toInt()) + "%"
        soundManager!!.updateClick(
            clickBpm,
            clickVolume,
            clickBeatsInBar,
            sound,
            javaClass.simpleName
        )
    }

    public override fun onResume() {
        super.onResume()
        bringInDefaultPrefs()
    }

    public override fun onStart() {
        super.onStart()
    }

    public override fun onStop() {
        Log.v("themelodymaster", "onStop selected")
        soundManager!!.stopClickImmediately()
        super.onStop()
    }

    companion object {
        const val LOG_TAG: String = "themelodymaster"
        var beatIdx: Int = 0
        var beatValues: Array<String> = arrayOf(
            "4-4",
            "0-4",
            "2-4",
            "3-4",
            "5-4",
            "6-4",
            "7-4",
            "2-2",
            "3-8",
            "6-8",
            "9-8",
            "12-8"
        )
        var clickBeatsInBar: Int = 4
        var clickBpm: Int = 100
        var clickVolume: Float = 0.5f
        private const val maxClickBpm = 400
        private const val minClickBpm = 20
        private var sharedPrefs: SharedPreferences? = null
        var sound: String? = "Click"
        var soundIdx: Int = 0
        var soundValues: Array<String> = arrayOf(
            SoundManager.SOUND_LOW_VIBRATION,
            SoundManager.SOUND_VIBRATION,
            SoundManager.SOUND_TRIANGLE,
            SoundManager.SOUND_TICK,
            SoundManager.SOUND_CLICK,
            SoundManager.SOUND_SNARE
        )
    }
}
