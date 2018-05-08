package ru.taxcom.mobile.android.egais.presentation.waybill.presenter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.v4.content.ContextCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.domain.exciseStamp.ExciseStampInteractor
import ru.taxcom.mobile.android.egais.domain.scanner.ScannerInteractor
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import ru.taxcom.mobile.android.egais.presentation.waybill.views.WayBillView
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class WayBillPresenterImpl @Inject constructor(
        private var wayBillView: WayBillView,
        private var exciseStampInteractor: ExciseStampInteractor,
        private var scannerInteractor: ScannerInteractor,
        private var context: Context) : WayBillPresenter {

    private val TIME_PATTERN = "HH:mm"
    private val DATE_PATTERN = "EEE, yyyy.MM.dd"

    override fun shaping(wayBillModel: WayBillModel?) {
        val dateTime = formatDate(wayBillModel?.date, TIME_PATTERN)
        val date = firstUpperCase(formatDate(wayBillModel?.date, DATE_PATTERN))
        val number = wayBillModel?.number ?: "-"
        val orgName = wayBillModel?.orgNme ?: "-"
        var list: List<ItemExciseStamp> = ArrayList()
        if (wayBillModel?.docflowId != null) list = exciseStampInteractor.getExciseStamps(wayBillModel.docflowId.toString())
        wayBillView.showExciseStampList(list)
        wayBillView.fillScreen(dateTime, date, number, orgName)
        val isPiTECH = Build.BRAND == "PiTECH"
        wayBillView.showFab(isPiTECH)
        wayBillView.showSmartDroidTextHint(list.isEmpty() && isPiTECH)
    }

    private fun formatDate(dateString: String?, pattern: String): String {
        return when (dateString) {
            null -> "-"
            else -> {
                val dateLong = dateString.toLong()
                val format = SimpleDateFormat(pattern, Locale.getDefault())
                format.format(dateLong * 1000)
            }
        }
    }

    private fun firstUpperCase(string: String): String {
        return when {
            string.isEmpty() -> ""
            string.length > 1 -> string.substring(0, 1).toUpperCase().plus(string.substring(1))
            else -> string.toUpperCase()
        }
    }

    override fun smartDroidScan(data: String, docflowId: String) {
        scannerInteractor.scan(data, docflowId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val dateTime = Date().time.toString()
                    exciseStampInteractor.saveExciseStamp(data, docflowId, dateTime)
                    handleSuccess(data, dateTime)
                }, ::handleError)
    }

    private fun handleSuccess(data: String, dateTime: String) {
        showResult(
                context.getString(R.string.scanner_result_success_title),
                context.getString(R.string.scanner_result_success_text),
                ContextCompat.getColor(context, R.color.scanned_result_success)
        )
        val handler = Handler()
        handler.postDelayed({ wayBillView.hideResultScreen() }, 2000L)
        wayBillView.addExciseStamp(ItemExciseStamp(data, dateTime))
    }

    private fun handleError(throwable: Throwable) {
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
        showResult(title, message, color)
        val handler = Handler()
        handler.postDelayed({ wayBillView.hideResultScreen() }, 2000L)
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody?.string())
            jsonObject.getString("Message")
        } catch (e: Exception) {
            e.message
        }
    }

    private fun showResult(title: String, message: String, color: Int) {
        wayBillView.showResultScreen(title, message, color)
    }
}