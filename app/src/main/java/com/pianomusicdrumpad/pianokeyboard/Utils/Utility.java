package com.pianomusicdrumpad.pianokeyboard.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * Created by lenovo on 16/11/16.
 */
public class Utility extends AppCompatActivity {

    public static OnTaskCompleted listener;
    public static Dialog dialog;
    public  static int temPValue = 0;

    static String startString = SharePrefUtils.getString(ConstantAd.START_SCREEN_TEXTs, "");
    public static String[] startStringText = startString.split(",");


    public static Dialog loadingAdDailog;

    public static void displaySnackBarWithMessage(View view, String Message) {
        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
    }

    public static void alertDialog(Context context, OnTaskCompleted onTaskCompleted, String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

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

    public static void OpenCustomGameZopBrowser(Activity activity){
        String PACKAGE_NAME = "com.android.chrome";
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(Uri.parse("https://www.gamezop.com/?id=4303"));
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, PACKAGE_NAME))
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
        }
        customTabsIntent.launchUrl(activity, Uri.parse("https://www.gamezop.com/?id=4303"));
    }

    public static boolean isConnectivityAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void OpenCustomQurekaBrowser(Context activity){
        if (SharePrefUtils.getString(ConstantAd.AD_QUREKA, "").equals("")) {

        } else {
            String PACKAGE_NAME = "com.android.chrome";
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.intent.setData(Uri.parse(SharePrefUtils.getString(ConstantAd.AD_QUREKA, "")));
            PackageManager packageManager = activity.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resolveInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                if (TextUtils.equals(packageName, PACKAGE_NAME))
                    customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }
            customTabsIntent.launchUrl(activity, Uri.parse(SharePrefUtils.getString(ConstantAd.AD_QUREKA, "")));

        }
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
