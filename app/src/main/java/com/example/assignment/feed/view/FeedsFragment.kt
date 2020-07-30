package com.example.assignment.feed.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core2.isNetworkConnected
import com.example.assignment.R
import com.example.core2.*
import com.example.core2.api.takeIfError
import com.example.core2.api.takeIfErrorMessage
import com.example.core2.api.takeIfNoConnection
import com.google.android.material.snackbar.Snackbar

class FeedsFragment : AbstractBaseFragment() {

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
        contentView = R.layout.feeds_fragment
        abstractBinding = activityMainBinder
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_FLAG_CALL_API, false)
        super.onSaveInstanceState(outState)
    }

    /**
     * Method to handle swipe refresh callback
     */
    private fun setupRefreshCallbacks() {
        activityMainBinder.onRefreshCallback = {
            if (requireContext().isNetworkConnected())
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
        feedsDetailViewModel.feedsLiveData.observe(viewLifecycleOwner, Observer {
            activityMainBinder.showSwipeProgress(false)
            activityMainBinder.setRecyclerLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL, false
                )
            )
            activity?.title = feedsDetailViewModel.getPageTitle()
            activityMainBinder.feedsAdapter?.addAllItems(it)
        })
        feedsDetailViewModel.errorLiveData.observe(viewLifecycleOwner, EventObserver {
            activityMainBinder.showSwipeProgress(false)
            activity?.title = getString(R.string.failed_to_load)
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
