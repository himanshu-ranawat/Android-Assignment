package com.example.assignment.feed.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment.feed.model.FeedInfoDto
import com.example.core2.Event
import com.example.core2.api.*

/*
 * Repository class used to provide connection to API end-points or Database for feeds
 */

class FeedsRepository private constructor(
    val feedsRemoteDataSource: FeedsRemoteDataSource,
    val feedsLocalDataSource: FeedsLocalDataSource
) {
    object HOLDER {
        val instance = FeedsRepository(
            feedsRemoteDataSource = FeedsRemoteDataSource.getInstance(),
            feedsLocalDataSource = FeedsLocalDataSource.getInstance()
        )
    }

    companion object {
        /**
         * Provides Singleton object of [FeedsRepository]
         */
        @JvmStatic
        fun getInstance() = HOLDER.instance
    }

    /**
     * [LiveData] to respond single event error based on type of [ResultResponse]
     */
    val errorLiveData: LiveData<Event<ResultResponse<MutableList<FeedInfoDto?>?>>> =
        MutableLiveData()
    private val feedsLiveData: LiveData<MutableList<FeedInfoDto?>?> = MutableLiveData()

    /**
     * Clear table `Feeds` from local db
     */
    fun clearAllLocalFeeds() {
        feedsLocalDataSource.clearFeedsTable()
    }

    /**
     * Decides whether data should be fetched from Network/Locally,
     * @return [LiveData] of wrapper [MutableList] of [FeedInfoDto] to be observed
     */
    fun getFeeds(): LiveData<MutableList<FeedInfoDto?>?> {
        if (feedsLocalDataSource.getFeedsCount() == 0L) {
            feedsRemoteDataSource.getFeeds { response ->
                response takeIfSuccess {
                    data?.feedTitle?.let { feedsLocalDataSource.saveTitle(it) }
                    data?.feedsList?.let { feedsLocalDataSource.addAllFeeds(it) }
                    (feedsLiveData as? MutableLiveData)?.postValue(data?.feedsList)
                } takeIfError {
                    (errorLiveData as? MutableLiveData)?.postValue(Event(this))
                } takeIfNoConnection {
                    (errorLiveData as? MutableLiveData)?.postValue(Event(this))
                } takeIfErrorMessage {
                    (errorLiveData as? MutableLiveData)?.postValue(Event(this))
                }
            }
        } else {
            (feedsLiveData as? MutableLiveData)?.postValue(feedsLocalDataSource.getAllFeeds())
        }
        return feedsLiveData
    }

    /**
     * Get title from local data source
     *
     * @return [String] as title
     */
    fun getTitle(): String = feedsLocalDataSource.getTitle()
}