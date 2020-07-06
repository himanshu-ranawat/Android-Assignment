package com.example.assignment.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment.MainApplication
import com.example.assignment.feed.data.FeedDao
import com.example.assignment.feed.model.FeedInfoDto

/*
 * Main Database class defined to provide and store data using [FeedDao] for [RoomDatabase] as ORM help for this project.
 */
@Database(
    entities = [FeedInfoDto::class],
    version = AssignmentDbContract.DB_VERSION
)
abstract class AssignmentDb : RoomDatabase() {
    private object Holder {
        val INSTANCE = with(MainApplication.getContext()) {
            Room.databaseBuilder(
                this,
                AssignmentDb::class.java,
                AssignmentDbContract.DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

        }
    }

    companion object {
        /**
         * Singleton instance of [AssignmentDb]
         */
        val instance: AssignmentDb? by lazy { Holder.INSTANCE }

    }

    /**
     * Main Feeds DataAccessObject to handle local db connections using interface APIs
     */
    abstract val feedDao: FeedDao
}