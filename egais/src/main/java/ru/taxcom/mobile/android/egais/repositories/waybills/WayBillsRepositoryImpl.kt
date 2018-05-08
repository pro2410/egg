package ru.taxcom.mobile.android.egais.repositories.waybills

import io.reactivex.Observable
import ru.taxcom.mobile.android.egais.data.network.EgaisApiService
import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel
import ru.taxcom.mobile.android.egais.models.waybill.PagingModel
import javax.inject.Inject

class WayBillsRepositoryImpl
@Inject constructor(private val api: EgaisApiService) : WayBillsRepository {

    override fun getWayBills(paging: PagingModel): Observable<DataPageModel> {
        return api.waybills(paging.loadingPage, paging.isDescOrder, paging.orderField, paging.pageSize)
    }
}