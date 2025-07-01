package com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.Array_Adapter
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.C0934b
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.RecoedSound_c
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SQLiteHelper
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SharePreference
import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0945e
import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0946f
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.activity.CallCutPopupActivity
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getSharedPreferencesData
import com.pianomusicdrumpad.pianokeyboard.databinding.CfsFragmentRecoredSoundBinding
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.StreamCorruptedException
import java.util.Vector


class CallCutRecordFragment : Fragment() {
    lateinit var binding: CfsFragmentRecoredSoundBinding
    lateinit var sharedPreferences: SharedPreferences
    var callCutPopupActivity: CallCutPopupActivity? = null

    var Vector: Vector<C0946f> = Vector()
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {
        if (this@CallCutRecordFragment.isRecordSound) {
            this@CallCutRecordFragment.m68d()
        }
    }

    var sQLiteHelper: SQLiteHelper? = null
    var f131g: Array_Adapter? = null
    var f129e: Int = -1

    var f133i: C0934b? = null
    var number: Int = 0

    var f137n: Int = -1
    lateinit var f138o: Array<C0945e>
    var isRecordSound: Boolean = true
    var imageView: ImageView? = null

    //    var timeView: TimeView? = null
    var RecoedImage: ImageView? = null

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callCutPopupActivity = activity as CallCutPopupActivity


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CfsFragmentRecoredSoundBinding.inflate(inflater, container, false)

        this.f133i = C0934b.m93a()
        this.number = 1002

        init()

        recordList()

        return binding.root
    }

    fun init() {

        sharedPreferences = getSharedPreferencesData(callCutPopupActivity!!)


//        binding.llVideo.setOnClickListener {
//            val i = Intent(callCutPopupActivity, MenuActivity::class.java)
//            startActivity(i)
//        }
//
//        binding.llAudio.setOnClickListener {
//            val i = Intent(callCutPopupActivity, MenuActivity::class.java)
//            startActivity(i)
//        }

    }

    internal inner class AcyncTask : AsyncTask<Any?, Any?, Any?>() {
        var progressDialog: ProgressDialog? = null

        override fun doInBackground(vararg objArr: Any?): Any? {
            f133i!!.mo19491e(this@CallCutRecordFragment.number)
            f133i!!.mo19485a(activity as Activity, this.progressDialog)
            return null
        }


        public override fun onPostExecute(obj: Any?) {
            super.onPostExecute(obj)
        }


        public override fun onPreExecute() {
            val progressDialog = ProgressDialog(callCutPopupActivity)
            this.progressDialog = progressDialog
            progressDialog.setTitle("Loading Sounds...")
            this.progressDialog!!.max = 88
            this.progressDialog!!.setCancelable(false)
            this.progressDialog!!.setOnDismissListener {
                this@CallCutRecordFragment.isRecordSound =
                    true
                val unused = this@CallCutRecordFragment.isRecordSound
                this@CallCutRecordFragment.removeHandler()
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
        val a = SQLiteHelper.m113a(callCutPopupActivity)
        this.sQLiteHelper = a
        this.f138o = a.mo19506a("recordingTable")
        val create = AlertDialog.Builder(callCutPopupActivity).create()
        create.setMessage("Delete recording?")
        create.setButton(
            -1, "      Yes      "
        ) { dialogInterface, i ->
            handler.removeCallbacks(
                runnable
            )
            this@CallCutRecordFragment.isRecordSound = false
            val unused = this@CallCutRecordFragment.isRecordSound
            this@CallCutRecordFragment.f137n = -1
            val unused2 = this@CallCutRecordFragment.f137n
            binding.timeView1!!.mo19616c()
            binding.timeView1!!.mo19614a()
            if (this@CallCutRecordFragment.imageView != null) {
                this@CallCutRecordFragment.imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
            }
            f131g!!.mo19498c()
            sQLiteHelper!!.mo19507b(
                "recordingTable",
                f138o[position].mo19510a()
            )
            this@CallCutRecordFragment.recordList()
        }
        create.setButton(
            -2, "       No       "
        ) { dialogInterface, i -> create.dismiss() }
        create.show()
    }


    fun recordList() {
        val r0: Array_Adapter =
            object : Array_Adapter(
                callCutPopupActivity,
                R.layout.recording_list_layout,
                binding.timeView1
            ) {
                override fun mo19269a() {
                    val recordedSoundActivity = this@CallCutRecordFragment
                    recordedSoundActivity.f137n = recordedSoundActivity.f129e
                    val unused = recordedSoundActivity.f137n
                    this@CallCutRecordFragment.isRecordSound = false
                    val unused2 = this@CallCutRecordFragment.isRecordSound
                    f209b.mo19616c()
                    if (this@CallCutRecordFragment.imageView != null) {
                        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
                    }
                }


                override fun mo19270a(i: Int) {
                    this@CallCutRecordFragment.deleteRecord(i)
                }


                override fun mo19271a(str: String, imageView: ImageView) {
                    this@CallCutRecordFragment.isRecordSound = true
                    if (this@CallCutRecordFragment.RecoedImage !== imageView) {
                        this@CallCutRecordFragment.f137n = -1
                        f209b.mo19614a()
                        this@CallCutRecordFragment.RecoedImage = imageView
                        val unused3 = this@CallCutRecordFragment.RecoedImage
                    }
                    this@CallCutRecordFragment.imageView = imageView
                    val unused4 = this@CallCutRecordFragment.imageView
                    this@CallCutRecordFragment.fileStream(str)
                }


                override fun mo19272b() {
                    handler.removeCallbacks(this@CallCutRecordFragment.runnable)
                    this@CallCutRecordFragment.isRecordSound = false
                    val unused = this@CallCutRecordFragment.isRecordSound
                    this@CallCutRecordFragment.f137n = -1
                    val unused2 = this@CallCutRecordFragment.f137n
                    this@CallCutRecordFragment.RecoedImage = null
                    val unused3 = this@CallCutRecordFragment.RecoedImage
                    f209b.mo19616c()
                    f209b.mo19614a()
                    if (this@CallCutRecordFragment.imageView != null) {
                        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
                    }
                    f131g!!.mo19498c()
                }
            }
        this.f131g = r0
        binding.recordinglistView!!.adapter = r0
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
                activity,
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
        binding.timeView1!!.mo19616c()
        binding.timeView1!!.mo19614a()
        imageView!!.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()
    }


    fun removeHandler() {
        binding.timeView1!!.mo19615b()
        this.f129e = this.f137n
        handler.removeCallbacks(this.runnable)
        handler.post(this.runnable)
    }

    override fun onPause() {
        this.f137n = this.f129e
        this.isRecordSound = false
        binding.timeView1!!.mo19616c()
        val imageView = this.imageView
        imageView?.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    override fun onDestroy() {

        handler.removeCallbacks(this.runnable)
        this.f137n = -1
        this.isRecordSound = false
        if (binding.timeView1!!.mo19617d()) {
            binding.timeView1!!.mo19616c()
            binding.timeView1!!.mo19614a()
        }
        val imageView = this.imageView
        imageView?.setImageResource(R.drawable.recording_btn_bg_play_selector)
        f131g!!.mo19498c()
        super.onDestroy()
    }
}