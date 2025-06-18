package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.ProgressListActivity
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ProgressHelper
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.SoundManager
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import java.util.TreeMap
import kotlin.math.sqrt

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btn1: ImageView
    lateinit var btn10: ImageView
    lateinit var btn11: ImageView
    lateinit var btn12: ImageView
    lateinit var btn13: ImageView
    lateinit var btn14: ImageView
    lateinit var btn2: ImageView
    lateinit var btn3: ImageView
    lateinit var btn4: ImageView
    lateinit var btn5: ImageView
    lateinit var btn6: ImageView
    lateinit var btn7: ImageView
    lateinit var btn8: ImageView
    lateinit var btn9: ImageView
    var firstPlayTimestamp: Long = 0
    var handler: Handler = Handler()

    lateinit var piano: ImageView

    var soundManager: SoundManager? = null

    var timer: CountDownTimer? = null
    var doubleBackToExitPressedOnce = false

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        setContentView(R.layout.activity_menu)

        bindview()
        this.soundManager = SoundManager.getInstance(this)
        volumeControlStream = 3
        var z = false
        xlarge = (resources.configuration.screenLayout and 15) == 4
        if ((resources.configuration.screenLayout and 15) == 3) {
            z = true
        }
        large = z
        val displayMetrics = resources.displayMetrics
        DENSITY = displayMetrics.density
        val f = (displayMetrics.widthPixels.toFloat()) / displayMetrics.xdpi
        val f2 = (displayMetrics.heightPixels.toFloat()) / displayMetrics.ydpi
        val sqrt = sqrt(((f2 * f2) + (f * f)).toDouble())
        if (sqrt >= 9.5) {
            isTenInchTablet = true
            defaultKeysize = "2.0"
        } else if (sqrt >= 6.78) {
            isSevenInchTablet = true
            defaultKeysize = "1.5"
        } else {
            defaultKeysize = "2.0"
        }
        //        //Log.v("themelodymaster", "size large:" + large + " xlarge:" + xlarge + " isSevenInchTablet:" + isSevenInchTablet + " isTenInchTablet:" + isTenInchTablet + " diagonalInches:" + sqrt);
        initProgressChartEntries()
    }

    private fun bindview() {

        btn1 = findViewById<View>(R.id.btn1) as ImageView
        btn1.setOnClickListener(this)

        btn2 = findViewById<View>(R.id.btn2) as ImageView
        btn2.setOnClickListener(this)

        btn3 = findViewById<View>(R.id.btn3) as ImageView
        btn3.setOnClickListener(this)

        btn4 = findViewById<View>(R.id.btn4) as ImageView
        btn4.setOnClickListener(this)

        btn5 = findViewById<View>(R.id.btn5) as ImageView
        btn5.setOnClickListener(this)

        btn6 = findViewById<View>(R.id.btn6) as ImageView
        btn6.setOnClickListener(this)

        btn7 = findViewById<View>(R.id.btn7) as ImageView
        btn7.setOnClickListener(this)

        btn8 = findViewById<View>(R.id.btn8) as ImageView
        btn8.setOnClickListener(this)

        btn9 = findViewById<View>(R.id.btn9) as ImageView
        btn9.setOnClickListener(this)

        btn10 = findViewById<View>(R.id.btn10) as ImageView
        btn10.setOnClickListener(this)

        btn11 = findViewById<View>(R.id.btn11) as ImageView
        btn11.setOnClickListener(this)

        btn12 = findViewById<View>(R.id.btn12) as ImageView
        btn12.setOnClickListener(this)

        btn13 = findViewById<View>(R.id.btn13) as ImageView
        btn13.setOnClickListener(this)

        btn14 = findViewById<View>(R.id.btn14) as ImageView
        btn14.setOnClickListener(this)

        piano = findViewById<View>(R.id.piano) as ImageView
        piano.setOnClickListener {
            startActivity(
                Intent(
                    this@MenuActivity,
                    PianoMain::class.java
                )
            )
        }


//        val imageView2 = findViewById<View>(R.id.btn2) as ImageView
//        this.btn2 = imageView2
//        imageView2.setOnClickListener(this)
//        val imageView3 = findViewById<View>(R.id.btn3) as ImageView
//        this.btn3 = imageView3
//        imageView3.setOnClickListener(this)
//        val imageView4 = findViewById<View>(R.id.btn4) as ImageView
//        this.btn4 = imageView4
//        imageView4.setOnClickListener(this)
//        val imageView5 = findViewById<View>(R.id.btn5) as ImageView
//        this.btn5 = imageView5
//        imageView5.setOnClickListener(this)
//        val imageView6 = findViewById<View>(R.id.btn6) as ImageView
//        this.btn6 = imageView6
//        imageView6.setOnClickListener(this)
//        val imageView7 = findViewById<View>(R.id.btn7) as ImageView
//        this.btn7 = imageView7
//        imageView7.setOnClickListener(this)
//        val imageView8 = findViewById<View>(R.id.btn8) as ImageView
//        this.btn8 = imageView8
//        imageView8.setOnClickListener(this)
//        val imageView9 = findViewById<View>(R.id.btn9) as ImageView
//        this.btn9 = imageView9
//        imageView9.setOnClickListener(this)
//        val imageView10 = findViewById<View>(R.id.btn10) as ImageView
//        this.btn10 = imageView10
//        imageView10.setOnClickListener(this)
//        val imageView11 = findViewById<View>(R.id.btn11) as ImageView
//        this.btn11 = imageView11
//        imageView11.setOnClickListener(this)
//        val imageView12 = findViewById<View>(R.id.btn12) as ImageView
//        this.btn12 = imageView12
//        imageView12.setOnClickListener(this)
//        val imageView13 = findViewById<View>(R.id.btn13) as ImageView
//        this.btn13 = imageView13
//        imageView13.setOnClickListener(this)
//        val imageView14 = findViewById<View>(R.id.btn14) as ImageView
//        this.btn14 = imageView14
//        imageView14.setOnClickListener(this)
//        val imageView15 = findViewById<View>(R.id.piano) as ImageView
//        this.piano = imageView15
//        imageView15.setOnClickListener {
//
//        }
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.btn1) {
            startActivity(Intent(this@MenuActivity, ScalesHelpActivity::class.java))

            return
        } else if (id == R.id.btn10) {
            startActivity(Intent(this@MenuActivity, UserSolosActivity::class.java))

            return
        } else if (id == R.id.btn11) {
            startActivity(Intent(this@MenuActivity, RecognitionHelpActivity::class.java))
        } else if (id == R.id.btn12) {
            startActivity(Intent(this@MenuActivity, RecognitionActivity::class.java))
        } else if (id == R.id.btn13) {
            startActivity(Intent(this@MenuActivity, ProgressListActivity::class.java))
        } else if (id == R.id.btn14) {
            startActivity(Intent(this@MenuActivity, SettingsActivity::class.java))
        } else if (id == R.id.btn2) {
            startActivity(Intent(this@MenuActivity, ScalesActivity::class.java))
        } else if (id == R.id.btn3) {
            startActivity(Intent(this@MenuActivity, ScalesGameHelpActivity::class.java))
        } else if (id == R.id.btn4) {
            startActivity(Intent(this@MenuActivity, ScalesGameActivity::class.java))
        } else if (id == R.id.btn5) {
            startActivity(Intent(this@MenuActivity, ChordsHelpActivity::class.java))
        } else if (id == R.id.btn6) {
            startActivity(Intent(this@MenuActivity, ChordsActivity::class.java))
        } else if (id == R.id.btn7) {
            startActivity(Intent(this@MenuActivity, ChordsGameHelpActivity::class.java))
        } else if (id == R.id.btn8) {
            startActivity(Intent(this@MenuActivity, ChordsGameActivity::class.java))
        } else if (id == R.id.btn9) {
            startActivity(Intent(this@MenuActivity, UserSolosHelpActivity::class.java))
        }
    }

    private fun initProgressChartEntries() {
        val treeMap = ProgressHelper.openProgressMapFromPrefs(this) as TreeMap<String, String>
        //Log.v("themelodymaster", "progressMap Read from onResume in ListActivity");
        if (!treeMap.containsKey("1|Beginner at Root Note:Random (C,F,G)")) {
            treeMap["1|Beginner at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|Beginner at Root Note:Random (C,F,G,D,A,E,Bb,Eb,Ab)")) {
            treeMap["1|Beginner at Root Note:Random (C,F,G,D,A,E,Bb,Eb,Ab)"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|Easy at Root Note:C")) {
            treeMap["1|Easy at Root Note:C"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|Easy at Root Note:Random (C,F,G)")) {
            treeMap["1|Easy at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|Medium at Root Note:C")) {
            treeMap["1|Medium at Root Note:C"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|Medium at Root Note:Random (C,F,G)")) {
            treeMap["1|Medium at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        if (!treeMap.containsKey("1|My Focus Group:Major, Minor, Blues at Root Note:Random (C,F,G)")) {
            treeMap["1|My Focus Group:Major, Minor, Blues at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        if (!treeMap.containsKey("2|Beginner at Root Note:C")) {
            treeMap["2|Beginner at Root Note:C"] = "0|0|2"
        }
        if (!treeMap.containsKey("2|Beginner at Root Note:Random (C,F,G)")) {
            treeMap["2|Beginner at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        if (!treeMap.containsKey("2|Beginner at Root Note:Random (C,F,G,D,A,E,Bb,Eb,Ab)")) {
            treeMap["2|Beginner at Root Note:Random (C,F,G,D,A,E,Bb,Eb,Ab)"] = "0|0|2"
        }
        if (!treeMap.containsKey("2|My Focus Group:Major, 6, 7 at Root Note:C")) {
            treeMap["2|My Focus Group:Major, 6, 7 at Root Note:C"] = "0|0|2"
        }
        if (!treeMap.containsKey("2|My Focus Group:Major, 6, 7 at Root Note:Random (C,F,G)")) {
            treeMap["2|My Focus Group:Major, 6, 7 at Root Note:Random (C,F,G)"] = "0|0|2"
        }
        ProgressHelper.saveProgressObjectViaJSON(treeMap, this)
    }


    override fun onBackPressed() {
//        rate_app()

        backLogic()
    }

    private fun backLogic() {
        if (doubleBackToExitPressedOnce) {
            ConstantAd.SPLASH_OPEN = false
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(
            this@MenuActivity,
            resources.getString(R.string.str_back_to_close_text),
            Toast.LENGTH_SHORT
        ).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    companion object {
        var DENSITY: Float = 1.0f
        const val KEY_AUTOSCROLL: String = "Key_Autoscroll"
        const val KEY_CHORD_LEVEL_IDX: String = "Chord_Level"
        const val KEY_CLICK_BEAT: String = "Click_Beat"
        const val KEY_CLICK_BPM: String = "Click_B_P_M"
        const val KEY_CLICK_SOUND: String = "Click_Sound"
        const val KEY_CLICK_VOLUME: String = "Click_Volume"
        const val KEY_FIRST_PLAY_TIMESTAMP: String = "First_Play_Timestamp"
        const val KEY_HAPTIC_FEEDBACK: String = "Key_Haptic_Feedback"
        const val KEY_HIGHLIGHT_ALL_NOTES: String = "Key_Highlight_All_Notes"
        const val KEY_INTERSTITIAL_LAST_SHOWN_TIMESTAMP: String =
            "Interstitial_Last_Shown_Timestamp"
        const val KEY_KEYSIZE: String = "Key_Size"
        const val KEY_NOTE_NAMES: String = "Key_Note_Name"
        const val KEY_PLAYALONG_SPEED: String = "Key_Play_Along_Speed"
        const val KEY_PLAYALONG_VOLUME: String = "Key_Play_Along_Volume"
        const val KEY_PRESSURE: String = "Key_Pressure"
        const val KEY_ROOT_NOTE: String = "Key_Root_Note"
        const val KEY_ROOT_NOTE_INCLUDING_RANDOM: String = "Key_Root_Note_Including_Random"
        const val KEY_SCALE_DIRECTION: String = "Key_ScaleDirection"
        const val KEY_SCALE_LEVEL_IDX: String = "Scale_Level"
        const val KEY_SHOW_PATTERN: String = "Show_Pattern_Scales"
        const val LOG_TAG: String = "themelodymaster"
        private const val TWO_MINUTES: Long = 120000

        @JvmField
        var defaultAutoscroll: Boolean = true

        @JvmField
        var defaultChordLevelIdx: Int = 0

        @JvmField
        var defaultHapticFeedback: String = "MEDIUM"

        @JvmField
        var defaultHighlightAllNotes: Boolean = true

        @JvmField
        var defaultKeysize: String = "1.0"

        @JvmField
        var defaultNoteNames: String = "STANDARD"

        @JvmField
        var defaultPressure: Boolean = false

        @JvmField
        var defaultRootNote: String = "C"

        @JvmField
        var defaultRootNoteIncludingRandom: String = "C"

        @JvmField
        var defaultScaleDirection: String = "ASCENDING"

        @JvmField
        var defaultScaleLevelIdx: Int = 0

        @JvmField
        var defaultShowPattern: Boolean = true

        @JvmField
        var defaultSpeed: String = "100"

        @JvmField
        var defaultVolume: String = "100"
        var hapticSettingValues: Array<String> =
            arrayOf("OFF", "VERY LIGHT", "LIGHT", "MEDIUM", "STRONG")

        @JvmField
        var isSevenInchTablet: Boolean = false

        @JvmField
        var isTenInchTablet: Boolean = false
        var large: Boolean = false
        var noteNamesValues: Array<String> = arrayOf("NONE", "STANDARD", "SOLFEGE", "FINGERINGS")
        private val sharedPrefs: SharedPreferences? = null
        private const val timestampOfLastInterstitial: Long = 0
        var xlarge: Boolean = false
    }

    var isExit: Boolean = false

    fun rate_app() {
        val dialog = Dialog(this@MenuActivity)
        //      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.rate_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window

        val wlp = window!!.attributes

        wlp.gravity = Gravity.BOTTOM or Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val txtYes = dialog.findViewById<View>(R.id.tv_exit) as TextView
        val tv_rate_app = dialog.findViewById<View>(R.id.tv_rate_app) as TextView

        txtYes.setTextColor(resources.getColor(R.color.gray))

        tv_rate_app.setOnClickListener { //            txtYes.setBackground(getResources().getDrawable(R.drawable.bg_light_yellow_5dp));
            val params = Bundle()
            params.putString("mRateApp", "mRateApp")
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                )
            )
        }
        txtYes.setOnClickListener {
            if (isExit) {
                dialog.dismiss()
                ConstantAd.SPLASH_OPEN = false
                // finishAffinity();
                finishAffinity()
            }
        }
        dialog.setOnDismissListener {
            isExit = false
            dialog.dismiss()
            txtYes.setTextColor(resources.getColor(R.color.gray))
            timer!!.cancel()
        }

        dialog.setOnShowListener {
            timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    txtYes.text =
                        resources.getString(R.string.exit_app) + "(" + millisUntilFinished / 1000 + ")"
                    // logic to set the EditText could go here
                }

                override fun onFinish() {
                    txtYes.setTextColor(resources.getColor(R.color.colorPrimary))
                    txtYes.text = resources.getString(R.string.exit_app)
                    isExit = true
                }
            }.start()
        }
        dialog.show()
    }

}
