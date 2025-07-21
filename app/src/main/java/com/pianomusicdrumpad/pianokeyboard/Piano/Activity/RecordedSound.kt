package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.Array_Adapter
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.C0934b
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.RecoedSound_c
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SQLiteHelper
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SharePreference
import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0945e
import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0946f
import com.pianomusicdrumpad.pianokeyboard.Piano.views.TimeView
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.StreamCorruptedException
import java.util.Vector

class RecordedSound : Activity() {
    var Iv_back_save: ImageView? = null
    var f125a: SQLiteHelper? = null
    var Vector: Vector<C0946f> = Vector()
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {
        if (this@RecordedSound.isRecordSound) {
            this@RecordedSound.m68d()
        }
    }
    var f129e: Int = -1
    private var listview: ListView? = null
    var f131g: Array_Adapter? = null
    var timeView: TimeView? = null
    var f133i: C0934b? = null
    var number: Int = 0
    var imageView: ImageView? = null
    var RecoedImage: ImageView? = null
    var f137n: Int = -1
    lateinit var f138o: Array<C0945e>
    var isRecordSound: Boolean = true


    internal inner class AcyncTask : AsyncTask<Any?, Any?, Any?>() {
        var progressDialog: ProgressDialog? = null

        override fun doInBackground(vararg objArr: Any?): Any? {
            f133i!!.mo19491e(this@RecordedSound.number)
            f133i!!.mo19485a(this@RecordedSound as Activity, this.progressDialog)
            return null
        }


        public override fun onPostExecute(obj: Any?) {
            super.onPostExecute(obj)
        }


        public override fun onPreExecute() {
            val progressDialog = ProgressDialog(this@RecordedSound)
            this.progressDialog = progressDialog
            progressDialog.setTitle("Loading Sounds...")
            this.progressDialog!!.max = 88
            this.progressDialog!!.setCancelable(false)
            this.progressDialog!!.setOnDismissListener {
                this@RecordedSound.isRecordSound =
                    true
                val unused = this@RecordedSound.isRecordSound
                this@RecordedSound.removeHandler()
            }
            this.progressDialog!!.show()
            super.onPreExecute()
        }
    }


    private fun m64a(i: Int) {
        AcyncTask().execute(*arrayOfNulls<Any>(1))
    }


    fun fileStream(str: String?) {
        try {
            val fileInputStream = FileInputStream(str)
            val objectInputStream = ObjectInputStream(fileInputStream)
            this.Vector = objectInputStream.readObject() as Vector<C0946f>
            objectInputStream.close()
            fileInputStream.close()
            val d = Vector[0].mo19518d()
            this.number = d
            m64a(d)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: StreamCorruptedException) {
            e2.printStackTrace()
        } catch (e3: IOException) {
            e3.printStackTrace()
        } catch (e4: ClassNotFoundException) {
            e4.printStackTrace()
        }
    }


    fun deleteRecord(position: Int) {
        val a = SQLiteHelper.m113a(this as Context)
        this.f125a = a
        this.f138o = a.mo19506a("recordingTable")
        val create = AlertDialog.Builder(this).create()
        create.setMessage("Delete recording?")
        create.setButton(
            -1, "      Yes      "
        ) { dialogInterface, i ->
            handler.removeCallbacks(
                runnable
            )
            this@RecordedSound.isRecordSound = false
            val unused = this@RecordedSound.isRecordSound
            this@RecordedSound.f137n = -1
            val unused2 = this@RecordedSound.f137n
            this@RecordedSound.RecoedImage = null
            val unused3 = this@RecordedSound.RecoedImage
            timeView!!.mo19616c()
            timeView!!.mo19614a()
            if (this@RecordedSound.imageView != null) {
                imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
            }
            f131g!!.mo19498c()
            f125a!!.mo19507b(
                "recordingTable",
                f138o[position].mo19510a()
            )
            this@RecordedSound.recordList()
        }
        create.setButton(
            -2, "       No       "
        ) { dialogInterface, i -> create.dismiss() }
        create.show()
    }


    fun recordList() {
        val r0: Array_Adapter =
            object : Array_Adapter(this, R.layout.recording_list_layout, this.timeView) {
                override fun mo19269a() {
                    val recordedSound = this@RecordedSound
                    recordedSound.f137n = recordedSound.f129e
                    val unused = recordedSound.f137n
                    this@RecordedSound.isRecordSound = false
                    val unused2 = this@RecordedSound.isRecordSound
                    f209b.mo19616c()
                    if (this@RecordedSound.imageView != null) {
                        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
                    }
                }


                override fun mo19270a(i: Int) {
                    this@RecordedSound.deleteRecord(i)
                }


                override fun mo19271a(str: String, imageView: ImageView) {
                    this@RecordedSound.isRecordSound = true
                    if (this@RecordedSound.RecoedImage !== imageView) {
                        this@RecordedSound.f137n = -1
                        f209b.mo19614a()
                        this@RecordedSound.RecoedImage = imageView
                        val unused3 = this@RecordedSound.RecoedImage
                    }
                    this@RecordedSound.imageView = imageView
                    val unused4 = this@RecordedSound.imageView
                    this@RecordedSound.fileStream(str)
                }


                override fun mo19272b() {
                    handler.removeCallbacks(this@RecordedSound.runnable)
                    this@RecordedSound.isRecordSound = false
                    val unused = this@RecordedSound.isRecordSound
                    this@RecordedSound.f137n = -1
                    val unused2 = this@RecordedSound.f137n
                    this@RecordedSound.RecoedImage = null
                    val unused3 = this@RecordedSound.RecoedImage
                    f209b.mo19616c()
                    f209b.mo19614a()
                    if (this@RecordedSound.imageView != null) {
                        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
                    }
                    f131g!!.mo19498c()
                }
            }
        this.f131g = r0
        listview!!.adapter = r0
    }


    fun m68d() {
        val i = this.f129e + 1
        this.f129e = i
        if (i < Vector.size) {
            val a = Vector[f129e].mo19514a()
            Vector[f129e].mo19518d()
            val b = Vector[f129e].mo19516b()
            val c = Vector[f129e].mo19517c()
            val e = Vector[f129e].mo19519e()
            var j: Long = 0
            if (this.f129e + 1 != Vector.size) {
                j = Vector[f129e + 1].mo19517c()
            }
            val intValue = ((SharePreference.m89a(
                applicationContext,
                "soundValumeKey"
            ) as Int).toFloat()) / 100.0f
            if (b.equals("writeString", ignoreCase = true)) {
                if (e) {
                    RecoedSound_c.m88a("Key Id vol$intValue", "black Key plauing$a")
                    f133i!!.mo19490d(a)
                    f133i!!.mo19488b(a, intValue)
                } else {
                    f133i!!.mo19487b(a)
                }
            } else if (e) {
                f133i!!.mo19489c(a)
                f133i!!.mo19484a(a, intValue)
            } else {
                f133i!!.mo19483a(a)
            }
            handler.postDelayed(this.runnable, j - c)
            return
        }
        handler.removeCallbacks(this.runnable)
        this.isRecordSound = false
        this.f137n = -1
        timeView!!.mo19616c()
        timeView!!.mo19614a()
        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()
    }


    fun removeHandler() {
        timeView!!.mo19615b()
        this.f129e = this.f137n
        handler.removeCallbacks(this.runnable)
        handler.post(this.runnable)
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.recored_sound)
        this.f133i = C0934b.m93a()
        this.number = 1002
        this.timeView = findViewById<View>(R.id.timeView1) as TimeView
        this.listview = findViewById<View>(R.id.recordinglistView) as ListView
        val imageView = findViewById<View>(R.id.Iv_back_save) as ImageView
        this.Iv_back_save = imageView
        imageView.setOnClickListener { this@RecordedSound.onBackPressed() }
        recordList()
    }


    override fun onBackPressed() {
        handler.removeCallbacks(this.runnable)
        this.f137n = -1
        this.isRecordSound = false
        if (timeView!!.mo19617d()) {
            timeView!!.mo19616c()
            timeView!!.mo19614a()
        }
        val imageView = this.imageView
        imageView?.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()

        finish()
    }

    override fun onPause() {
        this.f137n = this.f129e
        this.isRecordSound = false
        timeView!!.mo19616c()
        val imageView = this.imageView
        imageView?.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        putString(ConstantAd.AD_CHECK_RESUME, "1")
    }
}
