package com.pianomusicdrumpad.pianokeyboard.Activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility.setLocale
import com.pianomusicdrumpad.pianokeyboard.ads.MainInterfaceV2
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.exit.Utility

class AgreeScreenActivity : AppCompatActivity() {
    var tvHeading: TextView? = null
    var tvDecline: TextView? = null
    var tvAccept: TextView? = null
    var tvTurmCondition: TextView? = null
    var tvPrivacyPolicy: TextView? = null

    var cb_check: CheckBox? = null
    private val REQUEST_OVERLAY_PERMISSION = 1001
    var REQUEST_CODE_PERMISSIONS_FOR_CALL = 5

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
        setContentView(R.layout.activity_cm_agree_screen)

        findViews()
        clickListeners()

        val permissionScreenAdsShow =
            SharePrefUtils.getString(ConstantAd.PERMISSION_SCREEN_ADS_SHOW, "1")

        if (permissionScreenAdsShow == "1") {
            loadBanner()
        }

    }

    private fun clickListeners() {
        tvDecline!!.setOnClickListener { finishAffinity() }
        tvAccept!!.setOnClickListener {
            if (cb_check!!.isChecked) {

//                putBoolean(ConstantAd.AGREE_SCREEN, true)
//                val i = Intent(this@AgreeScreenActivity, FeatureActivity::class.java)
//                startActivity(i)

                checkPermission()


            } else {
                Toast.makeText(
                    this@AgreeScreenActivity,
                    "Please select check box to accept Privacy Policy and Terms of Services",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        tvTurmCondition!!.setOnClickListener {

            if (!Utility.isConnectivityAvailable(this)) {
                Toast.makeText(this, "Please Connect Internet", Toast.LENGTH_SHORT).show()
            } else {
                val `in` = Intent(this@AgreeScreenActivity, PrivacyPolicyActivity::class.java)
                startActivity(`in`)
            }
        }
        tvPrivacyPolicy!!.setOnClickListener {

            if (!Utility.isConnectivityAvailable(this)) {
                Toast.makeText(this, "Please Connect Internet", Toast.LENGTH_SHORT).show()
            } else {
                val `in` = Intent(this@AgreeScreenActivity, PrivacyPolicyActivity::class.java)
                startActivity(`in`)
            }

            if (!Utility.isConnectivityAvailable(this)) {
                Toast.makeText(this, "Please Connect Internet", Toast.LENGTH_SHORT).show()
            } else {
                val `in` = Intent(this@AgreeScreenActivity, PrivacyPolicyActivity::class.java)
                startActivity(`in`)
            }

        }
        cb_check!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                tvAccept!!.setBackgroundResource(R.drawable.shape_accept)
            } else {
                tvAccept!!.setBackgroundResource(R.drawable.shape_accept_disable)
            }
        }
    }

    private fun findViews() {
        tvHeading = findViewById(R.id.tvHeading)
        tvDecline = findViewById(R.id.tvDecline)
        tvAccept = findViewById(R.id.tvAccept)
        tvTurmCondition = findViewById(R.id.tvTurmCondition)
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy)
        cb_check = findViewById(R.id.cb_check)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun checkPermission() {

//        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arrayOf(
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.CALL_PHONE,
//                Manifest.permission.POST_NOTIFICATIONS,
//            )
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            arrayOf(
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.CALL_PHONE,
//            )
//        } else {
//            arrayOf(
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.CALL_PHONE,
//            )
//        }

        if (CommonUtils.hasPermissions(permissions, this)) {
            // Permissions are granted, proceed with your operation
//            afterPermissionUpdate()
//            putBoolean(ConstantAd.AGREE_SCREEN, true)
//            val i = Intent(this@AgreeScreenActivity, FeatureActivity::class.java)
//            startActivity(i)
            if (!Settings.canDrawOverlays(this)) {
                ConstantAd.SPLASH_OPEN = false
//                val intent = Intent(
//                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + this.packageName)
//                )
//                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)

                SharePrefUtils.putBoolean(ConstantAd.AGREE_SCREEN, true)
                val i = Intent(this@AgreeScreenActivity, OverlayScreenActivity::class.java)
                startActivity(i)

            } else {

                val i = Intent(this@AgreeScreenActivity, MenuActivity::class.java)
                startActivity(i)

            }
        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_CODE_PERMISSIONS_FOR_CALL
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //        Log.e("code123", "onActivityResult: app download failed"+requestCode);
        if (requestCode == REQUEST_CODE_PERMISSIONS_FOR_CALL) {

            if (CommonUtils.hasPermissions(permissions, this)) {
                // Permissions are granted, proceed with your operation
//            afterPermissionUpdate()

                if (!Settings.canDrawOverlays(this)) {
                    SharePrefUtils.putBoolean(ConstantAd.AGREE_SCREEN, true)
                    val i = Intent(this@AgreeScreenActivity, OverlayScreenActivity::class.java)
                    startActivity(i)

                } else {
                    val i = Intent(this@AgreeScreenActivity, MenuActivity::class.java)
                    startActivity(i)
                }
            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    permissions,
//                    REQUEST_CODE_PERMISSIONS_FOR_CALL
//                )

                val i = Intent(this@AgreeScreenActivity, MenuActivity::class.java)
                startActivity(i)
            }
        } else if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            ConstantAd.SPLASH_OPEN = true
            SharePrefUtils.putBoolean(ConstantAd.AGREE_SCREEN, true)
            val i = Intent(this@AgreeScreenActivity, MenuActivity::class.java)
            startActivity(i)
        }
    }

    private fun loadBanner() {
        MainInterfaceV2.loadBanner(
            this,
            findViewById(R.id.Admob_Native_Frame_two),
            adNativeBannerSimmer = R.layout.ad_native_adptive_banner_simmer,
            bannerId = ConstantAd.AD_PERMISSION_BANNER
        )
    }

    override fun onResume() {
        setLocale(this, getString(ConstantAd.LANGUAGE_CODE, "en"))
        super.onResume()
    }

}