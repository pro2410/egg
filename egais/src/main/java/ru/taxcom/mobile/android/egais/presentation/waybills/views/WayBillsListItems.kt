package ru.taxcom.mobile.android.egais.presentation.waybills.views

interface WayBillsListItem {
    val itemType: WayBillsItemType
}

enum class WayBillsItemType { PROGRESS, DATA, ERROR }

class WayBillsItemProgress : WayBillsListItem {
    override val itemType: WayBillsItemType
        get() = WayBillsItemType.PROGRESS
}

class WayBillsItemError : WayBillsListItem {
    override val itemType: WayBillsItemType
        get() = WayBillsItemType.ERROR
}
