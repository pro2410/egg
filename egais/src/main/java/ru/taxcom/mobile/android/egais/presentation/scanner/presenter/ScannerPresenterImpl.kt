package ru.taxcom.mobile.android.egais.presentation.scanner.presenter

import android.content.Context
import android.os.Handler
import android.support.v4.content.ContextCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractor
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import ru.taxcom.mobile.android.egais.presentation.scanner.views.ScannerView
import ru.taxcom.mobile.android.egais.repositories.excise_stamp.ExciseStampRepositoryImpl
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ScannerPresenterImpl @Inject constructor(
        private var scannerView: ScannerView,
        private var scannerInteractor: ScannerInteractor,
        private var scannerDbInteractor: ExciseStampRepositoryImpl,
        private var context: Context) : ScannerPresenter {

    private val itemsExciseStamp = ArrayList<ItemExciseStamp>()

    override fun scan(data: String, docflowId: String) {
        scannerView.showProgress()
        scannerInteractor.scan(data, docflowId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val dateTime = Date().time.toString()
                    scannerDbInteractor.saveExciseStamp(data, docflowId, dateTime)
                    handleSuccess(data, dateTime)
                }, ::handleError)
    }

    override fun closeScan() {
        when (itemsExciseStamp.size) {
            0 -> scannerView.closeScan()
            else -> scannerView.returnSuccessData(itemsExciseStamp)
        }
    }

    private fun handleSuccess(data: String, dateTime: String) {
        scannerView.hideProgress()
        showResult(
                false,
                context.getString(R.string.scanner_result_success_title),
                context.getString(R.string.scanner_result_success_text),
                ContextCompat.getColor(context, R.color.scanned_result_success)
        )
        val handler = Handler()
        handler.postDelayed({ itemsExciseStamp.add(ItemExciseStamp(data, dateTime)); scannerView.hideResultScreen() }, 2000L)
    }

    private fun handleError(throwable: Throwable) {
        scannerView.hideProgress()
        val color = ContextCompat.getColor(context, R.color.scanned_result_error)
        val title = context.getString(R.string.scanner_result_error_title)
        var message = context.getString(R.string.scanner_result_error_text)
        if (throwable is HttpException) {
            message = if (throwable.response().code() == 401) {
                context.getString(R.string.error_server_connection)
            } else {
                val responseBody: ResponseBody? = throwable.response().errorBody()
                val error = getErrorMessage(responseBody)
                error ?: context.getString(R.string.scanner_result_error_text)
            }
        } else if (throwable is UnknownHostException) {
            message = context.getString(R.string.error_network)
        }
        showResult(true, title, message, color)
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody?.string())
            jsonObject.getString("Message")
        } catch (e: Exception) {
            e.message
        }
    }

    private fun showResult(handVisible: Boolean, title: String, message: String, color: Int) {
        scannerView.showResultScreen(handVisible, title, message, color)
    }
}