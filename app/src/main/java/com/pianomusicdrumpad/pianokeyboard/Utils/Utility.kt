package com.pianomusicdrumpad.pianokeyboard.Utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.LocaleListCompat
import com.google.android.material.snackbar.Snackbar
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import java.util.Locale

/**
 * Created by lenovo on 16/11/16.
 */
object Utility : AppCompatActivity() {
    var listener: OnTaskCompleted? = null
    var dialog: Dialog? = null
    var temPValue: Int = 0

    var startString: String = getString(ConstantAd.START_SCREEN_TEXTs, "")
    var startStringText: Array<String> =
        startString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


    var loadingAdDailog: Dialog? = null

    fun displaySnackBarWithMessage(view: View, Message: String) {
        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show()
    }

    fun alertDialog(context: Context, onTaskCompleted: OnTaskCompleted?, msg: String?) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.setTitle("No Internet Connection") // Sets title for your alertbox

        alertDialog.setCancelable(false)
        alertDialog.setMessage("Please check your internet connection and try again.")

        alertDialog.setPositiveButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        alertDialog.setNegativeButton(
            "Retry"
        ) { dialog, which ->
            listener!!.onTaskCompleted(
                true
            )
        }

        /*AlertDialog alert = alertDialog.create();
    alert.show();
    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
    nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
    pbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));

    listener = onTaskCompleted;
    dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.custom_error_dailog);
    final TextView txtMessage = (TextView) dialog.findViewById(R.id.txtErrorMsg_Dialog);
    final TextView txtTryAgain = (TextView) dialog.findViewById(R.id.txtTryAgain_Dialog);

    txtMessage.setText(msg);

    txtTryAgain.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dialog.cancel();
            listener.onTaskCompleted(true);
        }
    });

    dialog.show();
    Window window = dialog.getWindow();
    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/
    }

    /*  AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("No Internet Connection"); // Sets title for your alertbox

        alertDialog.setCancelable(false);
        alertDialog.setMessage("Please check your internet connection and try again.");

        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }

        });

        alertDialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                listener.onTaskCompleted(true);
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));*/
    fun OpenCustomGameZopBrowser(activity: Activity) {
        val PACKAGE_NAME = "com.android.chrome"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.setData(Uri.parse("https://www.gamezop.com/?id=4303"))
        val packageManager = activity.packageManager
        val resolveInfoList = packageManager.queryIntentActivities(
            customTabsIntent.intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        for (resolveInfo in resolveInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            if (TextUtils.equals(packageName, PACKAGE_NAME)) customTabsIntent.intent.setPackage(
                PACKAGE_NAME
            )
        }
        customTabsIntent.launchUrl(activity, Uri.parse("https://www.gamezop.com/?id=4303"))
    }

    fun isConnectivityAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun OpenCustomQurekaBrowser(activity: Context) {
        if (getString(ConstantAd.AD_QUREKA, "") == "") {
        } else {
            val PACKAGE_NAME = "com.android.chrome"
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.intent.setData(Uri.parse(getString(ConstantAd.AD_QUREKA, "")))
            val packageManager = activity.packageManager
            val resolveInfoList = packageManager.queryIntentActivities(
                customTabsIntent.intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                if (TextUtils.equals(packageName, PACKAGE_NAME)) customTabsIntent.intent.setPackage(
                    PACKAGE_NAME
                )
            }
            customTabsIntent.launchUrl(activity, Uri.parse(getString(ConstantAd.AD_QUREKA, "")))
        }
    }

    fun isNetworkAvailable(c: Context): Boolean {
        val connectivityManager = c
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @JvmStatic
    fun setLocale(context: Context, lang: String?) {
        val myLocale = Locale(lang)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(myLocale)
        res.updateConfiguration(conf, dm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val appLocale = LocaleListCompat.forLanguageTags(lang)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
