package ru.taxcom.mobile.android.egais.domain.exciseStamp

import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp

interface ExciseStampInteractor {

    fun getExciseStamps(docflowId: String): List<ItemExciseStamp>

    fun saveExciseStamp(data: String, docflowId: String, dateTime: String)

    fun deleteAll()
}