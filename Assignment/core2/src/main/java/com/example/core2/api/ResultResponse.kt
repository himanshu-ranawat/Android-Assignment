package com.example.core2.api

import android.util.Log
import com.example.core2.isNetworkConnected
import com.example.core2.provider.ContextProvider
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * [ResultResponse] :
 *
 * ### Generic sealed class provides variations of response from API based on conditions like: ###
 *
 * 1. If success then [ResultResponse.Success]
 * 2. If error then [ResultResponse.Error]
 * 3. If error message then [ResultResponse.ErrorMessage]
 * 4. If no connection then [ResultResponse.NoConnection]
 *
 */
sealed class ResultResponse<out T : Any?> {
    /**
     * [Success] :
     *
     * Class that provides **success response** from API when response needs to denote like success.
     *
     * Provides [data] as [T] as success data
     *
     */
    data class Success<out T : Any?>(val data: T) : ResultResponse<T>()

    /**
     * [Error] :
     *
     * Class that provides **throwable** from API when any kind of error occurred from API call.
     *
     * Provides [tr] as [Throwable] as error data
     *
     */
    data class Error(val tr: Throwable) : ResultResponse<Nothing>()

    /**
     * [ErrorMessage] :
     *
     * Class that provides **Error message** from API when any kind of error occurred from API call
     * and provides it's error message
     *
     * Provides [errorBody] as [ResponseBody] to provide error data message
     *
     * See method #[ErrorMessage.getErrorMessage]
     *
     */
    data class ErrorMessage(val errorBody: ResponseBody?) : ResultResponse<Nothing>() {

        /**
         * Method converts [errorBody] to simple [String] error message
         *
         * @return [String] error message
         */
        fun getErrorMessage(): String {
            return if (errorBody == null)
                ""
            else {
                try {
                    JSONObject(errorBody.string()).getString("Message")
                        .replace("\\[\"".toRegex(), "")
                        .replace("\"]".toRegex(), "")
                } catch (e: JSONException) {
                    Log.e("Error", e.message)
                    ""
                } catch (e: IOException) {
                    Log.e("Error", e.message)
                    ""
                }
            }
        }
    }

    /**
     * [NoConnection] :
     *
     * Class that provides **response** from API when there's no internet connection.
     *
     * Provides [msg] of [String] as no connection string
     *
     */
    data class NoConnection(val msg: String = "") : ResultResponse<Nothing>()

    /**
     * [HandledException] :
     *
     * Class that provides **response** from API to handle scenarios
     * like SocketTimeout or Other Connection related exceptions.
     *
     * Provides [tr] as [Throwable] based on exception caused
     *
     */
    data class HandledException(val tr: Throwable?) : ResultResponse<Nothing>() {
        fun filterResponses(): ResultResponse<Nothing> {
            return tr?.let {
                when (tr.cause) {
                    is ConnectException, is SSLHandshakeException, is SocketTimeoutException, is HttpException, is IOException -> {
                        if (ContextProvider.getInstance().context.isNetworkConnected())
                            Error(tr)
                        else
                            NoConnection()
                    }
                    else -> {
                        Error(tr)
                    }
                }
            } ?: NoConnection()
        }
    }
}