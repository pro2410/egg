package ru.taxcom.mobile.android.egais.presentation.waybills.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.taxcom.mobile.android.egais.domain.waybills.WayBillsInteractor
import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel
import ru.taxcom.mobile.android.egais.models.waybill.PagingModel
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import ru.taxcom.mobile.android.egais.presentation.waybills.views.WayBillsView
import ru.taxcom.mobile.android.egais.utils.error.ErrorMessageFactory
import javax.inject.Inject

class WayBillsFragmentPresenterImpl @Inject constructor(
        private var wayBillsView: WayBillsView,
        private var interactor: WayBillsInteractor,
        private var errorMessageFactory: ErrorMessageFactory) :
        WayBillsFragmentPresenter {

    private var countPages: Int = 0
    private lateinit var pageModel: PagingModel
    private var loading: Boolean = false
    private var errorMode: Boolean = false

    override fun loadAndShowWayBills() {
        if (loading) return
        loading = true
        errorMode = false
        pageModel = PagingModel()
        wayBillsView.showRefresh()
        interactor.getWayBills(pageModel)
                .map(this::map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError)
    }

    override fun loadNextPage() {
        if (loading) return
        if (pageModel.loadingPage == countPages || errorMode) {
            return
        }
        loading = true
        wayBillsView.showNextPageProgress()

        ++pageModel.loadingPage
        interactor.getWayBills(pageModel)
                .map(this::map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlePageSuccess, this::handlePageError)
    }

    override fun loadNextPageRetry() {
        errorMode = false
        loadNextPage()
    }

    private fun handleSuccess(items: List<WayBillModel>) {
        loading = false
        wayBillsView.hideRefresh()
        wayBillsView.showList(items)
        wayBillsView.showEmptyView(items.isEmpty())
    }

    private fun handleError(throwable: Throwable) {
        wayBillsView.hideRefresh()
        wayBillsView.clearList()
        val error = errorMessageFactory.getError(throwable)
        wayBillsView.showEmptyView(false)
        wayBillsView.showErrorView(error)
        loading = false
    }

    private fun handlePageSuccess(items: List<WayBillModel>) {
        loading = false
        wayBillsView.hideNextPageProgress()
        wayBillsView.loadNextPage(items)
    }

    private fun handlePageError(t: Throwable) {
        wayBillsView.loadNextPageError()
        --pageModel.loadingPage
        loading = false
        errorMode = true
    }

    private fun map(dataPageModel: DataPageModel): List<WayBillModel> {
        countPages = dataPageModel.totalPages
        return dataPageModel.items
    }
}