package ru.taxcom.mobile.android.egais.models.excise_stamp

import android.os.Parcel
import android.os.Parcelable

class ItemExciseStamp(var code: String,
                      var date: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(code)
        writeString(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ItemExciseStamp> = object : Parcelable.Creator<ItemExciseStamp> {
            override fun createFromParcel(source: Parcel): ItemExciseStamp = ItemExciseStamp(source)
            override fun newArray(size: Int): Array<ItemExciseStamp?> = arrayOfNulls(size)
        }
    }
}

