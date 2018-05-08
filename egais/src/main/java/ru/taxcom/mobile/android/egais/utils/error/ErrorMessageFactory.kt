package ru.taxcom.mobile.android.egais.utils.error

import android.app.Application
import android.content.Context
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import ru.taxcom.mobile.android.egais.R
import java.net.UnknownHostException

class ErrorMessageFactory(application: Application){

    var context: Context = application.applicationContext

    fun getError(throwable: Throwable) = when {
        throwable is UnknownHostException ->
            context.getString(R.string.error_network)
        throwable is HttpException ->
            if (throwable.code() == 401) {
                context.getString(R.string.error_input)
            } else {
                val responseBody: ResponseBody? = throwable.response().errorBody()
                val error = getErrorMessage(responseBody)
                error ?: context.getString(R.string.error_server_connection)
            }
        else ->
            context.getString(R.string.error_server_connection)
    }!!

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody?.string())
            jsonObject.getString("CommonDescription")
        } catch (e: Exception) {
            e.message
        }
    }
}