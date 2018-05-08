package ru.taxcom.mobile.android.egais.di.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.taxcom.mobile.android.egais.di.scanner.ScannerModule
import ru.taxcom.mobile.android.egais.di.waybill.WayBillModule
import ru.taxcom.mobile.android.egais.presentation.scanner.ScannerActivity
import ru.taxcom.mobile.android.egais.presentation.waybill.WayBillActivity

@Module
abstract class EgaisActivityBuilder {

    @ContributesAndroidInjector(modules = [WayBillModule::class])
    abstract fun bindWayBillActivity(): WayBillActivity

    @ContributesAndroidInjector(modules = [ScannerModule::class])
    abstract fun bindScannerActivity(): ScannerActivity
}