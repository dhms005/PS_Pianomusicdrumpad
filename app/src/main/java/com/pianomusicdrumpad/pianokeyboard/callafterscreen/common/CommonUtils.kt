package com.pianomusicdrumpad.pianokeyboard.callafterscreen.common

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.SIMAccount
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.SimModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CommonUtils {

    const val sim1Text: String = "SIM 1"
    const val sim2Text: String = "SIM 2"
    const val otherText: String = "Unknown SIM"
    const val outgoingCallText: String = "Outgoing call"
    const val incomingCallText: String = "Incoming call"
    const val missedCallText: String = "Missed call"
    const val sharedPreferencesNumber: String = "sharedPreferencesNumber"

    fun placeCall(context: Context, callNumber: String?, simSlot: Int) {
        // Ensure the CALL_PHONE permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Call permission is not granted", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the TelecomManager
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val phoneAccounts = telecomManager.callCapablePhoneAccounts

        if (phoneAccounts.isNotEmpty() && simSlot < phoneAccounts.size) {
            // Retrieve the PhoneAccountHandle corresponding to the SIM slot
            val phoneAccountHandle = phoneAccounts[simSlot]

            // Create the call Uri
            val uri = Uri.parse("tel:$callNumber")

            // Bundle up the extra with the chosen PhoneAccountHandle
            val extras = Bundle().apply {
                putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
            }

            // Place the call
            telecomManager.placeCall(uri, extras)
        } else {
            // Fallback: If no phone accounts or simSlot index is invalid, try a default call intent.
            try {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_CALL,
                    Uri.parse("tel:$callNumber")
                )
                // Some devices allow specifying the SIM slot using these extras.
                intent.putExtra("com.android.phone.extra.slot", simSlot)
                intent.putExtra("slot", simSlot)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to place call", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    fun hasPermissions(permissions: Array<String>, context: Context): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getSharedPreferencesData(context: Context): SharedPreferences {
        // "0" is equivalent to Context.MODE_PRIVATE
        return context.getSharedPreferences("Contacts: Phone Dialer App", Context.MODE_PRIVATE)
    }

    fun getString(sharedPreferences: SharedPreferences, key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putString(sharedPreferences: SharedPreferences, key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Save a list of strings
    fun saveStringList(sharedPreferences: SharedPreferences, key: String, list: List<String>) {
        val json = Gson().toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    // Retrieve a list of strings
    fun getStringList(
        sharedPreferences: SharedPreferences,
        key: String,
        default: List<String> = emptyList()
    ): List<String> {
        val json = sharedPreferences.getString(key, null) ?: return default
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type) ?: default
    }

    fun getAvailablesimList(context: Context): ArrayList<SimModel> {
        val simList = ArrayList<SimModel>()

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(context, "Call permission is not granted", Toast.LENGTH_SHORT).show()
//            return
//        }

        // Get TelecomManager to retrieve the available PhoneAccountHandles
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val phoneAccounts = telecomManager.callCapablePhoneAccounts

        // Optional: Use SubscriptionManager to get detailed SIM info (requires appropriate permission)
        val subscriptionManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
        val subscriptionInfoList = subscriptionManager?.activeSubscriptionInfoList

        // Loop through each phone account to create a corresponding SimModel entry
        phoneAccounts.forEachIndexed { index, phoneAccountHandle ->
            // Default operator name if subscription details are not available
            var operatorName = "SIM ${index + 1}"

            // If we have subscription info, try to get the carrier name
            if (subscriptionInfoList != null && index < subscriptionInfoList.size) {
                val subscriptionInfo = subscriptionInfoList[index]
                operatorName = subscriptionInfo.carrierName?.toString() ?: operatorName
            }

            // Create a SimModel instance; simId can be the index (or use subscription id if available)
            simList.add(SimModel(index, phoneAccountHandle, operatorName))
        }

        return simList
    }

    fun getCallStatusText(callStatus: String): String {
        return when (callStatus) {
            "4", "1", "9", "3", "7" -> "Outgoing call"
            "2" -> "Incoming call"
            else -> "Missed call"
        }
    }

    suspend fun getContactNameFromPhoneNumber(context: Context, phoneNumber: String): String? {
        return withContext(Dispatchers.IO) {
            var contactName: String? = null
            // Build the URI to search for the phone number
            val uri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )
            // Specify the projection (fields to return)
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            // Query the Contacts Provider
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    // Get the contact name from the cursor
                    contactName = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                    )
                }
            }
            contactName
        }
    }

    fun getPhotoUri(context: Context, contactId: String): Uri? {
        return try {
            // Build the contact URI using the contactId.
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId.toLong()
            )
            // Define the projection (the column we want to retrieve)
            val projection = arrayOf(ContactsContract.Contacts.PHOTO_URI)

            // Query the contact
            context.contentResolver.query(contactUri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val photoUriString = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI)
                    )
                    if (!photoUriString.isNullOrEmpty()) {
                        Uri.parse(photoUriString)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getContactIdFromPhoneNumber(context: Context, phoneNumber: String): String? {
        // Build the URI to filter contacts based on the phone number
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        // Specify the projection, here we only need the contact ID
        val projection = arrayOf(ContactsContract.PhoneLookup._ID)

        // Query the Contacts Provider
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                // Retrieve the contact ID from the cursor
                return cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
            }
        }
        return null
    }

    fun formatTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        val timeDifference = currentDate - timestamp

        val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())  // For time format (today)
        val sdfDayWeek = SimpleDateFormat(
            "EEE, hh:mm a",
            Locale.getDefault()
        )  // For day of the week and time (within a week)
        val sdfFullDate = SimpleDateFormat(
            "d MMM, hh:mm a",
            Locale.getDefault()
        )  // For full date and time (more than a week ago)

        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // If the call was made today
        if (timestamp >= todayStart) {
            return sdfTime.format(Date(timestamp)) // Only time (HH:mm)
        }

        // If the call was made within the last 7 days
        if (timeDifference <= 7 * 24 * 60 * 60 * 1000) { // 7 days in milliseconds
            return sdfDayWeek.format(Date(timestamp)) // Day of the week and time (EEE, HH:mm)
        }

        // If the call was made more than a week ago
        return sdfFullDate.format(Date(timestamp)) // Full date and time (d MMM, HH:mm)
    }

    fun fetchNameFromNumberDetails(context: Context, phoneNumber: String): String {
        var contactName: String = "";
        try {
            // Build the URI to search for the phone number
            val uri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )
            // Specify the projection (fields to return)
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            // Query the Contacts Provider
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    // Get the contact name from the cursor
                    contactName = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (contactName == "") phoneNumber else
            contactName
    }

    fun formatDuration(seconds: Long): String {
        val minutes = seconds / 60
        val sec = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, sec) // Use explicit Locale
    }

    fun Context.getAvailableSIMCardLabels(): List<SIMAccount> {
        val simAccounts = mutableListOf<SIMAccount>()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            try {
                val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                telecomManager.callCapablePhoneAccounts.forEachIndexed { index, account ->
                    val phoneAccount = telecomManager.getPhoneAccount(account)
                    var label = phoneAccount.label.toString()
                    var address = phoneAccount.address.toString()
                    if (address.startsWith("tel:") && address.substringAfter("tel:").isNotEmpty()) {
                        address = Uri.decode(address.substringAfter("tel:"))
                        label += " ($address)"
                    }

                    val sim = SIMAccount(
                        index + 1,
                        phoneAccount.accountHandle,
                        label,
                        address.substringAfter("tel:")
                    )
                    simAccounts.add(sim)
                }
            } catch (ignored: Exception) {
                Toast.makeText(this, ignored.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
        }

        return simAccounts
    }

    fun sendMessageWithText(context: Context, phoneNumber: String, message: String) {
        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber") // "smsto:" ensures only SMS apps handle it
            putExtra("sms_body", message) // Pre-fill the message
        }

        try {
            context.startActivity(smsIntent) // Launch SMS app
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to send message.", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(context: Context, phoneNumber: String) {
        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber") // "smsto:" ensures only SMS apps handle it
        }

        try {
            context.startActivity(smsIntent) // Launch SMS app
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to send message.", Toast.LENGTH_SHORT).show()
        }
    }

    fun createContactWithNumber(context: Context, phoneNumber: String) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber) // Pre-fill phone number
        }

        try {
            context.startActivity(intent) // Open the Contacts app
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to open Contacts app.", Toast.LENGTH_SHORT).show()
        }
    }

    fun getContactIdFromNumber(context: Context, phoneNumber: String): String? {
        val contentResolver = context.contentResolver
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )

        val cursor = contentResolver.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup._ID),
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
            }
        }
        return null // No contact found with this number
    }

    fun editContactWithNumber(context: Context, phoneNumber: String) {
        val contactId = getContactIdFromNumber(context, phoneNumber)

        if (contactId != null) {
            val intent = Intent(Intent.ACTION_EDIT).apply {
                data = ContactsContract.Contacts.CONTENT_URI.buildUpon()
                    .appendPath(contactId)
                    .build()
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to open contact editor.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Contact not found.", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendWhatAppMessage(context: Context, phoneNumber: String) {
        try {
            val formattedNumber = if (phoneNumber.startsWith("+")) {
                phoneNumber
            } else {
                "+$phoneNumber" // Ensure international format
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$formattedNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun openDialer(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        context.startActivity(intent)
    }

    fun openContact(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_CONTACTS)
        }
        context.startActivity(intent)
    }

    fun openWhatApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage("com.whatsapp")
        if (intent != null) {
            context.startActivity(intent) // Open WhatsApp
        } else {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    fun logCallAfterScreenEvent(mFirebaseAnalytics: FirebaseAnalytics) {
//        val firebaseAnalytics = Firebase.analytics
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, "CallAfterScreen")
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "CallAfterActivity")
        }
        mFirebaseAnalytics.logEvent("call_after_screen_opened", bundle)
    }

    fun trackEvent(mFirebaseAnalytics: FirebaseAnalytics, eventName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName)
        mFirebaseAnalytics.logEvent(eventName, bundle)
    }
}