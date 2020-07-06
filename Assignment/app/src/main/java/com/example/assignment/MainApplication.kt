package com.example.assignment

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/*
 * [MainApplication] : Main Application class as entry point to this app, on create simply set context variable from companion object
 * that can be used further in app.
 */

class MainApplication : Application() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        @Volatile
        @JvmStatic
        private lateinit var context: MainApplication

        /**
         * Provides context or throw [UninitializedPropertyAccessException] if it's not initialized yet.
         */
        @Throws
        fun getContext(): Context =
            if (this::context.isInitialized) context
            else throw UninitializedPropertyAccessException("Context is null, did you forget to initialize it?")
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}
