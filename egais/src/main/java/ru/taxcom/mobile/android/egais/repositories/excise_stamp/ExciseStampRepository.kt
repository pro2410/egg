package ru.taxcom.mobile.android.egais.repositories.excise_stamp

import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp

interface ExciseStampRepository {

    fun getExciseStamps(docflowId: String): List<ItemExciseStamp>

    fun saveExciseStamp(data: String, docflowId: String, dateTime: String)

    fun deleteAll()
}