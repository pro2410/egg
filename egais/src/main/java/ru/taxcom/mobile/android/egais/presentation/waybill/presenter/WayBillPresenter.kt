package ru.taxcom.mobile.android.egais.presentation.waybill.presenter

import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel

interface WayBillPresenter {
    fun shaping(wayBillModel: WayBillModel?)
    fun smartDroidScan(data: String, docflowId: String)
}