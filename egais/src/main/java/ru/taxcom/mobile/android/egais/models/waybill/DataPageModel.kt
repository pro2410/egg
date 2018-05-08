package ru.taxcom.mobile.android.egais.models.waybill

import com.google.gson.annotations.SerializedName

data class DataPageModel(
        @SerializedName("Items") var items: List<WayBillModel>,
        @SerializedName("TotalItems") var totalItems: Int,
        @SerializedName("PageSize") var pageSize: Int,
        @SerializedName("CurrentPage") var currentPage: Int,
        @SerializedName("TotalPages") var totalPages: Int)