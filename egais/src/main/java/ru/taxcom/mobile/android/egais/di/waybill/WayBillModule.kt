package ru.taxcom.mobile.android.egais.di.waybill

import dagger.Binds
import dagger.Module
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractor
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractorImpl
import ru.taxcom.mobile.android.egais.presentation.waybill.WayBillActivity
import ru.taxcom.mobile.android.egais.presentation.waybill.presenter.WayBillPresenter
import ru.taxcom.mobile.android.egais.presentation.waybill.presenter.WayBillPresenterImpl
import ru.taxcom.mobile.android.egais.presentation.waybill.views.WayBillView
import ru.taxcom.mobile.android.egais.repositories.scanner.ScannerRepository
import ru.taxcom.mobile.android.egais.repositories.scanner.ScannerRepositoryImpl

@Module
abstract class WayBillModule {

    @Binds
    abstract fun bindWayBillView(wayBillActivity: WayBillActivity): WayBillView

    @Binds
    abstract fun bindWayBillPresenter(wayBillPresenterImpl: WayBillPresenterImpl): WayBillPresenter

    @Binds
    abstract fun bindScannerInteractor(scannerInteractorImpl: ScannerInteractorImpl): ScannerInteractor

    @Binds
    abstract fun bindScannerRepository(scannerRepositoryImpl: ScannerRepositoryImpl): ScannerRepository
}