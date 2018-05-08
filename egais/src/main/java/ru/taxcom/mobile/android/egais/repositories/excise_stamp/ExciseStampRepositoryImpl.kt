package ru.taxcom.mobile.android.egais.repositories.excise_stamp

import io.realm.Realm
import ru.taxcom.mobile.android.egais.data.db.excise_stamp.ExciseStamp
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import javax.inject.Inject

class ExciseStampRepositoryImpl @Inject constructor(): ExciseStampRepository {

    override fun getExciseStamps(docflowId: String): List<ItemExciseStamp> {
        val realm = Realm.getDefaultInstance()
        val exciseStamps = realm.where(ExciseStamp::class.java).equalTo("docFlowId", docflowId).findAll()
        val list: List<ExciseStamp>  = realm.copyFromRealm(exciseStamps)
        val resultList: MutableList<ItemExciseStamp> = ArrayList()
        list.forEach{
            resultList.add(ItemExciseStamp(it.exciseStamp, it.dateTime))
        }
        realm.close()
        return resultList.toList()
    }

    override fun saveExciseStamp(data: String, docflowId: String, dateTime: String) {
        val realm = Realm.getDefaultInstance()
        val exciseStamp = ExciseStamp()
        exciseStamp.id = docflowId.plus("_").plus(data)
        exciseStamp.exciseStamp = data
        exciseStamp.dateTime = dateTime
        exciseStamp.docFlowId = docflowId
        realm.executeTransaction { it.insertOrUpdate(exciseStamp) }
        realm.close()
    }

    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.delete(ExciseStamp::class.java) }
        realm.close()
    }
}