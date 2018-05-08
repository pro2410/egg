package ru.taxcom.mobile.android.egais.data.db.user

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class User : RealmObject() {

    @PrimaryKey
    var id: Int = 0
    var login: String? = null
    var password: String? = null
    var server: String? = null
    var token: String? = null
}