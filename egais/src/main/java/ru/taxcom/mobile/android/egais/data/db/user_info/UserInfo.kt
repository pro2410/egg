package ru.taxcom.mobile.android.egais.data.db.user_info

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserInfo : RealmObject() {

    @PrimaryKey
    var id: Int = 0
    var name: String? = null
    var inn: String? = null
    var kpp: String? = null
    var fsrar: String? = null
}