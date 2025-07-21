package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.PointerIconCompat
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.C0934b
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale

class PianoMain : Activity() {
    private var temp = 1
    private var album: ImageView? = null


    private var homeAcousticImageView: ImageView? = null


    private var homeSynthImageView: ImageView? = null


    private var homeElectricPianoImageView: ImageView? = null


    private var homeGuitarImageView: ImageView? = null
    private var homePlayButtonImageView2: ImageView? = null


    private fun NextScreen() {
        val i: Int
        val intent = Intent(this, PianoPlayScreen::class.java)
        val i2 = this.temp
        i = if (i2 == 0) {
            1002
        } else if (i2 == 1) {
            PointerIconCompat.TYPE_HELP
        } else if (i2 == 2) {
            PointerIconCompat.TYPE_WAIT
        } else if (i2 == 3) {
            1005
        } else {
            return
        }
        intent.putExtra("instrumentId", i)
        startActivity(intent)
        //        showAdmobInterstitial();
    }


    private fun init() {
        this.homeAcousticImageView = findViewById<View>(R.id.homeAcousticImageView) as ImageView
        this.homeSynthImageView = findViewById<View>(R.id.homeSynthImageView) as ImageView
        this.homeElectricPianoImageView =
            findViewById<View>(R.id.homeElectricPianoImageView) as ImageView
        this.homeGuitarImageView = findViewById<View>(R.id.homeGuitarImageView) as ImageView
        this.homePlayButtonImageView2 =
            findViewById<View>(R.id.homePlayButtonImageView2) as ImageView
        homePlayButtonImageView2!!.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext, R.anim.blink
            )
        )
        val imageView = findViewById<View>(R.id.album) as ImageView
        this.album = imageView
        imageView.setOnClickListener {
            this@PianoMain.startActivity(
                Intent(
                    this@PianoMain,
                    RecordedSound::class.java
                )
            )
        }
    }


    private fun setImages() {
        val imageView: ImageView?
        val i = this.temp
        var i2 = R.drawable.home_electric_piano
        var i3 = R.drawable.home_synth
        var i4 = R.drawable.guitar_button
        if (i == 0) {
            homeAcousticImageView!!.setImageResource(R.drawable.home_acoustic_marked)
            imageView = this.homeSynthImageView
        } else if (i == 1) {
            homeAcousticImageView!!.setImageResource(R.drawable.home_acoustic)
            homeSynthImageView!!.setImageResource(R.drawable.home_synth)
            val imageView2 = this.homeElectricPianoImageView
            i2 = R.drawable.home_electric_piano_marked
            imageView2!!.setImageResource(R.drawable.home_electric_piano_marked)
            imageView = this.homeGuitarImageView
            imageView!!.setImageResource(R.drawable.guitar_button)
        } else if (i == 2) {
            homeAcousticImageView!!.setImageResource(R.drawable.home_acoustic)
            imageView = this.homeSynthImageView
            i3 = R.drawable.home_synth_marked
        } else if (i == 3) {
            homeAcousticImageView!!.setImageResource(R.drawable.home_acoustic)
            homeSynthImageView!!.setImageResource(R.drawable.home_synth)
            homeElectricPianoImageView!!.setImageResource(R.drawable.home_electric_piano)
            imageView = this.homeGuitarImageView
            i4 = R.drawable.guitar_button_focus
            imageView!!.setImageResource(R.drawable.guitar_button_focus)
        } else {
            return
        }
        imageView!!.setImageResource(i3)
        homeElectricPianoImageView!!.setImageResource(i2)
        homeGuitarImageView!!.setImageResource(i4)
    }


    private fun Play() {
//        adClick = 1;


        NextScreen()
    }

    fun acousticPressed(view: View?) {
        this.temp = 0
        setImages()
    }

    fun electricPianoPressed(view: View?) {
        this.temp = 1
        setImages()
    }

    override fun finish() {
        val a = C0934b.m93a()
        a?.mo19486b()
        super.finish()
    }

    fun guitarPressed(view: View?) {
        this.temp = 3
        setImages()
    }


    public override fun onActivityResult(i: Int, i2: Int, intent: Intent) {
        super.onActivityResult(i, i2, intent)
        if (i == 826 && i2 == 847) {
            finish()
        }
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
        setContentView(R.layout.piano_main)
        init()
        //        mInterstitialAdMob = showAdmobFullAd();
//        loadAdmobAd();
    }

    fun pianoPlayScreen(view: View?) {
        Play()
    }

    fun synthPressed(view: View?) {
        this.temp = 2
        setImages()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        putString(ConstantAd.AD_CHECK_RESUME, "1")
    }
}
