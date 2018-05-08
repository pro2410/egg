package ru.taxcom.mobile.android.egais.presentation.scanner.views

import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp

interface ScannerView {

    fun showProgress()
    fun hideProgress()
    fun returnSuccessData(items: ArrayList<ItemExciseStamp>)
    fun closeScan()
    fun showResultScreen(isHandVisible: Boolean, title: String, message: String, color: Int)
    fun hideResultScreen()
}