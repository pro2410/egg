package ru.taxcom.mobile.android.egais.repositories.scanner

import io.reactivex.Completable

interface ScannerRepository {

    fun scan(data: String, docflowId: String): Completable
}