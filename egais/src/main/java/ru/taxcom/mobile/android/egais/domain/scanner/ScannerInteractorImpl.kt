package ru.taxcom.mobile.android.egais.domain.scanner

import io.reactivex.Completable
import ru.taxcom.mobile.android.egais.repositories.scanner.ScannerRepository
import javax.inject.Inject

class ScannerInteractorImpl @Inject constructor(private var scannerRepository: ScannerRepository) : ScannerInteractor {

    override fun scan(data: String, docflowId: String): Completable {
        return scannerRepository.scan(data, docflowId)
    }
}