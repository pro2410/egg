package ru.taxcom.mobile.android.egais.models.barcode

import com.google.gson.annotations.SerializedName

class BarCodeScanModel(
        @SerializedName("Code") val code: String?,
        @SerializedName("DocflowId") val docflowId: String)
