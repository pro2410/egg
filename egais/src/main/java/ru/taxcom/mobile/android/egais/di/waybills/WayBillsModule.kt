package ru.taxcom.mobile.android.egais.di.waybills

import dagger.Binds
import dagger.Module
import ru.taxcom.mobile.android.egais.domain.waybills.WayBillsInteractor
import ru.taxcom.mobile.android.egais.domain.waybills.WayBillsInteractorImpl
import ru.taxcom.mobile.android.egais.presentation.waybills.presenter.WayBillsFragmentPresenter
import ru.taxcom.mobile.android.egais.presentation.waybills.presenter.WayBillsFragmentPresenterImpl
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsFragment
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsView
import ru.taxcom.mobile.android.egais.repositories.waybills.WayBillsRepository
import ru.taxcom.mobile.android.egais.repositories.waybills.WayBillsRepositoryImpl

@Module
abstract class WayBillsModule {

    @Binds
    abstract fun bindWayBillsView(wayBillsFragment: WayBillsFragment): WayBillsView

    @Binds
    abstract fun bindWayBillsFragmentPresenter(wayBillsFragmentPresenterImpl: WayBillsFragmentPresenterImpl): WayBillsFragmentPresenter

    @Binds
    abstract fun bindWayBillsInteractor (wayBillsInteractorImpl: WayBillsInteractorImpl): WayBillsInteractor

    @Binds
    abstract fun bindBillsRepository (wayBillsRepository: WayBillsRepositoryImpl): WayBillsRepository


}