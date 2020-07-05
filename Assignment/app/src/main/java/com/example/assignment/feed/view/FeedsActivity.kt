package com.example.assignment.feed.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.assignment.R
import com.example.core2.AbstractBaseActivity
import com.example.core2.EventObserver
import com.example.core2.absBuilder
import com.example.core2.api.takeIfError
import com.example.core2.api.takeIfErrorMessage
import com.example.core2.api.takeIfNoConnection
import com.example.core2.isNetworkConnected
import com.google.android.material.snackbar.Snackbar

/*
 * [FeedsActivity] : Activity class that loads feeds from network/local database with the help of ViewModel
 * and binds it to UI with the help of binder class.
 *
 * Provides feeds whether from local database if available as cached else performs network call to fetch feeds
 *
 * Also handles screen rotation to maintain data across configuration changes.
 */

class FeedsActivity : AbstractBaseActivity() {
    companion object {
        const val EXTRA_FLAG_CALL_API = "makeApiCall"
    }

    /**
     * Lazy Binder class to handle data-binding
     */
    private val activityMainBinder by lazy {
        return@lazy FeedsActivityBinder()
    }
    /**
     * ViewModel class to handle UI Logic & Observable Data
     */
    private val feedsDetailViewModel by lazy {
        ViewModelProviders.of(this)[FeedsViewModel::class.java]
    }

    override fun setUpBuilder() = absBuilder {
        contentView = R.layout.activity_feed
        abstractBinding = activityMainBinder
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        title = getString(R.string.app_name)
        setupRefreshCallbacks()
        observeData()
        if (savedInstanceState == null) {
            getFeeds()
        } else {
            if (savedInstanceState.keySet().contains(EXTRA_FLAG_CALL_API)
                && savedInstanceState.getBoolean(EXTRA_FLAG_CALL_API, false)
            )
                getFeeds()
        }
    }

    /**
     * Method to handle swipe refresh callback
     */
    private fun setupRefreshCallbacks() {
        activityMainBinder.onRefreshCallback = {
            if (isNetworkConnected())
                feedsDetailViewModel.refreshFeeds()
            else {
                rootView.showSnackBar(getString(R.string.no_internet))
                activityMainBinder.showSwipeProgress(false)
            }
        }
    }

    /**
     * Requests ViewModel for latest list of feeds
     */
    private fun getFeeds() {
        feedsDetailViewModel.getFeeds()
    }

    /**
     * Method contains several live data observed to maintain UI state Up-to-date
     */
    private fun observeData() {
        feedsDetailViewModel.feedsLiveData.observe(this, Observer {
            activityMainBinder.showSwipeProgress(false)
            when (resources?.configuration?.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    activityMainBinder.setRecyclerLayoutManager(
                        StaggeredGridLayoutManager(
                            2,
                            StaggeredGridLayoutManager.VERTICAL
                        )
                    )
                }
                else -> {
                    activityMainBinder.setRecyclerLayoutManager(
                        LinearLayoutManager(
                            this,
                            RecyclerView.VERTICAL, false
                        )
                    )
                }
            }
            title = feedsDetailViewModel.getPageTitle()
            activityMainBinder.feedsAdapter?.addAllItems(it)
        })
        feedsDetailViewModel.errorLiveData.observe(this, EventObserver {
            activityMainBinder.showSwipeProgress(false)
            title = getString(R.string.failed_to_load)
            it takeIfError {
                activityMainBinder.feedsAdapter?.clear()
                tr.message?.let { msg -> rootView.showSnackBar(msg) }
            } takeIfNoConnection {
                activityMainBinder.feedsAdapter?.clear()
                rootView.showSnackBar(getString(R.string.no_internet))
            } takeIfErrorMessage {
                activityMainBinder.feedsAdapter?.clear()
                rootView.showSnackBar(getErrorMessage())
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_FLAG_CALL_API, false)
        super.onSaveInstanceState(outState)
    }

    /**
     * Extension to show `snackbar` based on [View] and displays [message] on the screen for Short period of time.
     *
     * @param message to be displayed on the screen as [String]
     */
    private fun View?.showSnackBar(message: String) {
        val snackBar = this?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackBar?.show()
    }
}
