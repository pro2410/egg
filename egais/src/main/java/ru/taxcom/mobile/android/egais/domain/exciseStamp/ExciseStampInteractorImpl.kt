package ru.taxcom.mobile.android.egais.domain.exciseStamp

import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import ru.taxcom.mobile.android.egais.repositories.excise_stamp.ExciseStampRepository
import javax.inject.Inject

class ExciseStampInteractorImpl @Inject constructor(var exciseStampRepository: ExciseStampRepository) : ExciseStampInteractor {

    override fun getExciseStamps(docflowId: String): List<ItemExciseStamp> {
        return exciseStampRepository.getExciseStamps(docflowId)
    }

    override fun saveExciseStamp(data: String, docflowId: String, dateTime: String) {
        exciseStampRepository.saveExciseStamp(data, docflowId, dateTime)
    }

    override fun deleteAll() {
        exciseStampRepository.deleteAll()
    }
}