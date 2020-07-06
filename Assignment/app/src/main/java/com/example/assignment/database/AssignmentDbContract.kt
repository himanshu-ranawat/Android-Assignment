package com.example.assignment.database

import com.example.assignment.feed.model.FeedInfoDto

/*
* Data contract class that provides constant data related to [AssignmentDbContract] & it's related entity models.
 */

class AssignmentDbContract {
    companion object {
        // Tag for logcat.
        const val TAG = "AssignmentDbContract"
        /**
         * defines database version for [AssignmentDb]
         */
        const val DB_VERSION = 1
        /**
         * defines new database version for [AssignmentDb] migration
         */
        const val NEW_DB_VERSION = 1
        /**
         * defines database name for [AssignmentDb]
         */
        const val DB_NAME = "AssignmentApp"

        /**
         * defines Feeds table name for [FeedInfoDto]
         */
        const val FEEDS_TABLE_NAME = "Feeds"

        /**
         * defines column name `id` for [FeedInfoDto]
         */
        const val COLUMN_FEED_ID = "id"
        /**
         * defines column name `title` for [FeedInfoDto]
         */
        const val COLUMN_FEED_TITLE = "title"
        /**
         * defines column name `description` for [FeedInfoDto]
         */
        const val COLUMN_FEED_DESCRIPTION = "description"
        /**
         * defines column name `image_url` for [FeedInfoDto]
         */
        const val COLUMN_FEED_URL = "image_url"
    }
}