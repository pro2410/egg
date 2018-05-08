package ru.taxcom.mobile.android.egais.repositories.scanner

import io.reactivex.Completable
import ru.taxcom.mobile.android.egais.data.network.EgaisApiService
import ru.taxcom.mobile.android.egais.models.barcode.BarCodeScanModel
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(private val api: EgaisApiService) : ScannerRepository {

    override fun scan(data: String, docflowId: String): Completable {
        val barCodeScanModel = BarCodeScanModel(data, docflowId)
        return api.scan(barCodeScanModel)
    }
}