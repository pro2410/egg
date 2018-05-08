package ru.taxcom.mobile.android.egais.di.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.taxcom.mobile.android.egais.di.waybills.WayBillsModule
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsFragment

@Module
abstract class EgaisFragmentBuilder {

    @ContributesAndroidInjector(modules = [WayBillsModule::class])
    abstract fun bindWayBillsFragment(): WayBillsFragment
}