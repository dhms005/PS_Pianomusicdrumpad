package com.pianomusicdrumpad.pianokeyboard.callafterscreen.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ItemRecent(
    @SerializedName("dur")
    val dur: Long = 0,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("number")
    val number: String? = null,

    @SerializedName("simId")
    val simId: String? = null,

    @SerializedName("time")
    val time: Long = 0,

    @SerializedName("type")
    val type: Int = 0,

    @SerializedName("totalCount")
    val totalCount: Int = 0,

    @SerializedName("name")
    val name: String? = null

) : Parcelable {
    // Parcel constructor
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    // Write to parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dur)
        parcel.writeString(id)
        parcel.writeString(number)
        parcel.writeString(simId)
        parcel.writeLong(time)
        parcel.writeInt(type)
        parcel.writeInt(totalCount)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ItemRecent> = object : Parcelable.Creator<ItemRecent> {
            override fun createFromParcel(parcel: Parcel): ItemRecent {
                return ItemRecent(parcel)
            }

            override fun newArray(size: Int): Array<ItemRecent?> {
                return arrayOfNulls(size)
            }
        }
    }
}
