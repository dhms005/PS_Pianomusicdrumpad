package com.pianomusicdrumpad.pianokeyboard.Activities

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.Constant
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.SharedPrefrencesApp

class PrivacyPolicyActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var url: String? = null
    var preferences: SharedPrefrencesApp? = null
    private val mAdView: LinearLayout? = null
    private var mediation: String? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_activity)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mDeclaration()
    }

    private fun mDeclaration() {
        preferences = SharedPrefrencesApp(this@PrivacyPolicyActivity)
        preferences!!.setPreferences(preferences!!.Check_ID, "1")

        mediation = SharePrefUtils.getString(ConstantAd.MEDIATION, "0")
        url = Constant.PRIVACY_POLICY

        webView = findViewById<View>(R.id.webView_privacy) as WebView
        webView!!.webViewClient = MyBrowser()
        webView!!.webChromeClient = WebChromeClientDemo()
        webView!!.settings.loadsImagesAutomatically = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.useWideViewPort = true
        webView!!.setInitialScale(1)
        webView!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        progressBar = findViewById<View>(R.id.prgBar_privacy) as ProgressBar

        callUrl()
    }

    private fun callUrl() {
        progressBar!!.visibility = View.VISIBLE
        webView!!.loadUrl(url!!)
    }

    private inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            progressBar!!.visibility = View.VISIBLE
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)

            progressBar!!.visibility = View.GONE
            progressBar!!.progress = 100
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
            progressBar!!.visibility = View.VISIBLE
            progressBar!!.progress = 0
        }
    }

    private inner class WebChromeClientDemo : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            progressBar!!.progress = progress
        }
    }

    override fun onBackPressed() {
        val params = Bundle()
        params.putString("onBackPressed", "onBackPressed")
        mFirebaseAnalytics!!.logEvent("PrivacypolicyActivity", params)

        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        preferences!!.setPreferences(preferences!!.Check_ID, "0")
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        preferences!!.setPreferences(preferences!!.Check_ID, "1")
    }

    override fun onDestroy() {
        preferences!!.setPreferences(preferences!!.Check_ID, "2")
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
