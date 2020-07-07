package com.example.assignment.feed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignment.database.AssignmentDbContract
import com.example.assignment.feed.model.FeedInfoDto

/*
 * Interface provided to expose some method related to CRUD operations on database for [FeedInfoDto]
 */

@Dao
interface FeedDao {
    /**
     * insert all feeds to the database, replace entries on conflict
     *
     * @param feeds as [List] of [FeedInfoDto]s object
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllFeeds(feeds: List<FeedInfoDto?>): List<Long?>

    /**
     * Get list of [FeedInfoDto] from local db
     *
     * @return list of [FeedInfoDto]
     */
    @Query("SELECT * FROM ${AssignmentDbContract.FEEDS_TABLE_NAME}")
    fun getAllFeeds(): MutableList<FeedInfoDto?>?

    /**
     *  get count (no of rows) from the table [AssignmentDbContract.FEEDS_TABLE_NAME]
     *
     *  @return count: [Long] number of rows present in table
     */
    @Query("SELECT COUNT(${AssignmentDbContract.COLUMN_FEED_ID}) FROM ${AssignmentDbContract.FEEDS_TABLE_NAME}")
    fun getFeedsCount(): Long

    /**
     * Clear table [AssignmentDbContract.FEEDS_TABLE_NAME]
     */
    @Query("DELETE FROM ${AssignmentDbContract.FEEDS_TABLE_NAME}")
    fun clearFeedsTable()
}