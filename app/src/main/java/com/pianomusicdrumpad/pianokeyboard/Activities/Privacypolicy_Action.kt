package com.pianomusicdrumpad.pianokeyboard.Activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.Constant
import com.pianomusicdrumpad.pianokeyboard.Utils.SharedPrefrencesApp

class Privacypolicy_Action : AppCompatActivity() {
    private var mChecked: CheckBox? = null
    private var mStart: Button? = null
    private var mPrivacyText: TextView? = null
    private var mBottomStart: LinearLayout? = null
    private var progressBar: ProgressBar? = null
    private var pref: SharedPrefrencesApp? = null
    private val animSlideUp: Animation? = null
    private var disclaimer_text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacypolicy__action)
        progressBar = findViewById<View>(R.id.progress) as ProgressBar
        progressBar!!.visibility = View.VISIBLE

        //        AdSettings.addTestDevice("56040181-bdf0-42d8-a595-37ab3058c071");
        pref = SharedPrefrencesApp(applicationContext)

        mChecked = findViewById<View>(R.id.mPrivacyCheck) as CheckBox
        mBottomStart = findViewById<View>(R.id.bottom_ll) as LinearLayout
        mStart = findViewById(R.id.img_start)

        //        mBulbRelative = findViewById(R.id.relative_bulb);
        mPrivacyText = findViewById<View>(R.id.mPrivacy_text) as TextView
        mPrivacyText!!.paintFlags =
            mPrivacyText!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        disclaimer_text = findViewById(R.id.disclaimer_text)

        //        believe.cht.fadeintextview.TextView textView = (believe.cht.fadeintextview.TextView) findViewById(R.id.textView);
//
//        textView.setLetterDuration(125); // sets letter duration programmatically
//        textView.isAnimating(); // returns current animation state (boolean)
//        textView.setText(getString(R.string.app_name));
    }

    private fun initView() {
        mChecked!!.visibility = View.VISIBLE
        mStart!!.visibility = View.VISIBLE
        mPrivacyText!!.visibility = View.VISIBLE
        mBottomStart!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.GONE
        disclaimer_text!!.setOnClickListener {
            AlertDialog.Builder(
                this@Privacypolicy_Action,
                R.style.MyAlertDialogStyle
            )
                .setTitle(getString(R.string.Disclaimer))
                .setCancelable(false)
                .setMessage(getString(R.string.disclaimer_discption))
                .setPositiveButton(
                    "OK"
                ) { dialog, whichButton -> dialog.cancel() }.show()
        }

        mStart!!.setOnClickListener {
            if (mChecked!!.isChecked) {
                pref!!.setBooleanPreferences(SharedPrefrencesApp.Privacy, true)
                val i = Intent(this@Privacypolicy_Action, MenuActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(
                    this@Privacypolicy_Action,
                    "Please Check Privacy Policy to move forward...",
                    Toast.LENGTH_SHORT
                ).show()
                /*Intent i = new Intent(FirstScreenActivity.this, FirstScreenActivity1.class);
                        startActivity(i);
                        finish();*/
            }
        }

        mPrivacyText!!.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constant.PRIVACY_POLICY)
            )
            startActivity(browserIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!pref!!.getBooleanPreferences(SharedPrefrencesApp.Privacy, false)) {
            initView()
        } else {
            val `in` = Intent(this@Privacypolicy_Action, MenuActivity::class.java)
            startActivity(`in`)
            finish()
        }
    }
}
