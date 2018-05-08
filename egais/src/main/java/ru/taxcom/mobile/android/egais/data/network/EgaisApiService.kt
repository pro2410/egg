package ru.taxcom.mobile.android.egais.data.network

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.taxcom.mobile.android.egais.models.barcode.BarCodeScanModel
import ru.taxcom.mobile.android.egais.models.waybill.DataPageModel

interface EgaisApiService {

    @POST("barcodes/scan")
    fun scan(@Body barCodeScan: BarCodeScanModel): Completable

    @GET("waybills")
    fun waybills(@Query("paging.currentPage") loadingPage: Int,
                 @Query("paging.isDescOrder") isDescOrder: Boolean,
                 @Query("paging.orderField") orderField: Int,
                 @Query("paging.pageSize") pageSize: Int): Observable<DataPageModel>
}