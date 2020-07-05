package com.example.core2.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * [CoreInitProvider] :
 *
 * [ContentProvider] class that can be used to initiate library without providing or calling from external app
 * Content provider classes gets called when process initiates, so that you don't for your consumer for
 * initializing your library from application class every time, in-such way you can be independent from
 * `Application` class initialization.
 *
 * After [ContentProvider.onCreate] invocation, [ContextProvider] gets initiated to retrieve context from it.
 *
 * @see [ContextProvider]
 * @see [ContentProvider]
 */
class CoreInitProvider : ContentProvider() {
    companion object {
        // Tag for logcat.
        const val TAG = "CoreInitProvider"
    }

    override fun onCreate(): Boolean {
        ContextProvider.init(this.context as? Application)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw NotImplementedError("unimplemented")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        throw NotImplementedError("unimplemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw NotImplementedError("unimplemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw NotImplementedError("unimplemented")
    }

    override fun getType(uri: Uri): String? {
        throw NotImplementedError("unimplemented")
    }
}