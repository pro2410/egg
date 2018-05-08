package ru.taxcom.mobile.android.egais.domain.scanner

import io.reactivex.Completable

interface ScannerInteractor {
    fun scan(data: String, docflowId: String): Completable
}