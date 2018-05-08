package ru.taxcom.mobile.android.egais.presentation.waybill.views

import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp

interface WayBillView {
    fun fillScreen(dateTime: String, date: String, number: String, orgName: String)
    fun showExciseStampList(exciseStampList: List<ItemExciseStamp>)
    fun hideResultScreen()
    fun showResultScreen(title: String, message: String, color: Int)
    fun addExciseStamp(itemsExciseStamp: ItemExciseStamp)
    fun showFab(isPiTECH: Boolean)
    fun showSmartDroidTextHint(b: Boolean)
}