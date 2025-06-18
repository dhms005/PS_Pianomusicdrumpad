package com.pianomusicdrumpad.pianokeyboard.Activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.MenuActivity
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.Utils.SharedPrefrencesApp
import com.pianomusicdrumpad.pianokeyboard.Utils.Utility

class FirstActivity : AppCompatActivity() {
    private var mRecyclerViewTreadingApp: RecyclerView? = null
    private var mRecyclerViewNewUpdate: RecyclerView? = null
    private var prefrencesApp: SharedPrefrencesApp? = null
    var api_key: String? = null
    var mDisclaimer: TextView? = null
    var img_Start: ImageView? = null
    var img_more: ImageView? = null
    var img_rate: ImageView? = null
    var img_share: ImageView? = null
    var img_privacy: ImageView? = null
    private var mAppUpdateManager: AppUpdateManager? = null
    private var coordinatorLayout: RelativeLayout? = null
    private val RC_APP_UPDATE = 100

    var timer: CountDownTimer? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    var downloadDialogVPN: Dialog? = null
    var vpn_progressBar: ProgressBar? = null
    var txtCityName: TextView? = null
    var txtStateName: TextView? = null
    var txtCountryName: TextView? = null
    var txtIpAdressName: TextView? = null
    var txtTimeZone: TextView? = null
    var txtZipcode: TextView? = null
    var details: LinearLayout? = null
    lateinit var permissionsList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharePrefUtils.getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        setContentView(R.layout.activity_first_screen_)

        val preferences = this.getSharedPreferences("GCM", MODE_PRIVATE)
        prefrencesApp = SharedPrefrencesApp(this)
        mDeclarationMethod()

        mClickListener()
        //  checkForUpdate();
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: Network?
        var vpnInUse = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeNetwork = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(activeNetwork)
            vpnInUse = caps!!.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }
        VPNDialog()
        if (vpnInUse) {
            if (SharePrefUtils.getString(ConstantAd.VPN_DETECT, "1") == "1") {
                downloadDialogVPN!!.show()
            }
        }

    }

    fun VPNDialog() {
        downloadDialogVPN = Dialog(this@FirstActivity)
        downloadDialogVPN!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        downloadDialogVPN!!.setCancelable(false)
        downloadDialogVPN!!.setContentView(R.layout.dialog_vpn_connection)
        val window = downloadDialogVPN!!.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER or Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        downloadDialogVPN!!.window!!
            .setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        downloadDialogVPN!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val install_now = downloadDialogVPN!!.findViewById<TextView>(R.id.update)

        vpn_progressBar = downloadDialogVPN!!.findViewById(R.id.progress_circular)
        txtCityName = downloadDialogVPN!!.findViewById(R.id.txtCityName)
        txtStateName = downloadDialogVPN!!.findViewById(R.id.txtStateName)
        txtCountryName = downloadDialogVPN!!.findViewById(R.id.txtCountryName)
        txtIpAdressName = downloadDialogVPN!!.findViewById(R.id.txtIpAdressName)
        txtTimeZone = downloadDialogVPN!!.findViewById(R.id.txtTimeZone)
        txtZipcode = downloadDialogVPN!!.findViewById(R.id.txtZipcode)
        details = downloadDialogVPN!!.findViewById(R.id.details)

        //        getCityDetails();
        install_now.setOnClickListener {
            finish()
            ConstantAd.SPLASH_OPEN = false
            // finishAffinity();
            finishAffinity()
        }
    }


    private fun mDeclarationMethod() {
        img_more = findViewById(R.id.img_more)
        img_Start = findViewById(R.id.img_start)

        img_rate = findViewById(R.id.img_rate)
        img_share = findViewById(R.id.img_share)
        img_privacy = findViewById(R.id.img_privacy)
        mDisclaimer = findViewById(R.id.text_disclaimer)

        coordinatorLayout = findViewById(R.id.coordinatorLayout)

        mRecyclerViewNewUpdate =
            findViewById<View>(R.id.recycler_view_new_update_app) as RecyclerView
        mRecyclerViewTreadingApp =
            findViewById<View>(R.id.recycler_view_trading_app) as RecyclerView

        mRecyclerViewNewUpdate!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerViewTreadingApp!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun mClickListener() {
        img_Start!!.setOnClickListener {
            if (Permission_check(this@FirstActivity)) {
                val i = Intent(this@FirstActivity, MenuActivity::class.java)
                startActivity(i)
            }
        }
        img_more!!.setOnClickListener {
            //           adClick = 2;
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(
                                ConstantAd.ACCOUNT,
                                ""
                            )
                        )
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(
                                ConstantAd.ACCOUNT,
                                ""
                            )
                        )
                    )
                )
            }
            //                Intent i = new Intent(FirstActivity.this, FindOwnerDrivingActivity.class);
            //                startActivity(i);
        }

        img_rate!!.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                )
            )
        }

        img_share!!.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.setType("text/plain")
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post")
            share.putExtra(
                Intent.EXTRA_TEXT,
                """${getString(R.string.share_text)} 
 https://play.google.com/store/apps/details?id=${applicationContext.packageName}"""
            )
            startActivity(Intent.createChooser(share, "Share link!"))
        }


        img_privacy!!.setOnClickListener {
            if (!Utility.isConnectivityAvailable(
                    this@FirstActivity
                )
            ) {
                Toast.makeText(
                    this@FirstActivity,
                    "Please Connect Internet",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val `in` = Intent(
                    this@FirstActivity,
                    PrivacyPolicyActivity::class.java
                )
                startActivity(`in`)
            }
        }

        mDisclaimer!!.setOnClickListener { disclaimerDialog() }
    }

    private fun disclaimerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer")
            .setCancelable(false)
            .setMessage(
                getString(R.string.disclaimer_discption) + SharePrefUtils.getString(
                    ConstantAd.EMAIL,
                    ""
                ) + "."
            )
            .setPositiveButton(
                "OK"
            ) { dialog, whichButton -> dialog.cancel() }.show()
    }

    override fun onBackPressed() {
        if (SharePrefUtils.getString(ConstantAd.START_SCREEN_COUNT, "0") == "0") {
            rate_app()
        } else {
            finish()
        }
    }

    var isExit: Boolean = false

    fun rate_app() {
        val dialog = Dialog(this@FirstActivity)
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


    fun checkForUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this)

        mAppUpdateManager!!.registerListener(installStateUpdatedListener)

        mAppUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    mAppUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@FirstActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    mAppUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@FirstActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            } else {
//                Log.e("error", "checkForAppUpdateAvailability: something else");
            }
        }
    }

    var installStateUpdatedListener: InstallStateUpdatedListener =
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) {
                        mAppUpdateManager!!.unregisterListener(this)
                    }
                } else {
                    Log.e("error", "InstallStateUpdatedListener: state: " + state.installStatus())
                }
            }
        }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar =
            Snackbar.make(
                coordinatorLayout!!,
                "New app is ready!",
                Snackbar.LENGTH_INDEFINITE
            )

        snackbar.setAction("Install") { view: View? ->
            if (mAppUpdateManager != null) {
                mAppUpdateManager!!.completeUpdate()
            }
        }
        snackbar.setActionTextColor(resources.getColor(R.color.install_color))
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "1")
    }


    //flexible update
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("code", "onActivityResult: app download failed")
            }
        }
    }

    fun Permission_check(context: Activity): Boolean {
//        var PERMISSIONS : Array<String>
//         var PERMISSIONS: Array<String>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList = arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,

                )
            if (!hasPermissions(context, *permissionsList)) {
                context.requestPermissions(permissionsList, 200)
            } else {
                return true
            }
        } else {
            permissionsList = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

            )
            if (!hasPermissions(context, *permissionsList)) {
                context.requestPermissions(permissionsList, 2)
            } else {
                return true
            }
        }


        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //        Log.e("code123", "onActivityResult: app download failed"+requestCode);
        if (requestCode == 2) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val i = Intent(this@FirstActivity, MenuActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 200) {
            val i = Intent(this@FirstActivity, MenuActivity::class.java)
            startActivity(i)
        }
    }

    override fun onPause() {
        SharePrefUtils.putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    override fun onDestroy() {
        if (handler != null) {
            handler!!.removeCallbacks(runnable!!)
        }
        super.onDestroy()
    }

    companion object {
        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }
    }


}