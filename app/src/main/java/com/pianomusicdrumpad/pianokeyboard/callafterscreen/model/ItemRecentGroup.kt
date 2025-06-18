package com.pianomusicdrumpad.pianokeyboard.callafterscreen.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ItemRecentGroup(
    @SerializedName("arrRecent")
    val arrRecent: ArrayList<ItemRecent> = ArrayList(),

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("time")
    var time: Long = 0
) : Parcelable {

    // Add a recent item and update the time
    fun addRecent(itemRecent: ItemRecent) {
        arrRecent.add(itemRecent)
        time = maxOf(time, itemRecent.time)
    }

    // Parcelable constructor
    constructor(parcel: Parcel) : this(
        arrRecent = parcel.createTypedArrayList(ItemRecent.CREATOR) ?: ArrayList(),
        name = parcel.readString(),
        time = parcel.readLong()
    )

    // Write to Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(arrRecent)
        parcel.writeString(name)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ItemRecentGroup> = object : Parcelable.Creator<ItemRecentGroup> {
            override fun createFromParcel(parcel: Parcel): ItemRecentGroup {
                return ItemRecentGroup(parcel)
            }

            override fun newArray(size: Int): Array<ItemRecentGroup?> {
                return arrayOfNulls(size)
            }
        }
    }
}
