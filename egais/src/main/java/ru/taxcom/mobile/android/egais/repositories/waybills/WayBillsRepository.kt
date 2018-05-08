package ru.taxcom.mobile.android.egais.repositories.waybills

import io.reactivex.Observable
import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel
import ru.taxcom.mobile.android.egais.models.waybill.PagingModel

interface WayBillsRepository {

    fun getWayBills(paging: PagingModel): Observable<DataPageModel>
}