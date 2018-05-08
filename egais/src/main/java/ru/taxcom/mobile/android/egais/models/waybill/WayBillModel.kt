package ru.taxcom.mobile.android.egais.models.waybill

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsItemType
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsListItem

data class WayBillModel constructor(
        @SerializedName("DocumentId") val documentId: String?,
        @SerializedName("DocflowId") val docflowId: String?,
        @SerializedName("Number") val number: String?,
        @SerializedName("Date") val date: String?,
        @SerializedName("OrganizationName") val orgNme: String?) : Parcelable, WayBillsListItem {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(documentId)
        dest.writeString(docflowId)
        dest.writeString(number)
        dest.writeString(date)
        dest.writeString(orgNme)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WayBillModel> {
        override fun createFromParcel(parcel: Parcel): WayBillModel {
            return WayBillModel(parcel)
        }

        override fun newArray(size: Int): Array<WayBillModel?> {
            return arrayOfNulls(size)
        }
    }

    override val itemType: WayBillsItemType
        get() = WayBillsItemType.DATA
}