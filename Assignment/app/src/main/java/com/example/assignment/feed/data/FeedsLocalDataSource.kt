package com.example.assignment.feed.data

import com.example.assignment.MainApplication
import com.example.assignment.database.AssignmentDb
import com.example.assignment.feed.model.FeedInfoDto
import com.example.core2.getDefaultPreference
import com.example.core2.getValue
import com.example.core2.putValue
import com.example.core2.runInBackground

/*
 * Local data source file handles communication between Repository and Database/SharedPreference by
 * providing business logic to it.
 */

class FeedsLocalDataSource private constructor() : FeedDao {
    object HOLDER {
        val instance = FeedsLocalDataSource()
    }

    private val database by lazy {
        return@lazy AssignmentDb.instance
    }

    companion object {
        const val PREF_KEY_TITLE = "title"

        /**
         * Provides Singleton instance of [FeedsLocalDataSource]
         */
        @JvmStatic
        fun getInstance() = HOLDER.instance
    }

    override fun getAllFeeds(): MutableList<FeedInfoDto?>? {
        return database?.feedDao?.runInBackground { getAllFeeds() }
    }

    override fun addAllFeeds(feeds: List<FeedInfoDto?>): List<Long?> {
        return database?.feedDao?.runInBackground {
            if (feeds.isNullOrEmpty().not()) {
                clearFeedsTable()
                addAllFeeds(feeds)
            } else emptyList()
        } ?: emptyList()
    }

    override fun clearFeedsTable() {
        database?.feedDao?.runInBackground { clearFeedsTable() }
    }

    override fun getFeedsCount(): Long {
        return database?.feedDao?.runInBackground { getFeedsCount() } ?: 0
    }

    /**
     * Store `title` to shared preference to set it on UI when local data requested
     */
    fun saveTitle(title: String?) {
        MainApplication.getContext().getDefaultPreference()
            .putValue(PREF_KEY_TITLE, title)
    }

    /**
     * Return title saved in SharedPreference
     *
     * @return [String] as title or empty if null
     */
    fun getTitle(): String {
        return MainApplication.getContext().getDefaultPreference().getValue(PREF_KEY_TITLE) ?: ""
    }
}
