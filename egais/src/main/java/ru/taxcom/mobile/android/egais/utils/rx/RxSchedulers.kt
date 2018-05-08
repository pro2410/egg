package ru.taxcom.mobile.android.egais.utils.rx

import io.reactivex.Scheduler

interface RxSchedulers {

    fun io(): Scheduler

    fun mainThread(): Scheduler

    fun computation(): Scheduler

    fun trampoline(): Scheduler

    fun newThread(): Scheduler

    fun single(): Scheduler
}