@file:JvmName("ApiCallbackDsl")

package com.example.core2.api

import androidx.annotation.RestrictTo
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ## Method to retrieve your [API] service object to connection to API server using Retrofit client. ##
 *
 * Provide your base url of api end point using [baseUrl] string type in your project and handle callback of your
 * [Request.Builder] on [reqBuilderCallback] parameter.
 *
 * Uses [RetrofitSingleton] object class to build it's request on `Retrofit` object & returns your object of created API service [API] as return type.
 *
 * ### How to use this method: ###
 *
 * Take variable and call this method on assignment like below, also provide necessary objects and callback for it.
 *
 *     val apiService = provideApiService<MyApiEndPoint>(baseUrl) {
 *
 *     }
 *
 * @return [API] as object of your created service from `Retrofit`
 */
inline fun <reified API> provideApiService(
    baseUrl: String,
    noinline reqBuilderCallback: Request.Builder.() -> Unit
): API {
    return RetrofitSingleton.provideRetrofit(baseUrl, reqBuilderCallback = reqBuilderCallback)
        .create(API::class.java)
}

/**
 * Extension method of [Call] to create new API call from your API endpoint service object
 * and returns [CallbackImpl] object as handled callback object for DSL methods [success] & [failure]
 *
 * @return [CallbackImpl] of [T] to handle response of API call
 */
fun <T> Call<T?>?.enqueueOn(): CallbackImpl<T?>? {
    val callback = CallbackImpl<T?>()
    this?.enqueue(callback)
    return callback
}

/**
 * Extension Method if [CallbackImpl] to handle response if API call that was made is successfully executed
 * returns [CallbackImpl] as continuous DSL callback to handle other callbacks like Rail-way oriented programming
 *
 * @return [CallbackImpl] of [T] to handle other callbacks
 */
infix fun <T> CallbackImpl<T?>?.success(success: (Call<T?>, Response<T?>) -> Unit): CallbackImpl<T?>? {
    this?.onSuccess = success
    return this
}

/**
 * Extension Method if [CallbackImpl] to handle response if API call that was made failed executed
 * returns [CallbackImpl] as continuous DSL callback to handle other callbacks like Rail-way oriented programming
 *
 * @return [CallbackImpl] of [T] to handle other callbacks
 */
infix fun <T> CallbackImpl<T?>?.failure(failure: (Call<T?>, t: Throwable) -> Unit): CallbackImpl<T?>? {
    this?.onFailure = failure
    return this
}

/**
 * [CallbackImpl] :
 *
 * Custom class to handle [Callback] and provide responses separately
 *
 * @see [Callback]
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class CallbackImpl<T> : Callback<T?> {
    var onFailure: ((Call<T?>, t: Throwable) -> Unit)? = null
    var onSuccess: ((call: Call<T?>, response: Response<T?>) -> Unit)? = null

    override fun onFailure(call: Call<T?>, t: Throwable) {
        this.onFailure?.invoke(call, t)
    }

    override fun onResponse(call: Call<T?>, response: Response<T?>) {
        this.onSuccess?.invoke(call, response)
    }
}