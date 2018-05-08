package ru.taxcom.mobile.android.egais.data.db.excise_stamp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ExciseStamp : RealmObject() {

    @PrimaryKey
    var id: String = ""
    var exciseStamp: String = ""
    var docFlowId: String = ""
    var dateTime: String = ""
}