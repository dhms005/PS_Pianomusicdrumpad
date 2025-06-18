package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString

class ScalesGameHelpActivity : Activity(), View.OnClickListener {
    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        setContentView(R.layout.scales_game_help)

        findViewById<View>(R.id.back_arrow_view).setOnClickListener(this)
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
        putString(ConstantAd.AD_CHECK_RESUME, "1")
    }
}
