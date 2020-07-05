package com.example.assignment.feed.data

import com.example.assignment.BuildConfig
import com.example.assignment.MainApplication
import com.example.assignment.api.FeedsApi
import com.example.assignment.feed.model.FeedListDto
import com.example.core2.api.*
import com.example.core2.isNetworkConnected
/*
 * Remote connection provider for feeds repository, basically performs network API call asynchronously
 * and provides list of feeds as callback result, see method [FeedsRemoteDataSource.getFeeds]
 */
class FeedsRemoteDataSource {
    object HOLDER {
        val instance = FeedsRemoteDataSource()
    }

    companion object {
        /**
         * Provides Singleton object [FeedsRemoteDataSource]
         */
        @JvmStatic
        fun getInstance() = HOLDER.instance
    }

    /**
     * Provides API connection object as [FeedsApi] for Retrofit setup
     *
     * @see provideApiService
     */
    private val mApiService by lazy {
        return@lazy provideApiService<FeedsApi?>(BuildConfig.BASE_URL) {}
    }

    /**
     * Get list of feeds from API end asynchronously and provides result to [onFeedsCallback] as
     * lambda method parameter
     *
     * @param onFeedsCallback receives result from network async and forward it to observer.
     */
    fun getFeeds(onFeedsCallback: (ResultResponse<FeedListDto?>?) -> Unit) {
        if (MainApplication.getContext().isNetworkConnected()) {
            mApiService?.getFeedsDetails().enqueueOn().success { _, response ->
                when {
                    response.isSuccessful && response.code() == 200 -> {
                        onFeedsCallback(ResultResponse.Success(response.body()))
                    }
                    else -> {
                        onFeedsCallback(ResultResponse.ErrorMessage(response.errorBody()))
                    }
                }
            } failure { _, t ->
                onFeedsCallback(ResultResponse.Error(t))
            }
        } else
            onFeedsCallback(ResultResponse.NoConnection())
    }
}