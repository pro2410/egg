package ru.taxcom.mobile.android.egais.presentation.waybills.views

import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel

interface WayBillsView {

    fun showList(list: List<WayBillModel>)

    fun showEmptyView(visible: Boolean)

    fun showErrorView(textError: String?)

    fun hideRefresh()

    fun showRefresh()

    fun clearList()

    fun loadNextPage(newPageBillsList: List<WayBillModel>)

    fun loadNextPageError()

    fun showNextPageProgress()

    fun hideNextPageProgress()

    fun onLoadNextPageRetry()
}