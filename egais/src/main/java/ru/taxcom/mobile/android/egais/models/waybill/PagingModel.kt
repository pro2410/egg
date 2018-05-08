package ru.taxcom.mobile.android.egais.models.waybill


data class PagingModel(

        var loadingPage: Int = 1,

        var isDescOrder: Boolean = true,

        var orderField: Int = 0,

        var pageSize: Int = 7 //DEFAULT_SIZE_PAGE
)