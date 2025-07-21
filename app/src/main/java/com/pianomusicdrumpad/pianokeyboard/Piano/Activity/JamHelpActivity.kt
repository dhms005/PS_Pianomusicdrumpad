package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale

class JamHelpActivity : Activity(), View.OnClickListener {
    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        requestWindowFeature(1)
        window.setFlags(1024, 1024)
        setContentView(R.layout.jam_help)
        findViewById<View>(R.id.back_arrow_view).setOnClickListener(this)
    }

    override fun onResume() {
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        super.onResume()
    }
}
