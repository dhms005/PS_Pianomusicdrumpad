package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.PointerIconCompat
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.C0934b
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.RecoedSound_c
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SQLiteHelper
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SharePreference
import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0946f
import com.pianomusicdrumpad.pianokeyboard.Piano.views.PianoFullStripView
import com.pianomusicdrumpad.pianokeyboard.Piano.views.SoundVolumeSeekBarView
import com.pianomusicdrumpad.pianokeyboard.Piano.views.TimeView
import com.pianomusicdrumpad.pianokeyboard.Piano.views.a
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.ads.MainInterfaceV2
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.StreamCorruptedException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Vector

class PianoPlayScreen : Activity() {
    lateinit var f111a: C0934b
    var vector: Vector<C0946f> = Vector()
    var isplay: Boolean = false
    private var pianoScreenLayout: LinearLayout? = null
    private lateinit var aObject: a
    var f116f: SQLiteHelper? = null
    var PlayIconType: Int = 0
    private val pianoCode = arrayOf(
        "A0",
        "B0",
        "C1",
        "D1",
        "E1",
        "F1",
        "G1",
        "A1",
        "B1",
        "C2",
        "D2",
        "E2",
        "F2",
        "G2",
        "A2",
        "B2",
        "C3",
        "D3",
        "E3",
        "F3",
        "G3",
        "A3",
        "B3",
        "C4",
        "D4",
        "E4",
        "F4",
        "G4",
        "A4",
        "B4",
        "C5",
        "D5",
        "E5",
        "F5",
        "G5",
        "A5",
        "B5",
        "C6",
        "D6",
        "E6",
        "F6",
        "G6",
        "A6",
        "B6",
        "C7",
        "D7",
        "E7",
        "F7",
        "G7",
        "A7",
        "B7",
        "C8"
    )
    private val f119k = -1
    private var recordPlayPauseImageView: ImageView? = null
    private var stopRecordingImageView: ImageView? = null
    var playScreenTimeView: TimeView? = null
    var aBoolean: Boolean = false


    fun close() {
    }


    fun backActivity(view: View?) {
        onBackPressed()
    }

    internal inner class AsyncTaskBack : AsyncTask<Any?, Any?, Any?>() {
        var progressDialog: ProgressDialog? = null

        override fun doInBackground(vararg objArr: Any?): Any? {
            f111a!!.mo19491e(this@PianoPlayScreen.PlayIconType)
            f111a!!.mo19485a(this@PianoPlayScreen as Activity, this.progressDialog)
            return null
        }


        public override fun onPostExecute(obj: Any?) {
            super.onPostExecute(obj)
        }


        public override fun onPreExecute() {
            val progressDialog = ProgressDialog(this@PianoPlayScreen)
            this.progressDialog = progressDialog
            progressDialog.setTitle("Loading Sounds...")
            this.progressDialog!!.max = 88
            this.progressDialog!!.setCancelable(true)
            this.progressDialog!!.show()
            super.onPreExecute()
        }
    }


    private fun init() {
        this.pianoScreenLayout = findViewById<View>(R.id.pianoScreenLayout) as LinearLayout
        this.recordPlayPauseImageView =
            findViewById<View>(R.id.recordPlayPauseImageView) as ImageView
        this.stopRecordingImageView = findViewById<View>(R.id.stopRecordingImageView) as ImageView
        this.playScreenTimeView = findViewById<View>(R.id.playScreenTimeView) as TimeView
    }


    private fun saveFileDialog(str: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.save_file_dialog)
        val editText = dialog.findViewById<View>(R.id.recordNameEditText) as EditText
        val textView = dialog.findViewById<View>(R.id.fileNotExistText) as TextView
        val str2 = str
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentTime: String = formatter.format(Date())
        editText.setText(currentTime)
        val dialog2 = dialog
        (dialog.findViewById<View>(R.id.saveImageView) as ImageView).setOnClickListener(
            View.OnClickListener {
                textView.visibility = View.GONE
                val obj = editText.text.toString()
//                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
//                val currentTime: String = formatter.format(Date())

//                if (obj.trim { it <= ' ' }.equals("", ignoreCase = true)) {
//                    textView.visibility = View.VISIBLE
//                    textView.text = "File Name Can Not Be Empty"
//                    return@OnClickListener
//                }
                val str = "$str2/$obj"

                Log.e("getpath", str)

                if (f116f!!.mo19505a("recordingTable", obj)) {
                    f116f!!.mo19504a("recordingTable", obj, str)
                    val pianoPlayScreen = this@PianoPlayScreen
                    pianoPlayScreen.fileStream(pianoPlayScreen.vector, str)
                    vector.removeAllElements()
                    playScreenTimeView!!.mo19614a()
                    dialog2.dismiss()
                } else {
                    textView.visibility = View.VISIBLE
                }
                if (this@PianoPlayScreen.aBoolean) {
                    this@PianoPlayScreen.aBoolean = false
                    this@PianoPlayScreen.finish()
                }
            })
        (dialog.findViewById<View>(R.id.discardImageView) as ImageView).setOnClickListener {
            vector.removeAllElements()
            playScreenTimeView!!.mo19614a()
            if (this@PianoPlayScreen.aBoolean) {
                this@PianoPlayScreen.aBoolean = false
                val unused = this@PianoPlayScreen.aBoolean
                this@PianoPlayScreen.finish()
            }
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }


    fun fileStream(vector: Vector<C0946f>?, str: String) {
        val file = File(str)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val fileOutputStream = FileOutputStream(file)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(vector)
            objectOutputStream.flush()
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e2: FileNotFoundException) {
            e2.printStackTrace()
        } catch (e3: StreamCorruptedException) {
            e3.printStackTrace()
        } catch (e4: IOException) {
            e4.printStackTrace()
        }
    }


    fun addVactor(i: Int) {
        if (i > 0 && i - 1 < 52) {
            if (this.isplay) {
                vector.add(
                    C0946f(
                        i, "writeString",
                        playScreenTimeView!!.millisSeconds, this.PlayIconType, true
                    )
                )
            }
            val intValue = ((SharePreference.m89a(
                applicationContext,
                "soundValumeKey"
            ) as Int).toFloat()) / 100.0f
            RecoedSound_c.m88a("Key Id vol $intValue", "Key Id playing$i")
            f111a!!.mo19490d(i)
            f111a!!.mo19488b(i, intValue)
        }
    }


    private fun changeInstrumentDialog() {
        val dialog = Dialog(this, 16973841)
        dialog.setContentView(R.layout.choose_instrument_dailog)
        val checkBox = dialog.findViewById<View>(R.id.acousticCheckBox) as CheckBox
        val checkBox2 = dialog.findViewById<View>(R.id.brightCheckBox) as CheckBox
        val checkBox3 = dialog.findViewById<View>(R.id.synthCheckBox) as CheckBox
        val checkBox4 = dialog.findViewById<View>(R.id.guitarCheckBox) as CheckBox
        dialog.findViewById<View>(R.id.dialogCloseBtn).setOnClickListener {
            this@PianoPlayScreen.close()
            dialog.dismiss()
        }
        val i = this.PlayIconType
        if (i == 1002) {
            checkBox.isChecked = true
        } else if (i == 1003) {
            checkBox2.isChecked = true
        } else if (i == 1004) {
            checkBox3.isChecked = true
        } else if (i == 1005) {
            checkBox4.isChecked = true
        }
        checkBox.setOnCheckedChangeListener { compoundButton, z ->
            if (z) {
                this@PianoPlayScreen.PlayIconType = 1002
                val pianoPlayScreen = this@PianoPlayScreen
                pianoPlayScreen.AsyncTask(pianoPlayScreen.PlayIconType)
                this@PianoPlayScreen.close()
                dialog.dismiss()
            }
        }
        checkBox2.setOnCheckedChangeListener { compoundButton, z ->
            if (z) {
                this@PianoPlayScreen.PlayIconType = PointerIconCompat.TYPE_HELP
                val unused = this@PianoPlayScreen.PlayIconType
                val pianoPlayScreen = this@PianoPlayScreen
                pianoPlayScreen.AsyncTask(pianoPlayScreen.PlayIconType)
                this@PianoPlayScreen.close()
                dialog.dismiss()
            }
        }
        checkBox3.setOnCheckedChangeListener { compoundButton, z ->
            if (z) {
                this@PianoPlayScreen.PlayIconType = PointerIconCompat.TYPE_WAIT
                val unused = this@PianoPlayScreen.PlayIconType
                val pianoPlayScreen = this@PianoPlayScreen
                pianoPlayScreen.AsyncTask(pianoPlayScreen.PlayIconType)
                this@PianoPlayScreen.close()
                dialog.dismiss()
            }
        }
        checkBox4.setOnCheckedChangeListener { compoundButton, z ->
            if (z) {
                this@PianoPlayScreen.PlayIconType = 1005
                val unused = this@PianoPlayScreen.PlayIconType
                val pianoPlayScreen = this@PianoPlayScreen
                pianoPlayScreen.AsyncTask(pianoPlayScreen.PlayIconType)
                this@PianoPlayScreen.close()
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    fun AsyncTask(i: Int) {
        AsyncTaskBack().execute(*arrayOfNulls<Any>(1))
    }


    private fun volumeSeekBaar() {
        val soundVolumeSeekBarView =
            findViewById<View>(R.id.soundVolumeSeekBarView1) as SoundVolumeSeekBarView
        soundVolumeSeekBarView.visibility = View.VISIBLE
        soundVolumeSeekBarView.setVolumeProgess { i ->
            SharePreference.m90a(
                this@PianoPlayScreen.applicationContext,
                "soundValumeKey",
                i
            )
        }
    }

    private fun stopAndSaveRecoredSound() {
        playScreenTimeView!!.mo19616c()
        playScreenTimeView!!.mo19614a()
        this.isplay = false
        recordPlayPauseImageView!!.setImageDrawable(resources.getDrawable(R.drawable.play_btn))
        if (vector.size > 0) {
            println("Recording Stop")
            saveFileDialog(getAppFolder(this@PianoPlayScreen))
        }
    }


    fun playIconType(i: Int) {
        if (i > 0 && i - 1 < 36) {
            if (this.isplay) {
                vector.add(
                    C0946f(
                        i, "blackString",
                        playScreenTimeView!!.millisSeconds, this.PlayIconType, true
                    )
                )
            }
            val intValue = ((SharePreference.m89a(
                applicationContext,
                "soundValumeKey"
            ) as Int).toFloat()) / 100.0f
            RecoedSound_c.m88a("Key Id vol$intValue", "black Key plauing$i")
            f111a!!.mo19489c(i)
            f111a!!.mo19484a(i, intValue)
        }
    }

    fun changeInstrument(view: View?) {
        changeInstrumentDialog()
    }

    override fun finish() {
        Log.d("onfinish", "onfinis")
        super.finish()
    }

    fun myRecordings(view: View?) {
        startActivityForResult(Intent(this, RecordedSound::class.java), 2531)
    }


    public override fun onActivityResult(i: Int, i2: Int, intent: Intent) {
        if (i == 2531) {
            AsyncTask(this.PlayIconType)
        }
        super.onActivityResult(i, i2, intent)
    }

    override fun onBackPressed() {
        if (vector.size > 0) {
            this.aBoolean = true
            stopAndSaveRecoredSound()
            return
        }

        finish()
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions

        setContentView(R.layout.piano_play_screen)
        init()

        this.f111a = C0934b.m93a()
        this.PlayIconType = PointerIconCompat.TYPE_HELP
        val intExtra = intent.getIntExtra("instrumentId", PointerIconCompat.TYPE_HELP)
        this.PlayIconType = intExtra
        AsyncTask(intExtra)
        this.f116f = SQLiteHelper.m113a(this as Context)
        aObject = object : a(this) {
            override fun a(i: Int) {
                this@PianoPlayScreen.addVactor(i)
            }


            override fun b(i: Int) {
                this@PianoPlayScreen.playIconType(i)
            }


            override fun c(i: Int) {
                if (this@PianoPlayScreen.PlayIconType == 1004 && i > 0 && i - 1 < 36) {
                    if (this@PianoPlayScreen.isplay) {
                        vector.add(
                            C0946f(
                                i, "blackString",
                                playScreenTimeView!!.millisSeconds,
                                this@PianoPlayScreen.PlayIconType, false
                            )
                        )
                    }
                    f111a.mo19483a(i)
                }
            }


            override fun d(i: Int) {
                if (this@PianoPlayScreen.PlayIconType == 1004 && i > 0 && i - 1 < 52) {
                    if (this@PianoPlayScreen.isplay) {
                        vector.add(
                            C0946f(
                                i, "writeString",
                                playScreenTimeView!!.millisSeconds,
                                this@PianoPlayScreen.PlayIconType, false
                            )
                        )
                    }
                    f111a.mo19487b(i)
                }
            }
        }
        //        this.aObject = r3;
        aObject.setWhiteKeyNotesText(this.pianoCode)
        pianoScreenLayout!!.addView(this.aObject)
        (findViewById<View>(R.id.pianoFullStripView1) as PianoFullStripView).setPianoView(
            this.aObject
        )
        volumeSeekBaar()

        val homeAdShow =
            SharePrefUtils.getString(ConstantAd.HOME_AD_SHOW, "1")

        if (homeAdShow == "1") {
            loadBanner()
        }
    }


    public override fun onPause() {
        playScreenTimeView!!.mo19616c()
        super.onPause()
    }


    public override fun onResume() {
        super.onResume()
        if (this.isplay) {
            playScreenTimeView!!.mo19615b()
        }
    }


    public override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
    }

    fun rateApp(view: View?) {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("market://details?id=" + javaClass.getPackage().name)
            )
        )
    }

    fun shareApp(view: View?) {
        val intent = Intent()
        intent.setAction("android.intent.action.SEND")
        intent.putExtra(
            "android.intent.extra.TEXT",
            "Check out this cool Piano App at: https://play.google.com/store/apps/details?id=" + javaClass.getPackage().name
        )
        intent.setType("text/plain")
        startActivity(intent)
    }

    fun soundRecordingPlayPause(view: View?) {
        val z = !isplay
        isplay = z
        if (z) {
            isplay = true
            recordPlayPauseImageView!!.setImageDrawable(resources.getDrawable(R.drawable.pause_btn))
            playScreenTimeView!!.mo19615b()
            println("Recording Stop")
            return
        }
        println("Recording Start")
        isplay = false
        recordPlayPauseImageView!!.setImageDrawable(resources.getDrawable(R.drawable.rec_pause_btn))
        playScreenTimeView!!.mo19616c()
    }

    fun stopAndSaveRecoredSound(view: View?) {
        stopAndSaveRecoredSound()
    }

    companion object {
        fun getAppFolder(activity: Context): String {
            return activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path
        }
    }

    private fun loadBanner() {
        MainInterfaceV2.loadBanner(
            this,
            findViewById(R.id.Admob_Native_Frame_two),
            adNativeBannerSimmer = R.layout.ad_native_adptive_banner_simmer,
            bannerId = ConstantAd.AD_HOME_BANNER
        )
    }
}
