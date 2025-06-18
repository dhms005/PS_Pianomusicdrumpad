package com.pianomusicdrumpad.pianokeyboard.callafterscreen.ViewModel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.common.CommonUtils.getAvailableSIMCardLabels
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.ItemRecent
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.model.ItemRecentGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class RecentCallViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to observe the recent call groups
    val recentCallsLiveData = MutableLiveData<ArrayList<ItemRecentGroup>>()

    private val _recentContactGroups = MutableLiveData<List<ItemRecent>>()
    val recentContactGroups: LiveData<List<ItemRecent>> get() = _recentContactGroups

    private val _recentContactItem = MutableLiveData<ItemRecent>()
    val recentContactItem: LiveData<ItemRecent> get() = _recentContactItem

    // ExecutorService removed in favor of Kotlin Coroutines for better management
    // of background tasks with proper lifecycle handling.

    // This is a simpler list of call ids for tracking deletions
    private val deletedCallIds = ArrayList<String>()

    // Method to handle the call action, e.g., dialing the number from a recent call group
    fun onCallAction(context: Context, itemRecentGroup: ItemRecentGroup) {
        val number = itemRecentGroup.arrRecent.getOrNull(0)?.number
        if (!number.isNullOrEmpty()) {
            // Trigger the dialing or call action via utility function
            CommonUtils.placeCall(context, number, 0)

            // Get the current value of the recent calls list from LiveData
            val currentList = recentCallsLiveData.value?.toMutableList()

            // Remove the group of calls with the same number
            val filteredList = currentList?.filter { group ->
                group.arrRecent.none { it.number == number }
            }?.toMutableList()

            // Update the LiveData to reflect the changes
            recentCallsLiveData.value = filteredList as ArrayList<ItemRecentGroup>?
        }
    }

    // Method to delete a specific call log from a given call group
    fun deleteCallFromGroup(context: Context, groupIndex: Int) {
        if (context != null && ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_CALL_LOG
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val currentList = recentCallsLiveData.value ?: return

            val itemRecentGroup = currentList.getOrNull(groupIndex) ?: return
            val idsToDelete = itemRecentGroup.arrRecent.map { it.id }

            // Launch a coroutine for background task
            viewModelScope.launch {
                // Perform the deletion in background
                withContext(Dispatchers.IO) {
                    val contentResolver: ContentResolver = context.contentResolver

                    // Delete all the calls in the group from the call log
                    idsToDelete.forEach { callId ->
                        contentResolver.delete(
                            CallLog.Calls.CONTENT_URI,
                            "_id = ?",
                            arrayOf(callId.toString())
                        )
                    }
                }

                // Update the UI after deletion
                val updatedList = currentList.toMutableList()
                updatedList.removeAt(groupIndex) // Remove the group entirely
                recentCallsLiveData.postValue(updatedList as ArrayList<ItemRecentGroup>?)
            }
        }
    }

    // Method to delete a specific call log item
    fun deleteSingleCall(context: Context, callId: String) {
        if (context != null && ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_CALL_LOG
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val contentResolver: ContentResolver = context.contentResolver
            contentResolver.delete(CallLog.Calls.CONTENT_URI, "_id = ?", arrayOf(callId))

            // You can optionally remove the call from the LiveData as well, if needed
            deletedCallIds.add(callId) // Track deleted call ids
        }
    }

    // Method to add new recent call items to the LiveData
    fun addRecentCalls(newCalls: List<ItemRecentGroup>) {
        val currentList = recentCallsLiveData.value ?: mutableListOf()
        currentList.addAll(newCalls)
        recentCallsLiveData.value = currentList as ArrayList<ItemRecentGroup>?
    }

//    fun getRecentCallsLast3Days(context: Context) {
//
//
//        if (context != null && ContextCompat.checkSelfPermission(
//                context,
//                android.Manifest.permission.READ_CALL_LOG
//            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//        ) {
//            // Obtain the ContentResolver from the context
//            val contentResolver = context.contentResolver
//
//            val threeDaysAgo = Calendar.getInstance().apply {
//                add(Calendar.DAY_OF_YEAR, -3)
//            }.timeInMillis
//
//            val projection = arrayOf(
//                CallLog.Calls._ID,
//                CallLog.Calls.NUMBER,
//                CallLog.Calls.DATE,
//                CallLog.Calls.DURATION,
//                CallLog.Calls.TYPE,
//                CallLog.Calls.PHONE_ACCOUNT_ID
//            )
//
//            val selection = "${CallLog.Calls.DATE} >= ?"
//            val selectionArgs = arrayOf(threeDaysAgo.toString())
//
//            val cursor = contentResolver.query(
//                CallLog.Calls.CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                "${CallLog.Calls.DATE} DESC"
//            )
//
//            cursor?.use {
//                val recentCallsList = mutableListOf<ItemRecent>()
//                val idIndex = it.getColumnIndex(CallLog.Calls._ID)
//                val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
//                val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
//                val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
//                val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
//                val phoneAccountIdIndex = it.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)
//
//                while (it.moveToNext()) {
//                    val id = it.getString(idIndex)
//                    val number = it.getString(numberIndex)
//                    val date = it.getLong(dateIndex)
//                    val duration = it.getInt(durationIndex)
//                    val type = it.getInt(typeIndex)
//                    val phoneAccountId = it.getInt(phoneAccountIdIndex)
//
//
//                    // Determine which SIM was used (if multiple SIMs are available)
//                    var simIdentifier = "Unknown SIM"
////
////                    Log.e("Sim Account Id ", phoneAccountId.toString())
//                    simIdentifier = if (phoneAccountId == 1) {
//                        "SIM 1"
//                    } else (if (phoneAccountId == 2) {
//                        "SIM 2"
//                    } else {
//                        "Unknown SIM"
//                    }).toString()
//                    val itemRecent = ItemRecent(
//                        id = id,
//                        number = number,
//                        time = date,
//                        dur = duration.toLong(),
//                        type = type,
//                        simId = simIdentifier
//                    )
//                    recentCallsList.add(itemRecent)
//                }
//                _recentContactGroups.postValue(recentCallsList)
//            }
//
//        }
//    }

    fun getRecentCallsLast3Days(context: Context) {
        if (context != null && ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CALL_LOG
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {

            val accountIdToSimIDMap = HashMap<String, Int>()
            context.getAvailableSIMCardLabels().forEach {
                accountIdToSimIDMap[it.handle.id] = it.id
            }

//            Log.e("accountIdToSimIDMap  ",""+accountIdToSimIDMap)
            // Obtain the ContentResolver from the context
            val contentResolver = context.contentResolver

            val threeDaysAgo = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -3)
            }.timeInMillis

            val projection = arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID
            )

            val selection = "${CallLog.Calls.DATE} >= ?"
            val selectionArgs = arrayOf(threeDaysAgo.toString())

            val cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                "${CallLog.Calls.DATE} DESC"
            )



            cursor?.use {
                val recentCallsList = mutableListOf<ItemRecent>()
                val callGroups =
                    mutableMapOf<Pair<String, String>, CallGroupInfo>() // Key is Pair(number, simId)

                val idIndex = it.getColumnIndex(CallLog.Calls._ID)
                val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
                val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
                val phoneAccountIdIndex = it.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)

                while (it.moveToNext()) {
                    val id = it.getString(idIndex)
                    val number = it.getString(numberIndex)
                    val date = it.getLong(dateIndex)
                    val duration = it.getInt(durationIndex)
                    val type = it.getInt(typeIndex)
                    val phoneAccountId = it.getString(phoneAccountIdIndex) ?: "Unknown SIM"
                    val simID3 = accountIdToSimIDMap[phoneAccountId] ?: -1




//                    Log.e("Sim phone Id ",""+phoneAccountId)
//                    Log.e("simID3 ",""+simID3)
//                    Log.e("accountIdToSimIDMap  ",""+accountIdToSimIDMap)


                    // Determine which SIM was used (if multiple SIMs are available)
                    val simIdentifier = when (simID3) {
                        1 -> CommonUtils.sim1Text
                        2 -> CommonUtils.sim2Text
                        else -> CommonUtils.otherText
                    }

                    // Create a key for grouping by phone number and SIM card
                    val key = Pair(number, simIdentifier)

                    // Group the calls and store additional information
                    val currentGroup = callGroups.getOrDefault(key, CallGroupInfo())

                    // Store the first call type only (don't override after first assignment)
                    if (currentGroup.callType == 0) { // Assuming 0 is the default uninitialized value
                        currentGroup.callType = type
                    }

                    currentGroup.apply {
                        callCount += 1
                        totalDuration += duration
                        recentTime =
                            maxOf(recentTime, date) // Keep track of the most recent call time
                        // For simplicity, we take the latest type for that number/SIM combination
//                        callType = type
                    }
                    callGroups[key] = currentGroup
                }

                // Now, process the grouped calls and create the list of ItemRecent
                for ((key, groupInfo) in callGroups) {
                    val (number, simId) = key
                    val itemRecent = ItemRecent(
                        id = "",  // You may choose to handle the ID for each call here
                        number = number,
                        time = groupInfo.recentTime,  // Most recent call time
                        dur = groupInfo.totalDuration.toLong(),  // Total duration of all calls for that number/SIM
                        type = groupInfo.callType,  // Call type (you can improve logic for call type aggregation)
                        simId = simId,  // SIM identifier (SIM 1 or SIM 2)
                        totalCount = groupInfo.callCount,  // The count of calls for this number and SIM
                        name = CommonUtils.fetchNameFromNumberDetails(
                            context = context,
                            number
                        )  // The name of calls
                    )
                    recentCallsList.add(itemRecent)
                }

                // Post the final grouped list to your LiveData or other output mechanism
                _recentContactGroups.postValue(recentCallsList)
            }
        }
    }

//    fun getCallDetails(context: Context, phoneNumber: String?) {
//        if (phoneNumber.isNullOrEmpty()) return
//
//        val cursor: Cursor? = context.contentResolver.query(
//            CallLog.Calls.CONTENT_URI,
//            arrayOf(
//                CallLog.Calls._ID,
//                CallLog.Calls.DATE,
//                CallLog.Calls.DURATION,
//                CallLog.Calls.TYPE,
//            ),
//            "${CallLog.Calls.NUMBER}=?",
//            arrayOf(phoneNumber),
//            "${CallLog.Calls.DATE} DESC LIMIT 1" // Get latest call
//        )
//        cursor?.use {
//            if (it.moveToFirst()) {
//                val dateMillis = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
//                val durationSec = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
//
//                val id = it.getString(it.getColumnIndexOrThrow(CallLog.Calls._ID))
//                val type = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
//
//                val itemRecent = ItemRecent(
//                    id = id,  // You may choose to handle the ID for each call here
//                    number = phoneNumber,
//                    time = dateMillis,  // Most recent call time
//                    dur = durationSec.toLong(),  // Total duration of all calls for that number/SIM
//                    type = type,  // Call type (you can improve logic for call type aggregation)
//                    simId = CommonUtils.otherText,  // SIM identifier (SIM 1 or SIM 2)
//                    totalCount = 0,  // The count of calls for this number and SIM
//                    name = CommonUtils.fetchNameFromNumberDetails(
//                        context = context,
//                        phoneNumber
//                    )  // The name of calls
//                )
//
//                _recentContactItem.postValue(itemRecent)
//            }
//        }
//        cursor?.close()
//    }

    fun getCallDetails(context: Context, phoneNumber: String?) {
        if (phoneNumber.isNullOrEmpty()) return

        val cursor: Cursor? = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID // For SIM identification
            ),
            "${CallLog.Calls.NUMBER}=?",
            arrayOf(phoneNumber),
            "${CallLog.Calls.DATE} DESC" // Get latest call first
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val dateMillis = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val durationSec = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                val id = it.getString(it.getColumnIndexOrThrow(CallLog.Calls._ID))
                val type = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                val simId = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID))
                    ?: "Unknown SIM"

//                // Convert call type to a readable format
//                val callTypeString = when (type) {
//                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
//                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
//                    CallLog.Calls.MISSED_TYPE -> "Missed"
//                    CallLog.Calls.REJECTED_TYPE -> "Rejected"
//                    else -> "Unknown"
//                }

                val itemRecent = ItemRecent(
                    id = id,
                    number = phoneNumber,
                    time = dateMillis,
                    dur = durationSec.toLong(),
                    type = type,
                    simId = simId,  // Store SIM info
                    totalCount = 1,  // Currently fetching only the latest call
                    name = CommonUtils.fetchNameFromNumberDetails(context, phoneNumber)
                )
                _recentContactItem.postValue(itemRecent)
            }
        }
        cursor?.close()
    }


    // Helper data class to store information about each call group
    data class CallGroupInfo(
        var callCount: Int = 0,
        var totalDuration: Int = 0, // In seconds
        var recentTime: Long = 0,  // Timestamp of the most recent call
        var callType: Int = 0,      // Call type (Incoming, Outgoing, Missed)
        var callName: String = ""      // Name
    )


}
