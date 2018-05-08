package ru.taxcom.mobile.android.egais.domain.waybills

import io.reactivex.Observable
import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel
import ru.taxcom.mobile.android.egais.models.waybill.PagingModel

interface WayBillsInteractor {

    fun getWayBills (paging: PagingModel): Observable<DataPageModel>

}