package ru.taxcom.mobile.android.egais.di.scanner

import dagger.Binds
import dagger.Module
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractor
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractorImpl
import ru.taxcom.mobile.android.egais.presentation.scanner.ScannerActivity
import ru.taxcom.mobile.android.egais.presentation.scanner.presenter.ScannerPresenter
import ru.taxcom.mobile.android.egais.presentation.scanner.presenter.ScannerPresenterImpl
import ru.taxcom.mobile.android.egais.presentation.scanner.views.ScannerView
import ru.taxcom.mobile.android.egais.repositories.scanner.ScannerRepository
import ru.taxcom.mobile.android.egais.repositories.scanner.ScannerRepositoryImpl

@Module
abstract class ScannerModule {

    @Binds
    abstract fun bindScannerView(scannerActivity: ScannerActivity): ScannerView

    @Binds
    abstract fun bindScannerPresenter(scannerPresenterImpl: ScannerPresenterImpl): ScannerPresenter

    @Binds
    abstract fun bindScannerInteractor(scannerInteractorImpl: ScannerInteractorImpl): ScannerInteractor

    @Binds
    abstract fun bindScannerRepository(scannerRepositoryImpl: ScannerRepositoryImpl): ScannerRepository
}