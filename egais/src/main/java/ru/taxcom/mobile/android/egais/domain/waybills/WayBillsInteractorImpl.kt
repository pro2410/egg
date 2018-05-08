package ru.taxcom.mobile.android.egais.domain.waybills

import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel
import ru.taxcom.mobile.android.egais.models.waybill.PagingModel
import io.reactivex.Observable
import ru.taxcom.mobile.android.egais.repositories.waybills.WayBillsRepository
import javax.inject.Inject

class WayBillsInteractorImpl @Inject constructor(private val repository: WayBillsRepository) : WayBillsInteractor {

    override fun getWayBills(paging: PagingModel): Observable<DataPageModel> {
        return repository.getWayBills(paging)
    }
}