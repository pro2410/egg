package ru.taxcom.mobile.android.egais.presentation.scanner.presenter

interface ScannerPresenter {

    fun scan(data: String, docflowId: String)
    fun closeScan()
}